package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.*;
import nikita.webapp.util.error.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_SERIES,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
@Tag(name = "SeriesController",
        description = "Contains CRUD operations for " +
                "Series. Create operations are only for entities that can be " +
                "associated with a series e.g. File / ClassificationSystem. Update " +
                "and delete operations are on individual series entities identified " +
                "by systemId. Read operations are either on individual series" +
                "entities or pageable iterable sets of series")
public class SeriesHateoasController
        extends NoarkController {

    private final IClassificationSystemService classificationSystemService;
    private final ISeriesService seriesService;
    private final ICaseFileService caseFileService;
    private final IFileService fileService;
    private final IRecordService recordService;

    public SeriesHateoasController(
            IClassificationSystemService classificationSystemService,
            ISeriesService seriesService,
            ICaseFileService caseFileService,
            IFileService fileService,
            IRecordService recordService) {
        this.classificationSystemService = classificationSystemService;
        this.seriesService = seriesService;
        this.caseFileService = caseFileService;
        this.fileService = fileService;
        this.recordService = recordService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Create a new file
    // POST [contextPath][api]/arkivstruktur/arkivdel/ny-klassifikasjonsystem/
    @Operation(summary = "Persists a File object associated with the given " +
            "Series systemId",
            description = "Returns the newly created file object after it was" +
                    " associated with a Series object and persisted to the " +
                    "database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "File " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "File " +
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
                            " of type File"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASSIFICATION_SYSTEM,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassificationSystemHateoas>
    createClassificationSystemAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of series to associate the " +
                            "ClassificationSystem with",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "ClassificationSystem",
                    description = "Incoming ClassificationSystem object",
                    required = true)
            @RequestBody ClassificationSystem classificationSystem)
            throws NikitaException {
        validateForCreate(classificationSystem);
        ClassificationSystemHateoas classificationSystemHateoas =
                seriesService.createClassificationSystem(systemID,
                        classificationSystem);
        return ResponseEntity.status(CREATED).body(classificationSystemHateoas);
    }

    // Create a new file
    // POST [contextPath][api]/arkivstruktur/arkivdel/ny-mappe/
    @Operation(summary = "Persists a File object associated with the given Series systemId",
            description = "Returns the " +
                    "newly created file object after it was associated with a Series object and persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "File " + API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "File " +
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
                            " of type File"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_FILE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FileHateoas> createFileAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of series to associate the " +
                            "caseFile with",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "File",
                    description = "Incoming file object",
                    required = true)
            @RequestBody File file) throws NikitaException {
        validateForCreate(file);
        return ResponseEntity.status(CREATED)
                .body(seriesService
                        .createFileAssociatedWithSeries(systemID, file));
    }

    // Create a new casefile
    // POST [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-saksmappe/
    // This currently is not supported in the standard, but probably will be later
    @Operation(summary = "Persists a CaseFile object associated with the given Series systemId",
            description = "Returns " +
                    "the newly created caseFile object after it was associated with a Series object and persisted to " +
                    "the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "File " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "File " +
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
                            " of type CaseFile"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CASE_FILE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CaseFileHateoas> createCaseFileAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of series to associate the " +
                            "caseFile with",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "caseFile",
                    description = "Incoming caseFile object",
                    required = true)
            @RequestBody CaseFile caseFile) throws NikitaException {
        validateForCreate(caseFile);
        return ResponseEntity.status(CREATED)
                .body(seriesService
                        .createCaseFileAssociatedWithSeries(systemID, caseFile));
    }

    // Create a new record
    // POST [contextPath][api]/arkivstruktur/arkivdel/ny-registrering/
    @Operation(summary = "Persists a Record object associated with the given Series systemId",
            description = "Returns the " +
                    "newly created record object after it was associated with a Series object and persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Record " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Record " +
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
                            " of type Record"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_RECORD,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ApiError> createRecordAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of series to associate the record" +
                            " with",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "Record",
                    description = "Incoming record object",
                    required = true)
            @RequestBody Record record) throws NikitaException {
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Create a default StorageLocation
    // POST [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-oppbevaringssted
    @Operation(summary = "Get a default StorageLocation object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "StorageLocation returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value =
            SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_STORAGE_LOCATION)
    public ResponseEntity<StorageLocationHateoas> createStorageLocation(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "StorageLocation",
                    description = "Incoming storageLocation",
                    required = true)
            @RequestBody StorageLocation storageLocation) throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(seriesService
                        .createStorageLocationAssociatedWithSeries(
                                systemID, storageLocation));
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Associate ClassificationSystem to identified Series
    // PUT [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-klassifikasjonssystem/
    @Operation(summary = "Associates a ClassificationSystem with a Series",
            description = "Association can only occur if "
                    + "nothing (record, file) has been associated with the Series")
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
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CLASSIFICATION_SYSTEM,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> associateSeriesWithClassificationSystem(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Series",
                    required = true)
            @PathVariable String systemID,
            @Parameter(name = "id",
                    description = "Address of the ClassificationSystem to associate",
                    required = true)
            @RequestParam StringBuffer id) throws NikitaException {
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, NOT_IMPLEMENTED);
    }

    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/skjermingmetadata/
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/skjermingmetadata/
    @Operation(summary = "Create a ScreeningMetadata associated with a File " +
            "identified by the given systemId",
            description = "Returns the newly updated ScreeningMetadata")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "ScreeningMetadata " +
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
                            " of type ScreeningMetadata"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_SCREENING_METADATA,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ScreeningMetadataHateoas>
    createScreeningMetadataBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of File to associate " +
                            "ScreeningMetadata with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "ScreeningMetadata",
                    description = "Incoming ScreeningMetadata object",
                    required = true)
            @RequestBody final Metadata screeningMetadata)
            throws NikitaException {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                seriesService.createScreeningMetadataAssociatedWithSeries(
                        systemID, screeningMetadata);
        return ResponseEntity.status(CREATED)
                .body(screeningMetadataHateoas);
    }


    // Update an identified Series
    // PUT [contextPath][api]/arkivstruktur/arkivdel/{systemId}
    @Operation(summary = "Updates a Series object",
            description = "Returns the newly update Series object after it is" +
                    " persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Series " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Series " +
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
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SeriesHateoas> updateSeries(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of fonds to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "series",
                    description = "Incoming series object",
                    required = true)
            @RequestBody Series series) throws NikitaException {
        validateForUpdate(series);
        return ResponseEntity.status(OK)
                .body(seriesService
                        .handleUpdate(systemID, parseETAG(
                                request.getHeader(ETAG)), series));
    }
    // API - All GET Requests (CRUD - READ)

    // Retrieve a Series given a systemId
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/
    @Operation(summary = "Retrieves a single Series entity identified " +
            "by the given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Series returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<SeriesHateoas> findOneSeriesBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService.findBySystemId(systemID));
    }


    // Retrieve all ScreeningMetadata associated with the Screening of a Series
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/skjermingmetadata
    @Operation(summary = "Retrieves all ScreeningMetadata associated with the" +
            " Screening object of a Series")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value =
            SLASH + SYSTEM_ID_PARAMETER + SLASH + SCREENING_METADATA)
    public ResponseEntity<ScreeningMetadataHateoas>
    getScreeningMetadataAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to retrieve " +
                            "screening metadata",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService
                        .getScreeningMetadataAssociatedWithSeries(systemID));
    }

    // Create a ClassificationSystem object with default values
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-klassifikasjonssystem/
    @Operation(summary = "Create a ClassificationSystem with default values")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASSIFICATION_SYSTEM)
    public ResponseEntity<ClassificationSystemHateoas>
    createClassificationSystem() {
        return ResponseEntity.status(OK)
                .body(classificationSystemService.generateDefaultClassificationSystem());
    }


    // Create a default ScreeningMetadata
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-skjermingmetadata
    @Operation(summary = "Get a default ScreeningMetadata object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value =
            SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SCREENING_METADATA)
    public ResponseEntity<ScreeningMetadataHateoas>
    getDefaultScreeningMetadata(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService.getDefaultScreeningMetadata(systemID));
    }

    // Retrieve all StorageLocation associated with a Series
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/oppbevaringssted
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/oppbevaringssted/
    @Operation(summary = "Retrieves all StorageLocation associated with a Series")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "StorageLocation returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + STORAGE_LOCATION)
    public ResponseEntity<StorageLocationHateoas>
    findStorageLocationAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to retrieve " +
                            "StorageLocation",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService.findStorageLocationAssociatedWithSeries(systemID));
    }

    // Create a default StorageLocation
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-oppbevaringssted
    @Operation(summary = "Get a default StorageLocation object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "StorageLocation returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value =
            SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_STORAGE_LOCATION)
    public ResponseEntity<StorageLocationHateoas>
    getDefaultStorageLocation(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService.getDefaultStorageLocation(systemID));
    }

    // Create a File object with default values
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-mappe/
    @Operation(summary = "Create a File with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "File returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_FILE)
    public ResponseEntity<FileHateoas> createDefaultFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Series to create default File " +
                            "for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultFile(systemID));
    }

    // Create a CaseFile object with default values
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-saksmappe/
    @Operation(summary = "Create a CaseFile with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CaseFile returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CASE_FILE)
    public ResponseEntity<CaseFileHateoas> createDefaultCaseFile() {
        return ResponseEntity.status(OK)
                .body(caseFileService.generateDefaultCaseFile());
    }

    // Create a Record with default values
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/ny-registrering
    @Operation(summary = "Create a Record with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Record returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_RECORD)
    public ResponseEntity<RecordHateoas> createDefaultRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Series to create default " +
                            "Record for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultRecord(systemID));
    }

    // Retrieve all Series (paginated)
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/klassifikasjonssystem/
    @Operation(summary = "Retrieves multiple Series entities limited by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Series list found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping
    public ResponseEntity<SeriesHateoas> findAllSeries() {
        return ResponseEntity.status(OK)
                .body(seriesService.findAll());
    }

    // Retrieve all Records associated with a Series (paginated)
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/registrering/
    @Operation(summary = "Retrieves a lit of Records associated with a Series")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Record list found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + RECORD)
    public ResponseEntity<RecordHateoas> findAllRecordAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to find associated records",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService.findAllRecordAssociatedWithSeries(systemID));
    }

    // Retrieve all Files associated with a Series (paginated)
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/mappe/
    @Operation(summary = "Retrieves a list of Files associated with a Series")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "File list found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FILE)
    public ResponseEntity<FileHateoas> findAllFileAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService.findAllFileAssociatedWithSeries(systemID));
    }

    // Retrieve all ClassificationSystem associated with Series identified by a
    // systemId
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/klassifikasjonsystem
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klassifikasjonsystem/
    @Operation(summary = "Retrieves a single ClassificationSystem that is " +
            "the parent of the Series entity identified by systemId")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CLASSIFICATION_SYSTEM)
    public ResponseEntity<ClassificationSystemHateoas>
    findParentClassificationSystemByFileSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Series ",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService
                        .findClassificationSystemAssociatedWithSeries(systemID));
    }

    // Retrieve the Fonds associated with the Series identified by the given
    // systemId
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/arkiv
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/
    @Operation(summary = "Retrieves a single Fonds that is " +
            "the parent of the Series entity identified by systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Fonds returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FONDS)
    public ResponseEntity<FondsHateoas> findParentFondsAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Series ",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService.findFondsAssociatedWithSeries(systemID));
    }


    // Retrieve all CaseFiles associated with a Series (paginated)
    // GET [contextPath][api]/arkivstruktur/arkivdel/{systemId}/saksmappe/
    @Operation(summary = "Retrieves a list of CaseFiles associated with a Series")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CaseFile list found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CASE_FILE)
    public ResponseEntity<CaseFileHateoas> findAllCaseFileAssociatedWithCaseFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(seriesService.findCaseFilesBySeries(systemID));
    }

    // API - All DELETE Requests (CRUD - DELETE)

    // Delete a Series identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/arkivdel/{systemId}/
    @Operation(summary = "Deletes a single Series entity identified by " +
            SYSTEM_ID)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted Series"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteSeriesBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        seriesService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Delete all Series
    // DELETE [contextPath][api]/arkivstruktur/arkivdel/
    @Operation(summary = "Deletes all Series")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all Series"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping
    public ResponseEntity<String> deleteAllSeries() {
        seriesService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
