package utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.metadata.AssociatedWithRecordAs;
import nikita.common.model.noark5.v5.metadata.DocumentStatus;
import nikita.common.model.noark5.v5.metadata.DocumentType;

import java.io.IOException;
import java.io.StringWriter;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;
import static utils.TestConstants.*;

public final class DocumentDescriptionCreator {

    /**
     * Create a default DocumentDescription for testing purposes
     *
     * @return a DocumentDescription with all values set
     */
    public static DocumentDescription createDocumentDescription() {
        DocumentDescription documentDescription = new DocumentDescription();
        documentDescription.setTitle(TITLE_TEST);
        AssociatedWithRecordAs associatedWithRecordAs =
                new AssociatedWithRecordAs();
        associatedWithRecordAs.setCode(DOCUMENT_ASS_REC_CODE_TEST);
        associatedWithRecordAs.setCodeName(DOCUMENT_ASS_REC_CODE_NAME_TEST);
        documentDescription.setAssociatedWithRecordAs(associatedWithRecordAs);
        DocumentType documentType = new DocumentType();
        documentType.setCode(DOCUMENT_TYPE_CODE_TEST);
        documentType.setCodeName(DOCUMENT_TYPE_CODE_NAME_TEST);
        documentDescription.setDocumentType(documentType);
        DocumentStatus documentStatus = new DocumentStatus();
        documentStatus.setCode(DOCUMENT_STATUS_CODE_TEST);
        documentStatus.setCodeName(DOCUMENT_STATUS_CODE_NAME_TEST);
        documentDescription.setDocumentStatus(documentStatus);
        documentDescription.setStorageLocation(STORAGE_LOCATION_TEST);
        return documentDescription;
    }

    public static String createDocumentDescriptionAsJSON() throws IOException {
        return createDocumentDescriptionAsJSON(createDocumentDescription());
    }

    public static String createDocumentDescriptionAsJSON(
            DocumentDescription documentDescription) throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonWriter);
        jgen.writeStartObject();
        printTitleAndDescription(jgen, documentDescription);
        printNullableMetadata(jgen,
                DOCUMENT_DESCRIPTION_DOCUMENT_TYPE,
                documentDescription.getDocumentType());
        printNullableMetadata(jgen,
                DOCUMENT_DESCRIPTION_STATUS,
                documentDescription.getDocumentStatus());
        printNullable(jgen, DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER,
                documentDescription.getDocumentNumber());
        printNullableMetadata(jgen,
                DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS,
                documentDescription.getAssociatedWithRecordAs());
        printNullable(jgen, STORAGE_LOCATION, documentDescription
                .getStorageLocation());
        jgen.writeEndObject();
        jgen.close();
        return jsonWriter.toString();
    }
}
