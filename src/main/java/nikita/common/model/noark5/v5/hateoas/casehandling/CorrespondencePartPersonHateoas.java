package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.CorrespondencePartPersonHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_PERSON;

/**
 * Calls super to handle the links etc., provides a way to automatically
 * deserialiase a CorrespondencePartPersonHateoas object
 */
@JsonSerialize(using = CorrespondencePartPersonHateoasSerializer.class)
public class CorrespondencePartPersonHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public CorrespondencePartPersonHateoas(INoarkEntity entity) {
        super(entity);
    }

    public CorrespondencePartPersonHateoas(List<INoarkEntity> entityList) {
        super(entityList, CORRESPONDENCE_PART_PERSON);
    }
}
