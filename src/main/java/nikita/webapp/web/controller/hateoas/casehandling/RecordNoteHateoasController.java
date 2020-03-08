package nikita.webapp.web.controller.hateoas.casehandling;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import static nikita.common.config.N5ResourceMappings.*;

import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.*;

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
    @ApiOperation(value = "Create a new DocumentFlow and associate it with "+
                  "the given RecordNote systemId",
                  notes = "Returns the newly created DocumentFlow after it " +
                  "was associated with a RecordNote and persisted to the " +
                  "database.",
                  response = DocumentFlow.class) // DocumentFlowHateoas
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
			 response = DocumentFlow.class), // DocumentFlowHateoas
            @ApiResponse(code = 201, message = "DocumentFlow " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
			 response = DocumentFlow.class), // DocumentFlowHateoas
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type DocumentFlow"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_FLOW,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentFlowHateoas>
    createDocumentFlowAssociatedWithRecordNote(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                      value = "systemID of registry entry to associate the document flow with.",
                      required = true)
            @PathVariable String systemID,
            @ApiParam(name = "documentFlow",
                      value = "Incoming documentFlow object",
                      required = true)
            @RequestBody DocumentFlow documentFlow)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(recordNoteService.associateDocumentFlowWithRecordNote
                      (systemID, documentFlow));
    }

    // Retrieve a single recordNote identified by systemId
    // GET [contextPath][api]/casehandling/arkivnotat/{systemID}
    @ApiOperation(value = "Retrieves a single RecordNote entity given a " +
            "systemId", response = RecordNoteHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "RecordNote returned",
                    response = RecordNoteHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<RecordNoteHateoas> findRecordNoteBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the recordNote to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String recordNoteSystemId) {
        return recordNoteService.findBySystemId(recordNoteSystemId);
    }

    // Get all recordNote
    // GET [contextPath][api]/casehandling/arkivnotat/
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/arkivnotat/
    @ApiOperation(value = "Retrieves multiple RecordNote entities limited by " +
            "ownership rights", response = RecordNoteHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "RecordNote found",
                    response = RecordNoteHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping()
    public ResponseEntity<RecordNoteHateoas> findAllRecordNote() {
        return recordNoteService.findAllByOwner();
    }

    // GET [contextPath][api]/casehandling/journalpost/{systemId}/dokumentflyt
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/dokumentflyt/
    @ApiOperation(value = "Retrieve all DocumentFlow associated with a RecordNote identified by systemId",
            response = DocumentFlow.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow returned", response = DocumentFlow.class), //DocumentFlowHateoas
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + DOCUMENT_FLOW)
    public ResponseEntity<DocumentFlowHateoas> findAllDocumentFlowAssociatedWithRecordNote(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated RecordNote",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity
                .status(OK)
                .body(recordNoteService.
                        findAllDocumentFlowWithRecordNoteBySystemId(
                                systemID));
    }


    // GET [contextPath][api]/arkivstruktur/arkivnotat/{systemId}/ny-dokumentflyt
    //  https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-dokumentflyt/
    @ApiOperation(value = "Create a DocumentFlow with default values",
                  response = DocumentFlow.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow returned",
			 response = DocumentFlow.class), // DocumentFlowHateoas
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_FLOW)
    public ResponseEntity<DocumentFlowHateoas> createDefaultDocumentFlow(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                      value = "systemID of the recordNote",
                      required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordNoteService.
                        generateDefaultDocumentFlow(systemID));
    }

    // Delete a Record identified by systemID
    // DELETE [contextPath][api]/casehandling/arkivnotat/{systemId}/
    @ApiOperation(value = "Deletes a single RecordNote entity identified by " +
            SYSTEM_ID, response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "RecordNote deleted",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteRecordNoteBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the recordNote to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return recordNoteService.deleteEntity(systemID);
    }

    // Delete all RecordNote
    // DELETE [contextPath][api]/arkivstruktur/arkivnotat/
    @ApiOperation(value = "Deletes all RecordNote belonging to the logged in " +
            "user", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all RecordNotes",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<String> deleteAllRecordNote() {
        return recordNoteService.deleteAllByOwnedBy();
    }

    // Update a RecordNote with given values
    // PUT [contextPath][api]/casehandling/arkivnotat/{systemId}
    @ApiOperation(value = "Updates a RecordNote identified by a given systemId",
            notes = "Returns the newly updated recordNote",
            response = RecordNoteHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "RecordNote" + API_MESSAGE_OBJECT_UPDATED,
                    response = RecordNoteHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RecordNoteHateoas> updateRecordNote(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of recordNote to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "RecordNote",
                    value = "Incoming recordNote object",
                    required = true)
            @RequestBody RecordNote recordNote) throws NikitaException {
        validateForUpdate(recordNote);
        return recordNoteService.handleUpdate(systemID, recordNote);
    }
}
