package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing DocumentDescription object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the
 * output. We need to be able to especially control the HATEOAS links and the
 * actual format of the HATEOAS links might change over time with the standard.
 * This allows us to be able to easily adapt to any changes
 * <p>
 * Only Norwegian property names are used on the outgoing JSON property names
 * and are in accordance with the Noark standard.
 * <p>
 * Note. Only values that are part of the standard are included in the JSON.
 * Properties like 'id' or 'deleted' are not exported
 */

public class DocumentDescriptionHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkEntity,
            HateoasNoarkObject documentDescriptionHateoas,
            JsonGenerator jgen
    ) throws IOException {
        DocumentDescription documentDescription =
                (DocumentDescription) noarkEntity;
        jgen.writeStartObject();

        // handle DocumentDescription properties
        printSystemIdEntity(jgen, documentDescription);
        printNullableMetadata(jgen,
             DOCUMENT_DESCRIPTION_DOCUMENT_TYPE,
             documentDescription.getDocumentType());
        printNullableMetadata(jgen,
             DOCUMENT_DESCRIPTION_STATUS,
             documentDescription.getDocumentStatus());
        printTitleAndDescription(jgen, documentDescription);
        printNullable(jgen, DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER,
                      documentDescription.getDocumentNumber());
        printNullableDateTime(jgen, DOCUMENT_DESCRIPTION_ASSOCIATED_DATE,
                              documentDescription.getAssociationDate());
        // TODO why test on date when printing the 'by' value?
        if (documentDescription.getAssociationDate() != null) {
            jgen.writeStringField(DOCUMENT_DESCRIPTION_ASSOCIATED_BY,
                    documentDescription.getAssociatedBy());
        }
        printNullableMetadata(jgen,
             DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS,
             documentDescription.getAssociatedWithRecordAs());
        printCreateEntity(jgen, documentDescription);
        printModifiedEntity(jgen, documentDescription);
        printDisposal(jgen, documentDescription);
        printDisposalUndertaken(jgen, documentDescription);
        printDeletion(jgen, documentDescription);
        printScreening(jgen, documentDescription);
        printClassified(jgen, documentDescription);
        printNullable(jgen, STORAGE_LOCATION,
                      documentDescription.getStorageLocation());
        printNullable(jgen, DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE,
                      documentDescription.getExternalReference());
        printElectronicSignature(jgen, documentDescription);
        printHateoasLinks(jgen,
                documentDescriptionHateoas.getLinks(documentDescription));
        jgen.writeEndObject();
    }
}
