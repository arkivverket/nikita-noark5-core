package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.ClassificationSystemHateoasSerializer;

import java.util.List;

@JsonSerialize(using = ClassificationSystemHateoasSerializer.class)
public class ClassificationSystemHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public ClassificationSystemHateoas(INoarkEntity entity) {
        super(entity);
    }

    public ClassificationSystemHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.CLASSIFICATION_SYSTEM);
    }
}
