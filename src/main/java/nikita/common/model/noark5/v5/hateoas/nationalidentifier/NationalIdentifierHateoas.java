package nikita.common.model.noark5.v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.NationalIdentifierSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.NATIONAL_IDENTIFIER;

@JsonSerialize(using = NationalIdentifierSerializer.class)
public class NationalIdentifierHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public NationalIdentifierHateoas(INoarkEntity entity) {
        super(entity);
    }

    public NationalIdentifierHateoas(List<INoarkEntity> entityList) {
        super(entityList, NATIONAL_IDENTIFIER);
    }
}
