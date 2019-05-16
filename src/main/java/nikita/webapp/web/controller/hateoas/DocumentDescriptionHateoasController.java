package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v4.DocumentDescription;
import nikita.common.model.noark5.v4.DocumentObject;
import nikita.common.model.noark5.v4.Record;
import nikita.common.model.noark5.v4.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v4.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v4.hateoas.RecordHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.IDocumentObjectHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH +
        SLASH + DOCUMENT_DESCRIPTION, produces = {NOARK5_V4_CONTENT_TYPE_JSON
        , NOARK5_V4_CONTENT_TYPE_JSON_XML})
@SuppressWarnings("unchecked")
public class DocumentDescriptionHateoasController
        extends NoarkController {

    private IDocumentDescriptionService documentDescriptionService;
    private IDocumentDescriptionHateoasHandler documentDescriptionHateoasHandler;
    private IDocumentObjectHateoasHandler documentObjectHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;
    private IRecordHateoasHandler recordHateoasHandler;

    public DocumentDescriptionHateoasController(
            IDocumentDescriptionService documentDescriptionService,
            IDocumentDescriptionHateoasHandler documentDescriptionHateoasHandler,
            IDocumentObjectHateoasHandler documentObjectHateoasHandler,
            ApplicationEventPublisher applicationEventPublisher,
            IRecordHateoasHandler recordHateoasHandler) {
        this.documentDescriptionService = documentDescriptionService;
        this.documentDescriptionHateoasHandler = documentDescriptionHateoasHandler;
        this.documentObjectHateoasHandler = documentObjectHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
        this.recordHateoasHandler = recordHateoasHandler;
    }

    // API - All POST Requests (CRUD - CREATE)

    @ApiOperation(value = "Persists a DocumentObject object associated with " +
            "the given DocumentDescription systemId",
            notes = "Returns the newly created documentObject after it was " +
                    "associated with a DocumentDescription object and " +
                    "persisted to the database",
            response = DocumentObjectHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "DocumentObject " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 201, message = "DocumentObject " +
                    API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type DocumentObject"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SYSTEM_ID_PARAMETER + NEW_DOCUMENT_OBJECT,
            consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<DocumentObjectHateoas>
    createDocumentObjectAssociatedWithDocumentDescription(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of documentDescription to associate the" +
                            " documentObject with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "documentObject",
                    value = "Incoming documentObject object",
                    required = true)
            @RequestBody DocumentObject documentObject)
            throws NikitaException {
        DocumentObject createdDocumentObject =
                documentDescriptionService.
                        createDocumentObjectAssociatedWithDocumentDescription(
                systemID, documentObject);
        DocumentObjectHateoas documentObjectHateoas =
                new DocumentObjectHateoas(documentObject);
        documentObjectHateoasHandler.addLinks(documentObjectHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityCreatedEvent(this,
                createdDocumentObject));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdDocumentObject.getVersion().toString())
                .body(documentObjectHateoas);
    }

    // API - All GET Requests (CRUD - READ)

    @ApiOperation(value = "Retrieves a single DocumentDescription entity " +
            "given a systemId", response = DocumentDescriptionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentDescription returned",
                    response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SYSTEM_ID_PARAMETER)
    public ResponseEntity<DocumentDescriptionHateoas>
    findOneDocumentDescriptionBySystemId(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentDescription to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        DocumentDescription documentDescription = documentDescriptionService.
                findBySystemId(systemID);
        if (documentDescription == null) {
            throw new NoarkEntityNotFoundException(systemID);
        }
        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas(documentDescription);
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(documentDescription.getVersion().toString())
                .body(documentDescriptionHateoas);
    }

    @ApiOperation(value = "Retrieves multiple DocumentDescription entities " +
            "limited by ownership rights",  
            response = DocumentDescriptionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentDescription list found",
                    response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping
    public ResponseEntity<DocumentDescriptionHateoas> findAllDocumentDescription(
            HttpServletRequest request) {
        String ownedBy = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas((List<INikitaEntity>) (List)
                documentDescriptionService.findByOwnedBy(ownedBy));
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionHateoas);
    }

    // Create a DocumentObject with default values
    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-dokumentobjekt
    @ApiOperation(value = "Create a DocumentObject with default values",
            response = DocumentObjectHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentObject returned",
                    response = DocumentObjectHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SYSTEM_ID_PARAMETER + NEW_DOCUMENT_OBJECT)
    public ResponseEntity<DocumentObjectHateoas> createDefaultDocumentObject(
            HttpServletRequest request) {

        DocumentObject defaultDocumentObject = new DocumentObject();
        // This is just temporary code as this will have to be replaced if
        // this ever goes into production
        defaultDocumentObject.setMimeType(MediaType.APPLICATION_XML.toString());
        defaultDocumentObject.setVariantFormat(PRODUCTION_VERSION);
        defaultDocumentObject.setFormat("XML");
        defaultDocumentObject.setVersionNumber(1);

        DocumentObjectHateoas documentObjectHateoas = new
                DocumentObjectHateoas(defaultDocumentObject);
        documentObjectHateoasHandler.addLinksOnNew(documentObjectHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentObjectHateoas);
    }

    // Retrieve all DocumentObjects associated with a DocumentDescription
    // identified by systemId
    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/dokumentobjekt
    @ApiOperation(value = "Retrieves a list of DocumentObjects associated " +
            "with a DocumentDescription",
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
    @GetMapping(value = SYSTEM_ID_PARAMETER + DOCUMENT_OBJECT)
    public ResponseEntity<DocumentObjectHateoas>
    findAllDocumentDescriptionAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable("systemID") final String systemID) {

        DocumentDescription documentDescription =
                documentDescriptionService.findBySystemId(systemID);
        if (documentDescription == null) {
            throw new NoarkEntityNotFoundException("Could not find " +
                    "DocumentDescription object with systemID " +
                    systemID);
        }
        DocumentObjectHateoas documentObjectHateoas = new
                DocumentObjectHateoas((List<INikitaEntity>)
                (List) documentDescription
                        .getReferenceDocumentObject());
        documentObjectHateoasHandler.addLinks(documentObjectHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(
                        request.getServletPath()))
                .body(documentObjectHateoas);
    }

    // Delete a DocumentDescription identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/dokumentobjekt/{systemId}/
    @ApiOperation(value = "Deletes a single DocumentDescription entity " +
            "identified by systemID", response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Parent Fonds returned",
                    response = RecordHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SYSTEM_ID_PARAMETER)
    public ResponseEntity<RecordHateoas> deleteAllDocumentDescriptionBySystemId(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentDescription to delete",
                    required = true)
            @PathVariable("systemID") final String systemID) {

        DocumentDescription documentDescription =
                documentDescriptionService.findBySystemId(systemID);
        List<Record> record = new ArrayList<>();
        documentDescription.getReferenceRecord().addAll(record);
        documentDescriptionService.deleteEntity(systemID);
        RecordHateoas recordHateoas = new RecordHateoas((List)
                record);
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, documentDescription));
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordHateoas);
    }


    // Delete all DocumentDescription
    // DELETE [contextPath][api]/arkivstruktur/dokumentbeskrivelse/
    @ApiOperation(value = "Deletes all DocumentDescription",
            response = Count.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "Deleted all DocumentDescription",
                    response = Count.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<Count> deleteAllDocumentDescription() {
        return ResponseEntity.status(NO_CONTENT).
                body(new Count(
                        documentDescriptionService.deleteAllByOwnedBy()));
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a DocumentDescription
    // PUT [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemID}
    @ApiOperation(value = "Updates a DocumentDescription object",
            notes = "Returns the newly updated DocumentDescription object " +
                    "after it is persisted to the database",
            response = DocumentDescriptionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentDescription " +
                    API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 201, message = "DocumentDescription " +
                    API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type DocumentDescription"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = SYSTEM_ID_PARAMETER,
            consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<DocumentDescriptionHateoas> updateDocumentDescription(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of documentDescription to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @ApiParam(name = "documentDescription",
                    value = "Incoming documentDescription object",
                    required = true)
            @RequestBody DocumentDescription documentDescription)
            throws NikitaException {
        validateForUpdate(documentDescription);

        DocumentDescription updatedDocumentDescription =
                documentDescriptionService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)), documentDescription);
        DocumentDescriptionHateoas documentDescriptionHateoas =
                new DocumentDescriptionHateoas(updatedDocumentDescription);
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this,
                        updatedDocumentDescription));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedDocumentDescription.getVersion().toString())
                .body(documentDescriptionHateoas);
    }
}
