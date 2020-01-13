package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.FondsCreatorHateoasSerializer;

import java.util.List;

@JsonSerialize(using = FondsCreatorHateoasSerializer.class)
public class FondsCreatorHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public FondsCreatorHateoas(INoarkEntity entity) {
        super(entity);
    }

    public FondsCreatorHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.FONDS_CREATOR);
    }
}
