package nikita.common.repository.n5v5.admin;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IAdministrativeUnitRepository
        extends PagingAndSortingRepository<AdministrativeUnit, UUID> {


    // -- All SAVE operations
    @Override
    AdministrativeUnit save(AdministrativeUnit administrativeUnit);

    // -- All READ operations
    @Override
    List<AdministrativeUnit> findAll();

    Optional<AdministrativeUnit> findFirstByOrderByCreatedDateAsc();

    // systemId
    AdministrativeUnit findBySystemId(UUID systemId);

    // administrativeUnitName
    List<AdministrativeUnit> findByAdministrativeUnitName(
            String administrativeUnitName);

    // shortName
    List<AdministrativeUnit> findByShortName(String shortName);

    Optional<AdministrativeUnit> findByUsersInAndDefaultAdministrativeUnit(
            Set<User> user, Boolean defaultAdministrativeUnit);

    int deleteByOwnedBy(String ownedBy);
}
