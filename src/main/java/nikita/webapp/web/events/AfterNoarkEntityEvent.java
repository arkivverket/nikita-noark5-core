package nikita.webapp.web.events;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotNull;

/**
 * Base class that can be used to identify various CRUD events that can occur
 * in nikita.
 */
public class AfterNoarkEntityEvent
        extends ApplicationEvent {

    ISystemId entity;

    public AfterNoarkEntityEvent(Object source,
                                 @NotNull ISystemId entity) {
        super(source);
        this.entity = entity;
    }

    public String toString() {
        return entity.toString();
    }

    public ISystemId getEntity() {
        return entity;
    }
}
