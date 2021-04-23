package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.CaseFileExpansionHateoasSerializer;

@JsonSerialize(using = CaseFileExpansionHateoasSerializer.class)
public class CaseFileExpansionHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {
    public CaseFileExpansionHateoas(INoarkEntity entity) {
        super(entity);
    }
}
