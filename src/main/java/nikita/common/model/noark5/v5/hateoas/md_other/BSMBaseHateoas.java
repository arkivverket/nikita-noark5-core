package nikita.common.model.noark5.v5.hateoas.md_other;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.metadata.BSMBaseHateoasSerializer;

import java.util.List;

@JsonSerialize(using = BSMBaseHateoasSerializer.class)
public class BSMBaseHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public BSMBaseHateoas(INoarkEntity entity) {
        super(entity);
    }

    public BSMBaseHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.FILE);
    }
}
