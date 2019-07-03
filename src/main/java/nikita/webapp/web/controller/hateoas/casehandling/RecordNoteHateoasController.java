package nikita.webapp.web.controller.hateoas.casehandling;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.casehandling.IRecordNoteService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID_PARAMETER;

@RestController
@RequestMapping(value = HREF_BASE_RECORD_NOTE,
        produces = {NOARK5_V5_CONTENT_TYPE_JSON,
                NOARK5_V5_CONTENT_TYPE_JSON_XML})
public class RecordNoteHateoasController
        extends NoarkController {

    private final IRecordNoteService recordNoteService;

    public RecordNoteHateoasController(
            IRecordNoteService recordNoteService) {
        this.recordNoteService = recordNoteService;
    }

    // API - All POST Requests (CRUD - CREATE)
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
    @GetMapping(value = SYSTEM_ID_PARAMETER)
    public ResponseEntity<RecordNoteHateoas> findRecordNoteBySystemId(
            @ApiParam(name = "systemID",
                    value = "systemID of the recordNote to retrieve",
                    required = true)
            @PathVariable("systemID") final String recordNoteSystemId) {
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

    // Delete a Record identified by systemID
    // DELETE [contextPath][api]/casehandling/arkivnotat/{systemId}/
    @ApiOperation(value = "Deletes a single RecordNote entity identified by " +
            "systemID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "RecordNote deleted",
                    response = Count.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SYSTEM_ID_PARAMETER)
    public ResponseEntity<Count> deleteRecordNoteBySystemId(
            @ApiParam(name = "systemID",
                    value = "systemID of the recordNote to delete",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return recordNoteService.deleteEntity(systemID);
    }

    // Delete all RecordNote
    // DELETE [contextPath][api]/arkivstruktur/arkivnotat/
    @ApiOperation(value = "Deletes all RecordNote belonging to the logged in " +
            "user", response = Count.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all RecordNotes",
                    response = Count.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<Count> deleteAllRecordNote() {
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
    @PutMapping(value = SYSTEM_ID_PARAMETER,
            consumes = {NOARK5_V5_CONTENT_TYPE_JSON})
    public ResponseEntity<RecordNoteHateoas> updateRecordNote(
            @ApiParam(name = "systemID",
                    value = "systemId of recordNote to update",
                    required = true)
            @PathVariable("systemID") final String systemID,
            @ApiParam(name = "RecordNote",
                    value = "Incoming recordNote object",
                    required = true)
            @RequestBody RecordNote recordNote) throws NikitaException {
        validateForUpdate(recordNote);
        return recordNoteService.handleUpdate(systemID, recordNote);
    }
}
