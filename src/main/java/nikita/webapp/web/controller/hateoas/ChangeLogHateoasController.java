package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.ChangeLogHateoas;
import nikita.common.model.noark5.v5.ChangeLog;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.IChangeLogHateoasHandler;
import nikita.webapp.service.interfaces.IChangeLogService;
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
public class ChangeLogHateoasController
        extends NoarkController {

    private IChangeLogHateoasHandler changeLogHateoasHandler;
    private IChangeLogService changeLogService;

    public ChangeLogHateoasController
        (IChangeLogHateoasHandler changeLogHateoasHandler,
         IChangeLogService changeLogService) {
        this.changeLogHateoasHandler = changeLogHateoasHandler;
        this.changeLogService = changeLogService;
    }

    // GET [contextPath][api]/loggingogsporing/endringslogg/
    @ApiOperation(
            value = "Retrieves all ChangeLog entities limited by ownership  " +
                    "rights",
            response = ChangeLogHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ChangeLog found",
                    response = ChangeLogHateoas.class),
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
    @GetMapping(CHANGE_LOG)
    public ResponseEntity<ChangeLogHateoas> findAllChangeLog(
            HttpServletRequest request) {
        ChangeLogHateoas changeLogHateoas = changeLogService.
                findChangeLogByOwner();
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(changeLogHateoas);
    }

    // GET [contextPath][api]/loggingogsporing/endringslogg/{systemId}/
    @ApiOperation(value = "Retrieves a single changeLog entity given a systemId",
            response = ChangeLog.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ChangeLog returned",
                    response = ChangeLog.class),
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
    @GetMapping(value = CHANGE_LOG + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<ChangeLogHateoas> findOne(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of changeLog to retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        ChangeLogHateoas changeLogHateoas =
            changeLogService.findSingleChangeLog(systemID);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(changeLogHateoas.getEntityVersion().toString())
                .body(changeLogHateoas);
    }

    // PUT [contextPath][api]/loggingogsporing/endringslogg/{systemId}/
    @ApiOperation(
            value = "Updates a ChangeLog object",
            notes = "Returns the newly updated ChangeLog object after it is " +
                    "persisted to the database",
            response = ChangeLogHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "ChangeLog " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ChangeLogHateoas.class),
            @ApiResponse(
                    code = 201,
                    message = "ChangeLog " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ChangeLogHateoas.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type ChangeLog"),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = CHANGE_LOG + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ChangeLogHateoas> updateChangeLog(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of changeLog to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "changeLog",
                    value = "Incoming changeLog object",
                    required = true)
            @RequestBody ChangeLog changeLog) throws NikitaException {
        ChangeLogHateoas changeLogHateoas =
                changeLogService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)), changeLog);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(changeLogHateoas.getEntityVersion().toString())
                .body(changeLogHateoas);
    }

    // DELETE [contextPath][api]/loggingogsporing/endringslogg/{systemId}/
    @ApiOperation(
            value = "Deletes a single ChangeLog entity identified by systemID",
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
    @DeleteMapping(value = CHANGE_LOG + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteChangeLogBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the changeLog to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        changeLogService.deleteEntity(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
