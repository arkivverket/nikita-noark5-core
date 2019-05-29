package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.application.FondsStructureDetails;
import nikita.webapp.service.application.ApplicationService;
import nikita.webapp.service.interfaces.IClassificationSystemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value =
        HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH,
        produces = {NOARK5_V5_CONTENT_TYPE_JSON,
                NOARK5_V5_CONTENT_TYPE_JSON_XML})
public class ClassificationSystemHateoasController
        extends NoarkController {

    private IClassificationSystemService classificationSystemService;

    private ApplicationService applicationService;

    public ClassificationSystemHateoasController(
            IClassificationSystemService classificationSystemService,
            ApplicationService applicationService) {
        this.classificationSystemService = classificationSystemService;
        this.applicationService = applicationService;
    }

    // API - All POST Requests (CRUD - CREATE)

    @ApiOperation(value = "Persists a ClassificationSystem object",
            notes = "Returns the newly created classificationSystem object " +
                    "after it was persisted to the database",
            response = ClassificationSystemHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ClassificationSystem " +
                    API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassificationSystemHateoas.class),
            @ApiResponse(code = 201, message = "ClassificationSystem " +
                    API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ClassificationSystemHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type ClassificationSystem"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(method = POST,
            value = NEW_CLASSIFICATION_SYSTEM,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassificationSystemHateoas>
    createClassificationSystem(
            HttpServletRequest request,
            @ApiParam(name = "classificationSystem",
                    value = "Incoming classificationSystem object",
                    required = true)
            @RequestBody ClassificationSystem classificationSystem)
            throws NikitaException {
        ClassificationSystemHateoas classificationSystemHateoas =
                classificationSystemService.
                        save(classificationSystem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(classificationSystem.getVersion().toString())
                .body(classificationSystemHateoas);
    }

    @ApiOperation(value = "Persists a Class object associated with the given" +
            " ClassificationSystem systemId", notes = "Returns the newly " +
            "created class object after it was associated with a " +
            "classificationSystem object and persisted to the database",
            response = ClassHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message =
                    "Class " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = Class.class),
            @ApiResponse(code = 201,
                    message =
                            "Class " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = Class.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Class"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(method = POST,
            value = CLASSIFICATION_SYSTEM + SLASH + LEFT_PARENTHESIS +
                    "systemID" + RIGHT_PARENTHESIS + SLASH +
                    NEW_CLASS,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassHateoas>
    createClassAssociatedWithClassificationSystem(
            HttpServletRequest request,
            @ApiParam(name = "systemId",
                    value = "systemId of classificationSystem to associate " +
                            "the class with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "klass",
                    value = "Incoming class object",
                    required = true)
            @RequestBody Class klass) throws NikitaException {

        ClassHateoas classHateoas =
                classificationSystemService.
                        createClassAssociatedWithClassificationSystem(
                                systemID, klass);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(klass.getVersion().toString())
                .body(classHateoas);
    }

    // API - All GET Requests (CRUD - READ)

    @RequestMapping(method = GET,
            value = CLASSIFICATION_SYSTEM + SLASH + LEFT_PARENTHESIS +
                    SYSTEM_ID + RIGHT_PARENTHESIS)
    public ResponseEntity<ClassificationSystemHateoas> findOne(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of classificationSystem to retrieve.",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        ClassificationSystemHateoas classificationSystemHateoas =
                classificationSystemService.
                        findSingleClassificationSystem(systemID);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(classificationSystemHateoas.getEntityVersion().toString())
                .body(classificationSystemHateoas);
    }


    // Retrieve all Series associated with the ClassificationSystem
    // identified by the given systemId
    // GET [contextPath][api]/arkivstruktur/klassifikasjonssystem/{systemId}/arkivdel
    // https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/arkivdel/
    @ApiOperation(value = "Retrieves a a list of Series that are parents of " +
            " the ClassificationSystem entity identified by systemId",
            response = ClassificationSystemHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "ClassificationSystem returned",
                    response = ClassificationSystemHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SYSTEM_ID_PARAMETER + SLASH + SERIES)
    public ResponseEntity<SeriesHateoas>
    findParentClassificationSystemByFileSystemId(
            @ApiParam(name = "systemID",
                    value = "systemID of the classificationSystem",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return classificationSystemService.
                findSeriesAssociatedWithClassificationSystem(systemID);
    }

    @ApiOperation(value = "Retrieves multiple ClassificationSystem entities " +
            "limited by ownership rights",
            response = ClassificationSystemHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "ClassificationSystem list found",
                    response = ClassificationSystemHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(value = CLASSIFICATION_SYSTEM, method = GET)
    public ResponseEntity<ClassificationSystemHateoas>
    findAllClassificationSystem(
            HttpServletRequest request) {
        ClassificationSystemHateoas classificationSystemHateoas =
                classificationSystemService.findAllClassificationSystem();
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationSystemHateoas);
    }

    // Return a Class object with default values
    //GET [contextPath][api]/arkivstruktur/klassifikasjonssystem/{systemId}/klasse
    @ApiOperation(
            value = "Retrieves the class's associated with a " +
                    "ClassificationSystem identified by a systemId",
            response = Class.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Class returned", response = Class.class),
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
    @GetMapping(value =
            CLASSIFICATION_SYSTEM + SLASH + LEFT_PARENTHESIS + SYSTEM_ID +
                    RIGHT_PARENTHESIS + SLASH + CLASS
    )
    public ResponseEntity<ClassHateoas>
    findClassAssociatedWithClassificationSystem(
            HttpServletRequest request,
            @ApiParam(
                    name = "systemID",
                    value = "systemId of ClassificationSystem you want " +
                            "retrieve associated  Class objects for",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationSystemService.
                        findAllClassAssociatedWithClassificationSystem(
                                systemID));
    }

    // Return a Class object with default values
    //GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-underklasse
    @ApiOperation(
            value = "Create a Class with default values",
            response = Class.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Class returned", response = Class.class),
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

    @GetMapping(value = CLASSIFICATION_SYSTEM + SLASH + LEFT_PARENTHESIS +
            SYSTEM_ID + RIGHT_PARENTHESIS + SLASH + NEW_CLASS
    )
    public ResponseEntity<ClassHateoas> createDefaultClass(
            HttpServletRequest request,
            @ApiParam(
                    name = "systemID",
                    value = "systemId of Class to associate Class with.",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationSystemService.generateDefaultClass(systemID));
    }

    // Delete a ClassificationSystem identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/klassifikasjonssystem/{systemId}/
    @ApiOperation(value = "Deletes a single ClassificationSystem entity " +
            "identified by systemID", response = FondsStructureDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Parent ApplicationDetails returned",
                    response = FondsStructureDetails.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(method = DELETE,
            value = SLASH + CLASSIFICATION_SYSTEM + SLASH + LEFT_PARENTHESIS +
                    SYSTEM_ID + RIGHT_PARENTHESIS)
    public ResponseEntity<FondsStructureDetails>
    deleteClassificationSystemBySystemId(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the ClassificationSystem to delete",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        classificationSystemService.deleteEntity(systemID);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(applicationService.getFondsStructureDetails());
    }


    // Delete all ClassificationSystem
    // DELETE [contextPath][api]/arkivstruktur/klassifikasjonssystem/
    @ApiOperation(value = "Deletes all ClassificationSystem",
            response = Count.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all ClassificationSystem",
                    response = Count.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(CLASSIFICATION_SYSTEM)
    public ResponseEntity<Count> deleteAllClassificationSystem() {
        return ResponseEntity.status(NO_CONTENT).
                body(new Count(classificationSystemService.deleteAllByOwnedBy()));
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a ClassificationSystem
    // PUT [contextPath][api]/arkivstruktur/klassifikasjonssystem/{systemID}
    @ApiOperation(value = "Updates a ClassificationSystem object",
            notes = "Returns the newly updated ClassificationSystem object " +
                    "after it is persisted to the database",
            response = ClassificationSystemHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "ClassificationSystem " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassificationSystemHateoas.class),
            @ApiResponse(code = 201, message =
                    "ClassificationSystem persisted",
                    response = ClassificationSystemHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type ClassificationSystem"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(method = PUT,
            value = CLASSIFICATION_SYSTEM + SLASH + LEFT_PARENTHESIS +
                    SYSTEM_ID + RIGHT_PARENTHESIS,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassificationSystemHateoas>
    updateClassificationSystem(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of classificationSystem to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @ApiParam(name = "classificationSystem",
                    value = "Incoming classificationSystem object",
                    required = true)
            @RequestBody ClassificationSystem classificationSystem)
            throws NikitaException {
        validateForUpdate(classificationSystem);
        ClassificationSystemHateoas classificationSystemHateoas =
                classificationSystemService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)),
                        classificationSystem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(classificationSystemHateoas.getEntityVersion().toString())
                .body(classificationSystemHateoas);
    }
}
