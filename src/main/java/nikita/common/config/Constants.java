package nikita.common.config;

import static nikita.common.config.N5ResourceMappings.*;

/**
 * Application constants.
 */
public final class Constants {

    public static final int TITLE_LENGTH = 5000;
    public static final int DESCRIPTION_LENGTH = 5000;

    public static final String NEW = "ny";
    public static final String DASH = "-";
    public static final String SUB = "under";
    public static final String PARENT = "over";
    public static final String DM_OWNED_BY = "ownedBy";
    public static final String SYSTEM = "system";

    // Spring profile for development and production
    public static final String SPRING_PROFILE_SWAGGER = "swagger";
    public static final String SPRING_PROFILE_METRICS = "metrics";

    public static final String REGEX_UUID = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4" +
            "}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

    // Definition of Authorities / Roles
    // Equivalent of the Arkivansvarlig role
    public static final String ROLE_RECORDS_MANAGER = "RECORDS_MANAGER";
    // Equivalent of the Arkivar role
    public static final String ROLE_RECORDS_KEEPER = "RECORDS_KEEPER";
    // Equivalent of the Saksbehandler role
    public static final String ROLE_CASE_HANDLER = "CASE_HANDLER";
    // Equivalent of the Leder role
    public static final String ROLE_LEADER = "LEADER";
    // Not part of standard, basically a guest that has limited access
    public static final String ROLE_GUEST = "GUEST";

    // Names of APIs that the core supports
    public static final String HATEOAS_API_PATH = "api";

    public static final String SLASH = "/";
    public static final String LEFT_PARENTHESIS = "{";
    public static final String RIGHT_PARENTHESIS = "}";
    public static final String ENTITY_ROOT_NAME_LIST = "results";
    public static final String ENTITY_ROOT_NAME_LIST_COUNT = "count";

    public static final String NEW_ANYTHING = NEW + DASH + "**";
    public static final String ANYTHING = "*";

    public static final String DELETE_RESPONSE = "{}";

    // Create for new arkivstruktur objects
    public static final String NEW_FONDS = NEW + DASH + FONDS;
    public static final String NEW_FONDS_CREATOR = NEW + DASH + FONDS_CREATOR;
    public static final String NEW_SERIES = NEW + DASH + SERIES;
    public static final String NEW_CLASSIFICATION_SYSTEM = NEW + DASH + CLASSIFICATION_SYSTEM;
    public static final String NEW_SECONDARY_CLASSIFICATION_SYSTEM = NEW + DASH + CLASSIFICATION_SYSTEM;
    public static final String NEW_CLASS = NEW + DASH + CLASS;
    public static final String NEW_FILE = NEW + DASH + FILE;
    public static final String NEW_RECORD = NEW + DASH + RECORD;
    public static final String NEW_DOCUMENT_DESCRIPTION = NEW + DASH + DOCUMENT_DESCRIPTION;
    public static final String NEW_DOCUMENT_OBJECT = NEW + DASH + DOCUMENT_OBJECT;
    public static final String NEW_REFERENCE_SERIES = NEW + DASH + REFERENCE_SERIES;

    public static final String PERSON = "person";
    public static final String UNIT = "enhet";
    public static final String INTERNAL = "intern";

    public static final String NEW_PART = NEW + DASH + PART;

    // Other arkivstruktur commands
    public static final String FILE_END = "avslutt-mappe";
    public static final String FILE_EXPAND_TO_CASE_FILE = "utvid-til-" + CASE_FILE;
    public static final String FILE_EXPAND_TO_MEETING_FILE = "utvid-til-" + MEETING_FILE;
    public static final String NEW_COMMENT = NEW + DASH + COMMENT;
    public static final String PARENT_FILE = PARENT + FILE;
    public static final String SUB_FILE = SUB + FILE;
    public static final String NEW_CROSS_REFERENCE = NEW + DASH + CROSS_REFERENCE;
    public static final String REFERENCE_NEW_SERIES = NEW + DASH + "referanseArkivdel";
    public static final String NEW_STORAGE_LOCATION = NEW + DASH + STORAGE_LOCATION;
    public static final String NEW_CONVERSION = NEW + DASH + CONVERSION;
    public static final String NEW_SIGN_OFF = NEW + DASH + SIGN_OFF;
    public static final String NEW_DOCUMENT_FLOW = NEW + DASH + DOCUMENT_FLOW;
    public static final String NEW_CORRESPONDENCE_PART = NEW + DASH + CORRESPONDENCE_PART;
    public static final String NEW_CORRESPONDENCE_PART_PERSON = NEW_CORRESPONDENCE_PART + PERSON;
    public static final String NEW_CORRESPONDENCE_PART_UNIT = NEW_CORRESPONDENCE_PART + UNIT;
    public static final String NEW_CORRESPONDENCE_PART_INTERNAL = NEW_CORRESPONDENCE_PART + INTERNAL;
    public static final String NEW_PART_PERSON = NEW_PART + PERSON;
    public static final String NEW_PART_UNIT = NEW_PART + UNIT;
    public static final String NEW_SERIES_SUCCESSOR = NEW + DASH + SERIES_SUCCESSOR;
    public static final String NEW_SERIES_PRECURSOR = NEW + DASH + SERIES_PRECURSOR;

    public static final String DOCUMENT_FILE = "fil";

    public static final String REFERENCE_TO = "referanseTil";
    public static final String REFERENCE_TO_CLASS =
            REFERENCE_TO + CLASS.substring(0, 1).toUpperCase() +
                    CLASS.substring(1);

    public static final String REFERENCE_TO_FILE =
            REFERENCE_TO + FILE.substring(0, 1).toUpperCase() +
                    FILE.substring(1);

    public static final String REFERENCE_TO_REGISTRATION =
            REFERENCE_TO + RECORD.substring(0, 1).toUpperCase() +
                    RECORD.substring(1);

    // Create for new casehandling objects
    public static final String NEW_CASE_FILE = NEW + DASH + CASE_FILE;
    public static final String NEW_REGISTRY_ENTRY = NEW + DASH + REGISTRY_ENTRY;
    public static final String NEW_RECORD_NOTE = NEW + DASH + RECORD_NOTE;
    public static final String NEW_PRECEDENCE = NEW + DASH + PRECEDENCE;

    public static final String NEW_CASE_STATUS = NEW + DASH + CASE_STATUS;
    public static final String NEW_SECONDARY_CLASSIFICATION =
	NEW + DASH + SECONDARY_CLASSIFICATION;

    // Create for new metadata objects
    public static final String NEW_DOCUMENT_MEDIUM = NEW + DASH +
            DOCUMENT_MEDIUM;
    public static final String NEW_FONDS_STATUS = NEW + DASH + FONDS_STATUS;
    public static final String NEW_SERIES_STATUS = NEW + DASH + SERIES_STATUS;

    public static final String NEW_DOCUMENT_STATUS = NEW + DASH +
            DOCUMENT_STATUS;

    public static final String NEW_REGISTRY_ENTRY_STATUS = NEW + DASH +
            REGISTRY_ENTRY_STATUS;

    public static final String NEW_PRECEDENCE_STATUS = NEW + DASH +
            PRECEDENCE_STATUS;

    public static final String NEW_SIGN_OFF_METHOD = NEW + DASH +
            SIGN_OFF_METHOD;

    public static final String NEW_ELECTRONIC_SIGNATURE_SECURITY_LEVEL = NEW +
            DASH + ELECTRONIC_SIGNATURE_SECURITY_LEVEL;

    public static final String NEW_ELECTRONIC_SIGNATURE_VERIFIED = NEW +
            DASH + ELECTRONIC_SIGNATURE_VERIFIED;

    public static final String NEW_FORMAT = NEW + DASH + FORMAT;

    public static final String NEW_FLOW_STATUS = NEW + DASH + FLOW_STATUS;

    public static final String NEW_REGISTRY_ENTRY_TYPE = NEW + DASH +
            REGISTRY_ENTRY_TYPE;

    public static final String NEW_PART_ROLE = NEW + DASH +
            PART_ROLE;

    public static final String NEW_FILE_TYPE = NEW + DASH + FILE_TYPE;

    public static final String NEW_VARIANT_FORMAT = NEW + DASH + VARIANT_FORMAT;

    public static final String NEW_CLASSIFICATION_TYPE =
            NEW + DASH + CLASSIFICATION_TYPE;

    public static final String NEW_CLASSIFIED_CODE =
            NEW + DASH + CLASSIFIED_CODE;

    public static final String NEW_COMMENT_TYPE = NEW + DASH + COMMENT_TYPE;

    public static final String NEW_DOCUMENT_TYPE = NEW + DASH +
            DOCUMENT_TYPE;

    public static final String NEW_ASSOCIATED_WITH_RECORD_AS =
            NEW + DASH + ASSOCIATED_WITH_RECORD_AS;

    public static final String NEW_ACCESS_RESTRICTION =
            NEW + DASH + ACCESS_RESTRICTION;

    public static final String NEW_DISPOSAL_DECISION =
            NEW + DASH + DISPOSAL_DECISION;

    public static final String NEW_CORRESPONDENCE_PART_TYPE = NEW + DASH +
            CORRESPONDENCE_PART_TYPE;

    public static final String NEW_ACCESS_CATEGORY =
	NEW + DASH + ACCESS_CATEGORY;

    public static final String NEW_DELETION_TYPE =
	NEW + DASH + DELETION_TYPE;

    public static final String NEW_EVENT_TYPE =
	NEW + DASH + EVENT_TYPE;

    public static final String NEW_COORDINATE_SYSTEM =
	NEW + DASH + COORDINATE_SYSTEM;

    // String used for default / template object
    public static final String DEFAULT_TITLE = "Default title for ";
    public static final String DEFAULT_DESCRIPTION = "This object, if " +
            "persisted, will be associated with ";
    public static final String DEFAULT_CASE_STATUS_CODE = "R";

    // Some user identifiers used for testing
    public static final String TEST_USER_CASE_HANDLER_1 = "example test user case handler 1";
    public static final String TEST_USER_CASE_HANDLER_2 = "example test user case handler 2";
    public static final String TEST_USER_ADMIN = "example test user admin";
    public static final String TEST_USER_RECORD_KEEPER = "example test user record keeper";

    // Some strings used during testing
    public static final String TEST_TITLE = "example test title";
    public static final String TEST_DESCRIPTION = "example test description";
    public static final String TEST_ADMINISTRATIVE_UNIT = "example test administrative unit";
    public static final String TEST_REGISTRY_ENTRY_STATUS_CODE = "J";
    public static final String TEST_REGISTRY_ENTRY_TYPE_CODE = "I";

    public static final String NOARK_DATE_FORMAT_PATTERN = "yyyy-MM-dd+HH:mm";
    public static final String NOARK_ZONED_DATE_FORMAT_PATTERN = "yyyy-MM-ddZ";
    public static final String NOARK_TIME_FORMAT_PATTERN = "HH:mm:ss";
    public static final String NOARK_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss+HH:mm";
    public static final String NOARK_ZONED_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final String REFERENCE_FILE = "referanseFil";
    public static final String CONVERT_FILE = "konverterFil";

    // namespace definition
    public static final String NOARK_VERSION = "noark5/v5";
    public static final String BASE_REL = "https://rel.arkivverket.no/";

    public static final String NOARK_BASE_REL =
            BASE_REL + NOARK_VERSION + "/api/";
    public static final String NIKITA_CONFORMANCE_REL =
            "https://nikita.arkivlab.no/" + NOARK_VERSION + "/";

    public static final String NOARK_FONDS_STRUCTURE_PATH = "arkivstruktur";
    public static final String NOARK_METADATA_PATH = "metadata";
    public static final String NOARK_ADMINISTRATION_PATH = "admin";
    public static final String NOARK_CASE_HANDLING_PATH = "sakarkiv";
    public static final String NOARK_LOGGING_PATH = "loggingogsporing";
    public static final String ODATA_PATH = "/odata";
    public static final int ODATA_OFFSET_LENGTH =
            (NOARK_FONDS_STRUCTURE_PATH + SLASH).length();
    public static final String LOGIN_REL_PATH = "login";
    public static final String LOGOUT_REL_PATH = "logout";
    public static final String LOGIN_PATH = "oauth/token";
    public static final String LOGOUT_PATH = "oauth/revoke-token";
    public static final String CHECK_TOKEN_PATH = "oauth/check_token";
    // Logging using JWT / RFC7519
    public static final String LOGIN_JWT = "rfc7519";
    public static final String LOGIN_OAUTH = "rfc6749";
    public static final String LOGIN_OAUTH2_PATH = "/oauth/token";

    public static final String NOARK5_V5_CONTENT_TYPE_JSON =
            "application/vnd.noark5+json";

    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final String INFO_CANNOT_FIND_OBJECT = "Cannot find object of type ";
    public static final String INFO_CANNOT_ASSOCIATE_WITH_CLOSED_OBJECT = "Cannot associate with a closed object";
    public static final String INFO_INVALID_STRUCTURE = "Invalid Noark structure";

    public static final String TEMPLATE_FONDS_STATUS_CODE = "Opprettet";
    public static final String TEMPLATE_FONDS_STATUS_NAME =
            "Arkivet er opprettet og i aktiv bruk";

    public static final String TEMPLATE_SERIES_STATUS_CODE = "O";

    public static final String TEMPLATE_DOCUMENT_MEDIUM_CODE =
            "Elektronisk arkiv";
    public static final String TEMPLATE_DOCUMENT_MEDIUM_NAME =
            "Bare elektroniske dokumenter";
    public static final String TEMPLATE_DOCUMENT_STATUS_CODE = "B";
    public static final String TEMPLATE_DOCUMENT_STATUS_NAME =
            "Dokumentet er under redigering";
    public static final String TEMPLATE_DOCUMENT_TYPE_CODE = "B";
    public static final String TEMPLATE_DOCUMENT_TYPE_NAME =
            "Brev";

    public static final String TEMPLATE_REGISTRY_ENTRY_STATUS_CODE = "J";
    public static final String TEMPLATE_REGISTRY_ENTRY_STATUS_NAME =
            "Journalført";

    public static final String TEMPLATE_DOCUMENT_FLOW_FLOW_STATUS_CODE = "I";

    public static final String TEMPLATE_PRECEDENCE_STATUS_CODE = "G";
    public static final String TEMPLATE_PRECEDENCE_STATUS_NAME =
            "Gjeldende";

    public static final String TEMPLATE_SIGN_OFF_METHOD_CODE = "BE";
    public static final String TEMPLATE_SIGN_OFF_METHOD_NAME =
            "Besvart med e-post";

    public static final String
            TEMPLATE_ELECTRONIC_SIGNATURE_SECURITY_LEVEL_CODE = "SK";
    public static final String
            TEMPLATE_ELECTRONIC_SIGNATURE_SECURITY_LEVEL_NAME =
            "Symmetrisk kryptert";

    public static final String
            TEMPLATE_ELECTRONIC_SIGNATURE_VERIFIED_CODE = "I";
    public static final String
            TEMPLATE_ELECTRONIC_SIGNATURE_VERIFIED_NAME =
            "Signatur påført, ikke verifisert";

    public static final String TEMPLATE_FORMAT_CODE = "RA-PDF";
    public static final String TEMPLATE_FORMAT_NAME = "PDF/A - ISO " +
            "19005-1:2005";

    public static final String TEMPLATE_FLOW_STATUS_CODE = "G";
    public static final String TEMPLATE_FLOW_STATUS_NAME = "Godkjent";

    public static final String TEMPLATE_PART_ROLE_CODE = "KLI";
    public static final String TEMPLATE_PART_ROLE_NAME = "Klient";

    public static final String TEMPLATE_FILE_TYPE_CODE = "S";
    public static final String TEMPLATE_FILE_TYPE_NAME = "Saksmappe";

    public static final String TEMPLATE_VARIANT_FORMAT_CODE = "P";
    public static final String TEMPLATE_VARIANT_FORMAT_NAME =
            "Produksjonsformat";

    public static final String TEMPLATE_CASE_STATUS_CODE = "R";
    public static final String TEMPLATE_CASE_STATUS_NAME =
            "Opprettet av saksbehandler";

    public static final String TEMPLATE_COUNTRY_CODE = "NO";
    public static final String TEMPLATE_COUNTRY_NAME = "Norge";

    public static final String TEMPLATE_POST_CODE_CODE = "0001";
    public static final String TEMPLATE_POST_CODE_NAME =
            "OSLO";

    public static final String TEMPLATE_CLASSIFICATION_TYPE_CODE = "FH";
    public static final String TEMPLATE_CLASSIFICATION_TYPE_NAME =
            "Funksjonsbasert, hierarkisk";

    public static final String TEMPLATE_CLASSIFIED_CODE_CODE = "SH";
    public static final String TEMPLATE_CLASSIFIED_CODE_NAME =
            "Strengt hemmelig (sikkerhetsgrad)";

    public static final String TEMPLATE_COMMENT_TYPE_CODE = "MS";
    public static final String TEMPLATE_COMMENT_TYPE_NAME =
            "Merknad fra saksbehandler";

    public static final String TEMPLATE_REGISTRY_ENTRY_TYPE_CODE = "I";
    public static final String TEMPLATE_REGISTRY_ENTRY_TYPE_NAME =
            "Inngående dokument";

    public static final String TEMPLATE_REGISTRY_ENTRY_STATUS = "Journalført";
    public static final String TEMPLATE_REGISTRY_ENTRY_TYPE = "Journalført";

    public static final String TEMPLATE_SCREENING_METADATA_CODE = "H";
    public static final String TEMPLATE_SCREENING_METADATA_NAME =
            "Skjerming av hele dokumentet";
    public static final String TEMPLATE_SCREENING_DOCUMENT_CODE = "D";
    public static final String TEMPLATE_SCREENING_DOCUMENT_NAME =
            "Skjerming av deler av dokumentet";


    public static final int UUIDLength = 32;

    // Messages used for API description
    public static final String API_MESSAGE_OBJECT_ALREADY_PERSISTED = "object already persisted.";
    public static final String API_MESSAGE_OBJECT_UPDATED =
            " object successfully updated";
    public static final String API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED = "object successfully created.";
    public static final String API_MESSAGE_UNAUTHENTICATED_USER = "Unauthenticated. User has not provided necessary credentials to carry out the request.";
    public static final String API_MESSAGE_UNAUTHORISED_FOR_USER = "Unauthorised. User does not have necessary rights to carry out the request.";
    public static final String API_MESSAGE_PARENT_DOES_NOT_EXIST = "Non-existent parent object. The parent specified does not exist";
    public static final String API_MESSAGE_NOT_FOUND = "Object not found";
    public static final String API_MESSAGE_CONFLICT = "Conflict. The resource is being used by someone else";
    public static final String API_MESSAGE_INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String API_MESSAGE_NOT_IMPLEMENTED = "Not implemented yet";
    public static final String API_MESSAGE_MALFORMED_PAYLOAD = "Incoming data is malformed";


    public static final String REL_LOGIN = NOARK_BASE_REL + "login/";

    public static final String REL_METADATA =
            NOARK_BASE_REL + NOARK_METADATA_PATH + SLASH;
    public static final String REL_ADMINISTRATION =
            NOARK_BASE_REL + NOARK_ADMINISTRATION_PATH + SLASH;
    public static final String REL_FONDS_STRUCTURE =
            NOARK_BASE_REL + NOARK_FONDS_STRUCTURE_PATH + SLASH;
    public static final String REL_CASE_HANDLING =
            NOARK_BASE_REL + NOARK_CASE_HANDLING_PATH + SLASH;
    public static final String REL_LOGGING =
            NOARK_BASE_REL + NOARK_LOGGING_PATH + SLASH;

    public static final String REL_SYSTEM_INFORMATION =
            REL_ADMINISTRATION + SYSTEM + SLASH;

    // FondsHateoas REL links
    public static final String REL_METADATA_FONDS_STATUS =
            REL_METADATA + FONDS_STATUS + SLASH;
    public static final String REL_FONDS_STRUCTURE_FONDS_CREATOR =
            REL_FONDS_STRUCTURE + FONDS_CREATOR + SLASH;
    public static final String REL_FONDS_STRUCTURE_FONDS =
            REL_FONDS_STRUCTURE + FONDS + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_FONDS_CREATOR =
            REL_FONDS_STRUCTURE + NEW_FONDS_CREATOR + SLASH;
    public static final String REL_FONDS_STRUCTURE_SUB_FONDS =
            REL_FONDS_STRUCTURE + SUB_FONDS + SLASH;

    public static final String REL_FONDS_STRUCTURE_PARENT_FONDS =
            REL_FONDS_STRUCTURE + PARENT_FONDS + SLASH;

    public static final String REL_FONDS_STRUCTURE_FONDS_STATUS = REL_METADATA + SLASH + FONDS_STATUS + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_FONDS = REL_FONDS_STRUCTURE + NEW_FONDS + SLASH;

    // Common FondsHateoas/SeriesHateoas REL links
    public static final String REL_FONDS_STRUCTURE_NEW_SERIES = REL_FONDS_STRUCTURE + NEW_SERIES + SLASH;
    public static final String REL_FONDS_STRUCTURE_SERIES = REL_FONDS_STRUCTURE + SERIES + SLASH;

    // Common SeriesHateoas/FileHateoas/RegistrationHateoas REL links
    public static final String REL_FONDS_STRUCTURE_NEW_RECORD = REL_FONDS_STRUCTURE + NEW_RECORD + SLASH;
    public static final String REL_FONDS_STRUCTURE_RECORD = REL_FONDS_STRUCTURE + RECORD + SLASH;

    // Common SeriesHateoas/FileHateoas REL links
    public static final String REL_FONDS_STRUCTURE_NEW_FILE = REL_FONDS_STRUCTURE + NEW_FILE + SLASH;
    public static final String REL_FONDS_STRUCTURE_FILE = REL_FONDS_STRUCTURE + FILE + SLASH;

    public static final String REL_CASE_HANDLING_NEW_CASE_FILE = REL_CASE_HANDLING + NEW_CASE_FILE + SLASH;
    public static final String REL_CASE_HANDLING_CASE_FILE = REL_CASE_HANDLING + CASE_FILE + SLASH;


    // Common SeriesHateoas/ClassificationHateoas REL links
    public static final String REL_FONDS_STRUCTURE_NEW_CLASSIFICATION_SYSTEM = REL_FONDS_STRUCTURE + NEW_CLASSIFICATION_SYSTEM + SLASH;
    public static final String REL_FONDS_STRUCTURE_CLASSIFICATION_SYSTEM = REL_FONDS_STRUCTURE + CLASSIFICATION_SYSTEM + SLASH;

    // SeriesHateoas REL links
    public static final String REL_METADATA_SERIES_STATUS = REL_METADATA + SERIES_STATUS + SLASH;

    // Series precursor / successor
    public static final String REL_FONDS_STRUCTURE_SUCCESSOR = NIKITA_CONFORMANCE_REL + SERIES_SUCCESSOR + SLASH;
    public static final String REL_FONDS_STRUCTURE_PRECURSOR = NIKITA_CONFORMANCE_REL + SERIES_PRECURSOR + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_SUCCESSOR = NIKITA_CONFORMANCE_REL + NEW_SERIES_SUCCESSOR + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_PRECURSOR = NIKITA_CONFORMANCE_REL + NEW_SERIES_PRECURSOR + SLASH;

    public static final String REL_FONDS_STRUCTURE_PART = REL_FONDS_STRUCTURE + PART + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_PART = REL_FONDS_STRUCTURE + NEW_PART + SLASH;


    // CaseFileHateoas REL links
    public static final String REL_CASE_HANDLING_NEW_CLASS = REL_FONDS_STRUCTURE + NEW_CLASS + SLASH;
    public static final String REL_CASE_HANDLING_CLASS = REL_FONDS_STRUCTURE + CLASS + SLASH;
    public static final String REL_CASE_HANDLING_SECONDARY_CLASSIFICATION = REL_CASE_HANDLING + SECONDARY_CLASSIFICATION + SLASH;
    public static final String REL_CASE_HANDLING_NEW_SECONDARY_CLASSIFICATION = NIKITA_CONFORMANCE_REL + NEW_SECONDARY_CLASSIFICATION + SLASH;
    public static final String REL_METADATA_CASE_STATUS = REL_METADATA + CASE_STATUS + SLASH;

    public static final String REL_METADATA_COUNTRY = REL_METADATA + COUNTRY + SLASH;
    public static final String REL_METADATA_POST_CODE = REL_METADATA + POST_CODE + SLASH;

    public static final String REL_CASE_HANDLING_NEW_REGISTRY_ENTRY = REL_CASE_HANDLING + NEW_REGISTRY_ENTRY + SLASH;
    public static final String REL_CASE_HANDLING_REGISTRY_ENTRY = REL_CASE_HANDLING + REGISTRY_ENTRY + SLASH;

    public static final String REL_CASE_HANDLING_RECORD_NOTE =
            REL_CASE_HANDLING + RECORD_NOTE + SLASH;
    public static final String REL_CASE_HANDLING_NEW_RECORD_NOTE =
            REL_CASE_HANDLING + NEW_RECORD_NOTE + SLASH;

    // FileHateoas REL links
    public static final String REL_FONDS_STRUCTURE_EXPAND_TO_CASE_FILE =
            REL_CASE_HANDLING + FILE_EXPAND_TO_CASE_FILE + SLASH;
    public static final String REL_FONDS_STRUCTURE_EXPAND_TO_MEETING_FILE =
            NIKITA_CONFORMANCE_REL + FILE_EXPAND_TO_MEETING_FILE + SLASH;
    public static final String REL_FONDS_STRUCTURE_END_FILE =
            NIKITA_CONFORMANCE_REL + FILE_END + SLASH;

    // Comment
    public static final String REL_FONDS_STRUCTURE_COMMENT =
            REL_FONDS_STRUCTURE + COMMENT + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_COMMENT =
            REL_FONDS_STRUCTURE + NEW_COMMENT + SLASH;

    public static final String REL_FONDS_STRUCTURE_PARENT_FILE =
            REL_FONDS_STRUCTURE + PARENT_FILE + SLASH;
    public static final String REL_FONDS_STRUCTURE_SUB_FILE =
            REL_FONDS_STRUCTURE + SUB_FILE + SLASH;

    // CrossReference
    public static final String REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE = REL_FONDS_STRUCTURE + NEW_CROSS_REFERENCE + SLASH;
    public static final String REL_FONDS_STRUCTURE_CROSS_REFERENCE = REL_FONDS_STRUCTURE + CROSS_REFERENCE + SLASH;

    // Class
    public static final String REL_FONDS_STRUCTURE_CLASS = REL_FONDS_STRUCTURE + CLASS + SLASH;
    public static final String REL_FONDS_STRUCTURE_PARENT_CLASS =
	REL_FONDS_STRUCTURE + PARENT_CLASS + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_CLASS = REL_FONDS_STRUCTURE + NEW_CLASS + SLASH;

    public static final String REL_FONDS_STRUCTURE_SUB_CLASS = REL_FONDS_STRUCTURE + SUB + CLASS + SLASH;

    // Series
    public static final String REL_FONDS_STRUCTURE_REFERENCE_SERIES = NIKITA_CONFORMANCE_REL + REFERENCE_SERIES + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES = NIKITA_CONFORMANCE_REL + REFERENCE_NEW_SERIES + SLASH;

    // StorageLocation
    public static final String REL_FONDS_STRUCTURE_STORAGE_LOCATION = NIKITA_CONFORMANCE_REL + STORAGE_LOCATION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION = NIKITA_CONFORMANCE_REL + NEW_STORAGE_LOCATION + SLASH;

    // Author
    public static final String REL_FONDS_STRUCTURE_AUTHOR =
            NIKITA_CONFORMANCE_REL + AUTHOR + SLASH;

    public static final String REL_FONDS_STRUCTURE_NEW_AUTHOR =
            NIKITA_CONFORMANCE_REL + NEW_AUTHOR + SLASH;

    // DocumentObject
    public static final String REL_FONDS_STRUCTURE_DOCUMENT_OBJECT = REL_FONDS_STRUCTURE + DOCUMENT_OBJECT + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_DOCUMENT_OBJECT = REL_FONDS_STRUCTURE + NEW_DOCUMENT_OBJECT + SLASH;

    // Conversion
    public static final String REL_FONDS_STRUCTURE_CONVERSION =
            REL_FONDS_STRUCTURE + CONVERSION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_CONVERSION =
            REL_FONDS_STRUCTURE + NEW_CONVERSION + SLASH;

    // DocumentDescription
    public static final String REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION =
            REL_FONDS_STRUCTURE + DOCUMENT_DESCRIPTION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_DOCUMENT_DESCRIPTION =
            REL_FONDS_STRUCTURE + NEW_DOCUMENT_DESCRIPTION + SLASH;

    // Precedence
    public static final String REL_CASE_HANDLING_PRECEDENCE =
            REL_CASE_HANDLING + PRECEDENCE + SLASH;
    public static final String REL_CASE_HANDLING_NEW_PRECEDENCE =
            REL_CASE_HANDLING + NEW_PRECEDENCE + SLASH;

    // SignOff
    public static final String REL_CASE_HANDLING_SIGN_OFF =
            REL_CASE_HANDLING + SIGN_OFF + SLASH;
    public static final String REL_CASE_HANDLING_NEW_SIGN_OFF =
            REL_CASE_HANDLING + NEW_SIGN_OFF + SLASH;
    public static final String REL_CASE_HANDLING_SIGN_OFF_REFERENCE_RECORD =
            NIKITA_CONFORMANCE_REL + SIGN_OFF_REFERENCE_RECORD + SLASH;

    // DocumentFlow
    public static final String REL_CASE_HANDLING_DOCUMENT_FLOW =
            REL_CASE_HANDLING + DOCUMENT_FLOW + SLASH;
    public static final String REL_CASE_HANDLING_NEW_DOCUMENT_FLOW =
            REL_CASE_HANDLING + NEW_DOCUMENT_FLOW + SLASH;

    public static final String REL_FONDS_STRUCTURE_CORRESPONDENCE_PART =
            REL_FONDS_STRUCTURE + CORRESPONDENCE_PART + SLASH;

    // CorrespondencePartPerson
    public static final String REL_FONDS_STRUCTURE_CORRESPONDENCE_PART_PERSON =
            REL_FONDS_STRUCTURE + CORRESPONDENCE_PART_PERSON + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_PERSON =
            REL_FONDS_STRUCTURE + NEW_CORRESPONDENCE_PART_PERSON + SLASH;

    // CorrespondencePartUnit
    public static final String REL_FONDS_STRUCTURE_CORRESPONDENCE_PART_UNIT =
            REL_FONDS_STRUCTURE + CORRESPONDENCE_PART_UNIT + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_UNIT =
            REL_FONDS_STRUCTURE + NEW_CORRESPONDENCE_PART_UNIT + SLASH;

    // CorrespondencePartInternal
    public static final String REL_FONDS_STRUCTURE_CORRESPONDENCE_PART_INTERNAL =
            REL_FONDS_STRUCTURE + CORRESPONDENCE_PART_INTERNAL + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_INTERNAL =
            REL_FONDS_STRUCTURE + NEW_CORRESPONDENCE_PART_INTERNAL + SLASH;

    // PartUnit
    public static final String REL_FONDS_STRUCTURE_NEW_PART_UNIT =
            REL_FONDS_STRUCTURE + NEW_PART_UNIT + SLASH;

    public static final String REL_FONDS_STRUCTURE_PART_UNIT =
            REL_FONDS_STRUCTURE + PART_UNIT + SLASH;

    // PartPerson
    public static final String REL_FONDS_STRUCTURE_NEW_PART_PERSON =
            REL_FONDS_STRUCTURE + NEW_PART_PERSON + SLASH;

    public static final String REL_FONDS_STRUCTURE_PART_PERSON =
            REL_FONDS_STRUCTURE + PART_PERSON + SLASH;

    // File
    public static final String REL_FONDS_STRUCTURE_DOCUMENT_FILE =
            REL_FONDS_STRUCTURE + DOCUMENT_FILE + SLASH;
    public static final String REL_FONDS_STRUCTURE_CONVERT_FILE =
            NIKITA_CONFORMANCE_REL + CONVERT_FILE + SLASH;

    // national identifiers

    public static final String REL_FONDS_STRUCTURE_NATIONAL_IDENTIFIER =
            REL_FONDS_STRUCTURE + NATIONAL_IDENTIFIER + SLASH;
    public static final String REL_FONDS_STRUCTURE_BUILDING =
            REL_FONDS_STRUCTURE + BUILDING + SLASH;
    public static final String REL_FONDS_STRUCTURE_CADASTRAL_UNIT =
            REL_FONDS_STRUCTURE + CADASTRAL_UNIT + SLASH;
    public static final String REL_FONDS_STRUCTURE_D_NUMBER =
            REL_FONDS_STRUCTURE + D_NUMBER + SLASH;
    public static final String REL_FONDS_STRUCTURE_PLAN =
            REL_FONDS_STRUCTURE + PLAN + SLASH;
    public static final String REL_FONDS_STRUCTURE_POSITION =
            REL_FONDS_STRUCTURE + POSITION + SLASH;
    public static final String REL_FONDS_STRUCTURE_SOCIAL_SECURITY_NUMBER =
            REL_FONDS_STRUCTURE + SOCIAL_SECURITY_NUMBER + SLASH;
    public static final String REL_FONDS_STRUCTURE_NI_UNIT =
            REL_FONDS_STRUCTURE + NI_UNIT + SLASH;

    public static final String REL_FONDS_STRUCTURE_NEW_BUILDING =
            REL_FONDS_STRUCTURE + NEW_BUILDING + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_CADASTRAL_UNIT =
            REL_FONDS_STRUCTURE + NEW_CADASTRAL_UNIT + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_D_NUMBER =
            REL_FONDS_STRUCTURE + NEW_D_NUMBER + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_PLAN =
            REL_FONDS_STRUCTURE + NEW_PLAN + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_POSITION =
            REL_FONDS_STRUCTURE + NEW_POSITION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_SOCIAL_SECURITY_NUMBER =
            REL_FONDS_STRUCTURE + NEW_SOCIAL_SECURITY_NUMBER + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_NI_UNIT =
            REL_FONDS_STRUCTURE + NEW_NI_UNIT + SLASH;

    public static final String REL_LOGGING_CHANGE_LOG =
            REL_LOGGING + CHANGE_LOG + SLASH;
    public static final String REL_LOGGING_NEW_CHANGE_LOG =
            REL_LOGGING + NEW_CHANGE_LOG + SLASH;

    public static final String REL_LOGGING_EVENT_LOG =
            REL_LOGGING + EVENT_LOG + SLASH;
    public static final String REL_LOGGING_NEW_EVENT_LOG =
            REL_LOGGING + NEW_EVENT_LOG + SLASH;

    // Metadata RELS
    public static final String REL_METADATA_DOCUMENT_MEDIUM =
            REL_METADATA + DOCUMENT_MEDIUM + SLASH;

    public static final String REL_METADATA_CORRESPONDENCE_PART =
            REL_METADATA + CORRESPONDENCE_PART + SLASH;

    public static final String REL_METADATA_CORRESPONDENCE_PART_TYPE =
            REL_METADATA + CORRESPONDENCE_PART_TYPE + SLASH;

    public static final String REL_METADATA_PART_ROLE =
            REL_METADATA + PART_ROLE + SLASH;

    public static final String REL_METADATA_VARIANT_FORMAT = REL_METADATA +
            VARIANT_FORMAT + SLASH;

    public static final String REL_METADATA_FORMAT = REL_METADATA + FORMAT +
            SLASH;

    public static final String REL_METADATA_FLOW_STATUS = REL_METADATA +
            FLOW_STATUS + SLASH;

    public static final String REL_METADATA_REGISTRY_ENTRY_TYPE = REL_METADATA +
            REGISTRY_ENTRY_TYPE + SLASH;

    public static final String REL_METADATA_DOCUMENT_STATUS = REL_METADATA +
            DOCUMENT_STATUS + SLASH;

    public static final String REL_METADATA_DOCUMENT_TYPE = REL_METADATA +
            DOCUMENT_TYPE + SLASH;

    public static final String REL_METADATA_DISPOSAL_DECISION = REL_METADATA +
            DISPOSAL_DECISION + SLASH;

    public static final String REL_METADATA_DELETION_TYPE = REL_METADATA +
            DELETION_TYPE + SLASH;

    public static final String REL_METADATA_EVENT_TYPE = REL_METADATA +
            EVENT_TYPE + SLASH;

    public static final String REL_METADATA_POSTAL_NUMBER = REL_METADATA +
            POSTAL_NUMBER + SLASH;

    public static final String REL_METADATA_ACCESS_CATEGORY = REL_METADATA +
            ACCESS_CATEGORY + SLASH;

    public static final String REL_METADATA_ACCESS_RESTRICTION = REL_METADATA +
            ACCESS_RESTRICTION + SLASH;

    public static final String REL_METADATA_ASSOCIATED_WITH_RECORD_AS =
            REL_METADATA + ASSOCIATED_WITH_RECORD_AS + SLASH;

    public static final String REL_METADATA_SIGN_OFF_METHOD = REL_METADATA +
            SIGN_OFF_METHOD + SLASH;

    public static final String REL_METADATA_ELECTRONIC_SIGNATURE_SECURITY_LEVEL
            = REL_METADATA + ELECTRONIC_SIGNATURE_SECURITY_LEVEL + SLASH;

    public static final String REL_METADATA_ELECTRONIC_SIGNATURE_VERIFIED
            = REL_METADATA + ELECTRONIC_SIGNATURE_VERIFIED + SLASH;

    public static final String REL_METADATA_REGISTRY_ENTRY_STATUS =
            REL_METADATA + REGISTRY_ENTRY_STATUS + SLASH;

    public static final String REL_METADATA_PRECEDENCE_STATUS =
            REL_METADATA + PRECEDENCE_STATUS + SLASH;

    public static final String REL_METADATA_FILE_TYPE =
            REL_METADATA + FILE_TYPE + SLASH;

    public static final String REL_METADATA_CLASSIFICATION_TYPE =
            REL_METADATA + CLASSIFICATION_TYPE + SLASH;

    public static final String REL_METADATA_COMMENT_TYPE =
            REL_METADATA + COMMENT_TYPE + SLASH;

    // M086 dokumenttype
    public static final String REL_METADATA_CLASSIFICATION_SYSTEM_TYPE =
            REL_METADATA + CLASSIFICATION_SYSTEM_TYPE + SLASH;

    public static final String REL_ADMIN_NEW_ADMINISTRATIVE_UNIT =
            REL_ADMINISTRATION + NEW_ADMINISTRATIVE_UNIT + SLASH;

    public static final String REL_ADMIN_ADMINISTRATIVE_UNIT =
            REL_ADMINISTRATION + ADMINISTRATIVE_UNIT + SLASH;

    public static final String REL_ADMIN_NEW_USER =
            REL_ADMINISTRATION + NEW_USER + SLASH;

    public static final String REL_ADMIN_USER =
            REL_ADMINISTRATION + USER + SLASH;

    public static final String REL_METADATA_SCREENING_DOCUMENT =
            REL_METADATA + SCREENING_DOCUMENT + SLASH;

    public static final String REL_METADATA_CLASSIFIED_CODE =
            REL_METADATA + CLASSIFIED_CODE + SLASH;

    public static final String REL_METADATA_SCREENING_METADATA =
            REL_METADATA + SCREENING_METADATA + SLASH;

    public static final String REL_METADATA_COORDINATE_SYSTEM =
            REL_METADATA + COORDINATE_SYSTEM + SLASH;

    public static final String REL_LOGIN_OIDC = REL_LOGIN + "oidc/";
    public static final String REL_LOGIN_OAUTH2 = REL_LOGIN + "rfc6749/";

    public static final String REL_LOGOUT_OAUTH2 =
            NIKITA_CONFORMANCE_REL + LOGOUT_REL_PATH + SLASH +
                    LOGIN_OAUTH + SLASH;


    // Database Constants

    public static final String TABLE_USER = "ad_user";
    public static final String TABLE_AUTHORITY = "ad_authority";

    // Table Constants
    public static final String TABLE_FONDS = "as_fonds";
    public static final String TABLE_SERIES = "as_series";
    public static final String TABLE_RECORD = "as_record";
    public static final String TABLE_PART = "as_part";
    public static final String TABLE_FILE = "as_file";
    public static final String TABLE_CLASS = "as_class";
    public static final String TABLE_RECORD_NOTE = "sa_record_note";
    public static final String TABLE_REGISTRY_ENTRY = "sa_registry_entry";
    public static final String TABLE_PRECEDENCE = "sa_precedence";
    public static final String TABLE_DOCUMENT_OBJECT = "as_document_object";
    public static final String TABLE_FONDS_CREATOR = "as_fonds_creator";
    public static final String TABLE_STORAGE_LOCATION = "as_storage_location";
    public static final String TABLE_CROSS_REFERENCE = "as_cross_reference";
    public static final String TABLE_CLASSIFICATION_SYSTEM =
            "as_classification_system";
    public static final String TABLE_MEETING_FILE = "mu_meeting_file";
    public static final String TABLE_DELETION = "as_deletion";
    public static final String TABLE_DISPOSAL = "as_disposal";
    public static final String TABLE_ELECTRONIC_SIGNATURE = "as_electronic_signature";
    public static final String TABLE_DISPOSAL_UNDERTAKEN =
            "as_disposal_undertaken";
    public static final String TABLE_CORRESPONDENCE_PART =
            "as_correspondence_part";

    public static final String TABLE_AUTHORITY_SEQ = "ad_authority_seq";
    public static final String TABLE_USER_AUTHORITY = "ad_user_authority";
    public static final String TABLE_CONTACT_CLASSIFIED = "as_classified";
    public static final String TABLE_COMMENT = "as_comment";
    public static final String TABLE_CONVERSION = "as_conversion";
    public static final String TABLE_CONTACT_AUTHOR = "as_author";
    public static final String TABLE_CONTACT_INFORMATION =
            "as_contact_information";


    public static final String TABLE_RECORD_PART =
            "as_record_part";

    public static final String TABLE_RECORD_CORRESPONDENCE_PART =
            "as_record_correspondence_part";

    public static final String TABLE_CASE_FILE_SEQUENCE =
            "sa_sequence_generator";
    public static final String TABLE_ADMINISTRATIVE_UNIT =
            "ad_administrative_unit";

    public static final String TABLE_ADMINISTRATIVE_UNIT_USER =
            "ad_administrative_unit_user";
    public static final String TABLE_REGISTRY_ENTRY_CORRESPONDENCE_PART_INTERNAL =
            "as_registry_entry_correspondence_part_internal";

    // Join table names
    public static final String TABLE_FONDS_FONDS_CREATOR =
            "as_fonds_fonds_creator";
    public static final String TABLE_FILE_STORAGE_LOCATION =
            "as_file_storage_location";
    public static final String TABLE_SERIES_STORAGE_LOCATION =
            "as_series_storage_location";
    public static final String TABLE_SERIES_CLASSIFICATION_SYSTEM =
            "as_series_classification_system";
    public static final String TABLE_FILE_KEYWORD =
            "as_file_keyword";
    public static final String TABLE_FILE_COMMENT =
            "as_file_comment";
    public static final String TABLE_FILE_PARTY = "as_file_party";
    public static final String TABLE_RECORD_AUTHOR = "as_record_author";
    public static final String TABLE_REGISTRY_ENTRY_SIGN_OFF =
            "sa_registry_entry_sign_off";
    public static final String TABLE_REGISTRY_ENTRY_CORRESPONDENCE_PART_PERSON =
            "sa_registry_entry_correspondence_part_person";
    public static final String TABLE_REGISTRY_ENTRY_CORRESPONDENCE_PART_UNIT =
            "sa_registry_entry_correspondence_part_unit";
    public static final String TABLE_REGISTRY_ENTRY_PART_UNIT =
            "as_registry_entry_part_unit";
    public static final String TABLE_REGISTRY_ENTRY_PART_PERSON =
            "as_registry_entry_person_unit";
    public static final String TABLE_RECORD_DOCUMENT_DESCRIPTION =
            "as_record_document_description";
    public static final String TABLE_RECORD_PARTY = "as_record_party";
    public static final String TABLE_CLASS_KEYWORD = "as_class_keyword";
    public static final String TABLE_DOCUMENT_DESCRIPTION_PARTY =
            "as_document_description_party";
    public static final String TABLE_FONDS_STORAGE_LOCATION =
            "as_fonds_storage_location";
    public static final String TABLE_RECORD_KEYWORD =
            "as_record_keyword";
    public static final String TABLE_RECORD_COMMENT =
            "as_record_comment";
    public static final String TABLE_REGISTRY_ENTRY_PRECEDENCE =
            "sa_registry_entry_precedence";
    public static final String TABLE_DOCUMENT_DESCRIPTION_COMMENT =
            "as_document_description_comment";
    public static final String TABLE_DOCUMENT_DESCRIPTION_AUTHOR =
            "as_document_description_author";

    public static final String TABLE_PART_PERSON = "as_part_person";
    public static final String TABLE_PART_UNIT = "as_part_unit";

    public static final String TABLE_CORRESPONDENCE_PART_INTERNAL =
            "as_correspondence_part_internal";

    public static final String TABLE_CORRESPONDENCE_PART_PERSON =
            "as_correspondence_part_person";

    public static final String TABLE_CORRESPONDENCE_PART_UNIT =
            "as_correspondence_part_unit";

    public static final String TABLE_CASE_FILE_PRECEDENCE =
            "sa_case_file_precedence";
    public static final String TABLE_RECORD_STORAGE_LOCATION =
            "as_record_storage_location";

    public static final String TABLE_DOCUMENT_FLOW = "as_document_flow";
    public static final String TABLE_CASE_FILE = "sa_case_file";
    public static final String TABLE_MEETING_PARTICIPANT =
            "mu_meeting_participant";
    public static final String TABLE_MEETING_RECORD = "mu_meeting_record";
    public static final String TABLE_POSTAL_ADDRESS = "as_postal_address";
    public static final String TABLE_BUSINESS_ADDRESS = "as_business_address";
    public static final String TABLE_RESIDING_ADDRESS = "as_residing_address";
    public static final String TABLE_KEYWORD = "as_keyword";
    public static final String TABLE_SCREENING = "as_screening";
    public static final String TABLE_SIGN_OFF = "as_sign_off";
    public static final String TABLE_DOCUMENT_DESCRIPTION =
            "as_document_description";

    public static final String TABLE_NATIONAL_IDENTIFIER =
            "as_national_identifier";
    public static final String TABLE_BUILDING = "as_building";
    public static final String TABLE_CADASTRAL = "as_cadastral";
    public static final String TABLE_SOCIAL_SECURITY_NUMBER = "" +
            "as_social_security_number";
    public static final String TABLE_D_NUMBER = "" +
            "as_d_number";
    public static final String TABLE_PLAN = "as_plan";
    public static final String TABLE_POSITION = "as_position";
    public static final String TABLE_UNIT = "as_unit";

    // Metadata tablenames
    public static final String TABLE_ACCESS_CATEGORY = "md_access_category";
    public static final String TABLE_ACCESS_RESTRICTION = 
            "md_access_restriction";
    public static final String TABLE_ASSOCIATED_WITH_RECORD_AS = 
            "md_associated_with_record_as";
    public static final String TABLE_CASE_STATUS = "md_case_status";
    public static final String TABLE_CLASSIFICATION_TYPE =
            "md_classification_type";
    public static final String TABLE_CLASSIFIED_CODE = "md_classified_code";
    public static final String TABLE_COMMENT_TYPE = "md_comment_type";
    public static final String TABLE_CORRESPONDENCE_PART_TYPE =
            "md_correspondence_part_type";
    public static final String TABLE_PART_TYPE = "md_part_type";
    public static final String TABLE_COORDINATE_SYSTEM = "md_coordinate_system";
    public static final String TABLE_COUNTRY = "md_country";
    public static final String TABLE_DELETION_TYPE = "md_deletion_type";
    public static final String TABLE_DISPOSAL_DECISION = "md_disposal_decision";
    public static final String TABLE_DOCUMENT_MEDIUM = "md_document_medium";
    public static final String TABLE_DOCUMENT_STATUS = "md_document_status";
    public static final String TABLE_DOCUMENT_TYPE = "md_document_type";
    public static final String TABLE_ELECTRONIC_SIGNATURE_SECURITY_LEVEL =
            "md_electronic_signature_security_level";
    public static final String TABLE_ELECTRONIC_SIGNATURE_VERIFIED =
            "md_electronic_signature_verified";
    public static final String TABLE_EVENT_TYPE = "md_event_type";
    public static final String TABLE_FILE_TYPE = "md_file_type";
    public static final String TABLE_FLOW_STATUS = "md_flow_status";
    public static final String TABLE_FONDS_STATUS = "md_fonds_status";
    public static final String TABLE_FORMAT = "md_format";
    public static final String TABLE_MEETING_FILE_TYPE = "md_meeting_file_type";
    public static final String TABLE_MEETING_PARTICIPANT_FUNCTION =
            "md_meeting_participant_function";
    public static final String TABLE_MEETING_REGISTRATION_STATUS =
            "md_meeting_registration_status";
    public static final String TABLE_MEETING_REGISTRATION_TYPE =
            "md_meeting_registration_type";
    public static final String TABLE_PART_ROLE = "md_part_role";
    public static final String TABLE_POSTAL_CODE = "md_postal_code";
    public static final String TABLE_PRECEDENCE_STATUS = "md_precedence_status";
    public static final String TABLE_REGISTRY_ENTRY_STATUS =
            "md_registry_entry_status";
    public static final String TABLE_REGISTRY_ENTRY_TYPE =
            "md_registry_entry_type";
    public static final String TABLE_SCREENING_DOCUMENT =
            "md_screening_document";
    public static final String TABLE_SCREENING_METADATA =
            "md_screening_metadata";
    public static final String TABLE_SERIES_STATUS = "md_series_status";
    public static final String TABLE_SIGN_OFF_METHOD = "md_sign_off_method";
    public static final String TABLE_VARIANT_FORMAT = "md_variant_format";
    public static final String TABLE_CHANGE_LOG = "as_changelog";
    public static final String TABLE_EVENT_LOG = "as_event_log";

    // Column Constants
    // Foreign key names
    public static final String FOREIGN_KEY_FONDS_PK = "f_pk_fonds_id";
    public static final String FOREIGN_KEY_CORRESPONDENCE_PART_UNIT_PK =
            "f_pk_correspondence_part_unit_id";
    public static final String FOREIGN_KEY_PART_UNIT_PK =
            "f_pk_part_unit_id";
    public static final String FOREIGN_KEY_PART_PERSON_PK =
            "f_pk_part_person_id";
    public static final String FOREIGN_KEY_SIGN_OFF_PK = "f_pk_sign_off_id";
    public static final String FOREIGN_KEY_SERIES_PK = "f_pk_series_id";
    public static final String FOREIGN_KEY_FONDS_CREATOR_PK =
            "f_pk_fonds_creator_id";

    public static final String FOREIGN_KEY_DOCUMENT_DESCRIPTION_PK =
            "f_pk_document_description_id";
    public static final String FOREIGN_KEY_AUTHOR_PK = "f_pk_author_id";
    public static final String FOREIGN_KEY_CASE_FILE_PK = "f_pk_case_file_id";
    public static final String FOREIGN_KEY_PRECEDENCE_PK = "f_pk_precedence_id";
    public static final String FOREIGN_KEY_CLASS_PK = "f_pk_class_id";
    public static final String FOREIGN_KEY_USER_PK = "f_pk_user_id";
    public static final String FOREIGN_KEY_ADMINISTRATIVE_UNIT_PK =
            "f_pk_administrative_unit_id";

    public static final String FOREIGN_KEY_KEYWORD_PK =
            "f_pk_keyword_id";
    public static final String FOREIGN_KEY_FILE_PK = "f_pk_file_id";
    public static final String FOREIGN_KEY_COMMENT_PK = "f_pk_comment_id";
    public static final String FOREIGN_KEY_PART_PK = "f_pk_part_id";
    public static final String FOREIGN_KEY_CORRESPONDENCE_PART_PK =
            "f_pk_correspondence_part_id";
    public static final String FOREIGN_KEY_RECORD_PK = "f_pk_record_id";

    public static final String FOREIGN_KEY_STORAGE_LOCATION_PK =
            "f_pk_storage_location_id";
    public static final String FOREIGN_KEY_CLASSIFICATION_SYSTEM_PK =
            "f_pk_classification_system_id";
    public static final String FOREIGN_KEY_CORRESPONDENCE_PART_PERSON_PK =
            "f_pk_correspondence_part_person_id";

    public static final String FOREIGN_KEY_CORRESPONDENCE_PART_INTERNAL_ID =
            "f_pk_correspondence_part_internal_id";

    public static final String FILE_SERIES_ID = "file_series_id";
    public static final String CONVERSION_DOCUMENT_OBJECT_ID =
            "conversion_document_object_id";
    public static final String CASE_FILE_ADMINISTRATIVE_UNIT_ID =
            "case_file_administrative_unit_id";
    public static final String FILE_CLASS_ID = "file_class_id";
    public static final String FILE_CLASSIFIED_ID = "file_classified_id";
    public static final String FILE_SCREENING_ID = "file_screening_id";
    public static final String FILE_DISPOSAL_ID = "file_disposal_id";
    public static final String DOCUMENT_FLOW_FLOW_TO_ID =
            "document_flow_flow_to_id";
    public static final String DOCUMENT_FLOW_FLOW_FROM_ID =
            "document_flow_flow_from_id";
    public static final String DOCUMENT_FLOW_REGISTRY_ENTRY_ID =
            "document_flow_registry_entry_id";
    public static final String DOCUMENT_FLOW_RECORD_NOTE_ID =
            "document_flow_record_note_id";

    public static final String PRECEDENCE_APPROVED_BY_ID =
            "precedence_approved_by_id";

    public static final String SYSTEM_ID_ENTITY_ID = "system_id_entity_id";
    public static final String RECORD_FILE_ID = "record_file_id";
    public static final String RECORD_CLASS_ID = "record_class_id";
    public static final String RECORD_CLASSIFIED_ID = "record_classified_id";
    public static final String RECORD_DISPOSAL_ID = "record_disposal_id";
    public static final String RECORD_SERIES_ID = "record_series_id";
    public static final String CLASS_CLASSIFIED_ID = "class_classified_id";
    public static final String CLASS_DISPOSAL_ID = "class_disposal_id";
    public static final String CLASS_SCREENING_ID = "class_screening_id";
    public static final String MEETING_PARTICIPANT_FILE_ID =
            "meeting_participant_file_id";
    public static final String DOCUMENT_OBJECT_DOCUMENT_DESCRIPTION_ID =
            "document_object_document_description_id";

    public static final String CORRESPONDENCE_PART_CORRESPONDENCE_PART_TYPE_ID =
            "correspondence_part_correspondence_part_type_id";

    public static final String PART_PART_ROLE_ID = "part_part_role_id";

    public static final String SERIES_FONDS_ID = "series_fonds_id";
    public static final String SERIES_DISPOSAL_ID = "series_disposal_id";
    public static final String SERIES_SCREENING_ID = "series_screening_id";
    public static final String SERIES_CLASSIFIED_ID = "series_classified_id";
    public static final String SERIES_DISPOSAL_UNDERTAKEN_ID =
            "series_disposal_undertaken_id";
    public static final String SERIES_CLASSIFICATION_SYSTEM_ID =
            "series_classification_system_id";
    public static final String SERIES_DELETION_ID = "series_deletion_id";
    public static final String DOCUMENT_DESCRIPTION_DISPOSAL_ID =
            "document_description_disposal_id";

    public static final String DOCUMENT_DESCRIPTION_CLASSIFIED_ID =
            "document_description_classified_id";

    public static final String DOCUMENT_DESCRIPTION_DISPOSAL_UNDERTAKEN_ID =
            "document_description_disposal_undertaken_id";
    public static final String DOCUMENT_DESCRIPTION_DELETION_ID =
            "document_description_deletion_id";

    public static final String DOCUMENT_DESCRIPTION_SCREENING_ID =
            "document_description_screening_id";

    public static final String CROSS_REFERENCE_FILE_ID =
            "cross_reference_file_id";
    public static final String CROSS_REFERENCE_CLASS_ID =
            "cross_reference_class_id";
    public static final String CROSS_REFERENCE_RECORD_ID =
            "cross_reference_record_id";

    public static final String NATIONAL_IDENTIFIER_RECORD_ID =
            "national_identifier_record_id";
    public static final String NATIONAL_IDENTIFIER_FILE_ID =
            "national_identifier_file_id";

    // Primary key names
    public static final String PRIMARY_KEY_SYSTEM_ID = "system_id";

    public static final String JOIN_CASE_FILE_STATUS =
            "case_file_case_file_status_id";

    public static final String HREF_OPENID_CONFIGURATION =
            ".well-known/openid-configuration";

    public static final String HREF_BASE_FONDS_STRUCTURE =
            HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH;

    public static final String HREF_BASE_ADMIN =
            HATEOAS_API_PATH + SLASH + NOARK_ADMINISTRATION_PATH;

    public static final String HREF_BASE_METADATA =
            HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH;

    public static final String HREF_BASE_CASE_HANDLING =
            HATEOAS_API_PATH + SLASH + NOARK_CASE_HANDLING_PATH;

    public static final String HREF_BASE_LOGGING =
            HATEOAS_API_PATH + SLASH + NOARK_LOGGING_PATH;

    public static final String HREF_SYSTEM_INFORMATION =
            HREF_BASE_ADMIN + SLASH + SYSTEM;

    public static final String HREF_BASE_DOCUMENT_DESCRIPTION =
            HREF_BASE_FONDS_STRUCTURE + SLASH + DOCUMENT_DESCRIPTION;

    public static final String HREF_BASE_DOCUMENT_OBJECT =
            HREF_BASE_FONDS_STRUCTURE + SLASH + DOCUMENT_OBJECT;

    public static final String HREF_BASE_RECORD =
            HREF_BASE_FONDS_STRUCTURE + SLASH + RECORD;

    public static final String HREF_BASE_RECORD_NOTE =
            HREF_BASE_CASE_HANDLING + SLASH + RECORD_NOTE;

    public static final String HREF_BASE_REGISTRY_ENTRY =
            HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY;

    public static final String HREF_BASE_FILE =
            HREF_BASE_FONDS_STRUCTURE + SLASH + FILE;

    public static final String HREF_BASE_CASE_FILE =
            HREF_BASE_CASE_HANDLING + SLASH + CASE_FILE;

    public static final String HREF_BASE_SERIES =
            HREF_BASE_FONDS_STRUCTURE + SLASH + SERIES;

    public static final String HREF_BASE_CLASS =
            HREF_BASE_FONDS_STRUCTURE + SLASH + CLASS;

    public static final String HREF_BASE_CLASSIFICATION_SYSTEM =
            HREF_BASE_FONDS_STRUCTURE + SLASH + CLASSIFICATION_SYSTEM;

    public static final String HREF_BASE_FONDS =
            HREF_BASE_FONDS_STRUCTURE + SLASH + FONDS;

    public static final String HREF_BASE_DOCUMENT_FLOW =
            HREF_BASE_CASE_HANDLING + SLASH + DOCUMENT_FLOW;

    private Constants() {
    }
}
