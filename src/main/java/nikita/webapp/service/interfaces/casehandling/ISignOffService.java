package nikita.webapp.service.interfaces.casehandling;

import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.secondary.SignOff;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface ISignOffService {

    SignOffHateoas save(@NotNull final SignOff signOff);

    // All find methods
    SignOffHateoas findBySystemId(@NotNull final UUID systemId);

    SignOffHateoas updateSignOff(
            @NotNull final UUID systemId,
            @NotNull final SignOff signOff);

    void deleteSignOff(@NotNull final UUID systemId);

    SignOff findSignOffBySystemId(@NotNull final UUID systemId);

    SignOffHateoas generateDefaultSignOff(@NotNull final UUID systemId);

    SignOffHateoas packAsHateoas(@NotNull final SignOff signOff);
}
