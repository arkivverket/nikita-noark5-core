package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing DocumentObject object as JSON.
 */

public class DocumentObjectHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INikitaEntity noarkSystemIdEntity,
                                     HateoasNoarkObject documentObjectHateoas,
                                     JsonGenerator jgen) throws IOException {

        DocumentObject documentObject = (DocumentObject) noarkSystemIdEntity;

        jgen.writeStartObject();

        // handle DocumentObject properties
        printNikitaEntity(jgen, documentObject);

        if (documentObject.getVersionNumber() != null) {
            jgen.writeNumberField(DOCUMENT_OBJECT_VERSION_NUMBER,
                    documentObject.getVersionNumber());
        }
        if (documentObject.getVariantFormatCode() != null) {
            jgen.writeObjectFieldStart(DOCUMENT_OBJECT_VARIANT_FORMAT);
            printCode(jgen,
                    documentObject.getVariantFormatCode(),
                    documentObject.getVariantFormatCodeName());
            jgen.writeEndObject();
        }
	if (documentObject.getFormatCode() != null) {
            jgen.writeObjectFieldStart(DOCUMENT_OBJECT_FORMAT);
            printCode(jgen,
                    documentObject.getFormatCode(),
                    documentObject.getFormatCodeName());
            jgen.writeEndObject();
        }
        if (documentObject.getFormatDetails() != null) {
            jgen.writeStringField(DOCUMENT_OBJECT_FORMAT_DETAILS,
                    documentObject.getFormatDetails());
        }
        if (documentObject.getChecksum() != null) {
            jgen.writeStringField(DOCUMENT_OBJECT_CHECKSUM,
                    documentObject.getChecksum());
        }
        if (documentObject.getChecksumAlgorithm() != null) {
            jgen.writeStringField(DOCUMENT_OBJECT_CHECKSUM_ALGORITHM,
                    documentObject.getChecksumAlgorithm());
        }
        if (documentObject.getFileSize() != null) {
            jgen.writeStringField(DOCUMENT_OBJECT_FILE_SIZE,
                    Long.toString(documentObject.getFileSize()));
        }
        if (documentObject.getOriginalFilename() != null) {
            jgen.writeStringField(DOCUMENT_OBJECT_FILE_NAME,
                    documentObject.getOriginalFilename());
        }
        if (documentObject.getMimeType() != null) {
            jgen.writeStringField(DOCUMENT_OBJECT_MIME_TYPE,
                    documentObject.getMimeType());
        }
        printElectronicSignature(jgen, documentObject);
        printConversion(jgen, documentObject);
        printHateoasLinks(jgen, documentObjectHateoas.getLinks(documentObject));
        jgen.writeEndObject();
    }
}
