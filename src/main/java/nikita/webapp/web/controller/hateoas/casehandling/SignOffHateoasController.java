package nikita.webapp.web.controller.hateoas.casehandling;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.webapp.service.interfaces.casehandling.ISignOffService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@RestController
@RequestMapping(value = HREF_BASE_CASE_HANDLING + SLASH + SIGN_OFF,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class SignOffHateoasController
        extends NoarkController {

    private final ISignOffService signOffService;

    public SignOffHateoasController(ISignOffService signOffService) {
        this.signOffService = signOffService;
    }

    // API - All GET Requests (CRUD - READ)
    // Get a SignOff identified by systemID
    // GET [contextPath][api]/sakarkiv/avskrivning/{systemId}
    @ApiOperation(value = "Retrieves a single SignOff entity given a systemId",
            response = SignOff.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SignOff returned",
                    response = SignOff.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<SignOffHateoas> findSignOffBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the signOff to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID signOffSystemId) {
        return signOffService.findBySystemId(signOffSystemId);
    }

    // API - All GET Requests (CRUD - UPDATE)
    // Update a SignOff identified by systemID
    // GET [contextPath][api]/sakarkiv/avskrivning/{systemId}
    @ApiOperation(value = "Update a single SignOff entity given a systemId",
            response = SignOff.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SignOff returned",
                    response = SignOff.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<SignOffHateoas> updateSignOff(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the signOff to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID signOffSystemId,
            @ApiParam(name = "signOff",
                    value = "Incoming signOff object",
                    required = true)
            @RequestBody SignOff signOff) {
        return signOffService.updateSignOff(signOffSystemId, signOff);
    }
}
