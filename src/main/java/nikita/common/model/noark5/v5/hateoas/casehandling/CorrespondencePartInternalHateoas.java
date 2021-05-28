package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.CorrespondencePartInternalHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_INTERNAL;

@JsonSerialize(using = CorrespondencePartInternalHateoasSerializer.class)
public class CorrespondencePartInternalHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public CorrespondencePartInternalHateoas(INoarkEntity entity) {
        super(entity);
    }

    public CorrespondencePartInternalHateoas(NikitaPage page) {
        super(page, CORRESPONDENCE_PART_INTERNAL);
    }
}
