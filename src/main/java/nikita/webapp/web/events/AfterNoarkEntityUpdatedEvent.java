package nikita.webapp.web.events;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

import javax.validation.constraints.NotNull;

/**
 * Base class that can be used to identify UPDATE events that can occur in
 * nikita.
 */
public class AfterNoarkEntityUpdatedEvent
        extends AfterNoarkEntityEvent {

    public AfterNoarkEntityUpdatedEvent(Object source,
                                        @NotNull ISystemId entity) {
        super(source, entity);
    }
}
