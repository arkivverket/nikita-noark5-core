package nikita.webapp.web.controller;


import nikita.webapp.application.*;
import nikita.webapp.service.application.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.OK;

/**
 * REST controller that returns information about the Noark 5 cores
 * conformity to standards.
 */
@RestController
@RequestMapping(produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * identify the interfaces the core supports
     *
     * @return the application details along with the correct response code
     * (200 OK, or 500 Internal Error)
     */
    // API - All GET Requests (CRUD - READ)
        @GetMapping
    public ResponseEntity<ApplicationDetails> identify(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getApplicationDetails());
    }

    @GetMapping(value = HREF_OPENID_CONFIGURATION, produces = CONTENT_TYPE_JSON)
    public ResponseEntity<OIDCConfiguration> getOpenIdConfiguration(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getOpenIdConfiguration());
    }

    @GetMapping(value = HREF_SYSTEM_INFORMATION)
    public ResponseEntity<SystemInformation> getSystemInformation(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getSystemInformation());
    }

    @GetMapping(value = HREF_BASE_FONDS_STRUCTURE)
    public ResponseEntity<FondsStructureDetails> getFondsStructure(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getFondsStructureDetails());
    }

    @GetMapping(value = HREF_BASE_METADATA)
    public ResponseEntity<MetadataDetails> getMetadataPath(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getMetadataDetails());
    }

    @GetMapping(value = HREF_BASE_ADMIN)
    public ResponseEntity<AdministrationDetails> getAdminPath(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getAdministrationDetails());
    }

    @GetMapping(value = HREF_BASE_CASE_HANDLING)
    public ResponseEntity<CaseHandlingDetails> getCaseHandling(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getCaseHandlingDetails());
    }

    @GetMapping(value = HREF_BASE_LOGGING)
    public ResponseEntity<LoggingDetails> getLogging(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getLoggingDetails());
    }
}
