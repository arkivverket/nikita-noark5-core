package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.metadata.IDocumentMediumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CODE;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_MEDIUM;

@RestController
@RequestMapping(value = Constants.HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH,
        produces = {NOARK5_V5_CONTENT_TYPE_JSON, NOARK5_V5_CONTENT_TYPE_JSON_XML})
public class DocumentMediumController {

    private IDocumentMediumService documentMediumService;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public DocumentMediumController(IDocumentMediumService documentMediumService,
                                    IMetadataHateoasHandler metadataHateoasHandler) {
        this.documentMediumService = documentMediumService;
        this.metadataHateoasHandler = metadataHateoasHandler;
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

    @RequestMapping(method = RequestMethod.POST, value = DOCUMENT_MEDIUM + SLASH + NEW_DOCUMENT_MEDIUM)
    public ResponseEntity<MetadataHateoas> createDocumentMedium(
            HttpServletRequest request,
            @RequestBody DocumentMedium documentMedium)
            throws NikitaException {
        DocumentMedium newDocumentMedium = documentMediumService.createNewDocumentMedium(documentMedium);
        MetadataHateoas metadataHateoas = new MetadataHateoas(newDocumentMedium);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(newDocumentMedium.getVersion().toString())
                .body(metadataHateoas);
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

    @RequestMapping(method = RequestMethod.GET, value = DOCUMENT_MEDIUM)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        //ArrayList <DocumentMedium> documentMediumList = (ArrayList<DocumentMedium>) documentMediumService.findAll2();
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>)
                        (List) documentMediumService.findAll(),
                DOCUMENT_MEDIUM);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // Retrieves a given documentMedium identified by a kode
    // GET [contextPath][api]/metadata/dokumentmedium/{kode}/
    @ApiOperation(value = "Gets documentMedium identified by its kode", notes = "Returns the requested " +
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

    @RequestMapping(value =
            DOCUMENT_MEDIUM + SLASH + LEFT_PARENTHESIS + CODE + RIGHT_PARENTHESIS + SLASH, method = RequestMethod.GET)
    public ResponseEntity<MetadataHateoas> findByCode(@PathVariable("kode") final String code,
                                                          HttpServletRequest request) {
        DocumentMedium documentMedium = documentMediumService.findByCode(code);
        MetadataHateoas metadataHateoas = new MetadataHateoas(documentMedium);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(documentMedium.getVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested documentMedium(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-dokumentmedium
    @ApiOperation(value = "Creates a suggested DocumentMedium", response = DocumentMedium.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentMedium codes found",
                    response = DocumentMedium.class),
            @ApiResponse(code = 404, message = "No DocumentMedium found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = RequestMethod.GET, value = NEW_DOCUMENT_MEDIUM)
    public ResponseEntity<MetadataHateoas> getDocumentMediumTemplate(HttpServletRequest request) {
        DocumentMedium documentMedium = new DocumentMedium();
        documentMedium.setCode(TEMPLATE_DOCUMENT_MEDIUM_CODE);
        documentMedium.setCode(TEMPLATE_DOCUMENT_MEDIUM_NAME);
        MetadataHateoas metadataHateoas = new MetadataHateoas(documentMedium);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
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

    @RequestMapping(method = RequestMethod.PUT, value = DOCUMENT_MEDIUM + SLASH + DOCUMENT_MEDIUM)
    public ResponseEntity<MetadataHateoas> updateDocumentMedium(@RequestBody DocumentMedium documentMedium,
                                                                HttpServletRequest request)
            throws NikitaException {
        documentMediumService.update(documentMedium);
        MetadataHateoas metadataHateoas = new MetadataHateoas(documentMedium);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
