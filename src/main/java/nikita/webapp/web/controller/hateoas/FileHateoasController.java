package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.ICrossReferenceEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_FILE,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class FileHateoasController
        extends NoarkController {

    private IFileService fileService;
    private IFileHateoasHandler fileHateoasHandler;
    private IRecordService recordService;
    private IRecordHateoasHandler recordHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public FileHateoasController(
            IFileService fileService,
            IFileHateoasHandler fileHateoasHandler,
            IRecordService recordService,
            IRecordHateoasHandler recordHateoasHandler,
            ApplicationEventPublisher applicationEventPublisher) {
        this.fileService = fileService;
        this.fileHateoasHandler = fileHateoasHandler;
        this.recordService = recordService;
        this.recordHateoasHandler = recordHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Create a Record
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-registrering
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-registrering/
    @ApiOperation(value = "Persists a Record associated with the given Series systemId",
            notes = "Returns the newly created record after it was associated with a File and " +
                    "persisted to the database", response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201, message = "Record " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Record"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_RECORD,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RecordHateoas> createRecordAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = "fileSystemId",
                    value = "systemId of file to associate the record with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Record",
                    value = "Incoming record",
                    required = true)
            @RequestBody Record record) throws NikitaException {
        return fileService.createRecordAssociatedWithFile(systemID, record);
    }

    // Create a CrossReference
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-kryssreferanse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-kryssreferanse/
    @ApiOperation(value = "Persists a Record associated with the given Series systemId",
            notes = "Returns the newly created Record after it was associated with a File and " +
                    "persisted to the database", response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201, message = "Record " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Record"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CROSS_REFERENCE,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> createCrossReferenceAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to associate the Record with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "crossReferenceEntity",
                    value = "Noark entity that support cross reference functionality",
                    required = true)
            @RequestBody ICrossReferenceEntity crossReferenceEntity) throws NikitaException {

        // applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
        //return ResponseEntity.status(HttpStatus.CREATED)
        //        .eTag(crossReference.getVersion().toString())
        //        .body(crossReferenceHateoas);
        // Think about how to handle if cross reference is to Record or class. Do we need
        // to specify this in the URL
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                             API_MESSAGE_NOT_IMPLEMENTED);
    }

    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-mappe
    // REL: https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-mappe/
    @ApiOperation(value = "Persists a File object associated with the " +
            "(other) given File systemId", notes = "Returns the newly " +
            "created file object after it was associated with a file" +
            "object and persisted to the database",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                         message = "File " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                         response = File.class),
            @ApiResponse(code = 201,
                         message = "File " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                         response = File.class),
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
    public ResponseEntity<FileHateoas> createSubFileAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = "systemId",
                      value = "systemId of parent file",
                      required = true)
            @PathVariable String systemId,
            @ApiParam(name = "file",
                      value = "Incoming file object",
                      required = true)
            @RequestBody File file)
            throws NikitaException {
        FileHateoas fileHateoas = fileService.
                createFileAssociatedWithFile(systemId, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fileHateoas.getEntityVersion().toString())
                .body(fileHateoas);
    }

    // Add a Comment to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-merknad/
    @ApiOperation(value = "Associates a Comment with a File identified by systemID",
            notes = "Returns the comment", response = CommentHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = COMMENT + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CommentHateoas.class),
            @ApiResponse(code = 201, message = COMMENT + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CommentHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + COMMENT),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_COMMENT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CommentHateoas> addCommentToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the Comment with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Comment",
                    value = "comment",
                    required = true)
            @RequestBody Comment comment) throws NikitaException {
        CommentHateoas commentHateoas =
                fileService.createCommentAssociatedWithFile(
                        systemID, comment);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(commentHateoas.getEntityVersion().toString())
                .body(commentHateoas);
    }

    // Add a Class to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-klasse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-klasse/
    @ApiOperation(value = "Associates a Class with a File identified by systemID",
            notes = "Returns the File with the Class associated with it", response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = CLASS + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201, message = CLASS + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + CLASS),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASS,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> addClassToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the Class with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "klass",
                    value = "Class",
                    required = true)
            @RequestBody Class klass) throws NikitaException {
        // applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
        //        return ResponseEntity.status(HttpStatus.CREATED)
//                .eTag(createdClass.getVersion().toString())
//                .body(classHateoas);
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                             API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Add a reference to a secondary Series associated with the File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-referanseArkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-referanseArkivdel/
    @ApiOperation(value = "Associates a Secondary Series with a File identified by systemID",
            notes = "Returns the File after the secondary Series is successfully associated with it." +
                    "Note a secondary series allows a File to be associated with another Series.",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = CLASS + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201, message = CLASS + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + CLASS),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_REFERENCE_SERIES,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> addReferenceSeriesToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the secondary Series with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Series",
                    value = "series",
                    required = true)
            @RequestBody Series series) throws NikitaException {
        //TODO: What do we return here? File ? maybe just 200 OK
        // applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
        //return ResponseEntity.status(HttpStatus.CREATED)
        //        .eTag(file.getVersion().toString())
        //       .body(fileHateoas);
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                             API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Add a secondary Class to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-sekundaerklassifikasjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-sekundaerklassifikasjon/
    @ApiOperation(value = "Associates a Class with a File identified by systemID as secondary Class",
            notes = "Returns the File with the Class associated with it. Note a File can only have one Class " +
                    "associated with it, but can have multiple secondary Class associated with it. An example" +
                    "is the use of K-Koder on case-handling and a secondary classification of person",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = COMMENT + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201, message = COMMENT + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + CLASS),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SECONDARY_CLASSIFICATION,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> addReferenceToSecondaryClassToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the secondary Class with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "klass",
                    value = "Class",
                    required = true)
            @RequestBody Class klass) throws NikitaException {
        // applicationEventPublisher.publishEvent(new AfterNoa  rkEntityCreatedEvent(this, ));
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                             API_MESSAGE_NOT_IMPLEMENTED);
    }

    // API - All GET Requests (CRUD - READ)

    // Retrieve all Records associated with File identified by systemId
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/registrering
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/
    @ApiOperation(value = "Retrieve all Record associated with a File identified by systemId",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record returned", response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + RECORD)
    public ResponseEntity<RecordHateoas> findAllRecordsAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {

        File file = fileService.findBySystemId(systemID);
        if (file == null) {
            throw new NoarkEntityNotFoundException(
                    "Could not find File object with systemID " + systemID);
        }
        RecordHateoas recordHateoas = new
                RecordHateoas((List<INoarkEntity>)
                (List) file.getReferenceRecord());
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-mappe/
    @ApiOperation(value = "Return a template File with default values",
                  response = File.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File returned", response = File.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_FILE)
    public ResponseEntity<FileHateoas> createDefaultFile(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.generateDefaultFile());
    }

    // Create a suggested PartUnit (like a template) object 
    // with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-partenhet
    @ApiOperation(value = "Suggests the contents of a new Part " +
            "object", notes = "Returns a pre-filled Part object" +
            " with values relevant for the logged-in user",
            response = PartUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_UNIT)
    public ResponseEntity<PartUnitHateoas>
    getPartUnitTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.
                        generateDefaultPartUnit(systemID));
    }

    // Create a suggested PartPerson (like a template) object 
    // with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-partenhet
    @ApiOperation(value = "Suggests the contents of a new Part " +
            "object", notes = "Returns a pre-filled Part object" +
            " with values relevant for the logged-in user",
            response = PartPersonHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_PERSON)
    public ResponseEntity<PartPersonHateoas>
    getPartPersonTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.
                        generateDefaultPartPerson(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/part
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/part/
    @ApiOperation(value = "Retrieves a list of Part associated with a File",
            response = PartHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Part returned",
                    response = PartHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + PART)
    public ResponseEntity<PartHateoas>
    findAllPartAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated File",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.getPartAssociatedWithFile(systemID));
    }

    // Retrieve all Series associated with File identified by a systemId
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/arkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/
    @ApiOperation(value = "Retrieves a single Series entity given a systemId",
            response = SeriesHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Series returned", response = SeriesHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SERIES)
    public ResponseEntity<SeriesHateoas> findParentSeriesByFileSystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return fileService.findSeriesAssociatedWithFile(systemID);
    }

    // Retrieve all Class associated with File identified by a systemId
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/klasse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/
    @ApiOperation(value = "Retrieves a single Class entity given a systemId",
            response = ClassHateoas.class)
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
    public ResponseEntity<ClassHateoas> findParentClassByFileSystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the class to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return fileService.findClassAssociatedWithFile(systemID);
    }


    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/undermappe
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/undermappe/
    @ApiOperation(value = "Retrieves all children associated with identified " +
            "file", response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File children found",
                         response = FileHateoas.class),
            @ApiResponse(code = 401,
                         message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                         message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                         message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SUB_FILE)
    public ResponseEntity<FileHateoas> findAllSubFileAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                      value = "systemID of parent File",
                      required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        FileHateoas fileHateoas = fileService.findAllChildren(systemID);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/nasjonalidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/nasjonalidentifikator/
    @ApiOperation(value = "Retrieves a list of NationalIdentifier associated with a File",
                  response = NationalIdentifierHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "NationalIdentifier returned",
                    response = NationalIdentifierHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NATIONAL_IDENTIFIER)
    public ResponseEntity<NationalIdentifierHateoas>
    findAllNIAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated File",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.getNationalIdentifierAssociatedWithFile(systemID));
    }

    // Create a Record with default values
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-registrering
    @ApiOperation(value = "Create a Record with default values", response = Record.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record returned", response = Record.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_RECORD)
    public ResponseEntity<RecordHateoas> createDefaultRecord(
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultRecord());
    }

    // Retrieve a file identified by a systemId
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}
    @ApiOperation(value = "Retrieves a single File entity given a systemId", response = File.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File returned", response = File.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<FileHateoas> findOneFileBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        File file = fileService.findBySystemId(systemID);
        // TODO: If null return not found exception
        FileHateoas fileHateoas = new FileHateoas(file);
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(file.getVersion().toString())
                .body(fileHateoas);
    }

    // Retrieves all files
    // GET [contextPath][api]/arkivstruktur/mappe
    @ApiOperation(value = "Retrieves multiple File entities limited by ownership rights", notes = "The field skip" +
            "tells how many File rows of the result set to ignore (starting at 0), while  top tells how many rows" +
            " after skip to return. Note if the value of top is greater than system value " +
            " nikita-noark5-core.pagination.maxPageSize, then nikita-noark5-core.pagination.maxPageSize is used. ",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File list found",
                    response = FileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping
    public ResponseEntity<FileHateoas> findAllFiles(
            HttpServletRequest request) {

        String ownedBy = SecurityContextHolder.getContext().
                getAuthentication().getName();
        FileHateoas fileHateoas = new
                    FileHateoas((List<INoarkEntity>) (List)
                    fileService.findByOwnedBy(ownedBy));

        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileHateoas);
    }

    // Create a Comment with default values
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-merknad
    @ApiOperation(value = "Create a Comment with default values", response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment returned", response = Comment.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_COMMENT)
    public ResponseEntity<CommentHateoas> createDefaultComment(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.generateDefaultComment());
    }

    // Retrieve all Comments associated with a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/merknad/
    @ApiOperation(value = "Retrieves all Comments associated with a File identified by a systemId",
            response = CommentHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File returned", response = FileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + COMMENT)
    public ResponseEntity<CommentHateoas> findAllCommentsAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve comments for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.getCommentAssociatedWithFile(systemID));
    }

    // Retrieve all CrossReference associated with a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/kryssreferanse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/kryssreferanse/
    @ApiOperation(value = "Retrieves all CrossReference associated with a File identified by a systemId",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File returned", response = FileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CROSS_REFERENCE)
    public ResponseEntity<String> findAllCrossReferenceAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the File to retrieve CrossReferences for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                             API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Retrieve all Class associated with a File as secondary classification
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/sekundaerklassifikasjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/sekundaerklassifikasjon/
    @ApiOperation(value = "Retrieves all secondary Class associated with a File identified by a systemId",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File returned", response = FileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SECONDARY_CLASSIFICATION)
    public ResponseEntity<String> findSecondaryClassAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the File to retrieve secondary Class for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        //return ResponseEntity.status(HttpStatus.CREATED)
        //        .eTag(klass.getVersion().toString())
        //        .body(classHateoas);
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                             API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Retrieve all secondary Series associated with a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/referanseArkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/referanseArkivdel/
    @ApiOperation(value = "Retrieves all secondary Series associated with a File identified by a systemId",
            response = SeriesHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Series returned", response = SeriesHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + REFERENCE_SERIES)
    public ResponseEntity<String> findSecondarySeriesAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the File to retrieve secondary Class for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Add a Building to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-bygning
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-bygning/
    @ApiOperation(value = "Associates a Building (national identifier) with a" +
            " File identified by systemID", notes = "Returns the File with " +
            "the building associated with it", response = BuildingHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = BUILDING + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = BuildingHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_BUILDING)
    public ResponseEntity<BuildingHateoas> getNIBuildingToFileTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the Building with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.generateDefaultBuilding());
    }

    // Add a DNumber to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-dnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-dnummer/
    @ApiOperation(value = "Associates a DNumber (national identifier) with a" +
            " File identified by systemID", notes = "Returns the File with " +
            "the dNumber associated with it", response = DNumberHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = D_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DNumberHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_D_NUMBER)
    public ResponseEntity<DNumberHateoas> getNIDNumberToFileTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the DNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.generateDefaultDNumber());
    }

    // Add a SocialSecurityNumber to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-foedselsnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-foedselsnummer/
    @ApiOperation(value = "Associates a SocialSecurityNumber (national " +
            "identifier) with a File identified by systemID",
            notes = "Returns the File with the socialSecurityNumber " +
                    "associated with it",
            response = SocialSecurityNumberHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SocialSecurityNumberHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SOCIAL_SECURITY_NUMBER)
    public ResponseEntity<SocialSecurityNumberHateoas> getNISocialSecurityNumberToFileTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the " +
                            "SocialSecurityNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.generateDefaultSocialSecurityNumber());
    }

    // Add a CadastralUnit to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-matrikkel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-matrikkel/
    @ApiOperation(value = "Associates a CadastralUnit (national identifier) " +
            "with a File identified by systemID",
            notes = "Returns the File with the cadastralUnit associated with" +
                    " it", response = CadastralUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CadastralUnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CADASTRAL_UNIT)
    public ResponseEntity<CadastralUnitHateoas> getNICadastralUnitToFileTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the CadastralUnit " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.generateDefaultCadastralUnit());
    }

    // Add a Position to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-posisjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-posisjon/
    @ApiOperation(value = "Associates a Position (national identifier) with a" +
            " File identified by systemID",
            notes = "Returns the File with the position associated with it",
            response = PositionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = POSITION +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PositionHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_POSITION)
    public ResponseEntity<PositionHateoas> getNIPositionToFileTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the " +
                            "Position with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.generateDefaultPosition());
    }

    // Add a Plan to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-plan
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-plan/
    @ApiOperation(value = "Associates a Plan (national " +
            "identifier) with a File identified by systemID",
            notes = "Returns the File with  the plan " +
                    "associated with it",
            response = PlanHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = PLAN +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PlanHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PLAN)
    public ResponseEntity<PlanHateoas> getNIPlanToFileTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the " +
                            "Plan with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.generateDefaultPlan());
    }

    // Add a Unit to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-enhetsidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-enhetsidentifikator/
    @ApiOperation(value = "Associates a Unit (national identifier) with a " +
            "File identified by systemID",
            notes = "Returns the File with the unit associated with it",
            response = UnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = NI_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = UnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_NI_UNIT)
    public ResponseEntity<UnitHateoas> getNIUnitToFileTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the " +
                            "Unit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fileService.generateDefaultUnit());
    }

    // API - All PUT Requests (CRUD - UPDATE)

    // Update a File with given values
    // PUT [contextPath][api]/arkivstruktur/mappe/{systemId}
    @ApiOperation(value = "Updates a File identified by a given systemId", notes = "Returns the newly updated file",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201, message = "File " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type File"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FileHateoas> updateFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "File",
                    value = "Incoming file object",
                    required = true)
            @RequestBody File file) throws NikitaException {
        validateForUpdate(file);

        File updatedFile = fileService.handleUpdate(systemID, parseETAG(request.getHeader(ETAG)), file);
        FileHateoas fileHateoas = new FileHateoas(updatedFile);
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, updatedFile));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedFile.getVersion().toString())
                .body(fileHateoas);
    }

    // Finalise a File
    // PUT [contextPath][api]/arkivstruktur/mappe/{systemId}/avslutt-mappe
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/avslutt-mappe/
    @ApiOperation(value = "Updates a File identified by a given systemId", notes = "Returns the newly updated file",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201, message = "File " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type File"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FILE_END,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> finaliseFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) throws NikitaException {

        /*
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, ));
        FileHateoas fileHateoas = new FileHateoas(fileService.updateFile(systemID, file));
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return new ResponseEntity<>(fileHateoas, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .eTag(finalisedFile.getVersion().toString())
                .body(fileHateoas);*/
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                             API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Expand a File to a CaseFile
    // PUT [contextPath][api]/arkivstruktur/mappe/{systemId}/utvid-til-saksmappe
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/utvid-til-saksmappe/
    @ApiOperation(value = "Expands a File identified by a systemId to a CaseFile", notes = "Returns the newly updated " +
            "CaseFile",
            response = CaseFileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CaseFile " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 201, message = "CaseFile " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type File"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FILE_EXPAND_TO_CASE_FILE,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> expandFileToCaseFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to expand",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) throws NikitaException {
        /*
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, ));
        FileHateoas fileHateoas = new FileHateoas(fileService.updateFile(systemID, file));
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return new ResponseEntity<>(fileHateoas, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .eTag(expandedFile.getVersion().toString())
                .body(caseFileHateoas);
        */
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                             API_MESSAGE_NOT_IMPLEMENTED);
    }


    // Create a new PartUnit and associate it with the given file
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-partenhet
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-partenhet/
    @ApiOperation(value = "Persists a PartUnit object " +
            "associated with the given File systemId",
            notes = "Returns the newly created PartUnit object " +
                    "after it was associated with a File object and " +
                    "persisted to the database",
            response = PartUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PartUnit " +
                    API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 201, message = "PartUnit " +
                    API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type PartUnit"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_UNIT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartUnitHateoas>
    createPartUnitAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to associate the " +
                            "PartUnit with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "PartUnit",
                    value = "Incoming PartUnit object",
                    required = true)
            @RequestBody PartUnit partUnit)
            throws NikitaException {

        PartUnitHateoas partUnitHateoas =
                fileService.createPartUnitAssociatedWithFile(
                        systemID, partUnit);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partUnitHateoas.getEntityVersion().toString())
                .body(partUnitHateoas);
    }

    // Create a new PartPerson and associate it with the given file
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-partenhet
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-partenhet/
    @ApiOperation(value = "Persists a PartPerson object " +
            "associated with the given File systemId",
            notes = "Returns the newly created PartPerson object " +
                    "after it was associated with a File object and " +
                    "persisted to the database",
            response = PartPersonHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PartPerson " +
                    API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 201, message = "PartPerson " +
                    API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type PartPerson"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_PERSON,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartPersonHateoas>
    createPartPersonAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to associate the " +
                            "PartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "PartPerson",
                    value = "Incoming PartPerson object",
                    required = true)
            @RequestBody PartPerson partPerson)
            throws NikitaException {

        PartPersonHateoas partPersonHateoas =
                fileService.createPartPersonAssociatedWithFile(
                        systemID, partPerson);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partPersonHateoas.getEntityVersion().toString())
                .body(partPersonHateoas);
    }

    // Add a Building to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-bygning
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-bygning/
    @ApiOperation(value = "Associates a Building (national identifier) with a" +
            " File identified by systemID", notes = "Returns the File with " +
            "the building associated with it", response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = BUILDING + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201,
                    message = BUILDING +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_BUILDING,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<BuildingHateoas> addNIBuildingToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the Building with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Building",
                    value = "building",
                    required = true)
            @RequestBody Building building) throws NikitaException {
        BuildingHateoas buildingHateoas =
                fileService.createBuildingAssociatedWithFile(
                        systemID, building);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(buildingHateoas.getEntityVersion().toString())
                .body(buildingHateoas);
    }

    // Add a DNumber to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-dnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-dnummer/
    @ApiOperation(value = "Associates a DNumber (national identifier) with a" +
            " File identified by systemID", notes = "Returns the File with " +
            "the dNumber associated with it", response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = D_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201,
                    message = D_NUMBER +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_D_NUMBER,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DNumberHateoas> addNIDNumberToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the DNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "DNumber",
                    value = "dNumber",
                    required = true)
            @RequestBody DNumber dNumber)
            throws NikitaException {
        DNumberHateoas dNumberHateoas =
                fileService.createDNumberAssociatedWithFile(
                        systemID, dNumber);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(dNumberHateoas.getEntityVersion().toString())
                .body(dNumberHateoas);
    }

    // Add a SocialSecurityNumber to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-foedselsnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-foedselsnummer/
    @ApiOperation(value = "Associates a SocialSecurityNumber (national " +
            "identifier) with a File identified by systemID",
            notes = "Returns the File with  the socialSecurityNumber " +
                    "associated with it",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201,
                    message = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SOCIAL_SECURITY_NUMBER,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SocialSecurityNumberHateoas> addNISocialSecurityNumberToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the " +
                            "SocialSecurityNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "SocialSecurityNumber",
                    value = "socialSecurityNumber",
                    required = true)
            @RequestBody SocialSecurityNumber socialSecurityNumber)
            throws NikitaException {
        SocialSecurityNumberHateoas socialSecurityNumberHateoas =
                fileService.createSocialSecurityNumberAssociatedWithFile(
                        systemID, socialSecurityNumber);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(socialSecurityNumberHateoas.getEntityVersion().toString())
                .body(socialSecurityNumberHateoas);
    }

    // Add a CadastralUnit to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-matrikkel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-matrikkel/
    @ApiOperation(value = "Associates a CadastralUnit (national identifier) " +
            "with a File identified by systemID",
            notes = "Returns the File with the cadastralUnit associated with" +
                    " it", response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201,
                    message = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CADASTRAL_UNIT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CadastralUnitHateoas> addNICadastralUnitToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the CadastralUnit " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "CadastralUnit",
                    value = "cadastralUnit",
                    required = true)
            @RequestBody CadastralUnit cadastralUnit)
            throws NikitaException {
        CadastralUnitHateoas cadastralUnitHateoas =
                fileService.createCadastralUnitAssociatedWithFile(
                        systemID, cadastralUnit);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(cadastralUnitHateoas.getEntityVersion().toString())
                .body(cadastralUnitHateoas);
    }

    // Add a Position to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-posisjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-posisjon/
    @ApiOperation(value = "Associates a Position (national identifier) with a" +
            " File identified by systemID",
            notes = "Returns the File with the position associated with it",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = POSITION +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201,
                    message = POSITION +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_POSITION,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PositionHateoas> addNIPositionToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the " +
                            "Position with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Position",
                    value = "position",
                    required = true)
            @RequestBody Position position)
            throws NikitaException {
        PositionHateoas positionHateoas =
                fileService.createPositionAssociatedWithFile(
                        systemID, position);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(positionHateoas.getEntityVersion().toString())
                .body(positionHateoas);
    }

    // Add a Plan to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-plan
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-plan/
    @ApiOperation(value = "Associates a Plan (national " +
            "identifier) with a File identified by systemID",
            notes = "Returns the File with  the plan " +
                    "associated with it",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = PLAN +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201,
                    message = PLAN +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PLAN,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PlanHateoas> addNIPlanToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the " +
                            "Plan with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Plan",
                    value = "plan",
                    required = true)
            @RequestBody Plan plan)
            throws NikitaException {
        PlanHateoas planHateoas =
                fileService.createPlanAssociatedWithFile(
                        systemID, plan);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(planHateoas.getEntityVersion().toString())
                .body(planHateoas);
    }

    // Add a Unit to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-enhetsidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-enhetsidentifikator/
    @ApiOperation(value = "Associates a Unit (national identifier) with a " +
            "File identified by systemID",
            notes = "Returns the File with the unit associated with it",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = NI_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FileHateoas.class),
            @ApiResponse(code = 201,
                    message = NI_UNIT +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_NI_UNIT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<UnitHateoas> addNIUnitToFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of File to associate the " +
                            "Unit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Unit",
                    value = "unit",
                    required = true)
            @RequestBody Unit unit)
            throws NikitaException {
        UnitHateoas unitHateoas =
                fileService.createUnitAssociatedWithFile(
                        systemID, unit);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(unitHateoas.getEntityVersion().toString())
                .body(unitHateoas);
    }

    // Expand a File to a MeetingFile
    // PUT [contextPath][api]/arkivstruktur/mappe/{systemId}/utvid-til-moetemappe
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/utvid-til-moetemappe/
    // TODO: At implementation time, we are missing MeetingFileHateoas. Leaving as CaseFileHateoas
    // just to allow continued compilation
    @ApiOperation(value = "Expands a File identified by a systemId to a MeetingFile", notes = "Returns the newly " +
            "updated MeetingFile. Note TODO in FileHateoasController. Fix this before swagger is published",
            response = CaseFileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CaseFile " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 201, message = "CaseFile " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type File"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FILE_EXPAND_TO_MEETING_FILE,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> expandFileToMeetingFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to expand",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) throws NikitaException {
        /* applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, ));
        FileHateoas fileHateoas = new FileHateoas(fileService.updateFile(systemID, file));
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return new ResponseEntity<>(fileHateoas, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .eTag(expandedFile.getVersion().toString())
                .body(meetingFileHateoas);
        */
        return errorResponse(HttpStatus.NOT_IMPLEMENTED,
                             API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Delete a File identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/mappe/{systemId}/
    @ApiOperation(value = "Deletes a single File entity identified by systemID",
            response = HateoasNoarkObject.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "File deleted",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteFileBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        File file = fileService.findBySystemId(systemID);
        fileService.deleteEntity(systemID);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, file));
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete all File
    // DELETE [contextPath][api]/arkivstruktur/mappe/
    @ApiOperation(value = "Deletes all File", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all File",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<String> deleteAllFile() {
        fileService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}

/*
Properties check
public void checkForObligatoryFileValues(File file) {

    if (file.getFileId() == null) {
        throw new NikitaMalformedInputDataException("The mappe you tried to create is malformed. The "
                + "mappeID field is mandatory, and you have submitted an empty value.");
    }
}

 */
