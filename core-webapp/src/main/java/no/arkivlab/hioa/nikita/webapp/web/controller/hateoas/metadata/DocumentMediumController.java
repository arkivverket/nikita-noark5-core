package no.arkivlab.hioa.nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.config.Constants;
import nikita.model.noark5.v4.metadata.DocumentMedium;
import nikita.util.exceptions.NikitaException;
import no.arkivlab.hioa.nikita.webapp.service.interfaces.metadata.IDocumentMediumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static nikita.config.Constants.*;
import static nikita.config.N5ResourceMappings.DOCUMENT_MEDIUM;

@RestController
@RequestMapping(value = Constants.HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH + DOCUMENT_MEDIUM,
        produces = {NOARK5_V4_CONTENT_TYPE_JSON, NOARK5_V4_CONTENT_TYPE_JSON_XML})
public class DocumentMediumController {

    private IDocumentMediumService documentMediumService;

    public DocumentMediumController(IDocumentMediumService documentMediumService) {
        this.documentMediumService = documentMediumService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new documentmedium
    // POST [contextPath][api]/metadata/dokumentmedium/ny-dokumentmedium
    @ApiOperation(value = "Persists a new DocumentMedium object", notes = "Returns the newly" +
            " created DocumentMedium object after it is persisted to the database", response = DocumentMedium.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentMedium " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentMedium.class),
            @ApiResponse(code = 201, message = "DocumentMedium " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentMedium.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted
    @Timed
    @RequestMapping(method = RequestMethod.POST, value = SLASH + NEW_DOCUMENT_MEDIUM)
    public ResponseEntity<DocumentMedium> createDocumentMedium(@RequestBody DocumentMedium documentMedium)
            throws NikitaException {
        DocumentMedium newDocumentMedium = documentMediumService.createNewDocumentMedium(documentMedium);
        return new ResponseEntity<>(newDocumentMedium, HttpStatus.CREATED);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all documentMedium
    // GET [contextPath][api]/metadata/dokumentmedium/
    @ApiOperation(value = "Retrieves all DocumentMedium ", response = DocumentMedium.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentMedium codes found",
                    response = DocumentMedium.class),
            @ApiResponse(code = 404, message = "No DocumentMedium found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @Timed
    @RequestMapping(method = RequestMethod.GET, value = SLASH)
    public ResponseEntity<Iterable<DocumentMedium>> findAll() {
        Iterable<DocumentMedium> documentMediumList = documentMediumService.findAll();
        return new ResponseEntity<>(documentMediumList, HttpStatus.OK);
    }

    // Retrieves a given documentMedium identified by a systemId
    // GET [contextPath][api]/metadata/dokumentmedium/{systemId}/
    @ApiOperation(value = "Gets documentMedium identified by its systemId", notes = "Returns the requested " +
            " documentMedium object", response = DocumentMedium.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentMedium " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentMedium.class),
            @ApiResponse(code = 201, message = "DocumentMedium " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentMedium.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted
    @Timed
    @RequestMapping(value = SLASH + "{systemID}" + SLASH, method = RequestMethod.GET)
    public ResponseEntity<DocumentMedium> findBySystemId(@PathVariable("systemID") final String systemId) {
        DocumentMedium documentMediumList = documentMediumService.findBySystemId(systemId);
        return new ResponseEntity<>(documentMediumList, HttpStatus.OK);
    }

    // Create a suggested documentMedium(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/metadata/dokumentmedium/ny-dokumentmedium
    @ApiOperation(value = "Creates a suggested DocumentMedium", response = DocumentMedium.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentMedium codes found",
                    response = DocumentMedium.class),
            @ApiResponse(code = 404, message = "No DocumentMedium found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @Timed
    @RequestMapping(method = RequestMethod.GET, value = SLASH + NEW_DOCUMENT_MEDIUM)
    public ResponseEntity<DocumentMedium> getDocumentMediumTemplate() {
        DocumentMedium documentMedium = new DocumentMedium();
        documentMedium.setCode(TEMPLATE_DOCUMENT_MEDIUM_CODE);
        documentMedium.setDescription(TEMPLATE_DOCUMENT_MEDIUM_DESCRIPTION);
        return new ResponseEntity<>(documentMedium, HttpStatus.OK);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a documentmedium
    // PUT [contextPath][api]/metatdata/dokumentmedium/
    @ApiOperation(value = "Updates a DocumentMedium object", notes = "Returns the newly" +
            " updated DocumentMedium object after it is persisted to the database", response = DocumentMedium.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentMedium " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentMedium.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @Timed
    @RequestMapping(method = RequestMethod.PUT, value = SLASH + DOCUMENT_MEDIUM)
    public ResponseEntity<DocumentMedium> updateDocumentMedium(@RequestBody DocumentMedium documentMedium)
            throws NikitaException {
        DocumentMedium newDocumentMedium = documentMediumService.update(documentMedium);
        return new ResponseEntity<>(newDocumentMedium, HttpStatus.OK);
    }
}
