package nikita.common.model.noark5.v5.hateoas.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.admin.UserHateoasSerializer;

import java.util.List;

/**
 * Calls super to handle the links etc., provides a way to automatically deserialiase a AdministrativeUnitHateoas object
 */
@JsonSerialize(using = UserHateoasSerializer.class)
public class UserHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public UserHateoas(INoarkEntity entity) {
        super(entity);
    }

    public UserHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.USER);
    }
}
