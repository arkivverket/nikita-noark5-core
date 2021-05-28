package nikita.common.model.noark5.v5.hateoas.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.admin.AdministrativeUnitHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.RIGHT;


@JsonSerialize(using = AdministrativeUnitHateoasSerializer.class)
public class RightsHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public RightsHateoas(INoarkEntity entity) {
        super(entity);
    }

    public RightsHateoas(NikitaPage page) {
        super(page, RIGHT);
    }
}
