package nikita.webapp.web.controller.hateoas.secondary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.secondary.IPrecedenceService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_CASE_HANDLING + SLASH + PRECEDENCE,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class PrecedenceHateoasController
        extends NoarkController {

    private final IPrecedenceService precedenceService;

    public PrecedenceHateoasController(
            IPrecedenceService precedenceService) {
        this.precedenceService = precedenceService;
    }

    // API - All GET Requests (CRUD - READ)
    // GET [contextPath][api]/sakarkiv/presedens/
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/presedens/
    @Operation(summary = "Retrieves multiple Precedence entities limited " +
            "by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping()
    public ResponseEntity<PrecedenceHateoas> findAllPrecedence() {
        PrecedenceHateoas precedenceHateoas =
                precedenceService.findAll();
        return ResponseEntity.status(OK)
                .body(precedenceHateoas);
    }

    // GET [contextPath][api]/sakarkiv/presedens/{systemId}
    @Operation(summary = "Retrieves a single Precedence entity given a " +
            "systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence returned"),
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
    public ResponseEntity<PrecedenceHateoas> findPrecedenceBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Precedence to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemId) {
        PrecedenceHateoas precedenceHateoas = precedenceService
                .findBySystemId(systemId);
        return ResponseEntity.status(OK)
                .body(precedenceHateoas);
    }

    // PUT [contextPath][api]/sakarkiv/presedens/{systemId}
    @Operation(summary = "Updates a Precedence identified by a given systemId",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Precedence " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Precedence"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PrecedenceHateoas> updatePrecedenceBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of Precedence to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Precedence",
                    description = "Incoming Precedence object",
                    required = true)
            @RequestBody Precedence precedence) throws NikitaException {
        PrecedenceHateoas precedenceHateoas =
                precedenceService.updatePrecedenceBySystemId(systemID,
                        parseETAG(request.getHeader(ETAG)), precedence);
        return ResponseEntity.status(OK)
                .body(precedenceHateoas);
    }

    // DELETE [contextPath][api]/sakarkiv/presedens/{systemID}/
    @Operation(summary = "Deletes a single Precedence entity identified by " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence deleted"),
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
    public ResponseEntity<String> deletePrecedenceBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the precedence to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        precedenceService.deletePrecedenceBySystemId(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
