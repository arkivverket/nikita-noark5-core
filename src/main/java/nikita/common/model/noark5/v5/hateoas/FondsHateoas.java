package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.FondsHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.FONDS;

@JsonSerialize(using = FondsHateoasSerializer.class)
public class FondsHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public FondsHateoas(INoarkEntity entity) {
        super(entity);
    }

    public FondsHateoas(NikitaPage page) {
        super(page, FONDS);
    }
}
