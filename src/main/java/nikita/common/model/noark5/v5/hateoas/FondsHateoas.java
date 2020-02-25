package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.FondsHateoasSerializer;

import java.util.List;

/**
 * Calls super to handle the links etc., provides a way to automatically
 * deserialiase a FondsHateoas object
 */
@JsonSerialize(using = FondsHateoasSerializer.class)
public class FondsHateoas extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    // Empty constructor in test scope, should not be used otherwise
    public FondsHateoas() {

    }

    public FondsHateoas(INoarkEntity entity) {
        super(entity);
    }

    public FondsHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.FONDS);
    }
}
