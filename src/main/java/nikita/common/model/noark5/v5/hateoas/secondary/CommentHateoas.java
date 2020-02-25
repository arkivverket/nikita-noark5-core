package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.CommentHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.COMMENT;

@JsonSerialize(using = CommentHateoasSerializer.class)
public class CommentHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public CommentHateoas() {
    }

    public CommentHateoas(INoarkEntity entity) {
        super(entity);
    }

    public CommentHateoas(List<INoarkEntity> entityList) {
        super(entityList, COMMENT);
    }
}
