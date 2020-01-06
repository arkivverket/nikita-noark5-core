package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.deserialisers.secondary.AuthorDeserializer;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.AuthorHateoasSerializer;

import java.util.List;

@JsonSerialize(using = AuthorHateoasSerializer.class)
@JsonDeserialize(using = AuthorDeserializer.class)
public class AuthorHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public AuthorHateoas() {
    }

    public AuthorHateoas(INikitaEntity entity) {
        super(entity);
    }

    public AuthorHateoas(List<INikitaEntity> entityList, String entityType) {
        super(entityList, entityType);
    }
}
