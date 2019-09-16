package nikita.webapp.run;

import nikita.webapp.util.DemoData;
import nikita.webapp.util.InternalNameTranslator;
import nikita.webapp.util.MetadataInsert;
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
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.FileConstants.*;
import static nikita.common.util.CommonUtils.FileUtils.addProductionToArchiveVersion;
import static nikita.common.util.CommonUtils.FileUtils.setDefaultMimeTypesAsConvertible;
import static nikita.common.util.CommonUtils.WebUtils.addRequestToMethodMap;
import static org.springframework.http.HttpMethod.*;

/**
 * Create som basic data if application is in demo mode
 */
@Component
public class AfterApplicationStartup {

    private static final Logger logger =
            LoggerFactory.getLogger(AfterApplicationStartup.class);
    private final RequestMappingHandlerMapping handlerMapping;

    private ApplicationContext applicationContext;
    private InternalNameTranslator internalNameTranslator;
    private DemoData demoData;
    private MetadataInsert metadataInsert;


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
                                   DemoData demoData,
                                   MetadataInsert metadataInsert,
                                   ApplicationContext applicationContext,
                                   InternalNameTranslator internalNameTranslator) {
        this.handlerMapping = handlerMapping;
        this.demoData = demoData;
        this.metadataInsert = metadataInsert;
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

        metadataInsert.populateMetadataEntities();
        if (createDirectoryStore) {
            createDirectoryStoreIfNotExists();
        }

        if (createUsers) {
            demoData.addAdminUnit();
            demoData.addAuthorities();
            demoData.addUserAdmin();
            demoData.addUserRecordKeeper();
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
                                httpMethods.add(GET);
                            } else if (requestMethod.equals(RequestMethod.DELETE)) {
                                httpMethods.add(DELETE);
                            } else if (requestMethod.equals(RequestMethod.OPTIONS)) {
                                httpMethods.add(OPTIONS);
                            } else if (requestMethod.equals(RequestMethod.HEAD)) {
                                httpMethods.add(HEAD);
                            } else if (requestMethod.equals(RequestMethod.PATCH)) {
                                httpMethods.add(PATCH);
                            } else if (requestMethod.equals(RequestMethod.POST)) {
                                httpMethods.add(POST);
                            } else if (requestMethod.equals(RequestMethod.PUT)) {
                                httpMethods.add(PUT);
                            } else if (requestMethod.equals(RequestMethod
                                    .TRACE)) {
                                httpMethods.add(TRACE);
                            }
                        }
                        out.println("Adding " + servletPath + " methods " + httpMethods);
                        addRequestToMethodMap(servletPath, httpMethods);
                    } else {
                        logger.warn("Missing HTTP Methods for the following servletPath [" + servletPath + "]");
                    }
                }
            }
        }

        // This has to be a temporary addition. This approach to cors is silly!!
        // Cors should just work, we should not have to do this work!!
        Set<HttpMethod> httpMethods = new TreeSet<>();
        httpMethods.add(GET);
        httpMethods.add(OPTIONS);
        httpMethods.add(POST);
        String path = "/oauth/token/";
        logger.info("Adding " + path + " methods " + httpMethods);
        addRequestToMethodMap(path, httpMethods);
        path = "/oauth/check_token/";
        logger.info("Adding " + path + " methods " + httpMethods);
        addRequestToMethodMap(path, httpMethods);
    }
}
