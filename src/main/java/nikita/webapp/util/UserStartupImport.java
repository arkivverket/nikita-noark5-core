package nikita.webapp.util;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.Authority;
import nikita.common.model.noark5.v5.admin.AuthorityName;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.repository.nikita.AuthorityRepository;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import nikita.webapp.service.impl.admin.AdministrativeUnitService;
import nikita.webapp.service.impl.admin.UserService;
import nikita.webapp.util.exceptions.UsernameExistsException;
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
@Transactional
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

    public void addAuthorities() {
        addAuthority(RECORDS_MANAGER);
        addAuthority(RECORDS_KEEPER);
        addAuthority(CASE_HANDLER);
        addAuthority(LEADER);
        addAuthority(GUEST);
    }

    public void addUserAdmin() {
        AdministrativeUnit administrativeUnit = getAdministrativeUnitOrThrow();
        User admin = new User();
        admin.setPassword("password");
        admin.setFirstname("Frank");
        admin.setLastname("Grimes");
        admin.setUsername("admin@example.com");
        admin.addAuthority(authorityRepository
                .findByAuthorityName(RECORDS_MANAGER));
        admin.addAdministrativeUnit(administrativeUnit);
        try {
            userService.createNewUser(admin);
        } catch (UsernameExistsException e) {
            logger.info("During startup, user " + admin.getUsername() +
                    "is already registered in the database");
        }
    }

    public void addUserRecordKeeper() {
        AdministrativeUnit administrativeUnit = getAdministrativeUnitOrThrow();
        User recordKeeper = new User();
        if (!userService.userExists("recordkeeper@example.com")) {
            recordKeeper.setPassword("password");
            recordKeeper.setFirstname("Moe");
            recordKeeper.setLastname("Szyslak");
            recordKeeper.setUsername("recordkeeper@example.com");
            recordKeeper.addAuthority(authorityRepository.
                    findByAuthorityName(RECORDS_KEEPER));
            administrativeUnit.addUser(recordKeeper);
            recordKeeper.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(recordKeeper);
        }
    }

    public void addUserCaseHandler() {
        AdministrativeUnit administrativeUnit = getAdministrativeUnitOrThrow();
        User caseHandler = new User();
        if (!userService.userExists("casehandler@example.com")) {
            caseHandler.setPassword("password");
            caseHandler.setFirstname("Rainier");
            caseHandler.setLastname("Wolfcastle");
            caseHandler.setUsername("casehandler@example.com");
            caseHandler.addAuthority(authorityRepository.
                    findByAuthorityName(CASE_HANDLER));
            administrativeUnit.addUser(caseHandler);
            caseHandler.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(caseHandler);
        }
    }

    public void addUserLeader() {
        AdministrativeUnit administrativeUnit = getAdministrativeUnitOrThrow();
        User leader = new User();
        if (!userService.userExists("leader@example.com")) {
            leader.setPassword("password");
            leader.setFirstname("Johnny");
            leader.setLastname("Tightlips");
            leader.setUsername("leader@example.com");
            leader.addAuthority(authorityRepository.
                    findByAuthorityName(LEADER));
            administrativeUnit.addUser(leader);
            leader.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(leader);
        }
    }

    public void addUserGuest() {
        AdministrativeUnit administrativeUnit = getAdministrativeUnitOrThrow();
        User guest = new User();
        if (!userService.userExists("cletus@example.com")) {
            guest.setPassword("password");
            guest.setFirstname("Cletus");
            guest.setLastname("'Spuckler'");
            guest.setUsername("cletus@example.com");
            guest.addAuthority(authorityRepository.
                    findByAuthorityName(GUEST));
            administrativeUnit.addUser(guest);
            guest.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(guest);
        }
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

    private Authority addAuthority(AuthorityName authorityName) {
        Authority authority = new Authority();
        if (!userService.authorityExists(authorityName)) {
            authority.setAuthorityName(authorityName);
            authorityRepository.save(authority);
        }
        return authority;
    }
}
