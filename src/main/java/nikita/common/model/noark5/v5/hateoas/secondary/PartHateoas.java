package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.PartHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.PART;

@JsonSerialize(using = PartHateoasSerializer.class)
public class PartHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PartHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PartHateoas(NikitaPage page) {
        super(page, PART);
    }
}
