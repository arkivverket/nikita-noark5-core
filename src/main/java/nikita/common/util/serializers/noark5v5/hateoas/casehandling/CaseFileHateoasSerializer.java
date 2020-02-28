package nikita.common.util.serializers.noark5v5.hateoas.casehandling;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing CaseFile object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the
 * output. We need to be able to especially control the HATEOAS links and the
 * actual format of the HATEOAS links might change over time with the standard.
 * This allows us to be able to easily adapt to any later changes.
 * <p>
 * Only Norwegian property names are used on the outgoing JSON property names
 * and are in accordance with the Noark standard.
 * <p>
 * Note. Only values that are part of the standard are included in the JSON.
 * Properties like 'id' or 'deleted' are not exported
 * <p>
 * Note! Some functionality is missing. This will be added later!
 * TODO: You are missing M209 referanseSekundaerKlassifikasjon
 * TODO: Missing FileType!
 */

public class CaseFileHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject caseFileHateoas,
                                     JsonGenerator jgen) throws IOException {

        CaseFile caseFile = (CaseFile) noarkSystemIdEntity;

        jgen.writeStartObject();

        printFileEntity(jgen, caseFile);
        printCaseFileEntity(jgen, caseFile);

        printKeyword(jgen, caseFile);
        printStorageLocation(jgen, caseFile);
        printDocumentMedium(jgen, caseFile);
        printKeyword(jgen, caseFile);
        printFinaliseEntity(jgen, caseFile);
        printCrossReferences(jgen, caseFile);
        printDisposal(jgen, caseFile);
        printScreening(jgen, caseFile);
        printClassified(jgen, caseFile);
        printHateoasLinks(jgen, caseFileHateoas.getLinks(caseFile));
        jgen.writeEndObject();
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        throw new UnsupportedOperationException();
    }
}
