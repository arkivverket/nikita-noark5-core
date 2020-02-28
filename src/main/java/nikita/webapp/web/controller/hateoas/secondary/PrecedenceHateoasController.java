package nikita.webapp.web.controller.hateoas.secondary;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.secondary.IPrecedenceHateoasHandler;
import nikita.webapp.service.interfaces.secondary.IPrecedenceService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 4/25/17.
 */
@RestController
@RequestMapping(value = HREF_BASE_CASE_HANDLING + SLASH + PRECEDENCE,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class PrecedenceHateoasController
        extends NoarkController {

    private IPrecedenceHateoasHandler precedenceHateoasHandler;
    private IPrecedenceService precedenceService;

    public PrecedenceHateoasController
        (IPrecedenceHateoasHandler precedenceHateoasHandler,
         IPrecedenceService precedenceService) {
        this.precedenceHateoasHandler = precedenceHateoasHandler;
        this.precedenceService = precedenceService;
    }

    // API - All GET Requests (CRUD - READ)

    // GET [contextPath][api]/sakarkiv/presedens/
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/presedens/
    @ApiOperation(value = "Retrieves multiple Precedence entities limited " +
                  "by ownership rights",
                  response = PrecedenceHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Precedence found",
                    response = PrecedenceHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping()
    public ResponseEntity<PrecedenceHateoas> findAllPrecedence() {
        PrecedenceHateoas precedenceHateoas =
            precedenceService.findAllByOwner();
        return ResponseEntity.status(HttpStatus.OK)
                .body(precedenceHateoas);
    }

    // GET [contextPath][api]/sakarkiv/presedens/{systemId}
    @ApiOperation(value = "Retrieves a single Precedence entity given a systemId",
            response = PrecedenceHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Precedence returned",
                    response = Precedence.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PrecedenceHateoas> findPrecedenceBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the Precedence to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        PrecedenceHateoas precedenceHateoas = precedenceService.findBySystemId(systemId);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(precedenceHateoas);
    }

    // PUT [contextPath][api]/sakarkiv/presedens/{systemId}
    @ApiOperation(value = "Updates a Precedence identified by a given systemId",
            notes = "Returns the newly updated nationalIdentifierPerson",
            response = PrecedenceHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Precedence " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PrecedenceHateoas.class),
            @ApiResponse(code = 201, message = "Precedence " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PrecedenceHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Precedence"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PrecedenceHateoas> updatePrecedenceBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Precedence to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Precedence",
                    value = "Incoming Precedence object",
                    required = true)
            @RequestBody Precedence precedence) throws NikitaException {
        PrecedenceHateoas precedenceHateoas =
            precedenceService.updatePrecedenceBySystemId
            (systemID, parseETAG(request.getHeader(ETAG)), precedence);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(precedenceHateoas);
    }

    // DELETE [contextPath][api]/sakarkiv/presedens/{systemID}/
    @ApiOperation(value = "Deletes a single Precedence entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Precedence deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deletePrecedenceBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the precedence to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        precedenceService.deletePrecedenceBySystemId(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
