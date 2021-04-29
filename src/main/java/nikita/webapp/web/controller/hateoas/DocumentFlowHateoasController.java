package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.secondary.IDocumentFlowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_CASE_HANDLING + SLASH + DOCUMENT_FLOW,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class DocumentFlowHateoasController
        extends NoarkController {

    private final IDocumentFlowService documentFlowService;

    public DocumentFlowHateoasController(
            IDocumentFlowService documentFlowService) {
        this.documentFlowService = documentFlowService;
    }

    // API - All GET Requests (CRUD - READ)

    // GET [contextPath][api]/sakarkiv/dokumentflyt/
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/dokumentflyt/
    @Operation(summary = "Retrieves multiple DocumentFlow entities limited " +
            "by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentFlow found"),
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
    public ResponseEntity<DocumentFlowHateoas> findAllDocumentFlow() {
        DocumentFlowHateoas documentFlowHateoas =
                documentFlowService.findAllByOwner();
        return ResponseEntity.status(OK)
                .body(documentFlowHateoas);
    }

    // GET [contextPath][api]/sakarkiv/dokumentflyt/{systemId}
    @Operation(summary = "Retrieves a single DocumentFlow entity given a " +
            "systemID")
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<DocumentFlowHateoas> findDocumentFlowBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the DocumentFlow to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        DocumentFlowHateoas documentFlowHateoas =
                documentFlowService.findBySystemId(systemId);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentFlowHateoas);
    }

    // PUT [contextPath][api]/sakarkiv/dokumentflyt/{systemId}
    @Operation(summary = "Updates a DocumentFlow identified by a given systemId",
            description = "Returns the newly updated nationalIdentifierPerson")
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
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentFlowHateoas> updateDocumentFlowBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of DocumentFlow to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "DocumentFlow",
                    description = "Incoming DocumentFlow object",
                    required = true)
            @RequestBody DocumentFlow documentFlow) throws NikitaException {
        DocumentFlowHateoas documentFlowHateoas =
                documentFlowService
                        .updateDocumentFlowBySystemId(systemID,
                                parseETAG(request.getHeader(ETAG)),
                                documentFlow);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentFlowHateoas);
    }

    // DELETE [contextPath][api]/sakarkiv/dokumentflyt/{systemID}/
    @Operation(summary = "Deletes a single DocumentFlow entity identified by " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentFlow deleted"),
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
    public ResponseEntity<String> deleteDocumentFlowBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the documentFlow to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        documentFlowService.deleteDocumentFlowBySystemId(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
