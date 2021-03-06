package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.ScreeningMetadataHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.SCREENING_METADATA;

@JsonSerialize(using = ScreeningMetadataHateoasSerializer.class)
public class ScreeningMetadataHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public ScreeningMetadataHateoas(INoarkEntity entity) {
        super(entity);
    }

    public ScreeningMetadataHateoas(NikitaPage page) {
        super(page, SCREENING_METADATA);
    }
}
