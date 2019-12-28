package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.SignOffMethod;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.ISignOffMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 13/02/18.
 */

@RestController
@RequestMapping(value = HREF_BASE_METADATA,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class SignOffMethodController {

    private ISignOffMethodService signOffMethodService;

    public SignOffMethodController(
            ISignOffMethodService signOffMethodService) {
        this.signOffMethodService = signOffMethodService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new avskrivningsmaate
    // POST [contextPath][api]/metadata/avskrivningsmaate/ny-avskrivningsmaate
    @ApiOperation(
            value = "Persists a new SignOffMethod object",
            notes = "Returns the newly created SignOffMethod object  " +
                    "after it is persisted to the database",
            response = SignOffMethod.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "SignOffMethod " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SignOffMethod.class),
            @ApiResponse(
                    code = 201,
                    message = "SignOffMethod " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = SignOffMethod.class),
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
    @PostMapping(value = SIGN_OFF_METHOD + SLASH + NEW_SIGN_OFF_METHOD)
    public ResponseEntity<MetadataHateoas> createSignOffMethod(
            HttpServletRequest request,
            @RequestBody SignOffMethod signOffMethod)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                signOffMethodService.createNewSignOffMethod(
                        signOffMethod);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all SignOffMethod
    // GET [contextPath][api]/metadata/avskrivningsmaate/
    @ApiOperation(
            value = "Retrieves all SignOffMethod ",
            response = SignOffMethod.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "SignOffMethod codes found",
                    response = SignOffMethod.class),
            @ApiResponse(
                    code = 404,
                    message = "No SignOffMethod found"),
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
    @GetMapping(value = SIGN_OFF_METHOD)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(signOffMethodService.findAll());
    }

    // Retrieves a given SignOffMethod identified by a code
    // GET [contextPath][api]/metadata/avskrivningsmaate/{code}/
    @ApiOperation(
            value = "Gets SignOffMethod identified by its code",
            notes = "Returns the requested SignOffMethod object",
            response = SignOffMethod.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "SignOffMethod " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SignOffMethod.class),
            @ApiResponse(
                    code = 201,
                    message = "SignOffMethod " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = SignOffMethod.class),
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
    @GetMapping(value = SIGN_OFF_METHOD + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = signOffMethodService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested SignOffMethod(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-avskrivningsmaate
    @ApiOperation(
            value = "Creates a suggested SignOffMethod",
            response = SignOffMethod.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "SignOffMethod codes found",
                    response = SignOffMethod.class),
            @ApiResponse(
                    code = 404,
                    message = "No SignOffMethod found"),
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
    @GetMapping(value = NEW_SIGN_OFF_METHOD)
    public ResponseEntity<MetadataHateoas>
    generateDefaultSignOffMethod(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (signOffMethodService
                        .generateDefaultSignOffMethod());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a avskrivningsmaate
    // PUT [contextPath][api]/metatdata/avskrivningsmaate/
    @ApiOperation(
            value = "Updates a SignOffMethod object",
            notes = "Returns the newly updated SignOffMethod object after it " +
                    "is persisted to the database",
            response = SignOffMethod.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "SignOffMethod " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SignOffMethod.class),
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
    @PutMapping(value = SIGN_OFF_METHOD + SLASH + SIGN_OFF_METHOD)
    public ResponseEntity<MetadataHateoas> updateSignOffMethod(
            @ApiParam(name = "systemID",
                    value = "code of fonds to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @RequestBody SignOffMethod SignOffMethod,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = signOffMethodService
                .handleUpdate
                        (systemID,
                                CommonUtils.Validation.parseETAG(request.getHeader(ETAG)),
                                SignOffMethod);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
