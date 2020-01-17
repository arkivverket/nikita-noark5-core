package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ClassifiedCode;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IClassifiedCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 20/01/19.
 */

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class ClassifiedCodeController {

    private IClassifiedCodeService classifiedCodeService;

    public ClassifiedCodeController(IClassifiedCodeService
                                            classifiedCodeService) {
        this.classifiedCodeService = classifiedCodeService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new graderingskode
    // POST [contextPath][api]/metadata/ny-graderingskode
    @ApiOperation(
            value = "Persists a new ClassifiedCode object",
            notes = "Returns the newly created ClassifiedCode object after "
                    + "it is persisted to the database",
            response = ClassifiedCode.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassifiedCode " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassifiedCode.class),
            @ApiResponse(
                    code = 201,
                    message = "ClassifiedCode " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ClassifiedCode.class),
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
    @PostMapping(value = NEW_CLASSIFIED_CODE)
    public ResponseEntity<MetadataHateoas> createClassifiedCode(
            HttpServletRequest request,
            @RequestBody ClassifiedCode classifiedCode)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                classifiedCodeService.createNewClassifiedCode(
                        classifiedCode);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all classifiedCode
    // GET [contextPath][api]/metadata/graderingskode/
    @ApiOperation(
            value = "Retrieves all ClassifiedCode ",
            response = ClassifiedCode.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassifiedCode codes found",
                    response = ClassifiedCode.class),
            @ApiResponse(
                    code = 404,
                    message = "No ClassifiedCode found"),
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
    @GetMapping(value = CLASSIFIED_CODE)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classifiedCodeService.findAll());
    }

    // Retrieves a given classifiedCode identified by a code
    // GET [contextPath][api]/metadata/graderingskode/{code}/
    @ApiOperation(
            value = "Gets classifiedCode identified by its code",
            notes = "Returns the requested classifiedCode object",
            response = ClassifiedCode.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassifiedCode " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassifiedCode.class),
            @ApiResponse(
                    code = 201,
                    message = "ClassifiedCode " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ClassifiedCode.class),
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
    @GetMapping(value = CLASSIFIED_CODE + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
                classifiedCodeService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested classifiedCode(like a template) with default
    // values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-graderingskode
    @ApiOperation(
            value = "Creates a suggested ClassifiedCode",
            response = ClassifiedCode.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassifiedCode codes found",
                    response = ClassifiedCode.class),
            @ApiResponse(
                    code = 404,
                    message = "No ClassifiedCode found"),
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
    @GetMapping(value = NEW_CLASSIFIED_CODE)
    public ResponseEntity<MetadataHateoas>
    generateDefaultClassifiedCode(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (classifiedCodeService.generateDefaultClassifiedCode());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a graderingskode
    // PUT [contextPath][api]/metadata/graderingskode/{code}
    @ApiOperation(
            value = "Updates a ClassifiedCode object",
            notes = "Returns the newly updated ClassifiedCode object after"
                    + "it is persisted to the database",
            response = ClassifiedCode.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ClassifiedCode " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassifiedCode.class),
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
    @PutMapping(value = CLASSIFIED_CODE + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> updateClassifiedCode(
            @ApiParam(name = CODE,
                      value = "code of fonds to update.",
                      required = true)
            @PathVariable(CODE) String code,
            @RequestBody ClassifiedCode classifiedCode,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas =
            classifiedCodeService.handleUpdate(code,
                CommonUtils.Validation.parseETAG(request.getHeader(ETAG)),
                classifiedCode);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
