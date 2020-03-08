package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.secondary.IDocumentFlowHateoasHandler;
import nikita.webapp.service.interfaces.secondary.IDocumentFlowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

@RestController
@RequestMapping(value = HREF_BASE_CASE_HANDLING + SLASH + DOCUMENT_FLOW,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class DocumentFlowHateoasController
        extends NoarkController {

    private IDocumentFlowHateoasHandler documentFlowHateoasHandler;
    private IDocumentFlowService documentFlowService;

    public DocumentFlowHateoasController(IDocumentFlowHateoasHandler documentFlowHateoasHandler,
                                   IDocumentFlowService documentFlowService) {
        this.documentFlowHateoasHandler = documentFlowHateoasHandler;
        this.documentFlowService = documentFlowService;
    }

    // API - All GET Requests (CRUD - READ)

    // GET [contextPath][api]/sakarkiv/dokumentflyt/
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/dokumentflyt/
    @ApiOperation(value = "Retrieves multiple DocumentFlow entities limited " +
                  "by ownership rights",
                  response = DocumentFlowHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow found",
                    response = DocumentFlowHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping()
    public ResponseEntity<DocumentFlowHateoas> findAllDocumentFlow() {
        DocumentFlowHateoas documentFlowHateoas =
            documentFlowService.findAllByOwner();
        return ResponseEntity.status(HttpStatus.OK)
                .body(documentFlowHateoas);
    }

    // GET [contextPath][api]/sakarkiv/dokumentflyt/{systemId}
    @ApiOperation(value = "Retrieves a single DocumentFlow entity given a systemId",
            response = DocumentFlowHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow returned",
                    response = DocumentFlow.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<DocumentFlowHateoas> findDocumentFlowBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the DocumentFlow to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        DocumentFlowHateoas documentFlowHateoas = documentFlowService.findBySystemId(systemId);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentFlowHateoas);
    }

    // PUT [contextPath][api]/sakarkiv/dokumentflyt/{systemId}
    @ApiOperation(value = "Updates a DocumentFlow identified by a given systemId",
            notes = "Returns the newly updated nationalIdentifierPerson",
            response = DocumentFlowHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentFlowHateoas.class),
            @ApiResponse(code = 201, message = "DocumentFlow " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentFlowHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type DocumentFlow"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentFlowHateoas> updateDocumentFlowBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of DocumentFlow to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "DocumentFlow",
                    value = "Incoming DocumentFlow object",
                    required = true)
            @RequestBody DocumentFlow documentFlow) throws NikitaException {
        DocumentFlowHateoas documentFlowHateoas =
            documentFlowService.updateDocumentFlowBySystemId
            (systemID, parseETAG(request.getHeader(ETAG)), documentFlow);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentFlowHateoas);
    }

    // DELETE [contextPath][api]/sakarkiv/dokumentflyt/{systemID}/
    @ApiOperation(value = "Deletes a single DocumentFlow entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteDocumentFlowBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the documentFlow to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        documentFlowService.deleteDocumentFlowBySystemId(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
