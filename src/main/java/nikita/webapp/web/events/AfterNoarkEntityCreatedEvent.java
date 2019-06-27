package nikita.webapp.web.events;

import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

import javax.validation.constraints.NotNull;

/**
 * Base class that can be used to identify CREATE events that can occur in
 * nikita.
 */
public class AfterNoarkEntityCreatedEvent
        extends AfterNoarkEntityEvent {

    public AfterNoarkEntityCreatedEvent(Object source,
                                        @NotNull INikitaEntity entity) {
        super(source, entity);
    }
}
