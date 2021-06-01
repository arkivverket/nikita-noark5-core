package nikita.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.net.MediaType;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.secondary.*;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.*;
import nikita.common.model.noark5.v5.interfaces.entities.*;
import nikita.common.model.noark5.v5.interfaces.entities.admin.IAdministrativeUnitEntity;
import nikita.common.model.noark5.v5.interfaces.entities.admin.IUserEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.*;
import nikita.common.model.noark5.v5.metadata.*;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NikitaMalformedHeaderException;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.util.annotation.ANationalIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.util.UUID.fromString;
import static nikita.common.config.Constants.*;
import static nikita.common.config.ErrorMessagesConstants.STRING_IS_BLANK;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpMethod.*;

public final class CommonUtils {

    // Example 2020-06-30+02:00, 2020-06-30Z
    public static final Pattern DATE_PATTERN = Pattern.compile(
            "\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])" +
                    "((\\+?\\d{2}:\\d{2}$)|(Z))");
    // Example 2020-06-30 02:00
    public static final Pattern DB_DATE_PATTERN = Pattern.compile(
            "\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])" +
                    "(\\s|\\-|\\+)?\\d{2}:\\d{2}$");
    // Example 2020-06-30T16:25:50.041651+02:00,
    // 2020-06-30T16:25:50.041651Z,
    public static final Pattern DATE_TIME_PATTERN = Pattern.compile(
            "\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|[12][0-9]|3[01])T" +
                    "(?:00|0[0-9]|1[0-9]|2[0-3]):(?:00|[0-5][0-9]):" +
                    "(?:00|[0-5][0-9]).?[0-9]*" +
                    "(?:(?:[-+]\\d{2}:\\d{2})|Z)");
    // Example 2020-06-30T16:25:50.041651+02:00
    // Does not require T and drops ms part
    public static final Pattern DB_DATE_TIME_PATTERN = Pattern.compile(
            "\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])T" +
                    "(00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):" +
                    "([0-9]|[0-5][0-9])" + "(\\.\\d\\d?\\d?\\d?\\d)?" +
                    "(\\+|\\-)\\d{2}:\\d{2}$");
    public static final Map<String, Class<?>> entityMap = new HashMap<>();
    public static final Map<String, Class<?>> natIdentMap = new HashMap<>();
    /**
     * Holds a list of servletPaths and their HTTP methods
     */
    private static final Map<String, Set<HttpMethod>>
            requestMethodMap = new HashMap<>();
    /**
     * Holds a list of mimeType and boolean value stating if we should attempt
     * to convert a file of that mimeType to an archive version
     */
    private static final Map<String, Boolean>
            mimeTypeConversionMap = new HashMap<>();
    /**
     * Holds a mapping of Norwegian entity names to English entity names
     * e.g mappe->file
     */
    private static final Map<String, ModelNames>
            nor2engEntityMap = new HashMap<>();
    /**
     * Holds a mapping of mimeTypes and their archive equivalent. The equivalent
     * contains both the equivalent mimeType and file extension.
     */
    private static final Map<String, FileExtensionAndMimeType>
            archiveVersion = new HashMap<>();

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
         */
        public static void validateUpdateNoarkEntity(
                @NotNull INoarkEntity nikitaEntity) {
            if (nikitaEntity instanceof ITitleDescription) {
                rejectIfEmptyOrWhitespace(((ITitleDescription) nikitaEntity)
                        .getDescription(), DESCRIPTION);
                rejectIfEmptyOrWhitespace(((ITitleDescription) nikitaEntity)
                        .getTitle(), TITLE);
            }
        }

        public static void rejectIfEmptyOrWhitespace(
                String stringToCheck, String attribute) {
            if (stringToCheck.isBlank()) {
                throw new NikitaMalformedInputDataException(
                        String.format(STRING_IS_BLANK, attribute));
            }
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

        public static void addClassToMap(String simpleName, Class klass) {
            entityMap.put(simpleName, klass);
        }

        public static void addClassToNatIdentMap(
                Class klass, Annotation annotation) {
            natIdentMap.put(
                    ((ANationalIdentifier) annotation).name(), klass);
        }

        public static Class getClassFromName(String className) {
            return Optional.ofNullable(entityMap.get(className))
                    .orElseThrow(() -> new BadRequestException(
                            "Unsupported Noark class: " + className));
        }

        public static Boolean mimeTypeIsConvertible(@NotNull String mimeType) {
            return mimeTypeConversionMap.containsKey(mimeType);
        }

        public static void setDefaultMimeTypesAsConvertible() {
            // TODO At the moment there seems to be a problem with
            // unoconv and I don't have time to debug this to find out
            // why ...
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
                if (split.length != 2) {
                    throw new NikitaMalformedInputDataException(
                            "OData filtering problem with URL [" + url + "]");
                }
                return ODATA_PATH + split[1];
            } else
                return url;
        }

        public static String getEnglishNameObject(String norwegianName) {
            ModelNames names = nor2engEntityMap.get(norwegianName);
            if (null != names) {
                return names.getEnglishNameObject();
            } else
                return norwegianName;
        }

        public static String getEnglishNameObjectOrThrow(String norwegianName) {
            ModelNames names = nor2engEntityMap.get(norwegianName);
            if (null == names) {
                String error = norwegianName + " is not recognised attribute";
                logger.error(error);
                throw new NikitaMalformedInputDataException(error);
            }
            return names.getEnglishNameObject();
        }

        public static String getEnglishNameDatabase(String norwegianName) {
            return nor2engEntityMap.get(norwegianName).getEnglishNameDatabase();
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
         * @param servletPath The incoming servletPath e.g. /api/arkivstruktur/
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
         * @param servletPath The servletPath
         */
        public static String getMethodsForRequestAsListOrThrow(
                @NotNull String servletPath) {
            HttpMethod[] methods = getMethodsForRequest(servletPath);
            if (null == methods) {
                String msg = "Error servletPath [" + servletPath
                        + "] has no known HTTP methods.";
                logger.error(msg);
                throw new NikitaException(msg);
            }
            return Arrays.stream(methods)
                    .map(a -> String.valueOf(a.toString()))
                    .collect(Collectors.joining(","));
        }

        /**
         * Provides the ability to throw an Exception if this call fails.
         * This is just a helper to make the code more readable in other places.
         *
         * @param servletPath The servletPath
         */

        public static HttpMethod[] getMethodsForRequestOrThrow(@NotNull String servletPath) {
            HttpMethod[] methods = getMethodsForRequest(servletPath);
            if (null == methods) {
                String msg = "Error servletPath [" +
                        servletPath + "] has no known HTTP methods.";
                logger.error(msg);
                throw new NikitaException(msg);
            }
            return methods;
        }

        /**
         * Provides the ability to throw an Exception if this call fails.
         * This is just a helper to make the code more readable in other places.
         *
         * @param servletPath The servletPath
         */
        public static HttpMethod[] getMethodsForRequest(@NotNull String servletPath) {
            // Adding a trailing slash as the map is setup with a trailing slash
            if (!servletPath.endsWith("/")) {
                servletPath += SLASH;
            }
            // Next, we have to replace the first occurrence of an
            // actual UUID with the word {systemID}, the next with
            // {subSystemID} for non-metadata entries, and {kode} or
            // {code}/{value} for metadata entries.
            String updatedServletPath;
            if (servletPath.startsWith(SLASH + HREF_BASE_METADATA + SLASH)) {
                // Anything to do with metadata, just let it through
                HttpMethod[] methods = new HttpMethod[5];
                methods[0] = GET;
                methods[1] = POST;
                methods[2] = DELETE;
                methods[3] = PUT;
                methods[4] = OPTIONS;
                return methods;
            } else {
                // The following pattern is taken from
                // https://stackoverflow.com/questions/136505/searching-for-uuids-in-text-with-regex#6640851
                Pattern pattern = Pattern.compile(
                        "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
                Matcher matcher = pattern.matcher(servletPath.toLowerCase());
                servletPath = matcher.replaceFirst(SYSTEM_ID_PARAMETER);
                matcher = pattern.matcher(servletPath.toLowerCase());
                updatedServletPath = matcher.replaceFirst(SUB_SYSTEM_ID_PARAMETER);
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

            public static Integer deserializeInteger(String fieldname,
                                                     ObjectNode objectNode,
                                                     StringBuilder errors,
                                                     boolean required) {
                JsonNode currentNode = objectNode.get(fieldname);
                if (null != currentNode) {
                    objectNode.remove(fieldname);
                    if (currentNode.isNumber()) {
                        return currentNode.intValue();
                    } else {
                        errors.append(fieldname);
                        errors.append(" (\"");
                        errors.append(currentNode.textValue());
                        errors.append("\") is not numeric. ");
                        return null;
                    }
                } else if (required) {
                    errors.append(fieldname);
                    errors.append(" is missing. ");
                }
                return null;
            }

            public static Long deserializeLong(String fieldname,
                                               ObjectNode objectNode,
                                               StringBuilder errors,
                                               boolean required) {
                JsonNode currentNode = objectNode.get(fieldname);
                if (null != currentNode) {
                    objectNode.remove(fieldname);
                    if (currentNode.isNumber()) {
                        return currentNode.longValue();
                    } else {
                        errors.append(fieldname);
                        errors.append(" (\"");
                        errors.append(currentNode.textValue());
                        errors.append("\") is not numeric. ");
                        return null;
                    }
                } else if (required) {
                    errors.append(fieldname);
                    errors.append(" is missing. ");
                }
                return null;
            }

            public static UUID deserializeUUID(String fieldname,
                                               ObjectNode objectNode,
                                               StringBuilder errors,
                                               boolean required) {
                JsonNode currentNode = objectNode.get(fieldname);
                if (null != currentNode) {
                    objectNode.remove(fieldname);
                    if (currentNode.isTextual()) {
                        try {
                            return fromString(currentNode.textValue());
                        } catch (IllegalArgumentException e) {
                            errors.append(fieldname);
                            errors.append(" (\"");
                            errors.append(currentNode.textValue());
                            errors.append("\") is not valid UUID.");
                            return null;
                        }
                    } else {
                        errors.append(fieldname);
                        errors.append(" (\"");
                        errors.append(currentNode.textValue());
                        errors.append("\") is not numeric. ");
                        return null;
                    }
                } else if (required) {
                    errors.append(fieldname);
                    errors.append(" is missing. ");
                }
                return null;
            }

            public static OffsetDateTime deserializeDate(String date) {
                DateTimeFormatter dateFormatter;
                if (date.endsWith("Z")) {
                    dateFormatter = new DateTimeFormatterBuilder().appendPattern(
                            "yyyy-MM-ddX")
                            .parseDefaulting(HOUR_OF_DAY, 0)
                            .toFormatter();
                } else {
                    dateFormatter = new DateTimeFormatterBuilder()
                            .append(ISO_OFFSET_DATE)
                            // default values for hour and minute
                            .parseDefaulting(HOUR_OF_DAY, 0)
                            .parseDefaulting(MINUTE_OF_HOUR, 0)
                            .toFormatter();
                }
                ZonedDateTime zonedDateTime =
                        ZonedDateTime.parse(date, dateFormatter);
                if (null == zonedDateTime) {
                    String error = "Could not deserialise " + date;
                    throw new NikitaMalformedInputDataException(error);
                }
                return zonedDateTime.toOffsetDateTime();
            }

            public static OffsetDateTime deserializeDate(String fieldname,
                                                         ObjectNode objectNode,
                                                         StringBuilder errors) {
                return deserializeDate(fieldname, objectNode, errors, false);
            }

            public static OffsetDateTime deserializeDate(String fieldname,
                                                         ObjectNode objectNode,
                                                         StringBuilder errors,
                                                         boolean required) {
                OffsetDateTime d = null;
                JsonNode currentNode = objectNode.get(fieldname);
                if (null != currentNode) {
                    try {
                        d = deserializeDate(currentNode.textValue());
                    } catch (DateTimeParseException e) {
                        errors.append("Malformed ");
                        errors.append(fieldname);
                        errors.append(". Make sure format is either ");
                        errors.append(NOARK_DATE_FORMAT_PATTERN + " or ");
                        errors.append(NOARK_ZONED_DATE_FORMAT_PATTERN + ". ");
                        errors.append("Message is '");
                        errors.append(e.getMessage());
                        errors.append("'. ");
                    }
                    objectNode.remove(fieldname);
                } else if (required) {
                    errors.append(fieldname);
                    errors.append(" is missing. ");
                }
                return d;
            }

            public static OffsetDateTime deserializeDateTime(String dateTime) {
                return parse(dateTime, ISO_OFFSET_DATE_TIME);
            }


            public static OffsetDateTime deserializeDateTime(String fieldname,
                                                             ObjectNode objectNode,
                                                             StringBuilder errors,
                                                             boolean required) {
                OffsetDateTime d = null;
                JsonNode currentNode = objectNode.get(fieldname);
                if (null != currentNode) {
                    try {
                        d = deserializeDateTime(currentNode.textValue());
                    } catch (DateTimeParseException e) {
                        errors.append("Malformed ");
                        errors.append(fieldname);
                        errors.append(". Make sure format is ");
                        errors.append(NOARK_DATE_TIME_FORMAT_PATTERN + " or ");
                        errors.append(NOARK_ZONED_DATE_TIME_FORMAT_PATTERN + ". ");
                        errors.append("Message is '");
                        errors.append(e.getMessage());
                        errors.append("'. ");
                    }
                    objectNode.remove(fieldname);
                } else if (required) {
                    errors.append(fieldname);
                    errors.append(" is missing. ");
                }
                return d;
            }

            public static OffsetDateTime deserializeDateTime(String fieldname,
                                                             ObjectNode objectNode,
                                                             StringBuilder errors) {
                return deserializeDateTime(fieldname, objectNode, errors, false);
            }

            public static IMetadataEntity
            deserialiseMetadataValue(ObjectNode objectNode,
                                     String parentname,
                                     IMetadataEntity entity,
                                     StringBuilder errors,
                                     Boolean required) {
                JsonNode metadataNode = objectNode.get(parentname);
                if (null != metadataNode) {
                    deserialiseMetadataValue(
                            metadataNode, parentname, entity, errors);
                    if (null != entity.getCode()) {
                        objectNode.remove(parentname);
                    }
                } else if (required) {
                    errors.append(parentname);
                    errors.append(" is missing. ");
                }
                return entity;
            }

            public static IMetadataEntity
            deserialiseMetadataValueWithoutParent(
                    @NotNull JsonNode metadataNode,
                    @NotNull IMetadataEntity entity,
                    @NotNull StringBuilder errors) {
                return deserialiseMetadataValue(metadataNode,
                        "MetadataValueWithoutParent", entity, errors);
            }

            public static IMetadataEntity deserialiseMetadataValue(
                    JsonNode metadataNode, String parentname,
                    IMetadataEntity entity, StringBuilder errors) {
                JsonNode node = metadataNode.get(CODE);
                if (null != node) {
                    entity.setCode(node.textValue());
                } else {
                    errors.append(parentname);
                    errors.append(".");
                    errors.append(CODE);
                    errors.append(" is missing. ");
                }
                node = metadataNode.get(CODE_NAME);
                if (null != node) {
                    entity.setCodeName(node.textValue());
                }
                return entity;
            }

            public static void deserialiseDocumentMedium(IDocumentMedium documentMediumEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize documentMedium
                DocumentMedium documentMedium = (DocumentMedium)
                        deserialiseMetadataValue(objectNode,
                                DOCUMENT_MEDIUM,
                                new DocumentMedium(),
                                errors, false);
                documentMediumEntity.setDocumentMedium(documentMedium);
            }

            public static void deserialiseNoarkSystemIdEntity(
                    ISystemId noarkSystemIdEntity,
                    ObjectNode objectNode) {
                // Deserialize systemId
                JsonNode currentNode = objectNode.get(SYSTEM_ID);
                if (null != currentNode) {
                    noarkSystemIdEntity.setSystemId(
                            fromString(currentNode.textValue()));
                    objectNode.remove(SYSTEM_ID);
                }
            }


            public static void deserialiseNoarkMetadataEntity(
                    IMetadataEntity metadataEntity, ObjectNode objectNode) {
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

            public static void deserialiseCaseStatus(ICaseFileEntity caseFile,
                                                     ObjectNode objectNode, StringBuilder errors) {
                CaseStatus caseStatus = (CaseStatus)
                        deserialiseMetadataValue(objectNode,
                                CASE_STATUS,
                                new CaseStatus(),
                                errors, false);
                caseFile.setCaseStatus(caseStatus);
            }

            public static void deserialiseKeyword(IKeyword keywordEntity,
                                                  ObjectNode objectNode) {
                // Deserialize keyword
                JsonNode currentNode = objectNode.get(KEYWORD);
                if (null != currentNode) {
                    if (currentNode.isArray()) {
                        currentNode.iterator();
                        for (JsonNode node : currentNode) {
                            String keywordText = node.textValue();
                            Keyword keyword = new Keyword();
                            keyword.setKeyword(keywordText);
                            keywordEntity.addKeyword(keyword);
                        }
                    }
                    objectNode.remove(KEYWORD);
                }
            }

            private static void deserialiseCorrespondencePartType(
                    ICorrespondencePartEntity correspondencePart,
                    ObjectNode objectNode, StringBuilder errors) {
                // Deserialize korrespondanseparttype
                CorrespondencePartType entity = (CorrespondencePartType)
                        deserialiseMetadataValue(objectNode,
                                CORRESPONDENCE_PART_TYPE,
                                new CorrespondencePartType(),
                                errors, true);
                correspondencePart.setCorrespondencePartType(entity);
            }

            private static void deserialisePartRole(
                    IPartEntity part,
                    ObjectNode objectNode, StringBuilder errors) {
                // Deserialize partrole
                PartRole entity = (PartRole)
                        deserialiseMetadataValue(objectNode,
                                PART_ROLE_FIELD,
                                new PartRole(),
                                errors, true);
                part.setPartRole(entity);
            }

            public static void deserialiseClassificationSystemType(
                    IClassificationSystemEntity classificationSystem,
                    ObjectNode objectNode, StringBuilder errors) {
                ClassificationType classificationType = (ClassificationType)
                        deserialiseMetadataValue(objectNode,
                                CLASSIFICATION_SYSTEM_TYPE,
                                new ClassificationType(),
                                errors, false);
                classificationSystem
                        .setClassificationType(classificationType);
            }

            public static void deserialiseStorageLocation(
                    IStorageLocation storageLocationEntity,
                    ObjectNode objectNode) {
                // Deserialize storageLocation
                JsonNode currentNode = objectNode.get(STORAGE_LOCATION);
                if (null != currentNode) {
                    if (currentNode.isArray()) {
                        currentNode.iterator();
                        for (JsonNode node : currentNode) {
                            String location = node.textValue();
                            StorageLocation storageLocation =
                                    new StorageLocation();
                            storageLocation.setStorageLocation(location);
                            storageLocationEntity.addReferenceStorageLocation(
                                    storageLocation);
                        }
                    }
                    objectNode.remove(STORAGE_LOCATION);
                }
            }

            public static void deserialiseScreeningMetadataLocal(
                    IScreeningEntity screeningEntity,
                    JsonNode objectNode, StringBuilder errors) {
                // Deserialize screeningMetadata
                if (objectNode.isArray()) {
                    for (JsonNode node : objectNode) {
                        ScreeningMetadataLocal screeningMetadataLocal =
                                (ScreeningMetadataLocal)
                                        deserialiseMetadataValueWithoutParent(
                                                node.deepCopy(),
                                                new ScreeningMetadataLocal(),
                                                errors);
                        screeningEntity.addReferenceScreeningMetadata(
                                screeningMetadataLocal);
                    }

                }
            }

            public static void deserialiseNoarkCreateEntity(ICreate noarkCreateEntity,
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

            /*
             * Deserialize to make sure GET + modify + PUT work.
             */
            public static void deserialiseNoarkLastModifiedEntity(
                    ILastModified nikitaEntity,
                    ObjectNode objectNode, StringBuilder errors) {
                JsonNode currentNode = objectNode.get(LAST_MODIFIED_DATE);
                if (null != currentNode) {
                    nikitaEntity.setLastModifiedDate(deserializeDateTime(
                            LAST_MODIFIED_DATE, objectNode, errors));
                    objectNode.remove(LAST_MODIFIED_DATE);
                }
                currentNode = objectNode.get(LAST_MODIFIED_BY);
                if (null != currentNode) {
                    nikitaEntity.setLastModifiedBy(currentNode.textValue());
                    objectNode.remove(LAST_MODIFIED_BY);
                }
            }

            public static void deserialiseNoarkFinaliseEntity(IFinalise finaliseEntity,
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

            public static void deserialiseNoarkTitleDescriptionEntity(ITitleDescription
                                                                              titleDescriptionEntity,
                                                                      ObjectNode objectNode, StringBuilder errors) {
                // Deserialize title
                JsonNode currentNode = objectNode.get(TITLE);
                if (null != currentNode) {
                    titleDescriptionEntity.setTitle(currentNode.textValue());
                    objectNode.remove(TITLE);
                } else {
                    errors.append(TITLE + " is missing. ");
                }
                // Deserialize description
                currentNode = objectNode.get(DESCRIPTION);
                if (null != currentNode) {
                    titleDescriptionEntity.setDescription(currentNode.textValue());
                    objectNode.remove(DESCRIPTION);
                }
            }

            public static void deserialiseSystemIdEntity(
                    ISystemId systemIdEntity, ObjectNode objectNode,
                    StringBuilder errors) {
                deserialiseNoarkSystemIdEntity(
                        systemIdEntity, objectNode);
                deserialiseNoarkCreateEntity(
                        systemIdEntity, objectNode, errors);
                deserialiseNoarkLastModifiedEntity(
                        systemIdEntity, objectNode, errors);
            }

            public static void deserialiseNoarkGeneralEntity(INoarkGeneralEntity noarkGeneralEntity, ObjectNode objectNode, StringBuilder errors) {
                deserialiseSystemIdEntity(noarkGeneralEntity, objectNode, errors);
                deserialiseNoarkFinaliseEntity(noarkGeneralEntity, objectNode, errors);
                deserialiseNoarkTitleDescriptionEntity(noarkGeneralEntity, objectNode, errors);
            }

            public static String checkNodeObjectEmpty(JsonNode objectNode) {
                StringBuilder result = new StringBuilder();
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
                        objectNode);
                JsonNode currentNode = objectNode.get(REFERENCE_TO_CLASS);
                if (null != currentNode) {
                    crossReferenceEntity.setReferenceType(REFERENCE_TO_CLASS);
                    crossReferenceEntity.setToSystemId(fromString(
                            currentNode.textValue()));
                    objectNode.remove(REFERENCE_TO_CLASS);
                }
                currentNode = objectNode.get(REFERENCE_TO_REGISTRATION);
                if (null != currentNode) {
                    crossReferenceEntity.setReferenceType(
                            REFERENCE_TO_REGISTRATION);
                    crossReferenceEntity.setToSystemId(fromString(
                            currentNode.textValue()));
                    objectNode.remove(REFERENCE_TO_REGISTRATION);
                }
                currentNode = objectNode.get(REFERENCE_TO_FILE);
                if (null != currentNode) {
                    crossReferenceEntity.setReferenceType(REFERENCE_TO_FILE);
                    crossReferenceEntity.setToSystemId(fromString(
                            currentNode.textValue()));
                    objectNode.remove(REFERENCE_TO_FILE);
                }
                objectNode.remove(CROSS_REFERENCE);
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
                disposalEntity.setPreservationTime
                        (deserializeInteger(DISPOSAL_PRESERVATION_TIME,
                                objectNode, errors, false));
                // Deserialize disposalDate
                disposalEntity.setDisposalDate(
                        deserializeDateTime(DISPOSAL_DATE, objectNode, errors));
            }

            public static DisposalUndertaken deserialiseDisposalUndertaken(
                    ObjectNode objectNode, StringBuilder errors) {
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
                JsonNode currentNode = objectNode.get(DELETION);
                if (null != currentNode
                        && !currentNode.equals(NullNode.getInstance())) {
                    deletion = new Deletion();
                    ObjectNode deletionObjectNode = currentNode.deepCopy();
                    deserialiseDeletionEntity(deletion, deletionObjectNode,
                            errors);
                    if (0 == deletionObjectNode.size()) {
                        objectNode.remove(DELETION);
                    }
                } else if (null != currentNode) { // Remove NullNode
                    objectNode.remove(DELETION);
                }
                return deletion;
            }

            public static void deserialiseDeletionEntity(IDeletionEntity deletionEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize deletionBy
                JsonNode currentNode = objectNode.get(DELETION_BY);
                if (null != currentNode
                        && !currentNode.equals(NullNode.getInstance())) {
                    deletionEntity.setDeletionBy(currentNode.textValue());
                    objectNode.remove(DELETION_BY);
                } else {
                    errors.append(DELETION_BY + " is missing. ");
                }
                // TODO referanseSlettetAv
                // Deserialize deletionType
                DeletionType entity = (DeletionType)
                        deserialiseMetadataValue(objectNode,
                                DELETION_TYPE,
                                new DeletionType(),
                                errors, true);
                deletionEntity.setDeletionType(entity);
                // Deserialize deletionDate
                deletionEntity.setDeletionDate(deserializeDateTime(DELETION_DATE, objectNode, errors));
            }

            public static void deserialiseAdministrativeUnitEntity(
                    IAdministrativeUnitEntity administrativeUnit,
                    ObjectNode objectNode, StringBuilder errors) {
                if (null != administrativeUnit) {
                    deserialiseNoarkSystemIdEntity(administrativeUnit, objectNode);
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
                                fromString(currentNode.textValue()));
                        parent.getReferenceChildAdministrativeUnit().add((AdministrativeUnit) administrativeUnit);
                        administrativeUnit.setParentAdministrativeUnit(parent);
                        objectNode.remove(ADMINISTRATIVE_UNIT_PARENT_REFERENCE);
                    }
                }
            }

            public static void deserialiseUserEntity(
                    IUserEntity user, ObjectNode objectNode,
                    StringBuilder errors) {
                // TODO implement
            }

            public static void deserialiseContactInformationEntity(
                    IContactInformationEntity contactInformation,
                    ObjectNode objectNode) {
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
                    String addressType,
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
                    } else {
                        errors.append(addressType);
                        errors.append(".");
                        errors.append(POSTAL_TOWN);
                        errors.append(" is missing. ");
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
                deserialiseGenericPersonEntity(partPerson, objectNode);
                // Deserialize postalAddress
                JsonNode currentNode = objectNode.get(POSTAL_ADDRESS);
                if (null != currentNode) {
                    PostalAddress postalAddress = new PostalAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(POSTAL_ADDRESS,
                            simpleAddress, currentNode.deepCopy(), errors);
                    postalAddress.setSimpleAddress(simpleAddress);
                    postalAddress.setReferenceCorrespondencePartPerson(
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
                    deserialiseSimpleAddressEntity(RESIDING_ADDRESS,
                            simpleAddress, currentNode.deepCopy(), errors);
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
                            contactInformation, currentNode.deepCopy());
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
                deserialiseGenericPersonEntity(partPersonEntity, objectNode);
                // TODO : Ugly hack as code evolved where metadataentity
                // and nikitaentity suddenly no longer are the same
                // just to keep things moving ...
                ((INoarkGeneralEntity) partPersonEntity).setTitle("TEMP: REMOVE ME!");
                // Deserialize postalAddress
                JsonNode currentNode = objectNode.get(POSTAL_ADDRESS);
                if (null != currentNode) {
                    PostalAddress postalAddress = new PostalAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(POSTAL_ADDRESS,
                            simpleAddress, currentNode.deepCopy(), errors);
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
                    deserialiseSimpleAddressEntity(RESIDING_ADDRESS,
                            simpleAddress, currentNode.deepCopy(), errors);
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
                            contactInformation, currentNode.deepCopy());
                    partPersonEntity.
                            setContactInformation(contactInformation);
                    objectNode.remove(CONTACT_INFORMATION);
                }
            }

            public static void deserialisePartUnitEntity(
                    IPartUnitEntity partUnit,
                    ObjectNode objectNode, StringBuilder errors) {
                deserialisePartRole(partUnit, objectNode, errors);
                // TODO : Ugly hack as code evolved where metadataentity
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
                currentNode = objectNode.get(UNIT_IDENTIFIER);
                if (null != currentNode) {
                    JsonNode node = currentNode.get(ORGANISATION_NUMBER);
                    if (null != node) {
                        partUnit.setUnitIdentifier(node.textValue());
                        // This remove() call is placed inside block
                        // to report error if no organisasjonsnummer
                        // was found.
                        objectNode.remove(UNIT_IDENTIFIER);
                    }
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
                    deserialiseSimpleAddressEntity(POSTAL_ADDRESS,
                            simpleAddress, currentNode.deepCopy(), errors);
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
                    deserialiseSimpleAddressEntity(BUSINESS_ADDRESS,
                            simpleAddress, currentNode.deepCopy(), errors);
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
                            contactInformation, currentNode.deepCopy());
                    partUnit.setContactInformation(contactInformation);
                    objectNode.remove(CONTACT_INFORMATION);
                }
            }

            public static void deserialiseGenericPersonEntity(
                    IGenericPersonEntity person, ObjectNode objectNode) {
                JsonNode currentNode = objectNode.get(PERSON_IDENTIFIER);
                if (null != currentNode) {
                    // Deserialize foedselsnummer
                    JsonNode node = currentNode.get(SOCIAL_SECURITY_NUMBER);
                    if (null != node) {
                        person.setSocialSecurityNumber(node.textValue());
                    }
                    // Deserialize dnummer
                    node = currentNode.get(D_NUMBER_FIELD);
                    if (null != node) {
                        person.setdNumber(node.textValue());
                    }
                    objectNode.remove(PERSON_IDENTIFIER);
                }
                // Deserialize navn
                currentNode = objectNode.get(NAME);
                if (null != currentNode) {
                    person.setName(currentNode.textValue());
                    objectNode.remove(NAME);
                }
            }


            public static void deserialiseCorrespondencePartInternalEntity(
                    ICorrespondencePartInternalEntity
                            correspondencePartInternal,
                    ObjectNode objectNode, StringBuilder errors) {
                deserialiseCorrespondencePartType(
                        correspondencePartInternal, objectNode, errors);
                // Deserialize administrativEnhet
                JsonNode currentNode =
                        objectNode.get(ADMINISTRATIVE_UNIT_FIELD);
                if (null != currentNode) {
                    correspondencePartInternal.setAdministrativeUnit(
                            currentNode.textValue());
                    objectNode.remove(ADMINISTRATIVE_UNIT_FIELD);
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
                // Deserialize enhetsidentifikator
                JsonNode currentNode = objectNode.get(UNIT_IDENTIFIER);
                // { "enhetsidentifikator": { "organisasjonsnummer": "123458"}}
                if (null != currentNode) {
                    currentNode = currentNode.get(ORGANISATION_NUMBER);
                    if (null != currentNode) {
                        correspondencePartUnit.setUnitIdentifier(
                                currentNode.textValue());
                    }
                    objectNode.remove(UNIT_IDENTIFIER);
                }
                // Deserialize kontaktperson
                currentNode = objectNode.get(CONTACT_PERSON);
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
                // Deserialize postadresse
                currentNode = objectNode.get(POSTAL_ADDRESS);
                if (null != currentNode) {
                    PostalAddress postalAddressEntity = new PostalAddress();
                    SimpleAddress simpleAddress = new SimpleAddress();
                    deserialiseSimpleAddressEntity(POSTAL_ADDRESS,
                            simpleAddress, currentNode.deepCopy(), errors);
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
                    deserialiseSimpleAddressEntity(BUSINESS_ADDRESS,
                            simpleAddress, currentNode.deepCopy(), errors);
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
                            contactInformation, currentNode.deepCopy());
                    correspondencePartUnit.
                            setContactInformation(contactInformation);
                    objectNode.remove(CONTACT_INFORMATION);
                }
            }

            public static void deserialiseFondsCreator(IFondsCreatorEntity fondsCreatorEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize fondsCreatorId
                JsonNode currentNode = objectNode.get(FONDS_CREATOR_ID);
                if (null != currentNode) {
                    fondsCreatorEntity.setFondsCreatorId(currentNode.textValue());
                    objectNode.remove(FONDS_CREATOR_ID);
                } else {
                    errors.append(FONDS_CREATOR_ID + " is missing. ");
                }
                // Deserialize fondsCreatorName
                currentNode = objectNode.get(FONDS_CREATOR_NAME);
                if (null != currentNode) {
                    fondsCreatorEntity.setFondsCreatorName(currentNode.textValue());
                    objectNode.remove(FONDS_CREATOR_NAME);
                } else {
                    errors.append(FONDS_CREATOR_NAME + " is missing. ");
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
                if (null != screeningNode
                        && !screeningNode.equals(NullNode.getInstance())) {
                    screening = new Screening();
                    ObjectNode screeningObjectNode = screeningNode.deepCopy();
                    deserialiseScreeningEntity(screening, screeningObjectNode,
                            errors);
                    if (0 == screeningObjectNode.size()) {
                        objectNode.remove(SCREENING);
                    }
                } else { // Remove NullNode
                    objectNode.remove(SCREENING);
                }
                return screening;
            }

            public static void deserialiseScreeningEntity(
                    IScreeningEntity screeningEntity, ObjectNode objectNode,
                    StringBuilder errors) {
                // Deserialize accessRestriction
                AccessRestriction accessRestriction = (AccessRestriction)
                        deserialiseMetadataValue(objectNode,
                                SCREENING_ACCESS_RESTRICTION,
                                new AccessRestriction(),
                                errors, true);
                screeningEntity.setAccessRestriction(accessRestriction);
                // Deserialize screeningAuthority
                JsonNode currentNode = objectNode.get(SCREENING_AUTHORITY);
                if (null != currentNode) {
                    screeningEntity.setScreeningAuthority(currentNode.textValue());
                    objectNode.remove(SCREENING_AUTHORITY);
                } else {
                    errors.append(SCREENING_AUTHORITY + " is missing. ");
                }
                // Deserialize screeningMetadata
                currentNode = objectNode.get(SCREENING_SCREENING_METADATA);
                if (null != currentNode) {
                    deserialiseScreeningMetadataLocal(screeningEntity,
                            currentNode, errors);
                    objectNode.remove(SCREENING_SCREENING_METADATA);
                }
                // Deserialize screeningDocument
                ScreeningDocument screeningDocument = (ScreeningDocument)
                        deserialiseMetadataValue(objectNode,
                                SCREENING_SCREENING_DOCUMENT,
                                new ScreeningDocument(),
                                errors, false);
                screeningEntity.setScreeningDocument(screeningDocument);
                // Deserialize screeningExpiresDate
                screeningEntity.setScreeningExpiresDate(
                        deserializeDateTime(SCREENING_EXPIRES_DATE, objectNode, errors));
                // Deserialize screeningDuration
                screeningEntity.setScreeningDuration
                        (deserializeInteger(SCREENING_DURATION,
                                objectNode, errors, false));
            }

            public static Classified deserialiseClassified(ObjectNode objectNode, StringBuilder errors) {
                Classified classified = null;
                JsonNode classifiedNode = objectNode.get(CLASSIFIED);
                if (null != classifiedNode
                        && !classifiedNode.equals(NullNode.getInstance())) {
                    classified = new Classified();
                    ObjectNode classifiedObjectNode = classifiedNode.deepCopy();
                    deserialiseClassifiedEntity(classified,
                            classifiedObjectNode, errors);
                    if (0 == classifiedObjectNode.size()) {
                        objectNode.remove(CLASSIFIED);
                    }
                } else if (null != classifiedNode) { // Remove NullNode
                    objectNode.remove(CLASSIFIED);
                }
                return classified;
            }

            public static void deserialiseClassifiedEntity(IClassifiedEntity classifiedEntity, ObjectNode objectNode, StringBuilder errors) {
                // Deserialize classification
                ClassifiedCode classifiedCode = (ClassifiedCode)
                        deserialiseMetadataValue(objectNode,
                                CLASSIFICATION,
                                new ClassifiedCode(),
                                errors, true);
                classifiedEntity.setClassification(classifiedCode);
                // Deserialize classificationDate
                classifiedEntity.setClassificationDate(deserializeDateTime(CLASSIFICATION_DATE, objectNode, errors));
                // Deserialize classificationBy
                JsonNode currentNode = objectNode.get(CLASSIFICATION_BY);
                if (null != currentNode) {
                    classifiedEntity.setClassificationBy(currentNode.textValue());
                    objectNode.remove(CLASSIFICATION_BY);
                }
                // Deserialize classificationDowngradedDate
                classifiedEntity.setClassificationDowngradedDate(deserializeDateTime(CLASSIFICATION_DOWNGRADED_DATE,
                        objectNode, errors));
                // Deserialize
                currentNode = objectNode.get(CLASSIFICATION_DOWNGRADED_BY);
                if (null != currentNode) {
                    classifiedEntity.setClassificationDowngradedBy(currentNode.textValue());
                    objectNode.remove(CLASSIFICATION_DOWNGRADED_BY);
                }
            }

            public static ElectronicSignature deserialiseElectronicSignature(
                    ObjectNode objectNode, StringBuilder errors) {
                ElectronicSignature es = null;
                JsonNode esNode = objectNode.get(ELECTRONIC_SIGNATURE);
                if (null != esNode
                        && !esNode.equals(NullNode.getInstance())) {
                    es = new ElectronicSignature();
                    ObjectNode esObjectNode = esNode.deepCopy();
                    deserialiseElectronicSignatureEntity(es, esObjectNode,
                            errors);
                    if (0 == esObjectNode.size()) {
                        objectNode.remove(ELECTRONIC_SIGNATURE);
                    }
                } else if (null != esNode) { // Remove NullNode
                    objectNode.remove(ELECTRONIC_SIGNATURE);
                }
                return es;
            }

            public static ElectronicSignature deserialiseElectronicSignatureEntity(
                    ElectronicSignature electronicSignature,
                    ObjectNode objectNode, StringBuilder errors) {
                // Deserialise elektroniskSignaturSikkerhetsnivaa
                ElectronicSignatureSecurityLevel essLevel =
                        (ElectronicSignatureSecurityLevel)
                                deserialiseMetadataValue(objectNode,
                                        ELECTRONIC_SIGNATURE_SECURITY_LEVEL_FIELD,
                                        new ElectronicSignatureSecurityLevel(),
                                        errors, true);
                electronicSignature
                        .setElectronicSignatureSecurityLevel(essLevel);
                // Deserialise elektroniskSignaturVerifisert
                ElectronicSignatureVerified esVerified =
                        (ElectronicSignatureVerified)
                                deserialiseMetadataValue(objectNode,
                                        ELECTRONIC_SIGNATURE_VERIFIED_FIELD,
                                        new ElectronicSignatureVerified(),
                                        errors, true);
                electronicSignature.setElectronicSignatureVerified(esVerified);
                // Deserialise verifisertDato
                JsonNode currentNode =
                        objectNode.get(ELECTRONIC_SIGNATURE_VERIFIED_DATE);
                if (null != currentNode) {
                    electronicSignature.setVerifiedDate(
                            deserializeDateTime(currentNode.textValue(),
                                    objectNode, errors));
                    objectNode.remove(ELECTRONIC_SIGNATURE_VERIFIED_DATE);
                } else {
                    errors.append(ELECTRONIC_SIGNATURE
                            + "." + ELECTRONIC_SIGNATURE_VERIFIED_DATE
                            + " is missing. ");
                }
                // Deserialise verifisertAv
                currentNode = objectNode.get(ELECTRONIC_SIGNATURE_VERIFIED_BY);
                if (null != currentNode) {
                    electronicSignature.setVerifiedBy(currentNode.textValue());
                    objectNode.remove(ELECTRONIC_SIGNATURE_VERIFIED_BY);
                } else {
                    errors.append(ELECTRONIC_SIGNATURE
                            + "." + ELECTRONIC_SIGNATURE_VERIFIED_BY
                            + " is missing. ");
                }
                return electronicSignature;
            }
        }

        public static final class Serialize {
            public static String formatDate(OffsetDateTime value) {
                return value.format(ISO_OFFSET_DATE);
            }

            public static String formatDateTime(OffsetDateTime value) {
                return value.format(ISO_OFFSET_DATE_TIME);
            }

            public static void printNullable(JsonGenerator jgen,
                                             String fieldName,
                                             String value)
                    throws IOException {
                if (null != value)
                    jgen.writeStringField(fieldName, value);
            }

            public static void printNullable(JsonGenerator jgen,
                                             String fieldName,
                                             Integer value)
                    throws IOException {
                if (null != value)
                    jgen.writeNumberField(fieldName, value);
            }

            public static void printNullable(JsonGenerator jgen,
                                             String fieldName,
                                             Long value)
                    throws IOException {
                if (null != value)
                    jgen.writeNumberField(fieldName, value);
            }

            public static void printNullable(JsonGenerator jgen,
                                             String fieldName,
                                             Double value)
                    throws IOException {
                if (null != value)
                    jgen.writeNumberField(fieldName, value);
            }

            public static void printNullable(JsonGenerator jgen,
                                             String fieldName,
                                             Boolean value)
                    throws IOException {
                if (null != value)
                    jgen.writeBooleanField(fieldName, value);
            }

            public static void printNullableDate(JsonGenerator jgen,
                                                 String fieldName,
                                                 OffsetDateTime value)
                    throws IOException {
                if (null != value)
                    jgen.writeStringField(fieldName, formatDate(value));
            }

            public static void printNullableDateTime(JsonGenerator jgen,
                                                     String fieldName,
                                                     OffsetDateTime value)
                    throws IOException {
                if (null != value)
                    jgen.writeStringField(fieldName, formatDateTime(value));
            }

            public static void printTitleAndDescription(JsonGenerator jgen,
                                                        ITitleDescription titleDescriptionEntity)
                    throws IOException {
                if (titleDescriptionEntity != null) {
                    printNullable(jgen, TITLE,
                            titleDescriptionEntity.getTitle());
                    printNullable(jgen, DESCRIPTION,
                            titleDescriptionEntity.getDescription());
                }
            }

            public static void printCreateEntity(JsonGenerator jgen,
                                                 ICreate createEntity)
                    throws IOException {
                if (createEntity != null) {
                    printNullableDateTime(jgen, CREATED_DATE,
                            createEntity.getCreatedDate());
                    printNullable(jgen, CREATED_BY,
                            createEntity.getCreatedBy());
                }
            }

            public static void printClassificationSystemEntity(
                    JsonGenerator jgen,
                    IClassificationSystemEntity classificationSystem)
                    throws IOException {
                printSystemIdEntity(jgen, classificationSystem);
                printTitleAndDescription(jgen, classificationSystem);
                printNullableMetadata
                        (jgen, CLASSIFICATION_SYSTEM_TYPE,
                                classificationSystem.getClassificationType());
            }

            public static void printFileEntity(JsonGenerator jgen,
                                               IFileEntity file)
                    throws IOException {
                printSystemIdEntity(jgen, file);
                printStorageLocation(jgen, file);
                printNullable(jgen, FILE_ID, file.getFileId());
                printTitleAndDescription(jgen, file);
                printNullable(jgen, FILE_PUBLIC_TITLE, file.getPublicTitle());
            }

            public static void printCaseFileEntity(JsonGenerator jgen,
                                                   ICaseFileEntity caseFile)
                    throws IOException {
                printCreateEntity(jgen, caseFile);
                printFinaliseEntity(jgen, caseFile);
                if (caseFile.getCaseYear() != null) {
                    jgen.writeNumberField(CASE_YEAR,
                            caseFile.getCaseYear());
                }
                if (caseFile.getCaseSequenceNumber() != null) {
                    jgen.writeNumberField(CASE_SEQUENCE_NUMBER,
                            caseFile.getCaseSequenceNumber());
                }
                printNullableDateTime(jgen, CASE_DATE, caseFile.getCaseDate());
                printNullable(jgen, CASE_RESPONSIBLE,
                        caseFile.getCaseResponsible());
                printNullable(jgen, CASE_RECORDS_MANAGEMENT_UNIT,
                        caseFile.getRecordsManagementUnit());
                printNullableMetadata(jgen, CASE_STATUS,
                        caseFile.getCaseStatus());
                printNullableDateTime(jgen, CASE_LOANED_DATE,
                        caseFile.getLoanedDate());
                printNullable(jgen, CASE_LOANED_TO,
                        caseFile.getLoanedTo());
            }

            public static void printCode(
                    JsonGenerator jgen, String code, String name)
                    throws IOException {
                jgen.writeStringField(CODE, code);
                if (name != null) {
                    jgen.writeStringField(CODE_NAME, name);
                }
            }

            public static void printCode
                    (JsonGenerator jgen, IMetadataEntity metadataEntity)
                    throws IOException {
                printCode(jgen, metadataEntity.getCode(),
                        metadataEntity.getCodeName());
            }

            public static void printNullableMetadata
                    (JsonGenerator jgen, String fieldName, IMetadataEntity m)
                    throws IOException {
                if (null != m && null != m.getCode()) {
                    jgen.writeObjectFieldStart(fieldName);
                    printCode(jgen, m);
                    jgen.writeEndObject();
                }
            }

            public static void printNullableMetadata
                    (JsonGenerator jgen, String fieldName, IMetadataEntity m,
                     boolean printFieldName)
                    throws IOException {
                if (printFieldName) {
                    printNullableMetadata(jgen, fieldName, m);
                } else if (null != m && null != m.getCode()) {
                    jgen.writeStartObject(fieldName);
                    printCode(jgen, m);
                    jgen.writeEndObject();
                }
            }

            public static void printRecordEntity(JsonGenerator jgen,
                                                 IRecordEntity record)
                    throws IOException {
                if (record != null) {
                    printSystemIdEntity(jgen, record);
                    printCreateEntity(jgen, record);
                    printNullableDateTime(jgen, RECORD_ARCHIVED_DATE,
                            record.getArchivedDate());
                    printNullable(jgen, RECORD_ARCHIVED_BY,
                            record.getArchivedBy());
                    printNullableDateTime(jgen, RECORD_ARCHIVED_DATE,
                            record.getArchivedDate());
                    printNullable(jgen, RECORD_ARCHIVED_BY,
                            record.getArchivedBy());
                    printNullable(jgen, RECORD_ID,
                            record.getRecordId());
                    printTitleAndDescription(jgen, record);
                    printNullable(jgen, FILE_PUBLIC_TITLE,
                            record.getPublicTitle());
                }
            }

            public static void printRecordNoteEntity(
                    JsonGenerator jgen, IRecordNoteEntity recordNote)
                    throws IOException {
                if (recordNote != null) {
                    printNullableDateTime(jgen, REGISTRY_ENTRY_DOCUMENT_DATE,
                            recordNote.getDocumentDate());
                    printNullableDateTime(jgen, REGISTRY_ENTRY_RECEIVED_DATE,
                            recordNote.getReceivedDate());
                    printNullableDateTime(jgen, REGISTRY_ENTRY_SENT_DATE,
                            recordNote.getSentDate());
                    printNullableDateTime(jgen, REGISTRY_ENTRY_DUE_DATE,
                            recordNote.getDueDate());
                    printNullableDateTime(jgen,
                            REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE,
                            recordNote.getFreedomAssessmentDate());
                    if (recordNote.getNumberOfAttachments() != null) {
                        jgen.writeNumberField(REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS,
                                recordNote.getNumberOfAttachments());
                    }
                    printNullableDateTime(jgen, CASE_LOANED_DATE,
                            recordNote.getLoanedDate());
                    printNullable(jgen, CASE_LOANED_TO,
                            recordNote.getLoanedTo());
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
                    printNullableMetadata(jgen, REGISTRY_ENTRY_TYPE,
                            registryEntry.getRegistryEntryType());
                    printNullableMetadata(jgen, REGISTRY_ENTRY_STATUS,
                            registryEntry.getRegistryEntryStatus());
                    printNullableDateTime(jgen, REGISTRY_ENTRY_DATE,
                            registryEntry.getRecordDate());
                    printNullable(jgen, CASE_RECORDS_MANAGEMENT_UNIT,
                            registryEntry.getRecordsManagementUnit());
                }
            }

            public static void printDocumentMedium(JsonGenerator jgen,
                                                   IDocumentMedium documentMedium)
                    throws IOException {
                printNullableMetadata
                        (jgen, DOCUMENT_MEDIUM,
                                documentMedium.getDocumentMedium());
            }

            public static void printFinaliseEntity(JsonGenerator jgen,
                                                   IFinalise finaliseEntity)
                    throws IOException {
                printNullable(jgen, FINALISED_BY,
                        finaliseEntity.getFinalisedBy());
                printNullableDateTime(jgen, FINALISED_DATE,
                        finaliseEntity.getFinalisedDate());
            }

            public static void printModifiedEntity(JsonGenerator jgen,
                                                   ILastModified lastModified)
                    throws IOException {
                printNullableDateTime(jgen, LAST_MODIFIED_DATE,
                        lastModified.getLastModifiedDate());
                printNullable(jgen, LAST_MODIFIED_BY,
                        lastModified.getLastModifiedBy());
            }

            public static void printSystemIdEntity(
                    JsonGenerator jgen, ISystemId systemIdEntity)
                    throws IOException {
                if (systemIdEntity != null &&
                        systemIdEntity.getSystemId() != null) {
                    jgen.writeStringField(SYSTEM_ID,
                            systemIdEntity.getSystemIdAsString());
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
                    printCode(jgen, metadataEntity);
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
                    printCode(jgen, metadataEntity);
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
                    printNullableMetadata
                            (jgen, CORRESPONDENCE_PART_TYPE,
                                    correspondencePart.getCorrespondencePartType());
                }
            }

            public static void printContactInformation(JsonGenerator jgen, IContactInformationEntity contactInformation)
                    throws IOException {
                if (null != contactInformation) {
                    jgen.writeFieldName(CONTACT_INFORMATION);
                    jgen.writeStartObject();
                    printNullable(jgen, EMAIL_ADDRESS,
                            contactInformation.getEmailAddress());
                    printNullable(jgen, MOBILE_TELEPHONE_NUMBER,
                            contactInformation.getMobileTelephoneNumber());
                    printNullable(jgen, TELEPHONE_NUMBER,
                            contactInformation.getTelephoneNumber());
                    jgen.writeEndObject();
                }
            }

            public static void printAddress(JsonGenerator jgen,
                                            ISimpleAddressEntity address)
                    throws IOException {
                if (null != address) {
                    jgen.writeFieldName(address.getAddressType());
                    jgen.writeStartObject();
                    printNullable(jgen, ADDRESS_LINE_1,
                            address.getAddressLine1());
                    printNullable(jgen, ADDRESS_LINE_2,
                            address.getAddressLine2());
                    printNullable(jgen, ADDRESS_LINE_3,
                            address.getAddressLine3());
                    if (null != address.getPostalNumber()) {
                        PostalNumber postalNumber = address.getPostalNumber();
                        if (null != postalNumber &&
                                null != postalNumber.getPostalNumber()) {
                            jgen.writeStringField(POSTAL_NUMBER,
                                    postalNumber.getPostalNumber());
                        }
                    }
                    printNullable(jgen, POSTAL_TOWN,
                            address.getPostalTown());
                    printNullable(jgen, COUNTRY_CODE,
                            address.getCountryCode());
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
                    printNullableMetadata(jgen, PART_ROLE_FIELD,
                            part.getPartRole());
                }
            }

            public static void printGenericUnit(
                    JsonGenerator jgen,
                    IGenericUnitEntity unit)
                    throws IOException {
                if (null != unit) {
                    if (null != unit.getUnitIdentifier()) {
                        jgen.writeObjectFieldStart(UNIT_IDENTIFIER);
                        jgen.writeStringField(ORGANISATION_NUMBER,
                                unit.getUnitIdentifier());
                        jgen.writeEndObject();
                    }
                    printNullable(jgen, NAME, unit.getName());
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
                    printNullable(jgen, CONTACT_PERSON,
                            unit.getContactPerson());
                }
            }

            public static void printGenericPerson(
                    JsonGenerator jgen,
                    IGenericPersonEntity partPerson)
                    throws IOException {
                if (null != partPerson) {
                    String ssn = partPerson.getSocialSecurityNumber();
                    String dnumber = partPerson.getdNumber();
                    if (null != ssn || null != dnumber) {
                        jgen.writeObjectFieldStart(PERSON_IDENTIFIER);
                        printNullable(jgen, SOCIAL_SECURITY_NUMBER, ssn);
                        printNullable(jgen, D_NUMBER_FIELD, dnumber);
                        jgen.writeEndObject();
                    }
                    printNullable(jgen, NAME, partPerson.getName());
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
                    printNullable
                            (jgen, ADMINISTRATIVE_UNIT_FIELD,
                                    correspondencePartInternal.getAdministrativeUnit());
//                    if (null != correspondencePartInternal.getReferenceAdministrativeUnit()) {
//                        UUID systemID = correspondencePartInternal.getReferenceAdministrativeUnit().getSystemId();
//                        if (null != systemID) {
//                            jgen.writeStringField(REFERENCE_ADMINISTRATIVE_UNIT, systemID);
//                        }
//                    }
                    printNullable
                            (jgen, CASE_HANDLER,
                                    correspondencePartInternal.getCaseHandler());
//                    if (null != correspondencePartInternal.getReferenceUser()) {
//                        UUID systemID = correspondencePartInternal
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

            public static void printStorageLocation(
                    JsonGenerator jgen, IStorageLocation storageLocationEntity)
                    throws IOException {
                Set<StorageLocation> storageLocation = storageLocationEntity
                        .getReferenceStorageLocation();
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

            public static void printFondsCreators(
                    JsonGenerator jgen, IFondsCreator fondsCreatorObject)
                    throws IOException {
                Set<FondsCreator> fondsCreators =
                        fondsCreatorObject.getReferenceFondsCreator();
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
                    printNullable(jgen, SYSTEM_ID,
                            fondsCreatorEntity.getSystemIdAsString());
                    printNullable(jgen, FONDS_CREATOR_ID,
                            fondsCreatorEntity.getFondsCreatorId());
                    printNullable(jgen, FONDS_CREATOR_NAME,
                            fondsCreatorEntity.getFondsCreatorName());
                    printNullable(jgen, DESCRIPTION,
                            fondsCreatorEntity.getDescription());
                }
            }

            public static void printElectronicSignature(JsonGenerator jgen,
                                                        IElectronicSignature esEntity)
                    throws IOException {
                ElectronicSignature es =
                        esEntity.getReferenceElectronicSignature();
                if (es != null) {
                    jgen.writeObjectFieldStart(ELECTRONIC_SIGNATURE);
                    printNullable(jgen, ELECTRONIC_SIGNATURE_VERIFIED_BY,
                            es.getVerifiedBy());
                    printNullableDateTime(jgen, ELECTRONIC_SIGNATURE_VERIFIED_DATE,
                            es.getVerifiedDate());
                    printNullableMetadata
                            (jgen, ELECTRONIC_SIGNATURE_SECURITY_LEVEL_FIELD,
                                    es.getElectronicSignatureSecurityLevel());
                    printNullableMetadata
                            (jgen, ELECTRONIC_SIGNATURE_VERIFIED_FIELD,
                                    es.getElectronicSignatureVerified());
                    printNullableDateTime(jgen, ELECTRONIC_SIGNATURE_VERIFIED_DATE,
                            es.getVerifiedDate());
                    jgen.writeEndObject();
                }
            }

            public static void printBSM(JsonGenerator jgen, IBSM bsm)
                    throws IOException {
                if (bsm != null && bsm.getReferenceBSMBase() != null
                        && bsm.getReferenceBSMBase().size() > 0) {
                    jgen.writeObjectFieldStart(BSM_DEF);
                    for (BSMBase bsmBase : bsm.getReferenceBSMBase()) {
                        switch (bsmBase.getDataType()) {
                            case TYPE_STRING:
                                printNullable(jgen, bsmBase.getValueName(),
                                        bsmBase.getStringValue());
                                break;
                            case TYPE_URI:
                                printNullable(jgen, bsmBase.getValueName(),
                                        bsmBase.getUriValue());
                                break;
                            case TYPE_INTEGER:
                                printNullable(jgen, bsmBase.getValueName(),
                                        bsmBase.getIntegerValue());
                                break;
                            case TYPE_DOUBLE:
                                printNullable(jgen, bsmBase.getValueName(),
                                        bsmBase.getDoubleValue());
                                break;
                            case TYPE_BOOLEAN:
                                printNullable(jgen, bsmBase.getValueName(),
                                        bsmBase.getBooleanValue());
                                break;
                            case TYPE_DATE:
                                printNullableDate(jgen, bsmBase.getValueName(),
                                        bsmBase.getDateTimeValue());
                                break;
                            case TYPE_DATE_TIME:
                                printNullableDateTime(jgen, bsmBase.getValueName(),
                                        bsmBase.getDateTimeValue());
                                break;
                        }
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
                        printNullableMetadata
                                (jgen, CLASSIFICATION,
                                        classified.getClassification());
                        printNullableDateTime
                                (jgen, CLASSIFICATION_DATE,
                                        classified.getClassificationDate());
                        printNullable(jgen, CLASSIFICATION_BY,
                                classified.getClassificationBy());
                        printNullableDateTime
                                (jgen, CLASSIFICATION_DOWNGRADED_DATE,
                                        classified.getClassificationDowngradedDate());
                        printNullable
                                (jgen, CLASSIFICATION_DOWNGRADED_BY,
                                        classified.getClassificationDowngradedBy());
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
                        printNullable(jgen, DISPOSAL_DECISION,
                                disposal.getDisposalDecision());
                        printNullable(jgen, DISPOSAL_AUTHORITY,
                                disposal.getDisposalAuthority());
                        printNullable(jgen, DISPOSAL_PRESERVATION_TIME,
                                disposal.getPreservationTime());
                        printNullableDateTime(jgen, DISPOSAL_DATE,
                                disposal.getDisposalDate());
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
                        printNullable(jgen, DISPOSAL_UNDERTAKEN_BY,
                                disposalUndertaken.getDisposalBy());
                        printNullableDate
                                (jgen, DISPOSAL_UNDERTAKEN_DATE,
                                        disposalUndertaken.getDisposalDate());
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
                        printNullable(jgen, DELETION_BY,
                                deletion.getDeletionBy());
                        printNullableMetadata(jgen, DELETION_TYPE,
                                deletion.getDeletionType());
                        printNullableDateTime(jgen, DELETION_DATE,
                                deletion.getDeletionDate());
                        jgen.writeEndObject();
                    }
                }
            }

            public static void printScreening(JsonGenerator jgen,
                                              Screening screening)
                    throws IOException {
                if (screening != null) {
                    printNullableMetadata(jgen, SCREENING_ACCESS_RESTRICTION,
                            screening.getAccessRestriction());
                    printNullable(jgen, SCREENING_AUTHORITY,
                            screening.getScreeningAuthority());
                    printScreeningMetadata(jgen,
                            screening.getReferenceScreeningMetadata());
                    printNullableMetadata(jgen, SCREENING_SCREENING_DOCUMENT,
                            screening.getScreeningDocument());
                    printNullableDateTime(jgen, SCREENING_EXPIRES_DATE,
                            screening.getScreeningExpiresDate());
                    printNullable(jgen, SCREENING_DURATION,
                            screening.getScreeningDuration());
                }
            }

            public static void printScreening(JsonGenerator jgen,
                                              IScreening screeningEntity)
                    throws IOException {
                if (null != screeningEntity && null !=
                        screeningEntity.getReferenceScreening()) {
                    jgen.writeObjectFieldStart(SCREENING);
                    printScreening(jgen,
                            screeningEntity.getReferenceScreening());
                    jgen.writeEndObject();
                }
            }

            public static void printScreeningMetadata(
                    JsonGenerator jgen,
                    Set<ScreeningMetadataLocal> screeningMetadataSet)
                    throws IOException {
                if (screeningMetadataSet.size() > 0) {
                    jgen.writeArrayFieldStart(SCREENING_METADATA);
                    for (ScreeningMetadataLocal screeningMetadata :
                            screeningMetadataSet) {
                        printScreeningMetadata(jgen, screeningMetadata);
                    }
                    jgen.writeEndArray();
                }
            }

            public static void printScreeningMetadata(
                    @NotNull JsonGenerator jgen,
                    @NotNull ScreeningMetadataLocal screeningMetadata)
                    throws IOException {
                printNullableMetadata(jgen, SCREENING_SCREENING_DOCUMENT,
                        screeningMetadata, false);
            }


            public static void printCrossReferences(
                    @NotNull JsonGenerator jgen,
                    @NotNull ICrossReference crossReferences)
                    throws IOException {
                if (crossReferences.getReferenceCrossReference().size() > 0) {
                    jgen.writeArrayFieldStart(CROSS_REFERENCES);
                    for (CrossReference crossReference :
                            crossReferences.getReferenceCrossReference()) {
                        jgen.writeStartObject();
                        printCrossReference(jgen, crossReference);
                        jgen.writeEndObject();
                    }
                    jgen.writeEndArray();
                }
            }

            public static void printCrossReference(
                    @NotNull JsonGenerator jgen,
                    @NotNull ICrossReferenceEntity crossReference)
                    throws IOException {
                if (crossReference != null) {
                    printSystemIdEntity(jgen, crossReference);
                    if (null != crossReference.getFromSystemId()) {
                        jgen.writeStringField(FROM_SYSTEM_ID,
                                crossReference.getFromSystemId().toString());
                    }
                    if (null != crossReference.getToSystemId()) {
                        jgen.writeStringField(TO_SYSTEM_ID,
                                crossReference.getToSystemId().toString());
                    }
                    if (null != crossReference.getReferenceType()) {
                        jgen.writeStringField(REFERENCE_TYPE,
                                crossReference.getReferenceType());
                    }
                }
            }

            public static void printKeyword(JsonGenerator jgen, IKeyword keywordEntity)
                    throws IOException {
                Set<Keyword> keywords = keywordEntity.getReferenceKeyword();
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
