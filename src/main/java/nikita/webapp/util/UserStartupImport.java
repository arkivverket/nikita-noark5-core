package nikita.webapp.util;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.Authority;
import nikita.common.model.noark5.v5.admin.AuthorityName;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.repository.nikita.AuthorityRepository;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import nikita.webapp.service.impl.admin.AdministrativeUnitService;
import nikita.webapp.service.impl.admin.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static nikita.common.config.Constants.TEST_ADMINISTRATIVE_UNIT;
import static nikita.common.model.noark5.v5.admin.AuthorityName.*;

/**
 * Create built in users.
 * TODO: Replace with data in JSON files so that it can be handled in the same
 * way metadata is imported
 */
@Component
public class UserStartupImport {

    private final UserService userService;
    private final AuthorityRepository authorityRepository;
    private final AdministrativeUnitService administrativeUnitService;

    private final Logger logger =
            LoggerFactory.getLogger(UserStartupImport.class);

    public UserStartupImport(
            UserService userService,
            AuthorityRepository authorityRepository,
            AdministrativeUnitService administrativeUnitService) {
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.administrativeUnitService = administrativeUnitService;
    }

    @Transactional
    public void addAdminUnit() {
        // Create an administrative unit
        AdministrativeUnit administrativeUnit = new AdministrativeUnit();
        administrativeUnit.setAdministrativeUnitName(TEST_ADMINISTRATIVE_UNIT);
        administrativeUnit.setShortName("test");
        administrativeUnit.setDefaultAdministrativeUnit(true);
        administrativeUnitService.
                createNewAdministrativeUnitBySystemNoDuplicate(
                        administrativeUnit);
    }

    @Transactional
    public void addAuthorities() {
        addAuthority(RECORDS_MANAGER);
        addAuthority(RECORDS_KEEPER);
        addAuthority(CASE_HANDLER);
        addAuthority(LEADER);
        addAuthority(GUEST);
    }

    @Transactional
    public void addUserAdmin() {
        if (userService.userExists("admin@example.com")) {
            logger.info("During startup, user admin@example.com is already " +
                    "registered in the database");
            return;
        }
        AdministrativeUnit administrativeUnit = getAdministrativeUnitOrThrow();
        User admin = new User();
        admin.setPassword("password");
        admin.setFirstname("Frank");
        admin.setLastname("Grimes");
        admin.setUsername("admin@example.com");
        admin.addAuthority(authorityRepository
                .findByAuthorityName(RECORDS_MANAGER));
        administrativeUnit.addUser(admin);
        userService.createNewUser(admin);
    }

    @Transactional
    public void addUserRecordKeeper() {
        if (userService.userExists("recordkeeper@example.com")) {
            logger.info("During startup, user recordkeeper@example.com is " +
                    "already registered in the database");
            return;
        }
        AdministrativeUnit administrativeUnit = getAdministrativeUnitOrThrow();
        User recordKeeper = new User();
        recordKeeper.setPassword("password");
        recordKeeper.setFirstname("Moe");
        recordKeeper.setLastname("Szyslak");
        recordKeeper.setUsername("recordkeeper@example.com");
        recordKeeper.addAuthority(authorityRepository
                .findByAuthorityName(RECORDS_KEEPER));
        administrativeUnit.addUser(recordKeeper);
        userService.createNewUser(recordKeeper);
    }

    private AdministrativeUnit getAdministrativeUnitOrThrow() {
        Optional<AdministrativeUnit> administrativeUnitOptional =
                administrativeUnitService.findFirst();
        if (administrativeUnitOptional.isPresent()) {
            return administrativeUnitOptional.get();
        } else {
            throw new NikitaMisconfigurationException("Could not find default" +
                    " administrativeUnit for demo users");
        }
    }

    private void addAuthority(AuthorityName authorityName) {
        Authority authority = new Authority();
        if (!userService.authorityExists(authorityName)) {
            authority.setAuthorityName(authorityName);
            authorityRepository.save(authority);
        }
    }
}
