package nikita.webapp.web.controller;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.Api;
import nikita.webapp.application.*;
import nikita.webapp.service.application.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;

/**
 * REST controller that returns information about the Noark 5 cores conformity to standards.
 */
@RestController("ApplicationController")
@RequestMapping(produces = {NOARK5_V5_CONTENT_TYPE_JSON, NOARK5_V5_CONTENT_TYPE_JSON_XML})
@Api(value = "Application", description = "Links to where the various interfaces can be accessed from")
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    private ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * identify the interfaces the core supports
     *
     * @return the application details along with the correct response code (200 OK, or 500 Internal Error)
     */
    // API - All GET Requests (CRUD - READ)
    @Counted
    @RequestMapping(method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity <ApplicationDetails> identify(HttpServletRequest request) {

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getApplicationDetails(request));
    }

    @Counted
    @GetMapping(value = HREF_OPENID_CONFIGURATION)
    public ResponseEntity <OIDCConfiguration> getOpenIdConfiguration(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getOpenIdConfiguration());
    }

    @Counted
    @RequestMapping(value = HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<FondsStructureDetails> fondsStructure(HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getFondsStructureDetails());
    }

    @Counted
    @RequestMapping(value = HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<MetadataDetails> metadataPath(HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getMetadataDetails());
    }

    @Counted
    @RequestMapping(value = HATEOAS_API_PATH + SLASH + NOARK_ADMINISTRATION_PATH, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<AdministrationDetails> adminPath(HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getAdministrationDetails());
    }

    @Counted
    @RequestMapping(value = HATEOAS_API_PATH + SLASH + NOARK_CASE_HANDLING_PATH, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<CaseHandlingDetails> caseHandling(HttpServletRequest request) {
        CaseHandlingDetails c1 = applicationService.getCaseHandlingDetails();
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(c1);
    }
}
