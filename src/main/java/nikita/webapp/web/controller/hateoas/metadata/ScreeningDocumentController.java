package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ScreeningDocument;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IScreeningDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 16/03/18.
 */

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class ScreeningDocumentController {

    private IScreeningDocumentService postalCodeService;

    public ScreeningDocumentController(IScreeningDocumentService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new skjermingdokument
    // POST [contextPath][api]/metadata/skjermingdokument/ny-skjermingdokument
    @ApiOperation(
            value = "Persists a new ScreeningDocument object",
            notes = "Returns the newly created ScreeningDocument object after it " +
                    "is persisted to the database",
            response = ScreeningDocument.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningDocument " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ScreeningDocument.class),
            @ApiResponse(
                    code = 201,
                    message = "ScreeningDocument " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ScreeningDocument.class),
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
    @PostMapping(value = SCREENING_DOCUMENT + SLASH + NEW_SCREENING_DOCUMENT)
    public ResponseEntity<MetadataHateoas> createScreeningDocument(
            HttpServletRequest request,
            @RequestBody ScreeningDocument postalCode)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                postalCodeService.createNewScreeningDocument(postalCode);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all postalCode
    // GET [contextPath][api]/metadata/skjermingdokument/
    @ApiOperation(
            value = "Retrieves all ScreeningDocument ",
            response = ScreeningDocument.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningDocument codes found",
                    response = ScreeningDocument.class),
            @ApiResponse(
                    code = 404,
                    message = "No ScreeningDocument found"),
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
    @GetMapping(value = SCREENING_DOCUMENT)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(postalCodeService.findAll());
    }

    // Retrieves a given postalCode identified by a code
    // GET [contextPath][api]/metadata/skjermingdokument/{code}/
    @ApiOperation(
            value = "Gets postalCode identified by its code",
            notes = "Returns the requested postalCode object",
            response = ScreeningDocument.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningDocument " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ScreeningDocument.class),
            @ApiResponse(
                    code = 201,
                    message = "ScreeningDocument " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ScreeningDocument.class),
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
    @GetMapping(value = SCREENING_DOCUMENT + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = postalCodeService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested postalCode(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-skjermingdokument
    @ApiOperation(
            value = "Creates a suggested ScreeningDocument",
            response = ScreeningDocument.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningDocument codes found",
                    response = ScreeningDocument.class),
            @ApiResponse(
                    code = 404,
                    message = "No ScreeningDocument found"),
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
    @GetMapping(value = NEW_SCREENING_DOCUMENT)
    public ResponseEntity<MetadataHateoas>
    generateDefaultScreeningDocument(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (postalCodeService.generateDefaultScreeningDocument());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a skjermingdokument
    // PUT [contextPath][api]/metatdata/skjermingdokument/
    @ApiOperation(
            value = "Updates a ScreeningDocument object",
            notes = "Returns the newly updated ScreeningDocument object after it " +
                    "is persisted to the database",
            response = ScreeningDocument.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningDocument " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ScreeningDocument.class),
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
    @PutMapping(value = SCREENING_DOCUMENT + SLASH + SCREENING_DOCUMENT)
    public ResponseEntity<MetadataHateoas> updateScreeningDocument(
            @ApiParam(name = "systemID",
                    value = "code of ScreeningDocument to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @RequestBody ScreeningDocument postalCode,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = postalCodeService.handleUpdate
                (systemID,
                        CommonUtils.Validation.parseETAG(
                                request.getHeader(ETAG)),
                        postalCode);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
