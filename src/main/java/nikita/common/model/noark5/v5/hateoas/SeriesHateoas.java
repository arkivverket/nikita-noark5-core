package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.SeriesHateoasSerializer;

import java.util.List;

@JsonSerialize(using = SeriesHateoasSerializer.class)
public class SeriesHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public SeriesHateoas(INoarkEntity entity) {
        super(entity);
    }

    public SeriesHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.SERIES);
    }
}
