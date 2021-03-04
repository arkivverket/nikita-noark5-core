package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.ChangeLog;
import nikita.common.model.noark5.v5.hateoas.ChangeLogHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.IChangeLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

@RestController
@RequestMapping(value = HREF_BASE_LOGGING + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class ChangeLogHateoasController
        extends NoarkController {

    private final IChangeLogService changeLogService;

    public ChangeLogHateoasController(IChangeLogService changeLogService) {
        this.changeLogService = changeLogService;
    }

    // GET [contextPath][api]/loggingogsporing/endringslogg/
    @Operation(
            summary = "Retrieves all ChangeLog entities limited by ownership " +
                    " rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ChangeLog found"),
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
    @Operation(summary = "Retrieves a single changeLog entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ChangeLog returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = CHANGE_LOG + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<ChangeLogHateoas> findOne(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of changeLog to retrieve.",
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
    @Operation(
            summary = "Updates a ChangeLog object",
            description = "Returns the newly updated ChangeLog object after " +
                    "it is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ChangeLog " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "ChangeLog " +
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
                            " of type ChangeLog"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = CHANGE_LOG + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ChangeLogHateoas> updateChangeLog(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of changeLog to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "changeLog",
                    description = "Incoming changeLog object",
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
    @Operation(
            summary = "Deletes a single ChangeLog entity identified by " +
                    "systemID")
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
    @DeleteMapping(value = CHANGE_LOG + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteChangeLogBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the changeLog to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        changeLogService.deleteEntity(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
