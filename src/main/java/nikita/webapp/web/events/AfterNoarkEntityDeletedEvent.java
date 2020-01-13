package nikita.webapp.web.events;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

import javax.validation.constraints.NotNull;

/**
 * Base class that can be used to identify DELETE events that can occur in
 * nikita.
 */
public class AfterNoarkEntityDeletedEvent
        extends AfterNoarkEntityEvent {

    public AfterNoarkEntityDeletedEvent(Object source,
                                        @NotNull ISystemId entity) {
        super(source, entity);
    }
}
