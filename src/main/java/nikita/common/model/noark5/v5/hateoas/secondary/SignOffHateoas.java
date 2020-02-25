package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.SignOffHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.SIGN_OFF;

@JsonSerialize(using = SignOffHateoasSerializer.class)
public class SignOffHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public SignOffHateoas() {
    }

    public SignOffHateoas(INoarkEntity entity) {
        super(entity);
    }

    public SignOffHateoas(List<INoarkEntity> entityList) {
        super(entityList, SIGN_OFF);
    }
}
