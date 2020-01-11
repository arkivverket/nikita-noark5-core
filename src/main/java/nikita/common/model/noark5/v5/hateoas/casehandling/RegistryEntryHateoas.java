package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.RegistryEntryHateoasSerializer;

import java.util.List;

/**
 * Created by tsodring on 11/1/17.
 * <p>
 * Using composition rather than inheritance. Although this class is really only a placeholder for the RegistryEntry object
 * along with the hateoas links. It's not intended that you will manipulate the RegistryEntry object from here.
 */
@JsonSerialize(using = RegistryEntryHateoasSerializer.class)
public class RegistryEntryHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public RegistryEntryHateoas(INoarkEntity entity) {
        super(entity);
    }

    public RegistryEntryHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.REGISTRY_ENTRY);
    }

}
