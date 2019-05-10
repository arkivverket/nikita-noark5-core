package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.DocumentDescription;
import nikita.common.model.noark5.v4.DocumentObject;
import nikita.common.model.noark5.v4.secondary.Conversion;
import nikita.common.repository.n5v4.IDocumentObjectRepository;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.common.util.exceptions.StorageException;
import nikita.common.util.exceptions.StorageFileNotFoundException;
import nikita.webapp.config.WebappProperties;
import nikita.webapp.service.interfaces.IDocumentObjectService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.ExceptionDetailsConstants.MISSING_DOCUMENT_DESCRIPTION_ERROR;
import static nikita.common.config.FileConstants.FILE_EXTENSION_PDF;
import static nikita.common.config.FileConstants.MIME_TYPE_PDF;
import static nikita.common.config.FormatDetailsConstants.FORMAT_PDF_DETAILS;
import static nikita.common.config.N5ResourceMappings.ARCHIVE_VERSION;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_OBJECT_FILE_NAME;
import static nikita.common.util.CommonUtils.FileUtils.mimeTypeIsConvertible;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;

/**
 * The following methods
 * - convertFileFromStorage
 * have been based on code from
 * - https://api.libreoffice.org/examples/java/DocumentHandling/
 * <p>
 * Note: document filenames are stored without reference to the root of the file
 * system. When retrieving a Path to a file associated with a documentObject
 * you have to add the diretoryStoreName in front of the filename
 * See: getDocumentFile(documentObject); how this is done
 */

@Service
@Transactional
@EnableConfigurationProperties(WebappProperties.class)
public class DocumentObjectService
        extends NoarkService
        implements IDocumentObjectService {

    private final Logger logger =
            LoggerFactory.getLogger(DocumentObjectService.class);

    private IDocumentObjectRepository documentObjectRepository;
    @Value("${nikita.startup.directory-store-name}")
    private String directoryStoreName = "/data/nikita/storage";
    @Value("${nikita.startup.incoming-directory}")
    private String incomingDirectoryName = "/data/nikita/storage/incoming";
    @Value("${nikita.application.checksum-algorithm}")
    private String defaultChecksumAlgorithm = "SHA-256";

    public DocumentObjectService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IDocumentObjectRepository documentObjectRepository) {
        super(entityManager, applicationEventPublisher);
        this.documentObjectRepository = documentObjectRepository;
    }

    // All CREATE operations

    public DocumentObject save(DocumentObject documentObject) {
        setSystemIdEntityValues(documentObject);
        setCreateEntityValues(documentObject);
        setNikitaEntityValues(documentObject);

        Long version =
                documentObjectRepository.
                        countByReferenceDocumentDescriptionAndVariantFormat(
                                documentObject.getReferenceDocumentDescription(),
                                documentObject.getVariantFormat());
        // + 1 because while arrays start at 0, document counts start at 1
        documentObject.setVersionNumber(version.intValue() + 1);

        checkChecksumAlgorithmSetIfNull(documentObject);
        return documentObjectRepository.save(documentObject);
    }

    /**
     * Store an incoming file associated with a DocumentObject. When writing the
     * Incoming filestream, calculate the checksum at the same time and
     * update the DocumentObject with referenceToFile, size (bytes), checksum
     * and checksum algorithm
     * <p>
     * inputStream.read calculates the checksum while reading the input file
     * as it is a DigestInputStream
     * <p>
     * The document is first stored in rootLocation/incoming. Once additional
     * processing e.g. checksum generation is finished, the file is moved to
     * its proper location.
     * <p>
     * Note: You can't overwrite an existing documentObject. If you are updating
     * the document, you need to create a new version attached to the
     * documentDescription.
     */
    @Override
    public void storeAndCalculateChecksum(InputStream inputStream,
                                          DocumentObject documentObject) {

        if (null != documentObject.getReferenceDocumentFile()) {
            throw new StorageException(
                    "There is already a file associated with " +
                            documentObject);
        }
        try {

            Path incoming = createIncomingFile(documentObject);
            copyDocumentContentsToIncomingAndSetValues(inputStream, incoming,
                    documentObject);

            setGeneratedDocumentFilename(documentObject);

            String mimeType = getMimeType(incoming);
            if (!mimeType.equals(documentObject.getMimeType())) {
                logger.warn("Overriding mime-type for documentObject [" +
                        documentObject.toString() + "]. Original was [" +
                        documentObject.getMimeType() + "]. Setting to [" +
                        mimeType);
            }
            documentObject.setMimeType(mimeType);

            documentObject.setFileSize(Files.size(incoming));
            moveIncomingToStorage(incoming, documentObject);

            // Try to convert the file upon upload. Silently ignore
            // if there is a problem
            if (supportForDocumentConversion(documentObject)) {
                convertDocumentToPDF(documentObject);
            }
            documentObjectRepository.save(documentObject);
        } catch (IOException e) {
            String msg = "When associating an uploaded file with " +
                    documentObject + " the following Exception occurred " +
                    e.toString();
            logger.error(msg);
            throw new StorageException(msg);
        }
    }

    @Override
    public List<DocumentObject> findDocumentObjectByOwner() {

        String loggedInUser = SecurityContextHolder.getContext().
                getAuthentication().getName();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DocumentObject> criteriaQuery = criteriaBuilder.
                createQuery(DocumentObject.class);
        Root<DocumentObject> from = criteriaQuery.from(DocumentObject.class);
        CriteriaQuery<DocumentObject> select = criteriaQuery.select(from);

        criteriaQuery.where(criteriaBuilder.equal(from.get("ownedBy"),
                loggedInUser));
        TypedQuery<DocumentObject> typedQuery = entityManager.createQuery(select);
        return typedQuery.getResultList();
    }


    private void setGeneratedDocumentFilename(DocumentObject documentObject) {

        String extension = FilenameUtils.getExtension(
                documentObject.getOriginalFilename());
        String documentFilename = documentObject.getSystemId();
        if (extension != null) {
            documentFilename += "." + extension;
        }
        documentObject.setReferenceDocumentFile(documentFilename);
    }

    protected Path convertFileFromStorage(
            DocumentObject productionDocumentObject,
            DocumentObject archiveDocumentObject
    )
            throws IOException, InterruptedException {

        Path productionVersion = getToFile(productionDocumentObject);

        // set the filename to the same as the original filename minus
        // the extension. This needs to be a bit more advanced, an own
        // method. Consider the scenario where a file called .htaccess
        // is uploaded, or a file with no file extension

        archiveDocumentObject.setFormat(FILE_EXTENSION_PDF);
        archiveDocumentObject.setMimeType(MIME_TYPE_PDF);
        archiveDocumentObject.setFormatDetails(FORMAT_PDF_DETAILS);
        archiveDocumentObject.setVariantFormat(ARCHIVE_VERSION);

        setFilenameAndExtensionForArchiveDocument(
                productionDocumentObject, archiveDocumentObject);

        Path archiveVersion = createIncomingFile(archiveDocumentObject);

        String command = "unoconv ";
        String toFormat = " -f pdf ";
        String fromFileLocation = productionVersion.
                toAbsolutePath().toString();

        String toFileLocation = " -o " +
                archiveVersion.toAbsolutePath().toString();

        String convertCommand = command + toFormat + toFileLocation + " " +
                fromFileLocation;

        try {
            Process p = Runtime.getRuntime().exec(convertCommand);
            p.waitFor();
        } catch (RuntimeException e) {
            logger.error("Error converting document in " +
                    productionDocumentObject + " to archive format");
            logger.error(e.toString());
        }

        // Even though this was set earlier, we set it to the actual
        // detected mime-type
        archiveDocumentObject.setMimeType(getMimeType(archiveVersion));

        archiveDocumentObject.setFileSize(Files.size(archiveVersion));
        setGeneratedDocumentFilename(archiveDocumentObject);
        archiveDocumentObject.setDeleted(false);
        archiveDocumentObject.setOwnedBy(productionDocumentObject.getOwnedBy());

        return archiveVersion;
    }


    private void setFilenameAndExtensionForArchiveDocument(
            DocumentObject productionDocumentObject,
            DocumentObject archiveDocumentObject) {

        String originalFilename = FilenameUtils.
                removeExtension(productionDocumentObject.
                        getOriginalFilename());

        if (originalFilename == null) {
            originalFilename = archiveDocumentObject.getSystemId() + "." +
                    getArchiveFileExtension(archiveDocumentObject);
        } else {
            originalFilename += "." +
                    getArchiveFileExtension(archiveDocumentObject);
        }

        archiveDocumentObject.setOriginalFilename(originalFilename);
    }

    /**
     * @param documentObject the documentObject you want a archive version
     *                       mimeType for.
     * @return the documents mimeType
     */

    private String getArchiveFileExtension(DocumentObject documentObject) {
        return CommonUtils.FileUtils.getArchiveFileExtension(
                documentObject.getMimeType());
    }

    public DocumentObject convertDocumentToPDF(DocumentObject
                                                       originalDocumentObject) {
        DocumentObject archiveDocumentObject = new DocumentObject();

        try {

            archiveDocumentObject.setSystemId(UUID.randomUUID().toString());

            Path archiveFile = convertFileFromStorage(originalDocumentObject,
                    archiveDocumentObject);

            // Parent document description
            DocumentDescription documentDescription = originalDocumentObject
                    .getReferenceDocumentDescription();

            // If it's null, throw an exception. You can't create a
            // related documentObject unless it has a document description
            if (documentDescription == null) {
                throw new NoarkEntityNotFoundException(
                        MISSING_DOCUMENT_DESCRIPTION_ERROR);
            }

            // TODO: Double check this. Standard says it's only applicable to
            // archive version, but we increment every time a new document is
            // uploaded to a document description
            archiveDocumentObject.setVersionNumber(
                    originalDocumentObject.getVersionNumber());


            // Set creation details. Logged in user is responsible
            String username = SecurityContextHolder.getContext().
                    getAuthentication().getName();
            archiveDocumentObject.setCreatedBy(username);
            archiveDocumentObject.setCreatedDate(ZonedDateTime.now());

            // Handle the conversion details
            Conversion conversion = new Conversion();
            // perhaps here capture unoconv --version
            conversion.setConversionTool("LibreOffice via uconov ");
            conversion.setConvertedBy(username);
            conversion.setConvertedDate(ZonedDateTime.now());
            conversion.setConvertedFromFormat(
                    originalDocumentObject.getFormat());
            conversion.setConvertedToFormat(archiveDocumentObject.getFormat());
            conversion.setReferenceDocumentObject(archiveDocumentObject);
            archiveDocumentObject.addReferenceConversion(conversion);

            // Tie the new document object and document description together
            archiveDocumentObject.setReferenceDocumentDescription
                    (documentDescription);
            documentDescription.addReferenceDocumentObject(archiveDocumentObject);

            archiveDocumentObject.setChecksum(
                    new DigestUtils(defaultChecksumAlgorithm).
                            digestAsHex(archiveFile.toFile()));
            archiveDocumentObject.setChecksumAlgorithm(
                    defaultChecksumAlgorithm);

            if (archiveDocumentObject.getFileSize() > 0) {
                moveIncomingToStorage(archiveFile, archiveDocumentObject);
                documentObjectRepository.save(archiveDocumentObject);
                return archiveDocumentObject;
            } else {
                logger.error("File size of archive version is not > 0. Not " +
                        "persisting this documentDescription to the database.");
            }
            return null;

        } catch (InterruptedException | IOException e) {
            logger.error("Problem when trying to convert to archive format"
                    + e.toString());
        }
        return archiveDocumentObject;
    }


    @Override
    public Resource loadAsResource(DocumentObject documentObject) {
        String filename = documentObject.getReferenceDocumentFile();
        try {

            Path file = getToFile(documentObject);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(
                    "Could not read file: " + filename);
        }
    }

    // id
    public Optional<DocumentObject> findById(Long id) {
        return documentObjectRepository.findById(id);
    }

    // systemId
    public DocumentObject findBySystemId(String systemId) {
        return getDocumentObjectOrThrow(systemId);
    }

    // All UPDATE operations
    public DocumentObject update(DocumentObject documentObject) {
        return documentObjectRepository.save(documentObject);
    }

    // All READ operations
    @Override
    public List<DocumentObject> findDocumentObjectByAnyColumn(String column, String value) {
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DocumentObject> criteriaQuery = criteriaBuilder.createQuery(DocumentObject.class);
        Root<DocumentObject> from = criteriaQuery.from(DocumentObject.class);
        CriteriaQuery<DocumentObject> select = criteriaQuery.select(from);

        if (column.equalsIgnoreCase(DOCUMENT_OBJECT_FILE_NAME)) {
            column = "originalFilename";
        }

        criteriaQuery.where(criteriaBuilder.equal(from.get("ownedBy"), loggedInUser));
        criteriaQuery.where(criteriaBuilder.equal(from.get(column), value));
        TypedQuery<DocumentObject> typedQuery = entityManager.createQuery(select);
        return typedQuery.getResultList();
    }


    // All UPDATE operations

    /**
     * Updates a DocumentDescription object in the database. First we try to
     * locate the DocumentDescription object. If the DocumentDescription object
     * does not exist a NoarkEntityNotFoundException exception is thrown that
     * the caller has to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingDocumentDescription object to the existingDocumentDescription
     * object and the existingDocumentDescription object will be persisted to
     * the database when the transaction boundary is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the DocumentDescription
     * object from the database a NoarkConcurrencyException is thrown. Note.
     * This happens when the call to DocumentDescription.setVersion() occurs.
     * <p>
     * Note: variantFormat amd versionNumber are not nullable
     *
     * @param systemId               systemId of the incoming documentObject
     *                               object
     * @param version                ETag version
     * @param incomingDocumentObject the incoming documentObject
     * @return the updated documentObject after it is persisted
     */
    @Override
    public DocumentObject handleUpdate(
            @NotNull final String systemId, @NotNull final Long version,
            @NotNull final DocumentObject incomingDocumentObject) {

        DocumentObject existingDocumentObject =
                getDocumentObjectOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        existingDocumentObject.setFormat(
                incomingDocumentObject.getFormat());
        existingDocumentObject.setFormatDetails(
                incomingDocumentObject.getFormatDetails());
        existingDocumentObject.setOriginalFilename
                (incomingDocumentObject.getOriginalFilename());
        if (null != incomingDocumentObject.getVariantFormat()) {
            existingDocumentObject.setVariantFormat(
                    incomingDocumentObject.getVariantFormat());
        }
        if (null != incomingDocumentObject.getVersionNumber()) {
            existingDocumentObject.setVersionNumber(
                    incomingDocumentObject.getVersionNumber());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingDocumentObject.setVersion(version);
        documentObjectRepository.save(existingDocumentObject);
        return existingDocumentObject;
    }

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String documentObjectSystemId) {
        DocumentObject documentObject =
                getDocumentObjectOrThrow(documentObjectSystemId);
        documentObjectRepository.delete(documentObject);
    }

    @Override
    //TODO: How do we handle if the document has already been converted?
    // Related metadata is a one:one. So we either overwrite that the
    // original conversion happened or throw an Exception
    // Probably need administrator rights to reconvert document.
    public DocumentObject convertDocumentToPDF(String documentObjectSystemId) {
        DocumentObject originalDocumentObject =
                getDocumentObjectOrThrow(documentObjectSystemId);
        return convertDocumentToPDF(originalDocumentObject);
    }


    // All HELPER operations

    /**
     * Retrieve mime-type of a file
     *
     * @param file A Path object pointing to the file
     * @return the actual mime-type
     */
    private String getMimeType(Path file) {

        MediaType mediaType;
        TikaInputStream stream = null;
        try {
            TikaConfig config = TikaConfig.getDefaultConfig();
            Detector detector = config.getDetector();

            stream = TikaInputStream.get(file);
            Metadata metadata = new Metadata();
            mediaType = detector.detect(stream, metadata);

            stream.close();
            return mediaType.toString();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return "unknown";
    }

    /**
     * Calculate the sha256 checksum of an identified file
     *
     * @throws IOException if there is a problem with the file
     */
    private void calculateAndSetChecksum(DocumentObject documentObject)
            throws IOException {
        documentObject.setChecksumAlgorithm(defaultChecksumAlgorithm);
        String checksum = new DigestUtils(defaultChecksumAlgorithm).digestAsHex(
                getToFile(documentObject).toFile());
        documentObject.setChecksum(checksum);
    }

    /**
     * Calculate the size of a file
     *
     * @param fileLocation The location of the file
     * @return the length of the file in bytes
     * @throws IOException if something goes wrong
     */
    private Long getFileSize(String fileLocation) throws IOException {
        return Files.size(Paths.get(fileLocation));
    }

    /**
     * copy the contents of an input stream to an output stream.
     * <p>
     * Note we are currently using copyLarge as there may be large files being
     * uploaded. Perhaps we should consider only using copyLarge if the
     * length is known in advance.
     *
     * @param inputStream    inputstream of incoming document
     * @param incoming       path where the incoming document is to be placed
     * @param documentObject documentObject associated with this document
     */

    private void copyDocumentContentsToIncomingAndSetValues(
            InputStream inputStream, Path incoming,
            DocumentObject documentObject) {

        try {

            MessageDigest md = MessageDigest.getInstance(
                    documentObject.getChecksumAlgorithm());

            // Create a DigestInputStream to be read with the identified
            // checksumAlgorithm. This is done to avoid having to create the
            // checksum after the document has been uploaded
            DigestInputStream digestInputStream = new DigestInputStream(
                    inputStream, md);
            FileOutputStream outputStream = new FileOutputStream(
                    incoming.toFile());

            checkValues(copyDocumentContentsToIncoming(digestInputStream,
                    outputStream), incoming, documentObject);
            checksumSetIfOK(digestInputStream, incoming, documentObject);

        } catch (NoSuchAlgorithmException | IOException e) {
            String msg = "Internal error, could not load checksum algorithm " +
                    "[" + documentObject.getChecksumAlgorithm() + "] when " +
                    "attempting to store a file associated with " +
                    documentObject;
            logger.warn(msg);
            throw new StorageException(msg);
        }
    }


    private void checkValues(Long bytesTotal, Path incoming,
                             DocumentObject documentObject)
            throws IOException {

        // Check that  we actually copied in some data
        if (bytesTotal == 0L) {
            Files.delete(incoming);
            String msg = "The file (" + incoming.getFileName() + ") " +
                    "has 0 length content. Rejecting upload! This file is" +
                    "  being associated with " + documentObject;
            logger.warn(msg);
            throw new StorageException(msg);
        }

        // If the client has identified the filesize prior to document
        // upload, check that the number bytes uploaded matches the
        // number of bytes the client said they would upload
        if (documentObject.getFileSize() != null &&
                !documentObject.getFileSize().equals(bytesTotal)) {
            Files.delete(incoming);
            String msg = "The  file (" + incoming.getFileName()
                    + ") has length [" + bytesTotal + "] This does not" +
                    "match the dokumentobjekt filstoerrelse field [" +
                    documentObject.getFileSize() + "]. Rejecting upload! " +
                    "This file is being associated with " + documentObject;
            logger.warn(msg);
            throw new StorageException(msg);
        }
    }

    private Long copyDocumentContentsToIncoming(
            DigestInputStream digestInputStream, OutputStream outputStream)
            throws IOException {

        long bytesTotal;
        try { // Try close without exceptions if copy() threw an exception.
            bytesTotal = IOUtils.copyLarge(digestInputStream, outputStream);

            // Tidy up and close outputStream
            outputStream.flush();
            outputStream.close();

            // Finished with inputStream now as well
            digestInputStream.close();

        } finally {
            try { // Try close without exceptions if copy() threw an exception.
                digestInputStream.close();
            } catch (IOException e) {
                // swallow any error to expose exceptions from IOUtil.copy()
            }
            try { // same for outputStream
                outputStream.close();
            } catch (IOException e) {
                // empty
            }
        }
        return bytesTotal;
    }

    private void checksumSetIfOK(DigestInputStream digestInputStream,
                                 Path incoming,
                                 DocumentObject documentObject)
            throws IOException {

        // Get the digest
        byte[] digest = digestInputStream.getMessageDigest().digest();

        // Convert digest to HEX
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        String oldDigest = documentObject.getChecksum();
        String newDigest = sb.toString();
        // If the client had not specified a checksum for the incoming document
        // set the checksum to the value calculated
        if (null == oldDigest) {
            documentObject.setChecksum(newDigest);
        } else if (!oldDigest.equals(newDigest)) {
            Files.delete(incoming);
            String msg = "The file (" + incoming.getFileName() + ") " +
                    "has the following server-side calculated checksum [" +
                    newDigest + "]. This does not match the checksum " +
                    "specified by the client in the documentObject [" +
                    oldDigest + "]. Rejecting upload! This file was being " +
                    "associated with " + documentObject;
            logger.warn(msg);
            throw new StorageException(msg);
        }
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid DocumentObject back. If there is no
     * valid DocumentObject, an exception is thrown
     *
     * @param documentObjectSystemId systemID of the documentObject to retrieve
     * @return the documentObject with the identified systemID
     */
    protected DocumentObject getDocumentObjectOrThrow(
            @NotNull String documentObjectSystemId) {
        DocumentObject documentObject =
                documentObjectRepository.findBySystemId(documentObjectSystemId);
        if (documentObject == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " DocumentObject, using systemId " +
                    documentObjectSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return documentObject;
    }

    /**
     * Check if the checksum the client identifies is one that we support. If
     * it is null, set the checksun value to default. We only support SHA256,
     * however we leave it open that other checksum algorithms may be
     * supported in the future.
     *
     * @param documentObject The documentObject to check the checksum of
     */
    private void checkChecksumAlgorithmSetIfNull(
            DocumentObject documentObject) {
        String currentChecksumAlgorithm = documentObject.getChecksumAlgorithm();
        if (null != currentChecksumAlgorithm &&
                !defaultChecksumAlgorithm.equals(currentChecksumAlgorithm)) {
            throw new NikitaMalformedInputDataException(
                    "The checksum algorithm " +
                            documentObject.getChecksumAlgorithm() +
                            " is not supported");
        } else if (currentChecksumAlgorithm == null) {
            documentObject.setChecksumAlgorithm(defaultChecksumAlgorithm);
        }
    }

    private Path createIncomingFile(DocumentObject documentObject)
            throws IOException {

        String extension = FilenameUtils.getExtension(
                documentObject.getOriginalFilename());
        // Set the filename to be the systemID of the documentObject,
        // effectively locking this in as one-to-one relationship
        Path incoming = Paths.get(incomingDirectoryName +
                File.separator + documentObject.getSystemId() + "." +
                extension);

        Path path = Files.createFile(incoming);

        // Check if we can write something to the file
        if (!Files.isWritable(incoming)) {
            throw new StorageException("The file (" +
                    incoming.getFileName() + ") is not writable " +
                    "server-side. This file is being associated with " +
                    documentObject);
        }
        return path;
    }

    private Path getToDirectory(DocumentObject documentObject) {
        return Paths.get(directoryStoreName + File.separator +
                calculateDirectoryStructure(documentObject));
    }

    private Path getToFile(DocumentObject documentObject) {
        return Paths.get(directoryStoreName + File.separator +
                calculateDirectoryStructure(documentObject) +
                documentObject.getReferenceDocumentFile());
    }

    private String calculateDirectoryStructure(DocumentObject documentObject) {
        String checksum = documentObject.getChecksum();
        if (checksum.length() > 6) {
            return String.format("%s%s%s%s%s%s",
                    checksum.substring(0, 2), File.separator,
                    checksum.substring(2, 4), File.separator,
                    checksum.substring(4, 6), File.separator);
        }
        return "";
    }

    private void moveIncomingToStorage(Path incoming,
                                       DocumentObject documentObject)
            throws IOException {
        Path toDirectory = getToDirectory(documentObject);
        Files.createDirectories(toDirectory);
        Path toFile = getToFile(documentObject);

        Files.move(incoming, toFile);
    }

    /**
     * Is this mimetype a format we can automatically convert to archive
     * format. If the mimeType is null, false should be returned. This method
     * will always return either true of false.
     *
     * @param documentObject the documentobject containg the mimetype
     * @return true if the mimetype is supported, false otherwise
     */

    private boolean supportForDocumentConversion(
            @NotNull DocumentObject documentObject) {
        return mimeTypeIsConvertible(documentObject.getMimeType());
    }
}
