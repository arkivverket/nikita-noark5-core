package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.PartUnit;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.IDocumentObjectHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import nikita.webapp.service.interfaces.IDocumentObjectService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_DOCUMENT_DESCRIPTION,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class DocumentDescriptionHateoasController
        extends NoarkController {

    private IDocumentDescriptionService documentDescriptionService;
    private IDocumentObjectService documentObjectService;
    private IDocumentDescriptionHateoasHandler documentDescriptionHateoasHandler;
    private IDocumentObjectHateoasHandler documentObjectHateoasHandler;

    public DocumentDescriptionHateoasController(
            IDocumentDescriptionService documentDescriptionService,
            IDocumentObjectService documentObjectService,
            IDocumentDescriptionHateoasHandler documentDescriptionHateoasHandler,
            IDocumentObjectHateoasHandler documentObjectHateoasHandler) {
        this.documentDescriptionService = documentDescriptionService;
        this.documentObjectService = documentObjectService;
        this.documentDescriptionHateoasHandler = documentDescriptionHateoasHandler;
        this.documentObjectHateoasHandler = documentObjectHateoasHandler;
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_OBJECT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
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
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdDocumentObject.getVersion().toString())
                .body(documentObjectHateoas);
    }

    // Create a new PartUnit and associate it with the given documentDescription
    // POST [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-partenhet
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-partenhet/
    @ApiOperation(value = "Persists a PartUnit object " +
            "associated with the given DocumentDescription systemId",
            notes = "Returns the newly created PartUnit object " +
                    "after it was associated with a DocumentDescription object and " +
                    "persisted to the database",
            response = PartUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PartUnit " +
                    API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 201, message = "PartUnit " +
                    API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type PartUnit"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_UNIT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartUnitHateoas>
    createPartUnitAssociatedWithDocumentDescription(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of documentDescription to associate the " +
                            "PartUnit with.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @ApiParam(name = "PartUnit",
                    value = "Incoming PartUnit object",
                    required = true)
            @RequestBody PartUnit partUnit)
            throws NikitaException {

        PartUnitHateoas partUnitHateoas =
                documentDescriptionService.
                        createPartUnitAssociatedWithDocumentDescription(
                                systemID, partUnit);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partUnitHateoas.getEntityVersion().toString())
                .body(partUnitHateoas);
    }

    // Create a new PartPerson and associate it with the given documentDescription
    // POST [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-partenhet
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-partenhet/
    @ApiOperation(value = "Persists a PartPerson object " +
            "associated with the given DocumentDescription systemId",
            notes = "Returns the newly created PartPerson object after it " +
                    "was associated with a DocumentDescription object and " +
                    "persisted to the database",
            response = PartPersonHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PartPerson " +
                    API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 201, message = "PartPerson " +
                    API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type PartPerson"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_PERSON,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartPersonHateoas>
    createPartPersonAssociatedWithDocumentDescription(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of documentDescription to associate the " +
                            "PartPerson with.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @ApiParam(name = "PartPerson",
                    value = "Incoming PartPerson object",
                    required = true)
            @RequestBody PartPerson partPerson)
            throws NikitaException {

        PartPersonHateoas partPersonHateoas =
                documentDescriptionService.
                        createPartPersonAssociatedWithDocumentDescription(
                                systemID, partPerson);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partPersonHateoas.getEntityVersion().toString())
                .body(partPersonHateoas);
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<DocumentDescriptionHateoas>
    findOneDocumentDescriptionBySystemId(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentDescription to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return documentDescriptionService.findBySystemId(systemID);
    }

    // Create a suggested PartUnit (like a template) object
    // with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-partenhet
    @ApiOperation(value = "Suggests the contents of a new Part " +
            "object", notes = "Returns a pre-filled Part object" +
            " with values relevant for the logged-in user",
            response = PartUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_UNIT)
    public ResponseEntity<PartUnitHateoas>
    getPartUnitTemplate(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentDescription to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService.
                        generateDefaultPartUnit(systemID));
    }

    // Create a suggested PartPerson (like a template) object
    // with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-partenhet
    @ApiOperation(value = "Suggests the contents of a new Part " +
            "object", notes = "Returns a pre-filled Part object" +
            " with values relevant for the logged-in user",
            response = PartPersonHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_PERSON)
    public ResponseEntity<PartPersonHateoas>
    getPartPersonTemplate(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentDescription to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService.
                        generateDefaultPartPerson(systemID));
    }

    @ApiOperation(value = "Retrieves multiple DocumentDescription entities " +
            "limited by ownership rights",
            response = DocumentDescriptionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "DocumentDescription list found",
                    response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping
    public ResponseEntity<DocumentDescriptionHateoas>
    findAllDocumentDescription() {
        return documentDescriptionService.findAll();
    }

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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_OBJECT)
    public ResponseEntity<DocumentObjectHateoas> createDefaultDocumentObject(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentObjectService.generateDefaultDocumentObject());
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
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + DOCUMENT_OBJECT)
    public ResponseEntity<DocumentObjectHateoas>
    findAllDocumentDescriptionAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the DocumentDescription to retrieve " +
                            "associated DocumentObject",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return documentDescriptionService.
                findAllDocumentObjectWithDocumentDescriptionBySystemId(
                        systemID);
    }

    // GET [contextPath][api]/sakarkiv/registrering/{systemId}/part
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/part/
    @ApiOperation(value = "Retrieves a list of Part associated with a DocumentDescription",
            response = PartHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Part returned",
                    response = PartHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + PART)
    public ResponseEntity<PartHateoas>
    findAllPartAssociatedWithDocumentDescription(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the file to retrieve associated DocumentDescription",
                    required = true)
            @PathVariable("systemID") final String systemID) {

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService.getPartAssociatedWithDocumentDescription(systemID));
    }
    // Retrieve all Record associated with a DocumentDescription identified
    // by systemId
    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/registrering
    @ApiOperation(value = "Retrieves a list of Record(s) associated " +
            "with a DocumentDescription",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Record returned",
                    response = RecordHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + RECORD)
    public ResponseEntity<RecordHateoas>
    findAllRecordAssociatedWithDocumentDescription(
            @ApiParam(name = "systemID",
                    value = "systemID of the DocumentDescription to retrieve " +
                            "associated Records",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return documentDescriptionService.
                findAllRecordWithDocumentDescriptionBySystemId(
                        systemID);
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
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<Count> deleteDocumentDescriptionBySystemId(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the documentDescription to delete",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return ResponseEntity.status(NO_CONTENT).
                body(new Count(documentDescriptionService.
                        deleteEntity(systemID)));
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

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
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
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedDocumentDescription.getVersion().toString())
                .body(documentDescriptionHateoas);
    }
}
