package nikita.common.model.noark5.v5.hateoas.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.admin.AdministrativeUnitHateoasSerializer;

import java.util.List;

/**
 * Calls super to handle the links etc., provides a way to automatically deserialiase a AdministrativeUnitHateoas object
 */
@JsonSerialize(using = AdministrativeUnitHateoasSerializer.class)
public class AdministrativeUnitHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public AdministrativeUnitHateoas(INoarkEntity entity) {
        super(entity);
    }

    public AdministrativeUnitHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.ADMINISTRATIVE_UNIT);
    }
}
