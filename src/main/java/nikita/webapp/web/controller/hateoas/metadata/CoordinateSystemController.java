package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.CoordinateSystem;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.ICoordinateSystemService;
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
public class CoordinateSystemController {

    private ICoordinateSystemService coordinateSystemService;

    public CoordinateSystemController(ICoordinateSystemService coordinateSystemService) {
        this.coordinateSystemService = coordinateSystemService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new land
    // POST [contextPath][api]/metadata/ny-land
    @ApiOperation(
            value = "Persists a new CoordinateSystem object",
            notes = "Returns the newly created CoordinateSystem object after it " +
                    "is persisted to the database",
            response = CoordinateSystem.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "CoordinateSystem " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CoordinateSystem.class),
            @ApiResponse(
                    code = 201,
                    message = "CoordinateSystem " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CoordinateSystem.class),
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
    @PostMapping(value = NEW_COORDINATE_SYSTEM)
    public ResponseEntity<MetadataHateoas> createCoordinateSystem(
            HttpServletRequest request,
            @RequestBody CoordinateSystem coordinateSystem)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                coordinateSystemService.createNewCoordinateSystem(coordinateSystem);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all coordinateSystem
    // GET [contextPath][api]/metadata/land/
    @ApiOperation(
            value = "Retrieves all CoordinateSystem ",
            response = CoordinateSystem.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "CoordinateSystem codes found",
                    response = CoordinateSystem.class),
            @ApiResponse(
                    code = 404,
                    message = "No CoordinateSystem found"),
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
    @GetMapping(value = COORDINATE_SYSTEM)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(coordinateSystemService.findAll());
    }

    // Retrieves a given coordinateSystem identified by a code
    // GET [contextPath][api]/metadata/land/{code}/
    @ApiOperation(
            value = "Gets coordinateSystem identified by its code",
            notes = "Returns the requested coordinateSystem object",
            response = CoordinateSystem.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "CoordinateSystem " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CoordinateSystem.class),
            @ApiResponse(
                    code = 201,
                    message = "CoordinateSystem " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CoordinateSystem.class),
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
    @GetMapping(value = COORDINATE_SYSTEM + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = coordinateSystemService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested coordinateSystem(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-land
    @ApiOperation(
            value = "Creates a suggested CoordinateSystem",
            response = CoordinateSystem.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "CoordinateSystem codes found",
                    response = CoordinateSystem.class),
            @ApiResponse(
                    code = 404,
                    message = "No CoordinateSystem found"),
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
    @GetMapping(value = NEW_COORDINATE_SYSTEM)
    public ResponseEntity<MetadataHateoas>
    generateDefaultCoordinateSystem(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (coordinateSystemService.generateDefaultCoordinateSystem());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a land
    // PUT [contextPath][api]/metadata/land/{code}
    @ApiOperation(
            value = "Updates a CoordinateSystem object",
            notes = "Returns the newly updated CoordinateSystem object after it " +
                    "is persisted to the database",
            response = CoordinateSystem.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "CoordinateSystem " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CoordinateSystem.class),
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
    @PutMapping(value = COORDINATE_SYSTEM + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> updateCoordinateSystem(
            @ApiParam(name = CODE,
                    value = "code of fonds to update.",
                    required = true)
            @PathVariable(CODE) String code,
            @RequestBody CoordinateSystem coordinateSystem,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = coordinateSystemService.handleUpdate
                (code, parseETAG(request.getHeader(ETAG)), coordinateSystem);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
