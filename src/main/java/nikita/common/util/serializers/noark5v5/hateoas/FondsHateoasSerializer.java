package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.CommonUtils;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.FONDS_STATUS;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing Fonds object as JSON.
 */
public class FondsHateoasSerializer 
        extends HateoasSerializer 
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity, HateoasNoarkObject fondsHateoas
            , JsonGenerator jgen) throws IOException {
        Fonds fonds = (Fonds) noarkSystemIdEntity;

        jgen.writeStartObject();
        printSystemIdEntity(jgen, fonds);
        printTitleAndDescription(jgen, fonds);
        printNullableMetadata(jgen, FONDS_STATUS,
                              fonds.getFondsStatus());
        printDocumentMedium(jgen, fonds);
        printStorageLocation(jgen, fonds);
        printFinaliseEntity(jgen, fonds);
        printCreateEntity(jgen, fonds);
        printModifiedEntity(jgen, fonds);
        printHateoasLinks(jgen, fondsHateoas.getLinks(fonds));
        jgen.writeEndObject();
    }
}
