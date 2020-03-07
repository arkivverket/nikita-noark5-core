package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.util.exceptions.NikitaException;
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

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
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
    @PostMapping(value = NEW_CLASSIFICATION_SYSTEM,
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
    @PostMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASS,
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

    @GetMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<ClassificationSystemHateoas> findOne(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of classificationSystem to retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
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
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/
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
    @GetMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER + SLASH + SERIES)
    public ResponseEntity<SeriesHateoas>
    findParentClassificationSystemByFileSystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the classificationSystem",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
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
    @GetMapping(value = CLASSIFICATION_SYSTEM)
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
    @GetMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER + SLASH + CLASS)
    public ResponseEntity<ClassHateoas>
    findClassAssociatedWithClassificationSystem(
            HttpServletRequest request,
            @ApiParam(
                    name = SYSTEM_ID,
                    value = "systemId of ClassificationSystem you want " +
                            "retrieve associated  Class objects for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationSystemService.
                        findAllClassAssociatedWithClassificationSystem(
                                systemID));
    }

    // Return a Class object with default values
    //GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-klasse
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

    @GetMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASS)
    public ResponseEntity<ClassHateoas> createDefaultClass(
            HttpServletRequest request,
            @ApiParam(
                    name = SYSTEM_ID,
                    value = "systemId of Class to associate Class with.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationSystemService.generateDefaultClass(systemID));
    }

    // Delete a ClassificationSystem identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/klassifikasjonssystem/{systemId}/
    @ApiOperation(value = "Deletes a single ClassificationSystem entity " +
            "identified by systemID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "ClassificationSystem deleted",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String>
    deleteClassificationSystemBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the ClassificationSystem to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        classificationSystemService.deleteEntity(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(DELETE_RESPONSE);
    }


    // Delete all ClassificationSystem
    // DELETE [contextPath][api]/arkivstruktur/klassifikasjonssystem/
    @ApiOperation(value = "Deletes all ClassificationSystem",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "All ClassificationSystem deleted",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = CLASSIFICATION_SYSTEM)
    public ResponseEntity<String> deleteAllClassificationSystem() {
        classificationSystemService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
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
    @PutMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassificationSystemHateoas>
    updateClassificationSystem(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of classificationSystem to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
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
