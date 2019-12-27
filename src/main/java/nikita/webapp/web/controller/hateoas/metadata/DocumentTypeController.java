package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.DocumentType;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IDocumentTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 31/1/18.
 */

@RestController
@RequestMapping(value = HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class DocumentTypeController {

    private IDocumentTypeService documentTypeService;

    public DocumentTypeController(IDocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new dokumenttype
    // POST [contextPath][api]/metadata/dokumenttype/ny-dokumenttype
    @ApiOperation(
            value = "Persists a new DocumentType object",
            notes = "Returns the newly created DocumentType object after it " +
                    "is persisted to the database",
            response = DocumentType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "DocumentType " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentType.class),
            @ApiResponse(
                    code = 201,
                    message = "DocumentType " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentType.class),
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
    @PostMapping(value = DOCUMENT_TYPE + SLASH + NEW_DOCUMENT_TYPE)
    public ResponseEntity<MetadataHateoas> createDocumentType(
            HttpServletRequest request,
            @RequestBody DocumentType documentType)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                documentTypeService.createNewDocumentType(documentType);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all documentType
    // GET [contextPath][api]/metadata/dokumenttype/
    @ApiOperation(
            value = "Retrieves all DocumentType ",
            response = DocumentType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "DocumentType codes found",
                    response = DocumentType.class),
            @ApiResponse(
                    code = 404,
                    message = "No DocumentType found"),
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
    @GetMapping(value = DOCUMENT_TYPE)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentTypeService.findAll());
    }

    // Retrieves a given documentType identified by a code
    // GET [contextPath][api]/metadata/dokumenttype/{code}/
    @ApiOperation(
            value = "Gets documentType identified by its code",
            notes = "Returns the requested documentType object",
            response = DocumentType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "DocumentType " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentType.class),
            @ApiResponse(
                    code = 201,
                    message = "DocumentType " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentType.class),
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
    @GetMapping(value = DOCUMENT_TYPE + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = documentTypeService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested documentType(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-dokumenttype
    @ApiOperation(
            value = "Creates a suggested DocumentType",
            response = DocumentType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "DocumentType codes found",
                    response = DocumentType.class),
            @ApiResponse(
                    code = 404,
                    message = "No DocumentType found"),
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
    @GetMapping(value = NEW_DOCUMENT_TYPE)
    public ResponseEntity<MetadataHateoas>
    generateDefaultDocumentType(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (documentTypeService.generateDefaultDocumentType());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a dokumenttype
    // PUT [contextPath][api]/metatdata/dokumenttype/
    @ApiOperation(
            value = "Updates a DocumentType object",
            notes = "Returns the newly updated DocumentType object after it " +
                    "is persisted to the database",
            response = DocumentType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "DocumentType " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentType.class),
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
    @PutMapping(value = DOCUMENT_TYPE + SLASH + DOCUMENT_TYPE)
    public ResponseEntity<MetadataHateoas> updateDocumentType(
            @ApiParam(name = "systemID",
                    value = "code of fonds to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @RequestBody DocumentType documentType,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = documentTypeService.handleUpdate
                (systemID,
                        CommonUtils.Validation.parseETAG(request.getHeader(ETAG)),
                        documentType);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
