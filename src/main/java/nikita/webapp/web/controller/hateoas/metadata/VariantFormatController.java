package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.VariantFormat;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IVariantFormatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 12/03/18.
 */

@RestController
@RequestMapping(value = HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class VariantFormatController {

    private IVariantFormatService variantFormatService;

    public VariantFormatController(IVariantFormatService variantFormatService) {
        this.variantFormatService = variantFormatService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new mappetype
    // POST [contextPath][api]/metadata/mappetype/ny-mappetype
    @ApiOperation(
            value = "Persists a new VariantFormat object",
            notes = "Returns the newly created VariantFormat object after it " +
                    "is persisted to the database",
            response = VariantFormat.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "VariantFormat " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = VariantFormat.class),
            @ApiResponse(
                    code = 201,
                    message = "VariantFormat " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = VariantFormat.class),
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
    @PostMapping(value = VARIANT_FORMAT + SLASH + NEW_VARIANT_FORMAT)
    public ResponseEntity<MetadataHateoas> createVariantFormat(
            HttpServletRequest request,
            @RequestBody VariantFormat variantFormat)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                variantFormatService.createNewVariantFormat(variantFormat);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all variantFormat
    // GET [contextPath][api]/metadata/mappetype/
    @ApiOperation(
            value = "Retrieves all VariantFormat ",
            response = VariantFormat.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "VariantFormat codes found",
                    response = VariantFormat.class),
            @ApiResponse(
                    code = 404,
                    message = "No VariantFormat found"),
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
    @GetMapping(value = VARIANT_FORMAT)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(variantFormatService.findAll());
    }

    // Retrieves a given variantFormat identified by a code
    // GET [contextPath][api]/metadata/mappetype/{code}/
    @ApiOperation(
            value = "Gets variantFormat identified by its code",
            notes = "Returns the requested variantFormat object",
            response = VariantFormat.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "VariantFormat " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = VariantFormat.class),
            @ApiResponse(
                    code = 201,
                    message = "VariantFormat " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = VariantFormat.class),
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
    @GetMapping(value = VARIANT_FORMAT + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = variantFormatService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested variantFormat(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-mappetype
    @ApiOperation(
            value = "Creates a suggested VariantFormat",
            response = VariantFormat.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "VariantFormat codes found",
                    response = VariantFormat.class),
            @ApiResponse(
                    code = 404,
                    message = "No VariantFormat found"),
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
    @GetMapping(value = NEW_VARIANT_FORMAT)
    public ResponseEntity<MetadataHateoas>
    generateDefaultVariantFormat(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (variantFormatService.generateDefaultVariantFormat());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a mappetype
    // PUT [contextPath][api]/metatdata/mappetype/
    @ApiOperation(
            value = "Updates a VariantFormat object",
            notes = "Returns the newly updated VariantFormat object after it " +
                    "is persisted to the database",
            response = VariantFormat.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "VariantFormat " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = VariantFormat.class),
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
    @PutMapping(value = VARIANT_FORMAT + SLASH + VARIANT_FORMAT)
    public ResponseEntity<MetadataHateoas> updateVariantFormat(
            @ApiParam(name = "systemID",
                    value = "code of fonds to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @RequestBody VariantFormat variantFormat,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = variantFormatService.handleUpdate
                (systemID,
                        CommonUtils.Validation.parseETAG(
                                request.getHeader(ETAG)),
                        variantFormat);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
