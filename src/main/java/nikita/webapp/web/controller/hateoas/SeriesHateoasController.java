package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.*;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.ICaseFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.ISeriesHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.*;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_SERIES,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@Api(value = "SeriesController", description = "Contains CRUD operations for Series. Create operations are only for " +
        "entities that can be associated with a series e.g. File / ClassificationSystem. Update and delete operations" +
        " are on individual series entities identified by systemId. Read operations are either on individual series" +
        "entities or pageable iterable sets of series")
public class SeriesHateoasController
        extends NoarkController {

    private IClassificationSystemService classificationSystemService;
    private ISeriesService seriesService;
    private ICaseFileService caseFileService;
    private IFileService fileService;
    private IRecordService recordService;
    private ISeriesHateoasHandler seriesHateoasHandler;
    private ICaseFileHateoasHandler caseFileHateoasHandler;
    private IFileHateoasHandler fileHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public SeriesHateoasController(IClassificationSystemService classificationSystemService,
                                   ISeriesService seriesService,
                                   ICaseFileService caseFileService,
                                   ISeriesHateoasHandler seriesHateoasHandler,
                                   ICaseFileHateoasHandler caseFileHateoasHandler,
                                   IFileHateoasHandler fileHateoasHandler,
                                   IFileService fileService,
                                   IRecordService recordService,
                                   ApplicationEventPublisher applicationEventPublisher) {

        this.classificationSystemService = classificationSystemService;
        this.seriesService = seriesService;
        this.caseFileService = caseFileService;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.caseFileHateoasHandler = caseFileHateoasHandler;
        this.fileHateoasHandler = fileHateoasHandler;
        this.fileService = fileService;
        this.recordService = recordService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)


    // Create a new file
    // POST [contextPath][api]/arkivstruktur/arkivdel/ny-mappe/
    @ApiOperation(value = "Persists a File object associated with the given Series systemId", notes = "Returns the " +
            "newly created file object after it was associated with a Series object and persisted to the database",
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

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASSIFICATION_SYSTEM,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassificationSystemHateoas>
    createClassificationSystemAssociatedWithSeries(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of series to associate the " +
                            "ClassificationSystem with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "ClassificationSystem",
                    value = "Incoming ClassificationSystem object",
                    required = true)
            @RequestBody ClassificationSystem classificationSystem)
            throws NikitaException {
        validateForCreate(classificationSystem);
        ClassificationSystemHateoas classificationSystemHateoas =
                seriesService.createClassificationSystem(systemID,
                        classificationSystem);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(classificationSystemHateoas.getEntityVersion().toString())
                .body(classificationSystemHateoas);
    }


    // Create a new file
    // POST [contextPath][api]/arkivstruktur/arkivdel/ny-mappe/
    @ApiOperation(value = "Persists a File object associated with the given Series systemId", notes = "Returns the " +
            "newly created file object after it was associated with a Series object and persisted to the database",
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

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_FILE,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FileHateoas> createFileAssociatedWithSeries(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of series to associate the caseFile with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "File",
                    value = "Incoming file object",
                    required = true)
            @RequestBody File file) throws NikitaException {
        validateForCreate(file);
        File createdFile = seriesService.createFileAssociatedWithSeries(systemID, file);
        FileHateoas fileHateoas = new FileHateoas(createdFile);
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, createdFile));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdFile.getVersion().toString())
                .body(fileHateoas);
    }

    // Create a new casefile
    // POST [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-saksmappe/
    // This currently is not supported in the standard, but probably will be later
    @ApiOperation(value = "Persists a CaseFile object associated with the given Series systemId", notes = "Returns " +
            "the newly created caseFile object after it was associated with a Series object and persisted to " +
            "the database", response = CaseFileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 201, message = "File " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type CaseFile"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CASE_FILE,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CaseFileHateoas> createCaseFileAssociatedWithSeries(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of series to associate the caseFile with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "caseFile",
                    value = "Incoming caseFile object",
                    required = true)
            @RequestBody CaseFile caseFile) throws NikitaException {
        validateForCreate(caseFile);
        CaseFile createdCaseFile = seriesService.createCaseFileAssociatedWithSeries(systemID, caseFile);
        CaseFileHateoas caseFileHateoas = new CaseFileHateoas(createdCaseFile);
        caseFileHateoasHandler.addLinks(caseFileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, createdCaseFile));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdCaseFile.getVersion().toString())
                .body(caseFileHateoas);
    }

    // Create a new record
    // POST [contextPath][api]/arkivstruktur/arkivdel/ny-registrering/
    @ApiOperation(value = "Persists a Record object associated with the given Series systemId", notes = "Returns the " +
            "newly created record object after it was associated with a Series object and persisted to the database",
            response = RecordHateoas.class)
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
    public ResponseEntity<String> createRecordAssociatedWithSeries(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of series to associate the record with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "Record",
                    value = "Incoming record object",
                    required = true)
            @RequestBody Record record) throws NikitaException {
        //  validateForCreate(record);
        //RecordHateoas recordHateoas = new RecordHateoas(seriesService.createRecordAssociatedWithSeries(systemID, record));
        //recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        // applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
        //  return ResponseEntity.status(CREATED)
        //.eTag(createdRecord.getVersion().toString())
        //.body(recordHateoas);
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // API - All PUT Requests (CRUD - UPDATE)

    // Associate ClassificationSystem to identified Series
    // PUT [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-klassifikasjonssystem/
    @ApiOperation(value = "Associates a ClassificationSystem with a Series", notes = "Association can only occur if "
            + "nothing (record, file) has been associated with the Series", response = ClassificationSystemHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ClassificationSystem " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassificationSystemHateoas.class),
            @ApiResponse(code = 201, message = "ClassificationSystem " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ClassificationSystemHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type ClassificationSystem"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CLASSIFICATION_SYSTEM,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> associateSeriesWithClassificationSystem(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "The systemId of the Series",
                    required = true)
            @PathVariable String classificationSystemSuccessorSystemId,
            @ApiParam(name = "id",
                    value = "Address of the ClassificationSystem to associate",
                    required = true)
            @RequestParam StringBuffer id) throws NikitaException {
        //String classificationSystemPrecursorSystemId =
        //      handleResolutionOfIncomingURLInternalGetSystemId(id);
//        ClassificationSystemHateoas classificationSystemHateoas = new
//                ClassificationSystemHateoas(classificationSystemService.associateClassificationSystemWithClassificationSystemSuccessor(classificationSystemSystemId, caseFile));
//        classificationSystemHateoasHandler.addLinks(classificationSystemHateoas, new Authorisation());
//        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, ));
//   return ResponseEntity.status(CREATED)
//                .eTag(classificationSystem.getVersion().toString())
//                .body(classificationSystemHateoas);
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, NOT_IMPLEMENTED);
    }


    // Update an identified Series
    // PUT [contextPath][api]/arkivstruktur/arkivdel/{systemId}
    @ApiOperation(value = "Updates a Series object", notes = "Returns the newly" +
            " update Series object after it is persisted to the database", response = SeriesHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Series " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SeriesHateoas.class),
            @ApiResponse(code = 201, message = "Series " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = SeriesHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type ClassificationSystem"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SeriesHateoas> updateSeries(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of fonds to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "series",
                    value = "Incoming series object",
                    required = true)
            @RequestBody Series series) throws NikitaException {
        validateForUpdate(series);
        Series updatedSeries = seriesService.handleUpdate(systemID, parseETAG(request.getHeader(ETAG)), series);
        SeriesHateoas seriesHateoas = new SeriesHateoas(updatedSeries);
        seriesHateoasHandler.addLinks(seriesHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, updatedSeries));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedSeries.getVersion().toString())
                .body(seriesHateoas);
    }
    // API - All GET Requests (CRUD - READ)

    // Retrieve a Series given a systemId
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/
    @ApiOperation(value = "Retrieves a single Series entity identified " +
            "by the given a systemId", response = SeriesHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Series returned",
                    response = SeriesHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<SeriesHateoas> findOneSeriesBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return seriesService.findBySystemId(systemID);
    }

    // Create a ClassificationSystem object with default values
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-klassifikasjonssystem/
    @ApiOperation(value = "Create a ClassificationSystem with default values", response = ClassificationSystem.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ClassificationSystem returned", response = File.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASSIFICATION_SYSTEM)
    public ResponseEntity<ClassificationSystemHateoas> createClassificationSystem(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classificationSystemService.generateDefaultClassificationSystem());
    }

    // Create a File object with default values
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-mappe/
    @ApiOperation(value = "Create a File with default values", response = File.class)
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

    // Create a CaseFile object with default values
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-saksmappe/
    @ApiOperation(value = "Create a CaseFile with default values", response = CaseFile.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CaseFile returned", response = CaseFile.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CASE_FILE)
    public ResponseEntity<CaseFileHateoas> createDefaultCaseFile(
            HttpServletRequest request) {


        CaseFileHateoas caseFileHateoas =
                caseFileService.generateDefaultCaseFile();

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(caseFileHateoas);
    }

    // Create a Record with default values
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-registrering
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
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultRecord());
    }

    // Retrieve all Series (paginated)
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/klassifikasjonssystem/
    @ApiOperation(value = "Retrieves multiple Series entities limited by ownership rights", notes = "The field skip" +
            "tells how many Series rows of the result set to ignore (starting at 0), while  top tells how many rows" +
            " after skip to return. Note if the value of top is greater than system value " +
            " nikita-noark5-core.pagination.maxPageSize, then nikita-noark5-core.pagination.maxPageSize is used. ",
            response = SeriesHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Series list found",
                    response = SeriesHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping
    public ResponseEntity<SeriesHateoas> findAllSeries() {
        return seriesService.findAll();
    }

    // Retrieve all Records associated with a Series (paginated)
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/registrering/
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/registrering/?top=5&skip=1
    @ApiOperation(value = "Retrieves a lit of Records associated with a Series", notes = "The field skip" +
            "tells how many Record rows of the result set to ignore (starting at 0), while  top tells how many rows" +
            " after skip to return. Note if the value of top is greater than system value " +
            " nikita-noark5-core.pagination.maxPageSize, then nikita-noark5-core.pagination.maxPageSize is used. ",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record list found",
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + RECORD)
    public ResponseEntity<RecordHateoas> findAllRecordAssociatedWithSeries(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the series to find associated records",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return seriesService.findAllRecordAssociatedWithSeries(systemID);
    }

    // Retrieve all Files associated with a Series (paginated)
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/mappe/
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/mappe/?top=5&skip=1
    @ApiOperation(value = "Retrieves a list of Files associated with a Series",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File list found",
                    response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code =
                    500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FILE)
    public ResponseEntity<FileHateoas> findAllFileAssociatedWithSeries(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return seriesService.findAllFileAssociatedWithSeries(systemID);
    }

    // Retrieve all ClassificationSystem associated with Series identified by a
    // systemId
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/klassifikasjonsystem
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klassifikasjonsystem/
    @ApiOperation(value = "Retrieves a single ClassificationSystem that is " +
            "the parent of the Series entity identified by systemId",
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the Series ",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return seriesService.findClassificationSystemAssociatedWithSeries(
                systemID);
    }

    // Retrieve the Fonds associated with the Series identified by the given
    // systemId
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/arkiv
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/
    @ApiOperation(value = "Retrieves a single Fonds that is " +
            "the parent of the Series entity identified by systemId",
            response = FondsHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Fonds returned",
                    response = FondsHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FONDS)
    public ResponseEntity<FondsHateoas> findParentFondsAssociatedWithSeries(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the Series ",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return seriesService.findFondsAssociatedWithSeries(systemID);
    }


    // Retrieve all CaseFiles associated with a Series (paginated)
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/saksmappe/
    @ApiOperation(value = "Retrieves a list of CaseFiles associated with a Series", notes = "The field skip" +
            "tells how many CaseFile rows of the result set to ignore (starting at 0), while  top tells how many rows" +
            " after skip to return. Note if the value of top is greater than system value " +
            " nikita-noark5-core.pagination.maxPageSize, then nikita-noark5-core.pagination.maxPageSize is used. ",
            response = CaseFileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CaseFile list found",
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CASE_FILE)
    public ResponseEntity<CaseFileHateoas> findAllCaseFileAssociatedWithCaseFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return seriesService.findCaseFilesBySeries(systemID);
    }

    // API - All DELETE Requests (CRUD - DELETE)

    // Delete a Series identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/arkivdel/{systemId}/
    @ApiOperation(value = "Deletes a single Series entity identified by " +
            SYSTEM_ID, response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted Series",
                    response = String.class),
            @ApiResponse(code = 401, message =
                    API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message =
                    API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteSeriesBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the series to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        seriesService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Delete all Series
    // DELETE [contextPath][api]/arkivstruktur/arkivdel/
    @ApiOperation(value = "Deletes all Series", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all Series",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<String> deleteAllSeries() {
        seriesService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
