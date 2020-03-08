package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.IDocumentObjectHateoasHandler;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import nikita.webapp.service.interfaces.IDocumentObjectService;
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of documentDescription to associate the" +
                            " documentObject with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "documentObject",
                    value = "Incoming documentObject object",
                    required = true)
            @RequestBody DocumentObject documentObject)
            throws NikitaException {
        DocumentObjectHateoas documentObjectHateoas =
	    documentDescriptionService
	    .createDocumentObjectAssociatedWithDocumentDescription
	    (systemID, documentObject);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(documentObjectHateoas.getEntityVersion().toString())
                .body(documentObjectHateoas);
    }

    // Create a new Author and associate it with the given DocumentDescription
    // POST [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-forfatter
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-forfatter/
    @ApiOperation(
            value = "Persists an author object associated with the given " +
                    "DocumentDescription systemId",
            notes = "Returns the newly created author object after it was " +
                    "associated with a DocumentDescription object and persisted to the " +
                    "database",
            response = AuthorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Author " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = AuthorHateoas.class),
            @ApiResponse(code = 201,
                    message = "Author " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = AuthorHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " +
                            "DocumentDescription"),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_AUTHOR,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<AuthorHateoas>
    addAuthorAssociatedWithDocumentDescription(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of the documentDescription to associate" +
                            " the author with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "author",
                    value = "Incoming author object",
                    required = true)
            @RequestBody Author author)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(documentDescriptionService.
                        associateAuthorWithDocumentDescription(
                                systemID, author));
    }

    // POST [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-merknad/
    @ApiOperation(value = "Associates a Comment with a DocumentDescription identified by systemID",
            notes = "Returns the comment", response = CommentHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = COMMENT + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CommentHateoas.class),
            @ApiResponse(code = 201, message = COMMENT + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CommentHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + COMMENT),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_COMMENT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CommentHateoas> addCommentToDocumentDescription(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of DocumentDescription to associate the Comment with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Comment",
                    value = "comment",
                    required = true)
            @RequestBody Comment comment) throws NikitaException {
        CommentHateoas commentHateoas = documentDescriptionService
            .createCommentAssociatedWithDocumentDescription
            (systemID, comment);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(commentHateoas.getEntityVersion().toString())
                .body(commentHateoas);
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of documentDescription to associate the " +
                            "PartUnit with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of documentDescription to associate the " +
                            "PartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the documentDescription to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        DocumentDescriptionHateoas documentDescriptionHateoas =
            documentDescriptionService.findBySystemId(systemID);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(documentDescriptionHateoas.getEntityVersion().toString())
                .body(documentDescriptionHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-merknad
    @ApiOperation(value = "Create a Comment with default values", response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment returned", response = Comment.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_COMMENT)
    public ResponseEntity<CommentHateoas> createDefaultComment(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService.generateDefaultComment());
    }

    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/merknad/
    @ApiOperation(value = "Retrieves all Comments associated with a DocumentDescription identified by a systemId",
            response = CommentHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentDescription returned", response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + COMMENT)
    public ResponseEntity<CommentHateoas> findAllCommentsAssociatedWithDocumentDescription(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the documentDescription to retrieve comments for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService
                      .getCommentAssociatedWithDocumentDescription(systemID));
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the documentDescription to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService.
                        generateDefaultPartUnit(systemID));
    }

    // Create a suggested Author (like a template) object with default values
    // (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-forfatter
    @ApiOperation(value = "Suggests the contents of a new Author object",
            notes = "Returns a pre-filled Author object with values relevant " +
                    "for the logged-in user",
            response = AuthorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Author returned",
                    response = AuthorHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_AUTHOR)
    public ResponseEntity<AuthorHateoas>
    getAuthorTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the documentDescription to retrieve " +
                            "associated Author",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService.
                        generateDefaultAuthor(systemID));
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the documentDescription to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
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
    findAllDocumentDescription(HttpServletRequest request) {
        DocumentDescriptionHateoas documentDescriptionHateoas =
            documentDescriptionService.findAll();
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(documentDescriptionHateoas.getEntityVersion().toString())
                .body(documentDescriptionHateoas);
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the DocumentDescription to retrieve " +
                            "associated DocumentObject",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        DocumentObjectHateoas documentObjectHateoas =
	    documentDescriptionService.
                findAllDocumentObjectWithDocumentDescriptionBySystemId(
                        systemID);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentObjectHateoas);
    }

    // Retrieve all Authors associated with a DocumentDescription identified
    // by systemId
    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/forfatter
    @ApiOperation(value = "Retrieves a list of Authors associated with a " +
            "DocumentDescription",
            response = AuthorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Author returned",
                    response = AuthorHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + AUTHOR)
    public ResponseEntity<AuthorHateoas>
    findAllAuthorAssociatedWithDocumentDescription(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the DocumentDescription to retrieve " +
                            "associated Authors",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity
                .status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService.
                        findAllAuthorWithDocumentDescriptionBySystemId(
                                systemID));
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated DocumentDescription",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {

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
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the DocumentDescription to retrieve " +
                            "associated Records",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        RecordHateoas recordHateoas =
            documentDescriptionService.
                findAllRecordWithDocumentDescriptionBySystemId(
                        systemID);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordHateoas);
    }

    // Delete a DocumentDescription identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/dokumentobjekt/{systemId}/
    @ApiOperation(value = "Deletes a single DocumentDescription entity " +
            "identified by systemID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "Record deleted",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteDocumentDescriptionBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the documentDescription to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        documentDescriptionService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete all DocumentDescription
    // DELETE [contextPath][api]/arkivstruktur/dokumentbeskrivelse/
    @ApiOperation(value = "Deletes all DocumentDescription",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "All DocumentDescription deleted",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<String> deleteAllDocumentDescription() {
        documentDescriptionService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of documentDescription to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "documentDescription",
                    value = "Incoming documentDescription object",
                    required = true)
            @RequestBody DocumentDescription documentDescription)
            throws NikitaException {
        DocumentDescriptionHateoas documentDescriptionHateoas =
                documentDescriptionService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)), documentDescription);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(documentDescriptionHateoas.getEntityVersion().toString())
                .body(documentDescriptionHateoas);
    }
}
