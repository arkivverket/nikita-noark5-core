package nikita.common.model.noark5.v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.PositionSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.POSITION;

@JsonSerialize(using = PositionSerializer.class)
public class PositionHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PositionHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PositionHateoas(List<INoarkEntity> entityList) {
        super(entityList, POSITION);
    }
}
