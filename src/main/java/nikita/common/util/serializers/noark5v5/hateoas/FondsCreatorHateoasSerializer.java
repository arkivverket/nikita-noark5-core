package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.CommonUtils;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.printFondsCreator;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;

/**
 * Serialise an outgoing FondsCreator object as JSON.
 */
public class FondsCreatorHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject fondsCreatorHateoas, JsonGenerator jgen)
            throws IOException {

        FondsCreator fondsCreator = (FondsCreator) noarkSystemIdEntity;
        jgen.writeStartObject();
        printFondsCreator(jgen, fondsCreator);
        printHateoasLinks(jgen, fondsCreatorHateoas.getLinks(fondsCreator));
        jgen.writeEndObject();
    }
}
