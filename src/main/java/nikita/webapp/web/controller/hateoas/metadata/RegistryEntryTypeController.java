package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.RegistryEntryType;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IRegistryEntryTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 18/02/18.
 */

@RestController
@RequestMapping(value = HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class RegistryEntryTypeController {

    private IRegistryEntryTypeService registryEntryTypeService;

    public RegistryEntryTypeController(IRegistryEntryTypeService
                                               registryEntryTypeService) {
        this.registryEntryTypeService = registryEntryTypeService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new registryEntryType
    // POST [contextPath][api]/metadata/journalposttype/ny-journalposttype
    @ApiOperation(
            value = "Persists a new RegistryEntryType object",
            notes = "Returns the newly created RegistryEntryType object after" +
                    " it is persisted to the database",
            response = RegistryEntryType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryType " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RegistryEntryType.class),
            @ApiResponse(
                    code = 201,
                    message = "RegistryEntryType " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RegistryEntryType.class),
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
    @PostMapping(value = REGISTRY_ENTRY_TYPE + SLASH + NEW_REGISTRY_ENTRY_TYPE)
    public ResponseEntity<MetadataHateoas>
    createRegistryEntryType(
            HttpServletRequest request,
            @RequestBody RegistryEntryType
                    registryEntryType)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                registryEntryTypeService.
                        createNewRegistryEntryType(
                                registryEntryType);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all RegistryEntryType
    // GET [contextPath][api]/metadata/journalposttype/
    @ApiOperation(
            value = "Retrieves all RegistryEntryType ",
            response = RegistryEntryType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryType codes found",
                    response = RegistryEntryType.class),
            @ApiResponse(
                    code = 404,
                    message = "No RegistryEntryType found"),
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
    @GetMapping(value = REGISTRY_ENTRY_TYPE)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryTypeService.findAll());
    }

    // Retrieves a given RegistryEntryType identified by a
    // kode
    // GET [contextPath][api]/metadata/journalposttype/
    // {kode}/
    @ApiOperation(
            value = "Gets RegistryEntryType identified by its kode",
            notes = "Returns the requested RegistryEntryType object",
            response = RegistryEntryType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryType " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RegistryEntryType.class),
            @ApiResponse(
                    code = 201,
                    message = "RegistryEntryType " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RegistryEntryType.class),
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
    @GetMapping(value = REGISTRY_ENTRY_TYPE + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByKode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
                registryEntryTypeService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested RegistryEntryType(like a template) with
    // default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-registryEntryType
    @ApiOperation(
            value = "Creates a suggested RegistryEntryType",
            response = RegistryEntryType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryType codes found",
                    response = RegistryEntryType.class),
            @ApiResponse(
                    code = 404,
                    message = "No RegistryEntryType found"),
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
    @GetMapping(value = NEW_REGISTRY_ENTRY_TYPE)
    public ResponseEntity<MetadataHateoas>
    generateDefaultRegistryEntryType(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (registryEntryTypeService
                        .generateDefaultRegistryEntryType());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a registryEntryType
    // PUT [contextPath][api]/metadata/journalposttype/
    @ApiOperation(
            value = "Updates a RegistryEntryType object",
            notes = "Returns the newly updated RegistryEntryType object after" +
                    " it is persisted to the database",
            response = RegistryEntryType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "RegistryEntryType " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RegistryEntryType.class),
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
    @PutMapping(value = REGISTRY_ENTRY_TYPE + SLASH + REGISTRY_ENTRY_TYPE)
    public ResponseEntity<MetadataHateoas>
    updateRegistryEntryType(
            @ApiParam(name = "kode",
                    value = "kode of registryEntryType to update.",
                    required = true)
            @PathVariable("kode") String code,
            @RequestBody RegistryEntryType registryEntryType,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
                registryEntryTypeService
                        .handleUpdate
                                (code, CommonUtils.Validation.
                                        parseETAG(request.getHeader
                                                (ETAG)), registryEntryType);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
