package nikita.webapp.run;

import nikita.common.model.noark5.v4.DocumentDescription;
import nikita.common.model.noark5.v4.Fonds;
import nikita.common.model.noark5.v4.Series;
import nikita.common.model.noark5.v4.admin.AdministrativeUnit;
import nikita.common.model.noark5.v4.admin.Authority;
import nikita.common.model.noark5.v4.admin.AuthorityName;
import nikita.common.model.noark5.v4.admin.User;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.model.noark5.v4.casehandling.RegistryEntry;
import nikita.common.repository.nikita.AuthorityRepository;
import nikita.common.util.CommonUtils;
import nikita.webapp.service.impl.admin.AdministrativeUnitService;
import nikita.webapp.service.impl.admin.UserService;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IFondsService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.service.interfaces.ISeriesService;
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
import static nikita.common.config.N5ResourceMappings.CASE_HANDLER;
import static nikita.common.config.N5ResourceMappings.REFERENCE_ADMINISTRATIVE_UNIT;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.model.noark5.v4.admin.AuthorityName.*;
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
                                   ApplicationContext applicationContext) {

        this.handlerMapping = handlerMapping;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.fondsService = fondsService;
        this.seriesService = seriesService;
        this.caseFileService = caseFileService;
        this.administrativeUnitService = administrativeUnitService;
        this.recordService = recordService;
        this.applicationContext = applicationContext;
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
     * Create a mapping of Norwegian names to english names for handling OData
     * requests.
     * <p>
     * The domain model in nikita is in English and this causes a problem when
     * a OData request is to be handled. We have to be able to map from a
     * Norwegian name to English e.g. tittel -> title
     */
    private void populateTranslatedNames() {

        // Add entity name mappings

        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ADMINISTRATIVE_UNIT,
                        ADMINISTRATIVE_UNIT_ENG,
                        ADMINISTRATIVE_UNIT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (AUTHOR,
                        AUTHOR_ENG,
                        AUTHOR_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (BASIC_RECORD,
                        BASIC_RECORD_ENG,
                        BASIC_RECORD_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_FILE,
                        CASE_FILE_ENG,
                        CASE_FILE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFIED_CODE,
                        CLASSIFIED_CODE_ENG,
                        CLASSIFIED_CODE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CODE,
                        CODE_ENG,
                        CODE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (COMMENT,
                        COMMENT_ENG,
                        COMMENT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CONVERSION,
                        CONVERSION_ENG,
                        CONVERSION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (COUNTRY,
                        COUNTRY_ENG,
                        COUNTRY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DELETION,
                        DELETION_ENG,
                        DELETION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_DESCRIPTION,
                        DOCUMENT_DESCRIPTION_ENG,
                        DOCUMENT_DESCRIPTION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_FLOW,
                        DOCUMENT_FLOW_ENG,
                        DOCUMENT_FLOW_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_MEDIUM,
                        DOCUMENT_MEDIUM_ENG,
                        DOCUMENT_MEDIUM_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT,
                        DOCUMENT_OBJECT_ENG,
                        DOCUMENT_OBJECT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ELECTRONIC_SIGNATURE,
                        ELECTRONIC_SIGNATURE_ENG,
                        ELECTRONIC_SIGNATURE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (EVENT_TYPE,
                        EVENT_TYPE_ENG,
                        EVENT_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FILE,
                        FILE_ENG,
                        FILE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FILE_TYPE,
                        FILE_TYPE_ENG,
                        FILE_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FLOW_STATUS,
                        FLOW_STATUS_ENG,
                        FLOW_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FONDS_CREATOR,
                        FONDS_CREATOR_ENG,
                        FONDS_CREATOR_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FONDS,
                        FONDS_ENG,
                        FONDS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (KEYWORD,
                        KEYWORD_ENG,
                        KEYWORD_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (MEETING_FILE,
                        MEETING_FILE_ENG,
                        MEETING_FILE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (MEETING_PARTICIPANT,
                        MEETING_PARTICIPANT_ENG,
                        MEETING_PARTICIPANT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (MEETING_RECORD,
                        MEETING_RECORD_ENG,
                        MEETING_RECORD_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRATION,
                        REGISTRATION_ENG,
                        REGISTRATION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY,
                        REGISTRY_ENTRY_ENG,
                        REGISTRY_ENTRY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SCREENING,
                        SCREENING_ENG,
                        SCREENING_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SECONDARY_CLASSIFICATION,
                        SECONDARY_CLASSIFICATION_ENG,
                        SECONDARY_CLASSIFICATION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SERIES,
                        SERIES_ENG,
                        SERIES_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SIGN_OFF,
                        SIGN_OFF_ENG,
                        SIGN_OFF_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (STORAGE_LOCATION,
                        STORAGE_LOCATION_ENG,
                        STORAGE_LOCATION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SUB_FONDS,
                        SUB_FONDS_ENG,
                        SUB_FONDS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (USER,
                        USER_ENG,
                        USER_ENG_OBJECT);

        // Add attribute name mappings
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ACCESS_CATEGORY,
                        ACCESS_CATEGORY_ENG,
                        ACCESS_CATEGORY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ACCESS_RESTRICTION,
                        ACCESS_RESTRICTION_ENG,
                        ACCESS_RESTRICTION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ADDRESS_LINE_1,
                        ADDRESS_LINE_1_ENG,
                        ADDRESS_LINE_1_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ADDRESS_LINE_2,
                        ADDRESS_LINE_2_ENG,
                        ADDRESS_LINE_2_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ADDRESS_LINE_3,
                        ADDRESS_LINE_3_ENG,
                        ADDRESS_LINE_3_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ADMINISTRATIVE_UNIT_NAME,
                        ADMINISTRATIVE_UNIT_NAME_ENG,
                        ADMINISTRATIVE_UNIT_NAME_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ADMINISTRATIVE_UNIT_PARENT_REFERENCE,
                        ADMINISTRATIVE_UNIT_PARENT_REFERENCE_ENG,
                        ADMINISTRATIVE_UNIT_PARENT_REFERENCE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ADMINISTRATIVE_UNIT_STATUS,
                        ADMINISTRATIVE_UNIT_STATUS_ENG,
                        ADMINISTRATIVE_UNIT_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ASSOCIATED_WITH_RECORD_AS,
                        ASSOCIATED_WITH_RECORD_AS_ENG,
                        ASSOCIATED_WITH_RECORD_AS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (BASIC_RECORD_ID,
                        BASIC_RECORD_ID_ENG,
                        BASIC_RECORD_ID_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (BUSINESS_ADDRESS,
                        BUSINESS_ADDRESS_ENG,
                        BUSINESS_ADDRESS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_DATE,
                        CASE_DATE_ENG,
                        CASE_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_HANDLER,
                        CASE_HANDLER_ENG,
                        CASE_HANDLER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_LOANED_DATE,
                        CASE_LOANED_DATE_ENG,
                        CASE_LOANED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_LOANED_TO,
                        CASE_LOANED_TO_ENG,
                        CASE_LOANED_TO_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_PARTY,
                        CASE_PARTY_ENG,
                        CASE_PARTY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_PARTY_ID,
                        CASE_PARTY_ID_ENG,
                        CASE_PARTY_ID_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_PARTY_NAME,
                        CASE_PARTY_NAME_ENG,
                        CASE_PARTY_NAME_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_PARTY_ROLE,
                        CASE_PARTY_ROLE_ENG,
                        CASE_PARTY_ROLE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_RECORDS_MANAGEMENT_UNIT,
                        CASE_RECORDS_MANAGEMENT_UNIT_ENG,
                        CASE_RECORDS_MANAGEMENT_UNIT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_RESPONSIBLE,
                        CASE_RESPONSIBLE_ENG,
                        CASE_RESPONSIBLE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_SEQUENCE_NUMBER,
                        CASE_SEQUENCE_NUMBER_ENG,
                        CASE_SEQUENCE_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_STATUS,
                        CASE_STATUS_ENG,
                        CASE_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CASE_YEAR,
                        CASE_YEAR_ENG,
                        CASE_YEAR_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASS,
                        CLASS_ENG,
                        CLASS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASS_ID,
                        CLASS_ID_ENG,
                        CLASS_ID_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFICATION_BY,
                        CLASSIFICATION_BY_ENG,
                        CLASSIFICATION_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFICATION,
                        CLASSIFICATION_ENG,
                        CLASSIFICATION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFICATION_DATE,
                        CLASSIFICATION_DATE_ENG,
                        CLASSIFICATION_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFICATION_DOWNGRADED_BY,
                        CLASSIFICATION_DOWNGRADED_BY_ENG,
                        CLASSIFICATION_DOWNGRADED_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFICATION_DOWNGRADED_DATE,
                        CLASSIFICATION_DOWNGRADED_DATE_ENG,
                        CLASSIFICATION_DOWNGRADED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFICATION_SYSTEM,
                        CLASSIFICATION_SYSTEM_ENG,
                        CLASSIFICATION_SYSTEM_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFICATION_SYSTEM_TYPE,
                        CLASSIFICATION_SYSTEM_TYPE_ENG,
                        CLASSIFICATION_SYSTEM_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFICATION_TYPE,
                        CLASSIFICATION_TYPE_ENG,
                        CLASSIFICATION_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CLASSIFIED,
                        CLASSIFIED_ENG,
                        CLASSIFIED_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (COMMENT_DATE,
                        COMMENT_DATE_ENG,
                        COMMENT_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (COMMENT_REGISTERED_BY,
                        COMMENT_REGISTERED_BY_ENG,
                        COMMENT_REGISTERED_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (COMMENT_TEXT,
                        COMMENT_TEXT_ENG,
                        COMMENT_TEXT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (COMMENT_TYPE,
                        COMMENT_TYPE_ENG,
                        COMMENT_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CONTACT_INFORMATION,
                        CONTACT_INFORMATION_ENG,
                        CONTACT_INFORMATION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CONTACT_PERSON,
                        CONTACT_PERSON_ENG,
                        CONTACT_PERSON_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CONVERSION_COMMENT,
                        CONVERSION_COMMENT_ENG,
                        CONVERSION_COMMENT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CONVERSION_TOOL,
                        CONVERSION_TOOL_ENG,
                        CONVERSION_TOOL_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CONVERTED_BY,
                        CONVERTED_BY_ENG,
                        CONVERTED_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CONVERTED_DATE,
                        CONVERTED_DATE_ENG,
                        CONVERTED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CONVERTED_FROM_FORMAT,
                        CONVERTED_FROM_FORMAT_ENG,
                        CONVERTED_FROM_FORMAT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CONVERTED_TO_FORMAT,
                        CONVERTED_TO_FORMAT_ENG,
                        CONVERTED_TO_FORMAT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CORRESPONDENCE_PART,
                        CORRESPONDENCE_PART_ENG,
                        CORRESPONDENCE_PART_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CORRESPONDENCE_PART_INTERNAL,
                        CORRESPONDENCE_PART_INTERNAL_ENG,
                        CORRESPONDENCE_PART_INTERNAL_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CORRESPONDENCE_PART_NAME,
                        CORRESPONDENCE_PART_NAME_ENG,
                        CORRESPONDENCE_PART_NAME_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CORRESPONDENCE_PART_PERSON,
                        CORRESPONDENCE_PART_PERSON_ENG,
                        CORRESPONDENCE_PART_PERSON_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CORRESPONDENCE_PART_TYPE,
                        CORRESPONDENCE_PART_TYPE_ENG,
                        CORRESPONDENCE_PART_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CORRESPONDENCE_PART_UNIT,
                        CORRESPONDENCE_PART_UNIT_ENG,
                        CORRESPONDENCE_PART_UNIT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (COUNTRY_CODE,
                        COUNTRY_CODE_ENG,
                        COUNTRY_CODE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CREATED_BY,
                        CREATED_BY_ENG,
                        CREATED_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CREATED_DATE,
                        CREATED_DATE_ENG,
                        CREATED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CROSS_REFERENCE_CLASS,
                        CROSS_REFERENCE_CLASS_ENG,
                        CROSS_REFERENCE_CLASS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CROSS_REFERENCE,
                        CROSS_REFERENCE_ENG,
                        CROSS_REFERENCE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CROSS_REFERENCE_FILE,
                        CROSS_REFERENCE_FILE_ENG,
                        CROSS_REFERENCE_FILE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (CROSS_REFERENCE_RECORD,
                        CROSS_REFERENCE_RECORD_ENG,
                        CROSS_REFERENCE_RECORD_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DELETION_BY,
                        DELETION_BY_ENG,
                        DELETION_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DELETION_DATE,
                        DELETION_DATE_ENG,
                        DELETION_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DELETION_TYPE,
                        DELETION_TYPE_ENG,
                        DELETION_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DESCRIPTION,
                        DESCRIPTION_ENG,
                        DESCRIPTION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DISPOSAL_AUTHORITY,
                        DISPOSAL_AUTHORITY_ENG,
                        DISPOSAL_AUTHORITY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DISPOSAL_DATE,
                        DISPOSAL_DATE_ENG,
                        DISPOSAL_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DISPOSAL_DECISION,
                        DISPOSAL_DECISION_ENG,
                        DISPOSAL_DECISION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DISPOSAL,
                        DISPOSAL_ENG,
                        DISPOSAL_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DISPOSAL_PRESERVATION_TIME,
                        DISPOSAL_PRESERVATION_TIME_ENG,
                        DISPOSAL_PRESERVATION_TIME_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DISPOSAL_UNDERTAKEN_BY,
                        DISPOSAL_UNDERTAKEN_BY_ENG,
                        DISPOSAL_UNDERTAKEN_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DISPOSAL_UNDERTAKEN_DATE,
                        DISPOSAL_UNDERTAKEN_DATE_ENG,
                        DISPOSAL_UNDERTAKEN_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DISPOSAL_UNDERTAKEN,
                        DISPOSAL_UNDERTAKEN_ENG,
                        DISPOSAL_UNDERTAKEN_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (D_NUMBER, D_NUMBER_ENG, D_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_DESCRIPTION_ASSOCIATED_BY,
                        DOCUMENT_DESCRIPTION_ASSOCIATED_BY_ENG,
                        DOCUMENT_DESCRIPTION_ASSOCIATED_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS,
                        DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS_ENG,
                        DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_DESCRIPTION_ASSOCIATION_BY,
                        DOCUMENT_DESCRIPTION_ASSOCIATION_BY_ENG,
                        DOCUMENT_DESCRIPTION_ASSOCIATION_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_DESCRIPTION_ASSOCIATION_DATE,
                        DOCUMENT_DESCRIPTION_ASSOCIATION_DATE_ENG,
                        DOCUMENT_DESCRIPTION_ASSOCIATION_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER,
                        DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER_ENG,
                        DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_DESCRIPTION_DOCUMENT_TYPE,
                        DOCUMENT_DESCRIPTION_DOCUMENT_TYPE_ENG,
                        DOCUMENT_DESCRIPTION_DOCUMENT_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_DESCRIPTION_STATUS,
                        DOCUMENT_DESCRIPTION_STATUS_ENG,
                        DOCUMENT_DESCRIPTION_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_FLOW_FLOW_COMMENT,
                        DOCUMENT_FLOW_FLOW_COMMENT_ENG,
                        DOCUMENT_FLOW_FLOW_COMMENT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_FLOW_FLOW_FROM,
                        DOCUMENT_FLOW_FLOW_FROM_ENG,
                        DOCUMENT_FLOW_FLOW_FROM_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_FLOW_FLOW_RECEIVED_DATE,
                        DOCUMENT_FLOW_FLOW_RECEIVED_DATE_ENG,
                        DOCUMENT_FLOW_FLOW_RECEIVED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_FLOW_FLOW_SENT_DATE,
                        DOCUMENT_FLOW_FLOW_SENT_DATE_ENG,
                        DOCUMENT_FLOW_FLOW_SENT_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_FLOW_FLOW_STATUS,
                        DOCUMENT_FLOW_FLOW_STATUS_ENG,
                        DOCUMENT_FLOW_FLOW_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_FLOW_FLOW_TO,
                        DOCUMENT_FLOW_FLOW_TO_ENG,
                        DOCUMENT_FLOW_FLOW_TO_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_CHECKSUM_ALGORITHM,
                        DOCUMENT_OBJECT_CHECKSUM_ALGORITHM_ENG,
                        DOCUMENT_OBJECT_CHECKSUM_ALGORITHM_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_CHECKSUM,
                        DOCUMENT_OBJECT_CHECKSUM_ENG,
                        DOCUMENT_OBJECT_CHECKSUM_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_FILE_NAME,
                        DOCUMENT_OBJECT_FILE_NAME_ENG,
                        DOCUMENT_OBJECT_FILE_NAME_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_FILE_SIZE,
                        DOCUMENT_OBJECT_FILE_SIZE_ENG,
                        DOCUMENT_OBJECT_FILE_SIZE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_FORMAT_DETAILS,
                        DOCUMENT_OBJECT_FORMAT_DETAILS_ENG,
                        DOCUMENT_OBJECT_FORMAT_DETAILS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_FORMAT,
                        DOCUMENT_OBJECT_FORMAT_ENG,
                        DOCUMENT_OBJECT_FORMAT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_MIME_TYPE,
                        DOCUMENT_OBJECT_MIME_TYPE_ENG,
                        DOCUMENT_OBJECT_MIME_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_REFERENCE_DOCUMENT_FILE,
                        DOCUMENT_OBJECT_REFERENCE_DOCUMENT_FILE_ENG,
                        DOCUMENT_OBJECT_REFERENCE_DOCUMENT_FILE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_VARIANT_FORMAT,
                        DOCUMENT_OBJECT_VARIANT_FORMAT_ENG,
                        DOCUMENT_OBJECT_VARIANT_FORMAT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_OBJECT_VERSION_NUMBER,
                        DOCUMENT_OBJECT_VERSION_NUMBER_ENG,
                        DOCUMENT_OBJECT_VERSION_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_STATUS,
                        DOCUMENT_STATUS_ENG,
                        DOCUMENT_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (DOCUMENT_TYPE,
                        DOCUMENT_TYPE_ENG,
                        DOCUMENT_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ELECTRONIC_SIGNATURE_SECURITY_LEVEL,
                        ELECTRONIC_SIGNATURE_SECURITY_LEVEL_ENG,
                        ELECTRONIC_SIGNATURE_SECURITY_LEVEL_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ELECTRONIC_SIGNATURE_VERIFIED_BY,
                        ELECTRONIC_SIGNATURE_VERIFIED_BY_ENG,
                        ELECTRONIC_SIGNATURE_VERIFIED_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ELECTRONIC_SIGNATURE_VERIFIED_DATE,
                        ELECTRONIC_SIGNATURE_VERIFIED_DATE_ENG,
                        ELECTRONIC_SIGNATURE_VERIFIED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ELECTRONIC_SIGNATURE_VERIFIED,
                        ELECTRONIC_SIGNATURE_VERIFIED_ENG,
                        ELECTRONIC_SIGNATURE_VERIFIED_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (EMAIL_ADDRESS,
                        EMAIL_ADDRESS_ENG,
                        EMAIL_ADDRESS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FILE_ID,
                        FILE_ID_ENG,
                        FILE_ID_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FILE_PUBLIC_TITLE,
                        FILE_PUBLIC_TITLE_ENG,
                        FILE_PUBLIC_TITLE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FINALISED_BY,
                        FINALISED_BY_ENG,
                        FINALISED_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FINALISED_DATE,
                        FINALISED_DATE_ENG,
                        FINALISED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FONDS_CREATOR_ID,
                        FONDS_CREATOR_ID_ENG,
                        FONDS_CREATOR_ID_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FONDS_CREATOR_NAME,
                        FONDS_CREATOR_NAME_ENG,
                        FONDS_CREATOR_NAME_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FONDS_STATUS,
                        FONDS_STATUS_ENG,
                        FONDS_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FOREIGN_ADDRESS,
                        FOREIGN_ADDRESS_ENG,
                        FOREIGN_ADDRESS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (FORMAT,
                        FORMAT_ENG,
                        FORMAT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (MEETING_FILE_TYPE,
                        MEETING_FILE_TYPE_ENG,
                        MEETING_FILE_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (MEETING_PARTICIPANT_FUNCTION,
                        MEETING_PARTICIPANT_FUNCTION_ENG,
                        MEETING_PARTICIPANT_FUNCTION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (MEETING_REGISTRATION_STATUS,
                        MEETING_REGISTRATION_STATUS_ENG,
                        MEETING_REGISTRATION_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (MEETING_REGISTRATION_TYPE,
                        MEETING_REGISTRATION_TYPE_ENG,
                        MEETING_REGISTRATION_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (MOBILE_TELEPHONE_NUMBER,
                        MOBILE_TELEPHONE_NUMBER_ENG,
                        MOBILE_TELEPHONE_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (NAME,
                        NAME_ENG,
                        NAME_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (ORGANISATION_NUMBER,
                        ORGANISATION_NUMBER_ENG,
                        ORGANISATION_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (PARENT_CLASS,
                        PARENT_CLASS_ENG,
                        PARENT_CLASS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (POSTAL_ADDRESS,
                        POSTAL_ADDRESS_ENG,
                        POSTAL_ADDRESS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (POSTAL_NUMBER,
                        POSTAL_NUMBER_ENG,
                        POSTAL_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (POSTAL_TOWN,
                        POSTAL_TOWN_ENG,
                        POSTAL_TOWN_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (POST_CODE,
                        POST_CODE_ENG,
                        POST_CODE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (PRECEDENCE_APPROVED_BY,
                        PRECEDENCE_APPROVED_BY_ENG,
                        PRECEDENCE_APPROVED_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (PRECEDENCE_APPROVED_DATE,
                        PRECEDENCE_APPROVED_DATE_ENG,
                        PRECEDENCE_APPROVED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (PRECEDENCE_AUTHORITY,
                        PRECEDENCE_AUTHORITY_ENG,
                        PRECEDENCE_AUTHORITY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (PRECEDENCE_DATE,
                        PRECEDENCE_DATE_ENG,
                        PRECEDENCE_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (PRECEDENCE,
                        PRECEDENCE_ENG,
                        PRECEDENCE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (PRECEDENCE_SOURCE_OF_LAW,
                        PRECEDENCE_SOURCE_OF_LAW_ENG,
                        PRECEDENCE_SOURCE_OF_LAW_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (PRECEDENCE_STATUS,
                        PRECEDENCE_STATUS_ENG,
                        PRECEDENCE_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (RECORD_ARCHIVED_BY, RECORD_ARCHIVED_BY_ENG,
                        RECORD_ARCHIVED_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (RECORD_ARCHIVED_DATE,
                        RECORD_ARCHIVED_DATE_ENG,
                        RECORD_ARCHIVED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REFERENCE_ADMINISTRATIVE_UNIT,
                        REFERENCE_ADMINISTRATIVE_UNIT_ENG,
                        REFERENCE_ADMINISTRATIVE_UNIT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REFERENCE_CASE_HANDLER,
                        REFERENCE_CASE_HANDLER_ENG,
                        REFERENCE_CASE_HANDLER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REFERENCE_SERIES,
                        REFERENCE_SERIES_ENG,
                        REFERENCE_SERIES_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_DATE,
                        REGISTRY_ENTRY_DATE_ENG,
                        REGISTRY_ENTRY_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_DOCUMENT_DATE,
                        REGISTRY_ENTRY_DOCUMENT_DATE_ENG,
                        REGISTRY_ENTRY_DOCUMENT_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_DUE_DATE,
                        REGISTRY_ENTRY_DUE_DATE_ENG,
                        REGISTRY_ENTRY_DUE_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS,
                        REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS_ENG,
                        REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_NUMBER,
                        REGISTRY_ENTRY_NUMBER_ENG,
                        REGISTRY_ENTRY_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_RECEIVED_DATE,
                        REGISTRY_ENTRY_RECEIVED_DATE_ENG,
                        REGISTRY_ENTRY_RECEIVED_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE,
                        REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE_ENG,
                        REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_SENT_DATE,
                        REGISTRY_ENTRY_SENT_DATE_ENG,
                        REGISTRY_ENTRY_SENT_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_SEQUENCE_NUMBER,
                        REGISTRY_ENTRY_SEQUENCE_NUMBER_ENG,
                        REGISTRY_ENTRY_SEQUENCE_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_STATUS,
                        REGISTRY_ENTRY_STATUS_ENG,
                        REGISTRY_ENTRY_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_TYPE,
                        REGISTRY_ENTRY_TYPE_ENG,
                        REGISTRY_ENTRY_TYPE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (REGISTRY_ENTRY_YEAR,
                        REGISTRY_ENTRY_YEAR_ENG,
                        REGISTRY_ENTRY_YEAR_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (RESIDING_ADDRESS,
                        RESIDING_ADDRESS_ENG,
                        RESIDING_ADDRESS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (RIGHT,
                        RIGHT_ENG,
                        RIGHT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SCREENING_ACCESS_RESTRICTION,
                        SCREENING_ACCESS_RESTRICTION_ENG,
                        SCREENING_ACCESS_RESTRICTION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SCREENING_AUTHORITY,
                        SCREENING_AUTHORITY_ENG,
                        SCREENING_AUTHORITY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SCREENING_DOCUMENT,
                        SCREENING_DOCUMENT_ENG,
                        SCREENING_DOCUMENT_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SCREENING_DURATION,
                        SCREENING_DURATION_ENG,
                        SCREENING_DURATION_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SCREENING_EXPIRES_DATE,
                        SCREENING_EXPIRES_DATE_ENG,
                        SCREENING_EXPIRES_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SCREENING_METADATA,
                        SCREENING_METADATA_ENG,
                        SCREENING_METADATA_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SECONDARY_CLASSIFICATION_SYSTEM,
                        SECONDARY_CLASSIFICATION_SYSTEM_ENG,
                        SECONDARY_CLASSIFICATION_SYSTEM_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SERIES,
                        SERIES_ENG,
                        SERIES_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SERIES_END_DATE,
                        SERIES_END_DATE_ENG,
                        SERIES_END_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SERIES_PRECURSOR,
                        SERIES_PRECURSOR_ENG,
                        SERIES_PRECURSOR_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SERIES_START_DATE,
                        SERIES_START_DATE_ENG,
                        SERIES_START_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SERIES_STATUS,
                        SERIES_STATUS_ENG,
                        SERIES_STATUS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SERIES_SUCCESSOR,
                        SERIES_SUCCESSOR_ENG,
                        SERIES_SUCCESSOR_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SHORT_NAME,
                        SHORT_NAME_ENG,
                        SHORT_NAME_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SIGN_OFF_BY,
                        SIGN_OFF_BY_ENG,
                        SIGN_OFF_BY_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SIGN_OFF_DATE,
                        SIGN_OFF_DATE_ENG,
                        SIGN_OFF_DATE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SIGN_OFF_METHOD,
                        SIGN_OFF_METHOD_ENG,
                        SIGN_OFF_METHOD_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SOCIAL_SECURITY_NUMBER,
                        SOCIAL_SECURITY_NUMBER_ENG,
                        SOCIAL_SECURITY_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SUB_CLASS,
                        SUB_CLASS_ENG,
                        SUB_CLASS_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (SYSTEM_ID,
                        SYSTEM_ID_ENG,
                        SYSTEM_ID_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (TELEPHONE_NUMBER,
                        TELEPHONE_NUMBER_ENG,
                        TELEPHONE_NUMBER_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (TITLE,
                        TITLE_ENG,
                        TITLE_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (USER_NAME,
                        USER_NAME_ENG,
                        USER_NAME_ENG_OBJECT);
        CommonUtils.WebUtils.addNorToEnglishNameMap
                (VARIANT_FORMAT,
                        VARIANT_FORMAT_ENG,
                        VARIANT_FORMAT_ENG_OBJECT);

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
