package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.CrossReferenceHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.CROSS_REFERENCE;

@JsonSerialize(using = CrossReferenceHateoasSerializer.class)
public class CrossReferenceHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public CrossReferenceHateoas() {
    }

    public CrossReferenceHateoas(INoarkEntity entity) {
        super(entity);
    }

    public CrossReferenceHateoas(List<INoarkEntity> entityList) {
        super(entityList, CROSS_REFERENCE);
    }
}
