package nikita.model.noark5.v4.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.model.noark5.v4.hateoas.HateoasNoarkObject;
import nikita.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.model.noark5.v4.interfaces.entities.INoarkSystemIdEntity;
import nikita.util.serializers.noark5v4.hateoas.FondsHateoasSerializer;

import java.util.List;

import static nikita.config.N5ResourceMappings.CORRESPONDENCE_PART;

/**
 * Calls super to handle the links etc., provides a way to automatically deserialiase a CorrespondencePartHateoas object
 */
@JsonSerialize(using = FondsHateoasSerializer.class)
public class CorrespondencePartHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public CorrespondencePartHateoas(INoarkSystemIdEntity entity) {
        super(entity);
    }

    public CorrespondencePartHateoas(List<INoarkSystemIdEntity> entityList) {
        super(entityList, CORRESPONDENCE_PART);
    }
}
