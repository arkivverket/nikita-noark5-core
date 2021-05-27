package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.EventLog;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.EventLogHateoas;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IEventLogService {

    EventLogHateoas generateDefaultEventLog(@NotNull final SystemIdEntity entity);

    EventLogHateoas createNewEventLog(@NotNull final EventLog eventLog,
                                      @NotNull final SystemIdEntity entity);

    EventLogHateoas findEventLogByOwner();

    EventLogHateoas findSingleEventLog(@NotNull final UUID systemId);

    EventLogHateoas handleUpdate(@NotNull final UUID systemId,
                                 @NotNull final Long version,
                                 @NotNull final EventLog incomingEventLog);

    void deleteEntity(@NotNull final UUID systemId);
}
