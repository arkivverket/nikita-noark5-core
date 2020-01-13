package nikita.common.model.noark5.v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.CadastralUnitSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.CADASTRAL_UNIT;

@JsonSerialize(using = CadastralUnitSerializer.class)
public class CadastralUnitHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public CadastralUnitHateoas(INoarkEntity entity) {
        super(entity);
    }

    public CadastralUnitHateoas(List<INoarkEntity> entityList) {
        super(entityList, CADASTRAL_UNIT);
    }
}
