package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.KeywordTemplateHateoasSerializer;

@JsonSerialize(using = KeywordTemplateHateoasSerializer.class)
public class KeywordTemplateHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public KeywordTemplateHateoas() {
    }

    public KeywordTemplateHateoas(INoarkEntity entity) {
        super(entity);
    }
}
