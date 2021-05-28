package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.CrossReferenceHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.secondary.CrossReference;
import nikita.common.model.noark5.v5.secondary.Keyword;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IClassService;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.IRecordService;
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
@RequestMapping(value = HREF_BASE_CLASS,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class ClassHateoasController
        extends NoarkController {

    private final ICaseFileService caseFileService;
    private final IClassService classService;
    private final IFileService fileService;
    private final IRecordService recordService;

    public ClassHateoasController(ICaseFileService caseFileService,
                                  IClassService classService,
                                  IFileService fileService,
                                  IRecordService recordService) {
        this.caseFileService = caseFileService;
        this.classService = classService;
        this.fileService = fileService;
        this.recordService = recordService;
    }

    // API - All POST Requests (CRUD - CREATE)

    // POST [contextPath][api]/arkivstruktur/klasse/{systemID}/ny-klasse
    // REL: https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-klasse/
    @Operation(summary = "Persists a Class object associated with the " +
            "(other) given Class systemId",
            description = "Returns the newly created class object after it " +
                    "was associated with a class object and persisted to the " +
                    "database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Class " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Class " +
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
                            " of type Class"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASS,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassHateoas>
    createClassAssociatedWithClass(
            @Parameter(name = SYSTEM_ID, description = "systemID of class to " +
                    "associate the klass with.",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "klass",
                    description = "Incoming class object",
                    required = true)
            @RequestBody Class klass)
            throws NikitaException {
        ClassHateoas classHateoas = classService.
                createClassAssociatedWithClass(systemID, klass);
        return ResponseEntity.status(CREATED)
                .body(classHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-skjermingmetadata/
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-skjermingmetadata/
    @Operation(summary = "Create a ScreeningMetadata associated with a Class " +
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
                    description = "systemId of Class to associate " +
                            "ScreeningMetadata with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "ScreeningMetadata",
                    description = "Incoming ScreeningMetadata object",
                    required = true)
            @RequestBody final Metadata screeningMetadata)
            throws NikitaException {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                classService.createScreeningMetadataAssociatedWithClass(
                        systemID, screeningMetadata);
        return ResponseEntity.status(CREATED)
                .body(screeningMetadataHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-skjermingmetadata/
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-skjermingmetadata/
    @Operation(summary = "Create a CrossReference associated with a Class " +
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
                    description = "systemId of Class to associate " +
                            "CrossReference with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CrossReference",
                    description = "Incoming CrossReference object",
                    required = true)
            @RequestBody CrossReference crossReference)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(classService
                        .createCrossReferenceAssociatedWithClass(
                                systemID, crossReference));
    }

    // POST [contextPath][api]/arkivstruktur/klasse/{systemID}/ny-mappe
    @Operation(summary = "Persists a File object associated with the " +
            "Class systemId",
            description = "Returns the newly created file object after it was" +
                    " associated with a class object and persisted to the " +
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_FILE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FileHateoas>
    createFileAssociatedWithClass(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Class to associate the file " +
                            "with.",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "file",
                    description = "Incoming file object",
                    required = true)
            @RequestBody File file)
            throws NikitaException {
        FileHateoas fileHateoas = classService.
                createFileAssociatedWithClass(systemID, file);
        return ResponseEntity.status(CREATED)
                .body(fileHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/klasse/{systemID}/ny-saksmappe
    @Operation(summary = "Persists a CaseFile object associated with the " +
            "Class systemId",
            description = "Returns the newly created caseFile object after it" +
                    " was associated with a class object and persisted to the" +
                    " database")
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
                            " of type CaseFile"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CASE_FILE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CaseFileHateoas>
    createCaseCaseFileAssociatedWithClass(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Class to associate the " +
                            "caseFile with.",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "caseFile",
                    description = "Incoming caseFile object",
                    required = true)
            @RequestBody CaseFile caseFile)
            throws NikitaException {
        CaseFileHateoas caseFileHateoas = classService.
                createCaseFileAssociatedWithClass(systemID, caseFile);
        return ResponseEntity.status(CREATED)
                .body(caseFileHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/klasse/{systemID}/ny-registrering
    @Operation(summary = "Persists a Record object associated with the " +
            "Class systemId",
            description = "Returns the newly created record object after it " +
                    "was associated with a class object and persisted to the " +
                    "database")
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
    public ResponseEntity<RecordHateoas>
    createRecordAssociatedWithClass(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Class to associate the record " +
                            "with.",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "record", description = "Incoming record object",
                    required = true)
            @RequestBody Record record)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(classService
                        .createRecordAssociatedWithClass(systemID, record));
    }

    // API - All GET Requests (CRUD - READ)

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<ClassHateoas> findOne(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of class to retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        ClassHateoas classHateoas = classService.findSingleClass(systemID);
        return ResponseEntity.status(OK)
                .body(classHateoas);
    }

    @Operation(summary = "Retrieves multiple Class entities limited by " +
            "ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Class list found"),
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
    public ResponseEntity<ClassHateoas> findAllClass() {
        ClassHateoas classHateoas = classService.findAll();
        return ResponseEntity.status(OK)
                .body(classHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/underklasse
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/underklasse/
    @Operation(summary = "Retrieves all children associated with identified " +
            "class")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Class children found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SUB_CLASS)
    public ResponseEntity<ClassHateoas> findAllChildrenClass(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of parent Class",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        ClassHateoas classHateoas = classService.findAllChildren(systemID);
        return ResponseEntity.status(OK)
                .body(classHateoas);
    }

    // Retrieve all ClassificationSystem associated with Class identified by a
    // systemId
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/klassifikasjonsystem
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klassifikasjonsystem/
    @Operation(summary = "Retrieves a single ClassificationSystem that is " +
            "the parent of the Class entity identified by systemId")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            CLASSIFICATION_SYSTEM)
    public ResponseEntity<ClassificationSystemHateoas>
    findParentClassificationSystemByFileSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the classificationSystem to " +
                            "retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(classService
                        .findClassificationSystemAssociatedWithClass(systemID));
    }

    // Retrieve all Class associated with Class identified by a systemId
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/klasse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/
    @Operation(summary = "Retrieves a single Class that is  the parent of " +
            "the Class entity identified by systemId")
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
    public ResponseEntity<ClassHateoas> findParentClassByClassSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the class to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(classService.findClassAssociatedWithClass(systemID));
    }


    // Retrieve all ScreeningMetadata associated with the Screening of a Class
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/skjermingmetadata
    @Operation(summary = "Retrieves all ScreeningMetadata associated with the" +
            " Screening object of a Class")
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
    getScreeningMetadataAssociatedWithClass(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the class to retrieve " +
                            "screening metadata",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(classService
                        .getScreeningMetadataAssociatedWithClass(systemID));
    }

    // Return a Class object with default values
    //GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-klasse
    @Operation(summary = "Create a Class with default values")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CLASS)
    public ResponseEntity<ClassHateoas> createDefaultClass(
            @Parameter(
                    name = SYSTEM_ID,
                    description = "systemID of Class to associate Class with.",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(classService.generateDefaultSubClass(systemID));
    }

    // Add a Keyword to a Class
    // POST [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-noekkelord
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
                classService.createKeywordAssociatedWithClass(
                        systemID, keyword);
        return ResponseEntity.status(CREATED)
                .body(keywordHateoas);
    }

    // Create a Keyword with default values
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-noekkelord
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
    public ResponseEntity<KeywordHateoas> createDefaultKeyword(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Class to create default " +
                            "Keyword for",
                    required = true)
            @PathVariable UUID systemID) {
        return ResponseEntity.status(OK)
                .body(classService.generateDefaultKeyword(systemID));
    }


    // Create a default ScreeningMetadata
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-skjermingmetadata
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
                    description = "systemID of the documentDescription",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(classService.getDefaultScreeningMetadata(systemID));
    }

    // Create a File object with default values
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-mappe/
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
                    description = "systemID of Class to create default File " +
                            "for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fileService.generateDefaultFile(systemID));
    }

    // Create a CaseFile object with default values
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-saksmappe/
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
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-registrering
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
                    description = "systemID of Class to create default " +
                            "Record for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultRecord(systemID));
    }

    // Get a CrossReference template
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/ny-kryssreferanse
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
                    description = "systemID of Class to create default " +
                            "CrossReference for",
                    required = true)
            @PathVariable UUID systemID) {
        return ResponseEntity.status(OK)
                .body(classService.getDefaultCrossReference(systemID));
    }

    // Retrieve all Records associated with a Class (paginated)
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/registrering/
    @Operation(summary = "Retrieves a lit of Records associated with a Class")
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
    public ResponseEntity<RecordHateoas> findAllRecordAssociatedWithClass(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the class to find associated records",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(classService.findAllRecordAssociatedWithClass(systemID));
    }

    // Retrieve all Files associated with a Class (paginated)
    // GET [contextPath][api]/arkivstruktur/klasse/{systemId}/mappe/
    @Operation(summary = "Retrieves a list of Files associated with a Class")
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
    public ResponseEntity<FileHateoas> findAllFileAssociatedWithClass(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the class to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(classService.findAllFileAssociatedWithClass(systemID));
    }

    // Delete a Class identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/klasse/{systemId}/
    @Operation(summary = "Deletes a single Class entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Class deleted"),
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
    public ResponseEntity<String> deleteClass(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the class to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        classService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete all Class
    // DELETE [contextPath][api]/arkivstruktur/klasse/
    @Operation(summary = "Deletes all Class")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all Class"),
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
    public ResponseEntity<String> deleteAllClass() {
        classService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a Class
    // PUT [contextPath][api]/arkivstruktur/klasse/{systemID}
    @Operation(summary = "Updates a Class object",
            description = "Returns the newly updated Class object after it is" +
                    " persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Class " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Class " +
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
                            " of type Class"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassHateoas> updateClass(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of class to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "class",
                    description = "Incoming class object",
                    required = true)
            @RequestBody Class klass)
            throws NikitaException {
        validateForUpdate(klass);
        ClassHateoas classHateoas = classService.handleUpdate(systemID,
                parseETAG(request.getHeader(ETAG)), klass);
        return ResponseEntity.status(OK)
                .body(classHateoas);
    }
}
