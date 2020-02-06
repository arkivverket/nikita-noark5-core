package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.PartUnitHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.PART_UNIT;

/**
 * Calls super to handle the links etc., provides a way to automatically
 * deserialiase a PartUnitHateoas object
 */
@JsonSerialize(using = PartUnitHateoasSerializer.class)
public class PartUnitHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PartUnitHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PartUnitHateoas(List<INoarkEntity> entityList) {
        super(entityList, PART_UNIT);
    }
}
