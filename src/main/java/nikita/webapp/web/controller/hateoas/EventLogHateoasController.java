package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.EventLogHateoas;
import nikita.common.model.noark5.v5.EventLog;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.IEventLogHateoasHandler;
import nikita.webapp.service.interfaces.IEventLogService;
import nikita.webapp.web.controller.hateoas.NoarkController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

@RestController
@RequestMapping(value = HREF_BASE_LOGGING + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class EventLogHateoasController
        extends NoarkController {

    private IEventLogHateoasHandler eventLogHateoasHandler;
    private IEventLogService eventLogService;

    public EventLogHateoasController
        (IEventLogHateoasHandler eventLogHateoasHandler,
         IEventLogService eventLogService) {
        this.eventLogHateoasHandler = eventLogHateoasHandler;
        this.eventLogService = eventLogService;
    }

    // GET [contextPath][api]/loggingogsporing/hendelseslogg/
    @ApiOperation(
            value = "Retrieves all EventLog entities limited by ownership  " +
                    "rights",
            response = EventLogHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "EventLog found",
                    response = EventLogHateoas.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @Counted
    @GetMapping(EVENT_LOG)
    public ResponseEntity<EventLogHateoas> findAllEventLog(
            HttpServletRequest request) {
        EventLogHateoas eventLogHateoas = eventLogService.
                findEventLogByOwner();
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(eventLogHateoas);
    }

    // GET [contextPath][api]/loggingogsporing/hendelseslogg/{systemId}/
    @ApiOperation(value = "Retrieves a single eventLog entity given a systemId",
            response = EventLog.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "EventLog returned",
                    response = EventLog.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = EVENT_LOG + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<EventLogHateoas> findOne(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of eventLog to retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        EventLogHateoas eventLogHateoas =
            eventLogService.findSingleEventLog(systemID);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(eventLogHateoas.getEntityVersion().toString())
                .body(eventLogHateoas);
    }

    // PUT [contextPath][api]/loggingogsporing/hendelseslogg/{systemId}/
    @ApiOperation(
            value = "Updates a EventLog object",
            notes = "Returns the newly updated EventLog object after it is " +
                    "persisted to the database",
            response = EventLogHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "EventLog " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = EventLogHateoas.class),
            @ApiResponse(
                    code = 201,
                    message = "EventLog " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = EventLogHateoas.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type EventLog"),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = EVENT_LOG + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<EventLogHateoas> updateEventLog(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of eventLog to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "eventLog",
                    value = "Incoming eventLog object",
                    required = true)
            @RequestBody EventLog eventLog) throws NikitaException {
        EventLogHateoas eventLogHateoas =
                eventLogService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)), eventLog);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(eventLogHateoas.getEntityVersion().toString())
                .body(eventLogHateoas);
    }

    // DELETE [contextPath][api]/loggingogsporing/hendelseslogg/{systemId}/
    @ApiOperation(
            value = "Deletes a single EventLog entity identified by systemID",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "ok message",
                    response = String.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = EVENT_LOG + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteEventLogBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the eventLog to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        eventLogService.deleteEntity(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
