package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.ScreeningMetadata;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IScreeningMetadataService;
import nikita.webapp.web.controller.hateoas.NoarkController;
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
@RequestMapping(
        value = Constants.HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH,
        produces = {NOARK5_V4_CONTENT_TYPE_JSON, NOARK5_V4_CONTENT_TYPE_JSON_XML})
@SuppressWarnings("unchecked")
public class ScreeningMetadataController
        extends NoarkController {

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

    @RequestMapping(
            method = RequestMethod.POST,
            value = SCREENING_METADATA + SLASH + NEW_SCREENING_METADATA
    )
    public ResponseEntity<MetadataHateoas> createScreeningMetadata(
            HttpServletRequest request,
            @RequestBody ScreeningMetadata postalCode)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                postalCodeService.createNewScreeningMetadata(postalCode,
                        getAddress(request));

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

    @RequestMapping(
            method = RequestMethod.GET,
            value = SCREENING_METADATA
    )
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(postalCodeService.findAll(getAddress(request)));
    }

    // Retrieves a given postalCode identified by a systemId
    // GET [contextPath][api]/metadata/skjermingdokument/{systemId}/
    @ApiOperation(
            value = "Gets postalCode identified by its systemId",
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

    @RequestMapping(
            value = SCREENING_METADATA + SLASH + LEFT_PARENTHESIS + SYSTEM_ID +
                    RIGHT_PARENTHESIS + SLASH,
            method = RequestMethod.GET
    )
    public ResponseEntity<MetadataHateoas> findBySystemId(
            @PathVariable("systemID") final String systemId,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = postalCodeService.find(systemId, getAddress(request));

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

    @RequestMapping(
            method = RequestMethod.GET,
            value = NEW_SCREENING_METADATA
    )
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

    @RequestMapping(
            method = RequestMethod.PUT,
            value = SCREENING_METADATA + SLASH + SCREENING_METADATA
    )
    public ResponseEntity<MetadataHateoas> updateScreeningMetadata(
            @ApiParam(name = "systemID",
                    value = "systemId of ScreeningMetadata to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @RequestBody ScreeningMetadata postalCode,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = postalCodeService.handleUpdate
                (systemID, parseETAG(request.getHeader(ETAG)), postalCode,
                        getAddress(request));

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
