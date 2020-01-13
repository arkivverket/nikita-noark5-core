package nikita.common.model.noark5.v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.SocialSecurityNumberSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.SOCIAL_SECURITY_NUMBER;

@JsonSerialize(using = SocialSecurityNumberSerializer.class)
public class SocialSecurityNumberHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public SocialSecurityNumberHateoas(INoarkEntity entity) {
        super(entity);
    }

    public SocialSecurityNumberHateoas(List<INoarkEntity> entityList) {
        super(entityList, SOCIAL_SECURITY_NUMBER);
    }
}
