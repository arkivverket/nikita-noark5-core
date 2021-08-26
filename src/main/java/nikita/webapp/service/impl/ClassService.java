package nikita.webapp.service.impl;

import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.CrossReferenceHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordTemplateHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.secondary.CrossReference;
import nikita.common.model.noark5.v5.secondary.Keyword;
import nikita.common.model.noark5.v5.secondary.Screening;
import nikita.common.repository.n5v5.IClassRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IClassHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IScreeningMetadataHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IClassService;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.ICrossReferenceService;
import nikita.webapp.service.interfaces.secondary.IKeywordService;
import nikita.webapp.service.interfaces.secondary.IScreeningMetadataService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

import static java.util.List.copyOf;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateScreening;

/**
 * Service class for Class
 * <p>
 * Provides basic CRUD functionality for Class using systemId.
 * <p>
 * Also supports CREATE for a (Noark Classification Class) Class.
 * <p>
 * All public methods return Hateoas objects
 */
@Service
public class ClassService
        extends NoarkService
        implements IClassService {

    private final IClassRepository classRepository;
    private final IFileService fileService;
    private final ICaseFileService caseFileService;
    private final IRecordService recordService;
    private final ICrossReferenceService crossReferenceService;
    private final IKeywordService keywordService;
    private final IClassHateoasHandler classHateoasHandler;
    private final IMetadataService metadataService;
    private final IScreeningMetadataService screeningMetadataService;
    private final IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler;

    public ClassService(EntityManager entityManager,
                        ApplicationEventPublisher applicationEventPublisher,
                        IODataService odataService,
                        IPatchService patchService,
                        IClassRepository classRepository,
                        IFileService fileService,
                        ICaseFileService caseFileService,
                        IRecordService recordService,
                        ICrossReferenceService crossReferenceService,
                        IKeywordService keywordService,
                        IClassHateoasHandler classHateoasHandler,
                        IMetadataService metadataService,
                        IScreeningMetadataService screeningMetadataService,
                        IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.classRepository = classRepository;
        this.fileService = fileService;
        this.keywordService = keywordService;
        this.crossReferenceService = crossReferenceService;
        this.caseFileService = caseFileService;
        this.recordService = recordService;
        this.classHateoasHandler = classHateoasHandler;
        this.metadataService = metadataService;
        this.screeningMetadataService = screeningMetadataService;
        this.screeningMetadataHateoasHandler = screeningMetadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new class object to the database. Some values are set in the
     * incoming payload (e.g. title) while some are set by the core.
     * owner, createdBy, createdDate are automatically set by the core.
     *
     * @param klass The class object object with some values set
     * @return the newly persisted class object wrapped as a classHateaos object
     */
    @Override
    @Transactional
    public ClassHateoas save(@NotNull final Class klass) {
        setFinaliseEntityValues(klass);
        validateScreening(metadataService, klass);
        return packAsHateoas(classRepository.save(klass));
    }

    /**
     * Persists a new class object to the database as a sub-class to an
     * existing class object. Some values are set in the incoming  payload
     * (e.g. title) while some are set by the core.  owner, createdBy,
     * createdDate are automatically set by the core.
     *
     * @param systemId systemId of the parent object to connect this
     *                 class as a child to
     * @param klass    The class object object with some values set
     * @return the newly persisted class object wrapped as a classHateaos object
     */
    @Override
    @Transactional
    public ClassHateoas createClassAssociatedWithClass(
            @NotNull final UUID systemId,
            @NotNull final Class klass) {
        klass.setReferenceParentClass(getClassOrThrow(systemId));
        return save(klass);
    }

    /**
     * Persists a new File object that is associated with the identified
     * Class object to the database. Some values are set in the incoming
     * payload (e.g. title) while some are set by the core.  owner, createdBy,
     * createdDate are automatically set by the core.
     *
     * @param systemId systemId of the Class object to associate this
     *                 File object to
     * @param file     The File object object with some values set
     * @return the newly persisted File object wrapped as a FileHateaos object
     */
    @Override
    @Transactional
    public FileHateoas createFileAssociatedWithClass(
            @NotNull final UUID systemId,
            @NotNull final File file) {
        file.setReferenceClass(getClassOrThrow(systemId));
        return fileService.save(file);
    }

    /**
     * Persists a new CaseFile object that is associated with the identified
     * Class object to the database. Some values are set in the incoming
     * payload (e.g. title) while some are set by the core.  owner, createdBy,
     * createdDate are automatically set by the core.
     *
     * @param systemId systemId of the Class object to associate this
     *                 CaseFile object to
     * @param caseFile The CaseFile object object with some values set
     * @return the newly persisted CaseFile object wrapped as a CaseFileHateaos
     * object
     */
    @Override
    @Transactional
    public CaseFileHateoas createCaseFileAssociatedWithClass(
            @NotNull final UUID systemId,
            @NotNull final CaseFile caseFile) {
        caseFile.setReferenceClass(getClassOrThrow(systemId));
        return caseFileService.save(caseFile);
    }

    @Override
    public ScreeningMetadataHateoas createScreeningMetadataAssociatedWithClass(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata) {
        Class klass = getClassOrThrow(systemId);
        if (null == klass.getReferenceScreening()) {
            throw new NoarkEntityNotFoundException(INFO_CANNOT_FIND_OBJECT +
                    " Screening, associated with Class with systemId " +
                    systemId);
        }
        return screeningMetadataService.createScreeningMetadata(
                klass.getReferenceScreening(), screeningMetadata);
    }

    @Override
    public ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId) {
        return screeningMetadataService.getDefaultScreeningMetadata(systemId);
    }

    @Override
    public KeywordTemplateHateoas generateDefaultKeyword(@NotNull final UUID systemId) {
        return keywordService.generateDefaultKeyword(systemId);
    }

    @Override
    public CrossReferenceHateoas getDefaultCrossReference(
            @NotNull final UUID systemId) {
        return crossReferenceService.getDefaultCrossReference(systemId);
    }

    /**
     * Generate a Default Class object that can be associated with the
     * identified Class. Note this object has not been persisted to the core.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the class object you wish to
     *                 generate a default class for
     * @return the Class object wrapped as a ClassHateoas object
     */
    @Override
    public ClassHateoas generateDefaultSubClass(@NotNull final UUID systemId) {
        Class klass = new Class();
        klass.setVersion(-1L, true);
        return packAsHateoas(klass);
    }

    @Override
    public ClassHateoas generateDefaultClass(@NotNull final UUID systemId) {
        Class klass = new Class();
        klass.setVersion(-1L, true);
        return packAsHateoas(klass);
    }

    /**
     * Persists a new Record object that is associated with the identified
     * Class object to the database. Some values are set in the incoming
     * payload (e.g. title) while some are set by the core.  owner, createdBy,
     * createdDate are automatically set by the core.
     *
     * @param systemId systemId of the Class object to associate this
     *                 Record object to
     * @param record   The Record object object with some values set
     * @return the newly persisted Record object wrapped as a RecordHateaos
     * object
     */
    @Override
    @Transactional
    public RecordHateoas createRecordAssociatedWithClass(
            @NotNull final UUID systemId,
            @NotNull final RecordEntity record) {
        record.setReferenceClass(getClassOrThrow(systemId));
        return recordService.save(record);
    }

    @Override
    public CrossReferenceHateoas createCrossReferenceAssociatedWithClass(
            @NotNull final UUID systemId,
            @NotNull final CrossReference crossReference) {
        return crossReferenceService.createCrossReferenceAssociatedWithClass(
                crossReference, getClassOrThrow(systemId));
    }

    @Override
    public KeywordHateoas createKeywordAssociatedWithClass(
            @NotNull final UUID systemId, @NotNull final Keyword keyword) {
        return keywordService.createKeywordAssociatedWithClass(
                keyword, getClassOrThrow(systemId));
    }

    // All READ operations

    /**
     * Retrieve all class objects the user owns.
     *
     * @return ClassHateoas object containing a list of Class objects
     */
    @Override
    public ClassHateoas findAll() {
        return (ClassHateoas) odataService.processODataQueryGet();
    }

    @Override
    public KeywordHateoas findKeywordAssociatedWithClass(
            @NotNull final UUID systemId) {
        return (KeywordHateoas) odataService.processODataQueryGet();
    }

    @Override
    public CrossReferenceHateoas findCrossReferenceAssociatedWithClass(
            @NotNull final UUID systemId) {
        return (CrossReferenceHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve a single class object identified by systemId
     * <p>
     * Note: This method can never return a null value.
     *
     * @param systemId The systemId of the Class object to retrieve
     * @return A ClassHateoas object containing the class
     */
    @Override
    public ClassHateoas findSingleClass(@NotNull final UUID systemId) {
        return packAsHateoas(getClassOrThrow(systemId));
    }

    @Override
    public ScreeningMetadataHateoas
    getScreeningMetadataAssociatedWithClass(@NotNull final UUID systemId) {
        Screening screening = getClassOrThrow(systemId)
                .getReferenceScreening();
        if (null == screening) {
            throw new NoarkEntityNotFoundException(
                    INFO_CANNOT_FIND_OBJECT + " Screening, using systemId " +
                            systemId);
        }
        return packAsHateoas(new NikitaPage(copyOf(
                screening.getReferenceScreeningMetadata())));
    }

    /**
     * Retrieve a list of children class belonging to the class object
     * identified by systemId
     *
     * @param systemId The systemId of the Class object to retrieve its
     *                 children
     * @return A ClassHateoas object containing the children class's
     */
    @Override
    public ClassHateoas findAllChildren(@NotNull final UUID systemId) {
        // Make sure class exists
        getClassOrThrow(systemId);
        return (ClassHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve the parent Class object associated with the Class object
     * identified by systemId
     *
     * @param systemId The systemId of the Class object to retrieve its parent
     *                 Class
     * @return A ClassHateoasList of Class objects
     */
    @Override
    public ClassHateoas
    findClassAssociatedWithClass(@NotNull final UUID systemId) {
        // Make sure class exists
        getClassOrThrow(systemId);
        return (ClassHateoas) odataService.processODataQueryGet();
    }


    /**
     * Retrieve the ClassificationSystemHateoas object associated with the
     * Class object identified by systemId
     *
     * @param systemId The systemId of the Class object to retrieve the
     *                 associated ClassificationSystemHateoas
     * @return A ClassificationSystemHateoas
     */
    @Override
    public ClassificationSystemHateoas
    findClassificationSystemAssociatedWithClass(@NotNull final UUID systemId) {
        // Make sure class exists
        getClassOrThrow(systemId);
        return (ClassificationSystemHateoas) odataService.processODataQueryGet();
    }

    @Override
    public FileHateoas findAllFileAssociatedWithClass(
            @NotNull final UUID systemId) {
        getClassOrThrow(systemId);
        return (FileHateoas) odataService.processODataQueryGet();
    }

    @Override
    public RecordHateoas findAllRecordAssociatedWithClass(
            @NotNull final UUID systemId) {
        // Make sure class exists
        getClassOrThrow(systemId);
        return (RecordHateoas) odataService.processODataQueryGet();
    }

    // All UPDATE operations

    /**
     * Updates a Class object in the database. First we try to locate the
     * Class object. If the Class object does not exist a
     * NoarkEntityNotFoundException exception is thrown that the caller has
     * to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingClass object to the existingClass object and the existingClass
     * object will be persisted to the database when the transaction boundary
     * is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the Class object from
     * the database a NoarkConcurrencyException is thrown. Note. This happens
     * when the call to Class.setVersion() occurs.
     * *
     *
     * @param systemId      systemId of the incoming class object
     * @param version       ETag version
     * @param incomingClass the contents to update
     * @return the updated Class object as a ClassHateoas object
     */
    @Override
    @Transactional
    public ClassHateoas handleUpdate(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final Class incomingClass) {
        Class existingClass = getClassOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingClass, existingClass);
        return packAsHateoas(existingClass);
    }

    // All DELETE operations

    /**
     * delete a Class entity with given systemId
     * <p>
     * Note: When deleting, observe the following behaviour if there are
     * multiple parents. If a Class object has both a Class and
     * ClassificationSystem object as parent, return the object at the
     * closest level. In this case it is Class.
     * <p>
     * Note: This method can return null! But it is unlikely that it will
     *
     * @param systemId systemId of the Class object to delete
     */
    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        deleteEntity(getClassOrThrow(systemId));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        classRepository.deleteByOwnedBy(getUser());
    }

    // All HELPER operations

    public ScreeningMetadataHateoas packAsHateoas(NikitaPage page) {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(page);
        applyLinksAndHeader(screeningMetadataHateoas,
                screeningMetadataHateoasHandler);
        return screeningMetadataHateoas;
    }

    public ClassHateoas packAsHateoas(@NotNull final Class klass) {
        ClassHateoas classHateoas = new ClassHateoas(klass);
        applyLinksAndHeader(classHateoas, classHateoasHandler);
        return classHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once.  If you call this, be aware
     * that you will only ever get a valid Class back. If there is no valid
     * Class, an exception is thrown
     *
     * @param systemId systemId of the class object you are looking for
     * @return the newly found class object or null if it does not exist
     */
    protected Class getClassOrThrow(@NotNull final UUID systemId) {
        Optional<Class> klass = classRepository.findBySystemId(systemId);
        if (klass.isEmpty()) {
            String error = INFO_CANNOT_FIND_OBJECT + " Class, using systemId " +
                    systemId;
            throw new NoarkEntityNotFoundException(error);
        }
        return klass.get();
    }
}
