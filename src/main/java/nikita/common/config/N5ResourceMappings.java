package nikita.common.config;

import static nikita.common.config.Constants.*;

public final class N5ResourceMappings {

    // Commonly used entities for REST request mappings
    public static final String FONDS = "arkiv";
    public static final String SUB_FONDS = SUB + FONDS;
    public static final String PARENT_FONDS = PARENT + FONDS;
    public static final String SERIES = "arkivdel";
    public static final String REFERENCE_SERIES = "referanseArkivdel";
    public static final String REFERENCE_ADMINISTRATIVE_UNIT = "referanseAdministratitivEnhet";
    public static final String REFERENCE_CASE_HANDLER = "referanseSaksbehandler";
    public static final String FILE = "mappe";
    public static final String CASE_FILE = "saksmappe";
    public static final String MEETING_FILE = "moetemappe";
    public static final String MEETING_PARTICIPANT = "moetedeltager";
    public static final String MEETING_RECORD = "moeteregistrering";
    public static final String RECORD = "registrering";
    public static final String REGISTRATION = "registrering";
    public static final String REGISTRY_ENTRY = "journalpost";
    public static final String RECORD_NOTE = "arkivnotat";
    public static final String DOCUMENT_DESCRIPTION = "dokumentbeskrivelse";
    public static final String DOCUMENT_OBJECT = "dokumentobjekt";
    public static final String STORAGE_LOCATION = "oppbevaringssted";

    public static final String KEYWORD = "noekkelord";

    public static final String FILE_TYPE = "mappetype";
    public static final String FLOW_STATUS = "flytstatus";
    public static final String EVENT_TYPE = "hendelsetype";
    // Might be confusion if it's land or lankode
    public static final String COUNTRY = "land";
    public static final String NEW_COUNTRY = NEW + DASH + COUNTRY;

    public static final String COUNTRY_CODE = "landkode";
    public static final String CLASSIFIED_CODE = "graderingskode";
    public static final String CLASSIFICATION_TYPE = "klassifikasjonstype";
    public static final String ASSOCIATED_WITH_RECORD_AS = "tilknyttetregistreringsom";
    public static final String ACCESS_RESTRICTION = "tilgangsrestriksjon";
    public static final String ACCESS_CATEGORY = "tilgangskategori";
    public static final String POSTAL_NUMBER = "postnummer";
    public static final String POSTAL_TOWN = "poststed";
    // For the sessions endpoint
    public static final String SESSIONS = "sessions";

    //Common to many entities column/attribute names
    public static final String TITLE = "tittel";
    public static final String DESCRIPTION = "beskrivelse";
    public static final String SYSTEM_ID = "systemID";
    public static final String SUB_SYSTEM_ID = "subSystemID";
    public static final String CREATED_DATE = "opprettetDato";
    public static final String CREATED_BY = "opprettetAv";
    public static final String FINALISED_DATE = "avsluttetDato";
    public static final String FINALISED_BY = "avsluttetAv";
    public static final String LAST_MODIFIED_BY = "oppdatertAv";
    public static final String LAST_MODIFIED_DATE = "oppdatertDato";
    public static final String DOCUMENT_MEDIUM = "dokumentmedium";

    public static final String SYSTEM_ID_PARAMETER =
            LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS;

    public static final String SUB_SYSTEM_ID_PARAMETER =
            LEFT_PARENTHESIS + SUB_SYSTEM_ID + RIGHT_PARENTHESIS;

    // For metadata entities
    public static final String CODE_NAME = "kodenavn";
    public static final String CODE_INACTIVE = "inaktiv";
    public static final String CODE = "kode";

    public static final String CODE_PARAMETER =
            LEFT_PARENTHESIS + CODE + RIGHT_PARENTHESIS;
    // Fonds
    public static final String FONDS_STATUS = "arkivstatus";

    // Series
    public static final String SERIES_STATUS = "arkivdelstatus";
    public static final String SERIES_START_DATE = "arkivperiodeStartDato";
    public static final String SERIES_END_DATE = "arkivperiodeSluttDato";
    public static final String SERIES_ASSOCIATE_AS_PRECURSOR = "referanseForloeper";
    public static final String SERIES_ASSOCIATE_AS_SUCCESSOR = "referanseArvtaker";
    public static final String SERIES_SUCCESSOR = "arvtager";
    public static final String SERIES_PRECURSOR = "forloeper";

    // ClassificationSystem
    public static final String CLASSIFICATION_SYSTEM = "klassifikasjonssystem";
    // Not known if officaly recognised as part of Noark 5 or just something
    // seen in interface standard
    public static final String SECONDARY_CLASSIFICATION_SYSTEM = "sekundaerklassifikasjonssystem";

    //  Class
    public static final String CLASS = "klasse";
    public static final String CLASS_ID = "klasseID";

    public static final String PARENT_CLASS = PARENT + CLASS;
    public static final String SUB_CLASS = SUB + CLASS;

    // File
    public static final String FILE_ID = "mappeID";

    // File / Record
    public static final String FILE_PUBLIC_TITLE = "offentligTittel";

    // CaseFile
    public static final String CASE_YEAR = "saksaar";
    public static final String CASE_SEQUENCE_NUMBER = "sakssekvensnummer";
    public static final String CASE_DATE = "saksdato";

    public static final String CASE_RESPONSIBLE = "saksansvarlig";
    public static final String CASE_RECORDS_MANAGEMENT_UNIT = "journalenhet";
    public static final String CASE_STATUS = "saksstatus";
    public static final String CASE_LOANED_DATE = "utlaantDato";
    public static final String CASE_LOANED_TO = "utlaantTil";

    // Part
    public static final String PART_ROLE = "partrolle";
    public static final String PART = "part";
    public static final String PART_PERSON = "partperson";
    public static final String PART_UNIT = "partenhet";
    public static final String PART_ID = "partID";
    public static final String PART_NAME = "partNavn";
    public static final String PART_ROLE_FIELD = "partRolle";

    // Record
    public static final String RECORD_ARCHIVED_BY = "arkivertAv";
    public static final String RECORD_ARCHIVED_DATE = "arkivertDato";

    // Record
    public static final String RECORD_ID = "registreringsID";

    // RegistryEntry
    public static final String REGISTRY_ENTRY_DATE = "journaldato";
    public static final String REGISTRY_ENTRY_DOCUMENT_DATE = "dokumentetsDato";
    public static final String REGISTRY_ENTRY_RECEIVED_DATE = "mottattDato";
    public static final String REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE = "offentlighetsvurdertDato";
    public static final String REGISTRY_ENTRY_SENT_DATE = "sendtDato";
    public static final String REGISTRY_ENTRY_DUE_DATE = "forfallsdato";
    public static final String REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS = "antallVedlegg";
    public static final String REGISTRY_ENTRY_YEAR = "journalaar";
    public static final String REGISTRY_ENTRY_SEQUENCE_NUMBER = "journalsekvensnummer";
    public static final String REGISTRY_ENTRY_NUMBER = "journalpostnummer";
    public static final String REGISTRY_ENTRY_TYPE = "journalposttype";
    public static final String REGISTRY_ENTRY_STATUS = "journalstatus";

    // CorrespondencePart
    public static final String CORRESPONDENCE_PART = "korrespondansepart";

    public static final String CORRESPONDENCE_PART_PERSON = "korrespondansepartperson";
    public static final String CORRESPONDENCE_PART_INTERNAL = "korrespondansepartintern";
    public static final String CORRESPONDENCE_PART_UNIT = "korrespondansepartenhet";
    public static final String CORRESPONDENCE_PART_TYPE = "korrespondanseparttype";
    public static final String CORRESPONDENCE_PART_NAME = "navn";
    public static final String NATIONAL_IDENTIFIER = "nasjonalidentifikator";
    public static final String PERSON_IDENTIFIER = "personidentifikator";
    public static final String UNIT_IDENTIFIER = "enhetsidentifikator";
    public static final String POST_CODE = "postnummer";
    public static final String NEW_POST_CODE = NEW + DASH + POST_CODE;

    public static final String SOCIAL_SECURITY_NUMBER = "foedselsnummer";
    public static final String NEW_SOCIAL_SECURITY_NUMBER =
            NEW + DASH + SOCIAL_SECURITY_NUMBER;
    public static final String D_NUMBER = "dnummer";
    public static final String D_NUMBER_FIELD = "dNummer";
    public static final String NEW_D_NUMBER = NEW + DASH + D_NUMBER;
    public static final String CADASTRAL_UNIT = "matrikkel";
    public static final String NEW_CADASTRAL_UNIT = NEW + DASH + CADASTRAL_UNIT;
    public static final String BUILDING = "bygning";
    public static final String NEW_BUILDING = NEW + DASH + BUILDING;
    public static final String POSITION = "posisjon";
    public static final String NEW_POSITION = NEW + DASH + POSITION;
    public static final String PLAN = "plan";
    public static final String NEW_PLAN = NEW + DASH + PLAN;
    public static final String NI_UNIT = "enhetsidentifikator";
    public static final String NEW_NI_UNIT = NEW + DASH + NI_UNIT;

    public static final String CASE_HANDLER = "saksbehandler";
    // This is probably CORRESPONDENCE_PART_NAME. Waiting for clarification
    public static final String NAME = "navn";
    public static final String ADDRESS_LINE_1 = "adresselinje1";
    public static final String ADDRESS_LINE_2 = "adresselinje2";
    public static final String ADDRESS_LINE_3 = "adresselinje3";
    public static final String CONTACT_INFORMATION = "kontaktinformasjon";
    public static final String EMAIL_ADDRESS = "epostadresse";
    public static final String TELEPHONE_NUMBER = "telefon";
    public static final String MOBILE_TELEPHONE_NUMBER = "mobiltelefon";
    public static final String FOREIGN_ADDRESS = "utenlandsadresse";
    public static final String CONTACT_PERSON = "kontaktperson";
    public static final String ORGANISATION_NUMBER = "organisasjonsnummer";
    public static final String POSTAL_ADDRESS = "postadresse";
    public static final String RESIDING_ADDRESS = "bostedsadresse";
    public static final String BUSINESS_ADDRESS = "forretningsadresse";

    // DocumentDescription
    public static final String DOCUMENT_DESCRIPTION_ASSOCIATED_DATE = "tilknyttetDato";
    public static final String DOCUMENT_DESCRIPTION_ASSOCIATED_BY = "tilknyttetAv";
    public static final String DOCUMENT_DESCRIPTION_DOCUMENT_TYPE = "dokumenttype";
    public static final String DOCUMENT_DESCRIPTION_STATUS = "dokumentstatus";
    public static final String DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS = "tilknyttetRegistreringSom";
    public static final String DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER = "dokumentnummer";
    public static final String DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE = "eksternReferanse";

    // DocumentObject
    public static final String DOCUMENT_OBJECT_VERSION_NUMBER = "versjonsnummer";
    public static final String DOCUMENT_OBJECT_VARIANT_FORMAT = "variantformat";
    public static final String DOCUMENT_OBJECT_FORMAT = "format";
    public static final String DOCUMENT_OBJECT_FORMAT_DETAILS = "formatDetaljer";
    public static final String DOCUMENT_OBJECT_REFERENCE_DOCUMENT_FILE = "referanseDokumentfil";
    public static final String DOCUMENT_OBJECT_CHECKSUM = "sjekksum";
    public static final String DOCUMENT_OBJECT_CHECKSUM_ALGORITHM = "sjekksumAlgoritme";
    public static final String DOCUMENT_OBJECT_FILE_SIZE = "filstoerrelse";

    // The following may or may not be part of the official
    // standard. Sent an email to find out but are specified in n5v5
    // tjenestegrensesnitt, but not in metadata catalogues and
    // therefore lack official identifying numbers
    public static final String DOCUMENT_OBJECT_FILE_NAME = "filnavn";
    public static final String DOCUMENT_OBJECT_MIME_TYPE = "mimeType";

    // Other Noark Objects

    // Precedence
    public static final String PRECEDENCE = "presedens";
    public static final String PRECEDENCE_DATE = "presedensDato";
    public static final String PRECEDENCE_AUTHORITY = "presedensHjemmel";
    public static final String PRECEDENCE_SOURCE_OF_LAW = "rettskildefaktor";
    public static final String PRECEDENCE_APPROVED_DATE = "presedensGodkjentDato";
    public static final String PRECEDENCE_APPROVED_BY = "presedensGodkjentAv";
    public static final String PRECEDENCE_REFERENCE_APPROVED_BY = "referansePresedensGodkjentAv";
    public static final String PRECEDENCE_PRECEDENCE_STATUS = "presedensStatus";
    public static final String PRECEDENCE_STATUS = "presedensstatus";

    // Disposal
    public static final String DISPOSAL = "kassasjon";
    public static final String DISPOSAL_DECISION = "kassasjonsvedtak";
    public static final String DISPOSAL_AUTHORITY = "kassasjonshjemmel";
    public static final String DISPOSAL_PRESERVATION_TIME = "bevaringstid";
    public static final String DISPOSAL_DATE = "kassasjonsdato";

    // DisposalUndertaken
    public static final String DISPOSAL_UNDERTAKEN = "utfoertKassasjon";
    public static final String DISPOSAL_UNDERTAKEN_BY = "kassertAv";
    public static final String DISPOSAL_UNDERTAKEN_DATE = "kassertDato";

    // Screening
    public static final String SCREENING = "skjerming";
    public static final String SCREENING_METADATA = "skjermingmetadata";
    public static final String SCREENING_DOCUMENT = "skjermingdokument";
    public static final String SCREENING_ACCESS_RESTRICTION = "tilgangsrestriksjon";
    public static final String SCREENING_AUTHORITY = "skjermingshjemmel";
    public static final String SCREENING_SCREENING_METADATA = "skjermingMetadata";
    public static final String SCREENING_SCREENING_DOCUMENT = "skjermingDokument";
    public static final String NEW_SCREENING_METADATA = NEW + DASH +
            SCREENING_METADATA;
    public static final String NEW_SCREENING_DOCUMENT = NEW + DASH +
            SCREENING_DOCUMENT;

    public static final String SCREENING_EXPIRES_DATE = "skjermingOpphoererDato";
    public static final String SCREENING_DURATION = "skjermingsvarighet";

    // Deletion
    public static final String DELETION = "sletting";
    public static final String DELETION_TYPE = "slettingstype";
    public static final String DELETION_BY = "slettetAv";
    public static final String DELETION_DATE = "slettetDato";

    // Comment
    public static final String COMMENT = "merknad";
    public static final String COMMENT_TEXT = "merknadstekst";
    public static final String COMMENT_TYPE = "merknadstype";
    public static final String COMMENT_DATE = "merknadsdato";
    public static final String COMMENT_REGISTERED_BY = "merknadRegistrertAv";

    // Classified
    public static final String CLASSIFIED = "gradering"; // root node
    public static final String CLASSIFICATION = "graderingskode"; // property node
    public static final String CLASSIFICATION_DATE = "graderingsdato";
    public static final String CLASSIFICATION_BY = "gradertAv";
    public static final String CLASSIFICATION_DOWNGRADED_DATE = "nedgraderingsdato";
    public static final String CLASSIFICATION_DOWNGRADED_BY = "nedgradertAv";

    // CrossReference
    public static final String CROSS_REFERENCE = "kryssreferanse";
    public static final String CROSS_REFERENCES = "kryssreferanser";

    // ElectronicSignature
    public static final String ELECTRONIC_SIGNATURE = "elektroniskSignatur";
    public static final String ELECTRONIC_SIGNATURE_SECURITY_LEVEL = "elektronisksignatursikkerhetsnivaa";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED = "elektronisksignaturverifisert";
    public static final String ELECTRONIC_SIGNATURE_SECURITY_LEVEL_FIELD = "elektroniskSignaturSikkerhetsnivaa";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED_FIELD = "elektroniskSignaturVerifisert";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED_DATE = "verifisertDato";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED_BY = "verifisertAv";

    // Meeting
    public static final String MEETING_FILE_TYPE = "moetesakstype";
    public static final String MEETING_PARTICIPANT_FUNCTION = "moetedeltakerfunksjon";
    public static final String MEETING_REGISTRATION_STATUS = "moteregistreringsstatus";
    public static final String MEETING_REGISTRATION_TYPE = "moeteregistreringstype";

    // FondsCreator
    public static final String FONDS_CREATOR = "arkivskaper";
    public static final String FONDS_CREATOR_ID = "arkivskaperID";
    public static final String FONDS_CREATOR_NAME = "arkivskaperNavn";

    // SignOff
    public static final String SIGN_OFF = "avskrivning";
    public static final String SIGN_OFF_DATE = "avskrivningsdato";
    public static final String SIGN_OFF_BY = "avskrevetAv";
    public static final String SIGN_OFF_METHOD = "avskrivningsmaate";
    public static final String SIGN_OFF_REFERENCE_RECORD = "referanseAvskrivesAvJournalpost";
    public static final String SIGN_OFF_REFERENCE_CORRESPONDENCE_PART = "referanseAvskrivesAvKorrespondansepart";

    // DocumentFlow
    public static final String DOCUMENT_FLOW = "dokumentflyt";
    public static final String DOCUMENT_FLOW_FLOW_TO = "flytTil";
    public static final String DOCUMENT_FLOW_REFERENCE_FLOW_FROM = "referanseFlytFra";
    public static final String DOCUMENT_FLOW_FLOW_FROM = "flytFra";
    public static final String DOCUMENT_FLOW_REFERENCE_FLOW_TO = "referanseFlytTil";
    public static final String DOCUMENT_FLOW_FLOW_RECEIVED_DATE = "flytMottattDato";
    public static final String DOCUMENT_FLOW_FLOW_SENT_DATE = "flytSendtDato";
    public static final String DOCUMENT_FLOW_FLOW_STATUS = "flytStatus";
    public static final String DOCUMENT_FLOW_FLOW_COMMENT = "flytMerknad";

    // Conversion
    public static final String CONVERSION = "konvertering";
    public static final String CONVERTED_DATE = "konvertertDato";
    public static final String CONVERTED_BY = "konvertertAv";
    public static final String CONVERTED_FROM_FORMAT = "konvertertFraFormat";
    public static final String CONVERTED_TO_FORMAT = "konvertertTilFormat";
    public static final String CONVERSION_TOOL = "konverteringsverktoey";
    public static final String CONVERSION_COMMENT = "konverteringskommentar";

    // Author
    public static final String AUTHOR = "forfatter";
    public static final String NEW_AUTHOR = NEW + DASH + AUTHOR;

    // National Identifier
    // CadastralUnit (Matrikkel)
    public static final String MUNICIPALITY_NUMBER = "kommunenummer";
    public static final String COUNTY_NUMBER = "fylkesnummer";
    public static final String HOLDING_NUMBER = "gaardsnummer";
    public static final String SUB_HOLDING_NUMBER = "bruksnummer";
    public static final String LEASE_NUMBER = "festenummer";
    public static final String SECTION_NUMBER = "seksjonsnummer";

    // Building
    public static final String BUILDING_NUMBER = "bygningsnummer";
    public static final String BUILDING_CHANGE_NUMBER = "endringsloepenummer";
    // dNumber
    public static final String PLAN_IDENTIFICATION = "planidentifikasjon";

    public static final String COORDINATE_SYSTEM = "koordinatsystem";
    public static final String COORDINATE_SYSTEM_ENG_OBJECT =
            "CoordinateSystem";
    public static final String COORDINATE_SYSTEM_ENG =
            "coordinate_system";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String Z = "z";

    public static final String SECONDARY_CLASSIFICATION = "sekundaerklassifikasjon";

    // Constant values defined in the FondsStatus Metadata catalogue

    public static final String FONDS_STATUS_OPEN_CODE = "O";
    public static final String FONDS_STATUS_CLOSED_CODE = "A";
    public static final String FONDS_STATUS_CLOSED = "Avsluttet";

    // Constant values defined in the SeriesStatus Metadata catalogue
    public static final String SERIES_STATUS_ACTIVE_CODE = "A";
    public static final String SERIES_STATUS_CLOSED_CODE = "P";

    // M300 dokumentmedium
    public static final String DOCUMENT_MEDIUM_PHYSICAL = "Fysisk arkiv";
    public static final String DOCUMENT_MEDIUM_ELECTRONIC = "Elektronisk arkiv";
    public static final String DOCUMENT_MEDIUM_ELECTRONIC_CODE = "E";
    public static final String DOCUMENT_MEDIUM_MIXED = "Blandet fysisk og elektronisk arkiv";

    // M217 tilknyttetRegistreringSom
    public static final String MAIN_DOCUMENT_CODE = "H";
    public static final String ATTACHMENT = "Vedlegg";

    // M083 dokumenttype
    public static final String LETTER_CODE = "B";
    public static final String CIRCULAR = "Rundskriv";
    public static final String INVOICE = "Faktura";
    public static final String CONFIRMATION = "Ordrebekreftelser";

    // M054 dokumentstatus
    public static final String DOCUMENT_STATUS_EDIT = "Dokumentet er under redigering";
    public static final String DOCUMENT_STATUS_FINALISED_CODE = "F";

    // M700 variantformat

    public static final String PRODUCTION_VERSION_CODE = "P";
    public static final String ARCHIVE_VERSION_CODE = "A";
    public static final String SCREENED_VERSION = "Dokument hvor deler av innholdet er skjermet";

    public static final String FORMAT = "format";
    public static final String VARIANT_FORMAT = "variantformat";
    public static final String DOCUMENT_STATUS = "dokumentstatus";
    public static final String DOCUMENT_TYPE = "dokumenttype";

    public static final String CLASSIFICATION_SYSTEM_TYPE = "klassifikasjonstype";

    public static final String USER = "bruker";
    public static final String USER_NAME = "brukerNavn";
    public static final String NEW_USER = NEW + DASH + USER;

    public static final String FIRST_NAME = "fornavn";
    public static final String SECOND_NAME = "etternavn";
    public static final String PASSWORD = "passord";

    public static final String RIGHT = "rettighet";
    public static final String NEW_RIGHT = NEW + DASH + RIGHT;

    public static final String ADMINISTRATIVE_UNIT = "administrativenhet";
    public static final String ADMINISTRATIVE_UNIT_FIELD = "administrativEnhet";
    public static final String NEW_ADMINISTRATIVE_UNIT = NEW + DASH + ADMINISTRATIVE_UNIT;

    public static final String ADMINISTRATIVE_UNIT_STATUS = "administrativEnhetsstatus";
    public static final String ADMINISTRATIVE_UNIT_NAME = "administrativEnhetNavn";
    public static final String ADMINISTRATIVE_UNIT_PARENT_REFERENCE = "referanseOverordnetEnhet";

    public static final String SHORT_NAME = "kortnavn";

    /* Logging and tracking */
    public static final String CHANGE_LOG = "endringslogg";
    public static final String NEW_CHANGE_LOG = NEW + CHANGE_LOG;
    public static final String REFERENCE_ARCHIVE_UNIT = "referanseArkivenhet";
    public static final String REFERENCE_METADATA = "referanseMetadata";
    public static final String CHANGED_DATE = "endretDato";
    public static final String CHANGED_BY = "endretAv";
    public static final String REFERENCE_CHANGED_BY = "referanseEndretAv";
    public static final String OLD_VALUE = "tidligereVerdi";
    public static final String NEW_VALUE = "nyVerdi";

    public static final String EVENT_LOG = "hendelseslogg";
    public static final String NEW_EVENT_LOG = NEW + EVENT_LOG;
    public static final String EVENT_DATE = "hendelseDato";

    // English version of above, sorted alphabetically. The following are the
    // table / column names within the Noark domain model
    // TODO: Do the actual translation job,
    public static final String ACCESS_CATEGORY_ENG = "access_category";
    public static final String ACCESS_RESTRICTION_ENG = "access_restriction";
    public static final String ADDRESS_LINE_1_ENG = "adresselinje1";
    public static final String ADDRESS_LINE_2_ENG = "adresselinje2";
    public static final String ADDRESS_LINE_3_ENG = "adresselinje3";
    public static final String ADMINISTRATIVE_UNIT_FIELD_ENG = "administrative_unit";
    public static final String ADMINISTRATIVE_UNIT_NAME_ENG = "administrativEnhetNavn";
    public static final String ADMINISTRATIVE_UNIT_PARENT_REFERENCE_ENG = "referanseOverordnetEnhet";
    public static final String ADMINISTRATIVE_UNIT_STATUS_ENG = "administrativEnhetsstatus";
    public static final String ASSOCIATED_WITH_RECORD_AS_ENG = "associated_with_record_as";
    public static final String AUTHOR_ENG = "forfatter";
    public static final String BUILDING_CHANGE_NUMBER_ENG = "building_change_number";
    public static final String BUILDING_NUMBER_ENG = "building_number";
    public static final String RECORD_ENG = "Record";
    public static final String RECORD_ID_ENG = "record_id";
    public static final String BUSINESS_ADDRESS_ENG = "forretningsadresse";
    public static final String CASE_DATE_ENG = "case_date";
    public static final String CASE_FILE_ENG = "caseFile";
    public static final String CASE_HANDLER_ENG = "saksbehandler";
    public static final String CASE_LOANED_DATE_ENG = "loaned_date";
    public static final String CASE_LOANED_TO_ENG = "loaned_to";
    public static final String PART_ENG = "part";
    public static final String PART_ID_ENG = "partID";
    public static final String PART_NAME_ENG = "part_name";
    public static final String PART_ROLE_FIELD_ENG = "part_roll";
    public static final String CASE_RECORDS_MANAGEMENT_UNIT_ENG = "records_management_unit";
    public static final String CASE_RESPONSIBLE_ENG = "case_responsible";
    public static final String CASE_SEQUENCE_NUMBER_ENG = "case_sequence_number";
    public static final String CASE_STATUS_ENG = "case_status";
    public static final String CASE_YEAR_ENG = "case_year";
    public static final String CHANGED_DATE_ENG = "changed_date";
    public static final String CHANGED_BY_ENG = "changed_by";
    public static final String CLASS_ENG = "class";
    public static final String CLASS_ID_ENG = "class_id";
    public static final String CLASSIFICATION_BY_ENG = "gradertAv";
    public static final String CLASSIFICATION_DATE_ENG = "graderingsdato";
    public static final String CLASSIFICATION_DOWNGRADED_BY_ENG = "nedgradertAv";
    public static final String CLASSIFICATION_DOWNGRADED_DATE_ENG = "nedgraderingsdato";
    public static final String CLASSIFICATION_ENG = "classification_name"; // property node
    public static final String CLASSIFICATION_SYSTEM_ENG =
            "classification_system";
    public static final String CLASSIFICATION_SYSTEM_TYPE_ENG = "klassifikasjonstype";
    public static final String CLASSIFICATION_TYPE_ENG = "klassifikasjonstype";
    public static final String CLASSIFIED_CODE_ENG = "classifiedcode";
    public static final String CLASSIFIED_ENG = "classified"; // root node
    public static final String CODE_ENG = "code";
    public static final String CODE_NAME_ENG = "code_name";
    public static final String CODE_INACTIVE_ENG = "inactive";
    public static final String COMMENT_DATE_ENG = "merknadsdato";
    public static final String COMMENT_ENG = "merknad";
    public static final String COMMENT_REGISTERED_BY_ENG = "merknadRegistrertAv";
    public static final String COMMENT_TEXT_ENG = "comment_text";
    public static final String COMMENT_TYPE_ENG = "comment_type";
    public static final String CONTACT_INFORMATION_ENG = "contact_information";
    public static final String CONTACT_PERSON_ENG = "contact_person";
    public static final String CONVERSION_COMMENT_ENG = "conversion_comment";
    public static final String CONVERSION_ENG = "konvertering";
    public static final String CONVERSION_TOOL_ENG = "conversion_tool";
    public static final String CONVERTED_BY_ENG = "converted_by";
    public static final String CONVERTED_DATE_ENG = "converted_date";
    public static final String CONVERTED_FROM_FORMAT_ENG = "converted_from_format";
    public static final String CONVERTED_TO_FORMAT_ENG = "converted_to_format";
    public static final String CORRESPONDENCE_PART_ENG = "correspondence_part";
    public static final String CORRESPONDENCE_PART_INTERNAL_ENG = "korrespondansepartintern";
    public static final String CORRESPONDENCE_PART_NAME_ENG = "name";
    public static final String CORRESPONDENCE_PART_PERSON_ENG = "korrespondansepartperson";
    public static final String CORRESPONDENCE_PART_TYPE_ENG = "correspondence_part_type";
    public static final String CORRESPONDENCE_PART_UNIT_ENG = "correspondence_part_unit";
    public static final String COUNTRY_CODE_ENG = "land_code";
    public static final String COUNTRY_ENG = "land";
    public static final String COUNTY_NUMBER_ENG = "county_number";
    public static final String CREATED_BY_ENG = "created_by";
    public static final String CREATED_DATE_ENG = "created_date";
    public static final String CROSS_REFERENCE_CLASS_ENG = "referanseTilKlasse";
    public static final String CROSS_REFERENCE_ENG = "kryssreferanse";
    public static final String CROSS_REFERENCE_FILE_ENG = "referanseTilMappe";
    public static final String CROSS_REFERENCE_RECORD_ENG = "referanseTilRegistrering";
    public static final String DELETION_BY_ENG = "deletion_by";
    public static final String DELETION_DATE_ENG = "deletion_date";
    public static final String DELETION_ENG = "sletting";
    public static final String DELETION_TYPE_ENG = "deletion_type";
    public static final String DESCRIPTION_ENG = "description";
    public static final String DISPOSAL_AUTHORITY_ENG = "kassasjonshjemmel";
    public static final String DISPOSAL_DATE_ENG = "kassasjonsdato";
    public static final String DISPOSAL_DECISION_ENG = "disposal_decision";
    public static final String DISPOSAL_ENG = "kassasjon";
    public static final String DISPOSAL_PRESERVATION_TIME_ENG = "bevaringstid";
    public static final String DISPOSAL_UNDERTAKEN_BY_ENG = "kassertAv";
    public static final String DISPOSAL_UNDERTAKEN_DATE_ENG = "kassertDato";
    public static final String DISPOSAL_UNDERTAKEN_ENG = "utfoertKassasjon";
    public static final String D_NUMBER_FIELD_ENG = "d_number";
    public static final String DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS_ENG = "tilknyttetRegistreringSom";
    public static final String DOCUMENT_DESCRIPTION_ASSOCIATED_BY_ENG = "associated_by";
    public static final String DOCUMENT_DESCRIPTION_ASSOCIATED_DATE_ENG = "association_date";
    public static final String DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER_ENG = "document_number";
    public static final String DOCUMENT_DESCRIPTION_DOCUMENT_TYPE_ENG = "document_type";
    public static final String DOCUMENT_DESCRIPTION_ENG = "documentDescrption";
    public static final String DOCUMENT_DESCRIPTION_STATUS_ENG = "dokumentstatus";
    public static final String DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE_ENG = "external_reference";
    public static final String DOCUMENT_FLOW_ENG = "document_flow";
    public static final String DOCUMENT_FLOW_FLOW_COMMENT_ENG = "flow_comment";
    public static final String DOCUMENT_FLOW_FLOW_FROM_ENG = "flow_from";
    public static final String DOCUMENT_FLOW_FLOW_RECEIVED_DATE_ENG = "flow_received_date";
    public static final String DOCUMENT_FLOW_FLOW_SENT_DATE_ENG = "flow_sent_date";
    public static final String DOCUMENT_FLOW_FLOW_STATUS_ENG = "flow_status";
    public static final String DOCUMENT_FLOW_FLOW_TO_ENG = "flow_to";
    public static final String DOCUMENT_FLOW_REFERENCE_FLOW_FROM_ENG = "reference_flow_from";
    public static final String DOCUMENT_FLOW_REFERENCE_FLOW_TO_ENG = "reference_flow_to";
    public static final String DOCUMENT_MEDIUM_ENG = "document_medium";
    public static final String DOCUMENT_OBJECT_CHECKSUM_ALGORITHM_ENG = "checksum_algorithm";
    public static final String DOCUMENT_OBJECT_CHECKSUM_ENG = "checksum";
    public static final String DOCUMENT_OBJECT_ENG = "documentObject";
    public static final String DOCUMENT_OBJECT_FILE_NAME_ENG =
            "original_filename";
    public static final String DOCUMENT_OBJECT_FILE_SIZE_ENG = "file_size";
    public static final String DOCUMENT_OBJECT_FORMAT_DETAILS_ENG = "format_details";
    public static final String DOCUMENT_OBJECT_FORMAT_ENG = "format";
    public static final String DOCUMENT_OBJECT_MIME_TYPE_ENG = "mime_type";
    public static final String DOCUMENT_OBJECT_REFERENCE_DOCUMENT_FILE_ENG = "reference_document_file";
    public static final String DOCUMENT_OBJECT_VARIANT_FORMAT_ENG = "variantformat";
    public static final String DOCUMENT_OBJECT_VERSION_NUMBER_ENG = "version_number";
    public static final String DOCUMENT_STATUS_ENG = "document_status";
    public static final String DOCUMENT_TYPE_ENG = "dokumenttype";
    public static final String ELECTRONIC_SIGNATURE_ENG = "elektronisksignatur";
    public static final String ELECTRONIC_SIGNATURE_SECURITY_LEVEL_FIELD_ENG = "electronic_signature_security_level_name";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED_BY_ENG = "verified_by";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED_DATE_ENG = "verified_date";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED_FIELD_ENG = "electronic_signature_verified_name";
    public static final String EMAIL_ADDRESS_ENG = "epostadresse";
    public static final String EVENT_DATE_ENG = "event_date";
    public static final String EVENT_TYPE_ENG = "event_type";
    public static final String FILE_ENG = "file";
    public static final String FILE_ID_ENG = "file_id";
    public static final String FILE_PUBLIC_TITLE_ENG = "public_title";
    public static final String FILE_TYPE_ENG = "mappetype";
    public static final String FINALISED_BY_ENG = "finalised_by";
    public static final String FINALISED_DATE_ENG = "finalised_date";
    public static final String FLOW_STATUS_ENG = "flow_status";
    public static final String FONDS_CREATOR_ENG = "fondsCreator";
    public static final String FONDS_CREATOR_ID_ENG = "fonds_creator_id";
    public static final String FONDS_CREATOR_NAME_ENG = "fonds_creator_name";
    public static final String FONDS_ENG = "fonds";
    public static final String FONDS_STATUS_ENG = "fonds_status";
    public static final String FOREIGN_ADDRESS_ENG = "utenlandsadresse";
    public static final String FORMAT_ENG = "format";
    public static final String HOLDING_NUMBER_ENG = "holding_number";
    public static final String KEYWORD_ENG = "noekkelord";
    public static final String LAST_MODIFIED_BY_ENG = "last_modified_by";
    public static final String LAST_MODIFIED_DATE_ENG = "last_modified_date";
    public static final String LEASE_NUMBER_ENG = "lease_number";
    public static final String MEETING_FILE_ENG = "moetemappe";
    public static final String MEETING_FILE_TYPE_ENG = "moetesakstype";
    public static final String MEETING_PARTICIPANT_ENG = "moetedeltager";
    public static final String MEETING_PARTICIPANT_FUNCTION_ENG = "moetedeltakerfunksjon";
    public static final String MEETING_REGISTRATION_ENG = "moeteregistrering";
    public static final String MEETING_REGISTRATION_STATUS_ENG = "moteregistreringsstatus";
    public static final String MEETING_REGISTRATION_TYPE_ENG = "moeteregistreringstype";
    public static final String MOBILE_TELEPHONE_NUMBER_ENG = "mobiltelefon";
    public static final String MUNICIPALITY_NUMBER_ENG = "municipality_number";
    public static final String NAME_ENG = "navn";
    public static final String NEW_VALUE_ENG = "new_value";
    public static final String OLD_VALUE_ENG = "old_value";
    public static final String ORGANISATION_NUMBER_ENG = "organisation_number";
    public static final String PARENT_CLASS_ENG = "overordnetklasse";
    public static final String PLAN_IDENTIFICATION_ENG = "plan_identification";
    public static final String POSTAL_ADDRESS_ENG = "postadresse";
    public static final String POSTAL_NUMBER_ENG = "postnummer";
    public static final String POSTAL_TOWN_ENG = "poststed";
    public static final String POST_CODE_ENG = "postnummer";
    public static final String PRECEDENCE_APPROVED_BY_ENG = "precedence_approved_by";
    public static final String PRECEDENCE_APPROVED_DATE_ENG = "precedence_approved_date";
    public static final String PRECEDENCE_AUTHORITY_ENG = "precedence_authority";
    public static final String PRECEDENCE_DATE_ENG = "precedence_date";
    public static final String PRECEDENCE_ENG = "precedence";
    public static final String PRECEDENCE_SOURCE_OF_LAW_ENG = "source_of_law";
    public static final String PRECEDENCE_REFERENCE_APPROVED_BY_ENG = "precedence_approved_by_reference";
    public static final String PRECEDENCE_PRECEDENCE_STATUS_ENG =
            "precedence_status";
    public static final String PRODUCTION_VERSION_ENG = "Produksjonsformat";
    public static final String RECORD_ARCHIVED_BY_ENG = "archived_by";
    public static final String RECORD_ARCHIVED_DATE_ENG = "archived_date";
    public static final String REFERENCE_ADMINISTRATIVE_UNIT_ENG = "referanseAdministratitivEnhet";
    public static final String REFERENCE_ARCHIVE_UNIT_ENG = "reference_archive_unit";
    public static final String REFERENCE_CHANGED_BY_ENG = "reference_changed_by";
    public static final String REFERENCE_METADATA_ENG = "reference_metadata";
    public static final String REFERENCE_CASE_HANDLER_ENG = "referanseSaksbehandler";
    public static final String REFERENCE_SERIES_ENG = "referanseArkivdel";
    public static final String REGISTRY_ENTRY_DATE_ENG = "journaldato";
    public static final String REGISTRY_ENTRY_DOCUMENT_DATE_ENG = "dokumentetsDato";
    public static final String REGISTRY_ENTRY_DUE_DATE_ENG = "forfallsdato";
    public static final String REGISTRY_ENTRY_ENG = "registryEntry";
    public static final String REGISTRY_ENTRY_NUMBER_ENG = "journalpostnummer";
    public static final String REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS_ENG = "antallVedlegg";
    public static final String REGISTRY_ENTRY_RECEIVED_DATE_ENG = "mottattDato";
    public static final String REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE_ENG = "offentlighetsvurdertDato";
    public static final String REGISTRY_ENTRY_SENT_DATE_ENG = "sendtDato";
    public static final String REGISTRY_ENTRY_SEQUENCE_NUMBER_ENG = "journalsekvensnummer";
    public static final String REGISTRY_ENTRY_STATUS_ENG =
            "registry_entry_status";
    public static final String REGISTRY_ENTRY_TYPE_ENG = "registry_entry_type";
    public static final String REGISTRY_ENTRY_YEAR_ENG = "journalaar";
    public static final String RESIDING_ADDRESS_ENG = "bostedsadresse";
    public static final String RIGHT_ENG = "rettighet";
    public static final String SCREENING_ACCESS_RESTRICTION_ENG = "tilgangsrestriksjon";
    public static final String SCREENING_AUTHORITY_ENG = "skjermingshjemmel";
    public static final String SCREENING_DURATION_ENG = "skjermingsvarighet";
    public static final String SCREENING_ENG = "skjerming";
    public static final String SCREENING_EXPIRES_DATE_ENG = "skjermingOpphoererDato";
    public static final String SCREENING_SCREENING_DOCUMENT_ENG = "screening_document";
    public static final String SCREENING_SCREENING_METADATA_ENG =
            "screening_metadata";
    public static final String SECONDARY_CLASSIFICATION_ENG = "sekundaerklassifikasjon";
    public static final String SECONDARY_CLASSIFICATION_SYSTEM_ENG = "sekundaerklassifikasjonssystem";
    public static final String SECTION_NUMBER_ENG = "section_number";
    public static final String SERIES_END_DATE_ENG = "series_end_date";
    public static final String SERIES_ENG = "series";
    public static final String SERIES_PRECURSOR_ENG = "forloeper";
    public static final String SERIES_START_DATE_ENG = "series_start_date";
    public static final String SERIES_STATUS_ENG = "series_status";
    public static final String SERIES_SUCCESSOR_ENG = "arvtager";
    public static final String SHORT_NAME_ENG = "short_name";
    public static final String SIGN_OFF_BY_ENG = "sign_off_by";
    public static final String SIGN_OFF_DATE_ENG = "sign_off_date";
    public static final String SIGN_OFF_ENG = "avskrivning";
    public static final String STORAGE_LOCATION_ENG = "storage_location";
    public static final String SIGN_OFF_METHOD_ENG = "sign_of_method";
    public static final String SOCIAL_SECURITY_NUMBER_ENG = "SignOfMethod";
    public static final String SUB_CLASS_ENG = "underklasse";
    public static final String SUB_FONDS_ENG = "underarkiv";
    public static final String SUB_HOLDING_NUMBER_ENG = "sub_holding_number";
    public static final String SYSTEM_ID_ENG = "systemID";
    public static final String TELEPHONE_NUMBER_ENG = "telefonnummer";
    public static final String TITLE_ENG = "title";
    public static final String USER_ENG = "user";
    public static final String USER_NAME_ENG = "username";
    public static final String VARIANT_FORMAT_ENG = "variant_format";
    public static final String X_ENG = "x";
    public static final String Y_ENG = "y";
    public static final String Z_ENG = "z";

    // English version of above, sorted alphabetically. The following are the
    // english object names as used within the Noark domain model
    // TODO: Do the actual translation job,
    public static final String ACCESS_CATEGORY_ENG_OBJECT = "AccessCategory";
    public static final String ACCESS_RESTRICTION_ENG_OBJECT =
            "AccessRestriction";
    public static final String ADDRESS_LINE_1_ENG_OBJECT = "adresselinje1";
    public static final String ADDRESS_LINE_2_ENG_OBJECT = "adresselinje2";
    public static final String ADDRESS_LINE_3_ENG_OBJECT = "adresselinje3";
    public static final String ADMINISTRATIVE_UNIT_FIELD_ENG_OBJECT = "administrativeUnit";
    public static final String ADMINISTRATIVE_UNIT_NAME_ENG_OBJECT = "administrativEnhetNavn";
    public static final String ADMINISTRATIVE_UNIT_PARENT_REFERENCE_ENG_OBJECT = "referanseOverordnetEnhet";
    public static final String ADMINISTRATIVE_UNIT_STATUS_ENG_OBJECT = "administrativEnhetsstatus";
    public static final String ASSOCIATED_WITH_RECORD_AS_ENG_OBJECT =
            "AssociatedWithRecordAs";
    public static final String AUTHOR_ENG_OBJECT = "forfatter";
    public static final String BUILDING_CHANGE_NUMBER_ENG_OBJECT = "continuousNumberingOfBuildingChange";
    public static final String BUILDING_NUMBER_ENG_OBJECT = "buildingNumber";
    public static final String RECORD_ENG_OBJECT = "Record";
    public static final String RECORD_ID_ENG_OBJECT = "recordId";
    public static final String BUSINESS_ADDRESS_ENG_OBJECT = "forretningsadresse";
    public static final String CASE_DATE_ENG_OBJECT = "caseDate";
    public static final String CASE_FILE_ENG_OBJECT = "CaseFile";
    public static final String CASE_HANDLER_ENG_OBJECT = "saksbehandler";
    public static final String CASE_LOANED_DATE_ENG_OBJECT = "loanedDate";
    public static final String CASE_LOANED_TO_ENG_OBJECT = "loanedTo";
    public static final String PART_ENG_OBJECT = "part";
    public static final String PART_ID_ENG_OBJECT = "partID";
    public static final String PART_NAME_ENG_OBJECT = "PartName";
    public static final String PART_ROLE_FIELD_ENG_OBJECT = "PartRoll";
    public static final String CASE_RECORDS_MANAGEMENT_UNIT_ENG_OBJECT = "recordsManagementUnit";
    public static final String CASE_RESPONSIBLE_ENG_OBJECT = "caseResponsible";
    public static final String CASE_SEQUENCE_NUMBER_ENG_OBJECT = "caseSequenceNumber";
    public static final String CASE_STATUS_ENG_OBJECT = "CaseStatus";
    public static final String CASE_YEAR_ENG_OBJECT = "caseYear";
    public static final String CHANGED_DATE_ENG_OBJECT = "changedDate";
    public static final String CHANGED_BY_ENG_OBJECT = "changedBy";
    public static final String CLASS_ENG_OBJECT = "Class";
    public static final String CLASS_ID_ENG_OBJECT = "classId";
    public static final String CLASSIFICATION_BY_ENG_OBJECT = "gradertAv";
    public static final String CLASSIFICATION_DATE_ENG_OBJECT = "graderingsdato";
    public static final String CLASSIFICATION_DOWNGRADED_BY_ENG_OBJECT = "nedgradertAv";
    public static final String CLASSIFICATION_DOWNGRADED_DATE_ENG_OBJECT = "nedgraderingsdato";
    public static final String CLASSIFICATION_ENG_OBJECT =
            "ClassificationName"; // property node
    public static final String CLASSIFICATION_SYSTEM_ENG_OBJECT =
            "ClassificationSystem";
    public static final String CLASSIFICATION_SYSTEM_TYPE_ENG_OBJECT = "klassifikasjonstype";
    public static final String CLASSIFICATION_TYPE_ENG_OBJECT = "klassifikasjonstype";
    public static final String CLASSIFIED_CODE_ENG_OBJECT = "classifiedcode";
    public static final String CLASSIFIED_ENG_OBJECT = "classified"; // rootnode
    public static final String CODE_ENG_OBJECT = "code";
    public static final String CODE_NAME_ENG_OBJECT = "codeName";
    public static final String CODE_INACTIVE_ENG_OBJECT = "inactive";
    public static final String COMMENT_DATE_ENG_OBJECT = "merknadsdato";
    public static final String COMMENT_ENG_OBJECT = "merknad";
    public static final String COMMENT_REGISTERED_BY_ENG_OBJECT = "merknadRegistrertAv";
    public static final String COMMENT_TEXT_ENG_OBJECT = "CommentText";
    public static final String COMMENT_TYPE_ENG_OBJECT = "CommentType";
    public static final String CONTACT_INFORMATION_ENG_OBJECT = "kontaktinformasjon";
    public static final String CONTACT_PERSON_ENG_OBJECT = "contactPerson";
    public static final String CONVERSION_COMMENT_ENG_OBJECT = "conversionComment";
    public static final String CONVERSION_ENG_OBJECT = "konvertering";
    public static final String CONVERSION_TOOL_ENG_OBJECT = "conversionTool";
    public static final String CONVERTED_BY_ENG_OBJECT = "convertedBy";
    public static final String CONVERTED_DATE_ENG_OBJECT = "convertedDate";
    public static final String CONVERTED_FROM_FORMAT_ENG_OBJECT = "convertedFromFormat";
    public static final String CONVERTED_TO_FORMAT_ENG_OBJECT = "convertedToFormat";
    public static final String CORRESPONDENCE_PART_ENG_OBJECT = "korrespondansepart";
    public static final String CORRESPONDENCE_PART_INTERNAL_ENG_OBJECT = "korrespondansepartintern";
    public static final String CORRESPONDENCE_PART_NAME_ENG_OBJECT = "name";
    public static final String CORRESPONDENCE_PART_PERSON_ENG_OBJECT = "korrespondansepartperson";
    public static final String CORRESPONDENCE_PART_TYPE_ENG_OBJECT =
            "CorrespondencePartType";
    public static final String CORRESPONDENCE_PART_UNIT_ENG_OBJECT =
            "CorrespondencePartUnit";
    public static final String COUNTRY_CODE_ENG_OBJECT = "CountryCode";
    public static final String COUNTRY_ENG_OBJECT = "land";
    public static final String COUNTY_NUMBER_ENG_OBJECT = "countyNumber";
    public static final String CREATED_BY_ENG_OBJECT = "createdBy";
    public static final String CREATED_DATE_ENG_OBJECT = "createdDate";
    public static final String CROSS_REFERENCE_CLASS_ENG_OBJECT = "referanseTilKlasse";
    public static final String CROSS_REFERENCE_ENG_OBJECT = "kryssreferanse";
    public static final String CROSS_REFERENCE_FILE_ENG_OBJECT = "referanseTilMappe";
    public static final String CROSS_REFERENCE_RECORD_ENG_OBJECT = "referanseTilRegistrering";
    public static final String DELETION_BY_ENG_OBJECT = "deletionBy";
    public static final String DELETION_DATE_ENG_OBJECT = "deletionDate";
    public static final String DELETION_ENG_OBJECT = "sletting";
    public static final String DELETION_TYPE_ENG_OBJECT = "DeletionType";
    public static final String DESCRIPTION_ENG_OBJECT = "description";
    public static final String DISPOSAL_AUTHORITY_ENG_OBJECT =
            "DisposalAuthority";
    public static final String DISPOSAL_DATE_ENG_OBJECT = "DisposalDate";
    public static final String DISPOSAL_DECISION_ENG_OBJECT =
            "DisposalDecision";
    public static final String DISPOSAL_ENG_OBJECT = "kassasjon";
    public static final String DISPOSAL_PRESERVATION_TIME_ENG_OBJECT = "bevaringstid";
    public static final String DISPOSAL_UNDERTAKEN_BY_ENG_OBJECT = "kassertAv";
    public static final String DISPOSAL_UNDERTAKEN_DATE_ENG_OBJECT = "kassertDato";
    public static final String DISPOSAL_UNDERTAKEN_ENG_OBJECT = "utfoertKassasjon";
    public static final String D_NUMBER_FIELD_ENG_OBJECT = "dNumber";
    public static final String DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS_ENG_OBJECT = "tilknyttetRegistreringSom";
    public static final String DOCUMENT_DESCRIPTION_ASSOCIATED_BY_ENG_OBJECT = "associatedBy";
    public static final String DOCUMENT_DESCRIPTION_ASSOCIATED_DATE_ENG_OBJECT = "associationDate";
    public static final String DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER_ENG_OBJECT = "documentNumber";
    public static final String DOCUMENT_DESCRIPTION_DOCUMENT_TYPE_ENG_OBJECT
            = "DocumentType";
    public static final String DOCUMENT_DESCRIPTION_ENG_OBJECT =
            "DocumentDescription";
    public static final String DOCUMENT_DESCRIPTION_STATUS_ENG_OBJECT = "dokumentstatus";
    public static final String DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE_ENG_OBJECT = "externalReference";
    public static final String DOCUMENT_FLOW_ENG_OBJECT = "dokumentflyt";
    public static final String DOCUMENT_FLOW_FLOW_COMMENT_ENG_OBJECT = "flowComment";
    public static final String DOCUMENT_FLOW_FLOW_FROM_ENG_OBJECT = "flowFrom";
    public static final String DOCUMENT_FLOW_FLOW_RECEIVED_DATE_ENG_OBJECT = "flowReceivedDate";
    public static final String DOCUMENT_FLOW_FLOW_SENT_DATE_ENG_OBJECT = "flowSentDate";
    public static final String DOCUMENT_FLOW_FLOW_STATUS_ENG_OBJECT = "flowStatus";
    public static final String DOCUMENT_FLOW_FLOW_TO_ENG_OBJECT = "flowTo";
    public static final String DOCUMENT_MEDIUM_ENG_OBJECT = "DocumentMedium";
    public static final String DOCUMENT_OBJECT_CHECKSUM_ALGORITHM_ENG_OBJECT = "checksumAlgorithm";
    public static final String DOCUMENT_OBJECT_CHECKSUM_ENG_OBJECT = "checksum";
    public static final String DOCUMENT_OBJECT_ENG_OBJECT = "DocumentObject";
    public static final String DOCUMENT_OBJECT_FILE_NAME_ENG_OBJECT = "originalFilename";
    public static final String DOCUMENT_OBJECT_FILE_SIZE_ENG_OBJECT = "fileSize";
    public static final String DOCUMENT_OBJECT_FORMAT_DETAILS_ENG_OBJECT = "formatDetails";
    public static final String DOCUMENT_OBJECT_FORMAT_ENG_OBJECT = "format";
    public static final String DOCUMENT_OBJECT_MIME_TYPE_ENG_OBJECT = "mimeType";
    public static final String DOCUMENT_OBJECT_REFERENCE_DOCUMENT_FILE_ENG_OBJECT = "referenceDocumentFile";
    public static final String DOCUMENT_OBJECT_VARIANT_FORMAT_ENG_OBJECT = "variantformat";
    public static final String DOCUMENT_OBJECT_VERSION_NUMBER_ENG_OBJECT = "versionNumber";
    public static final String DOCUMENT_STATUS_ENG_OBJECT = "DocumentStatus";
    public static final String DOCUMENT_TYPE_ENG_OBJECT = "DocumentType";
    public static final String ELECTRONIC_SIGNATURE_ENG_OBJECT =
            "ElectronicSignature";
    public static final String ELECTRONIC_SIGNATURE_SECURITY_LEVEL_FIELD_ENG_OBJECT
            = "ElectronicSignatureSecurityLevelName";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED_BY_ENG_OBJECT = "verifiedBy";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED_DATE_ENG_OBJECT = "verifiedDate";
    public static final String ELECTRONIC_SIGNATURE_VERIFIED_FIELD_ENG_OBJECT = "electronicSignatureVerifiedName";
    public static final String EMAIL_ADDRESS_ENG_OBJECT = "epostadresse";
    public static final String EVENT_DATE_ENG_OBJECT = "eventDate";
    public static final String EVENT_TYPE_ENG_OBJECT = "EventType";
    public static final String FILE_ENG_OBJECT = "File";
    public static final String FILE_ID_ENG_OBJECT = "fileId";
    public static final String FILE_PUBLIC_TITLE_ENG_OBJECT = "publicTitle";
    public static final String FILE_TYPE_ENG_OBJECT = "mappetype";
    public static final String FINALISED_BY_ENG_OBJECT = "finalisedBy";
    public static final String FINALISED_DATE_ENG_OBJECT = "finalisedDate";
    public static final String FLOW_STATUS_ENG_OBJECT = "FlowStatus";
    public static final String FONDS_CREATOR_ENG_OBJECT = "FondsCreator";
    public static final String FONDS_CREATOR_ID_ENG_OBJECT = "fondsCreatorId";
    public static final String FONDS_CREATOR_NAME_ENG_OBJECT = "fondsCreatorName";
    public static final String FONDS_ENG_OBJECT = "Fonds";
    public static final String FONDS_STATUS_ENG_OBJECT = "FondsStatus";
    public static final String FOREIGN_ADDRESS_ENG_OBJECT = "utenlandsadresse";
    public static final String FORMAT_ENG_OBJECT = "Format";
    public static final String HOLDING_NUMBER_ENG_OBJECT = "holdingNumber";
    public static final String KEYWORD_ENG_OBJECT = "noekkelord";
    public static final String LAST_MODIFIED_BY_ENG_OBJECT = "lastModifiedBy";
    public static final String LAST_MODIFIED_DATE_ENG_OBJECT = "lastModifiedDate";
    public static final String LEASE_NUMBER_ENG_OBJECT = "leaseNumber";
    public static final String MEETING_FILE_ENG_OBJECT = "moetemappe";
    public static final String MEETING_FILE_TYPE_ENG_OBJECT = "moetesakstype";
    public static final String MEETING_PARTICIPANT_ENG_OBJECT = "moetedeltager";
    public static final String MEETING_PARTICIPANT_FUNCTION_ENG_OBJECT = "moetedeltakerfunksjon";
    public static final String MEETING_REGISTRATION_ENG_OBJECT = "moeteregistrering";
    public static final String MEETING_REGISTRATION_STATUS_ENG_OBJECT = "moteregistreringsstatus";
    public static final String MEETING_REGISTRATION_TYPE_ENG_OBJECT = "moeteregistreringstype";
    public static final String MOBILE_TELEPHONE_NUMBER_ENG_OBJECT = "mobiltelefon";
    public static final String MUNICIPALITY_NUMBER_ENG_OBJECT = "municipalityNumber";
    public static final String NAME_ENG_OBJECT = "navn";
    public static final String NEW_VALUE_ENG_OBJECT = "newValue";
    public static final String OLD_VALUE_ENG_OBJECT = "oldValue";
    public static final String ORGANISATION_NUMBER_ENG_OBJECT = "organisationNumber";
    public static final String PARENT_CLASS_ENG_OBJECT = "overklasse";
    public static final String PLAN_IDENTIFICATION_ENG_OBJECT = "planIdentification";
    public static final String POSTAL_ADDRESS_ENG_OBJECT = "postadresse";
    public static final String POSTAL_NUMBER_ENG_OBJECT = "postnummer";
    public static final String POSTAL_TOWN_ENG_OBJECT = "poststed";
    public static final String POST_CODE_ENG_OBJECT = "postnummer";
    public static final String PRECEDENCE_APPROVED_BY_ENG_OBJECT = "precedenceApprovedBy";
    public static final String PRECEDENCE_APPROVED_DATE_ENG_OBJECT = "precedenceApprovedDate";
    public static final String PRECEDENCE_AUTHORITY_ENG_OBJECT = "precedenceAuthority";
    public static final String PRECEDENCE_DATE_ENG_OBJECT = "precedenceDate";
    public static final String PRECEDENCE_ENG_OBJECT = "precedence";
    public static final String PRECEDENCE_SOURCE_OF_LAW_ENG_OBJECT = "sourceOfLaw";
    public static final String PRECEDENCE_REFERENCE_APPROVED_BY_ENG_OBJECT = "referencePrecedenceApprovedBySystemID";
    public static final String PRECEDENCE_PRECEDENCE_STATUS_ENG_OBJECT =
            "PrecedenceStatus";
    public static final String PRODUCTION_VERSION_ENG_OBJECT = "Produksjonsformat";
    public static final String RECORD_ARCHIVED_BY_ENG_OBJECT = "arkivertAv";
    public static final String RECORD_ARCHIVED_DATE_ENG_OBJECT = "archivedDate";
    public static final String REFERENCE_ADMINISTRATIVE_UNIT_ENG_OBJECT = "referanseAdministratitivEnhet";
    public static final String REFERENCE_ARCHIVE_UNIT_ENG_OBJECT = "referenceArchiveUnit";
    public static final String REFERENCE_CHANGED_BY_ENG_OBJECT = "referenceChangedBy";
    public static final String REFERENCE_METADATA_ENG_OBJECT = "referenceMetadata";
    public static final String REFERENCE_CASE_HANDLER_ENG_OBJECT = "referanseSaksbehandler";
    public static final String REFERENCE_SERIES_ENG_OBJECT = "referanseArkivdel";
    public static final String REGISTRY_ENTRY_DATE_ENG_OBJECT = "journaldato";
    public static final String REGISTRY_ENTRY_DOCUMENT_DATE_ENG_OBJECT = "dokumentetsDato";
    public static final String REGISTRY_ENTRY_DUE_DATE_ENG_OBJECT = "forfallsdato";
    public static final String REGISTRY_ENTRY_ENG_OBJECT = "RegistryEntry";
    public static final String REGISTRY_ENTRY_NUMBER_ENG_OBJECT = "journalpostnummer";
    public static final String REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS_ENG_OBJECT = "antallVedlegg";
    public static final String REGISTRY_ENTRY_RECEIVED_DATE_ENG_OBJECT = "mottattDato";
    public static final String REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE_ENG_OBJECT = "offentlighetsvurdertDato";
    public static final String REGISTRY_ENTRY_SENT_DATE_ENG_OBJECT = "sendtDato";
    public static final String REGISTRY_ENTRY_SEQUENCE_NUMBER_ENG_OBJECT = "journalsekvensnummer";
    public static final String REGISTRY_ENTRY_STATUS_ENG_OBJECT =
            "RegistryEntryStatus";
    public static final String REGISTRY_ENTRY_TYPE_ENG_OBJECT =
            "RegistryEntryType";
    public static final String REGISTRY_ENTRY_YEAR_ENG_OBJECT = "journalaar";
    public static final String RESIDING_ADDRESS_ENG_OBJECT = "bostedsadresse";
    public static final String RIGHT_ENG_OBJECT = "rettighet";
    public static final String SCREENING_ACCESS_RESTRICTION_ENG_OBJECT = "tilgangsrestriksjon";
    public static final String SCREENING_AUTHORITY_ENG_OBJECT = "skjermingshjemmel";
    public static final String SCREENING_DURATION_ENG_OBJECT = "skjermingsvarighet";
    public static final String SCREENING_ENG_OBJECT = "skjerming";
    public static final String SCREENING_EXPIRES_DATE_ENG_OBJECT = "skjermingOpphoererDato";
    public static final String SCREENING_SCREENING_DOCUMENT_ENG_OBJECT =
            "ScreeningDocument";
    public static final String SCREENING_SCREENING_METADATA_ENG_OBJECT = "ScreeningMetadata";
    public static final String SECONDARY_CLASSIFICATION_ENG_OBJECT = "sekundaerklassifikasjon";
    public static final String SECONDARY_CLASSIFICATION_SYSTEM_ENG_OBJECT = "sekundaerklassifikasjonssystem";
    public static final String SECTION_NUMBER_ENG_OBJECT = "sectionNumber";
    public static final String SERIES_END_DATE_ENG_OBJECT = "seriesEndDate";
    public static final String SERIES_ENG_OBJECT = "Series";
    public static final String SERIES_PRECURSOR_ENG_OBJECT = "forloeper";
    public static final String SERIES_START_DATE_ENG_OBJECT = "seriesStartDate";
    public static final String SERIES_STATUS_ENG_OBJECT = "Seriesstatus";
    public static final String SERIES_SUCCESSOR_ENG_OBJECT = "arvtager";
    public static final String SHORT_NAME_ENG_OBJECT = "shortName";
    public static final String SIGN_OFF_BY_ENG_OBJECT = "signOffBy";
    public static final String SIGN_OFF_DATE_ENG_OBJECT = "signOffDate";
    public static final String SOCIAL_SECURITY_NUMBER_ENG_OBJECT = "socialSecurityNumber";
    public static final String SIGN_OFF_ENG_OBJECT = "avskrivning";
    public static final String STORAGE_LOCATION_ENG_OBJECT = "storageLocation";
    public static final String SIGN_OFF_METHOD_ENG_OBJECT = "avskrivningsmaate";
    public static final String SUB_CLASS_ENG_OBJECT = "underklasse";
    public static final String SUB_FONDS_ENG_OBJECT = "underarkiv";
    public static final String SUB_HOLDING_NUMBER_ENG_OBJECT = "subHoldingNumber";
    public static final String SYSTEM_ID_ENG_OBJECT = "systemId";
    public static final String TELEPHONE_NUMBER_ENG_OBJECT = "telefonnummer";
    public static final String TITLE_ENG_OBJECT = "title";
    public static final String USER_ENG_OBJECT = "bruker";
    public static final String USER_NAME_ENG_OBJECT = "brukerNavn";
    public static final String VARIANT_FORMAT_ENG_OBJECT = "variantformat";
    public static final String X_ENG_OBJECT = "x";
    public static final String Y_ENG_OBJECT = "y";
    public static final String Z_ENG_OBJECT = "z";

    public static final String REFERENCE_CLASSIFICATION_SYSTEM
            = "referenceClassificationSystem";
    private N5ResourceMappings() {
        throw new AssertionError();
    }
}

