package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.EventLogHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.EVENT_LOG;

@JsonSerialize(using = EventLogHateoasSerializer.class)
public class EventLogHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public EventLogHateoas() {
    }

    public EventLogHateoas(INoarkEntity entity) {
        super(entity);
    }

    public EventLogHateoas(List<INoarkEntity> entityList) {
        super(entityList, EVENT_LOG);
    }
}
