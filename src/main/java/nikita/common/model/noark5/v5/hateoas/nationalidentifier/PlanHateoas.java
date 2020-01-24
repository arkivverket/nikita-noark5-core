package nikita.common.model.noark5.v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.PlanSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.PLAN;

@JsonSerialize(using = PlanSerializer.class)
public class PlanHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PlanHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PlanHateoas(List<INoarkEntity> entityList) {
        super(entityList, PLAN);
    }
}
