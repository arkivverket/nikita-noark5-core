package nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

public class PositionSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject positionHateoas,
            JsonGenerator jgen) throws IOException {

        Position position = (Position) noarkSystemIdEntity;
        jgen.writeStartObject();
        printSystemIdEntity(jgen, position);
        printNullableMetadata(jgen, COORDINATE_SYSTEM,
                              position.getCoordinateSystem());
        printNullable(jgen, X, position.getX());
        printNullable(jgen, Y, position.getY());
        printNullable(jgen, Z, position.getZ());
        printHateoasLinks(jgen, positionHateoas.getLinks(position));
        jgen.writeEndObject();
    }
}
