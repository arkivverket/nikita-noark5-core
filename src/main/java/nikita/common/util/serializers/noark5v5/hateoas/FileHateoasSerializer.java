package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

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

        printFileEntity(jgen, file);
        if (file instanceof CaseFile) {
            printCaseFileEntity(jgen, (CaseFile) file);
        }
        printDocumentMedium(jgen, file);
        printKeyword(jgen, file);
        printCreateEntity(jgen, file);
        printFinaliseEntity(jgen, file);
        printCrossReferences(jgen, file);
        printComment(jgen, file);
        printDisposal(jgen, file);
        printScreening(jgen, file);
        printClassified(jgen, file);
        if (file instanceof CaseFile) {

        }
        printHateoasLinks(jgen, fileHateoas.getLinks(file));
        jgen.writeEndObject();
    }
}
