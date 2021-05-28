package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.secondary.Precedence;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IPrecedenceService {

    PrecedenceHateoas updatePrecedenceBySystemId(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final Precedence incomingPrecedence);

    PrecedenceHateoas createNewPrecedence(
            @NotNull final Precedence entity);

    void deletePrecedenceBySystemId(@NotNull final UUID systemId);

    PrecedenceHateoas findAll();

    PrecedenceHateoas findBySystemId(
            @NotNull final UUID systemId);

    PrecedenceHateoas generateDefaultPrecedence();

    boolean deletePrecedenceIfNotEmpty(@NotNull final Precedence precedence);
}
