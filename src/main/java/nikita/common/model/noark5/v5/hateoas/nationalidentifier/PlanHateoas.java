package nikita.common.model.noark5.v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.DNumberSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.D_NUMBER;

@JsonSerialize(using = DNumberSerializer.class)
public class PlanHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PlanHateoas(INikitaEntity entity) {
        super(entity);
    }

    public PlanHateoas(List<INikitaEntity> entityList) {
        super(entityList, D_NUMBER);
    }
}
