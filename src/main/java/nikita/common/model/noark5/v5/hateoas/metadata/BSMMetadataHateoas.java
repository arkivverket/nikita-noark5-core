package nikita.common.model.noark5.v5.hateoas.metadata;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.util.serializers.noark5v5.hateoas.metadata.BSMMetadataHateoasSerializer;

@JsonSerialize(using = BSMMetadataHateoasSerializer.class)
public class BSMMetadataHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public BSMMetadataHateoas(BSMMetadata entity) {
        super(entity);
    }

    public BSMMetadataHateoas(NikitaPage page, String entityType) {
        super(page, entityType);
    }
}

