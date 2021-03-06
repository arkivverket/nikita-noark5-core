package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ConversionHateoas;
import nikita.common.model.noark5.v5.secondary.Conversion;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import nikita.webapp.service.interfaces.IDocumentObjectService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_DOCUMENT_OBJECT)
public class DocumentObjectHateoasController
        extends NoarkController {

    private final Logger logger =
            LoggerFactory.getLogger(DocumentObjectHateoasController.class);
    private final IDocumentObjectService documentObjectService;

    public DocumentObjectHateoasController(
            IDocumentObjectService documentObjectService) {
        this.documentObjectService = documentObjectService;
    }

    // API - All GET Requests (CRUD - READ)
    // Get a documentObject identified by systemID
    // GET [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}
    @Operation(summary = "Retrieves a single DocumentObject entity " +
            "identified by given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentObject returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentObjectHateoas>
    findOneDocumentObjectBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the documentObject to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemId) {
        DocumentObjectHateoas documentObjectHateoas =
                documentObjectService.findBySystemId(systemId);
        return ResponseEntity.status(OK)
                .body(documentObjectHateoas);
    }

    // Get all documentObject
    // GET [contextPath][api]/arkivstruktur/dokumentobjekt/
    @Operation(summary = "Retrieves multiple DocumentObject entities limited " +
            "by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentObject list found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentObjectHateoas> findAllDocumentObject(
            HttpServletRequest request) {
        DocumentObjectHateoas documentObjectHateoas =
                documentObjectService.findAll();
        return ResponseEntity.status(OK)
                .body(documentObjectHateoas);
    }

    // Get a file identified by systemID retrievable with referanseFile
    // GET [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}/referanseFil
    @Operation(summary = "Downloads a file associated with the documentObject" +
            " identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "File download successful"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + REFERENCE_FILE)
    public void handleFileDownload(
            HttpServletRequest request, HttpServletResponse response,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the documentObject that has a file " +
                            "associated with it",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemId)
            throws IOException {
        Resource fileResource = documentObjectService.loadAsResource(
                systemId, request, response);
        try (InputStream filestream = fileResource.getInputStream()) {
            IOUtils.copyLarge(filestream,
                    response.getOutputStream());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NikitaMisconfigurationException(e.getMessage());
        }
        response.flushBuffer();
    }

    // GET [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}/konvertering
    @Operation(summary = "Return list of conversions related to the" +
            "documentObject identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "List of Conversions returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CONVERSION,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ConversionHateoas>
    findAllConversionAssociatedWithDocumentObject(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the documentObject",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(documentObjectService
                        .findAllConversionAssociatedWithDocumentObject(
                                systemID));
    }

    // GET [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}/konvertering/{subSystemID}
    @Operation(summary = "Return a conversion related to the documentObject " +
            "identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Conversion returned"),
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
            CONVERSION + SLASH + SUB_SYSTEM_ID_PARAMETER,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ConversionHateoas>
    findAllConversionAssociatedWithDocumentObject(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "subSystemID",
                    required = true)
            @PathVariable("subSystemID") final UUID subSystemID) {
        return ResponseEntity.status(OK)
                .body(documentObjectService
                        .findConversionAssociatedWithDocumentObject
                                (systemID, subSystemID));
    }

    // GET [contextPath][api]/arkivstruktur/dokumentobject/{systemId}/ny-konvertering
    @Operation(summary = "Create a Conversion with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Conversion returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CONVERSION,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ConversionHateoas> createDefaultConversion(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of documentObject to associate " +
                            "the conversion with.",
                    required = true)
            @PathVariable UUID systemID) {
        return ResponseEntity.status(OK)
                .body(documentObjectService
                        .generateDefaultConversion(systemID));
    }

    // API - All POST Requests (CRUD - CREATE)

    // POST [contextPath][api]/arkivstruktur/dokumentobject/{systemId}/ny-konvertering
    @Operation(summary = "Persists a Conversion object associated with " +
            "the given DocumentObject systemId",
            description = "Returns the newly created Conversion after it was " +
                    "associated with a DocumentObject object and  persisted " +
                    "to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Conversion " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Conversion " +
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
                            " of type Conversion"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CONVERSION,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ConversionHateoas>
    createConversionAssociatedWithDocumentObject(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of documentObject to associate " +
                            "the conversion with.",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "conversion",
                    description = "Incoming documentObject object",
                    required = true)
            @RequestBody Conversion conversion)
            throws NikitaException {
        ConversionHateoas conversionHateoas = documentObjectService
                .createConversionAssociatedWithDocumentObject(
                        systemID, conversion);
        return ResponseEntity.status(CREATED)
                .body(conversionHateoas);
    }

    // upload a file and associate it with a documentObject
    // POST [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}/referanseFil
    @Operation(summary = "Uploads a file and associates it with the " +
            "documentObject identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "File uploaded successfully"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + REFERENCE_FILE,
            headers = "Accept=*/*",
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentObjectHateoas> handleFileUpload(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the documentObject you wish to" +
                            " associate a file with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws IOException {
        DocumentObjectHateoas documentObjectHateoas =
                documentObjectService.handleIncomingFile(systemID, request);
        return ResponseEntity.status(CREATED)
                .body(documentObjectHateoas);
    }

    // Delete a DocumentObject identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/dokumentobjekt/{systemId}
    @Operation(summary = "Deletes a single DocumentObject object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Conversion deleted"),
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
    public ResponseEntity<String> deleteDocumentObjectBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the documentObject to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        documentObjectService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Delete a Conversion object identified by dokumentobjekt and
    // Conversion systemID
    // DELETE [contextPath][api]/arkivstruktur/dokumentobjekt/{systemId}/konvertering/{systemId}
    @Operation(summary = "Deletes a single Conversion object associated with " +
            "the given DocumentObject")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Conversion deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER +
            SLASH + CONVERSION + SLASH + SUB_SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteConversionByDocumentObjectAndSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the documentObject to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "subSystemID",
                    required = true)
            @PathVariable("subSystemID") final UUID subSystemID) {
        documentObjectService.deleteConversion(systemID, subSystemID);
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Delete all DocumentObject
    // DELETE [contextPath][api]/arkivstruktur/dokumentobjekt/
    @Operation(summary = "Deletes all DocumentObject")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all DocumentObject"),
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
    public ResponseEntity<String> deleteAllDocumentObject() {
        documentObjectService.deleteAll();
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a DocumentObject
    // PUT [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}
    @Operation(summary = "Updates a DocumentObject object",
            description = "Returns the newly update DocumentObject object " +
                    "after it is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentObject " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "DocumentObject " +
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
                            " of type DocumentObject"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentObjectHateoas> updateDocumentObject(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of documentObject to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "documentObject",
                    description = "Incoming documentObject object",
                    required = true)
            @RequestBody DocumentObject documentObject)
            throws NikitaException {
        DocumentObjectHateoas documentObjectHateoas =
                documentObjectService.handleUpdate
                        (systemID, parseETAG(
                                request.getHeader(ETAG)), documentObject);
        return ResponseEntity.status(OK)
                .body(documentObjectHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}/konvertering/{systemID}
    @Operation(summary = "Updates a Conversion object",
            description = "Returns the newly updateConversion object after it" +
                    " is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Conversion " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Conversion " +
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
                            " of type Conversion"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CONVERSION +
            SLASH + SUB_SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ConversionHateoas> updateConversion(
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of conversion to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "subSystemID",
                    description = "systemId of conversion to update.",
                    required = true)
            @PathVariable("subSystemID") UUID subSystemID,
            @Parameter(name = "conversion",
                    description = "Incoming conversion object",
                    required = true)
            @RequestBody Conversion conversion) throws NikitaException {
        return ResponseEntity.status(OK)
                .body(documentObjectService.handleUpdateConversionBySystemId
                        (systemID, subSystemID, conversion));
    }
}
