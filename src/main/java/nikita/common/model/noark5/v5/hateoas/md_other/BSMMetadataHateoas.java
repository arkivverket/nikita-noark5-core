package nikita.common.model.noark5.v5.hateoas.md_other;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.metadata.BSMMetadataHateoasSerializer;

import java.util.List;

@JsonSerialize(using = BSMMetadataHateoasSerializer.class)
public class BSMMetadataHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public BSMMetadataHateoas(INoarkEntity entity) {
        super(entity);
    }

    public BSMMetadataHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.FILE);
    }
}
