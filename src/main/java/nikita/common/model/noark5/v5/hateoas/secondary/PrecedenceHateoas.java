package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.PrecedenceHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.PRECEDENCE;

@JsonSerialize(using = PrecedenceHateoasSerializer.class)
public class PrecedenceHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public PrecedenceHateoas(INoarkEntity entity) {
        super(entity);
    }

    public PrecedenceHateoas(NikitaPage page) {
        super(page, PRECEDENCE);
    }
}
