package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.EventLog;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.EventLogHateoas;

import javax.validation.constraints.NotNull;

public interface IEventLogService {

    EventLogHateoas generateDefaultEventLog(SystemIdEntity entity);

    EventLogHateoas createNewEventLog(EventLog eventLog, SystemIdEntity entity);

    EventLogHateoas findEventLogByOwner();

    EventLogHateoas findSingleEventLog(String eventLogSystemId);

    EventLogHateoas handleUpdate(@NotNull String systemId,
                                 @NotNull Long version,
                                 @NotNull EventLog incomingEventLog);

    void deleteEntity(@NotNull String systemId);
}
