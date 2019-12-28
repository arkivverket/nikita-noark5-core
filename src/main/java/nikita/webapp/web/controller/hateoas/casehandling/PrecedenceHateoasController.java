package nikita.webapp.web.controller.hateoas.casehandling;

import nikita.common.config.Constants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

//import nikita.webapp.service.interfaces.secondary.IPrecedenceService;

/**
 * Created by tsodring on 4/25/17.
 */

@RestController
@RequestMapping(value = HATEOAS_API_PATH + SLASH + NOARK_CASE_HANDLING_PATH + SLASH + CORRESPONDENCE_PART,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class PrecedenceHateoasController {
/*
    IPrecedenceHateoasHandler precedenceHateoasHandler;
    IPrecedenceService precedenceService;

    public PrecedenceHateoasController(IPrecedenceHateoasHandler precedenceHateoasHandler,
                                       IPrecedenceService precedenceService) {
        this.precedenceHateoasHandler = precedenceHateoasHandler;
        this.precedenceService = precedenceService;
    }

    // API - All GET Requests (CRUD - READ)
    @ApiOperation(value = "Retrieves a single Precedence entity given a systemId", response = Precedence.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Precedence returned", response = Precedence.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PrecedenceHateoas> findOnePrecedenceBySystemId(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemID of the precedence to retrieve",
                    required = true)
            @PathVariable("systemID") final String precedenceSystemId) {
        Precedence precedence = precedenceService.findBySystemId(precedenceSystemId);
        PrecedenceHateoas precedenceHateoas = new PrecedenceHateoas(precedence);
        precedenceHateoasHandler.addLinks(precedenceHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .eTag(precedence.getVersion().toString())
                .body(precedenceHateoas);
    }
*/
}
