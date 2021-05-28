package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.CorrespondencePartUnitHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_UNIT;

@JsonSerialize(using = CorrespondencePartUnitHateoasSerializer.class)
public class CorrespondencePartUnitHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public CorrespondencePartUnitHateoas(INoarkEntity entity) {
        super(entity);
    }

    public CorrespondencePartUnitHateoas(NikitaPage page) {
        super(page, CORRESPONDENCE_PART_UNIT);
    }
}
