package nikita.common.repository.n5v4.admin;

import nikita.common.model.noark5.v4.admin.AdministrativeUnit;
import nikita.common.model.noark5.v4.admin.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IAdministrativeUnitRepository
        extends PagingAndSortingRepository<AdministrativeUnit, Long> {


    // -- All SAVE operations
    @Override
    AdministrativeUnit save(AdministrativeUnit administrativeUnit);

    // -- All READ operations
    @Override
    List<AdministrativeUnit> findAll();

    // id
    Optional<AdministrativeUnit> findById(Long id);

    // systemId
    AdministrativeUnit findBySystemId(String systemId);

    // administrativeUnitName
    AdministrativeUnit findByAdministrativeUnitName(
            String administrativeUnitName);

    // shortName
    AdministrativeUnit findByShortName(String shortName);

    Optional<AdministrativeUnit> findByUsersInAndDefaultAdministrativeUnit(
            Set<User> user, Boolean defaultAdministrativeUnit);

    int deleteByOwnedBy(String ownedBy);
}
