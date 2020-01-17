package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ElectronicSignatureVerified;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IElectronicSignatureVerifiedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 13/02/18.
 */

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class ElectronicSignatureVerifiedController {

    private IElectronicSignatureVerifiedService
            electronicSignatureVerifiedService;

    public ElectronicSignatureVerifiedController(
            IElectronicSignatureVerifiedService
                    electronicSignatureVerifiedService) {
        this.electronicSignatureVerifiedService =
                electronicSignatureVerifiedService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new elektronisksignaturverifisert
    // POST [contextPath][api]/metadata/ny-elektronisksignaturverifisert
    @ApiOperation(
            value = "Persists a new ElectronicSignatureVerified object",
            notes = "Returns the newly created " +
                    "ElectronicSignatureVerified  object after it is " +
                    "persisted to the database",
            response = ElectronicSignatureVerified.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ElectronicSignatureVerified " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ElectronicSignatureVerified.class),
            @ApiResponse(
                    code = 201,
                    message = "ElectronicSignatureVerified " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ElectronicSignatureVerified.class),
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
    @PostMapping(value = NEW_ELECTRONIC_SIGNATURE_VERIFIED)
    public ResponseEntity<MetadataHateoas>
    createElectronicSignatureVerified(
            HttpServletRequest request,
            @RequestBody ElectronicSignatureVerified
                    electronicSignatureVerified)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                electronicSignatureVerifiedService.
                        createNewElectronicSignatureVerified(
                                electronicSignatureVerified);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all ElectronicSignatureVerified
    // GET [contextPath][api]/metadata/elektronisksignaturverifisert/
    @ApiOperation(
            value = "Retrieves all ElectronicSignatureVerified ",
            response = ElectronicSignatureVerified.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ElectronicSignatureVerified codes found",
                    response = ElectronicSignatureVerified.class),
            @ApiResponse(
                    code = 404,
                    message = "No ElectronicSignatureVerified found"),
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
    @GetMapping(value = ELECTRONIC_SIGNATURE_VERIFIED)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(electronicSignatureVerifiedService.findAll());
    }

    // Retrieves a given ElectronicSignatureVerified identified by a
    // code
    // GET [contextPath][api]/metadata/elektronisksignaturverifisert/
    // {kode}/
    @ApiOperation(
            value = "Gets ElectronicSignatureVerified identified by its " +
                    "code",
            notes = "Returns the requested ElectronicSignatureVerified " +
                    "object",
            response = ElectronicSignatureVerified.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ElectronicSignatureVerified " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ElectronicSignatureVerified.class),
            @ApiResponse(
                    code = 201,
                    message = "ElectronicSignatureVerified " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ElectronicSignatureVerified.class),
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

    @GetMapping(value = ELECTRONIC_SIGNATURE_VERIFIED + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
                electronicSignatureVerifiedService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested ElectronicSignatureVerified(like a template) with
    // default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-elektronisksignaturverifisert
    @ApiOperation(
            value = "Creates a suggested ElectronicSignatureVerified",
            response = ElectronicSignatureVerified.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ElectronicSignatureVerified codes found",
                    response = ElectronicSignatureVerified.class),
            @ApiResponse(
                    code = 404,
                    message = "No ElectronicSignatureVerified found"),
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
    @GetMapping(value = NEW_ELECTRONIC_SIGNATURE_VERIFIED)
    public ResponseEntity<MetadataHateoas>
    generateDefaultElectronicSignatureVerified(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (electronicSignatureVerifiedService
                        .generateDefaultElectronicSignatureVerified());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a elektronisksignaturverifisert
    // PUT [contextPath][api]/metadata/elektronisksignaturverifisert/{kode}
    @ApiOperation(
            value = "Updates a ElectronicSignatureVerified object",
            notes = "Returns the newly updated " +
                    "ElectronicSignatureVerified object after it " +
                    "is persisted to the database",
            response = ElectronicSignatureVerified.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ElectronicSignatureVerified " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ElectronicSignatureVerified.class),
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
    @PutMapping(value = ELECTRONIC_SIGNATURE_VERIFIED + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas>
    updateElectronicSignatureVerified(
            @ApiParam(name = CODE,
                    value = "code of electronicSignatureVerified to update.",
                    required = true)
            @PathVariable(CODE) String code,
            @RequestBody ElectronicSignatureVerified
                    electronicSignatureVerified,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
            electronicSignatureVerifiedService.handleUpdate(code,
                CommonUtils.Validation.parseETAG(request.getHeader(ETAG)),
                electronicSignatureVerified);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
