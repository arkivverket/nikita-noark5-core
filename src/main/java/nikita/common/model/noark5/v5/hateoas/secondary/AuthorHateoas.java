package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.AuthorHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.AUTHOR;

@JsonSerialize(using = AuthorHateoasSerializer.class)
public class AuthorHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public AuthorHateoas(INoarkEntity entity) {
        super(entity);
    }

    public AuthorHateoas(NikitaPage page) {
        super(page, AUTHOR);
    }
}
