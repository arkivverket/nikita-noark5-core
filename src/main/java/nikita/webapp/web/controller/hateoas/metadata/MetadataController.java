package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CODE;
import static nikita.common.config.N5ResourceMappings.CODE_PARAMETER;

@RestController
@RequestMapping(value = HREF_BASE_METADATA  + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class MetadataController {

    private IMetadataService metadataService;

    public MetadataController(IMetadataService metadataService) {
        this.metadataService = metadataService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new Metadata
    // POST [contextPath][api]/metadata/ny-*
    @ApiOperation(value = "Persists a new Metadata object",
            notes = "Returns the newly created Metadata object after "
                    + "it is persisted to the database",
            response = Metadata.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201,
                    message = "Metadata " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = Metadata.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 400,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = "*")
    public ResponseEntity<MetadataHateoas> createMetadata(
            @RequestBody Metadata metadata)
            throws NikitaException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        return metadataService.createNewMetadataEntity(metadata);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all Metadata of a given type (entityname)
    // GET [contextPath][api]/metadata/entityname/
    @ApiOperation(value = "Retrieves all MetadataEntity",
            response = Metadata.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "MetadataEntity codes found",
                    response = Metadata.class),
            @ApiResponse(code = 404,
                    message = "No MetadataEntity found"),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = "**")
    public ResponseEntity<MetadataHateoas> findAll() {
        return metadataService.findAll();
    }

    // Retrieves a given fondsStatus identified by a code
    // GET [contextPath][api]/metadata/Metadata/{kode}/
    @ApiOperation(value = "Gets fondsStatus identified by its code", notes = "Returns the requested " +
            " fondsStatus object", response = Metadata.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Metadata " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = Metadata.class),
            @ApiResponse(code = 201, message = "Metadata " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = Metadata.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = "*" + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable(CODE) final String code) {
        return metadataService.findMetadataByCodeOrThrow(code);
    }

    // Create a suggested fondsStatus(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-Metadata
    @ApiOperation(value = "Creates a suggested Metadata", response = Metadata.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Metadata codes found",
                    response = Metadata.class),
            @ApiResponse(code = 404, message = "No MetadataEntity found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = NEW_ANYTHING)
    public ResponseEntity<String>
    getMetadataTemplate() {
        return metadataService.generateTemplateMetadata();
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a Metadata
    // PUT [contextPath][api]/metadata/Metadata/{kode}
    @ApiOperation(value = "Updates a Metadata object", notes = "Returns the newly" +
            " updated Metadata object after it is persisted to the database", response = Metadata.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Metadata " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = Metadata.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = "**" + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> updateMetadata(
            @ApiParam(name = CODE,
                    value = "code of metadata to update.",
                    required = true)
            @PathVariable(CODE) final String code,
            @RequestBody Metadata metadata) {
        return metadataService.updateMetadataEntity(code, metadata);
    }

    // API - All DELETE Requests (CRUD - DELETE)
    // Delete a Metadata
    // DELETE [contextPath][api]/metadata/Metadata/{kode}
    @ApiOperation(
            value = "Deletes a single Metadata entity identified by kode",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Metadata entity deleted",
                    response = String.class),
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
    @DeleteMapping(value = "**" + SLASH + CODE_PARAMETER)
    public ResponseEntity<String> deleteMetadata(
            @ApiParam(name = CODE,
                    value = "Code of metadata object to delete.",
                    required = true)
            @PathVariable(CODE) final String code) {
        return metadataService.deleteMetadataEntity(code);
    }
}
