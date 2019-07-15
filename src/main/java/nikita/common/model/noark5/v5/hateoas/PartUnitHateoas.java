package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v5.hateoas.PartUnitHateoasSerializer;

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

    public PartUnitHateoas(INikitaEntity entity) {
        super(entity);
    }

    public PartUnitHateoas(List<INikitaEntity> entityList) {
        super(entityList, PART_UNIT);
    }
}
