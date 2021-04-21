package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.IClassificationSystemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class ClassificationSystemHateoasController
        extends NoarkController {

    private final IClassificationSystemService classificationSystemService;

    public ClassificationSystemHateoasController(
            IClassificationSystemService classificationSystemService) {
        this.classificationSystemService = classificationSystemService;
    }

    // API - All POST Requests (CRUD - CREATE)

    @Operation(summary = "Persists a ClassificationSystem object",
            description = "Returns the newly created classificationSystem " +
                    "object after it was persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ClassificationSystem " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "ClassificationSystem " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type ClassificationSystem"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = NEW_CLASSIFICATION_SYSTEM,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassificationSystemHateoas>
    createClassificationSystem(
            HttpServletRequest request,
            @Parameter(name = "classificationSystem",
                    description = "Incoming classificationSystem object",
                    required = true)
            @RequestBody ClassificationSystem classificationSystem)
            throws NikitaException {
        ClassificationSystemHateoas classificationSystemHateoas =
                classificationSystemService.
                        save(classificationSystem);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(classificationSystem.getVersion().toString())
                .body(classificationSystemHateoas);
    }

    @Operation(summary = "Persists a Class object associated with the given" +
            " ClassificationSystem systemId",
            description = "Returns the newly created class object after it " +
                    "was associated with a classificationSystem object and " +
                    "persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description =
                            "Class " + API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description =
                            "Class " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Class"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER
            + SLASH + NEW_CLASS,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassHateoas>
    createClassAssociatedWithClassificationSystem(
            HttpServletRequest request,
            @Parameter(name = "systemID",
                    description = "systemID of classificationSystem to " +
                            "associate the class with.",
                    required = true)
            @PathVariable String systemID,
            @Parameter(name = "klass",
                    description = "Incoming class object",
                    required = true)
            @RequestBody Class klass) throws NikitaException {
        ClassHateoas classHateoas =
                classificationSystemService.
                        createClassAssociatedWithClassificationSystem(
                                systemID, klass);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(klass.getVersion().toString())
                .body(classHateoas);
    }

    // API - All GET Requests (CRUD - READ)

    @GetMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<ClassificationSystemHateoas> findOne(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of classificationSystem to " +
                            "retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        ClassificationSystemHateoas classificationSystemHateoas =
                classificationSystemService.
                        findSingleClassificationSystem(systemID);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(classificationSystemHateoas.getEntityVersion().toString())
                .body(classificationSystemHateoas);
    }


    // Retrieve all Series associated with the ClassificationSystem
    // identified by the given systemId
    // GET [contextPath][api]/arkivstruktur/klassifikasjonssystem/{systemId}/arkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/
    @Operation(summary = "Retrieves a a list of Series that are parents of " +
            " the ClassificationSystem entity identified by systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ClassificationSystem returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = CLASSIFICATION_SYSTEM + SLASH +
            SYSTEM_ID_PARAMETER + SLASH + SERIES)
    public ResponseEntity<SeriesHateoas>
    findParentClassificationSystemByFileSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the classificationSystem",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return classificationSystemService.
                findSeriesAssociatedWithClassificationSystem(systemID);
    }

    @Operation(summary = "Retrieves multiple ClassificationSystem entities " +
            "limited by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ClassificationSystem list found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = CLASSIFICATION_SYSTEM)
    public ResponseEntity<ClassificationSystemHateoas>
    findAllClassificationSystem(
            HttpServletRequest request) {
        ClassificationSystemHateoas classificationSystemHateoas =
                classificationSystemService.findAllClassificationSystem();
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationSystemHateoas);
    }

    // Return a Class object with default values
    //GET [contextPath][api]/arkivstruktur/klassifikasjonssystem/{systemId}/klasse
    @Operation(
            summary = "Retrieves the class's associated with a " +
                    "ClassificationSystem identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Class returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = CLASSIFICATION_SYSTEM + SLASH +
            SYSTEM_ID_PARAMETER + SLASH + CLASS)
    public ResponseEntity<ClassHateoas>
    findClassAssociatedWithClassificationSystem(
            HttpServletRequest request,
            @Parameter(
                    name = SYSTEM_ID,
                    description = "systemID of ClassificationSystem you want " +
                            "retrieve associated  Class objects for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationSystemService.
                        findAllClassAssociatedWithClassificationSystem(
                                systemID));
    }

    // Return a Class object with default values
    //GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-klasse
    @Operation(
            summary = "Create a Class with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Class returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})

    @GetMapping(value = CLASSIFICATION_SYSTEM + SLASH +
            SYSTEM_ID_PARAMETER + SLASH + NEW_CLASS)
    public ResponseEntity<ClassHateoas> createDefaultClass(
            HttpServletRequest request,
            @Parameter(
                    name = SYSTEM_ID,
                    description = "systemID of Class to associate Class with.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationSystemService.generateDefaultClass(systemID));
    }

    // Delete a ClassificationSystem identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/klassifikasjonssystem/{systemId}/
    @Operation(summary = "Deletes a single ClassificationSystem entity " +
            "identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "ClassificationSystem deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String>
    deleteClassificationSystemBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the ClassificationSystem to " +
                            "delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        classificationSystemService.deleteClassificationSystem(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(DELETE_RESPONSE);
    }


    // Delete all ClassificationSystem
    // DELETE [contextPath][api]/arkivstruktur/klassifikasjonssystem/
    @Operation(summary = "Deletes all ClassificationSystem")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "All ClassificationSystem deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = CLASSIFICATION_SYSTEM)
    public ResponseEntity<String> deleteAllClassificationSystem() {
        classificationSystemService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a ClassificationSystem
    // PUT [contextPath][api]/arkivstruktur/klassifikasjonssystem/{systemID}
    @Operation(summary = "Updates a ClassificationSystem object",
            description = "Returns the newly updated ClassificationSystem " +
                    "object after it is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ClassificationSystem " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "ClassificationSystem persisted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type ClassificationSystem"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = CLASSIFICATION_SYSTEM + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassificationSystemHateoas>
    updateClassificationSystem(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of classificationSystem to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "classificationSystem",
                    description = "Incoming classificationSystem object",
                    required = true)
            @RequestBody ClassificationSystem classificationSystem)
            throws NikitaException {
        validateForUpdate(classificationSystem);
        ClassificationSystemHateoas classificationSystemHateoas =
                classificationSystemService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)),
                        classificationSystem);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(classificationSystemHateoas.getEntityVersion().toString())
                .body(classificationSystemHateoas);
    }
}
