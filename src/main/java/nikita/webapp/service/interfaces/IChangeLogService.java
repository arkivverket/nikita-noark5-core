package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.ChangeLog;
import nikita.common.model.noark5.v5.hateoas.ChangeLogHateoas;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IChangeLogService {

    ChangeLogHateoas generateDefaultChangeLog();

    ChangeLogHateoas createNewChangeLog(@NotNull final ChangeLog changeLog);

    ChangeLogHateoas findChangeLogByOwner();

    ChangeLogHateoas findSingleChangeLog(@NotNull final UUID systemId);

    ChangeLogHateoas handleUpdate(@NotNull final UUID systemId,
                                  @NotNull final Long version,
                                  @NotNull ChangeLog incomingChangeLog);

    void deleteEntity(@NotNull final UUID systemId);
}
