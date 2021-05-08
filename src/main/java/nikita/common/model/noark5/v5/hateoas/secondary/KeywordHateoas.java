package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.KeywordHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.KEYWORD;

@JsonSerialize(using = KeywordHateoasSerializer.class)
public class KeywordHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public KeywordHateoas() {
    }

    public KeywordHateoas(INoarkEntity entity) {
        super(entity);
    }

    public KeywordHateoas(List<INoarkEntity> entityList) {
        super(entityList, KEYWORD);
    }
}
