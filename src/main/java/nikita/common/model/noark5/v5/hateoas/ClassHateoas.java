package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.ClassHateoasSerializer;

import java.util.List;

@JsonSerialize(using = ClassHateoasSerializer.class)
public class ClassHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public ClassHateoas(INoarkEntity entity) {
        super(entity);
    }

    public ClassHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.CLASS);
    }
}
