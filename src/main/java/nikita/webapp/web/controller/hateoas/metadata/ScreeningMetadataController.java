package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ScreeningMetadata;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IScreeningMetadataService;
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
@RequestMapping(value = HREF_BASE_METADATA,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class ScreeningMetadataController {

    private IScreeningMetadataService postalCodeService;

    public ScreeningMetadataController(IScreeningMetadataService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new skjermingdokument
    // POST [contextPath][api]/metadata/skjermingdokument/ny-skjermingdokument
    @ApiOperation(
            value = "Persists a new ScreeningMetadata object",
            notes = "Returns the newly created ScreeningMetadata object after it " +
                    "is persisted to the database",
            response = ScreeningMetadata.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningMetadata " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ScreeningMetadata.class),
            @ApiResponse(
                    code = 201,
                    message = "ScreeningMetadata " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ScreeningMetadata.class),
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
    @PostMapping(value = SCREENING_METADATA + SLASH + NEW_SCREENING_METADATA)
    public ResponseEntity<MetadataHateoas> createScreeningMetadata(
            HttpServletRequest request,
            @RequestBody ScreeningMetadata postalCode)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                postalCodeService.createNewScreeningMetadata(postalCode);

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
            value = "Retrieves all ScreeningMetadata ",
            response = ScreeningMetadata.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningMetadata codes found",
                    response = ScreeningMetadata.class),
            @ApiResponse(
                    code = 404,
                    message = "No ScreeningMetadata found"),
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
    @GetMapping(value = SCREENING_METADATA)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(postalCodeService.findAll());
    }

    // Retrieves a given postalCode identified by a kode
    // GET [contextPath][api]/metadata/skjermingdokument/{kode}/
    @ApiOperation(
            value = "Gets postalCode identified by its kode",
            notes = "Returns the requested postalCode object",
            response = ScreeningMetadata.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningMetadata " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ScreeningMetadata.class),
            @ApiResponse(
                    code = 201,
                    message = "ScreeningMetadata " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ScreeningMetadata.class),
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
    @GetMapping(value = SCREENING_METADATA + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String kode,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = postalCodeService.findByCode(kode);

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
            value = "Creates a suggested ScreeningMetadata",
            response = ScreeningMetadata.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningMetadata codes found",
                    response = ScreeningMetadata.class),
            @ApiResponse(
                    code = 404,
                    message = "No ScreeningMetadata found"),
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
    @GetMapping(value = NEW_SCREENING_METADATA)
    public ResponseEntity<MetadataHateoas>
    generateDefaultScreeningMetadata(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (postalCodeService.generateDefaultScreeningMetadata());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a skjermingdokument
    // PUT [contextPath][api]/metatdata/skjermingdokument/
    @ApiOperation(
            value = "Updates a ScreeningMetadata object",
            notes = "Returns the newly updated ScreeningMetadata object after it " +
                    "is persisted to the database",
            response = ScreeningMetadata.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ScreeningMetadata " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ScreeningMetadata.class),
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
    @PutMapping(value = SCREENING_METADATA + SLASH + SCREENING_METADATA)
    public ResponseEntity<MetadataHateoas> updateScreeningMetadata(
            @ApiParam(name = "kode",
                    value = "kode of ScreeningMetadata to update.",
                    required = true)
            @PathVariable("kode") String kode,
            @RequestBody ScreeningMetadata postalCode,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = postalCodeService.handleUpdate
                (kode,
                        CommonUtils.Validation.parseETAG(
                                request.getHeader(ETAG)),
                        postalCode);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
