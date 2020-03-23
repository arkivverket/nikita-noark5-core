package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.ChangeLogHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.CHANGE_LOG;

@JsonSerialize(using = ChangeLogHateoasSerializer.class)
public class ChangeLogHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public ChangeLogHateoas() {
    }

    public ChangeLogHateoas(INoarkEntity entity) {
        super(entity);
    }

    public ChangeLogHateoas(List<INoarkEntity> entityList) {
        super(entityList, CHANGE_LOG);
    }
}
