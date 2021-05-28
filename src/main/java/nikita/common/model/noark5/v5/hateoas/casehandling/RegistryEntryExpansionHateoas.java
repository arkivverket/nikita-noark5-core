package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.RecordNoteExpansionHateoasSerializer;

@JsonSerialize(using = RecordNoteExpansionHateoasSerializer.class)
public class RegistryEntryExpansionHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {
    public RegistryEntryExpansionHateoas(INoarkEntity entity) {
        super(entity);
    }
}
