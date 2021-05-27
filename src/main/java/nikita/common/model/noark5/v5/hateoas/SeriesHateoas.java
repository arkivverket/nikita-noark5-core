package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.SeriesHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.SERIES;

@JsonSerialize(using = SeriesHateoasSerializer.class)
public class SeriesHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public SeriesHateoas(INoarkEntity entity) {
        super(entity);
    }

    public SeriesHateoas(NikitaPage page) {
        super(page, SERIES);
    }
}
