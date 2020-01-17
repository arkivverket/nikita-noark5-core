package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.RegistryEntryStatus;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IRegistryEntryStatusService;
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
 * Created by tsodring on 12/02/18.
 */

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class RegistryEntryStatusController {

    private IRegistryEntryStatusService registryEntryStatusService;

    public RegistryEntryStatusController(
            IRegistryEntryStatusService registryEntryStatusService) {
        this.registryEntryStatusService = registryEntryStatusService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new journalpoststatus
    // POST [contextPath][api]/metadata/ny-journalpoststatus
    @ApiOperation(
            value = "Persists a new RegistryEntryStatus object",
            notes = "Returns the newly created RegistryEntryStatus object  " +
                    "after it is persisted to the database",
            response = RegistryEntryStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RegistryEntryStatus.class),
            @ApiResponse(
                    code = 201,
                    message = "RegistryEntryStatus " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RegistryEntryStatus.class),
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
    @PostMapping(value = NEW_REGISTRY_ENTRY_STATUS)
    public ResponseEntity<MetadataHateoas> createRegistryEntryStatus(
            HttpServletRequest request,
            @RequestBody RegistryEntryStatus registryEntryStatus)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                registryEntryStatusService.createNewRegistryEntryStatus(
                        registryEntryStatus);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all RegistryEntryStatus
    // GET [contextPath][api]/metadata/journalpoststatus/
    @ApiOperation(
            value = "Retrieves all RegistryEntryStatus ",
            response = RegistryEntryStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryStatus codes found",
                    response = RegistryEntryStatus.class),
            @ApiResponse(
                    code = 404,
                    message = "No RegistryEntryStatus found"),
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
    @GetMapping(value = REGISTRY_ENTRY_STATUS)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryStatusService.findAll());
    }

    // Retrieves a given RegistryEntryStatus identified by a code
    // GET [contextPath][api]/metadata/journalpoststatus/{code}/
    @ApiOperation(
            value = "Gets RegistryEntryStatus identified by its code",
            notes = "Returns the requested RegistryEntryStatus object",
            response = RegistryEntryStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RegistryEntryStatus.class),
            @ApiResponse(
                    code = 201,
                    message = "RegistryEntryStatus " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RegistryEntryStatus.class),
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
    @GetMapping(value = REGISTRY_ENTRY_STATUS + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = registryEntryStatusService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested RegistryEntryStatus(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-journalpoststatus
    @ApiOperation(
            value = "Creates a suggested RegistryEntryStatus",
            response = RegistryEntryStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryStatus codes found",
                    response = RegistryEntryStatus.class),
            @ApiResponse(
                    code = 404,
                    message = "No RegistryEntryStatus found"),
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
    @GetMapping(value = NEW_REGISTRY_ENTRY_STATUS)
    public ResponseEntity<MetadataHateoas>
    generateDefaultRegistryEntryStatus(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (registryEntryStatusService
                        .generateDefaultRegistryEntryStatus());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a journalpoststatus
    // PUT [contextPath][api]/metadata/journalpoststatus/{code}
    @ApiOperation(
            value = "Updates a RegistryEntryStatus object",
            notes = "Returns the newly updated RegistryEntryStatus object after it " +
                    "is persisted to the database",
            response = RegistryEntryStatus.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RegistryEntryStatus.class),
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
    @PutMapping(value = REGISTRY_ENTRY_STATUS + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> updateRegistryEntryStatus(
            @ApiParam(name = CODE,
                    value = "code of registry entry status to update.",
                    required = true)
            @PathVariable(CODE) String code,
            @RequestBody RegistryEntryStatus RegistryEntryStatus,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
            registryEntryStatusService.handleUpdate
                (code, parseETAG(request.getHeader(ETAG)), RegistryEntryStatus);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
