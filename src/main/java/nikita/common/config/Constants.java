package nikita.common.config;

import static nikita.common.config.N5ResourceMappings.*;

/**
 * Application constants.
 */
public final class Constants {

    public static final String NEW = "ny";
    public static final String DASH = "-";
    public static final String SUB = "under";
    public static final String PARENT = "forelder";

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
    public static final String HATEOAS_API_PATH = "hateoas-api";

    public static final String SLASH = "/";
    public static final String LEFT_PARENTHESIS = "{";
    public static final String RIGHT_PARENTHESIS = "}";
    public static final String ENTITY_ROOT_NAME_LIST = "results";

    // Create for new arkivstruktur objects
    public static final String NEW_FONDS = NEW + DASH + FONDS;
    public static final String NEW_FONDS_CREATOR = NEW + DASH + FONDS_CREATOR;
    public static final String NEW_SERIES = NEW + DASH + SERIES;
    public static final String NEW_CLASSIFICATION_SYSTEM = NEW + DASH + CLASSIFICATION_SYSTEM;
    public static final String NEW_SECONDARY_CLASSIFICATION_SYSTEM = NEW + DASH + CLASSIFICATION_SYSTEM;
    public static final String NEW_CLASS = NEW + DASH + CLASS;
    public static final String NEW_SUB_CLASS = NEW + DASH + SUB_CLASS;
    public static final String NEW_FILE = NEW + DASH + FILE;
    public static final String NEW_RECORD = NEW + DASH + RECORD;
    public static final String NEW_DOCUMENT_DESCRIPTION = NEW + DASH + DOCUMENT_DESCRIPTION;
    public static final String NEW_DOCUMENT_OBJECT = NEW + DASH + DOCUMENT_OBJECT;
    public static final String NEW_REFERENCE_SERIES = NEW + DASH + REFERENCE_SERIES;

    public static final String PERSON = "person";
    public static final String UNIT = "enhet";
    public static final String INTERNAL = "intern";

    // Other arkivstruktur commands
    public static final String FILE_END = "avslutt-mappe";
    public static final String FILE_EXPAND_TO_CASE_FILE = "utvid-til-" + CASE_FILE;
    public static final String FILE_EXPAND_TO_MEETING_FILE = "utvid-til-" + MEETING_FILE;
    public static final String NEW_COMMENT = NEW + DASH + COMMENT;
    public static final String NEW_SUB_FILE = NEW + DASH + "undermappe";
    public static final String SUB_FILE = SUB + FILE;
    public static final String NEW_SUB_FONDS = NEW + DASH + SUB + FONDS;
    public static final String NEW_CROSS_REFERENCE = NEW + DASH + CROSS_REFERENCE;
    public static final String NEW_REFERENCE_CLASS = NEW + DASH + "klassereferanse";
    public static final String REFERENCE_NEW_SERIES = NEW + DASH + "referanseArkivdel";
    public static final String NEW_CLASSIFIED = NEW + DASH + CLASSIFIED;
    public static final String NEW_SCREENING = NEW + DASH + SCREENING;
    public static final String NEW_DISPOSAL = NEW + DASH + DISPOSAL;
    public static final String NEW_DISPOSAL_UNDERTAKEN = NEW + DASH + DISPOSAL_UNDERTAKEN;
    public static final String NEW_DELETION = NEW + DASH + "sletting";
    public static final String NEW_STORAGE_LOCATION = NEW + DASH + STORAGE_LOCATION;
    public static final String NEW_STORAGE_LOCATIONS = NEW + DASH + STORAGE_LOCATIONS;
    public static final String NEW_AUTHOR = NEW + DASH + AUTHOR;
    public static final String NEW_ELECTRONIC_SIGNATURE = NEW + DASH + ELECTRONIC_SIGNATURE;
    public static final String NEW_CONVERSION = NEW + DASH + CONVERSION;
    public static final String NEW_KEYWORD = NEW + DASH + KEYWORD;
    public static final String NEW_SIGN_OFF = NEW + DASH + SIGN_OFF;
    public static final String NEW_DOCUMENT_FLOW = NEW + DASH + DOCUMENT_FLOW;
    public static final String NEW_CORRESPONDENCE_PART = NEW + DASH + CORRESPONDENCE_PART;
    public static final String NEW_CORRESPONDENCE_PART_PERSON = NEW_CORRESPONDENCE_PART + PERSON;
    public static final String NEW_CORRESPONDENCE_PART_UNIT = NEW_CORRESPONDENCE_PART + UNIT;
    public static final String NEW_CORRESPONDENCE_PART_INTERNAL = NEW_CORRESPONDENCE_PART + INTERNAL;
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
    public static final String NEW_PART = NEW + DASH + PART;
    public static final String NEW_CASE_STATUS = NEW + DASH + CASE_STATUS;
    public static final String NEW_SECONDARY_CLASSIFICATION = NEW + DASH +
            "sekundaerklassifikasjon";

    // Create for new metadata objects
    public static final String NEW_DOCUMENT_MEDIUM = NEW + DASH +
            DOCUMENT_MEDIUM;
    public static final String NEW_FONDS_STATUS = NEW + DASH + FONDS_STATUS;
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

    public static final String NEW_CORRESPONDENCE_PART_TYPE = NEW + DASH +
            CORRESPONDENCE_PART_TYPE;

    public static final String SERIES_ASSOCIATE_AS_SUCCESSOR = "referanseArvtager";
    public static final String SERIES_ASSOCIATE_AS_PRECURSOR = "referanseForloeper";

    // String used for default / template object
    public static final String DEFAULT_TITLE = "Default title for ";
    public static final String DEFAULT_DESCRIPTION = "This object is, if " +
            "persisted, will be associated with ";

    // Some user identifiers used for testing
    public static final String TEST_USER_CASE_HANDLER_1 = "example test user case handler 1";
    public static final String TEST_USER_CASE_HANDLER_2 = "example test user case handler 2";
    public static final String TEST_USER_ADMIN = "example test user admin";
    public static final String TEST_USER_RECORD_KEEPER = "example test user record keeper";

    // Some strings used during testing
    public static final String TEST_TITLE = "example test title";
    public static final String TEST_DESCRIPTION = "example test description";
    public static final String TEST_ADMINISTRATIVE_UNIT = "example test administrative unit";
    public static final String TEST_RECORD_STATUS = "J";
    public static final String TEST_REGISTRY_ENTRY_TYPE = "I";

    public static final String NOARK_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String NOARK_TIME_FORMAT_PATTERN = "HH:mm:ss";
    public static final String NOARK_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String REFERENCE_FILE = "referanseFil";

    // namespace definition
    public static final String NOARK_CONFORMANCE_REL =
            "https://rel.arkivverket.no/noark5/v5/api/";
    public static final String NIKITA_CONFORMANCE_REL =
            "https://nikita.arkivlab.no/noark5/v5/";
    public static final String NOARK_VERSION = "noark5v5";

    public static final String NOARK_FONDS_STRUCTURE_PATH = "arkivstruktur";
    public static final String NOARK_METADATA_PATH = "metadata";
    public static final String NOARK_ADMINISTRATION_PATH = "admin";
    public static final String NOARK_CASE_HANDLING_PATH = "sakarkiv";
    public static final String NOARK_LOGGING_PATH = "loggingogpsoring";
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

    public static final String NOARK5_V5_CONTENT_TYPE_JSON = "application/vnd.noark5-v5+json";
    public static final String NOARK5_V5_CONTENT_TYPE_JSON_XML = "application/vnd.noark5-v5+xml";

    public static final String INFO_CANNOT_FIND_OBJECT = "Cannot find object of type ";
    public static final String INFO_CANNOT_ASSOCIATE_WITH_CLOSED_OBJECT = "Cannot associate with a closed object";
    public static final String INFO_INVALID_STRUCTURE = "Invalid Noark structure";

    public static final String TEMPLATE_FONDS_STATUS_CODE = "Opprettet";
    public static final String TEMPLATE_FONDS_STATUS_DESCRIPTION = "Arkivet er opprettet og i aktiv bruk";
    public static final String TEMPLATE_DOCUMENT_MEDIUM_CODE = "Elektronisk arkiv";
    public static final String TEMPLATE_DOCUMENT_MEDIUM_DESCRIPTION = "Bare elektroniske dokumenter";
    public static final String TEMPLATE_DOCUMENT_STATUS_CODE = "B";
    public static final String TEMPLATE_DOCUMENT_STATUS_DESCRIPTION =
            "Dokumentet er under redigering";
    public static final String TEMPLATE_DOCUMENT_TYPE_CODE = "B";
    public static final String TEMPLATE_DOCUMENT_TYPE_DESCRIPTION =
            "Brev";

    public static final String TEMPLATE_REGISTRY_ENTRY_STATUS_CODE = "J";
    public static final String TEMPLATE_REGISTRY_ENTRY_STATUS_DESCRIPTION =
            "Journalført";

    public static final String TEMPLATE_PRECEDENCE_STATUS_CODE = "G";
    public static final String TEMPLATE_PRECEDENCE_STATUS_DESCRIPTION =
            "Gjeldende";

    public static final String TEMPLATE_SIGN_OFF_METHOD_CODE = "BE";
    public static final String TEMPLATE_SIGN_OFF_METHOD_DESCRIPTION =
            "Besvart med e-post";

    public static final String
            TEMPLATE_ELECTRONIC_SIGNATURE_SECURITY_LEVEL_CODE = "SK";
    public static final String
            TEMPLATE_ELECTRONIC_SIGNATURE_SECURITY_LEVEL_DESCRIPTION =
            "Symmetrisk kryptert";

    public static final String
            TEMPLATE_ELECTRONIC_SIGNATURE_VERIFIED_CODE = "I";
    public static final String
            TEMPLATE_ELECTRONIC_SIGNATURE_VERIFIED_DESCRIPTION =
            "Signatur påført, ikke verifisert";

    public static final String TEMPLATE_FORMAT_CODE = "RA-PDF";
    public static final String TEMPLATE_FORMAT_DESCRIPTION = "PDF/A - ISO " +
            "19005-1:2005";

    public static final String TEMPLATE_FLOW_STATUS_CODE = "G";
    public static final String TEMPLATE_FLOW_STATUS_DESCRIPTION = "Godkjent";

    public static final String TEMPLATE_PART_ROLE_CODE = "KLI";
    public static final String TEMPLATE_PART_ROLE_DESCRIPTION = "Klient";

    public static final String TEMPLATE_FILE_TYPE_CODE = "S";
    public static final String TEMPLATE_FILE_TYPE_DESCRIPTION = "Saksmappe";

    public static final String TEMPLATE_VARIANT_FORMAT_CODE = "P";
    public static final String TEMPLATE_VARIANT_FORMAT_DESCRIPTION =
            "Produksjonsformat";

    public static final String TEMPLATE_CASE_STATUS_CODE = "R";
    public static final String TEMPLATE_CASE_STATUS_DESCRIPTION =
            "Opprettet av saksbehandler";

    public static final String TEMPLATE_COUNTRY_CODE = "NO";
    public static final String TEMPLATE_COUNTRY_DESCRIPTION =
            "Norge";

    public static final String TEMPLATE_POST_CODE_CODE = "0001";
    public static final String TEMPLATE_POST_CODE_DESCRIPTION =
            "OSLO";

    public static final String TEMPLATE_CLASSIFICATION_TYPE_CODE = "FH";
    public static final String TEMPLATE_CLASSIFICATION_TYPE_DESCRIPTION =
            "Funksjonsbasert, hierarkisk";

    public static final String TEMPLATE_CLASSIFIED_CODE_CODE = "SH";
    public static final String TEMPLATE_CLASSIFIED_CODE_DESCRIPTION =
            "Strengt hemmelig (sikkerhetsgrad)";

    public static final String TEMPLATE_COMMENT_TYPE_CODE = "MS";
    public static final String TEMPLATE_COMMENT_TYPE_DESCRIPTION =
            "Merknad fra saksbehandler";

    public static final String TEMPLATE_REGISTRY_ENTRY_TYPE_CODE = "I";
    public static final String TEMPLATE_REGISTRY_ENTRY_TYPE_DESCRIPTION =
            "Inngående dokument";

    public static final String TEMPLATE_REGISTRY_ENTRY_STATUS = "Journalført";
    public static final String TEMPLATE_REGISTRY_ENTRY_TYPE = "Journalført";

    public static final String TEMPLATE_SCREENING_METADATA_CODE = "H";
    public static final String TEMPLATE_SCREENING_METADATA_DESCRIPTION =
            "Skjerming av hele dokumentet";
    public static final String TEMPLATE_SCREENING_DOCUMENT_CODE = "D";
    public static final String TEMPLATE_SCREENING_DOCUMENT_DESCRIPTION =
            "Skjerming av deler av dokumentet";


    public static final int UUIDLength = 32;

    public static final String REFERENCE_CLASS = "referanseKlasse";

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


    public static final String REL_METADATA = "https://rel.arkivverket.no/noark5/v5/api/metadata/";
    public static final String REL_ADMINISTRATION = "https://rel.arkivverket.no/noark5/v5/api/administrasjon/";
    public static final String REL_FONDS_STRUCTURE = "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/";
    public static final String REL_CASE_HANDLING = "https://rel.arkivverket.no/noark5/v5/api/sakarkiv/";

    // FondsHateoas REL links

    public static final String REL_METADATA_FONDS_STATUS = REL_METADATA + FONDS_STATUS + SLASH;
    public static final String REL_FONDS_STRUCTURE_FONDS_CREATOR = REL_FONDS_STRUCTURE + FONDS_CREATOR + SLASH;
    public static final String REL_FONDS_STRUCTURE_FONDS = REL_FONDS_STRUCTURE + FONDS + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_FONDS_CREATOR = REL_FONDS_STRUCTURE + NEW_FONDS_CREATOR + SLASH;
    public static final String REL_FONDS_STRUCTURE_SUB_FONDS = NIKITA_CONFORMANCE_REL + SUB_FONDS + SLASH;

    public static final String REL_FONDS_STRUCTURE_PARENT_FONDS =
            NIKITA_CONFORMANCE_REL + PARENT_FONDS + SLASH;

    public static final String REL_FONDS_STRUCTURE_NEW_SUB_FONDS = NIKITA_CONFORMANCE_REL + NEW_SUB_FONDS + SLASH;
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

    public static final String REL_FONDS_STRUCTURE_NEW_CASE_FILE = REL_CASE_HANDLING + NEW_CASE_FILE + SLASH;
    public static final String REL_FONDS_STRUCTURE_CASE_FILE = REL_CASE_HANDLING + CASE_FILE + SLASH;


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
    public static final String REL_CASE_HANDLING_PRECEDENCE = REL_CASE_HANDLING + PRECEDENCE + SLASH;
    public static final String REL_CASE_HANDLING_NEW_PRECEDENCE = REL_CASE_HANDLING + NEW_PRECEDENCE + SLASH;
    public static final String REL_CASE_HANDLING_SECONDARY_CLASSIFICATION = NIKITA_CONFORMANCE_REL + SECONDARY_CLASSIFICATION + SLASH;
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
    public static final String REL_FONDS_STRUCTURE_EXPAND_TO_CASE_FILE = NIKITA_CONFORMANCE_REL + FILE_EXPAND_TO_CASE_FILE + SLASH;
    public static final String REL_FONDS_STRUCTURE_EXPAND_TO_MEETING_FILE = NIKITA_CONFORMANCE_REL + FILE_EXPAND_TO_MEETING_FILE + SLASH;
    public static final String REL_FONDS_STRUCTURE_END_FILE = NIKITA_CONFORMANCE_REL + FILE_END + SLASH;

    // Comment
    public static final String REL_FONDS_STRUCTURE_COMMENT = REL_FONDS_STRUCTURE + COMMENT + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_COMMENT = REL_FONDS_STRUCTURE + NEW_COMMENT + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_SUB_FILE = NIKITA_CONFORMANCE_REL + NEW_SUB_FILE + SLASH;
    public static final String REL_FONDS_STRUCTURE_SUB_FILE = NIKITA_CONFORMANCE_REL + SUB_FILE + SLASH;

    // CrossReference
    public static final String REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE = REL_FONDS_STRUCTURE + NEW_CROSS_REFERENCE + SLASH;
    public static final String REL_FONDS_STRUCTURE_CROSS_REFERENCE = REL_FONDS_STRUCTURE + CROSS_REFERENCE + SLASH;

    // Class
    public static final String REL_FONDS_STRUCTURE_CLASS = REL_FONDS_STRUCTURE + CLASS + SLASH;
    public static final String REL_FONDS_STRUCTURE_PARENT_CLASS = NIKITA_CONFORMANCE_REL + PARENT + CLASS + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_CLASS = REL_FONDS_STRUCTURE + NEW_CLASS + SLASH;

    public static final String REL_FONDS_STRUCTURE_SUB_CLASS = REL_FONDS_STRUCTURE + SUB + CLASS + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_SUB_CLASS = REL_FONDS_STRUCTURE + NEW + DASH + SUB + CLASS + SLASH;

    // Series
    public static final String REL_FONDS_STRUCTURE_REFERENCE_SERIES = NIKITA_CONFORMANCE_REL + REFERENCE_SERIES + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES = NIKITA_CONFORMANCE_REL + REFERENCE_NEW_SERIES + SLASH;

    // Classified
    public static final String REL_FONDS_STRUCTURE_CLASSIFIED = NIKITA_CONFORMANCE_REL + CLASSIFIED + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_CLASSIFIED = NIKITA_CONFORMANCE_REL + NEW_CLASSIFIED + SLASH;

    // Screening
    public static final String REL_FONDS_STRUCTURE_SCREENING = NIKITA_CONFORMANCE_REL + SCREENING + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_SCREENING = NIKITA_CONFORMANCE_REL + NEW_SCREENING + SLASH;

    // Disposal
    public static final String REL_FONDS_STRUCTURE_DISPOSAL = NIKITA_CONFORMANCE_REL + DISPOSAL + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_DISPOSAL = NIKITA_CONFORMANCE_REL + NEW_DISPOSAL + SLASH;

    // DisposalUndertaken
    public static final String REL_FONDS_STRUCTURE_DISPOSAL_UNDERTAKEN = NIKITA_CONFORMANCE_REL + DISPOSAL_UNDERTAKEN + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_DISPOSAL_UNDERTAKEN = NIKITA_CONFORMANCE_REL + NEW_DISPOSAL_UNDERTAKEN + SLASH;

    // StorageLocation
    public static final String REL_FONDS_STRUCTURE_STORAGE_LOCATION = NIKITA_CONFORMANCE_REL + STORAGE_LOCATION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION = NIKITA_CONFORMANCE_REL + NEW_STORAGE_LOCATION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION_LIST = NIKITA_CONFORMANCE_REL + NEW_STORAGE_LOCATIONS + SLASH;

    // DocumentObject
    public static final String REL_FONDS_STRUCTURE_DOCUMENT_OBJECT = REL_FONDS_STRUCTURE + DOCUMENT_OBJECT + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_DOCUMENT_OBJECT = REL_FONDS_STRUCTURE + NEW_DOCUMENT_OBJECT + SLASH;

    // Deletion
    public static final String REL_FONDS_STRUCTURE_DELETION = NIKITA_CONFORMANCE_REL + DELETION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_DELETION = NIKITA_CONFORMANCE_REL + NEW_DELETION + SLASH;

    // Author
    public static final String REL_FONDS_STRUCTURE_AUTHOR = NIKITA_CONFORMANCE_REL + AUTHOR + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_AUTHOR = NIKITA_CONFORMANCE_REL + NEW_AUTHOR + SLASH;

    // Conversion
    public static final String REL_FONDS_STRUCTURE_CONVERSION =
            REL_FONDS_STRUCTURE + CONVERSION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_CONVERSION =
            REL_FONDS_STRUCTURE + NEW_CONVERSION + SLASH;

    // ElectronicSignature
    public static final String REL_FONDS_STRUCTURE_ELECTRONIC_SIGNATURE =
            REL_FONDS_STRUCTURE + ELECTRONIC_SIGNATURE + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_ELECTRONIC_SIGNATURE =
            REL_FONDS_STRUCTURE + NEW_ELECTRONIC_SIGNATURE + SLASH;

    // DocumentDescription
    public static final String REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION =
            REL_FONDS_STRUCTURE + DOCUMENT_DESCRIPTION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_DOCUMENT_DESCRIPTION =
            REL_FONDS_STRUCTURE + NEW_DOCUMENT_DESCRIPTION + SLASH;

    // Keyword
    public static final String REL_FONDS_STRUCTURE_KEYWORD =
            NIKITA_CONFORMANCE_REL + KEYWORD + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_KEYWORD =
            NIKITA_CONFORMANCE_REL + NEW_KEYWORD + SLASH;

    // Precedence
    public static final String REL_FONDS_STRUCTURE_PRECEDENCE =
            REL_CASE_HANDLING + PRECEDENCE + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_PRECEDENCE =
            REL_CASE_HANDLING + NEW_PRECEDENCE + SLASH;

    // Keyword
    public static final String REL_FONDS_STRUCTURE_SIGN_OFF =
            REL_CASE_HANDLING + SIGN_OFF + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_SIGN_OFF =
            REL_CASE_HANDLING + NEW_SIGN_OFF + SLASH;

    // DocumentFlow
    public static final String REL_FONDS_STRUCTURE_DOCUMENT_FLOW =
            REL_CASE_HANDLING + DOCUMENT_FLOW + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_DOCUMENT_FLOW =
            REL_CASE_HANDLING + NEW_DOCUMENT_FLOW + SLASH;

    // CorrespondencePartPerson
    public static final String REL_CASE_HANDLING_CORRESPONDENCE_PART_PERSON =
            REL_CASE_HANDLING + CORRESPONDENCE_PART_PERSON + SLASH;
    public static final String REL_CASE_HANDLING_NEW_CORRESPONDENCE_PART_PERSON =
            REL_CASE_HANDLING + NEW_CORRESPONDENCE_PART_PERSON + SLASH;

    // CorrespondencePartUnit
    public static final String REL_CASE_HANDLING_CORRESPONDENCE_PART_UNIT =
            REL_CASE_HANDLING + CORRESPONDENCE_PART_UNIT + SLASH;
    public static final String REL_CASE_HANDLING_NEW_CORRESPONDENCE_PART_UNIT =
            REL_CASE_HANDLING + NEW_CORRESPONDENCE_PART_UNIT + SLASH;

    // CorrespondencePartInternal
    public static final String REL_CASE_HANDLING_CORRESPONDENCE_PART_INTERNAL =
            REL_CASE_HANDLING + CORRESPONDENCE_PART_INTERNAL + SLASH;
    public static final String REL_CASE_HANDLING_NEW_CORRESPONDENCE_PART_INTERNAL =
            REL_CASE_HANDLING + NEW_CORRESPONDENCE_PART_INTERNAL + SLASH;

    // Fil
    public static final String REL_FONDS_STRUCTURE_DOCUMENT_FILE =
            REL_FONDS_STRUCTURE + DOCUMENT_FILE + SLASH;

    // SecondaryClassification
    public static final String REL_FONDS_STRUCTURE_SECONDARY_CLASSIFICATION =
            NIKITA_CONFORMANCE_REL + SECONDARY_CLASSIFICATION + SLASH;
    public static final String REL_FONDS_STRUCTURE_NEW_SECONDARY_CLASSIFICATION
            = NIKITA_CONFORMANCE_REL + NEW_SECONDARY_CLASSIFICATION + SLASH;

    // Metadata RELS
    public static final String REL_METADATA_DOCUMENT_MEDIUM =
            REL_METADATA + DOCUMENT_MEDIUM + SLASH;

    public static final String REL_METADATA_CORRESPONDENCE_PART =
            REL_METADATA + CORRESPONDENCE_PART + SLASH;

    public static final String REL_METADATA_CORRESPONDENCE_PART_TYPE =
            REL_METADATA + CORRESPONDENCE_PART_TYPE + SLASH;

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

    public static final String REL_METADATA_PART_ROLE =
            REL_METADATA + PART_ROLE + SLASH;

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

    public static final String REL_LOGIN_OAUTH2 =
            NIKITA_CONFORMANCE_REL + LOGIN_REL_PATH + SLASH +
                    LOGIN_OAUTH + SLASH;

    public static final String REL_LOGOUT_OAUTH2 =
            NIKITA_CONFORMANCE_REL + LOGOUT_REL_PATH + SLASH +
                    LOGIN_OAUTH + SLASH;


    // Database Constants

    // Table Constants
    public static final String TABLE_FONDS = "fonds";
    public static final String TABLE_SERIES = "series";
    public static final String TABLE_RECORD = "record";
    public static final String TABLE_PART = "part";
    public static final String TABLE_FILE = "file";
    public static final String TABLE_CLASS = "class";
    public static final String TABLE_RECORD_NOTE = "record_note";
    public static final String TABLE_REGISTRY_ENTRY = "registry_entry";
    public static final String TABLE_PRECEDENCE = "precedence";
    public static final String TABLE_DOCUMENT_OBJECT = "document_object";
    public static final String TABLE_FONDS_CREATOR = "fonds_creator";
    public static final String TABLE_STORAGE_LOCATION = "storage_location";
    public static final String TABLE_CROSS_REFERENCE = "cross_reference";
    public static final String TABLE_CLASSIFICATION_SYSTEM =
            "classification_system";
    public static final String TABLE_MEETING_FILE = "meeting_file";
    public static final String TABLE_CORRESPONDENCE_PART =
            "correspondence_part";
    public static final String TABLE_CONTACT_CLASSIFIED = "classified";
    public static final String TABLE_COMMENT = "comment";
    public static final String TABLE_CONTACT_AUTHOR = "author";
    public static final String TABLE_CONTACT_INFORMATION =
            "contact_information";

    public static final String TABLE_CASE_FILE_SEQUENCE = "sequence_generator";
    public static final String TABLE_NIKITA_ADMINISTRATIVE_UNIT =
            "nikita_administrative_unit";

    public static final String TABLE_ADMINISTRATIVE_UNIT_JOIN_NIKITA_USER =
            "administrative_unit_nikita_user";
    public static final String TABLE_REGISTRY_ENTRY_CORRESPONDENCE_PART_INTERNAL =
            "registry_entry_correspondence_part_internal";


    // Join table names
    public static final String TABLE_FONDS_FONDS_CREATOR =
            "fonds_fonds_creator";
    public static final String TABLE_FILE_STORAGE_LOCATION =
            "file_storage_location";
    public static final String TABLE_SERIES_STORAGE_LOCATION =
            "series_storage_location";
    public static final String TABLE_FILE_KEYWORD =
            "file_keyword";
    public static final String TABLE_FILE_COMMENT =
            "file_comment";
    public static final String TABLE_FILE_PARTY = "file_party";
    public static final String TABLE_RECORD_AUTHOR = "record_author";
    public static final String TABLE_REGISTRY_ENTRY_SIGN_OFF =
            "registry_entry_sign_off";
    public static final String TABLE_REGISTRY_ENTRY_CORRESPONDENCE_PART_PERSON =
            "registry_entry_correspondence_part_person";
    public static final String TABLE_REGISTRY_ENTRY_CORRESPONDENCE_PART_UNIT =
            "registry_entry_correspondence_part_unit";
    public static final String TABLE_RECORD_DOCUMENT_DESCRIPTION =
            "record_document_description";
    public static final String TABLE_RECORD_PARTY = "record_party";
    public static final String TABLE_CLASS_KEYWORD = "class_keyword";
    public static final String TABLE_DOCUMENT_DESCRIPTION_PARTY =
            "document_description_party";
    public static final String TABLE_FONDS_STORAGE_LOCATION =
            "fonds_storage_location";
    public static final String TABLE_RECORD_KEYWORD =
            "record_keyword";
    public static final String TABLE_RECORD_COMMENT =
            "record_comment";
    public static final String TABLE_REGISTRY_ENTRY_PRECEDENCE =
            "registry_entry_precedence";
    public static final String TABLE_DOCUMENT_DESCRIPTION_COMMENT =
            "document_description_comment";
    public static final String TABLE_DOCUMENT_DESCRIPTION_AUTHOR =
            "document_description_author";
    public static final String TABLE_CORRESPONDENCE_PART_INTERNAL =
            "correspondence_part_internal";

    public static final String TABLE_CORRESPONDENCE_PART_PERSON =
            "correspondence_part_person";
    public static final String TABLE_CORRESPONDENCE_PART_UNIT =
            "correspondence_part_unit";

    public static final String TABLE_CASE_FILE_PRECEDENCE =
            "case_file_precedence";
    public static final String TABLE_RECORD_STORAGE_LOCATION =
            "record_storage_location";

    public static final String TABLE_DOCUMENT_FLOW = "document_flow";
    public static final String TABLE_CASE_FILE = "case_file";
    public static final String TABLE_MEETING_PARTICIPANT = "meeting_participant";
    public static final String TABLE_MEETING_RECORD = "meeting_record";
    public static final String TABLE_POSTAL_ADDRESS = "postal_address";
    public static final String TABLE_RESIDING_ADDRESS = "residing_address";
    public static final String TABLE_DOCUMENT_DESCRIPTION =
            "document_description";

    // Column Constants
    // Foreign key names
    public static final String FOREIGN_KEY_FONDS_PK = "f_pk_fonds_id";
    public static final String FOREIGN_KEY_CORRESPONDENCE_PART_UNIT_PK =
            "f_pk_correspondence_part_unit_id";
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
    public static final String FOREIGN_KEY_RECORD_PK = "f_pk_record_id";

    public static final String FOREIGN_KEY_STORAGE_LOCATION_PK =
            "f_pk_storage_location_id";

    public static final String FOREIGN_KEY_CORRESPONDENCE_PART_PERSON_PK =
            "f_pk_correspondence_part_person_id";

    public static final String FOREIGN_KEY_ADMINISTRATIVE_UNIT_PK_ADMINISTRATIVE_UNIT_ID =
            "administrative_unit_pk_administrative_unit_id";

    public static final String FOREIGN_KEY_CORRESPONDENCE_PART_INTERNAL_ID =
            "f_pk_correspondence_part_internal_id";


    public static final String FILE_SERIES_ID = "file_series_id";
    public static final String CASE_FILE_ADMINISTRATIVE_UNIT_ID =
            "case_file_administrative_unit_id";
    public static final String FILE_CLASS_ID = "file_class_id";
    public static final String FILE_CLASSIFIED_ID = "file_classified_id";
    public static final String FILE_SCREENING_ID = "file_screening_id";
    public static final String FILE_DISPOSAL_ID = "file_disposal_id";
    public static final String WORK_FLOW_REGISTRY_ENTRY_ID =
            "work_flow_registry_entry_id";
    public static final String WORK_FLOW_RECORD_NOTE_ID =
            "work_flow_record_note_id";

    public static final String REFERENCE_ADMINISTRATIVE_UNIT =
            "referenceAdministrativeUnit";

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

    // Primary key names
    public static final String PRIMARY_KEY_SYSTEM_ID = "pk_fonds_id";
    /*
        public static final String PRIMARY_KEY_FONDS = "pk_fonds_id";
        public static final String PRIMARY_KEY_FONDS_CREATOR =
                "pk_fonds_creator_id";
        public static final String PRIMARY_KEY_SERIES = "pk_series_id";
        public static final String PRIMARY_KEY_CLASS = "pk_class_id";
        public static final String PRIMARY_KEY_FILE = "pk_file_id";
        public static final String PRIMARY_KEY_KEYWORD = "pk_keyword_id";
        public static final String PRIMARY_KEY_COMMENT = "pk_comment_id";
        public static final String PRIMARY_KEY_PART = "pk_part_id";
        public static final String PRIMARY_KEY_CLASSIFIED = "pk_classified_id";
        public static final String PRIMARY_KEY_DISPOSAL = "pk_disposal_id";
        public static final String PRIMARY_KEY_SCREENING = "pk_screening_id";
        public static final String PRIMARY_CROSS_REFERENCE =
                "pk_cross_reference_id";

        public static final String PRIMARY_KEY_USER = "pk_user_id";
        public static final String PRIMARY_KEY_AUTHOR = "pk_author_id";
        public static final String PRIMARY_KEY_RECORD = "pk_record_id";
        public static final String PRIMARY_KEY_STORAGE_LOCATION =
                "pk_storage_location_id";
        public static final String PRIMARY_KEY_ADMINISTRATIVE_UNIT =
                "pk_administrative_unit_id";
        public static final String PRIMARY_KEY_CORRESPONDENCE_PART_PERSON =
                "pk_correspondence_part_person_id";

        public static final String PRIMARY_KEY_CORRESPONDENCE_PART =
                "pk_correspondence_part_id";

        public static final String PRIMARY_KEY_CASE_FILE_STATUS =
                "pk_case_file_status_id";
    */
    public static final String JOIN_CASE_FILE_STATUS =
            "case_file_case_file_status_id";

    public static final String PRIMARY_KEY_DOCUMENT_DESCRIPTION =
            "pk_document_description_id";

    public static final String DM_OWNED_BY = "ownedBy";
    public static final String SYSTEM = "system";


    public static final String HREF_BASE_FONDS_STRUCTURE =
            HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH;

    public static final String HREF_BASE_METADATA =
            HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH;

    public static final String HREF_BASE_CASE_HANDLING =
            HATEOAS_API_PATH + SLASH + NOARK_CASE_HANDLING_PATH + SLASH;

    public static final String HREF_BASE_DOCUMENT_DESCRIPTION =
            HREF_BASE_FONDS_STRUCTURE + DOCUMENT_DESCRIPTION + SLASH;

    public static final String HREF_BASE_DOCUMENT_OBJECT =
            HREF_BASE_FONDS_STRUCTURE + DOCUMENT_OBJECT + SLASH;

    public static final String HREF_BASE_RECORD =
            HREF_BASE_FONDS_STRUCTURE + RECORD + SLASH;

    public static final String HREF_BASE_RECORD_NOTE =
            HREF_BASE_FONDS_STRUCTURE + RECORD_NOTE + SLASH;

    public static final String HREF_BASE_FILE =
            HREF_BASE_FONDS_STRUCTURE + FILE + SLASH;

    public static final String HREF_BASE_CASE_FILE =
            HREF_BASE_CASE_HANDLING + CASE_FILE + SLASH;

    public static final String HREF_BASE_SERIES =
            HREF_BASE_FONDS_STRUCTURE + SERIES + SLASH;

    public static final String HREF_BASE_CLASS =
            HREF_BASE_FONDS_STRUCTURE + CLASS + SLASH;

    public static final String HREF_BASE_CLASSIFICATION_SYSTEM =
            HREF_BASE_FONDS_STRUCTURE + CLASSIFICATION_SYSTEM + SLASH;

    public static final String HREF_BASE_FONDS =
            HREF_BASE_FONDS_STRUCTURE + FONDS + SLASH;

    public static final String HREF_BASE_DOCUMENT_FLOW =
            HREF_BASE_FONDS_STRUCTURE + DOCUMENT_FLOW + SLASH;

    public static final String HREF_METADATA_PATH =
            HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH;

    private Constants() {
    }
}
