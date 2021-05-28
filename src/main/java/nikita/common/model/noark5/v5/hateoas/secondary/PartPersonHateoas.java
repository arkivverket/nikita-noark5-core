package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.PartPersonHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.PART_PERSON;

@JsonSerialize(using = PartPersonHateoasSerializer.class)
public class PartPersonHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PartPersonHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PartPersonHateoas(NikitaPage page) {
        super(page, PART_PERSON);
    }
}
