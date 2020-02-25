package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.ConversionHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.CONVERSION;

@JsonSerialize(using = ConversionHateoasSerializer.class)
public class ConversionHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public ConversionHateoas() {
    }

    public ConversionHateoas(INoarkEntity entity) {
        super(entity);
    }

    public ConversionHateoas(List<INoarkEntity> entityList) {
        super(entityList, CONVERSION);
    }
}
