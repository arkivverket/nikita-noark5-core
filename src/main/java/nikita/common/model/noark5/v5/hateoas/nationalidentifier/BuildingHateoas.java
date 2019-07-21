package nikita.common.model.noark5.v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.UnitSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.BUILDING;

@JsonSerialize(using = UnitSerializer.class)
public class BuildingHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public BuildingHateoas(INikitaEntity entity) {
        super(entity);
    }

    public BuildingHateoas(List<INikitaEntity> entityList) {
        super(entityList, BUILDING);
    }
}
