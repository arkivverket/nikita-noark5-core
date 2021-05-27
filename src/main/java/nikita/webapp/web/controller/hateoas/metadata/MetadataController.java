package nikita.webapp.web.controller.hateoas.metadata;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.CODE;
import static nikita.common.config.N5ResourceMappings.CODE_PARAMETER;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class MetadataController {

    private final IMetadataService metadataService;

    public MetadataController(IMetadataService metadataService) {
        this.metadataService = metadataService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new Metadata
    // POST [contextPath][api]/metadata/ny-*
    @Operation(summary = "Persists a new Metadata object",
            description = "Returns the newly created Metadata object after "
                    + "it is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Metadata " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = "*")
    public ResponseEntity<MetadataHateoas> createMetadata(
            @RequestBody Metadata metadata)
            throws NikitaException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        return ResponseEntity.status(CREATED)
                .body(metadataService.createNewMetadataEntity(metadata));
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all Metadata of a given type (entityName)
    // GET [contextPath][api]/metadata/entityName/
    @Operation(summary = "Retrieves all MetadataEntity")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "MetadataEntity codes found"),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = "No MetadataEntity found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = "**")
    public ResponseEntity<MetadataHateoas> findAll() {
        return ResponseEntity.status(OK).body(metadataService.findAll());
    }

    // Retrieves a given fondsStatus identified by a code
    // GET [contextPath][api]/metadata/Metadata/{kode}/
    @Operation(summary = "Gets fondsStatus identified by its code",
            description = "Returns the requested fondsStatus object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Metadata " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Metadata " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = "*" + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable(CODE) final String code) {
        return ResponseEntity.status(OK).body(
                metadataService.findMetadataByCodeOrThrow(code));
    }

    // Create a suggested fondsStatus(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-Metadata
    @Operation(summary = "Creates a suggested Metadata")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Metadata codes found"),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = "No MetadataEntity found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = NEW_ANYTHING)
    public ResponseEntity<MetadataHateoas> getMetadataTemplate() {
        return ResponseEntity.status(OK).body(
                metadataService.generateTemplateMetadata());
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a Metadata
    // PUT [contextPath][api]/metadata/Metadata/{kode}
    @Operation(summary = "Updates a Metadata object",
            description = "Returns the newly updated Metadata object after it" +
                    " is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Metadata " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = "**" + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> updateMetadata(
            @Parameter(name = CODE,
                    description = "code of metadata to update.",
                    required = true)
            @PathVariable(CODE) final String code,
            @RequestBody Metadata metadata) {
        return ResponseEntity.status(OK).body(
                metadataService.updateMetadataEntity(code, metadata));
    }

    // API - All DELETE Requests (CRUD - DELETE)
    // Delete a Metadata
    // DELETE [contextPath][api]/metadata/Metadata/{kode}
    @Operation(
            summary = "Deletes a single Metadata entity identified by kode")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Metadata entity deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = "**" + SLASH + CODE_PARAMETER)
    public ResponseEntity<String> deleteMetadata(
            @Parameter(name = CODE,
                    description = "Code of metadata object to delete.",
                    required = true)
            @PathVariable(CODE) final String code) {
        return ResponseEntity.status(NO_CONTENT).body(
                metadataService.deleteMetadataEntity(code));
    }
}
