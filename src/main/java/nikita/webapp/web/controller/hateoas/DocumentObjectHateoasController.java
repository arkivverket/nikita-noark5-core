package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ConversionHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.IDocumentObjectHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IDocumentObjectService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CONVERSION;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID_PARAMETER;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_DOCUMENT_OBJECT)
public class DocumentObjectHateoasController
        extends NoarkController {

    private IDocumentObjectService documentObjectService;
    private IDocumentObjectHateoasHandler documentObjectHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public DocumentObjectHateoasController(
            IDocumentObjectService documentObjectService,
            IDocumentObjectHateoasHandler documentObjectHateoasHandler,
            ApplicationEventPublisher applicationEventPublisher) {
        this.documentObjectService = documentObjectService;
        this.documentObjectHateoasHandler = documentObjectHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All GET Requests (CRUD - READ)
    // Get a documentObject identified by systemID
    // GET [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}
    @ApiOperation(value = "Retrieves a single DocumentObject entity " +
            "identified by given a systemId",
            response = DocumentObjectHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "DocumentObject returned",
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentObjectHateoas> findOneDocumentObjectBySystemId(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentObject to retrieve",
                    required = true)
            @PathVariable("systemID") final String documentObjectSystemId) {
        return documentObjectService.findBySystemId(documentObjectSystemId);
    }

    // Get all documentObject
    // GET [contextPath][api]/arkivstruktur/dokumentobjekt/
    @ApiOperation(value = "Retrieves multiple DocumentObject entities limited by ownership rights", notes = "The field skip" +
            "tells how many DocumentObject rows of the result set to ignore (starting at 0), while  top tells how many rows" +
            " after skip to return. Note if the value of top is greater than system value " +
            " nikita-noark5-core.pagination.maxPageSize, then nikita-noark5-core.pagination.maxPageSize is used. ",
            response = DocumentObjectHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentObject list found",
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentObjectHateoas> findAllDocumentObject(
            HttpServletRequest request) {
        DocumentObjectHateoas documentObjectHateoas = new
                DocumentObjectHateoas((List<INikitaEntity>) (List)
                documentObjectService.findDocumentObjectByOwner());
        documentObjectHateoasHandler.addLinks(documentObjectHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentObjectHateoas);
    }

    // Get a file identified by systemID retrievable with referanseFile
    // GET [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}/referanseFil
    @ApiOperation(value = "Downloads a file associated with the documentObject" +
            " identified by a systemId", response = DocumentObjectHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "File download successful",
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + REFERENCE_FILE)
    public void handleFileDownload(
            HttpServletRequest request, HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentObject that has a file " +
                            "associated with it",
                    required = true)
            @PathVariable("systemID") final String documentObjectSystemId)
            throws IOException {

        Resource fileResource = documentObjectService.loadAsResource(
                documentObjectSystemId, request, response);

        InputStream filestream = fileResource.getInputStream();
        try {
            long bytesTotal = IOUtils.copyLarge(filestream,
                    response.getOutputStream());
            filestream.close();
        } finally {
            try {
                // Try close without exceptions if copy() threw an
                // exception.  If close() is called twice, the second
                // close() should be ignored.
                filestream.close();
            } catch (IOException e) {
                // swallow any error to expose exceptions from
                // IOUtil.copy() if the second close() failed.
            }
        }
        response.flushBuffer();
    }

    // GET [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}/konvertering
    @ApiOperation(value = "Return list of conversions related to the" +
		  "documentObject identified by a systemId",
		  response = ConversionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "List of Conversions returned",
                    response = ConversionHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CONVERSION,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ConversionHateoas>
    findAllConversionAssociatedWithDocumentObject(
            HttpServletRequest request, HttpServletResponse response,
            @ApiParam(name = "systemID",
		      value = "systemID of the documentObject",
		      required = true)
            @PathVariable("systemID") final String systemID)
            throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentObjectService.findAllConversionAssociatedWithDocumentObject(systemID));
    }

    // API - All POST Requests (CRUD - CREATE)

    // upload a file and associate it with a documentObject
    // POST [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}/referanseFil
    @ApiOperation(value = "Uploads a file and associates it with the documentObject identified by a systemId",
            response = DocumentObjectHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201,
                    message = "File uploaded successfully",
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + REFERENCE_FILE,
                 headers = "Accept=*/*",
                 produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentObjectHateoas> handleFileUpload(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentObject you wish to " +
                            "associate a file with",
                    required = true)
            @PathVariable("systemID") final String systemID)
            throws IOException {
        return documentObjectService.handleIncomingFile(systemID, request);
    }

    // konverterFil
    // upload a file and associate it with a documentObject
    // POST [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}/konverterFil
    @ApiOperation(value = "Converts the file associated with the " +
            "documentObject identified by a systemId",
            response = DocumentObjectHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File convertedsuccessfully",
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CONVERT_FILE,
                headers = "Accept=*/*",
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentObjectHateoas> convertFile(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentObject you wish to " +
                            "convert the file that is associated with it",
                    required = true)
            @PathVariable("systemID") final String documentObjectSystemId)
            throws Exception {
        return documentObjectService.
                convertDocumentToPDF(documentObjectSystemId);
    }

    // Delete a DocumentObject identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/dokumentobjekt/{systemId}/
    @ApiOperation(value = "Deletes a single DocumentObject entity identified " +
            "by systemID", response = Count.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Delete DocumentObject",
                    response = Count.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<Void> deleteDocumentObjectBySystemId(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentObject to delete",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        documentObjectService.deleteEntity(systemID);
        return ResponseEntity.
                status(NO_CONTENT).
                body(null);
    }

    // Delete all DocumentObject
    // DELETE [contextPath][api]/arkivstruktur/dokumentobjekt/
    @ApiOperation(value = "Deletes all DocumentObject", response = Count.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all DocumentObject",
                    response = Count.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<Count> deleteAllDocumentObject() {
        return ResponseEntity.status(NO_CONTENT).
                body(new Count(documentObjectService.deleteAll()));
    }


    // API - All PUT Requests (CRUD - UPDATE)
    // Update a DocumentObject
    // PUT [contextPath][api]/arkivstruktur/dokumentobjekt/{systemID}
    @ApiOperation(value = "Updates a DocumentObject object", notes = "Returns the newly" +
            " update DocumentObject object after it is persisted to the database", response = DocumentObjectHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentObject " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 201, message = "DocumentObject " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type DocumentObject"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentObjectHateoas> updateDocumentObject(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of documentObject to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @ApiParam(name = "documentObject",
                    value = "Incoming documentObject object",
                    required = true)
            @RequestBody DocumentObject documentObject) throws NikitaException {
        validateForUpdate(documentObject);

        DocumentObject updatedDocumentObject = documentObjectService.handleUpdate(systemID, parseETAG(request.getHeader(ETAG)), documentObject);
        DocumentObjectHateoas documentObjectHateoas = new DocumentObjectHateoas(updatedDocumentObject);
        documentObjectHateoasHandler.addLinks(documentObjectHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, updatedDocumentObject));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedDocumentObject.getVersion().toString())
                .body(documentObjectHateoas);
    }

}
