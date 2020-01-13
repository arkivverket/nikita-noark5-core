package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.deserialisers.secondary.ConversionDeserializer;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.ConversionHateoasSerializer;

import java.util.List;

@JsonSerialize(using = ConversionHateoasSerializer.class)
@JsonDeserialize(using = ConversionDeserializer.class)
public class ConversionHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public ConversionHateoas() {
    }

    public ConversionHateoas(INoarkEntity entity) {
        super(entity);
    }

    public ConversionHateoas(List<INoarkEntity> entityList, String entityType) {
        super(entityList, entityType);
    }
}
