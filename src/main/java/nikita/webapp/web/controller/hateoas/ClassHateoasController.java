package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.IClassService;
import nikita.webapp.service.interfaces.IRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_CLASS,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class ClassHateoasController
        extends NoarkController {

    private IClassService classService;
    private IRecordService recordService;

    public ClassHateoasController(IClassService classService,
				  IRecordService recordService) {
        this.classService = classService;
        this.recordService = recordService;
    }

    // API - All POST Requests (CRUD - CREATE)

    // POST [contextPath][api]/arkivstruktur/klasse/{systemID}/ny-underklasse
    @ApiOperation(value = "Persists a Class object associated with the " +
            "(other) given Class systemId", notes = "Returns the newly " +
            "created class object after it was associated with a class" +
            "object and persisted to the database",
            response = ClassHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Class " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = Class.class),
            @ApiResponse(code = 201,
                    message = "Class " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASS,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassHateoas>
    createClassAssociatedWithClass(
            HttpServletRequest request,
            @ApiParam(name = "systemID", value = "systemID of class to " +
                    "associate the klass with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "klass",
                    value = "Incoming class object",
                    required = true)
            @RequestBody Class klass)
            throws NikitaException {
        ClassHateoas classHateoas = classService.
                createClassAssociatedWithClass(systemID

                        , klass);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(classHateoas.getEntityVersion().toString())
                .body(classHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/klasse/{systemID}/ny-mappe
    @ApiOperation(value = "Persists a File object associated with the " +
            "Class systemId", notes = "Returns the newly " +
            "created file object after it was associated with a class" +
            "object and persisted to the database",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "File " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201,
                    message = "File " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type File"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_FILE,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FileHateoas>
    createFileAssociatedWithClass(
            HttpServletRequest request,
            @ApiParam(name = "systemId",
                    value = "systemId of Class to associate " +
                            "the file with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "file",
                    value = "Incoming file object",
                    required = true)
            @RequestBody File file)
            throws NikitaException {
        FileHateoas fileHateoas = classService.
                createFileAssociatedWithClass(systemID, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fileHateoas.getEntityVersion().toString())
                .body(fileHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/klasse/{systemID}/ny-saksmappe
    @ApiOperation(value = "Persists a CaseFile object associated with the " +
            "Class systemId", notes = "Returns the newly " +
            "created caseFile object after it was associated with a class" +
            "object and persisted to the database",
            response = CaseFileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "CaseFile " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 201,
                    message = "CaseFile " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type CaseFile"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CASE_FILE,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CaseFileHateoas>
    createCaseCaseFileAssociatedWithClass(
            HttpServletRequest request,
            @ApiParam(name = "systemId",
                    value = "systemId of Class to associate " +
                            "the caseFile with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "caseFile",
                    value = "Incoming caseFile object",
                    required = true)
            @RequestBody CaseFile caseFile)
            throws NikitaException {
        CaseFileHateoas caseFileHateoas = classService.
                createCaseFileAssociatedWithClass(systemID, caseFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(caseFileHateoas.getEntityVersion().toString())
                .body(caseFileHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/klasse/{systemID}/ny-registrering
    @ApiOperation(value = "Persists a Record object associated with the " +
            "Class systemId", notes = "Returns the newly " +
            "created record object after it was associated with a class " +
            "object and persisted to the database",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Record " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201,
                    message = "Record " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Record"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_RECORD,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RecordHateoas>
    createRecordAssociatedWithClass(
            @ApiParam(name = "systemId",
                    value = "systemId of Class to associate " +
                            "the record with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "record",
                    value = "Incoming record object",
                    required = true)
            @RequestBody Record record)
            throws NikitaException {
        return classService.
                createRecordAssociatedWithClass(systemID, record);
    }

    // API - All GET Requests (CRUD - READ)

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<ClassHateoas> findOne(
            HttpServletRequest request,
            @ApiParam(name = "systemId",
                    value = "systemId of class to retrieve.",
                    required = true)
            @PathVariable("systemID") final String classSystemId) {
        ClassHateoas classHateoas = classService.findSingleClass(classSystemId);
        return ResponseEntity.status(OK)
                .eTag(classHateoas.getEntityVersion().toString())
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classHateoas);
    }

    @ApiOperation(value = "Retrieves multiple Class entities limited by " +
            "ownership rights",
            response = ClassHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Class list found",
                    response = ClassHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping
    public ResponseEntity<ClassHateoas> findAllClass(
            HttpServletRequest request) {
        String ownedBy = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        ClassHateoas classHateoas = classService.findAll(ownedBy);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classHateoas);
    }

    @ApiOperation(value = "Retrieves all children associated with identified " +
            "class", response = ClassHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Class children found",
                    response = ClassHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SUB_CLASS)
    public ResponseEntity<ClassHateoas> findAllChildrenClass(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the class to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        ClassHateoas classHateoas = classService.findAllChildren(systemID);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classHateoas);
    }

    // Retrieve all ClassificationSystem associated with Class identified by a
    // systemId
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/klassifikasjonsystem
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klassifikasjonsystem/
    @ApiOperation(value = "Retrieves a single ClassificationSystem that is " +
            "the parent of the Class entity identified by systemId",
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CLASSIFICATION_SYSTEM)
    public ResponseEntity<ClassificationSystemHateoas>
    findParentClassificationSystemByFileSystemId(
            @ApiParam(name = "systemID",
                    value = "systemID of the classificationSystem to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return classService.findClassificationSystemAssociatedWithClass(
                systemID);
    }

    // Retrieve all Class associated with Class identified by a systemId
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/klasse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/
    @ApiOperation(value = "Retrieves a single Class that is  the parent of " +
            "the Class entity identified by systemId",
            response = ClassificationSystemHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Class returned", response = ClassHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CLASS)
    public ResponseEntity<ClassHateoas> findParentClassByClassSystemId(
            @ApiParam(name = "systemID",
                    value = "systemID of the class to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return classService.findClassAssociatedWithClass(systemID);
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASS)
    public ResponseEntity<ClassHateoas> createDefaultClass(
            HttpServletRequest request,
            @ApiParam(
                    name = "systemID",
                    value = "systemId of Class to associate Class with.",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classService.generateDefaultSubClass(systemID));
    }

    // Create a Record with default values
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-registrering
    @ApiOperation(value = "Create a Record with default values", response = Record.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record returned", response = Record.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_RECORD)
    public ResponseEntity<RecordHateoas> createDefaultRecord(
            final UriComponentsBuilder uriBuilder,
            HttpServletRequest request,
            final HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultRecord());
    }

    // Delete a Class identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/klasse/{systemId}/
    @ApiOperation(value = "Deletes a single Class entity identified by systemID",
            response = HateoasNoarkObject.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Parent entity (ClassificationSystem or Class) " +
                            "returned",
                    response = HateoasNoarkObject.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteClass(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the class to delete",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        classService.deleteEntity(systemID);
        return ResponseEntity.status(HttpStatus.OK)
                .body("{\"status\" : \"Success\"}");
    }

    // Delete all Class
    // DELETE [contextPath][api]/arkivstruktur/klasse/
    @ApiOperation(value = "Deletes all Class", response = Count.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all Class",
                    response = Count.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<Count> deleteAllClass() {
        return ResponseEntity.status(NO_CONTENT).
                body(new Count(classService.deleteAllByOwnedBy()));
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a Class
    // PUT [contextPath][api]/arkivstruktur/klasse/{systemID}
    @ApiOperation(value = "Updates a Class object", notes = "Returns the " +
            "newly updated Class object after it is persisted to the database",
            response = ClassHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Class " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassHateoas.class),
            @ApiResponse(code = 201,
                    message = "Class " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ClassHateoas.class),
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
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassHateoas> updateClass(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of class to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @ApiParam(name = "class",
                    value = "Incoming class object",
                    required = true)
            @RequestBody Class klass)
            throws NikitaException {
        validateForUpdate(klass);
        ClassHateoas classHateoas = classService.handleUpdate(systemID,
                parseETAG(request.getHeader(ETAG)), klass);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(classHateoas.getEntityVersion().toString())
                .body(classHateoas);
    }
}
