package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.PartUnitHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.PART_UNIT;

@JsonSerialize(using = PartUnitHateoasSerializer.class)
public class PartUnitHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PartUnitHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PartUnitHateoas(NikitaPage page) {
        super(page, PART_UNIT);
    }
}
