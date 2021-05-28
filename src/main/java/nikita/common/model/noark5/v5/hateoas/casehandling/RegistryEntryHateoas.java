package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.RegistryEntryHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.REGISTRY_ENTRY;


@JsonSerialize(using = RegistryEntryHateoasSerializer.class)
public class RegistryEntryHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public RegistryEntryHateoas(INoarkEntity entity) {
        super(entity);
    }

    public RegistryEntryHateoas(NikitaPage page) {
        super(page, REGISTRY_ENTRY);
    }

}
