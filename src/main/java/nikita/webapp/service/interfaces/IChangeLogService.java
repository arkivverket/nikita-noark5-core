package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.hateoas.ChangeLogHateoas;
import nikita.common.model.noark5.v5.ChangeLog;

import javax.validation.constraints.NotNull;

public interface IChangeLogService {

    ChangeLogHateoas generateDefaultChangeLog();

    ChangeLogHateoas createNewChangeLog(ChangeLog changeLog);

    ChangeLogHateoas findChangeLogByOwner();

    ChangeLogHateoas findSingleChangeLog(String changeLogSystemId);

    ChangeLogHateoas handleUpdate(@NotNull String systemId,
                                  @NotNull Long version,
                                  @NotNull ChangeLog incomingChangeLog);

    void deleteEntity(@NotNull String systemId);
}
