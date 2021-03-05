package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.EventLog;
import nikita.common.model.noark5.v5.hateoas.EventLogHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.IEventLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_LOGGING + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class EventLogHateoasController
        extends NoarkController {

    private final IEventLogService eventLogService;

    public EventLogHateoasController(
            IEventLogService eventLogService) {
        this.eventLogService = eventLogService;
    }

    // GET [contextPath][api]/loggingogsporing/hendelseslogg/
    @Operation(
            summary = "Retrieves all EventLog entities limited by ownership " +
                    "rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "EventLog found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @GetMapping(EVENT_LOG)
    public ResponseEntity<EventLogHateoas> findAllEventLog(
            HttpServletRequest request) {
        EventLogHateoas eventLogHateoas = eventLogService.
                findEventLogByOwner();
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(eventLogHateoas);
    }

    // GET [contextPath][api]/loggingogsporing/hendelseslogg/{systemId}/
    @Operation(summary = "Retrieves a single eventLog entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "EventLog returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = EVENT_LOG + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<EventLogHateoas> findOne(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of eventLog to retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        EventLogHateoas eventLogHateoas =
            eventLogService.findSingleEventLog(systemID);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(eventLogHateoas.getEntityVersion().toString())
                .body(eventLogHateoas);
    }

    // PUT [contextPath][api]/loggingogsporing/hendelseslogg/{systemId}/
    @Operation(
            summary = "Updates a EventLog object",
            description = "Returns the newly updated EventLog object after it" +
                    " is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "EventLog " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "EventLog " +
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
                            " of type EventLog"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = EVENT_LOG + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<EventLogHateoas> updateEventLog(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of eventLog to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "eventLog",
                    description = "Incoming eventLog object",
                    required = true)
            @RequestBody EventLog eventLog) throws NikitaException {
        EventLogHateoas eventLogHateoas =
                eventLogService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)), eventLog);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(eventLogHateoas.getEntityVersion().toString())
                .body(eventLogHateoas);
    }

    // DELETE [contextPath][api]/loggingogsporing/hendelseslogg/{systemId}/
    @Operation(
            summary = "Deletes a single EventLog entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "ok message"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = EVENT_LOG + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteEventLogBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the eventLog to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        eventLogService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
