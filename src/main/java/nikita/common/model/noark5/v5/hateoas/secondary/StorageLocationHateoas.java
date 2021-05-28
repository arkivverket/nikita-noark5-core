package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.StorageLocationHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.STORAGE_LOCATION;

@JsonSerialize(using = StorageLocationHateoasSerializer.class)
public class StorageLocationHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public StorageLocationHateoas() {
    }

    public StorageLocationHateoas(INoarkEntity entity) {
        super(entity);
    }

    public StorageLocationHateoas(NikitaPage page) {
        super(page, STORAGE_LOCATION);
    }
}
