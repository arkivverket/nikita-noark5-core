package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
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
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject documentObjectHateoas,
                                     JsonGenerator jgen) throws IOException {

        DocumentObject documentObject = (DocumentObject) noarkSystemIdEntity;

        jgen.writeStartObject();

        // handle DocumentObject properties
        printSystemIdEntity(jgen, documentObject);

        printNullable(jgen, DOCUMENT_OBJECT_VERSION_NUMBER,
                      documentObject.getVersionNumber());
        printNullableMetadata(jgen, DOCUMENT_OBJECT_VARIANT_FORMAT,
                              documentObject.getVariantFormat());
        printNullableMetadata(jgen, DOCUMENT_OBJECT_FORMAT,
                              documentObject.getFormat());
        printNullable(jgen, DOCUMENT_OBJECT_FORMAT_DETAILS,
                      documentObject.getFormatDetails());
        printNullable(jgen, DOCUMENT_OBJECT_CHECKSUM,
                      documentObject.getChecksum());
        printNullable(jgen, DOCUMENT_OBJECT_CHECKSUM_ALGORITHM,
                      documentObject.getChecksumAlgorithm());
        printNullable(jgen, DOCUMENT_OBJECT_FILE_SIZE,
                      documentObject.getFileSize());
        printNullable(jgen, DOCUMENT_OBJECT_FILE_NAME,
                      documentObject.getOriginalFilename());
        printNullable(jgen, DOCUMENT_OBJECT_MIME_TYPE,
                      documentObject.getMimeType());
        printCreateEntity(jgen, documentObject);
        printModifiedEntity(jgen, documentObject);
        printElectronicSignature(jgen, documentObject);
        printHateoasLinks(jgen, documentObjectHateoas.getLinks(documentObject));
        jgen.writeEndObject();
    }
}
