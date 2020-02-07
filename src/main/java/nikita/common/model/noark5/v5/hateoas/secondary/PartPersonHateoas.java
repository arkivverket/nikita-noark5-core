package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.PartPersonHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.PART_PERSON;

/**
 * Calls super to handle the links etc., provides a way to automatically
 * deserialiase a PartPersonHateoas object
 */
@JsonSerialize(using = PartPersonHateoasSerializer.class)
public class PartPersonHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PartPersonHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PartPersonHateoas(List<INoarkEntity> entityList) {
        super(entityList, PART_PERSON);
    }
}
