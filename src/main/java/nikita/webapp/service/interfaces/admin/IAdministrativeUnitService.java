package nikita.webapp.service.interfaces.admin;


import nikita.common.model.noark5.v4.admin.AdministrativeUnit;
import nikita.common.model.noark5.v4.admin.User;

import java.util.List;

public interface IAdministrativeUnitService {

    AdministrativeUnit update(String systemId, Long version,
                              AdministrativeUnit incomingAdministrativeUnit);

    AdministrativeUnit createNewAdministrativeUnitByUser(
            AdministrativeUnit entity, User user);

    AdministrativeUnit createNewAdministrativeUnitBySystem(
            AdministrativeUnit entity);

    AdministrativeUnit findBySystemId(String administrativeUnitSystemId);

    List<AdministrativeUnit> findAll(String outgoingAddress);
}
