package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.AssociatedWithRecordAs;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IAssociatedWithRecordAsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Validation.parseETAG;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class AssociatedWithRecordAsController {

    private IAssociatedWithRecordAsService associatedWithRecordAsService;

    public AssociatedWithRecordAsController(IAssociatedWithRecordAsService associatedWithRecordAsService) {
        this.associatedWithRecordAsService = associatedWithRecordAsService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new land
    // POST [contextPath][api]/metadata/ny-land
    @ApiOperation(
            value = "Persists a new AssociatedWithRecordAs object",
            notes = "Returns the newly created AssociatedWithRecordAs object after it " +
                    "is persisted to the database",
            response = AssociatedWithRecordAs.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "AssociatedWithRecordAs " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = AssociatedWithRecordAs.class),
            @ApiResponse(
                    code = 201,
                    message = "AssociatedWithRecordAs " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = AssociatedWithRecordAs.class),
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
    @PostMapping(value = NEW_ASSOCIATED_WITH_RECORD_AS)
    public ResponseEntity<MetadataHateoas> createAssociatedWithRecordAs(
            HttpServletRequest request,
            @RequestBody AssociatedWithRecordAs associatedWithRecordAs)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                associatedWithRecordAsService.createNewAssociatedWithRecordAs(associatedWithRecordAs);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all associatedWithRecordAs
    // GET [contextPath][api]/metadata/land/
    @ApiOperation(
            value = "Retrieves all AssociatedWithRecordAs ",
            response = AssociatedWithRecordAs.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "AssociatedWithRecordAs codes found",
                    response = AssociatedWithRecordAs.class),
            @ApiResponse(
                    code = 404,
                    message = "No AssociatedWithRecordAs found"),
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
    @GetMapping(value = ASSOCIATED_WITH_RECORD_AS)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(associatedWithRecordAsService.findAll());
    }

    // Retrieves a given associatedWithRecordAs identified by a code
    // GET [contextPath][api]/metadata/land/{code}/
    @ApiOperation(
            value = "Gets associatedWithRecordAs identified by its code",
            notes = "Returns the requested associatedWithRecordAs object",
            response = AssociatedWithRecordAs.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "AssociatedWithRecordAs " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = AssociatedWithRecordAs.class),
            @ApiResponse(
                    code = 201,
                    message = "AssociatedWithRecordAs " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = AssociatedWithRecordAs.class),
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
    @GetMapping(value = ASSOCIATED_WITH_RECORD_AS + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = associatedWithRecordAsService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested associatedWithRecordAs(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-land
    @ApiOperation(
            value = "Creates a suggested AssociatedWithRecordAs",
            response = AssociatedWithRecordAs.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "AssociatedWithRecordAs codes found",
                    response = AssociatedWithRecordAs.class),
            @ApiResponse(
                    code = 404,
                    message = "No AssociatedWithRecordAs found"),
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
    @GetMapping(value = NEW_ASSOCIATED_WITH_RECORD_AS)
    public ResponseEntity<MetadataHateoas>
    generateDefaultAssociatedWithRecordAs(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (associatedWithRecordAsService.generateDefaultAssociatedWithRecordAs());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a land
    // PUT [contextPath][api]/metadata/land/{code}
    @ApiOperation(
            value = "Updates a AssociatedWithRecordAs object",
            notes = "Returns the newly updated AssociatedWithRecordAs object after it " +
                    "is persisted to the database",
            response = AssociatedWithRecordAs.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "AssociatedWithRecordAs " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = AssociatedWithRecordAs.class),
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
    @PutMapping(value = ASSOCIATED_WITH_RECORD_AS + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> updateAssociatedWithRecordAs(
            @ApiParam(name = CODE,
                    value = "code of record to update.",
                    required = true)
            @PathVariable(CODE) String code,
            @RequestBody AssociatedWithRecordAs associatedWithRecordAs,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = associatedWithRecordAsService.handleUpdate
                (code, parseETAG(request.getHeader(ETAG)), associatedWithRecordAs);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
