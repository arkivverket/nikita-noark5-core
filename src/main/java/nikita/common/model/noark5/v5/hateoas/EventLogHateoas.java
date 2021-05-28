package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.EventLogHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.EVENT_LOG;

@JsonSerialize(using = EventLogHateoasSerializer.class)
public class EventLogHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public EventLogHateoas(INoarkEntity entity) {
        super(entity);
    }

    public EventLogHateoas(NikitaPage page) {
        super(page, EVENT_LOG);
    }
}
