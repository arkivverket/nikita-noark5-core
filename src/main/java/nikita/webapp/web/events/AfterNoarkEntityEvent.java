package nikita.webapp.web.events;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotNull;

/**
 * Base class that can be used to identify various CRUD events that can occur
 * in nikita.
 */
public class AfterNoarkEntityEvent
        extends ApplicationEvent {

    INoarkEntity entity;

    public AfterNoarkEntityEvent(Object source,
                                 @NotNull INoarkEntity entity) {
        super(source);
        this.entity = entity;
    }

    public String toString() {
        return entity.toString();
    }

    public INoarkEntity getEntity() {
        return entity;
    }
}
