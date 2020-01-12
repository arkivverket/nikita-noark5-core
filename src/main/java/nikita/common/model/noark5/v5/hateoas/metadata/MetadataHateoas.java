package nikita.common.model.noark5.v5.hateoas.metadata;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.metadata.MetadataHateoasSerializer;

import java.util.List;

/**
 * Created by tsodring on 4/3/17.
 */
@JsonSerialize(using = MetadataHateoasSerializer.class)
public class MetadataHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public MetadataHateoas(IMetadataEntity entity) {
        super(entity);
    }

    public MetadataHateoas(List<IMetadataEntity> entityList, String entityType) {
        super((List<INoarkEntity>) (List) entityList, entityType);
    }
}

