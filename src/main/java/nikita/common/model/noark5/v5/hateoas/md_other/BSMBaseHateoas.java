package nikita.common.model.noark5.v5.hateoas.md_other;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.metadata.BSMBaseHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.BSM_DEF;

@JsonSerialize(using = BSMBaseHateoasSerializer.class)
public class BSMBaseHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public BSMBaseHateoas(INoarkEntity entity) {
        super(entity);
    }

    public BSMBaseHateoas(NikitaPage page) {
        super(page, BSM_DEF);
    }
}
