package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.IEventLogEntity;
import nikita.common.model.noark5.v5.ChangeLog;
import nikita.common.model.noark5.v5.EventLog;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.IEventLogHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.EVENT_LOG;
import static nikita.common.config.N5ResourceMappings.EVENT_TYPE;

/*
 * Used to add EventLogHateoas links with EventLog specific information
 */
@Component("eventLogHateoasHandler")
public class EventLogHateoasHandler
        extends ChangeLogHateoasHandler
        implements IEventLogHateoasHandler {

    public EventLogHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfHref = getOutgoingAddress() +
            HREF_BASE_LOGGING + SLASH + EVENT_LOG + SLASH + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfHref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfHref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        EventLog eventLog = (EventLog) entity;
        addReferenceArchiveUnitLink((ChangeLog)eventLog, hateoasNoarkObject);
        addEventType(eventLog, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate
        (ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        EventLog eventLog = (EventLog) entity;
        addEventType(eventLog, hateoasNoarkObject);
    }

    public void addEventType
        (EventLog eventLog, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(eventLog, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + EVENT_TYPE,
                REL_METADATA_EVENT_TYPE, true));
    }
}
