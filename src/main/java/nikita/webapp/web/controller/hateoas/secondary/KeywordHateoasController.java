package nikita.webapp.web.controller.hateoas.secondary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordHateoas;
import nikita.common.model.noark5.v5.secondary.Keyword;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.secondary.IKeywordService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH + KEYWORD,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class KeywordHateoasController
        extends NoarkController {

    private final IKeywordService keywordService;

    public KeywordHateoasController(
            IKeywordService keywordService) {
        this.keywordService = keywordService;
    }

    // API - All GET Requests (CRUD - READ)
    // GET [contextPath][api]/arkivstruktur/noekkelord/
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/noekkelord/
    @Operation(summary = "Retrieves multiple Keyword entities limited " +
            "by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Keyword found"),
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
    public ResponseEntity<KeywordHateoas> findAllKeyword() {
        KeywordHateoas keywordHateoas = keywordService.findAllByOwner();
        return ResponseEntity.status(OK)
                .body(keywordHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/noekkelord/{systemId}
    @Operation(summary = "Retrieves a single Keyword entity given a systemId")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<KeywordHateoas> findKeywordBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Keyword to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        KeywordHateoas keywordHateoas = keywordService.findBySystemId(systemID);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(keywordHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/noekkelord/{systemId}
    @Operation(summary = "Updates a Keyword identified by a given systemId",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Keyword " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Keyword " +
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
                            " of type Keyword"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<KeywordHateoas> updateKeywordBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of Keyword to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Keyword",
                    description = "Incoming Keyword object",
                    required = true)
            @RequestBody Keyword keyword) throws NikitaException {
        KeywordHateoas keywordHateoas =
                keywordService.updateKeywordBySystemId(systemID,
                        parseETAG(request.getHeader(ETAG)), keyword);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(keywordHateoas);
    }

    // DELETE [contextPath][api]/arkivstruktur/noekkelord/{systemID}/
    @Operation(summary = "Deletes a single Keyword entity identified by " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Keyword deleted"),
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
    public ResponseEntity<String> deleteKeywordBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the keyword to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        keywordService.deleteKeywordBySystemId(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
