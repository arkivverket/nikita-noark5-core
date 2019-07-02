package nikita.webapp.util;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.Authority;
import nikita.common.model.noark5.v5.admin.AuthorityName;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.repository.nikita.AuthorityRepository;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import nikita.webapp.service.impl.admin.AdministrativeUnitService;
import nikita.webapp.service.impl.admin.UserService;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IFondsService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.service.interfaces.ISeriesService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static nikita.common.config.Constants.SYSTEM;
import static nikita.common.config.Constants.TEST_ADMINISTRATIVE_UNIT;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.model.noark5.v5.admin.AuthorityName.*;
import static nikita.common.model.noark5.v5.admin.AuthorityName.CASE_HANDLER;
import static nikita.common.model.noark5.v5.admin.AuthorityName.GUEST;
import static nikita.common.util.CommonUtils.WebUtils.addNorToEnglishNameMap;

/**
 * Created by tsodring
 * <p>
 * nikita implements the Noark 5 data model with english attributes e.g. 'title'
 * instead of 'tittel'. This is because we hope that others may find nikita
 * useful for their language and can build out an approach for their language.
 * <p>
 * Most of the time there wasn't an issue, but when
 * serialising/deserialising we simply put in the correct attribute name, but
 * when it came to handling OData requests and developing a OData parser, we
 * ended a better approach. While this approach still is suboptimal (it can't
 * handle multiple languages), it is what we landed on. Further it was taken
 * out to an own component in order to have a more SOLID approach to classes, as
 * opposed to singular gigantic classes that do too much.
 * <p>
 * Note: Not all attributes are mapped yet.
 */
@Service
@Transactional
public class DemoData {

    private UserService userService;
    private AuthorityRepository authorityRepository;
    private AdministrativeUnitService administrativeUnitService;
    private IFondsService fondsService;
    private ISeriesService seriesService;
    private ICaseFileService caseFileService;
    private IRecordService recordService;

    public DemoData(UserService userService,
                    AuthorityRepository authorityRepository,
                    AdministrativeUnitService administrativeUnitService,
                    IFondsService fondsService,
                    ISeriesService seriesService,
                    ICaseFileService caseFileService,
                    IRecordService recordService) {
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.administrativeUnitService = administrativeUnitService;
        this.fondsService = fondsService;
        this.seriesService = seriesService;
        this.caseFileService = caseFileService;
        this.recordService = recordService;
    }

    /**
     * Create a mapping of Norwegian names to english names for the Noark
     * domain model.
     */
    public void addAdminUnit() {
        // Create an administrative unit
        AdministrativeUnit administrativeUnit = new AdministrativeUnit();

        administrativeUnit.setAdministrativeUnitName(TEST_ADMINISTRATIVE_UNIT);
        administrativeUnit.setShortName("test");
        administrativeUnit.setDefaultAdministrativeUnit(true);

        administrativeUnitService.createNewAdministrativeUnitBySystem(
                administrativeUnit);
    }

    public void addAuthorities() {

        // Create some authorities and users
        Authority adminAuthority = new Authority();
        if (!userService.authorityExists(RECORDS_MANAGER)) {
            adminAuthority.setAuthorityName(RECORDS_MANAGER);
            authorityRepository.save(adminAuthority);
        }

        Authority recordsKeeperAuthority = new Authority();
        if (!userService.authorityExists(RECORDS_KEEPER)) {
            recordsKeeperAuthority.setAuthorityName(RECORDS_KEEPER);
            authorityRepository.save(recordsKeeperAuthority);
        }

        Authority caseHandlerAuthority = new Authority();
        if (!userService.authorityExists(CASE_HANDLER)) {
            caseHandlerAuthority.setAuthorityName(
                    CASE_HANDLER);
            authorityRepository.save(caseHandlerAuthority);
        }

        Authority leaderAuthority = new Authority();
        if (!userService.authorityExists(LEADER)) {
            leaderAuthority.setAuthorityName(LEADER);
            authorityRepository.save(leaderAuthority);
        }

        Authority guestAuthority = new Authority();
        if (!userService.authorityExists(GUEST)) {
            guestAuthority.setAuthorityName(GUEST);
            authorityRepository.save(guestAuthority);
        }
    }

    public void addUserAdmin() {

        AdministrativeUnit administrativeUnit = getAdministrativeUnitOrThrow();
        User admin = new User();

        if (!userService.userExists("admin@example.com")) {
            admin.setPassword("password");
            admin.setFirstname("Frank");
            admin.setLastname("Grimes");
            admin.setUsername("admin@example.com");
            admin.addAuthority(authorityRepository.
                    findByAuthorityName(RECORDS_MANAGER));
            administrativeUnit.addUser(admin);
            admin.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(admin);
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
}
