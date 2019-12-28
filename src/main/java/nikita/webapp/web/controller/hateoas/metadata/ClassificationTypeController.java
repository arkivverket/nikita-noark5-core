package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ClassificationType;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IClassificationTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 11/03/18.
 */

@RestController
@RequestMapping(value = HREF_BASE_METADATA,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class ClassificationTypeController {

    private IClassificationTypeService classificationTypeService;

    public ClassificationTypeController(IClassificationTypeService
                                                classificationTypeService) {
        this.classificationTypeService = classificationTypeService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new mappetype
    // POST [contextPath][api]/metadata/mappetype/ny-mappetype
    @ApiOperation(
            value = "Persists a new ClassificationType object",
            notes = "Returns the newly created ClassificationType object after "
                    + "it is persisted to the database",
            response = ClassificationType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassificationType " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassificationType.class),
            @ApiResponse(
                    code = 201,
                    message = "ClassificationType " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ClassificationType.class),
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
    @PostMapping(value = CLASSIFICATION_TYPE + SLASH + NEW_CLASSIFICATION_TYPE)
    public ResponseEntity<MetadataHateoas> createClassificationType(
            HttpServletRequest request,
            @RequestBody ClassificationType classificationType)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                classificationTypeService.createNewClassificationType(
                        classificationType);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all classificationType
    // GET [contextPath][api]/metadata/mappetype/
    @ApiOperation(
            value = "Retrieves all ClassificationType ",
            response = ClassificationType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassificationType codes found",
                    response = ClassificationType.class),
            @ApiResponse(
                    code = 404,
                    message = "No ClassificationType found"),
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
    @GetMapping(value = CLASSIFICATION_TYPE)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationTypeService.findAll());
    }

    // Retrieves a given classificationType identified by a code
    // GET [contextPath][api]/metadata/mappetype/{code}/
    @ApiOperation(
            value = "Gets classificationType identified by its code",
            notes = "Returns the requested classificationType object",
            response = ClassificationType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassificationType " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassificationType.class),
            @ApiResponse(
                    code = 201,
                    message = "ClassificationType " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ClassificationType.class),
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
    @GetMapping(value = CLASSIFICATION_TYPE + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
                classificationTypeService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested classificationType(like a template) with default
    // values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-mappetype
    @ApiOperation(
            value = "Creates a suggested ClassificationType",
            response = ClassificationType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassificationType codes found",
                    response = ClassificationType.class),
            @ApiResponse(
                    code = 404,
                    message = "No ClassificationType found"),
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
    @GetMapping(value = NEW_CLASSIFICATION_TYPE)
    public ResponseEntity<MetadataHateoas>
    generateDefaultClassificationType(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (classificationTypeService.generateDefaultClassificationType());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a mappetype
    // PUT [contextPath][api]/metatdata/mappetype/
    @ApiOperation(
            value = "Updates a ClassificationType object",
            notes = "Returns the newly updated ClassificationType object after"
                    + "it is persisted to the database",
            response = ClassificationType.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassificationType " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassificationType.class),
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
    @PutMapping(value = CLASSIFICATION_TYPE + SLASH + CLASSIFICATION_TYPE)
    public ResponseEntity<MetadataHateoas> updateClassificationType(
            @ApiParam(name = "systemID",
                    value = "code of fonds to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @RequestBody ClassificationType classificationType,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = classificationTypeService.handleUpdate
                (systemID,
                        CommonUtils.Validation.parseETAG(
                                request.getHeader(ETAG)),
                        classificationType);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
