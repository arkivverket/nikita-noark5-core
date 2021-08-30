package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileExpansionHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.IRecordService;
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
@RequestMapping(value = HREF_BASE_FILE,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class FileHateoasController
        extends NoarkController {

    private final IFileService fileService;
    private final IRecordService recordService;

    public FileHateoasController(
            IFileService fileService,
            IRecordService recordService) {
        this.fileService = fileService;
        this.recordService = recordService;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Create a Record
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-registrering
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-registrering/
    @Operation(summary = "Persists a Record associated with the given Series " +
            "systemID",
            description = "Returns the newly created record after it was " +
                    "associated with a File and persisted to the database")
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
    public ResponseEntity<RecordHateoas> createRecordAssociatedWithFile(
            @Parameter(name = "fileSystemId",
                    description = "systemID of file to associate the record with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Record",
                    description = "Incoming record",
                    required = true)
            @RequestBody RecordEntity record) throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(fileService
                        .createRecordAssociatedWithFile(systemID, record));
    }

    // API - All GET Requests (CRUD - READ)

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
                fileService.createScreeningMetadataAssociatedWithFile(
                        systemID, screeningMetadata);
        return ResponseEntity.status(CREATED)
                .body(screeningMetadataHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-mappe
    // REL: https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-mappe/
    @Operation(summary = "Persists a File object associated with the " +
            "(other) given File systemId",
            description = "Returns the newly " +
                    "created file object after it was associated with a file" +
                    "object and persisted to the database")
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_FILE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FileHateoas> createSubFileAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of parent file",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "file",
                    description = "Incoming file object",
                    required = true)
            @RequestBody File file)
            throws NikitaException {
        FileHateoas fileHateoas = fileService.
                createFileAssociatedWithFile(systemID, file);
        return ResponseEntity.status(CREATED)
                .body(fileHateoas);
    }

    // Add a Comment to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-merknad/
    @Operation(summary = "Associates a Comment with a File identified by systemID",
            description = "Returns the comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = COMMENT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = COMMENT +
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
                            " of type " + COMMENT),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_COMMENT,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CommentHateoas> addCommentToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Comment " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Comment",
                    description = "comment",
                    required = true)
            @RequestBody Comment comment) throws NikitaException {
        CommentHateoas commentHateoas =
                fileService.createCommentAssociatedWithFile(
                        systemID, comment);
        return ResponseEntity.status(CREATED)
                .body(commentHateoas);
    }

    // Add a Keyword to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-noekkelord
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-noekkelord/
    @Operation(summary = "Associates a Keyword with a File identified by systemID",
            description = "Returns the Keyword")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = KEYWORD +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = KEYWORD +
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
                            " of type " + KEYWORD),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_KEYWORD,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<KeywordHateoas> addKeywordToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Keyword " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "keyword",
                    description = "Keyword",
                    required = true)
            @RequestBody Keyword keyword) throws NikitaException {
        KeywordHateoas keywordHateoas =
                fileService.createKeywordAssociatedWithFile(
                        systemID, keyword);
        return ResponseEntity.status(CREATED)
                .body(keywordHateoas);
    }

    // Add a Class to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-klasse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-klasse/
    @Operation(summary = "Associates a Class with a File identified by systemID",
            description = "Returns the File with the Class associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = CLASS + API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = CLASS +
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
                            " of type " + CLASS),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASS,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    @SuppressWarnings("unused")
    public ResponseEntity<ApiError> addClassToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Class " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "klass",
                    description = "Class",
                    required = true)
            @RequestBody Class klass) throws NikitaException {
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Add a reference to a secondary Series associated with the File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-referanseArkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-referanseArkivdel/
    @Operation(summary = "Associates a secondary Series with a File " +
            "identified by systemID",
            description = "Returns the File after the secondary Series is " +
                    "successfully associated with it. Note a secondary series" +
                    " allows a File to be associated with another Series.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = CLASS + API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = CLASS +
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
                            " of type " + CLASS),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_REFERENCE_SERIES,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    @SuppressWarnings("unused")
    public ResponseEntity<ApiError> addReferenceSeriesToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the " +
                            "secondary Series with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Series",
                    description = "series",
                    required = true)
            @RequestBody Series series) throws NikitaException {
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Add a secondary Class to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-sekundaerklassifikasjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-sekundaerklassifikasjon/
    @Operation(summary = "Associates a Class  ith a File identified by " +
            "systemID as secondary Class",
            description = "Returns the File with the Class associated with it" +
                    ". Note a File can only have one Class associated with " +
                    "it, but can have multiple secondary Class associated " +
                    "with it. An example is the use of K-Koder on " +
                    "case-handling and a secondary classification of person")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = COMMENT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = COMMENT +
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
                            " of type " + CLASS),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_SECONDARY_CLASSIFICATION,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    @SuppressWarnings("unused")
    public ResponseEntity<ApiError> addReferenceToSecondaryClassToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the " +
                            "secondary Class with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "klass",
                    description = "Class",
                    required = true)
            @RequestBody Class klass) throws NikitaException {
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Create a CrossReference
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-kryssreferanse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-kryssreferanse/
    @Operation(summary = "Create a CrossReference associated with a File " +
            "identified by the given systemId",
            description = "Returns the newly updated CrossReference")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CrossReference " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "CrossReference " +
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
                            " of type CrossReference"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_CROSS_REFERENCE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CrossReferenceHateoas>
    createCrossReferenceBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of File to associate " +
                            "CrossReference with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CrossReference",
                    description = "Incoming CrossReference object",
                    required = true)
            @RequestBody CrossReference crossReference)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(fileService
                        .createCrossReferenceAssociatedWithFile(
                                systemID, crossReference));
    }

    // API - All GET Requests (CRUD - READ)

    // Retrieve all Records associated with File identified by systemId
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/registrering
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/
    @Operation(summary = "Retrieve all Record associated with a File identified by systemId")
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

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + RECORD)
    public ResponseEntity<RecordHateoas> findAllRecordsAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.findAllRecords(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-mappe/
    @Operation(summary = "Return a template File with default values")
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
                    description = "systemID of File to create default File " +
                            "for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultFile(systemID));
    }

    // Create a suggested PartUnit (like a template) object
    // with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-partenhet
    @Operation(summary = "Suggests the contents of a new Part object",
            description = "Returns a pre-filled Part object with values " +
                    "relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_UNIT)
    public ResponseEntity<PartUnitHateoas>
    getPartUnitTemplate(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.
                        generateDefaultPartUnit(systemID));
    }

    // Create a suggested PartPerson (like a template) object
    // with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-partenhet
    @Operation(summary = "Suggests the contents of a new Part object",
            description = "Returns a pre-filled Part object" +
                    " with values relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_PERSON)
    public ResponseEntity<PartPersonHateoas>
    getPartPersonTemplate(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.
                        generateDefaultPartPerson(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/part
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/part/
    @Operation(summary = "Retrieves a list of Part associated with a File")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Part returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + PART)
    public ResponseEntity<PartHateoas>
    findAllPartAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve associated File",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {

        return ResponseEntity.status(OK)
                .body(fileService.getPartAssociatedWithFile(systemID));
    }

    // Retrieve all Series associated with File identified by a systemId
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/arkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/
    @Operation(summary = "Retrieves a single Series entity given a systemId")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SERIES)
    public ResponseEntity<SeriesHateoas> findParentSeriesByFileSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.findSeriesAssociatedWithFile(systemID));
    }

    // Retrieve all Class associated with File identified by a systemId
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/klasse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/
    @Operation(summary = "Retrieves a single Class entity given a systemId")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CLASS)
    public ResponseEntity<ClassHateoas> findParentClassByFileSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the class to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.findClassAssociatedWithFile(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/undermappe
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/undermappe/
    @Operation(summary = "Retrieves all children associated with identified " +
            "file")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "File children found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SUB_FILE)
    public ResponseEntity<FileHateoas> findAllSubFileAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of parent File",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        FileHateoas fileHateoas = fileService.findAllChildren(systemID);
        return ResponseEntity.status(OK)
                .body(fileHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/nasjonalidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/nasjonalidentifikator/
    @Operation(summary = "Retrieves a list of NationalIdentifier associated " +
            "with a File")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "NationalIdentifier returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NATIONAL_IDENTIFIER)
    public ResponseEntity<NationalIdentifierHateoas>
    findAllNIAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated File",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {

        return ResponseEntity.status(OK)
                .body(fileService
                        .getNationalIdentifierAssociatedWithFile(systemID));
    }

    // Create a Record with default values
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-registrering
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
                    description = "systemID of File to create default " +
                            "Record for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID
    ) {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultRecord(systemID));
    }

    // Retrieve a file identified by a systemId
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}
    @Operation(summary = "Retrieves a single File entity given a systemId")
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

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<FileHateoas> findOneFileBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.findBySystemId(systemID));
    }

    // Retrieves all files
    // GET [contextPath][api]/arkivstruktur/mappe
    @Operation(summary = "Retrieves multiple File entities limited by " +
            "ownership rights")
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
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})

    @GetMapping
    public ResponseEntity<FileHateoas> findAllFiles() {
        return ResponseEntity.status(OK)
                .body(fileService.findAll());
    }

    // Retrieve all ScreeningMetadata associated with the Screening of a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/skjermingmetadata
    @Operation(summary = "Retrieves all ScreeningMetadata associated with the" +
            " Screening object of a File")
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
    getScreeningMetadataAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve screening" +
                            " metadata",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService
                        .getScreeningMetadataAssociatedWithFile(systemID));
    }

    // Retrieve all Keyword associated with a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/noekkelord
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/noekkelord/
    @Operation(summary = "Retrieves all Keyword associated with a File")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Keyword returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + KEYWORD)
    public ResponseEntity<KeywordHateoas> getKeywordAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve keyword",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.findKeywordAssociatedWithFile(systemID));
    }

    // Retrieve all StorageLocation associated with a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/oppbevaringssted
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/oppbevaringssted/
    @Operation(summary = "Retrieves all StorageLocation associated with a File")
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
    getStorageLocationAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "StorageLocation",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.getStorageLocationAssociatedWithFile(systemID));
    }

    // Create a Keyword with default values
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-noekkelord
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-noekkelord/
    @Operation(summary = "Create a Keyword with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Keyword returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_KEYWORD)
    public ResponseEntity<KeywordTemplateHateoas> createDefaultKeyword(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to create default " +
                            "Keyword for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultKeyword(systemID));
    }

    // Create a Comment with default values
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-merknad
    @Operation(summary = "Create a Comment with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Comment returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_COMMENT)
    public ResponseEntity<CommentHateoas> createDefaultComment(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to create default " +
                            "Keyword for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultComment(systemID));
    }

    // Get a CrossReference template
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-kryssreferanse
    @Operation(summary = "Get a default CrossReference")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CrossReference returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CROSS_REFERENCE)
    public ResponseEntity<CrossReferenceHateoas> getDefaultCrossReference(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to create default " +
                            "CrossReference for",
                    required = true)
            @PathVariable UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.getDefaultCrossReference(systemID));
    }

    // Create a default ScreeningMetadata
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-skjermingmetadata
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
                    description = "systemID of the file",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService
                        .getDefaultScreeningMetadata(systemID));
    }

    // Retrieve all Comments associated with a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/merknad/
    @Operation(summary = "Retrieves all Comments associated with a File " +
            "identified by a systemId")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + COMMENT)
    public ResponseEntity<CommentHateoas> findAllCommentsAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve comments " +
                            "for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.getCommentAssociatedWithFile(systemID));
    }

    // Retrieve all CrossReference associated with a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/kryssreferanse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/kryssreferanse/
    @Operation(summary = "Retrieves all CrossReference associated with a File" +
            " identified by a systemId")
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

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CROSS_REFERENCE)
    public ResponseEntity<CrossReferenceHateoas>
    findAllCrossReferenceAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the File to retrieve " +
                            "CrossReferences for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService
                        .findCrossReferenceAssociatedWithFile(systemID));
    }

    // Retrieve all Class associated with a File as secondary classification
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/sekundaerklassifikasjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/sekundaerklassifikasjon/
    @Operation(summary = "Retrieves all secondary Class associated with a " +
            "File identified by a systemId")
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

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            SECONDARY_CLASSIFICATION)
    public ResponseEntity<ApiError> findSecondaryClassAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the File to retrieve secondary" +
                            " Class for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Retrieve all secondary Series associated with a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/referanseArkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/referanseArkivdel/
    @Operation(summary = "Retrieves all secondary Series associated with a " +
            "File identified by a systemId")
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

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            REFERENCE_SERIES)
    public ResponseEntity<ApiError> findSecondarySeriesAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the File to retrieve secondary" +
                            " Class for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return errorResponse(NOT_IMPLEMENTED,
                API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Add a Building to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-bygning
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-bygning/
    @Operation(summary = "Associates a Building (national identifier) with a " +
            "File identified by systemID",
            description = "Returns the File with the building associated with" +
                    " it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = BUILDING +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_BUILDING)
    public ResponseEntity<BuildingHateoas> getNIBuildingToFileTemplate(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Building" +
                            " with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultBuilding(systemID));
    }

    // Add a DNumber to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-dnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-dnummer/
    @Operation(summary = "Associates a DNumber (national identifier) with a " +
            "File identified by systemID",
            description = "Returns the File with the dNumber associated with " +
                    "it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = D_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_D_NUMBER)
    public ResponseEntity<DNumberHateoas> getNIDNumberToFileTemplate(
            @Parameter(
                    name = SYSTEM_ID,
                    description = "systemID of File to associate the DNumber " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultDNumber(systemID));
    }

    // Add a SocialSecurityNumber to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-foedselsnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-foedselsnummer/
    @Operation(summary = "Associates a SocialSecurityNumber (national " +
            "identifier) with a File identified by systemID",
            description = "Returns the File with the socialSecurityNumber " +
                    "associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_SOCIAL_SECURITY_NUMBER)
    public ResponseEntity<SocialSecurityNumberHateoas>
    getNISocialSecurityNumberToFileTemplate(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the " +
                            "SocialSecurityNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultSocialSecurityNumber(systemID));
    }

    // Add a CadastralUnit to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-matrikkel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-matrikkel/
    @Operation(summary = "Associates a CadastralUnit (national identifier) " +
            "with a File identified by systemID",
            description = "Returns the File with the cadastralUnit associated" +
                    " with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_CADASTRAL_UNIT)
    public ResponseEntity<CadastralUnitHateoas>
    getNICadastralUnitToFileTemplate(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the " +
                            "CadastralUnit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultCadastralUnit(systemID));
    }


    // Create a default StorageLocation
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-oppbevaringssted
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
                    description = "systemID of the file",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.getDefaultStorageLocation(systemID));
    }

    // Add a Position to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-posisjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-posisjon/
    @Operation(summary = "Associates a Position (national identifier) with a" +
            " File identified by systemID",
            description = "Returns the File with the position associated with" +
                    " it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = POSITION +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_POSITION)
    public ResponseEntity<PositionHateoas> getNIPositionToFileTemplate(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Position" +
                            " with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultPosition(systemID));
    }

    // Add a Plan to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-plan
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-plan/
    @Operation(summary = "Associates a Plan (national identifier) with a File" +
            " identified by systemID",
            description = "Returns the File with  the plan associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = PLAN +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PLAN)
    public ResponseEntity<PlanHateoas> getNIPlanToFileTemplate(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Plan with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultPlan(systemID));
    }

    // Add a Unit to a File
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-enhetsidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-enhetsidentifikator/
    @Operation(summary = "Associates a Unit (national identifier) with a " +
            "File identified by systemID",
            description = "Returns the File with the unit associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = NI_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_NI_UNIT)
    public ResponseEntity<UnitHateoas> getNIUnitToFileTemplate(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Unit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultUnit(systemID));
    }

    // Get default values to use when expanding a File to a CaseFile
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/utvid-til-saksmappe
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/utvid-til-saksmappe/
    @Operation(summary = "Expands a File identified by a systemId to a CaseFile",
            description = "Returns the newly updated CaseFile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CaseFile " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            FILE_EXPAND_TO_CASE_FILE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CaseFileExpansionHateoas> getExpandFileToCaseFileTemplate(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to expand",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultValuesToExpandToCaseFile(
                        systemID));
    }

    // API - All PUT Requests (CRUD - UPDATE)

    // Update a File with given values
    // PUT [contextPath][api]/arkivstruktur/mappe/{systemId}
    @Operation(summary = "Updates a File identified by a given systemId",
            description = "Returns the newly updated file")
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
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FileHateoas> updateFile(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "File",
                    description = "Incoming file object",
                    required = true)
            @RequestBody File file) throws NikitaException {
        validateForUpdate(file);
        return ResponseEntity.status(OK)
                .body(fileService.handleUpdate(
                        systemID, parseETAG(request.getHeader(ETAG)), file));
    }

    // Update a File with given values
    // PATCH [contextPath][api]/arkivstruktur/mappe/{systemId}
    @Operation(summary = "Updates a File identified by a given systemId",
            description = "Returns the newly updated file")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "File OK"),
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
    @PatchMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FileHateoas> patchFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "File",
                    description = "Incoming file object",
                    required = true)
            @RequestBody PatchObjects patchObjects) throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.handleUpdate(systemID, patchObjects));
    }

    // Finalise a File
    // PUT [contextPath][api]/arkivstruktur/mappe/{systemId}/avslutt-mappe
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/avslutt-mappe/
    @Operation(summary = "Updates a File identified by a given systemId",
            description = "Returns the newly updated file")
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

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FILE_END,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ApiError> finaliseFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Expand a File to a CaseFile
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/utvid-til-saksmappe
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/utvid-til-saksmappe/
    @Operation(summary = "Expands a File identified by a systemId to a CaseFile",
            description = "Returns the newly updated CaseFile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CaseFile " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "CaseFile " +
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            FILE_EXPAND_TO_CASE_FILE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CaseFileHateoas> expandFileToCaseFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to expand",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @RequestBody PatchMerge patchMerge)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fileService.expandToCaseFile(systemID, patchMerge));
    }

    // Create a new PartUnit and associate it with the given file
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-partenhet
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-partenhet/
    @Operation(summary = "Persists a PartUnit object associated with the " +
            "given File systemId",
            description = "Returns the newly created PartUnit object after it" +
                    " was associated with a File object and persisted to the " +
                    "database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "PartUnit " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "PartUnit " +
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
                            " of type PartUnit"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_UNIT,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartUnitHateoas>
    createPartUnitAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to associate the PartUnit" +
                            " with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "PartUnit",
                    description = "Incoming PartUnit object",
                    required = true)
            @RequestBody PartUnit partUnit)
            throws NikitaException {
        PartUnitHateoas partUnitHateoas =
                fileService.createPartUnitAssociatedWithFile(
                        systemID, partUnit);
        return ResponseEntity.status(CREATED)
                .body(partUnitHateoas);
    }

    // Create a new PartPerson and associate it with the given file
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-partenhet
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-partenhet/
    @Operation(summary = "Persists a PartPerson object associated with the " +
            "given File systemId",
            description = "Returns the newly created PartPerson object after " +
                    "it was associated with a File object and persisted to " +
                    "the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "PartPerson " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "PartPerson " +
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
                            " of type PartPerson"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_PART_PERSON,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartPersonHateoas>
    createPartPersonAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to associate the " +
                            "PartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "PartPerson",
                    description = "Incoming PartPerson object",
                    required = true)
            @RequestBody PartPerson partPerson)
            throws NikitaException {
        PartPersonHateoas partPersonHateoas =
                fileService.createPartPersonAssociatedWithFile(
                        systemID, partPerson);
        return ResponseEntity.status(CREATED)
                .body(partPersonHateoas);
    }

    // Create a StorageLocation
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-oppbevaringssted
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
                    description = "systemID of the record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "StorageLocation",
                    description = "Incoming storageLocation",
                    required = true)
            @RequestBody StorageLocation storageLocation)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(fileService
                        .createStorageLocationAssociatedWithFile(
                                systemID, storageLocation));
    }

    // Add a Building to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-bygning
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-bygning/
    @Operation(summary = "Associates a Building (national identifier) with a" +
            " File identified by systemID",
            description = "Returns the File with the building associated with" +
                    " it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = BUILDING +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = BUILDING +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_BUILDING,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<BuildingHateoas> addNIBuildingToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Building" +
                            " with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Building",
                    description = "building",
                    required = true)
            @RequestBody Building building) throws NikitaException {
        BuildingHateoas buildingHateoas =
                fileService.createBuildingAssociatedWithFile(
                        systemID, building);
        return ResponseEntity.status(CREATED)
                .body(buildingHateoas);
    }

    // Add a DNumber to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-dnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-dnummer/
    @Operation(summary = "Associates a DNumber (national identifier) with a" +
            " File identified by systemID",
            description = "Returns the File with the dNumber associated with " +
                    "it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = D_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = D_NUMBER +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_D_NUMBER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DNumberHateoas> addNIDNumberToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the DNumber " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "DNumber",
                    description = "dNumber",
                    required = true)
            @RequestBody DNumber dNumber)
            throws NikitaException {
        DNumberHateoas dNumberHateoas =
                fileService.createDNumberAssociatedWithFile(
                        systemID, dNumber);
        return ResponseEntity.status(CREATED)
                .body(dNumberHateoas);
    }

    // Add a SocialSecurityNumber to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-foedselsnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-foedselsnummer/
    @Operation(summary = "Associates a SocialSecurityNumber (national " +
            "identifier) with a File identified by systemID",
            description = "Returns the File with  the socialSecurityNumber " +
                    "associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_SOCIAL_SECURITY_NUMBER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SocialSecurityNumberHateoas>
    addNISocialSecurityNumberToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the " +
                            "SocialSecurityNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "SocialSecurityNumber",
                    description = "socialSecurityNumber",
                    required = true)
            @RequestBody SocialSecurityNumber socialSecurityNumber)
            throws NikitaException {
        SocialSecurityNumberHateoas socialSecurityNumberHateoas =
                fileService.createSocialSecurityNumberAssociatedWithFile(
                        systemID, socialSecurityNumber);
        return ResponseEntity.status(CREATED)
                .body(socialSecurityNumberHateoas);
    }

    // Add a CadastralUnit to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-matrikkel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-matrikkel/
    @Operation(summary = "Associates a CadastralUnit (national identifier) " +
            "with a File identified by systemID",
            description = "Returns the File with the cadastralUnit associated" +
                    " with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_CADASTRAL_UNIT,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CadastralUnitHateoas> addNICadastralUnitToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the " +
                            "CadastralUnit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CadastralUnit",
                    description = "cadastralUnit",
                    required = true)
            @RequestBody CadastralUnit cadastralUnit)
            throws NikitaException {
        CadastralUnitHateoas cadastralUnitHateoas =
                fileService.createCadastralUnitAssociatedWithFile(
                        systemID, cadastralUnit);
        return ResponseEntity.status(CREATED)
                .body(cadastralUnitHateoas);
    }

    // Add a Position to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-posisjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-posisjon/
    @Operation(summary = "Associates a Position (national identifier) with a" +
            " File identified by systemID",
            description = "Returns the File with the position associated with" +
                    " it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = POSITION +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = POSITION +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_POSITION,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PositionHateoas> addNIPositionToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the " +
                            "Position with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Position",
                    description = "position",
                    required = true)
            @RequestBody Position position)
            throws NikitaException {
        PositionHateoas positionHateoas =
                fileService.createPositionAssociatedWithFile(
                        systemID, position);
        return ResponseEntity.status(CREATED)
                .body(positionHateoas);
    }

    // Add a Plan to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-plan
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-plan/
    @Operation(summary = "Associates a Plan (national identifier) with a File" +
            " identified by systemID",
            description = "Returns the File with  the plan associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = PLAN +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = PLAN +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PLAN,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PlanHateoas> addNIPlanToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Plan with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Plan",
                    description = "plan",
                    required = true)
            @RequestBody Plan plan)
            throws NikitaException {
        PlanHateoas planHateoas =
                fileService.createPlanAssociatedWithFile(
                        systemID, plan);
        return ResponseEntity.status(CREATED)
                .body(planHateoas);
    }

    // Add a Unit to a File
    // POST [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-enhetsidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-enhetsidentifikator/
    @Operation(summary = "Associates a Unit (national identifier) with a " +
            "File identified by systemID",
            description = "Returns the File with the unit associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = NI_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = NI_UNIT +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_NI_UNIT,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<UnitHateoas> addNIUnitToFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of File to associate the Unit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Unit",
                    description = "unit",
                    required = true)
            @RequestBody Unit unit)
            throws NikitaException {
        UnitHateoas unitHateoas =
                fileService.createUnitAssociatedWithFile(
                        systemID, unit);
        return ResponseEntity.status(CREATED)
                .body(unitHateoas);
    }

    // Expand a File to a MeetingFile
    // PUT [contextPath][api]/arkivstruktur/mappe/{systemId}/utvid-til-moetemappe
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/utvid-til-moetemappe/
    @Operation(summary = "Expands a File identified by a systemId to a " +
            "MeetingFile",
            description = "Returns the newly updated MeetingFile. Note TODO " +
                    "in FileHateoasController. Fix this before swagger is " +
                    "published")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CaseFile " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "CaseFile " +
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            FILE_EXPAND_TO_MEETING_FILE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ApiError> expandFileToMeetingFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to expand",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Delete a File identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/mappe/{systemId}/
    @Operation(summary = "Deletes a single File entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "File deleted"),
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
    public ResponseEntity<String> deleteFileBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        fileService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete all File
    // DELETE [contextPath][api]/arkivstruktur/mappe/
    @Operation(summary = "Deletes all File")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all File"),
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
    public ResponseEntity<String> deleteAllFile() {
        fileService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
