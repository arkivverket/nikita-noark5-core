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

import static nikita.common.util.CommonUtils.Hateoas.Serialize.printCaseFileEntity;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;

/**
 * Serialise the fields required for an outgoing expand to CaseFile object as
 * JSON.
 */

public class CaseFileExpansionHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkEntity,
                                     HateoasNoarkObject caseFileHateoas,
                                     JsonGenerator jgen) throws IOException {

        CaseFile caseFile = (CaseFile) noarkEntity;
        jgen.writeStartObject();
        printCaseFileEntity(jgen, caseFile);
        printHateoasLinks(jgen, caseFileHateoas.getLinks(caseFile));
        jgen.writeEndObject();
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        throw new UnsupportedOperationException();
    }
}
