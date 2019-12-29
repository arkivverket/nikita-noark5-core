package nikita.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.net.MediaType;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.Part;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.DocumentFlow;
import nikita.common.model.noark5.v5.casehandling.Precedence;
import nikita.common.model.noark5.v5.casehandling.secondary.*;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.*;
import nikita.common.model.noark5.v5.interfaces.entities.*;
import nikita.common.model.noark5.v5.interfaces.entities.admin.IAdministrativeUnitEntity;
import nikita.common.model.noark5.v5.interfaces.entities.admin.IUserEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.*;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NikitaMalformedHeaderException;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.*;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpMethod.*;

public final class CommonUtils {

    /**
     * Holds a list of servletPaths and their HTTP methods
     */
    private static Map<String, Set<HttpMethod>> requestMethodMap = new HashMap<>();

    /**
     * Holds a list of mimeType and boolean value stating if we should attempt
     * to convert a file of that mimeType to an archive version
     */
    private static Map<String, Boolean> mimeTypeConversionMap =
            new HashMap<>();

    /**
     * Holds a mapping of Norwegian entity names to English entity names
     * e.g mappe->file
     */
    private static Map<String, ModelNames> nor2engEntityMap = new HashMap<>();

    /**
     * Holds a mapping of mimeTypes and their archive equivalent. The equivalent
     * contains both the equivalent mimeType and file extension.
     */
    private static Map<String, FileExtensionAndMimeType> archiveVersion =
            new HashMap<>();

    // You shall not instantiate me!
    private CommonUtils() {
    }

    public static final class Validation {

        /**
         * Home made validation utility. We decided not to use springs validation framework as it is done on a
         * per-class basis. We need to validate on a per-class, per-CRUD method basis. E.g. A incoming fonds
         * will be validated differently if it's a CREATE operation as opposed to a UPDATE operation.
         *
         * @param nikitaEntity incoming nikita object
         * @return true if valid. If not valid an exception is thrown
         */
        public static boolean validateUpdateNoarkEntity(@NotNull INikitaEntity nikitaEntity) {

            if (nikitaEntity instanceof INoarkTitleDescriptionEntity) {
                rejectIfEmptyOrWhitespace(((INoarkTitleDescriptionEntity) nikitaEntity).getDescription());
                rejectIfEmptyOrWhitespace(((INoarkTitleDescriptionEntity) nikitaEntity).getTitle());
            }
            if (nikitaEntity instanceof INoarkTitleDescriptionEntity) {
                rejectIfEmptyOrWhitespace(((INoarkTitleDescriptionEntity) nikitaEntity).getDescription());
                rejectIfEmptyOrWhitespace(((INoarkTitleDescriptionEntity) nikitaEntity).getTitle());
            }


            return true;
        }

        public static void rejectIfEmptyOrWhitespace(String stringToCheck) {

        }

        public static boolean validateCreateNoarkEntity(@NotNull INoarkGeneralEntity noarkEntity) {
            return true;
        }

        public static Long parseETAG(String quotedETAG) {
            long etagVal = -1L;
            if (quotedETAG != null) {
                try {
                    etagVal = Long.parseLong(quotedETAG.replaceAll("^\"|\"$", ""));
                } catch (NumberFormatException nfe) {
                    throw new NikitaMalformedHeaderException("eTag value is not numeric. Nikita  uses numeric ETAG " +
                            "values >= 0.");
                }
            }
            if (etagVal < 0) {
                throw new NikitaMalformedHeaderException("eTag value is less than 0. This is illegal" +
                        "as ETAG values show version of an entity in the database and start at 0");
            }
            return etagVal;
        }
    }

    public static final class FileUtils {


        public static Boolean mimeTypeIsConvertible(@NotNull String mimeType) {
            return mimeTypeConversionMap.containsKey(mimeType);
        }

        public static void setDefaultMimeTypesAsConvertible() {
            // TODO: Add this back in after teaching. At the moment there seems
            // to be a problem with unoconv and I don't have time to debug this
            // to find out why ...
            /*
            // doc, ppt, xls
            addMimeTypeAsConvertible(MediaType.MICROSOFT_EXCEL);
            addMimeTypeAsConvertible(MediaType.MICROSOFT_POWERPOINT);
            addMimeTypeAsConvertible(MediaType.MICROSOFT_WORD);
            //docx, pptx, xlsx
            addMimeTypeAsConvertible(MediaType.OOXML_DOCUMENT);
            addMimeTypeAsConvertible(MediaType.OOXML_PRESENTATION);
            addMimeTypeAsConvertible(MediaType.OOXML_SHEET);
            //odt, odp, ods
            addMimeTypeAsConvertible(MediaType.OPENDOCUMENT_PRESENTATION);
            addMimeTypeAsConvertible(MediaType.OPENDOCUMENT_SPREADSHEET);
            addMimeTypeAsConvertible(MediaType.OPENDOCUMENT_TEXT);
            */
        }

        public static void addMimeTypeAsConvertible(@NotNull MediaType mimeType) {
            mimeTypeConversionMap.put(mimeType.toString(), true);
        }

        public static void addMimeTypeAsConvertible(@NotNull String mimeType) {
            mimeTypeConversionMap.put(mimeType, true);
        }

        public static void addProductionToArchiveVersion(
                @NotNull String productionMimeType,
                @NotNull String archiveFileExtension,
                @NotNull String archiveMimeType) {

            archiveVersion.put(productionMimeType,
                    new FileExtensionAndMimeType(archiveMimeType,
                            archiveFileExtension));
        }

        public static String getArchiveFileExtension(
                String productionMimeType) {

            String fileExtension = "unknown";

            if (null == productionMimeType) {
                return fileExtension;
            }

            FileExtensionAndMimeType fileExtensionAndMimeType =
                    archiveVersion.get(productionMimeType);
            if (null != fileExtensionAndMimeType) {
                fileExtension = archiveVersion.get(
                        productionMimeType).getFileExtension();
            } else {
                fileExtension = "unknown";
            }
            return fileExtension;
        }

        public static String getArchiveMimeType(
                String productionMimeType) {
            return archiveVersion.get(productionMimeType).getMimeType();
        }

        public static FileExtensionAndMimeType
        getArchiveFileExtensionAndMimeType(
                String productionMimeType) {
            return archiveVersion.get(productionMimeType);
        }

    }

    public static final class WebUtils {

        private static final Logger logger =
                LoggerFactory.getLogger(WebUtils.class);

        public static void addNorToEnglishNameMap(
                @NotNull String norwegianName,
                @NotNull String englishNameDatabase,
                @NotNull String englishNameObject) {
            nor2engEntityMap.put(norwegianName,
                    new ModelNames(englishNameDatabase, englishNameObject));
        }

        /**
         * split a URL in two, using the API name as the point to split. This is
         * only undertaken if the URL starts with 'http://' or 'https://',
         * otherwise the url is returned as is.
         * <p>
         * The reason for this is to solve the problem where spring is
         * rejecting query parameters that contain a double '//' as in
         * 'http://', as the '//' is seen as a potential security issue.
         *
         * @param url the URL as a string to sanitise for OData internal use
         * @return the sanitised URL as a String
         */
        public static String sanitiseUrlForOData(String url) {
            if (url != null &&
                    (url.contains("http://") || url.contains("https" +
                            "://"))) {
                String[] split = url.split(HATEOAS_API_PATH);
                if (split != null && split.length != 2) {
                    throw new NikitaMalformedInputDataException(
                            "OData filtering problem with URL [" + url + "]");
                }
                return ODATA_PATH + split[1];
            } else
                return url;
        }

        public static String getEnglishNameObject(String norwegianName) {
            ModelNames names = nor2engEntityMap.get(norwegianName);
            return names.getEnglishNameObject();
        }

        public static String getEnglishNameDatabase(String norwegianName) {
            return nor2engEntityMap.get(norwegianName).getEnglishNameDatabase();
        }

        public static String getSuccessStatusStringForDelete() {
            return "{\"status\" : \"Success\"}";
        }

        /**
         * requestMethodMap maps servletPaths to HTTP methods. If a particular
         * servletPath supports
         * <p>
         * As servletPaths and corresponding methods are added they are formatted into a proper
         * Allows description. An example /hateaoas-api/arkivstruktur/ny-arkivskaper supports both
         * GET and POST. Therefore the mapping of /hateaoas-api/arkivstruktur/ny-arkivskaper to its
         * methods should be GET, HEAD, POST
         * <p>
         * Note GET automatically implies support for HEAD. Added.
         * <p>
         * Consider adding a list of allowed HTTP methods, but assuming spring will return allowed methods
         *
         * @param servletPath The incoming servletPath e.g. /hateoas-api/arkivstruktur/
         * @param method      An instance of a single HTTP method
         */
        public static void addRequestToMethodMap(@NotNull String servletPath, @NotNull Set<HttpMethod> method) {

            Set<HttpMethod> methods = requestMethodMap.get(servletPath.toLowerCase());
            if (null == methods) {
                methods = new TreeSet<>();
            }

            // GET automatically implies HEAD
            if (method.contains(GET)) {
                methods.add(GET);
                methods.add(HEAD);
            }
            if (!method.contains(OPTIONS)) {
                methods.add(OPTIONS);
            }
            methods.addAll(method);
            requestMethodMap.put(servletPath.toLowerCase(), methods);
        }


        /**
         * Provides the ability to throw an Exception if this call fails.
         * This is just a helper to make the code more readable in other places.
         *
         * @param servletPath
         */

        public static HttpMethod[] getMethodsForRequestOrThrow(@NotNull String servletPath) {

            HttpMethod[] methods = getMethodsForRequest(servletPath);
            if (null == methods) {
                logger.error("Error servletPath [" + servletPath + "] has no known HTTP methods");
                throw new NikitaException("Error servletPath [" + servletPath + "] has no known HTTP methods");
            }
            return methods;
        }

        /**
         * Provides the ability to throw an Exception if this call fails.
         * This is just a helper to make the code more readable in other places.
         *
         * @param servletPath
         */
        public static HttpMethod[] getMethodsForRequest(@NotNull String servletPath) {

            // Adding a trailing slash as the map is setup with a trailing slash
            if (false == servletPath.endsWith("/")) {
                servletPath += SLASH;
            }

            //Next, we have to replace any occurrences of an actual UUID with the word systemID
            String updatedServletPath = servletPath;
            if (servletPath.startsWith(SLASH + HREF_BASE_METADATA)) {
                Pattern pattern = Pattern.compile(SLASH + HREF_BASE_METADATA +
                        "/[a-z]+/");
                int ignoreFirstChars = (SLASH + HREF_BASE_METADATA).length();
                String toCheck = servletPath.substring(ignoreFirstChars);
                int slashLocation = toCheck.indexOf("/");
                if (slashLocation > 0) {
                    toCheck = toCheck.substring(0, slashLocation);
                    String path = SLASH + HREF_BASE_METADATA + toCheck + SLASH;

                    if (!path.equals(servletPath)) {
                        updatedServletPath = path + "{" + CODE + "}" + SLASH;
                        if (servletPath.contains("/format/")) {
                            updatedServletPath += "{value}/";
                        }
                    }
                } else {
                    updatedServletPath = servletPath;
                }
            } else {

                // The following pattern is taken from
                // https://stackoverflow.com/questions/136505/searching-for-uuids-in-text-with-regex#6640851
                Pattern pattern = Pattern.compile(
                        "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
                Matcher matcher = pattern.matcher(servletPath.toLowerCase());
                updatedServletPath = matcher.replaceFirst(SYSTEM_ID_PARAMETER);
            }

            Set<HttpMethod> methods = requestMethodMap.get(
                    updatedServletPath.toLowerCase());
            if (methods == null) {
                return null;
            }
            return methods.toArray(new HttpMethod[methods.size()]);
        }
    }

    public static final class Hateoas {

        public static final class Deserialize {

            private final static DateTimeFormatter dateFormatter =
                    new DateTimeFormatterBuilder()
                            .append(ISO_DATE)
                            .parseDefaulting(HOUR_OF_DAY, 0)
                            .parseDefaulting(MINUTE_OF_HOUR, 0)
                            .toFormatter();

            public static OffsetDateTime deserializeDate(String fieldname,
                                                         ObjectNode objectNode,
                                                         StringBuilder errors,
                                                         boolean required) {
                OffsetDateTime d = null;
                JsonNode currentNode = objectNode.get(fieldname);
                if (null != currentNode) {
                    try {
                        String date = currentNode.textValue();
                        if (date.endsWith("Z")) {
                            DateTimeFormatter formatter = ISO_INSTANT;
                            d = ZonedDateTime.parse(date,
                                    formatter.withZone(ZoneId.of("UTC"))).
                                    toOffsetDateTime();
                            // d = ZonedDateTime.parse(date).toOffsetDateTime();
                        } else {
                            DateTimeFormatter dateFormatter =
                                    new DateTimeFormatterBuilder().appendPattern(
                                            "yyyy-MM-dd+HH:mm")
                                            .parseDefaulting(HOUR_OF_DAY, 0)
                                            .toFormatter()
                                            .withZone(ZoneId.of("Europe/Oslo"));
                            d = ZonedDateTime.parse(currentNode.textValue(),
                                    dateFormatter).toOffsetDateTime();
                        }
                    } catch (DateTimeParseException e) {
                        errors.append("Malformed ");
                        errors.append(fieldname);
                        errors.append(". Make sure format is either ");
                        errors.append(NOARK_DATE_FORMAT_PATTERN + " or ");
                        errors.append(NOARK_ZONED_DATE_FORMAT_PATTERN + ". ");
                        errors.append("Message is ");
                        errors.append(e.getMessage());
                    }
                    objectNode.remove(fieldname);
                } else if (required) {
                    errors.append(fieldname + " is missing. ");
                }
                return d;
/*
DateTimeFormatter dateFormatter =
                                    DateTimeFormatter.ofPattern("yyyy-MM-ddZ").
                                    withZone(OffsetDateTime.now().getOffset());
                            d = ZonedDateTime.parse(currentNode.textValue(),
                                    dateFormatter).toOffsetDateTime();
                        DateTimeFormatter dateFormatter =
                                new DateTimeFormatterBuilder().appendPattern(
                                        "yyyy-MM-dd+HH:mm")
                                        .parseDefaulting(HOUR_OF_DAY, 0)
                                        .toFormatter()
                                        .withZone(ZoneId.of("Europe/Oslo"));
                        d = ZonedDateTime.parse(currentNode.textValue(),
                                dateFormatter).toOffsetDateTime();

                        DateTimeFormatter dateFormatter =
                                new DateTimeFormatterBuilder().appendPattern(
                                        "yyyy-MM-ddZ")
                                        .toFormatter()
                                        .withZone(ZoneId.of("Europe/Oslo"));
                        d = ZonedDateTime.parse(currentNode.textValue(),
                                dateFormatter).toOffsetDateTime();

 */
            }

            public static OffsetDateTime deserializeDate(String fieldname,
                                                         ObjectNode objectNode,
                                                         StringBuilder errors) {
                return deserializeDate(fieldname, objectNode, errors, false);
            }

            public static OffsetDateTime deserializeDateTime(String fieldname,
                                                             ObjectNode objectNode,
                                                             StringBuilder errors,
                                                             boolean required) {
                OffsetDateTime d = null;
                JsonNode currentNode = objectNode.get(fieldname);
                if (null != currentNode) {
                    try {
                        d = parse(currentNode.textValue(),
                                ISO_OFFSET_DATE_TIME);
                    } catch (DateTimeParseException e) {
                        errors.append("Malformed ");
                        errors.append(fieldname);
                        errors.append(". Make sure format is ");
                        errors.append(NOARK_DATE_TIME_FORMAT_PATTERN + ". ");
                    }
                    objectNode.remove(fieldname);
                } else if (required) {
                    errors.append(fieldname + "is missing. ");
                }
                return d;
            }

            public static OffsetDateTime deserializeDateTime(String fieldname,
                                                             ObjectNode objectNode,
                                                             StringBuilder errors) {
                return deserializeDateTime(fieldname, objectNode, errors, false);
            }

            public static void deserialiseDocumentMedium(IDocumentMedium documentMediumEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize documentMedium
                JsonNode currentNode = objectNode.get(DOCUMENT_MEDIUM);
                if (null != currentNode) {
                    documentMediumEntity.setDocumentMedium(currentNode.textValue());
                    objectNode.remove(DOCUMENT_MEDIUM);
                }
            }

            public static void deserialiseNoarkSystemIdEntity(INikitaEntity noarkSystemIdEntity,
                                                              ObjectNode objectNode, StringBuilder errors) {
                // Deserialize systemId
                JsonNode currentNode = objectNode.get(SYSTEM_ID);
                if (null != currentNode) {
                    noarkSystemIdEntity.setSystemId(
                            UUID.fromString(currentNode.textValue()));
                    objectNode.remove(SYSTEM_ID);
                }
            }


            public static void deserialiseNoarkMetadataEntity(IMetadataEntity metadataEntity,
                                                              ObjectNode objectNode, StringBuilder errors) {
                // Deserialize systemId
                deserialiseNoarkSystemIdEntity(metadataEntity, objectNode, errors);

                JsonNode currentNode = objectNode.get(CODE);
                if (null != currentNode) {
                    metadataEntity.setCode(currentNode.textValue());
                    objectNode.remove(CODE);
                }
                currentNode = objectNode.get(CODE_NAME);
                if (null != currentNode) {
                    metadataEntity.setCodeName(currentNode.textValue());
                    objectNode.remove(CODE_NAME);
                }
                currentNode = objectNode.get(CODE_INACTIVE);
                if (null != currentNode) {
                    metadataEntity.setInactive(currentNode.booleanValue());
                    objectNode.remove(CODE_INACTIVE);
                }
            }

            public static void deserialiseKeyword(IKeyword keywordEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize keyword
                JsonNode currentNode = objectNode.get(KEYWORD);

                if (null != currentNode) {
                    ArrayList<Keyword> keywords = new ArrayList<>();
                    if (currentNode.isArray()) {
                        currentNode.iterator();
                        for (JsonNode node : currentNode) {
                            String keywordText = node.textValue();
                            Keyword keyword = new Keyword();
                            keyword.setKeyword(keywordText);
                            keywords.add(keyword);
                        }
                        keywordEntity.setReferenceKeyword(keywords);
                    }
                    objectNode.remove(KEYWORD);
                }
            }

            private static void deserialiseCorrespondencePartType(
                    ICorrespondencePartEntity correspondencePart,
                    ObjectNode objectNode, StringBuilder errors) {
                // Deserialize korrespondanseparttype
                JsonNode currentNode =
                        objectNode.get(CORRESPONDENCE_PART_TYPE);
                if (null != currentNode) {

                    JsonNode node = currentNode.get(CODE);
                    if (null != node) {
                        correspondencePart.setCorrespondencePartTypeCode(
                                node.textValue());
                    }
                    node = currentNode.get(CODE_NAME);
                    if (null != node) {
                        correspondencePart.
                                setCorrespondencePartTypeCodeName(
                                        node.textValue());
                    }
                    if (null != correspondencePart.
                            getCorrespondencePartTypeCode()) {
                        objectNode.remove(CORRESPONDENCE_PART_TYPE);
                    }
                } else {
                    errors.append("The CorrespondencePart object you tried to");
                    errors.append(" create is missing  ");
                    errors.append(CORRESPONDENCE_PART_TYPE);
                }
            }

            private static void deserialisePartRole(
                    IPartEntity part,
                    ObjectNode objectNode, StringBuilder errors) {
                // Deserialize parttype
                JsonNode currentNode =
                        objectNode.get(PART_ROLE);
                if (null != currentNode) {

                    JsonNode node = currentNode.get(CODE);
                    if (null != node) {
                        part.setPartTypeCode(node.textValue());
                    }
                    node = currentNode.get(CODE_NAME);
                    if (null != node) {
                        part.setPartTypeCodeName(node.textValue());
                    }
                    if (null != part.getPartTypeCode()) {
                        objectNode.remove(PART_ROLE);
                    }
                } else {
                    errors.append("The Part object you tried to");
                    errors.append(" create is missing  ");
                    errors.append(PART_ROLE);
                }
            }

            public static void deserialiseAuthor(IAuthor authorEntity,
                                                 ObjectNode objectNode, StringBuilder errors) {

                // Deserialize author
                JsonNode currentNode = objectNode.get(AUTHOR);
                if (null != currentNode) {
                    ArrayList<Author> authors = new ArrayList<>();
                    if (currentNode.isArray()) {
                        currentNode.iterator();
                        for (JsonNode node : currentNode) {
                            String location = node.textValue();
                            Author author = new Author();
                            author.setAuthor(location);
                            authors.add(author);
                        }
                        authorEntity.setReferenceAuthor(authors);
                    }
                    objectNode.remove(AUTHOR);
                }
            }

            public static void deserialiseStorageLocation(IStorageLocation storageLocationEntity,
                                                          ObjectNode objectNode, StringBuilder errors) {
                // Deserialize storageLocation
                JsonNode currentNode = objectNode.get(STORAGE_LOCATION);

                if (null != currentNode) {
                    ArrayList<StorageLocation> storageLocations = new ArrayList<>();
                    if (currentNode.isArray()) {
                        currentNode.iterator();
                        for (JsonNode node : currentNode) {
                            String location = node.textValue();
                            StorageLocation storageLocation = new StorageLocation();
                            storageLocation.setStorageLocation(location);
                            storageLocations.add(storageLocation);
                        }
                        storageLocationEntity.setReferenceStorageLocation(storageLocations);
                    }
                    objectNode.remove(STORAGE_LOCATION);
                }
            }

            public static void deserialiseNoarkCreateEntity(INoarkCreateEntity noarkCreateEntity,
                                                            ObjectNode objectNode, StringBuilder errors) {
                // Deserialize createdDate
                noarkCreateEntity.setCreatedDate(deserializeDateTime(CREATED_DATE, objectNode, errors));

                // Deserialize createdBy
                JsonNode currentNode = objectNode.get(CREATED_BY);
                if (null != currentNode) {
                    noarkCreateEntity.setCreatedBy(currentNode.textValue());
                    objectNode.remove(CREATED_BY);
                }
            }

            public static void deserialiseNoarkFinaliseEntity(INoarkFinaliseEntity finaliseEntity,
                                                              ObjectNode objectNode, StringBuilder errors) {
                // Deserialize finalisedDate
                finaliseEntity.setFinalisedDate(deserializeDateTime(FINALISED_DATE, objectNode, errors));

                // Deserialize finalisedBy
                JsonNode currentNode = objectNode.get(FINALISED_BY);
                if (null != currentNode) {
                    finaliseEntity.setFinalisedBy(currentNode.textValue());
                    objectNode.remove(FINALISED_BY);
                }
            }

            public static void deserialiseNoarkTitleDescriptionEntity(INoarkTitleDescriptionEntity
                                                                              titleDescriptionEntity,
                                                                      ObjectNode objectNode, StringBuilder errors) {
                // Deserialize title
                JsonNode currentNode = objectNode.get(TITLE);
                if (null != currentNode) {
                    titleDescriptionEntity.setTitle(currentNode.textValue());
                    objectNode.remove(TITLE);
                }

                // Deserialize description
                currentNode = objectNode.get(DESCRIPTION);
                if (null != currentNode) {
                    titleDescriptionEntity.setDescription(currentNode.textValue());
                    objectNode.remove(DESCRIPTION);
                }
            }

            public static void deserialiseNoarkEntity(INoarkGeneralEntity noarkEntity, ObjectNode objectNode, StringBuilder errors) {
                deserialiseNoarkSystemIdEntity(noarkEntity, objectNode, errors);
                deserialiseNoarkTitleDescriptionEntity(noarkEntity, objectNode, errors);
                deserialiseNoarkCreateEntity(noarkEntity, objectNode, errors);
                deserialiseNoarkFinaliseEntity(noarkEntity, objectNode, errors);
            }

            public static String checkNodeObjectEmpty(JsonNode objectNode) {
                StringBuffer result = new StringBuffer();
                if (objectNode.size() != 0) {
                    Iterator<Map.Entry<String, JsonNode>> nodes = objectNode.fields();
                    while (nodes.hasNext()) {
                        Map.Entry entry = nodes.next();
                        String keyField = (String) entry.getKey();
                        result.append(keyField);
                        if (nodes.hasNext()) {
                            result.append(", ");
                        }
                    }
                }
                return result.toString();
            }

            public static List<CrossReference> deserialiseCrossReferences(
                    @NotNull ICrossReference crossReferenceObject,
                    @NotNull ObjectNode objectNode,
                    @NotNull StringBuilder errors) {

                List<CrossReference> crossReferences = crossReferenceObject.
                        getReferenceCrossReference();
                if (null != crossReferences) {
                    crossReferences.forEach(
                            crossReference ->
                                    deserialiseCrossReferenceEntity(crossReference,
                                            objectNode, errors));
                }
                objectNode.remove(CROSS_REFERENCES);
                return crossReferences;
            }

            /**
             * Note: This approach requires an additional step to add the
             * fromSystemId. This can't be solved here.
             *
             * @param crossReferenceEntity
             * @param objectNode
             * @param errors
             */
            public static void deserialiseCrossReferenceEntity(
                    ICrossReferenceEntity crossReferenceEntity,
                    ObjectNode objectNode, StringBuilder errors) {

                deserialiseNoarkSystemIdEntity(crossReferenceEntity,
                        objectNode, errors);

                JsonNode currentNode = objectNode.get(REFERENCE_TO_CLASS);
                if (null != currentNode) {
                    crossReferenceEntity.setReferenceType(REFERENCE_TO_CLASS);
                    crossReferenceEntity.setToSystemId(
                            currentNode.textValue());
                    objectNode.remove(REFERENCE_TO_CLASS);
                }
                currentNode = objectNode.get(REFERENCE_TO_REGISTRATION);
                if (null != currentNode) {
                    crossReferenceEntity.setReferenceType(
                            REFERENCE_TO_REGISTRATION);
                    crossReferenceEntity.setToSystemId(
                            currentNode.textValue());
                    objectNode.remove(REFERENCE_TO_REGISTRATION);
                }
                currentNode = objectNode.get(REFERENCE_TO_FILE);
                if (null != currentNode) {
                    crossReferenceEntity.setReferenceType(REFERENCE_TO_FILE);
                    crossReferenceEntity.setToSystemId(
                            currentNode.textValue());
                    objectNode.remove(REFERENCE_TO_FILE);
                }
                objectNode.remove(CROSS_REFERENCE);
            }

            public static void deserialiseComments(IComment commentObject, ObjectNode objectNode, StringBuilder errors) {
                List<Comment> comments = commentObject.getReferenceComment();
                for (Comment comment : comments) {
                    deserialiseCommentEntity(comment, objectNode, errors);
                }
            }

            public static void deserialiseCommentEntity(ICommentEntity commentEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize commentText
                JsonNode currentNode = objectNode.get(COMMENT_TEXT);
                if (null != currentNode) {
                    commentEntity.setCommentText(currentNode.textValue());
                    objectNode.remove(COMMENT_TEXT);
                }
                // Deserialize commentType
                currentNode = objectNode.get(COMMENT_TYPE);
                if (null != currentNode) {
                    commentEntity.setCommentType(currentNode.textValue());
                    objectNode.remove(COMMENT_TYPE);
                }

                // Deserialize commentDate
                commentEntity.setCommentDate(deserializeDate(COMMENT_DATE, objectNode, errors));

                // Deserialize commentRegisteredBy
                currentNode = objectNode.get(COMMENT_REGISTERED_BY);
                if (null != currentNode) {
                    commentEntity.setCommentRegisteredBy(currentNode.textValue());
                    objectNode.remove(COMMENT_REGISTERED_BY);
                }
                objectNode.remove(COMMENT);
            }


            public static List<Series> deserialiseReferenceMultipleSeries(ObjectNode objectNode, StringBuilder errors) {
                List<Series> referenceSeries = null;
                JsonNode node = objectNode.get(REFERENCE_SERIES);
                if (node != null) {
                    referenceSeries = new ArrayList<>();
                    deserialiseReferenceSeries(referenceSeries, objectNode.deepCopy(), errors);
                }
                objectNode.remove(REFERENCE_SERIES);
                return referenceSeries;
            }

            public static void deserialiseReferenceSeries(List<Series> referenceSeries, ObjectNode objectNode, StringBuilder errors) {

            }

            public static Disposal deserialiseDisposal(ObjectNode objectNode, StringBuilder errors) {
                Disposal disposal = null;
                JsonNode disposalNode = objectNode.get(DISPOSAL);
                if (disposalNode != null) {
                    disposal = new Disposal();
                    deserialiseDisposalEntity(disposal, objectNode, errors);
                    objectNode.remove(DISPOSAL);
                }
                return disposal;
            }

            public static void deserialiseDisposalEntity(IDisposalEntity disposalEntity, ObjectNode objectNode, StringBuilder errors) {

                // Deserialize disposalDecision
                JsonNode currentNode = objectNode.get(DISPOSAL_DECISION);
                if (null != currentNode) {
                    disposalEntity.setDisposalDecision(currentNode.textValue());
                    objectNode.remove(DISPOSAL_DECISION);
                }
                // Deserialize disposalAuthority(
                currentNode = objectNode.get(DISPOSAL_AUTHORITY);
                if (null != currentNode) {
                    disposalEntity.setDisposalAuthority(currentNode.textValue());
                    objectNode.remove(DISPOSAL_AUTHORITY);
                }
                // Deserialize preservationTime
                currentNode = objectNode.get(DISPOSAL_PRESERVATION_TIME);
                if (null != currentNode) {
                    disposalEntity.setPreservationTime(Integer.valueOf(currentNode.intValue()));
                    objectNode.remove(DISPOSAL_PRESERVATION_TIME);
                }
                // Deserialize disposalDate
                disposalEntity.setDisposalDate(deserializeDate(DISPOSAL_DATE, objectNode, errors));
            }

            public static DisposalUndertaken deserialiseDisposalUndertaken(ObjectNode objectNode, StringBuilder errors) {
                DisposalUndertaken disposalUndertaken = null;
                JsonNode disposalUndertakenNode = objectNode.get(DISPOSAL_UNDERTAKEN);
                if (disposalUndertakenNode != null) {
                    disposalUndertaken = new DisposalUndertaken();
                    deserialiseDisposalUndertakenEntity(disposalUndertaken, objectNode, errors);
                    objectNode.remove(DISPOSAL_UNDERTAKEN);
                }
                return disposalUndertaken;
            }

            public static void deserialiseDisposalUndertakenEntity(IDisposalUndertakenEntity disposalUndertakenEntity,
                                                                   ObjectNode objectNode, StringBuilder errors) {
                // Deserialize disposalBy
                JsonNode currentNode = objectNode.get(DISPOSAL_UNDERTAKEN_BY);
                if (null != currentNode) {
                    disposalUndertakenEntity.setDisposalBy(currentNode.textValue());
                    objectNode.remove(DISPOSAL_UNDERTAKEN_BY);
                }

                // Deserialize disposalDate
                disposalUndertakenEntity.setDisposalDate(deserializeDate(DISPOSAL_UNDERTAKEN_DATE, objectNode, errors));
            }

            public static Deletion deserialiseDeletion(ObjectNode objectNode, StringBuilder errors) {
                Deletion deletion = null;
                JsonNode deletionNode = objectNode.get(DELETION);
                if (deletionNode != null) {
                    deletion = new Deletion();
                    deserialiseDeletionEntity(deletion, deletionNode.deepCopy(), errors);
                    // TODO consider if objectNode.remove(DELETION); is needed due to deepCopy()
                }
                return deletion;
            }

            public static void deserialiseDeletionEntity(IDeletionEntity deletionEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize deletionBy
                JsonNode currentNode = objectNode.get(DELETION_BY);
                if (null != currentNode) {
                    deletionEntity.setDeletionBy(currentNode.textValue());
                    objectNode.remove(DELETION_BY);
                }

                // Deserialize deletionType
                currentNode = objectNode.get(DELETION_TYPE);
                if (null != currentNode) {
                    deletionEntity.setDeletionType(currentNode.textValue());
                    objectNode.remove(DELETION_TYPE);
                }

                // Deserialize deletionDate
                deletionEntity.setDeletionDate(deserializeDate(DELETION_DATE, objectNode, errors));

                objectNode.remove(DELETION);
            }

            public static List<Precedence> deserialisePrecedences(ObjectNode objectNode, StringBuilder errors) {
//                objectNode.remove(PRECEDENCE);
                // TODO : Looks like I'm missing!!!
                return null;
            }

            public static void deserialisePrecedence(IPrecedenceEntity precedenceEntity, ObjectNode objectNode, StringBuilder errors) {

                deserialiseNoarkCreateEntity(precedenceEntity, objectNode, errors);
                deserialiseNoarkTitleDescriptionEntity(precedenceEntity, objectNode, errors);
                deserialiseNoarkFinaliseEntity(precedenceEntity, objectNode, errors);

                // Deserialize precedenceDate
                precedenceEntity.setPrecedenceDate(deserializeDate(PRECEDENCE_DATE, objectNode, errors));

                // Deserialize precedenceAuthority
                JsonNode currentNode = objectNode.get(PRECEDENCE_AUTHORITY);
                if (null != currentNode) {
                    precedenceEntity.setPrecedenceAuthority(currentNode.textValue());
                    objectNode.remove(PRECEDENCE_AUTHORITY);
                }
                // Deserialize sourceOfLaw
                currentNode = objectNode.get(PRECEDENCE_SOURCE_OF_LAW);
                if (null != currentNode) {
                    precedenceEntity.setSourceOfLaw(currentNode.textValue());
                    objectNode.remove(PRECEDENCE_SOURCE_OF_LAW);
                }
                // Deserialize precedenceApprovedBy
                currentNode = objectNode.get(PRECEDENCE_APPROVED_BY);
                if (null != currentNode) {
                    precedenceEntity.setPrecedenceApprovedBy(currentNode.textValue());
                    objectNode.remove(PRECEDENCE_APPROVED_BY);
                }
                // Deserialize precedenceStatus
                currentNode = objectNode.get(PRECEDENCE_STATUS);
                if (null != currentNode) {
                    precedenceEntity.setPrecedenceStatus(currentNode.textValue());
                    objectNode.remove(PRECEDENCE_STATUS);
                }
                // Deserialize precedenceApprovedDate
                precedenceEntity.setPrecedenceApprovedDate(deserializeDate(PRECEDENCE_APPROVED_DATE, objectNode, errors));
            }

            public static List<Part> deserialiseCaseParties(ObjectNode objectNode, StringBuilder errors) {
                ArrayList<Part> caseParties = new ArrayList<>();
                //JsonNode jsonPart = objectNode.get(PART);
                // TODO: I seem tobe missing my body of code ...
/*                for (CorrespondencePart correspondencePart: caseParties) {
                    deserialiseCorrespondencePart(correspondencePart, objectNode);
                    objectNode.remove(CORRESPONDENCE_PART);
                }
*/
                return caseParties;
            }

            public static void deserialiseAdministrativeUnitEntity(IAdministrativeUnitEntity administrativeUnit,
                                                                   ObjectNode objectNode, StringBuilder errors) {
                if (null != administrativeUnit) {

                    deserialiseNoarkSystemIdEntity(administrativeUnit, objectNode, errors);
                    deserialiseNoarkCreateEntity(administrativeUnit, objectNode, errors);
                    deserialiseNoarkFinaliseEntity(administrativeUnit, objectNode, errors);

                    // Deserialize kortnavn
                    JsonNode currentNode = objectNode.get(SHORT_NAME);
                    if (null != currentNode) {
                        administrativeUnit.setShortName(currentNode.textValue());
                        objectNode.remove(SHORT_NAME);
                    }

                    // Deserialize administrativEnhetNavn
                    currentNode = objectNode.get(ADMINISTRATIVE_UNIT_NAME);
                    if (null != currentNode) {
                        administrativeUnit.setAdministrativeUnitName(currentNode.textValue());
                        objectNode.remove(ADMINISTRATIVE_UNIT_NAME);
                    }

                    // It is unclear if status values are to be set by special endpoint calls
                    // e.g. adminunit->avsluttAdministrativeEnhet
                    // Deserialize administrativEnhetsstatus
                    currentNode = objectNode.get(ADMINISTRATIVE_UNIT_STATUS);
                    if (null != currentNode) {
                        administrativeUnit.setAdministrativeUnitStatus(currentNode.textValue());
                        objectNode.remove(ADMINISTRATIVE_UNIT_STATUS);
                    }

                    // Deserialize referanseOverordnetEnhet
                    currentNode = objectNode.get(ADMINISTRATIVE_UNIT_PARENT_REFERENCE);
                    if (null != currentNode) {
                        AdministrativeUnit parent = new AdministrativeUnit();
                        parent.setSystemId(
                                UUID.fromString(currentNode.textValue()));
                        parent.getReferenceChildAdministrativeUnit().add((AdministrativeUnit) administrativeUnit);
                        administrativeUnit.setParentAdministrativeUnit(parent);
                        objectNode.remove(ADMINISTRATIVE_UNIT_PARENT_REFERENCE);
                    }
                }
            }

            public static void deserialiseUserEntity(IUserEntity user, ObjectNode objectNode, StringBuilder errors) {
                // TODO implement

            }

            public static void deserialiseContactInformationEntity(IContactInformationEntity contactInformation,
                                                                   ObjectNode objectNode, StringBuilder errors) {
                if (contactInformation != null) {
                    // Deserialize telefonnummer
                    JsonNode currentNode = objectNode.get(TELEPHONE_NUMBER);
                    if (null != currentNode) {
                        contactInformation.setTelephoneNumber(currentNode.textValue());
                        objectNode.remove(TELEPHONE_NUMBER);
                    }
                    // Deserialize epostadresse
                    currentNode = objectNode.get(EMAIL_ADDRESS);
                    if (null != currentNode) {
                        contactInformation.setEmailAddress(currentNode.textValue());
                        objectNode.remove(EMAIL_ADDRESS);
                    }
                    // Deserialize mobiltelefon
                    currentNode = objectNode.get(MOBILE_TELEPHONE_NUMBER);
                    if (null != currentNode) {
                        contactInformation.setMobileTelephoneNumber(currentNode.textValue());
                        objectNode.remove(MOBILE_TELEPHONE_NUMBER);
                    }
                }
            }

            public static void deserialiseSimpleAddressEntity(
                    ISimpleAddressEntity simpleAddress, ObjectNode objectNode,
                    StringBuilder errors) {
                if (simpleAddress != null) {
                    // Deserialize adresselinje1
                    JsonNode currentNode = objectNode.get(ADDRESS_LINE_1);
                    if (null != currentNode) {
                        simpleAddress.setAddressLine1(currentNode.textValue());
                        objectNode.remove(ADDRESS_LINE_1);
                    }
                    // Deserialize adresselinje2
                    currentNode = objectNode.get(ADDRESS_LINE_2);
                    if (null != currentNode) {
                        simpleAddress.setAddressLine2(currentNode.textValue());
                        objectNode.remove(ADDRESS_LINE_2);
                    }
                    // Deserialize adresselinje3
                    currentNode = objectNode.get(ADDRESS_LINE_3);
                    if (null != currentNode) {
                        simpleAddress.setAddressLine3(currentNode.textValue());
                        objectNode.remove(ADDRESS_LINE_3);
                    }
                    // Deserialize postnummer
                    currentNode = objectNode.get(POSTAL_NUMBER);
                    if (null != currentNode) {
                        simpleAddress.setPostalNumber(
                                new PostalNumber(currentNode.textValue()));
                        objectNode.remove(POSTAL_NUMBER);
                    }
                    // Deserialize poststed
                    currentNode = objectNode.get(POSTAL_TOWN);
                    if (null != currentNode) {
                        simpleAddress.setPostalTown(currentNode.textValue());
                        objectNode.remove(POSTAL_TOWN);
                    }
                    // Deserialize landkode
                    currentNode = objectNode.get(COUNTRY_CODE);
                    if (null != currentNode) {
                        simpleAddress.setCountryCode(currentNode.textValue());
                        objectNode.remove(COUNTRY_CODE);
                    }
                }
            }

            public static void deserialiseCorrespondencePartPersonEntity(
                    ICorrespondencePartPersonEntity partPerson,
                    ObjectNode objectNode, StringBuilder errors) {

                deserialiseCorrespondencePartType(
                        partPerson, objectNode, errors);
                deserialiseGenericPersonEntity(partPerson, objectNode, errors);

                // Deserialize postalAddress
                JsonNode currentNode = objectNode.get(POSTAL_ADDRESS);
                if (null != currentNode) {
                    PostalAddress postalAddress = new PostalAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(
                            simpleAddress,
                            currentNode.deepCopy(), errors);
                    postalAddress.setSimpleAddress(simpleAddress);
                    postalAddress.
                            setCorrespondencePartPerson(
                                    (CorrespondencePartPerson) partPerson);
                    partPerson.setPostalAddress(postalAddress);
                    objectNode.remove(N5ResourceMappings.POSTAL_ADDRESS);
                }

                // Deserialize residingAddress
                currentNode = objectNode.get(RESIDING_ADDRESS);
                if (null != currentNode) {
                    ResidingAddress residingAddress =
                            new ResidingAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(
                            simpleAddress,
                            currentNode.deepCopy(), errors);
                    residingAddress.setSimpleAddress(simpleAddress);
                    residingAddress.
                            setCorrespondencePartPerson(
                                    (CorrespondencePartPerson) partPerson);
                    partPerson.setResidingAddress(residingAddress);
                    objectNode.remove(RESIDING_ADDRESS);
                }

                // Deserialize kontaktinformasjon
                currentNode = objectNode.get(CONTACT_INFORMATION);
                if (null != currentNode) {
                    ContactInformation contactInformation =
                            new ContactInformation();
                    deserialiseContactInformationEntity(
                            contactInformation, currentNode.deepCopy(), errors);
                    partPerson.
                            setContactInformation(contactInformation);
                    objectNode.remove(CONTACT_INFORMATION);
                }
            }

            public static void deserialisePartPersonEntity(
                    IPartPersonEntity partPersonEntity,
                    ObjectNode objectNode, StringBuilder errors) {

                deserialisePartRole(
                        partPersonEntity, objectNode, errors);

                deserialiseGenericPersonEntity(partPersonEntity,
                        objectNode, errors);

                // FIXME : Ugly hack as code evolved where metadataentity
                // and nikitaentity suddenly no longer are the same
                // just to keep things moving ...
                ((INoarkGeneralEntity) partPersonEntity).setTitle("TEMP: REMOVE ME!");

                // Deserialize postalAddress
                JsonNode currentNode = objectNode.get(POSTAL_ADDRESS);
                if (null != currentNode) {
                    PostalAddress postalAddress = new PostalAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(
                            simpleAddress,
                            currentNode.deepCopy(), errors);
                    postalAddress.setSimpleAddress(simpleAddress);
                    postalAddress.setPartPerson((PartPerson) partPersonEntity);
                    partPersonEntity.setPostalAddress(postalAddress);
                    objectNode.remove(N5ResourceMappings.POSTAL_ADDRESS);
                }

                // Deserialize residingAddress
                currentNode = objectNode.get(RESIDING_ADDRESS);
                if (null != currentNode) {
                    ResidingAddress residingAddress =
                            new ResidingAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(
                            simpleAddress,
                            currentNode.deepCopy(), errors);
                    residingAddress.setSimpleAddress(simpleAddress);
                    residingAddress.setPartPerson((PartPerson) partPersonEntity);
                    partPersonEntity.setResidingAddress(residingAddress);
                    objectNode.remove(RESIDING_ADDRESS);
                }

                // Deserialize kontaktinformasjon
                currentNode = objectNode.get(CONTACT_INFORMATION);
                if (null != currentNode) {
                    ContactInformation contactInformation =
                            new ContactInformation();
                    deserialiseContactInformationEntity(
                            contactInformation, currentNode.deepCopy(), errors);
                    partPersonEntity.
                            setContactInformation(contactInformation);
                    objectNode.remove(CONTACT_INFORMATION);
                }
            }

            public static void deserialisePartUnitEntity(
                    IPartUnitEntity partUnit,
                    ObjectNode objectNode, StringBuilder errors) {

                deserialisePartRole(partUnit, objectNode, errors);

                // FIXME : Ugly hack as code evolved where metadataentity
                // and nikitaentity suddenly no longer are the same
                // just to keep things moving ...
                ((INoarkGeneralEntity) partUnit).setTitle("TEMP: REMOVE ME!");

                // Deserialize kontaktperson
                JsonNode currentNode = objectNode.get(CONTACT_PERSON);
                if (null != currentNode) {
                    partUnit.setContactPerson(currentNode.textValue());
                    objectNode.remove(CONTACT_PERSON);
                }

                // Deserialize navn
                currentNode = objectNode.get(NAME);
                if (null != currentNode) {
                    partUnit.setName(currentNode.textValue());
                    objectNode.remove(NAME);
                }

                // Deserialize organisasjonsnummer
                currentNode = objectNode.get(ORGANISATION_NUMBER);
                if (null != currentNode) {
                    partUnit.setOrganisationNumber(currentNode.textValue());
                    objectNode.remove(ORGANISATION_NUMBER);
                }

                // Deserialize kontaktperson
                currentNode = objectNode.get(CONTACT_PERSON);
                if (null != currentNode) {
                    partUnit.setContactPerson(currentNode.textValue());
                    objectNode.remove(CONTACT_PERSON);
                }

                // Deserialize postadresse
                currentNode = objectNode.get(POSTAL_ADDRESS);
                if (null != currentNode) {
                    PostalAddress postalAddressEntity = new PostalAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(simpleAddress,
                            currentNode.deepCopy(), errors);
                    postalAddressEntity.setSimpleAddress(simpleAddress);
                    partUnit.setPostalAddress(postalAddressEntity);
                    objectNode.remove(POSTAL_ADDRESS);
                }

                // Deserialize forretningsadresse
                currentNode = objectNode.get(BUSINESS_ADDRESS);
                if (null != currentNode) {
                    BusinessAddress businessAddressEntity =
                            new BusinessAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(simpleAddress,
                            currentNode.deepCopy(), errors);
                    businessAddressEntity.setSimpleAddress(simpleAddress);
                    partUnit.setBusinessAddress(
                            businessAddressEntity);
                    objectNode.remove(BUSINESS_ADDRESS);
                }

                // Deserialize kontaktinformasjon
                currentNode = objectNode.get(CONTACT_INFORMATION);
                if (null != currentNode) {
                    ContactInformation contactInformation =
                            new ContactInformation();
                    deserialiseContactInformationEntity(
                            contactInformation, currentNode.deepCopy(), errors);
                    partUnit.setContactInformation(contactInformation);
                    objectNode.remove(CONTACT_INFORMATION);
                }
            }

            public static void deserialiseGenericPersonEntity(
                    IGenericPersonEntity person, ObjectNode objectNode,
                    StringBuilder errors) {

                // Deserialize foedselsnummer
                JsonNode currentNode = objectNode.get(SOCIAL_SECURITY_NUMBER);
                if (null != currentNode) {
                    person.setSocialSecurityNumber(currentNode.textValue());
                    objectNode.remove(SOCIAL_SECURITY_NUMBER);
                }

                // Deserialize dnummer
                currentNode = objectNode.get(D_NUMBER);
                if (null != currentNode) {
                    person.setdNumber(currentNode.textValue());
                    objectNode.remove(D_NUMBER);
                }

                // Deserialize navn
                currentNode = objectNode.get(NAME);
                if (null != currentNode) {
                    person.setName(currentNode.textValue());
                    objectNode.remove(NAME);
                }

            }


            public static void deserialiseCorrespondencePartInternalEntity(ICorrespondencePartInternalEntity
                                                                                   correspondencePartInternal,
                                                                           ObjectNode objectNode, StringBuilder errors) {
                deserialiseCorrespondencePartType(correspondencePartInternal, objectNode, errors);

                // Deserialize administrativEnhet
                JsonNode currentNode = objectNode.get(ADMINISTRATIVE_UNIT);
                if (null != currentNode) {
                    correspondencePartInternal.setAdministrativeUnit(currentNode.textValue());
                    objectNode.remove(ADMINISTRATIVE_UNIT);
                }
                // Deserialize saksbehandler
                currentNode = objectNode.get(CASE_HANDLER);
                if (null != currentNode) {
                    correspondencePartInternal.setCaseHandler(currentNode.textValue());
                    objectNode.remove(CASE_HANDLER);
                }

                // Deserialize referanseAdministratitivEnhet
                currentNode = objectNode.get(REFERENCE_ADMINISTRATIVE_UNIT);
                if (null != currentNode) {
                    AdministrativeUnit administrativeUnit = new AdministrativeUnit();
                    deserialiseAdministrativeUnitEntity(administrativeUnit, currentNode.deepCopy(), errors);
                    //correspondencePartInternal
                    //      .setReferenceAdministrativeUnit
                    //    (administrativeUnit);
                    objectNode.remove(REFERENCE_ADMINISTRATIVE_UNIT);
                }

                // Deserialize referanseSaksbehandler
                currentNode = objectNode.get(REFERENCE_CASE_HANDLER);
                if (null != currentNode) {
                    User user = new User();
                    deserialiseUserEntity(user, currentNode.deepCopy(), errors);
                    //correspondencePartInternal.setReferenceUser(user);
                    objectNode.remove(REFERENCE_CASE_HANDLER);
                }
            }

            public static void deserialiseCorrespondencePartUnitEntity(
                    ICorrespondencePartUnitEntity correspondencePartUnit,
                    ObjectNode objectNode, StringBuilder errors) {

                deserialiseCorrespondencePartType(correspondencePartUnit,
                        objectNode, errors);

                // Deserialize kontaktperson
                JsonNode currentNode = objectNode.get(CONTACT_PERSON);
                if (null != currentNode) {
                    correspondencePartUnit.setContactPerson(
                            currentNode.textValue());
                    objectNode.remove(CONTACT_PERSON);
                }

                // Deserialize navn
                currentNode = objectNode.get(NAME);
                if (null != currentNode) {
                    correspondencePartUnit.setName(currentNode.textValue());
                    objectNode.remove(NAME);
                }

                // Deserialize organisasjonsnummer
                currentNode = objectNode.get(ORGANISATION_NUMBER);
                if (null != currentNode) {
                    correspondencePartUnit.setOrganisationNumber(
                            currentNode.textValue());
                    objectNode.remove(ORGANISATION_NUMBER);
                }

                // Deserialize kontaktperson
                currentNode = objectNode.get(CONTACT_PERSON);
                if (null != currentNode) {
                    correspondencePartUnit.setContactPerson(
                            currentNode.textValue());
                    objectNode.remove(CONTACT_PERSON);
                }

                // Deserialize postadresse
                currentNode = objectNode.get(POSTAL_ADDRESS);
                if (null != currentNode) {
                    PostalAddress postalAddressEntity = new PostalAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(simpleAddress,
                            currentNode.deepCopy(), errors);
                    postalAddressEntity.setSimpleAddress(simpleAddress);
                    correspondencePartUnit.
                            setPostalAddress(postalAddressEntity);
                    objectNode.remove(N5ResourceMappings.POSTAL_ADDRESS);
                }

                // Deserialize forretningsadresse
                currentNode = objectNode.get(BUSINESS_ADDRESS);
                if (null != currentNode) {
                    BusinessAddress businessAddressEntity =
                            new BusinessAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(simpleAddress,
                            currentNode.deepCopy(), errors);
                    businessAddressEntity.setSimpleAddress(simpleAddress);
                    correspondencePartUnit.setBusinessAddress(
                            businessAddressEntity);
                    objectNode.remove(BUSINESS_ADDRESS);
                }

                // Deserialize kontaktinformasjon
                currentNode = objectNode.get(CONTACT_INFORMATION);
                if (null != currentNode) {
                    ContactInformation contactInformation =
                            new ContactInformation();
                    deserialiseContactInformationEntity(
                            contactInformation, currentNode.deepCopy(), errors);
                    correspondencePartUnit.
                            setContactInformation(contactInformation);
                    objectNode.remove(CONTACT_INFORMATION);
                }
            }

            // TODO: Double check how the JSON of this looks if multiple fondsCreators are embedded within a fonds
            // object There might be some 'root' node in the JSON to remove
            // This might be implemented as an array???
            public static List<FondsCreator> deserialiseFondsCreators(ObjectNode objectNode, StringBuilder errors) {
                return null;
            }

            public static void deserialiseFondsCreator(IFondsCreatorEntity fondsCreatorEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize systemID
                JsonNode currentNode = objectNode.get(SYSTEM_ID);
                if (null != currentNode) {
                    fondsCreatorEntity.setSystemId(
                            UUID.fromString(currentNode.textValue()));
                    objectNode.remove(SYSTEM_ID);
                }
                // Deserialize fondsCreatorId
                currentNode = objectNode.get(FONDS_CREATOR_ID);
                if (null != currentNode) {
                    fondsCreatorEntity.setFondsCreatorId(currentNode.textValue());
                    objectNode.remove(FONDS_CREATOR_ID);
                }
                // Deserialize fondsCreatorName
                currentNode = objectNode.get(FONDS_CREATOR_NAME);
                if (null != currentNode) {
                    fondsCreatorEntity.setFondsCreatorName(currentNode.textValue());
                    objectNode.remove(FONDS_CREATOR_NAME);
                }
                // Deserialize description
                currentNode = objectNode.get(DESCRIPTION);
                if (null != currentNode) {
                    fondsCreatorEntity.setDescription(currentNode.textValue());
                    objectNode.remove(DESCRIPTION);
                }
            }

            public static Screening deserialiseScreening(ObjectNode objectNode, StringBuilder errors) {
                Screening screening = null;
                JsonNode screeningNode = objectNode.get(SCREENING);
                if (screeningNode != null) {
                    screening = new Screening();
                    deserialiseScreeningEntity(screening, screeningNode.deepCopy(), errors);
                }
                objectNode.remove(SCREENING);
                return screening;
            }

            public static void deserialiseScreeningEntity(IScreeningEntity screeningEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize accessRestriction
                JsonNode currentNode = objectNode.get(SCREENING_ACCESS_RESTRICTION);
                if (null != currentNode) {
                    screeningEntity.setAccessRestriction(currentNode.textValue());
                    objectNode.remove(SCREENING_ACCESS_RESTRICTION);
                }
                // Deserialize screeningAuthority
                currentNode = objectNode.get(SCREENING_AUTHORITY);
                if (null != currentNode) {
                    screeningEntity.setScreeningAuthority(currentNode.textValue());
                    objectNode.remove(SCREENING_AUTHORITY);
                }
                // Deserialize screeningMetadata
                currentNode = objectNode.get(SCREENING_METADATA);
                if (null != currentNode) {
                    screeningEntity.setScreeningMetadata(currentNode.textValue());
                    objectNode.remove(SCREENING_METADATA);
                }
                // Deserialize screeningDocument
                currentNode = objectNode.get(SCREENING_DOCUMENT);
                if (null != currentNode) {
                    screeningEntity.setScreeningDocument(currentNode.textValue());
                    objectNode.remove(SCREENING_DOCUMENT);
                }
                // Deserialize screeningExpiresDate
                screeningEntity.setScreeningExpiresDate(deserializeDate(SCREENING_EXPIRES_DATE, objectNode, errors));

                // Deserialize screeningDuration
                currentNode = objectNode.get(SCREENING_DURATION);
                if (null != currentNode) {
                    screeningEntity.setScreeningDuration(currentNode.textValue());
                    objectNode.remove(SCREENING_DURATION);
                }
                objectNode.remove(SCREENING);
            }

            public static Classified deserialiseClassified(ObjectNode objectNode, StringBuilder errors) {
                Classified classified = null;
                JsonNode classifiedNode = objectNode.get(CLASSIFIED);
                if (classifiedNode != null) {
                    classified = new Classified();
                    deserialiseClassifiedEntity(classified, classifiedNode.deepCopy(), errors);
                }
                //TODO: Only remove if the hashset is actually empty, otherwise let it go back up with the extra values
                objectNode.remove(CLASSIFIED);
                return classified;
            }

            public static void deserialiseClassifiedEntity(IClassifiedEntity classifiedEntity, ObjectNode objectNode, StringBuilder errors) {

                // Deserialize classification
                JsonNode currentNode = objectNode.get(CLASSIFICATION);
                if (null != currentNode) {
                    classifiedEntity.setClassification(currentNode.textValue());
                    objectNode.remove(CLASSIFICATION);
                }
                // Deserialize classificationDate
                classifiedEntity.setClassificationDate(deserializeDate(CLASSIFICATION_DATE, objectNode, errors));

                // Deserialize classificationBy
                currentNode = objectNode.get(CLASSIFICATION_BY);
                if (null != currentNode) {
                    classifiedEntity.setClassificationBy(currentNode.textValue());
                    objectNode.remove(CLASSIFICATION_BY);
                }
                // Deserialize classificationDowngradedDate
                classifiedEntity.setClassificationDowngradedDate(deserializeDate(CLASSIFICATION_DOWNGRADED_DATE,
                        objectNode, errors));

                // Deserialize
                currentNode = objectNode.get(CLASSIFICATION_DOWNGRADED_BY);
                if (null != currentNode) {
                    classifiedEntity.setClassificationDowngradedBy(currentNode.textValue());
                    objectNode.remove(CLASSIFICATION_DOWNGRADED_BY);
                }
                objectNode.remove(CLASSIFIED); // TODO why is this removed here?
            }
        }

        public static final class Serialize {
            public static String formatDate(OffsetDateTime value) {
                return value.format(ISO_OFFSET_DATE);
            }

            public static String formatDateTime(OffsetDateTime value) {
                return value.format(ISO_OFFSET_DATE_TIME);
            }

            public static void printTitleAndDescription(JsonGenerator jgen,
                                                        INoarkTitleDescriptionEntity titleDescriptionEntity)
                    throws IOException {

                if (titleDescriptionEntity != null) {
                    if (titleDescriptionEntity.getTitle() != null) {
                        jgen.writeStringField(TITLE, titleDescriptionEntity.getTitle());
                    }
                    if (titleDescriptionEntity.getDescription() != null) {
                        jgen.writeStringField(DESCRIPTION, titleDescriptionEntity.getDescription());
                    }
                }
            }

            public static void printCreateEntity(JsonGenerator jgen,
                                                 INoarkCreateEntity createEntity)
                    throws IOException {
                if (createEntity != null) {
                    if (createEntity.getCreatedDate() != null) {
                        jgen.writeStringField(CREATED_DATE,
                                formatDateTime(createEntity.getCreatedDate()));
                    }
                    if (createEntity.getCreatedBy() != null) {
                        jgen.writeStringField(CREATED_BY, createEntity.getCreatedBy());
                    }
                }
            }

            public static void printFileEntity(JsonGenerator jgen,
                                               IFileEntity file)
                    throws IOException {
                printSystemIdEntity(jgen, file);
                printStorageLocation(jgen, file);

                if (file.getFileId() != null) {
                    jgen.writeStringField(FILE_ID, file.getFileId());
                }
                if (file.getTitle() != null) {
                    jgen.writeStringField(TITLE, file.getTitle());
                }
                if (file.getOfficialTitle() != null) {
                    jgen.writeStringField(FILE_PUBLIC_TITLE, file.getOfficialTitle());
                }
                if (file.getDescription() != null) {
                    jgen.writeStringField(DESCRIPTION, file.getDescription());
                }


            }

            public static void printCaseFileEntity(JsonGenerator jgen,
                                                   ICaseFileEntity caseFile)
                    throws IOException {

                if (caseFile.getCreatedDate() != null) {
                    jgen.writeStringField(CREATED_DATE,
                            formatDateTime(caseFile.getCreatedDate()));
                }
                if (caseFile.getCreatedBy() != null) {
                    jgen.writeStringField(CREATED_BY, caseFile.getCreatedBy());
                }
                if (caseFile.getFinalisedDate() != null) {
                    jgen.writeStringField(FINALISED_DATE,
                            formatDateTime(caseFile.getFinalisedDate()));
                }
                if (caseFile.getFinalisedBy() != null) {
                    jgen.writeStringField(FINALISED_BY,
                            caseFile.getFinalisedBy());
                }
                if (caseFile.getCaseYear() != null) {
                    jgen.writeNumberField(CASE_YEAR,
                            caseFile.getCaseYear());
                }
                if (caseFile.getCaseSequenceNumber() != null) {
                    jgen.writeNumberField(CASE_SEQUENCE_NUMBER,
                            caseFile.getCaseSequenceNumber());
                }
                if (caseFile.getCaseDate() != null) {
                    jgen.writeStringField(CASE_DATE,
                            formatDate(caseFile.getCaseDate()));
                }
                if (caseFile.getCaseResponsible() != null) {
                    jgen.writeStringField(CASE_RESPONSIBLE,
                            caseFile.getCaseResponsible());
                }
                if (caseFile.getRecordsManagementUnit() != null) {
                    jgen.writeStringField(CASE_RECORDS_MANAGEMENT_UNIT,
                            caseFile.getRecordsManagementUnit());
                }
                if (caseFile.getCaseStatusCode() != null) {
                    jgen.writeObjectFieldStart(CASE_STATUS);
                    printCode(jgen, caseFile.getCaseStatusCode(),
                            caseFile.getCaseStatusName());
                    jgen.writeEndObject();
                }
                if (caseFile.getLoanedDate() != null) {
                    jgen.writeStringField(CASE_LOANED_DATE,
                            formatDate(caseFile.getLoanedDate()));
                }
                if (caseFile.getLoanedTo() != null) {
                    jgen.writeStringField(CASE_LOANED_TO,
                            caseFile.getLoanedTo());
                }
            }

            public static void printCode(
                    JsonGenerator jgen, String code, String name)
                    throws IOException {
                jgen.writeStringField(CODE, code);
                if (name != null) {
                    jgen.writeStringField(CODE_NAME, name);
                }
            }

            public static void printRecordEntity(JsonGenerator jgen,
                                                 IRecordEntity record)
                    throws IOException {
                if (record != null) {
                    printSystemIdEntity(jgen, record);
                    printCreateEntity(jgen, record);
                    if (record.getArchivedDate() != null) {
                        jgen.writeStringField(RECORD_ARCHIVED_DATE,
                                formatDateTime(record.getArchivedDate()));
                    }
                    if (record.getArchivedBy() != null) {
                        jgen.writeStringField(RECORD_ARCHIVED_BY,
                                record.getArchivedBy());
                    }
                    if (record.getArchivedDate() != null) {
                        jgen.writeStringField(RECORD_ARCHIVED_DATE,
                                formatDateTime(record.getArchivedDate()));
                    }
                    if (record.getArchivedBy() != null) {
                        jgen.writeStringField(RECORD_ARCHIVED_BY,
                                record.getArchivedBy());
                    }
                    if (record.getRecordId() != null) {
                        jgen.writeStringField(RECORD_ID,
                                record.getRecordId());
                    }
                    if (record.getTitle() != null) {
                        jgen.writeStringField(TITLE, record.getTitle());
                    }
                    if (record.getOfficialTitle() != null) {
                        jgen.writeStringField(FILE_PUBLIC_TITLE,
                                record.getOfficialTitle());
                    }
                    if (record.getDescription() != null) {
                        jgen.writeStringField(DESCRIPTION,
                                record.getDescription());
                    }
                }
            }

            public static void printRecordNoteEntity(
                    JsonGenerator jgen, IRecordNoteEntity recordNote)
                    throws IOException {
                if (recordNote != null) {
                    if (recordNote.getDocumentDate() != null) {
                        jgen.writeStringField(REGISTRY_ENTRY_DOCUMENT_DATE,
                                formatDate(recordNote.getDocumentDate()));
                    }
                    if (recordNote.getReceivedDate() != null) {
                        jgen.writeStringField(REGISTRY_ENTRY_RECEIVED_DATE,
                                formatDate(recordNote.getReceivedDate()));
                    }
                    if (recordNote.getSentDate() != null) {
                        jgen.writeStringField(REGISTRY_ENTRY_SENT_DATE,
                                formatDate(recordNote.getSentDate()));
                    }
                    if (recordNote.getDueDate() != null) {
                        jgen.writeStringField(REGISTRY_ENTRY_DUE_DATE,
                                formatDate(recordNote.getDueDate()));
                    }
                    if (recordNote.getFreedomAssessmentDate() != null) {
                        jgen.writeStringField(
                                REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE,
                                formatDate(
                                        recordNote.getFreedomAssessmentDate()));
                    }
                    if (recordNote.getNumberOfAttachments() != null) {
                        jgen.writeNumberField(REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS,
                                recordNote.getNumberOfAttachments());
                    }
                    if (recordNote.getLoanedDate() != null) {
                        jgen.writeStringField(CASE_LOANED_DATE,
                                formatDate(recordNote.getLoanedDate()));
                    }
                    if (recordNote.getLoanedTo() != null) {
                        jgen.writeStringField(CASE_LOANED_TO,
                                recordNote.getLoanedTo());
                    }
                }
            }

            public static void printRegistryEntryEntity(
                    JsonGenerator jgen, IRegistryEntryEntity registryEntry)
                    throws IOException {
                if (registryEntry != null) {
                    if (registryEntry.getRecordYear() != null) {
                        jgen.writeNumberField(REGISTRY_ENTRY_YEAR,
                                registryEntry.getRecordYear());
                    }
                    if (registryEntry.getRecordSequenceNumber() != null) {
                        jgen.writeNumberField(REGISTRY_ENTRY_SEQUENCE_NUMBER,
                                registryEntry.getRecordSequenceNumber());
                    }
                    if (registryEntry.getRegistryEntryNumber() != null) {
                        jgen.writeNumberField(REGISTRY_ENTRY_NUMBER,
                                registryEntry.getRegistryEntryNumber());
                    }
                    if (registryEntry.getRegistryEntryType() != null) {
                        jgen.writeStringField(REGISTRY_ENTRY_TYPE,
                                registryEntry.getRegistryEntryType());
                    }
                    if (registryEntry.getRecordStatus() != null) {
                        jgen.writeStringField(REGISTRY_ENTRY_STATUS,
                                registryEntry.getRecordStatus());
                    }
                    if (registryEntry.getRecordDate() != null) {
                        jgen.writeStringField(REGISTRY_ENTRY_DATE,
                                formatDate(registryEntry.getRecordDate()));
                    }
                }
            }

            public static void printDocumentMedium(JsonGenerator jgen,
                                                   IDocumentMedium documentMedium)
                    throws IOException {
                if (documentMedium.getDocumentMedium() != null) {
                    jgen.writeStringField(DOCUMENT_MEDIUM, documentMedium.getDocumentMedium());
                }

            }

            public static void printFinaliseEntity(JsonGenerator jgen,
                                                   INoarkFinaliseEntity finaliseEntity)
                    throws IOException {
                if (finaliseEntity.getFinalisedBy() != null) {
                    jgen.writeStringField(FINALISED_BY, finaliseEntity.getFinalisedBy());
                }
                if (finaliseEntity.getFinalisedDate() != null) {
                    jgen.writeStringField(FINALISED_DATE,
                            formatDateTime(finaliseEntity.getFinalisedDate()));
                }
            }

            public static void printSystemIdEntity(JsonGenerator jgen,
                                                   INikitaEntity systemIdEntity)
                    throws IOException {
                if (systemIdEntity != null && systemIdEntity.getSystemId() != null) {
                    jgen.writeStringField(SYSTEM_ID, systemIdEntity.getSystemId());
                }
            }


            /**
             * Note: This method assumes that the startObject has already been
             * written
             * <p>
             * {
             * "_links": {
             * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/": {
             * "href": "https://n5.example.com/api/arkivstruktur"
             * },
             * "https://rel.arkivverket.no/noark5/v5/api/sakarkiv/": {
             * "href": "https://n5.example.com/api/sakarkiv"
             * },
             * "https://rel.arkivverket.no/noark5/v5/api/admin/system/": {
             * "href": "https://n5.example.com/api/admin/system/",
             * }
             * }
             * }
             *
             * @param jgen
             * @param links
             * @throws IOException
             */
            public static void printHateoasLinks(
                    JsonGenerator jgen, List<Link> links) throws IOException {

                if (links != null && links.size() > 0) {
                    jgen.writeObjectFieldStart(LINKS);
                    for (Link link : links) {
                        jgen.writeObjectFieldStart(link.getRel());
                        jgen.writeStringField(HREF, link.getHref());
                        if (link.getTemplated()) {
                            jgen.writeBooleanField(TEMPLATED,
                                    link.getTemplated());
                        }
                        jgen.writeEndObject();
                    }
                    jgen.writeEndObject();
                }
            }

            public static void printMetadataEntity(
                    JsonGenerator jgen, IMetadataEntity metadataEntity)
                    throws IOException {
                // e.g."mappetype" {}
                if (metadataEntity != null) {
                    printCode(jgen, metadataEntity.getCode(),
                            metadataEntity.getCodeName());
                    if (metadataEntity.getInactive() != null &&
                            metadataEntity.getInactive()) {
                        jgen.writeBooleanField(CODE_INACTIVE,
                                metadataEntity.getInactive());
                    }
                }
            }

            public static void printMetadataEntity(
                    JsonGenerator jgen, IMetadataEntity metadataEntity,
                    String objectName)
                    throws IOException {
                // e.g."mappetype" {}
                if (metadataEntity != null) {
                    jgen.writeObjectFieldStart(objectName);
                    printCode(jgen, metadataEntity.getCode(),
                            metadataEntity.getCodeName());
                    if (metadataEntity.getInactive() != null &&
                            metadataEntity.getInactive()) {
                        jgen.writeBooleanField(CODE_INACTIVE,
                                metadataEntity.getInactive());
                    }
                    jgen.writeEndObject();
                }
            }

            private static void printCorrespondencePart(
                    JsonGenerator jgen,
                    ICorrespondencePartEntity correspondencePart)
                    throws IOException {

                if (correspondencePart != null) {
                    printSystemIdEntity(jgen, correspondencePart);
                    if (correspondencePart.
                            getCorrespondencePartTypeCode() != null) {
                        jgen.writeObjectFieldStart(CORRESPONDENCE_PART_TYPE);
                        printCode(jgen,
                                correspondencePart.
                                        getCorrespondencePartTypeCode(),
                                correspondencePart.
                                        getCorrespondencePartTypeCodeName());
                        jgen.writeEndObject();
                    }
                }
            }

            public static void printContactInformation(JsonGenerator jgen, IContactInformationEntity contactInformation)
                    throws IOException {

                if (null != contactInformation) {
                    jgen.writeFieldName(CONTACT_INFORMATION);
                    jgen.writeStartObject();
                    if (null != contactInformation.getEmailAddress()) {
                        jgen.writeStringField(EMAIL_ADDRESS, contactInformation.getEmailAddress());
                    }
                    if (null != contactInformation.getMobileTelephoneNumber()) {
                        jgen.writeStringField(MOBILE_TELEPHONE_NUMBER, contactInformation.getMobileTelephoneNumber());
                    }
                    if (null != contactInformation.getTelephoneNumber()) {
                        jgen.writeStringField(TELEPHONE_NUMBER, contactInformation.getTelephoneNumber());
                    }
                    jgen.writeEndObject();
                }
            }

            public static void printAddress(JsonGenerator jgen,
                                            ISimpleAddressEntity address)
                    throws IOException {

                if (null != address) {
                    jgen.writeFieldName(address.getAddressType());
                    jgen.writeStartObject();

                    if (null != address.getAddressLine1()) {
                        jgen.writeStringField(ADDRESS_LINE_1,
                                address.getAddressLine1());
                    }
                    if (null != address.getAddressLine2()) {
                        jgen.writeStringField(ADDRESS_LINE_2,
                                address.getAddressLine2());
                    }
                    if (null != address.getAddressLine3()) {
                        jgen.writeStringField(ADDRESS_LINE_3,
                                address.getAddressLine3());
                    }
                    if (null != address.getPostalNumber()) {
                        PostalNumber postalNumber = address.getPostalNumber();
                        if (null != postalNumber &&
                                null != postalNumber.getPostalNumber()) {
                            jgen.writeStringField(POSTAL_NUMBER,
                                    postalNumber.getPostalNumber());
                        }
                    }
                    if (null != address.getPostalTown()) {
                        jgen.writeStringField(POSTAL_TOWN,
                                address.getPostalTown());
                    }
                    if (null != address.getCountryCode()) {
                        jgen.writeStringField(COUNTRY_CODE,
                                address.getCountryCode());
                    }
                    jgen.writeEndObject();
                }
            }

            public static void printCorrespondencePartPerson(
                    JsonGenerator jgen,
                    ICorrespondencePartPersonEntity correspondencePartPerson)
                    throws IOException {
                printCorrespondencePart(jgen, correspondencePartPerson);
                printGenericPerson(jgen, correspondencePartPerson);
            }

            public static void printPartPerson(
                    JsonGenerator jgen,
                    IPartPersonEntity person)
                    throws IOException {
                printPart(jgen, person);
                printGenericPerson(jgen, person);
            }

            public static void printPartUnit(
                    JsonGenerator jgen,
                    IGenericUnitEntity unit)
                    throws IOException {
                printPart(jgen, (IPartEntity) unit);
                printGenericUnit(jgen, unit);
            }

            public static void printCorrespondencePartUnit(
                    JsonGenerator jgen,
                    IGenericUnitEntity unit)
                    throws IOException {
                printCorrespondencePart(jgen, (ICorrespondencePartEntity) unit);
                printGenericUnit(jgen, unit);
            }

            private static void printPart(JsonGenerator jgen, IPartEntity part)
                    throws IOException {
                if (part != null) {
                    printSystemIdEntity(jgen, part);
                    if (part.getPartTypeCode() != null) {
                        jgen.writeObjectFieldStart(PART_ROLE);
                        printCode(jgen, part.getPartTypeCode(),
                                part.getPartTypeCodeName());
                        jgen.writeEndObject();
                    }
                }
            }

            public static void printGenericUnit(
                    JsonGenerator jgen,
                    IGenericUnitEntity unit)
                    throws IOException {
                if (null != unit) {
                    if (null != unit.getOrganisationNumber()) {
                        jgen.writeStringField(ORGANISATION_NUMBER,
                                unit.getOrganisationNumber());
                    }
                    if (null != unit.getName()) {
                        jgen.writeStringField(NAME,
                                unit.getName());
                    }
                    if (null != unit.getBusinessAddress()) {
                        printAddress(jgen,
                                unit.getBusinessAddress().
                                        getSimpleAddress());
                    }
                    if (null != unit.getPostalAddress()) {
                        printAddress(jgen, unit.
                                getPostalAddress().getSimpleAddress());
                    }

                    if (null != unit.getContactInformation()) {
                        printContactInformation(jgen,
                                unit.getContactInformation());
                    }
                    if (null != unit.getContactPerson()) {
                        jgen.writeStringField(CONTACT_PERSON,
                                unit.getContactPerson());
                    }
                }
            }

            public static void printGenericPerson(
                    JsonGenerator jgen,
                    IGenericPersonEntity partPerson)
                    throws IOException {
                if (null != partPerson) {

                    if (null !=
                            partPerson.getSocialSecurityNumber()) {
                        jgen.writeStringField(SOCIAL_SECURITY_NUMBER,
                                partPerson.
                                        getSocialSecurityNumber());
                    }
                    if (null != partPerson.getdNumber()) {
                        jgen.writeStringField(D_NUMBER,
                                partPerson.getdNumber());
                    }
                    if (null != partPerson.getName()) {
                        jgen.writeStringField(NAME,
                                partPerson.getName());
                    }
                    if (null != partPerson.getPostalAddress()) {
                        printAddress(jgen,
                                partPerson.
                                        getPostalAddress().
                                        getSimpleAddress());
                    }
                    if (null != partPerson.getResidingAddress()) {
                        printAddress(jgen, partPerson.
                                getResidingAddress().getSimpleAddress());
                    }
                    if (null !=
                            partPerson.getContactInformation()) {
                        printContactInformation(jgen,
                                partPerson.
                                        getContactInformation());
                    }
                }
            }

            public static void printCorrespondencePartInternal(JsonGenerator jgen,
                                                               ICorrespondencePartInternalEntity correspondencePartInternal)
                    throws IOException {
                if (null != correspondencePartInternal) {
                    printCorrespondencePart(jgen, correspondencePartInternal);
                    if (null != correspondencePartInternal.getAdministrativeUnit()) {
                        jgen.writeStringField(ADMINISTRATIVE_UNIT, correspondencePartInternal.getAdministrativeUnit());
                    }
//                    if (null != correspondencePartInternal.getReferenceAdministrativeUnit()) {
//                        String systemID = correspondencePartInternal.getReferenceAdministrativeUnit().getSystemId();
//                        if (null != systemID) {
//                            jgen.writeStringField(REFERENCE_ADMINISTRATIVE_UNIT, systemID);
//                        }
//                    }
                    if (null != correspondencePartInternal.getCaseHandler()) {
                        jgen.writeStringField(CASE_HANDLER, correspondencePartInternal.getCaseHandler());
                    }
//                    if (null != correspondencePartInternal.getReferenceUser()) {
//                        String systemID = correspondencePartInternal
//                                .getReferenceUser().getSystemId();
//                        if (null != systemID) {
//                            jgen.writeStringField(REFERENCE_CASE_HANDLER, systemID);
//                        }
//                    }
                }
            }



            /*
            Temporarily out. n5v5 has a new way of dealing with correspondenceparts, but this also likely includes
            listing correspondenceparts via the parent entity CorrespondencePart
            public static void printCorrespondenceParts(JsonGenerator jgen, ICorrespondencePart correspondencePartObject)
                    throws IOException {
                List<CorrespondencePart> correspondenceParts = correspondencePartObject.getReferenceCorrespondencePart();
                if (correspondenceParts != null && correspondenceParts.size() > 0) {
                    jgen.writeArrayFieldStart(CORRESPONDENCE_PART);
                    for (CorrespondencePart correspondencePart : correspondenceParts) {
                        jgen.writeStartObject();
                        printCorrespondencePart(jgen, correspondencePart);
                        jgen.writeEndObject();
                    }
                    jgen.writeEndArray();
                }
            }
            */

            public static void printSignOff(JsonGenerator jgen, ISignOff signOffEntity)
                    throws IOException {
                List<SignOff> signOffs = signOffEntity.getReferenceSignOff();
                if (signOffs != null && signOffs.size() > 0) {
                    jgen.writeArrayFieldStart(SIGN_OFF);
                    for (SignOff signOff : signOffs) {
                        if (signOff != null) {

                            jgen.writeObjectFieldStart(SIGN_OFF);

                            if (signOff.getSignOffDate() != null) {
                                jgen.writeStringField(SIGN_OFF_DATE,
                                        formatDate(signOff.getSignOffDate()));
                            }
                            if (signOff.getSignOffBy() != null) {
                                jgen.writeStringField(SIGN_OFF_BY, signOff.getSignOffBy());
                            }
                            if (signOff.getSignOffMethod() != null) {
                                jgen.writeStringField(SIGN_OFF_METHOD, signOff.getSignOffMethod());
                            }
                            jgen.writeEndObject();
                        }
                    }
                    jgen.writeEndArray();
                }
            }

            public static void printDocumentFlow(JsonGenerator jgen, IDocumentFlow documentFlowEntity)
                    throws IOException {
                List<DocumentFlow> documentFlows = documentFlowEntity.getReferenceDocumentFlow();
                if (documentFlows != null && documentFlows.size() > 0) {
                    jgen.writeArrayFieldStart(DOCUMENT_FLOW);
                    for (DocumentFlow documentFlow : documentFlows) {
                        if (documentFlow != null) {

                            jgen.writeObjectFieldStart(DOCUMENT_FLOW);
                            if (documentFlow.getFlowTo() != null) {
                                jgen.writeStringField(DOCUMENT_FLOW_FLOW_TO, documentFlow.getFlowTo());
                            }
                            if (documentFlow.getFlowFrom() != null) {
                                jgen.writeStringField(DOCUMENT_FLOW_FLOW_FROM, documentFlow.getFlowFrom());
                            }
                            if (documentFlow.getFlowReceivedDate() != null) {
                                jgen.writeStringField(DOCUMENT_FLOW_FLOW_RECEIVED_DATE,
                                        formatDate(documentFlow.getFlowReceivedDate()));
                            }
                            if (documentFlow.getFlowSentDate() != null) {
                                jgen.writeStringField(DOCUMENT_FLOW_FLOW_SENT_DATE,
                                        formatDate(documentFlow.getFlowSentDate()));
                            }
                            if (documentFlow.getFlowStatus() != null) {
                                jgen.writeStringField(DOCUMENT_FLOW_FLOW_STATUS, documentFlow.getFlowStatus());
                            }
                            if (documentFlow.getFlowComment() != null) {
                                jgen.writeStringField(DOCUMENT_FLOW_FLOW_COMMENT, documentFlow.getFlowComment());
                            }
                            jgen.writeEndObject();
                        }
                    }
                    jgen.writeEndArray();
                }
            }

            public static void printPrecedence(JsonGenerator jgen, IPrecedenceEntity precedence) throws IOException {
                if (precedence != null) {
                    if (null != precedence.getPrecedenceDate()) {
                        jgen.writeStringField(PRECEDENCE_DATE,
                                formatDate(precedence.getPrecedenceDate()));
                    }
                    if (null != precedence.getCreatedDate()) {
                        jgen.writeStringField(CREATED_DATE,
                                formatDateTime(precedence.getCreatedDate()));
                    }
                    if (null != precedence.getCreatedBy()) {
                        jgen.writeStringField(CREATED_BY, precedence.getCreatedBy());
                    }
                    if (null != precedence.getTitle()) {
                        jgen.writeStringField(TITLE, precedence.getTitle());
                    }
                    if (null != precedence.getDescription()) {
                        jgen.writeStringField(DESCRIPTION, precedence.getDescription());
                    }
                    if (null != precedence.getPrecedenceAuthority()) {
                        jgen.writeStringField(PRECEDENCE_AUTHORITY, precedence.getPrecedenceAuthority());
                    }
                    if (null != precedence.getSourceOfLaw()) {
                        jgen.writeStringField(PRECEDENCE_SOURCE_OF_LAW, precedence.getSourceOfLaw());
                    }
                    if (null != precedence.getPrecedenceApprovedDate()) {
                        jgen.writeStringField(PRECEDENCE_APPROVED_DATE,
                                formatDate(precedence.getPrecedenceApprovedDate()));
                    }
                    if (null != precedence.getPrecedenceApprovedBy()) {
                        jgen.writeStringField(PRECEDENCE_APPROVED_BY, precedence.getPrecedenceApprovedBy());
                    }
                    if (null != precedence.getFinalisedDate()) {
                        jgen.writeStringField(FINALISED_DATE,
                                formatDateTime(precedence.getFinalisedDate()));
                    }
                    if (null != precedence.getFinalisedBy()) {
                        jgen.writeStringField(FINALISED_BY, precedence.getFinalisedBy());
                    }
                    if (null != precedence.getPrecedenceStatus()) {
                        jgen.writeStringField(PRECEDENCE_STATUS, precedence.getPrecedenceStatus());
                    }
                }
            }

            public static void printPrecedences(JsonGenerator jgen, IPrecedence precedenceObject)
                    throws IOException {
                List<Precedence> precedences = precedenceObject.getReferencePrecedence();
                if (precedences != null && precedences.size() > 0) {
                    jgen.writeArrayFieldStart(PRECEDENCE);
                    for (Precedence precedence : precedences) {
                        jgen.writeStartObject();
                        printPrecedence(jgen, precedence);
                        jgen.writeEndObject();
                    }
                    jgen.writeEndArray();
                }
            }

            public static void printAuthor(JsonGenerator jgen, IAuthor authorEntity)
                    throws IOException {
                List<Author> author = authorEntity.getReferenceAuthor();
                if (author != null && author.size() > 0) {
                    jgen.writeArrayFieldStart(AUTHOR);
                    for (Author location : author) {
                        if (location.getAuthor() != null) {
                            jgen.writeString(location.getAuthor());
                        }
                    }
                    jgen.writeEndArray();
                }
            }

            public static void printStorageLocation(JsonGenerator jgen, IStorageLocation storageLocationEntity)
                    throws IOException {
                List<StorageLocation> storageLocation = storageLocationEntity.getReferenceStorageLocation();
                if (storageLocation != null && storageLocation.size() > 0) {
                    jgen.writeArrayFieldStart(STORAGE_LOCATION);
                    for (StorageLocation location : storageLocation) {
                        if (location.getStorageLocation() != null) {
                            jgen.writeString(location.getStorageLocation());
                        }
                    }
                    jgen.writeEndArray();
                }
            }

            public static void printConversion(JsonGenerator jgen,
                                               IConversion conversionEntity)
                    throws IOException {
                List<Conversion> conversions = conversionEntity.getReferenceConversion();
                if (conversions != null && conversions.size() > 0) {
                    for (Conversion conversion : conversions) {

                        if (conversion != null) {
                            jgen.writeObjectFieldStart(CONVERSION);
                            if (conversion.getConvertedDate() != null) {
                                jgen.writeStringField(CONVERTED_DATE,
                                        formatDate(conversion.getConvertedDate()));
                            }
                            if (conversion.getConvertedBy() != null) {
                                jgen.writeStringField(CONVERTED_BY,
                                        conversion.getConvertedBy());
                            }
                            if (conversion.getConvertedFromFormat() != null) {
                                jgen.writeStringField(CONVERTED_FROM_FORMAT,
                                        conversion.getConvertedFromFormat());
                            }
                            if (conversion.getConvertedToFormat() != null) {
                                jgen.writeStringField(CONVERTED_TO_FORMAT,
                                        conversion.getConvertedToFormat());
                            }
                            if (conversion.getConversionTool() != null) {
                                jgen.writeStringField(CONVERSION_TOOL,
                                        conversion.getConversionTool());
                            }
                            if (conversion.getConversionComment() != null) {
                                jgen.writeStringField(CONVERSION_COMMENT,
                                        conversion.getConversionComment());
                            }
                            jgen.writeEndObject();
                        }
                    }
                }
            }

            public static void printFondsCreators(JsonGenerator jgen, IFondsCreator fondsCreatorObject)
                    throws IOException {

                List<FondsCreator> fondsCreators = fondsCreatorObject.getReferenceFondsCreator();
                if (fondsCreators != null) {
                    for (FondsCreator fondsCreator : fondsCreators) {
                        if (fondsCreator != null) {
                            jgen.writeObjectFieldStart(FONDS_CREATOR);
                            printFondsCreator(jgen, fondsCreator);
                            jgen.writeEndObject();
                        }
                    }
                }
            }

            public static void printFondsCreator(JsonGenerator jgen,
                                                 IFondsCreatorEntity fondsCreatorEntity)
                    throws IOException {
                if (fondsCreatorEntity != null) {
                    if (fondsCreatorEntity.getSystemId() != null) {
                        jgen.writeStringField(SYSTEM_ID, fondsCreatorEntity.getSystemId());
                    }
                    if (fondsCreatorEntity.getFondsCreatorId() != null) {
                        jgen.writeStringField(FONDS_CREATOR_ID, fondsCreatorEntity.getFondsCreatorId());
                    }
                    if (fondsCreatorEntity.getFondsCreatorName() != null) {
                        jgen.writeStringField(FONDS_CREATOR_NAME, fondsCreatorEntity.getFondsCreatorName());
                    }
                    if (fondsCreatorEntity.getDescription() != null) {
                        jgen.writeStringField(DESCRIPTION, fondsCreatorEntity.getDescription());
                    }
                }
            }

            public static void printElectronicSignature(JsonGenerator jgen,
                                                        IElectronicSignature electronicSignatureEntity)
                    throws IOException {
                ElectronicSignature electronicSignature = electronicSignatureEntity.getReferenceElectronicSignature();
                if (electronicSignature != null) {
                    jgen.writeObjectFieldStart(ELECTRONIC_SIGNATURE);
                    if (electronicSignature.getElectronicSignatureSecurityLevel() != null) {
                        jgen.writeStringField(ELECTRONIC_SIGNATURE_SECURITY_LEVEL,
                                electronicSignature.getElectronicSignatureSecurityLevel());
                    }
                    if (electronicSignature.getElectronicSignatureVerified() != null) {
                        jgen.writeStringField(ELECTRONIC_SIGNATURE_VERIFIED,
                                electronicSignature.getElectronicSignatureVerified());
                    }
                    if (electronicSignature.getVerifiedDate() != null) {
                        jgen.writeStringField(ELECTRONIC_SIGNATURE_VERIFIED_DATE,
                                formatDate(electronicSignature.getVerifiedDate()));
                    }
                    if (electronicSignature.getVerifiedBy() != null) {
                        jgen.writeStringField(ELECTRONIC_SIGNATURE_VERIFIED_BY,
                                electronicSignature.getVerifiedBy());
                    }
                    jgen.writeEndObject();
                }
            }

            public static void printClassified(JsonGenerator jgen, IClassified classifiedEntity)
                    throws IOException {
                if (classifiedEntity != null) {
                    Classified classified = classifiedEntity.getReferenceClassified();
                    if (classified != null) {
                        jgen.writeObjectFieldStart(CLASSIFIED);
                        if (classified.getClassification() != null) {
                            jgen.writeStringField(CLASSIFICATION, classified.getClassification());
                        }
                        if (classified.getClassificationDate() != null) {
                            jgen.writeStringField(CLASSIFICATION_DATE,
                                    formatDateTime(classified.getClassificationDate()));
                        }
                        if (classified.getClassificationBy() != null) {
                            jgen.writeStringField(CLASSIFICATION_BY, classified.getClassificationBy());
                        }
                        if (classified.getClassificationDowngradedDate() != null) {
                            jgen.writeStringField(CLASSIFICATION_DOWNGRADED_DATE,
                                    formatDateTime(classified.getClassificationDowngradedDate()));
                        }
                        if (classified.getClassificationDowngradedBy() != null) {
                            jgen.writeStringField(CLASSIFICATION_DOWNGRADED_BY,
                                    classified.getClassificationDowngradedBy());
                        }
                        jgen.writeEndObject();
                    }
                }
            }

            public static void printDisposal(JsonGenerator jgen, IDisposal disposalEntity)
                    throws IOException {
                if (disposalEntity != null) {
                    Disposal disposal = disposalEntity.getReferenceDisposal();
                    if (disposal != null) {
                        jgen.writeObjectFieldStart(DISPOSAL);
                        if (disposal.getDisposalDecision() != null) {
                            jgen.writeStringField(DISPOSAL_DECISION,
                                    disposal.getDisposalDecision());
                        }
                        if (disposal.getDisposalAuthority() != null) {
                            jgen.writeStringField(DISPOSAL_AUTHORITY,
                                    disposal.getDisposalAuthority());
                        }
                        if (disposal.getPreservationTime() != null) {
                            jgen.writeStringField(DISPOSAL_PRESERVATION_TIME,
                                    Integer.toString(disposal.getPreservationTime()));
                        }
                        if (disposal.getDisposalDate() != null) {
                            jgen.writeStringField(DISPOSAL_DATE,
                                    formatDate(disposal.getDisposalDate()));
                        }
                        jgen.writeEndObject();
                    }
                }
            }

            public static void printDisposalUndertaken(JsonGenerator jgen, IDisposalUndertaken disposalUndertakenEntity)
                    throws IOException {
                if (disposalUndertakenEntity != null) {
                    DisposalUndertaken disposalUndertaken = disposalUndertakenEntity.getReferenceDisposalUndertaken();
                    if (disposalUndertaken != null) {
                        jgen.writeObjectFieldStart(DISPOSAL_UNDERTAKEN);
                        if (disposalUndertaken.getDisposalBy() != null) {
                            jgen.writeStringField(DISPOSAL_UNDERTAKEN_BY, disposalUndertaken.getDisposalBy());
                        }
                        if (disposalUndertaken.getDisposalDate() != null) {
                            jgen.writeStringField(DISPOSAL_UNDERTAKEN_DATE,
                                    formatDate(disposalUndertaken.getDisposalDate()));
                        }
                        jgen.writeEndObject();
                    }
                }
            }

            public static void printDeletion(JsonGenerator jgen, IDeletion deletionEntity)
                    throws IOException {
                if (deletionEntity != null) {
                    Deletion deletion = deletionEntity.getReferenceDeletion();
                    if (deletion != null) {
                        jgen.writeObjectFieldStart(DELETION);
                        if (deletion.getDeletionBy() != null) {
                            jgen.writeStringField(DELETION_BY, deletion.getDeletionBy());
                        }
                        if (deletion.getDeletionType() != null) {
                            jgen.writeStringField(DELETION_TYPE, deletion.getDeletionType());
                        }
                        if (deletion.getDeletionDate() != null) {
                            jgen.writeStringField(DELETION_DATE,
                                    formatDate(deletion.getDeletionDate()));
                        }
                        jgen.writeEndObject();
                    }
                }
            }

            public static void printScreening(JsonGenerator jgen, IScreening screeningEntity)
                    throws IOException {
                if (screeningEntity != null) {
                    Screening screening = screeningEntity.getReferenceScreening();
                    if (screening != null) {
                        jgen.writeObjectFieldStart(SCREENING);
                        if (screening.getAccessRestriction() != null) {
                            jgen.writeStringField(SCREENING_ACCESS_RESTRICTION,
                                    screening.getAccessRestriction());
                        }
                        if (screening.getScreeningAuthority() != null) {
                            jgen.writeStringField(SCREENING_AUTHORITY,
                                    screening.getScreeningAuthority());
                        }
                        if (screening.getScreeningMetadata() != null) {
                            jgen.writeStringField(SCREENING_METADATA,
                                    screening.getScreeningMetadata());
                        }
                        if (screening.getScreeningDocument() != null) {
                            jgen.writeStringField(SCREENING_DOCUMENT,
                                    screening.getScreeningDocument());
                        }
                        if (screening.getScreeningExpiresDate() != null) {
                            jgen.writeStringField(SCREENING_EXPIRES_DATE,
                                    formatDate(screening.getScreeningExpiresDate()));
                        }
                        if (screening.getScreeningDuration() != null) {
                            jgen.writeStringField(SCREENING_DURATION,
                                    screening.getScreeningDuration());
                        }
                        jgen.writeEndObject();
                    }
                }
            }

            public static void printComment(JsonGenerator jgen, IComment commentEntity)
                    throws IOException {
                List<Comment> comments = commentEntity.getReferenceComment();
                if (comments != null && comments.size() > 0) {
                    jgen.writeArrayFieldStart(COMMENT);
                    for (Comment comment : comments) {
                        if (comment.getCommentText() != null) {
                            jgen.writeStringField(COMMENT_TEXT, comment.getCommentText());
                        }
                        if (comment.getCommentType() != null) {
                            jgen.writeStringField(COMMENT_TYPE, comment.getCommentType());
                        }
                        if (comment.getCommentDate() != null) {
                            jgen.writeStringField(COMMENT_DATE,
                                    formatDate(comment.getCommentDate()));
                        }
                        if (comment.getCommentRegisteredBy() != null) {
                            jgen.writeStringField(COMMENT_REGISTERED_BY, comment.getCommentRegisteredBy());
                        }
                    }
                    jgen.writeEndArray();
                }
            }

            public static void printCrossReferences(
                    @NotNull JsonGenerator jgen,
                    @NotNull ICrossReference crossReferences)
                    throws IOException {
                if (crossReferences.getReferenceCrossReference().size() > 0) {
                    jgen.writeArrayFieldStart(CROSS_REFERENCES);
                    for (CrossReference crossReference :
                            crossReferences.getReferenceCrossReference()) {
                        printCrossReference(jgen, crossReference);
                    }
                    jgen.writeEndArray();
                }
            }

            private static void printCrossReference(
                    @NotNull JsonGenerator jgen,
                    @NotNull ICrossReferenceEntity crossReference)
                    throws IOException {
                if (crossReference != null) {
                    jgen.writeStartObject();
                    printSystemIdEntity(jgen, crossReference);
                    jgen.writeStringField(crossReference.getReferenceType(),
                            crossReference.getToSystemId());
                    jgen.writeEndObject();
                }
            }

            public static void printKeyword(JsonGenerator jgen, IKeyword keywordEntity)
                    throws IOException {
                List<Keyword> keywords = keywordEntity.getReferenceKeyword();
                if (keywords != null && keywords.size() > 0) {
                    jgen.writeArrayFieldStart(KEYWORD);
                    for (Keyword keyword : keywords) {
                        if (keyword.getKeyword() != null) {
                            jgen.writeString(keyword.getKeyword());
                        }
                    }
                    jgen.writeEndArray();
                }
            }
        }
    }
}
