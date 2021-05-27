package nikita.common.repository.n5v5.admin;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IAdministrativeUnitRepository
        extends PagingAndSortingRepository<AdministrativeUnit, UUID> {

    AdministrativeUnit findFirstByOrderByCreatedDateAsc();

    AdministrativeUnit findBySystemId(UUID systemId);

    List<AdministrativeUnit> findByAdministrativeUnitName(
            String administrativeUnitName);

    AdministrativeUnit findByUsersInAndDefaultAdministrativeUnit(
            Set<User> user, Boolean defaultAdministrativeUnit);

    int deleteByOwnedBy(String ownedBy);
}
