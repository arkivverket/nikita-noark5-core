package nikita.webapp.web.controller.hateoas.casehandling;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.webapp.service.interfaces.casehandling.ISignOffService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

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
    @Operation(summary = "Retrieves a single SignOff entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "SignOff returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<SignOffHateoas> findSignOffBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the signOff to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID signOffSystemId) {
        return ResponseEntity.status(OK).body(
                signOffService.findBySystemId(signOffSystemId));
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a SignOff identified by systemID
    // PUT [contextPath][api]/sakarkiv/avskrivning/{systemId}
    @Operation(summary = "Update a single SignOff entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "SignOff returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<SignOffHateoas> updateSignOff(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the signOff to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID signOffSystemId,
            @Parameter(name = "signOff",
                    description = "Incoming signOff object",
                    required = true)
            @RequestBody SignOff signOff) {
        return ResponseEntity.status(OK).body(
                signOffService.updateSignOff(signOffSystemId, signOff));
    }

    // API - All DELETE Requests (CRUD - DELETE)
    // Delete a SignOff identified by systemID
    // DELETE [contextPath][api]/sakarkiv/avskrivning/{systemId}
    @Operation(summary = "Delete a single SignOff entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "SignOff deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteSignOff(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the signOff to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID signOffSystemId) {
        signOffService.deleteSignOff(signOffSystemId);
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
