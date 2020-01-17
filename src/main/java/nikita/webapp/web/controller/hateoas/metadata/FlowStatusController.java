package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.FlowStatus;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IFlowStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Validation.parseETAG;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 15/02/18.
 */

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class FlowStatusController {

    private IFlowStatusService flowStatusService;

    public FlowStatusController(IFlowStatusService flowStatusService) {
        this.flowStatusService = flowStatusService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new flowStatus
    // POST [contextPath][api]/metadata/ny-flytstatus
    @ApiOperation(
            value = "Persists a new FlowStatus object",
            notes = "Returns the newly created FlowStatus object after it is " +
                    "persisted to the database",
            response = FlowStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "FlowStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FlowStatus.class),
            @ApiResponse(
                    code = 201,
                    message = "FlowStatus " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FlowStatus.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = NEW_FLOW_STATUS)
    public ResponseEntity<MetadataHateoas>
    createFlowStatus(
            HttpServletRequest request,
            @RequestBody FlowStatus
                    flowStatus)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                flowStatusService.
                        createNewFlowStatus(
                                flowStatus);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all FlowStatus
    // GET [contextPath][api]/metadata/flytstatus/
    @ApiOperation(
            value = "Retrieves all FlowStatus ",
            response = FlowStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "FlowStatus codes found",
                    response = FlowStatus.class),
            @ApiResponse(
                    code = 404,
                    message = "No FlowStatus found"),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = FLOW_STATUS)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(flowStatusService.findAll());
    }

    // Retrieves a given FlowStatus identified by a
    // code
    // GET [contextPath][api]/metadata/flytstatus/
    // {kode}/
    @ApiOperation(
            value = "Gets FlowStatus identified by its code",
            notes = "Returns the requested FlowStatus object",
            response = FlowStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "FlowStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FlowStatus.class),
            @ApiResponse(
                    code = 201,
                    message = "FlowStatus " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FlowStatus.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = FLOW_STATUS + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
                flowStatusService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested FlowStatus(like a template) with
    // default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-flowStatus
    @ApiOperation(
            value = "Creates a suggested FlowStatus",
            response = FlowStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "FlowStatus codes found",
                    response = FlowStatus.class),
            @ApiResponse(
                    code = 404,
                    message = "No FlowStatus found"),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = NEW_FLOW_STATUS)
    public ResponseEntity<MetadataHateoas>
    generateDefaultFlowStatus(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (flowStatusService
                        .generateDefaultFlowStatus());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a flowStatus
    // PUT [contextPath][api]/metadata/flytstatus/{kode}
    @ApiOperation(
            value = "Updates a FlowStatus object",
            notes = "Returns the newly updated FlowStatus object after it is " +
                    "persisted to the database",
            response = FlowStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "FlowStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FlowStatus.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = FLOW_STATUS + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas>
    updateFlowStatus(
            @ApiParam(name = CODE,
                    value = "code of fonds to update.",
                    required = true)
            @PathVariable(CODE) String code,
            @RequestBody FlowStatus flowStatus,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = flowStatusService.handleUpdate
            (code, parseETAG(request.getHeader(ETAG)), flowStatus);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
