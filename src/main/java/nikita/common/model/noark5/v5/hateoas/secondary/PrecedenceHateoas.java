package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.PrecedenceHateoasSerializer;

import java.util.List;

/**
 * Calls super to handle the links etc., provides a way to automatically deserialiase a PrecedenceHateoas object
 */
@JsonSerialize(using = PrecedenceHateoasSerializer.class)
public class PrecedenceHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public PrecedenceHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PrecedenceHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.PRECEDENCE);
    }
}
