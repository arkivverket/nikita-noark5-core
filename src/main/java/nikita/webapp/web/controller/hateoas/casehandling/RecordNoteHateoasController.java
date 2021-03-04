package nikita.webapp.web.controller.hateoas.casehandling;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.casehandling.IRecordNoteService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_RECORD_NOTE + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class RecordNoteHateoasController
        extends NoarkController {

    private final IRecordNoteService recordNoteService;

    public RecordNoteHateoasController(
            IRecordNoteService recordNoteService) {
        this.recordNoteService = recordNoteService;
    }

    // API - All POST Requests (CRUD - CREATE)

    // POST [contextPath][api]/casehandling/arkivnotat/{systemId}/ny-dokumentflyt
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-dokumentflyt/
    @Operation(summary = "Create a new DocumentFlow and associate it with " +
            "the given RecordNote systemId",
            description = "Returns the newly created DocumentFlow after" +
                    " it was associated with a RecordNote and persisted" +
                    " to the database.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentFlow " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "DocumentFlow " +
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
                            " of type DocumentFlow"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value =
            SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_FLOW,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentFlowHateoas>
    createDocumentFlowAssociatedWithRecordNote(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of registry entry to associate " +
                            "the document flow with.",
                    required = true)
            @PathVariable String systemID,
            @Parameter(name = "documentFlow",
                    description = "Incoming documentFlow object",
                    required = true)
            @RequestBody DocumentFlow documentFlow)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(recordNoteService.associateDocumentFlowWithRecordNote
                        (systemID, documentFlow));
    }

    // Retrieve a single recordNote identified by systemId
    // GET [contextPath][api]/casehandling/arkivnotat/{systemID}
    @Operation(summary = "Retrieves a single RecordNote entity given a " +
            "systemId")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<RecordNoteHateoas> findRecordNoteBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the recordNote to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String recordNoteSystemId) {
        return recordNoteService.findBySystemId(recordNoteSystemId);
    }

    // Get all recordNote
    // GET [contextPath][api]/casehandling/arkivnotat/
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/arkivnotat/
    @Operation(summary = "Retrieves multiple RecordNote entities limited by " +
            "ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RecordNote found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping()
    public ResponseEntity<RecordNoteHateoas> findAllRecordNote() {
        return recordNoteService.findAllByOwner();
    }

    // GET [contextPath][api]/casehandling/journalpost/{systemId}/dokumentflyt
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/dokumentflyt/
    @Operation(summary = "Retrieve all DocumentFlow associated with a " +
            "RecordNote identified by systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentFlow returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + DOCUMENT_FLOW)
    public ResponseEntity<DocumentFlowHateoas>
    findAllDocumentFlowAssociatedWithRecordNote(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated RecordNote",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity
                .status(OK)
                .body(recordNoteService.
                        findAllDocumentFlowWithRecordNoteBySystemId(systemID));
    }


    // GET [contextPath][api]/arkivstruktur/arkivnotat/{systemId}/ny-dokumentflyt
    //  https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-dokumentflyt/
    @Operation(summary = "Create a DocumentFlow with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentFlow returned"),
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
            NEW_DOCUMENT_FLOW)
    public ResponseEntity<DocumentFlowHateoas> createDefaultDocumentFlow(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the recordNote",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordNoteService.
                        generateDefaultDocumentFlow(systemID));
    }

    // Delete a Record identified by systemID
    // DELETE [contextPath][api]/casehandling/arkivnotat/{systemId}/
    @Operation(summary = "Deletes a single RecordNote entity identified by " +
            SYSTEM_ID)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "RecordNote deleted"),
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
    public ResponseEntity<String> deleteRecordNoteBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the recordNote to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return recordNoteService.deleteEntity(systemID);
    }

    // Delete all RecordNote
    // DELETE [contextPath][api]/arkivstruktur/arkivnotat/
    @Operation(summary = "Deletes all RecordNote belonging to the logged in " +
            "user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all RecordNotes"),
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
    public ResponseEntity<String> deleteAllRecordNote() {
        return recordNoteService.deleteAllByOwnedBy();
    }

    // Update a RecordNote with given values
    // PUT [contextPath][api]/casehandling/arkivnotat/{systemId}
    @Operation(summary = "Updates a RecordNote identified by a given systemId",
            description = "Returns the newly updated recordNote")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RecordNote" +
                            API_MESSAGE_OBJECT_UPDATED),
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
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RecordNoteHateoas> updateRecordNote(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of recordNote to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "RecordNote",
                    description = "Incoming recordNote object",
                    required = true)
            @RequestBody RecordNote recordNote) throws NikitaException {
        validateForUpdate(recordNote);
        return recordNoteService.handleUpdate(systemID, recordNote);
    }
}
