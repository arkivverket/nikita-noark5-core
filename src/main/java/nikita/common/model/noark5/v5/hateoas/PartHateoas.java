package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.PartHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.PART;

/**
 * Calls super to handle the links etc., provides a way to automatically
 * deserialiase a PartPersonHateoas object
 */
@JsonSerialize(using = PartHateoasSerializer.class)
public class PartHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PartHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PartHateoas(List<INoarkEntity> entityList) {
        super(entityList, PART);
    }
}
