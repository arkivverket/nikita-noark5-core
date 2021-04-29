package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.ScreeningMetadataLocal;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printMetadataEntity;

public class ScreeningMetadataHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkEntity,
                                     HateoasNoarkObject screeningMetadataHateoas,
                                     JsonGenerator jgen)
            throws IOException {

        ScreeningMetadataLocal screeningMetadata =
                (ScreeningMetadataLocal) noarkEntity;

        jgen.writeStartObject();
        printMetadataEntity(jgen, screeningMetadata);
        printHateoasLinks(jgen,
                screeningMetadataHateoas.getLinks(screeningMetadata));
        jgen.writeEndObject();
    }
}
