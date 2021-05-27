package nikita.webapp.service.interfaces.admin;


import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.admin.AdministrativeUnitHateoas;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IAdministrativeUnitService {

    AdministrativeUnitHateoas update(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final AdministrativeUnit incomingAdministrativeUnit);

    AdministrativeUnitHateoas createNewAdministrativeUnitByUser(
            @NotNull final AdministrativeUnit administrativeUnit,
            @NotNull final User user);

    AdministrativeUnitHateoas createNewAdministrativeUnitBySystem(
            @NotNull final AdministrativeUnit administrativeUnit);

    void createNewAdministrativeUnitBySystemNoDuplicate(
            @NotNull final AdministrativeUnit administrativeUnit);

    AdministrativeUnitHateoas findBySystemId(
            @NotNull final UUID administrativeUnitSystemId);

    AdministrativeUnitHateoas findAll();

    AdministrativeUnit findFirst();

    AdministrativeUnit getAdministrativeUnitOrThrow(
            @NotNull final User user);

    // All DELETE operations
    void deleteEntity(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    AdministrativeUnitHateoas generateDefaultAdministrativeUnit();
}
