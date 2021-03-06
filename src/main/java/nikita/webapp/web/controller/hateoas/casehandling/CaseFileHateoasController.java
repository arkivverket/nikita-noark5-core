package nikita.webapp.web.controller.hateoas.casehandling;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.web.controller.hateoas.NoarkController;
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
@RequestMapping(value = HREF_BASE_CASE_FILE,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class CaseFileHateoasController
        extends NoarkController {

    private final ICaseFileService caseFileService;

    public CaseFileHateoasController(
            ICaseFileService caseFileService) {
        this.caseFileService = caseFileService;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Create a RecordNote entity
    // POST [contextPath][api]/sakarkiv/saksmappe/{systemId}/ny-saksmappe
    @Operation(summary = "Persists a CaseFile object associated with the " +
            "given CaseFile systemId",
            description = "Returns the newly created casefile object after it" +
                    " was associated with a CaseFile object and persisted to " +
                    "the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CaseFile " +
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CASE_FILE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CaseFileHateoas>
    createCaseFileAssociatedWithCaseFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of CaseFile to associate the " +
                            "CaseFile with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CaseFile",
                    description = "Incoming caseFile object",
                    required = true)
            @RequestBody CaseFile caseFile) {
        return ResponseEntity.status(CREATED)
                .body(caseFileService
                        .createCaseFileToCaseFile(systemID, caseFile));
    }

    // Create a CaseFile entity
    // POST [contextPath][api]/sakarkiv/saksmappe/{systemId}/ny-arkivnotat
    @Operation(summary = "Persists a RecordNote object associated with the " +
            "given CaseFile systemId",
            description = "Returns the newly created record object after it " +
                    "was associated with a File object and persisted to the " +
                    "database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RecordNote " +
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
            NEW_RECORD_NOTE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RecordNoteHateoas>
    createRecordNoteAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to associate the " +
                            "recordNote with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "RecordNote",
                    description = "Incoming recordNote object",
                    required = true)
            @RequestBody RecordNote recordNote) {
        return ResponseEntity.status(CREATED)
                .body(caseFileService.createRecordNoteToCaseFile(
                        systemID, recordNote));
    }

    // Create a RegistryEntry entity
    // POST [contextPath][api]/sakarkiv/{systemId}/ny-journalpost
    @Operation(summary = "Persists a RegistryEntry object associated with the" +
            " given Series systemId",
            description = "Returns the newly created record object after it " +
                    "was associated with a File object and persisted to the " +
                    "database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RegistryEntry " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "RegistryEntry " +
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
                            " of type RegistryEntry"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_REGISTRY_ENTRY,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RegistryEntryHateoas>
    createRegistryEntryAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to associate the record " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "RegistryEntry",
                    description = "Incoming registryEntry object",
                    required = true)
            @RequestBody RegistryEntry registryEntry) {
        return ResponseEntity.status(CREATED)
                .body(caseFileService.
                        createRegistryEntryAssociatedWithCaseFile(systemID,
                                registryEntry));
    }

    // POST [contextPath][api]/sakarkiv/saksmappe/{systemId}/ny-presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-presedens/
    @Operation(summary = "Persists a Precedence object associated with the " +
            "given File systemId",
            description = "Returns the newly created Precedence object after " +
                    "it was associated with a File object and persisted to " +
                    "the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Precedence " +
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
                            " of type Precedence"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PRECEDENCE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PrecedenceHateoas>
    createPrecedenceAssociatedWithFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to associate the " +
                            "Precedence with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "Precedence",
                    description = "Incoming Precedence object",
                    required = true)
            @RequestBody Precedence precedence)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(caseFileService.createPrecedenceAssociatedWithFile
                        (systemID, precedence));
    }

    // API - All GET Requests (CRUD - READ)

    // Create a RegistryEntry object with default values
    // GET [contextPath][api]/sakarkiv/mappe/{systemID}/ny-journalpost
    @Operation(summary = "Create a RegistryEntry with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RegistryEntry returned"),
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
            NEW_REGISTRY_ENTRY)
    public ResponseEntity<RegistryEntryHateoas> createDefaultRegistryEntry(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the caseFile to retrieve a " +
                            "template RegistryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(caseFileService.generateDefaultRegistryEntry(systemID));
    }

    // Create a CaseFile object with default values
    // GET [contextPath][api]/sakarkiv/saksmappe/{systemID}/ny-saksmappe
    @Operation(summary = "Create a (sub)CaseFile with default values")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_CASE_FILE)
    public ResponseEntity<CaseFileHateoas> createDefaultCaseFile(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the caseFile to retrieve a " +
                            "template (sub)CaseFile for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(caseFileService.generateDefaultCaseFile());
    }

    // Create a RecordNote object with default values
    // GET [contextPath][api]/sakarkiv/mappe/{systemID}/ny-arkivnotat
    @Operation(summary = "Create a RecordNote with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RecordNote returned"),
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
            NEW_RECORD_NOTE)
    public ResponseEntity<RecordNoteHateoas> createDefaultRecordNote(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the caseFile to retrieve a " +
                            "template RecordNote",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(caseFileService.generateDefaultRecordNote(systemID));
    }

    // Retrieve a single casefile identified by systemId
    // GET [contextPath][api]/sakarkiv/saksmappe/{systemID}
    @Operation(summary = "Retrieves a single CaseFile entity given a systemId")
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

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<CaseFileHateoas> findOneCaseFileBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the caseFile to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(caseFileService.findBySystemId(systemID));
    }

    // Retrieve all RegistryEntry associated with a casefile identified by
    // systemId
    // GET [contextPath][api]/sakarkiv/saksmappe/{systemID}/journalpost
    @Operation(summary = "Retrieves all RegistryEntry associated with a " +
            "CaseFile identified by systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RegistryEntry list returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + REGISTRY_ENTRY)
    public ResponseEntity<RegistryEntryHateoas>
    findRegistryEntryToCaseFileBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the caseFile to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(caseFileService.findAllRegistryEntryToCaseFile(systemID));
    }

    // Retrieve all RecordNote associated with a casefile identified by systemId
    // GET [contextPath][api]/sakarkiv/saksmappe/{systemID}/journalpost
    @Operation(summary = "Retrieves all RecordNote associated with a " +
            "CaseFile identified by systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RecordNote list returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + RECORD_NOTE)
    public ResponseEntity<RecordNoteHateoas>
    findRecordNoteToCaseFileBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the caseFile to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(caseFileService.findAllRecordNoteToCaseFile(systemID));
    }

    @Operation(summary = "Retrieves multiple CaseFile entities limited by " +
            "ownership rights")
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
    @GetMapping
    public ResponseEntity<CaseFileHateoas> findAll() {
        return ResponseEntity.status(OK)
                .body(caseFileService.findAll());
    }

    // GET [contextPath][api]/sakarkiv/saksmappe/{systemID}/presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/presedens/
    @Operation(summary = "Retrieves all Precedence associated with a " +
            "CaseFile identified by systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence list returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + PRECEDENCE)
    public ResponseEntity<PrecedenceHateoas>
    findPrecedenceForCaseFileBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the caseFile to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity
                .status(OK)
                .body(caseFileService.
                        findAllPrecedenceForCaseFile(systemID));
    }

    // GET [contextPath][api]/sakarkiv/saksmappe/{systemId}/ny-presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-presedens/
    @Operation(summary = "Create a Precedence with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PRECEDENCE)
    public ResponseEntity<PrecedenceHateoas> createDefaultPrecedence(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of file to associate the " +
                            "Precedence with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID) {
        return ResponseEntity.status(OK)
                .body(caseFileService.
                        generateDefaultPrecedence(systemID));
    }

    // Delete a CaseFile identified by systemID
    // DELETE [contextPath][api]/sakarkiv/saksmappe/{systemId}/
    @Operation(summary = "Deletes a single CaseFile entity identified by " +
            SYSTEM_ID)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "CaseFile deleted"),
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
    public ResponseEntity<String> deleteCaseFileBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the caseFile to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        caseFileService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete all CaseFile
    // DELETE [contextPath][api]/sakarkiv/saksmappe/
    @Operation(summary = "Deletes all CaseFile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all CaseFile"),
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
    public ResponseEntity<String> deleteAllCaseFile() {
        caseFileService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Update a CaseFile with given values
    // PUT [contextPath][api]/sakarkiv/saksmappe/{systemId}
    @Operation(summary = "Updates a CaseFile identified by a given systemId",
            description = "Returns the newly updated caseFile")
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

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CaseFileHateoas> updateCaseFile(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of caseFile to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CaseFile",
                    description = "Incoming caseFile object",
                    required = true)
            @RequestBody CaseFile caseFile) throws NikitaException {
        validateForUpdate(caseFile);
        return ResponseEntity.status(OK)
                .body(caseFileService.handleUpdate(
                        systemID, parseETAG(request.getHeader(ETAG)), caseFile));
    }
}
