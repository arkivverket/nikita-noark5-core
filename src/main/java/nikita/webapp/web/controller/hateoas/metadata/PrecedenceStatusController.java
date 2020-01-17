package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.PrecedenceStatus;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IPrecedenceStatusService;
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
 * Created by tsodring on 19/02/18.
 */

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class PrecedenceStatusController {

    private IPrecedenceStatusService PrecedenceStatusService;

    public PrecedenceStatusController(IPrecedenceStatusService
                                              PrecedenceStatusService) {
        this.PrecedenceStatusService = PrecedenceStatusService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new PrecedenceStatus
    // POST [contextPath][api]/metadata/ny-journalposttype
    @ApiOperation(
            value = "Persists a new PrecedenceStatus object",
            notes = "Returns the newly created PrecedenceStatus object after" +
                    " it is persisted to the database",
            response = PrecedenceStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PrecedenceStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PrecedenceStatus.class),
            @ApiResponse(
                    code = 201,
                    message = "PrecedenceStatus " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PrecedenceStatus.class),
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
    @PostMapping(value = NEW_PRECEDENCE_STATUS)
    public ResponseEntity<MetadataHateoas>
    createPrecedenceStatus(
            HttpServletRequest request,
            @RequestBody PrecedenceStatus
                    PrecedenceStatus)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                PrecedenceStatusService.
                        createNewPrecedenceStatus(
                                PrecedenceStatus);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all PrecedenceStatus
    // GET [contextPath][api]/metadata/journalposttype/
    @ApiOperation(
            value = "Retrieves all PrecedenceStatus ",
            response = PrecedenceStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PrecedenceStatus codes found",
                    response = PrecedenceStatus.class),
            @ApiResponse(
                    code = 404,
                    message = "No PrecedenceStatus found"),
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
    @GetMapping(value = PRECEDENCE_STATUS)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(PrecedenceStatusService.findAll());
    }

    // Retrieves a given PrecedenceStatus identified by a
    // code
    // GET [contextPath][api]/metadata/journalposttype/
    // {code}/
    @ApiOperation(
            value = "Gets PrecedenceStatus identified by its code",
            notes = "Returns the requested PrecedenceStatus object",
            response = PrecedenceStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PrecedenceStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PrecedenceStatus.class),
            @ApiResponse(
                    code = 201,
                    message = "PrecedenceStatus " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PrecedenceStatus.class),
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
    @GetMapping(value = PRECEDENCE_STATUS + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
                PrecedenceStatusService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested PrecedenceStatus(like a template) with
    // default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-PrecedenceStatus
    @ApiOperation(
            value = "Creates a suggested PrecedenceStatus",
            response = PrecedenceStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PrecedenceStatus codes found",
                    response = PrecedenceStatus.class),
            @ApiResponse(
                    code = 404,
                    message = "No PrecedenceStatus found"),
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
    @GetMapping(value = NEW_PRECEDENCE_STATUS)
    public ResponseEntity<MetadataHateoas>
    generateDefaultPrecedenceStatus(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (PrecedenceStatusService
                        .generateDefaultPrecedenceStatus());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a PrecedenceStatus
    // PUT [contextPath][api]/metadata/presedensstatus/{code}
    @ApiOperation(
            value = "Updates a PrecedenceStatus object",
            notes = "Returns the newly updated PrecedenceStatus object after" +
                    " it is persisted to the database",
            response = PrecedenceStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PrecedenceStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PrecedenceStatus.class),
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
    @PutMapping(value = PRECEDENCE_STATUS + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas>
    updatePrecedenceStatus(
            @ApiParam(name = CODE,
                    value = "code of PrecedenceStatus to update.",
                    required = true)
            @PathVariable(CODE) String code,
            @RequestBody PrecedenceStatus PrecedenceStatus,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = PrecedenceStatusService.handleUpdate
            (code, parseETAG(request.getHeader(ETAG)), PrecedenceStatus);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
