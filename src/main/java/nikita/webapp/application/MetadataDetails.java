package nikita.webapp.application;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.webapp.util.serialisers.APIDetailsSerializer;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@JsonSerialize(using = APIDetailsSerializer.class)
public class MetadataDetails extends APIDetails {

    public MetadataDetails(String publicUrlPath) {
        super();
        // Add support for DocumentMedium
        metadataRel(publicUrlPath,
                DOCUMENT_MEDIUM,
                NEW_DOCUMENT_MEDIUM,
                REL_METADATA_DOCUMENT_MEDIUM);

        // Add support for FondsStatus
        metadataRel(publicUrlPath,
                FONDS_STATUS,
                NEW_FONDS_STATUS,
                REL_METADATA_FONDS_STATUS);


        // Add support for DocumentStatus
        metadataRel(publicUrlPath,
                DOCUMENT_STATUS,
                NEW_DOCUMENT_STATUS,
                REL_METADATA_DOCUMENT_STATUS);

        // Add support for DocumentType
        metadataRel(publicUrlPath,
                DOCUMENT_TYPE,
                NEW_DOCUMENT_TYPE,
                REL_METADATA_DOCUMENT_TYPE);

        // Add support for EventType
        metadataRel(publicUrlPath,
                EVENT_TYPE,
                NEW_EVENT_TYPE,
                REL_METADATA_EVENT_TYPE);

        // Add support for AccessCategory
        metadataRel(publicUrlPath,
                ACCESS_CATEGORY,
                NEW_ACCESS_CATEGORY,
                REL_METADATA_ACCESS_CATEGORY);

        // Add support for AssociatedWithRecordAs
        metadataRel(publicUrlPath,
                ASSOCIATED_WITH_RECORD_AS,
                NEW_ASSOCIATED_WITH_RECORD_AS,
                REL_METADATA_ASSOCIATED_WITH_RECORD_AS);

        // Add support for SeriesStatus
        metadataRel(publicUrlPath,
                SERIES_STATUS,
                NEW_SERIES_STATUS,
                REL_METADATA_SERIES_STATUS);

        // Add support for RegistryEntryStatus
        metadataRel(publicUrlPath,
                REGISTRY_ENTRY_STATUS,
                NEW_REGISTRY_ENTRY_STATUS,
                REL_METADATA_REGISTRY_ENTRY_STATUS);

        // Add support for precedenceStatus
        metadataRel(publicUrlPath,
                PRECEDENCE_STATUS,
                NEW_PRECEDENCE_STATUS,
                REL_METADATA_PRECEDENCE_STATUS);

        // Add support for accessRestriction
        metadataRel(publicUrlPath,
                ACCESS_RESTRICTION,
                NEW_ACCESS_RESTRICTION,
                REL_METADATA_ACCESS_RESTRICTION);

        // Add support for disposalDecision
        metadataRel(publicUrlPath,
                DISPOSAL_DECISION,
                NEW_DISPOSAL_DECISION,
                REL_METADATA_DISPOSAL_DECISION);

        // Add support for CorrespondencePartType
        metadataRel(publicUrlPath,
                CORRESPONDENCE_PART_TYPE,
                NEW_CORRESPONDENCE_PART_TYPE,
                REL_METADATA_CORRESPONDENCE_PART_TYPE);

        // Add support for SignOffMethod
        metadataRel(publicUrlPath,
                SIGN_OFF_METHOD,
                NEW_SIGN_OFF_METHOD,
                REL_METADATA_SIGN_OFF_METHOD);

        // Add support for ElectronicSignatureSecurityLevel
        metadataRel(publicUrlPath,
                ELECTRONIC_SIGNATURE_SECURITY_LEVEL,
                NEW_ELECTRONIC_SIGNATURE_SECURITY_LEVEL,
                REL_METADATA_ELECTRONIC_SIGNATURE_SECURITY_LEVEL);

        // Add support for ElectronicSignatureVerified
        metadataRel(publicUrlPath,
                ELECTRONIC_SIGNATURE_VERIFIED,
                NEW_ELECTRONIC_SIGNATURE_VERIFIED,
                REL_METADATA_ELECTRONIC_SIGNATURE_VERIFIED);

        // Add support for Format
        metadataRel(publicUrlPath,
                FORMAT,
                NEW_FORMAT,
                REL_METADATA_FORMAT);

        // Add support for FlowStatus
        metadataRel(publicUrlPath,
                FLOW_STATUS,
                NEW_FLOW_STATUS,
                REL_METADATA_FLOW_STATUS);

        // Add support for RegistryEntryType
        metadataRel(publicUrlPath,
                REGISTRY_ENTRY_TYPE,
                NEW_REGISTRY_ENTRY_TYPE,
                REL_METADATA_REGISTRY_ENTRY_TYPE);

        // Add support for PartRole
        metadataRel(publicUrlPath,
                PART_ROLE,
                NEW_PART_ROLE,
                REL_METADATA_PART_ROLE);

        // Add support for ClassificationType
        metadataRel(publicUrlPath,
                CLASSIFICATION_TYPE,
                NEW_CLASSIFICATION_TYPE,
                REL_METADATA_CLASSIFICATION_TYPE);

        // Add support for FileType
        metadataRel(publicUrlPath,
                FILE_TYPE,
                NEW_FILE_TYPE,
                REL_METADATA_FILE_TYPE);

        // Add support for VariantFormat
        metadataRel(publicUrlPath,
                VARIANT_FORMAT,
                NEW_VARIANT_FORMAT,
                REL_METADATA_VARIANT_FORMAT);

        // Add support for CommentType
        metadataRel(publicUrlPath,
                COMMENT_TYPE,
                NEW_COMMENT_TYPE,
                REL_METADATA_COMMENT_TYPE);

        // Add support for CaseStatus
        metadataRel(publicUrlPath,
                CASE_STATUS,
                NEW_CASE_STATUS,
                REL_METADATA_CASE_STATUS);

        // Add support for Country
        metadataRel(publicUrlPath,
                COUNTRY,
                NEW_COUNTRY,
                REL_METADATA_COUNTRY);

        // Add support for PostCode
        metadataRel(publicUrlPath,
                POST_CODE,
                NEW_POST_CODE,
                REL_METADATA_POST_CODE);

        // Add support for ScreeningMetadata
        metadataRel(publicUrlPath,
                SCREENING_METADATA,
                NEW_SCREENING_METADATA,
                REL_METADATA_SCREENING_METADATA);

        // Add support for ScreeningDocument
        metadataRel(publicUrlPath,
                SCREENING_DOCUMENT,
                NEW_SCREENING_DOCUMENT,
                REL_METADATA_SCREENING_DOCUMENT);

        // Add support for ClassifiedCode
        metadataRel(publicUrlPath,
                CLASSIFIED_CODE,
                NEW_CLASSIFIED_CODE,
                REL_METADATA_CLASSIFIED_CODE);

        // Add support for AccessCateogory
        metadataRel(publicUrlPath,
                ACCESS_CATEGORY,
                NEW_ACCESS_CATEGORY,
                REL_METADATA_ACCESS_CATEGORY);

        // Add support for DeletionType
        metadataRel(publicUrlPath,
                DELETION_TYPE,
                NEW_DELETION_TYPE,
                REL_METADATA_DELETION_TYPE);

        // Add support for DisposalDecision
        metadataRel(publicUrlPath,
                DISPOSAL_DECISION,
                NEW_DISPOSAL_DECISION,
                REL_METADATA_DISPOSAL_DECISION);

        // Add support for EventType
        metadataRel(publicUrlPath,
                EVENT_TYPE,
                NEW_EVENT_TYPE,
                REL_METADATA_EVENT_TYPE);

        // Add support for CoordinateSystem
        metadataRel(publicUrlPath,
                COORDINATE_SYSTEM,
                NEW_COORDINATE_SYSTEM,
                REL_METADATA_COORDINATE_SYSTEM);
    }

    private void
    metadataRel(String publicUrlPath,
                String listpath, String newpath,
                String officialrelation) {
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_METADATA + SLASH + listpath,
                officialrelation, true));
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_METADATA + SLASH + newpath,
                NIKITA_CONFORMANCE_REL + NOARK_METADATA_PATH + SLASH + newpath + SLASH,
                false));
    }
}
