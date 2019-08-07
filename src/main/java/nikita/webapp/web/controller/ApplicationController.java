package nikita.webapp.web.controller;

import com.codahale.metrics.annotation.Counted;
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
@RestController("ApplicationController")
@RequestMapping(produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class ApplicationController {

    private ApplicationService applicationService;

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
    @Counted
    @GetMapping
    public ResponseEntity<ApplicationDetails> identify(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getApplicationDetails());
    }

    @Counted
    @GetMapping(value = HREF_OPENID_CONFIGURATION)
    public ResponseEntity<OIDCConfiguration> getOpenIdConfiguration(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getOpenIdConfiguration());
    }

    @Counted
    @GetMapping(value = HREF_BASE_FONDS_STRUCTURE)
    public ResponseEntity<FondsStructureDetails> fondsStructure(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getFondsStructureDetails());
    }

    @Counted
    @GetMapping(value = HREF_BASE_METADATA)
    public ResponseEntity<MetadataDetails> metadataPath(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getMetadataDetails());
    }

    @Counted
    @GetMapping(value = HREF_BASE_METADATA)
    public ResponseEntity<AdministrationDetails> adminPath(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getAdministrationDetails());
    }

    @Counted
    @GetMapping(value = HREF_BASE_CASE_HANDLING)
    public ResponseEntity<CaseHandlingDetails> caseHandling(
            HttpServletRequest request) {
        CaseHandlingDetails c1 = applicationService.getCaseHandlingDetails();
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(c1);
    }
}
