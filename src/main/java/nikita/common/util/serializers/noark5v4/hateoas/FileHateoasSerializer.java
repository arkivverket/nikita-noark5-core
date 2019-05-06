package nikita.common.util.serializers.noark5v4.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v4.File;
import nikita.common.model.noark5.v4.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v4.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing File object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the
 * output. We need to be able to especially control the HATEOAS links and the
 * actual format of the HATEOAS links might change over time with the standard.
 * This allows us to be able to easily adapt to any changes.
 * <p>
 * Note. Only values that are part of the standard are included in the JSON.
 * properties like 'id' are not exported.
 */
public class FileHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INikitaEntity noarkSystemIdEntity,
                                     HateoasNoarkObject fileHateoas,
                                     JsonGenerator jgen) throws IOException {

        File file = (File) noarkSystemIdEntity;

        jgen.writeStartObject();
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

        printDocumentMedium(jgen, file);
        printKeyword(jgen, file);
        printCreateEntity(jgen, file);
        printFinaliseEntity(jgen, file);
        if (file.getReferenceSeries() != null &&
                file.getReferenceSeries().getSystemId() != null) {
            jgen.writeStringField(REFERENCE_SERIES,
                    file.getReferenceSeries().getSystemId());
        }
        //TODO: Add missing printCrossReference(jgen, file
        // .getReferenceCrossReference());
        printComment(jgen, file);
        printDisposal(jgen, file);
        printScreening(jgen, file);
        printClassified(jgen, file);
        printHateoasLinks(jgen, fileHateoas.getLinks(file));
        jgen.writeEndObject();
    }
}
