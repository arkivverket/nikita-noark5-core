package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.CorrespondencePartUnitHateoasSerializer;

import java.util.List;

/**
 * Calls super to handle the links etc., provides a way to automatically deserialiase a CorrespondencePartPersonHateoas
 * object
 */
@JsonSerialize(using = CorrespondencePartUnitHateoasSerializer.class)
public class CorrespondencePartUnitHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public CorrespondencePartUnitHateoas(INoarkEntity entity) {
        super(entity);
    }

    public CorrespondencePartUnitHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.CORRESPONDENCE_PART_UNIT);
    }
}
