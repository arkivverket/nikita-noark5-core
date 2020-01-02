package nikita.webapp.service.interfaces.admin;


import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAdministrativeUnitService {

    AdministrativeUnit update(String systemId, Long version,
                              AdministrativeUnit incomingAdministrativeUnit);

    AdministrativeUnit createNewAdministrativeUnitByUser(
            AdministrativeUnit entity, User user);

    AdministrativeUnit createNewAdministrativeUnitBySystem(
            AdministrativeUnit entity);

    AdministrativeUnit createNewAdministrativeUnitBySystemNoDuplicate(
            AdministrativeUnit entity);

    AdministrativeUnit findBySystemId(UUID administrativeUnitSystemId);

    List<AdministrativeUnit> findAll();

    Optional<AdministrativeUnit> findFirst();

    AdministrativeUnit getAdministrativeUnitOrThrow(User user);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
