package nikita.webapp.run;

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
import nikita.common.util.CommonUtils;
import nikita.webapp.service.impl.admin.AdministrativeUnitService;
import nikita.webapp.service.impl.admin.UserService;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IFondsService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.service.interfaces.ISeriesService;
import nikita.webapp.util.InternalNameTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.System.out;
import static nikita.common.config.Constants.*;
import static nikita.common.config.FileConstants.*;
import static nikita.common.model.noark5.v5.admin.AuthorityName.*;
import static nikita.common.util.CommonUtils.FileUtils.addProductionToArchiveVersion;
import static nikita.common.util.CommonUtils.FileUtils.setDefaultMimeTypesAsConvertible;

/**
 * Create som basic data if application is in demo mode
 */
@Component
public class AfterApplicationStartup {

    private static final Logger logger =
            LoggerFactory.getLogger(AfterApplicationStartup.class);
    private final RequestMappingHandlerMapping handlerMapping;
    private UserService userService;
    private AuthorityRepository authorityRepository;
    private AdministrativeUnitService administrativeUnitService;
    private IFondsService fondsService;
    private ISeriesService seriesService;
    private ICaseFileService caseFileService;
    private IRecordService recordService;
    private ApplicationContext applicationContext;
    private InternalNameTranslator internalNameTranslator;

    @Value("${nikita.startup.create-demo-users}")
    private Boolean createUsers = false;

    @Value("${nikita.startup.create-directory-store}")
    private Boolean createDirectoryStore = false;

    @Value("${nikita.startup.directory-store-name}")
    private String directoryStoreName = "/data/nikita/storage";

    @Value("${nikita.startup.incoming-directory}")
    private String incomingDirectoryName = "/data/nikita/storage/incoming";

    public AfterApplicationStartup(@Qualifier("requestMappingHandlerMapping")
                                           RequestMappingHandlerMapping handlerMapping,
                                   UserService userService,
                                   AuthorityRepository authorityRepository,
                                   AdministrativeUnitService
                                           administrativeUnitService,
                                   IFondsService fondsService,
                                   ISeriesService seriesService,
                                   ICaseFileService caseFileService,
                                   IRecordService recordService,
                                   ApplicationContext applicationContext,
                                   InternalNameTranslator internalNameTranslator) {

        this.handlerMapping = handlerMapping;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.fondsService = fondsService;
        this.seriesService = seriesService;
        this.caseFileService = caseFileService;
        this.administrativeUnitService = administrativeUnitService;
        this.recordService = recordService;
        this.applicationContext = applicationContext;
        this.internalNameTranslator = internalNameTranslator;
    }

    /**
     * Undertake some business logic after application starts. This is as
     * follows :
     * - mapEndpointsWithHttpMethods()
     * - populateTranslatedNames()
     * - createDemoUsers()
     */
    public void afterApplicationStarts() {
        mapEndpointsWithHttpMethods();
        mapProductionToArchiveMimeTypes();
        populateTranslatedNames();
        setDefaultMimeTypesAsConvertible();

        if (createDirectoryStore) {
            createDirectoryStoreIfNotExists();
        }

        if (createUsers) {
            createDemoUsers();
        }
    }

    private void populateTranslatedNames() {
        internalNameTranslator.populateTranslatedNames();
    }

    /**
     * Check if the document storage directory exists, if not try to
     * create it. If there is a problem shut down the core.
     */
    private void createDirectoryStoreIfNotExists() {
        try {
            if (!Files.exists(Paths.get(directoryStoreName))) {
                Files.createDirectories(Paths.get(directoryStoreName));
            }
            if (!Files.exists(Paths.get(incomingDirectoryName))) {
                Files.createDirectories(Paths.get(incomingDirectoryName));
            }
        } catch (IOException e) {
            ((ConfigurableApplicationContext) applicationContext).close();
            logger.error("Unable to create document store directories! " +
                    "Exiting!");
        }
    }


    private void mapProductionToArchiveMimeTypes() {

        addProductionToArchiveVersion(MIME_TYPE_ODT, FILE_EXTENSION_ODT,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_ODS, FILE_EXTENSION_ODS,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_ODP, FILE_EXTENSION_ODP,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_DOCX, FILE_EXTENSION_DOCX,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_DOC, FILE_EXTENSION_DOC,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_XLSX, FILE_EXTENSION_XLSX,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_XLS, FILE_EXTENSION_XLS,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_PPT, FILE_EXTENSION_PPT,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_PPTX, FILE_EXTENSION_PPTX,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_PNG, FILE_EXTENSION_PNG,
                MIME_TYPE_PDF);
        addProductionToArchiveVersion(MIME_TYPE_GIF, FILE_EXTENSION_GIF,
                MIME_TYPE_GIF);
        addProductionToArchiveVersion(MIME_TYPE_TEXT, FILE_EXTENSION_TEXT,
                MIME_TYPE_TEXT);
        addProductionToArchiveVersion(MIME_TYPE_PDF, FILE_EXTENSION_PDF,
                MIME_TYPE_PDF);
    }

    /**
     * map endpoints with their HTTP methods
     * <p>
     * Go through the list of endpoints and make a list of endpoints and the
     * HTTP methods they support. We were unable to get CORS implemented
     * properly and this approach was required to make to work correctly.
     * <p>
     * We shouldn't have to d this, it should be handled by spring. Out
     * application was over annotated and that could be the original cause of
     * the Cors problems. Eventually this code should be removed and Cors
     * handled by spring.
     */
    private void mapEndpointsWithHttpMethods() {

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry :
                handlerMapping.getHandlerMethods().entrySet()) {

            RequestMappingInfo requestMappingInfo = entry.getKey();
            // Assuming there is always a non-null value
            String servletPaths = requestMappingInfo.getPatternsCondition().toString();
            // Typically the servletPaths looks like [ /api/arkivstruktur/arkiv ]
            // If it contains two more it looks like
            //      [ /api/arkivstruktur/ny-arkivskaper || /api/arkivstruktur/arkiv/ny-arkivskaper ]
            // So simplest way to process is split on space and ignore the "||"
            //  ignore is done in false == servletPaths.contains("|") below
            // servletPath starts with "[" and ends with "]". Removing them if they are there
            if (servletPaths.startsWith("[")) {
                servletPaths = servletPaths.substring(1);
            }
            if (servletPaths.endsWith("]")) {
                servletPaths = servletPaths.substring(0, servletPaths.length() - 1);
            }

            String[] servletPathList = servletPaths.split("\\s+");

            for (String servletPath : servletPathList) {

                if (servletPath != null && !servletPath.contains("|")) {

                    // Adding a trailing slash as the incoming request may or may not have it
                    // This is done to be consist on a lookup
                    if (!servletPath.endsWith("/")) {
                        servletPath += SLASH;
                    }

                    Set<RequestMethod> httpMethodRequests = requestMappingInfo.getMethodsCondition().getMethods();
                    if (null != httpMethodRequests) {
                        // RequestMethod and HTTPMethod are different types, have to convert them here
                        Set<HttpMethod> httpMethods = new TreeSet<>();
                        for (RequestMethod requestMethod : httpMethodRequests) {
                            if (requestMethod.equals(RequestMethod.GET)) {
                                httpMethods.add(HttpMethod.GET);
                            } else if (requestMethod.equals(RequestMethod.DELETE)) {
                                httpMethods.add(HttpMethod.DELETE);
                            } else if (requestMethod.equals(RequestMethod.OPTIONS)) {
                                httpMethods.add(HttpMethod.OPTIONS);
                            } else if (requestMethod.equals(RequestMethod.HEAD)) {
                                httpMethods.add(HttpMethod.HEAD);
                            } else if (requestMethod.equals(RequestMethod.PATCH)) {
                                httpMethods.add(HttpMethod.PATCH);
                            } else if (requestMethod.equals(RequestMethod.POST)) {
                                httpMethods.add(HttpMethod.POST);
                            } else if (requestMethod.equals(RequestMethod.PUT)) {
                                httpMethods.add(HttpMethod.PUT);
                            } else if (requestMethod.equals(RequestMethod
                                    .TRACE)) {
                                httpMethods.add(HttpMethod.TRACE);
                            }
                        }
                        out.println("Adding " + servletPath + " methods " + httpMethods);
                        CommonUtils.WebUtils.addRequestToMethodMap(servletPath, httpMethods);
                    } else {
                        logger.warn("Missing HTTP Methods for the following servletPath [" + servletPath + "]");
                    }
                }
            }
        }


        // This has to be a temporary addition. This approach to cors is silly!!
        // Cors should just work, we should not have to do this work!!
        Set<HttpMethod> httpMethods = new TreeSet<>();
        httpMethods.add(HttpMethod.GET);
        httpMethods.add(HttpMethod.OPTIONS);
        httpMethods.add(HttpMethod.POST);
        String servletPath = "/oauth/token/";
        logger.info("Adding " + servletPath + " methods " + httpMethods);
        CommonUtils.WebUtils.addRequestToMethodMap(servletPath, httpMethods);

    }


    /**
     * Create users so application has some users out of the box. This would
     * not be done in production, but as a demo it's fine!
     */
    private void createDemoUsers() {

        // Create an administrative unit
        AdministrativeUnit administrativeUnit = new AdministrativeUnit();

        administrativeUnit.setAdministrativeUnitName(TEST_ADMINISTRATIVE_UNIT);
        administrativeUnit.setShortName("test");
        administrativeUnit.setCreatedBy(SYSTEM);
        administrativeUnit.setOwnedBy(SYSTEM);
        administrativeUnit.setDefaultAdministrativeUnit(true);

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
        if (!userService.authorityExists(AuthorityName.CASE_HANDLER)) {
            caseHandlerAuthority.setAuthorityName(
                    AuthorityName.CASE_HANDLER);
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

        User admin = new User();
        if (!userService.userExists("admin@example.com")) {
            admin.setPassword("password");
            admin.setFirstname("Frank");
            admin.setLastname("Grimes");
            admin.setUsername("admin@example.com");
            admin.addAuthority(adminAuthority);
            administrativeUnit.addUser(admin);
            admin.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(admin);
        }

        User recordKeeper = new User();
        if (!userService.userExists("recordkeeper@example.com")) {
            recordKeeper.setPassword("password");
            recordKeeper.setFirstname("Moe");
            recordKeeper.setLastname("Szyslak");
            recordKeeper.setUsername("recordkeeper@example.com");
            recordKeeper.addAuthority(recordsKeeperAuthority);
            administrativeUnit.addUser(recordKeeper);
            recordKeeper.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(recordKeeper);
        }

        User caseHandler = new User();
        if (!userService.userExists("casehandler@example.com")) {
            caseHandler.setPassword("password");
            caseHandler.setFirstname("Rainier");
            caseHandler.setLastname("Wolfcastle");
            caseHandler.setUsername("casehandler@example.com");
            caseHandler.addAuthority(caseHandlerAuthority);
            administrativeUnit.addUser(caseHandler);
            caseHandler.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(caseHandler);
        }

        User leader = new User();
        if (!userService.userExists("leader@example.com")) {
            leader.setPassword("password");
            leader.setFirstname("Johnny");
            leader.setLastname("Tightlips");
            leader.setUsername("leader@example.com");
            leader.addAuthority(leaderAuthority);
            administrativeUnit.addUser(leader);
            leader.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(leader);
        }

        User guest = new User();
        if (!userService.userExists("cletus@example.com")) {
            guest.setPassword("password");
            guest.setFirstname("Cletus");
            guest.setLastname("'Spuckler'");
            guest.setUsername("cletus@example.com");
            guest.addAuthority(guestAuthority);
            administrativeUnit.addUser(guest);
            guest.addAdministrativeUnit(administrativeUnit);
            userService.createNewUser(guest);
        }

        administrativeUnitService.createNewAdministrativeUnitBySystem(
                administrativeUnit);
    }

    private void createDemoData() {

        Fonds fonds = new Fonds();
        fonds.setTitle("Test fonds");
        fondsService.createNewFonds(fonds);
        fonds.setOwnedBy("admin@example.com");
        fondsService.handleUpdate(fonds.getSystemId(),
                fonds.getVersion(), fonds);

        Series series = new Series();
        series.setTitle("Test series");
        series.setSeriesStatus("Opprettet");
        fondsService.createSeriesAssociatedWithFonds(
                fonds.getSystemId(), series);

        series.setOwnedBy("admin@example.com");
        seriesService.handleUpdate(series.getSystemId(),
                fonds.getVersion(), series);


        fondsService.createSeriesAssociatedWithFonds(
                fonds.getSystemId(), series);

        CaseFile caseFile = new CaseFile();

        caseFile.setTitle("Søknad om barnehageplass");
        caseFile.setOfficialTitle("Søknad om barnehageplass");
        caseFile.setCaseStatus("Opprettet av saksbehandler");

        seriesService.createCaseFileAssociatedWithSeries(series.getSystemId(),
                caseFile);

        RegistryEntry registryEntry = new RegistryEntry();
        registryEntry.setTitle("Innkommende brev");
        registryEntry.setRecordStatus("Journalført");
        registryEntry.setRegistryEntryType("Inngående dokument");
        caseFileService.
                createRegistryEntryAssociatedWithCaseFile(caseFile.getSystemId(),
                        registryEntry);

        DocumentDescription documentDescription = new DocumentDescription();
        documentDescription.setTitle(registryEntry.getTitle());
        documentDescription.setDescription("Beskrivelsen!!");
        documentDescription.setAssociatedWithRecordAs("Hoveddokument");
        documentDescription.setDocumentType("Brev");
        registryEntry.addReferenceDocumentDescription(documentDescription);
        documentDescription.addReferenceRecord(registryEntry);

        recordService.createDocumentDescriptionAssociatedWithRecord(
                registryEntry.getSystemId(), documentDescription);
    }
}
