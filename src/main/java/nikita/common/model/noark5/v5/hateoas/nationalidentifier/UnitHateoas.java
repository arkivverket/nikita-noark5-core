package nikita.common.model.noark5.v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.UnitSerializer;

import java.util.List;

import static nikita.common.config.Constants.UNIT;

@JsonSerialize(using = UnitSerializer.class)
public class UnitHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public UnitHateoas(INikitaEntity entity) {
        super(entity);
    }

    public UnitHateoas(List<INikitaEntity> entityList) {
        super(entityList, UNIT);
    }
}
