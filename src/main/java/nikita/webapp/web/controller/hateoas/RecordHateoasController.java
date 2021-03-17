package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_RECORD,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class RecordHateoasController
        extends NoarkController {

    private final IDocumentDescriptionService documentDescriptionService;
    private final IRecordService recordService;
    private final IDocumentDescriptionHateoasHandler
            documentDescriptionHateoasHandler;
    private final IRecordHateoasHandler recordHateoasHandler;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RecordHateoasController(
            IDocumentDescriptionService documentDescriptionService,
            IRecordService recordService,
            IDocumentDescriptionHateoasHandler
                    documentDescriptionHateoasHandler,
            IRecordHateoasHandler recordHateoasHandler,
            ApplicationEventPublisher applicationEventPublisher) {
        this.documentDescriptionService = documentDescriptionService;
        this.recordService = recordService;
        this.documentDescriptionHateoasHandler = documentDescriptionHateoasHandler;
        this.recordHateoasHandler = recordHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Create a new DocumentDescription and associate it with the given Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-dokumentbeskrivelse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-dokumentbeskrivelse/
    @Operation(summary = "Persists a DocumentDescription object associated " +
            "with the given Record systemId",
            description = "Returns the newly created DocumentDescription " +
                    "object after it was associated with a Record object and " +
                    "persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentDescription " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "DocumentDescription " +
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
                            " of type DocumentDescription"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_DOCUMENT_DESCRIPTION,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentDescriptionHateoas>
    createDocumentDescriptionAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "documentDescription with.",
                    required = true)
            @PathVariable String systemID,
            @Parameter(name = "documentDescription",
                    description = "Incoming documentDescription object",
                    required = true)
            @RequestBody DocumentDescription documentDescription)
            throws NikitaException {
        validateForCreate(documentDescription);
        DocumentDescriptionHateoas documentDescriptionHateoas =
                recordService.
                        createDocumentDescriptionAssociatedWithRecord(
                                systemID, documentDescription);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(documentDescriptionHateoas.getEntityVersion().toString())
                .body(documentDescriptionHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/part
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/part/
    @Operation(summary = "Retrieves a list of Part associated with a Record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Part returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + PART)
    public ResponseEntity<PartHateoas>
    findAllPartAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.getPartAssociatedWithRecord(systemID));
    }

    // Retrieve all Authors associated with a Record identified by systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/forfatter
    @Operation(summary = "Retrieves a list of Authors associated with a " +
            "Record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Author returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + AUTHOR)
    public ResponseEntity<AuthorHateoas>
    findAllAuthorAssociatedWithRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Record to retrieve " +
                            "associated Authors",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity
                .status(OK)
                .body(recordService.
                        findAllAuthorWithRecordBySystemId(
                                systemID));
    }

    // Retrieve all CorrespondencePart associated with a Record identified by
    // systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/korrespondansepart
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/korrespondansepart/
    @Operation(summary = "Retrieves a list of CorrespondencePartHateoas " +
            "associated with a Record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CorrespondencePartUnit returned"),
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
            CORRESPONDENCE_PART)
    public ResponseEntity<CorrespondencePartHateoas>
    findAllCorrespondencePartUnitAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.getCorrespondencePartAssociatedWithRecord(
                        systemID));
    }

    // GET [contextPath][api]/sakarkiv/registrering/{systemId}/nasjonalidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/nasjonalidentifikator/
    @Operation(summary = "Retrieves a list of NationalIdentifier associated " +
            "with a File")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "NationalIdentifier returned"),
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
            NATIONAL_IDENTIFIER)
    public ResponseEntity<NationalIdentifierHateoas>
    findAllNIAssociatedWithFile(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated File",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService
                        .getNationalIdentifierAssociatedWithRecord(systemID));
    }

    // Add a reference to a secondary Series associated with the Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-referanseArkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-referanseArkivdel/
    @Operation(summary = "Associates a secondary Series with a Record " +
            "identified by systemID",
            description = "Returns the Record after the secondary Series is " +
                    "successfully associated with it. Note a secondary series" +
                    " allows a Record to be associated with another Series.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = CLASS + API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = CLASS +
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
                            " of type " + CLASS),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_REFERENCE_SERIES,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> addReferenceSeriesToRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "secondary Series with",
                    required = true)
            @PathVariable String systemID,
            @Parameter(name = "Series",
                    description = "series",
                    required = true)
            @RequestBody Series series) throws NikitaException {
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Create a new Author and associate it with the given Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-forfatter
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-forfatter/
    @Operation(
            summary = "Persists an author object associated with the given " +
                    "Record systemId",
            description = "Returns the newly created author object after it " +
                    "was associated with a Record object and persisted to the" +
                    " database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Author " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Author " +
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
                            " of type Record"),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_AUTHOR,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<AuthorHateoas> addAuthorAssociatedWithRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to associate the " +
                            "author with.",
                    required = true)
            @PathVariable String systemID,
            @Parameter(name = "author",
                    description = "Incoming author object",
                    required = true)
            @RequestBody Author author)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(recordService.associateAuthorWithRecord
                        (systemID, author));
    }

    // Create a suggested CorrespondencePartPerson (like a template)
    // object with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-korrespondansepartperson
    @Operation(summary = "Suggests the contents of a new CorrespondencePart " +
            "object",
            description = "Returns a pre-filled CorrespondencePart object" +
                    " with values relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CorrespondencePart " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
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
            NEW_CORRESPONDENCE_PART_PERSON)
    public ResponseEntity<CorrespondencePartPersonHateoas>
    getCorrespondencePartPersonTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.
                        generateDefaultCorrespondencePartPerson(systemID));
    }

    // Create a suggested CorrespondencePartUnit (like a template) object with
    // default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-korrespondansepartenhet
    @Operation(summary = "Suggests the contents of a new CorrespondencePart " +
            "object",
            description = "Returns a pre-filled CorrespondencePart object" +
                    " with values relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CorrespondencePart " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
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
            NEW_CORRESPONDENCE_PART_UNIT)
    public ResponseEntity<CorrespondencePartUnitHateoas>
    getCorrespondencePartUnitTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.
                        generateDefaultCorrespondencePartUnit(systemID));
    }

    // Create a suggested PartUnit (like a template) object with default values
    // (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-partenhet
    @Operation(summary = "Suggests the contents of a new Part object",
            description = "Returns a pre-filled Part object" +
                    " with values relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_UNIT)
    public ResponseEntity<PartUnitHateoas> getPartUnitTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "PartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID) throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.
                        generateDefaultPartUnit(systemID));
    }

    // Create a suggested PartUnit (like a template) object with default
    // values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-partperson
    @Operation(summary = "Suggests the contents of a new Part object",
            description = "Returns a pre-filled Part object with values " +
                    "relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_PERSON)
    public ResponseEntity<PartPersonHateoas> getPartPersonTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "PartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.
                        generateDefaultPartPerson(systemID));
    }

    // Create a suggested Author (like a template) object with default values
    // (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/dokumentbeskrivelse/{systemId}/ny-forfatter
    @Operation(summary = "Suggests the contents of a new Author object",
            description = "Returns a pre-filled Author object with values " +
                    "relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Author returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_AUTHOR)
    public ResponseEntity<AuthorHateoas>
    getAuthorTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to retrieve " +
                            "associated Author",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.
                        generateDefaultAuthor(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-merknad
    @Operation(summary = "Create a Comment with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Comment returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_COMMENT)
    public ResponseEntity<CommentHateoas> createDefaultComment(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultComment());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/merknad/
    @Operation(summary = "Retrieves all Comments associated with a Record " +
            "identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Record returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + COMMENT)
    public ResponseEntity<CommentHateoas> findAllCommentsAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to retrieve " +
                            "comments for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.getCommentAssociatedWithRecord(systemID));
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-merknad/
    @Operation(summary = "Associates a Comment with a Record identified by " +
            "systemID",
            description = "Returns the comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = COMMENT + API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = COMMENT + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type " + COMMENT),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_COMMENT,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CommentHateoas> addCommentToRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Comment with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "Comment",
                    description = "comment",
                    required = true)
            @RequestBody Comment comment) throws NikitaException {
        CommentHateoas commentHateoas = recordService
                .createCommentAssociatedWithRecord(systemID, comment);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(commentHateoas.getEntityVersion().toString())
                .body(commentHateoas);
    }

    // Create a new CorrespondencePartPerson and associate it with the given journalpost
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-korrespondansepartperson
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-korrespondansepartperson/
    @Operation(summary = "Persists a CorrespondencePartPerson object " +
            "associated with the given Record systemId",
            description = "Returns the newly created CorrespondencePartPerson" +
                    " object after it was associated with a Record object and" +
                    " persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CorrespondencePartPerson " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "CorrespondencePartPerson " +
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
                            " of type CorrespondencePartPerson"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_CORRESPONDENCE_PART_PERSON,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartPersonHateoas>
    createCorrespondencePartPersonAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "CorrespondencePartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "CorrespondencePartPerson",
                    description = "Incoming CorrespondencePartPerson object",
                    required = true)
            @RequestBody CorrespondencePartPerson correspondencePartPerson)
            throws NikitaException {
        CorrespondencePartPersonHateoas createdCorrespondencePartPerson =
                recordService.
                        createCorrespondencePartPersonAssociatedWithRecord(
                                systemID, correspondencePartPerson);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdCorrespondencePartPerson
                        .getEntityVersion().toString())
                .body(createdCorrespondencePartPerson);
    }

    // Create a new Part and associate it with the given journalpost
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-partperson
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-partperson/
    @Operation(summary = "Persists a Part object associated with the given " +
            "Record systemId",
            description = "Returns the newly created Part object after it was" +
                    " associated with a Record object and persisted to the " +
                    "database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Part " +
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
                            " of type Part"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_PART_PERSON,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartPersonHateoas>
    createPartPersonAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the Part " +
                            "with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "Part",
                    description = "Incoming Part object",
                    required = true)
            @RequestBody PartPerson partPerson)
            throws NikitaException {
        PartPersonHateoas partPersonHateoas =
                recordService.
                        createPartPersonAssociatedWithRecord(
                                systemID, partPerson);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partPersonHateoas.getEntityVersion().toString())
                .body(partPersonHateoas);
    }

    // Create a new Part and associate it with the given journalpost
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-partenhet
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-partenhet/
    @Operation(summary = "Persists a Part object associated with the given " +
            "Record systemId",
            description = "Returns the newly created Part object after it was" +
                    " associated with a Record object and persisted to the " +
                    "database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Part " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Part " +
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
                            " of type Part"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_UNIT,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartUnitHateoas>
    createPartUnitAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the Part " +
                            "with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "Part",
                    description = "Incoming Part object",
                    required = true)
            @RequestBody PartUnit partUnit)
            throws NikitaException {
        PartUnitHateoas partUnitHateoas =
                recordService.
                        createPartUnitAssociatedWithRecord(
                                systemID, partUnit);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partUnitHateoas.getEntityVersion().toString())
                .body(partUnitHateoas);
    }

    // Create a new CorrespondencePartInternal and associate it with the given
    // journalpost
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-korrespondansepartintern
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-korrespondansepartintern/
    @Operation(summary = "Persists a CorrespondencePartInternal object " +
            "associated with the given Record systemId",
            description = "Returns the newly created " +
                    "CorrespondencePartInternal object after it was " +
                    "associated with a Record object and persisted to the " +
                    "database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CorrespondencePartInternal " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "CorrespondencePartInternal " +
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
                            " of type CorrespondencePartInternal"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_CORRESPONDENCE_PART_INTERNAL,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartInternalHateoas>
    createCorrespondencePartInternalAssociatedWithRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "CorrespondencePartInternal with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "CorrespondencePartInternal",
                    description = "Incoming CorrespondencePartInternal object",
                    required = true)
            @RequestBody CorrespondencePartInternal correspondencePartInternal)
            throws NikitaException {
        return ResponseEntity.status(NOT_IMPLEMENTED).body(null);
    }

    // Create a new CorrespondencePartUnit and associate it with the given journalpost
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-korrespondansepartenhet
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-korrespondansepartenhet/
    @Operation(summary = "Persists a CorrespondencePartUnit object associated" +
            " with the given Record systemId",
            description = "Returns the newly created CorrespondencePartUnit " +
                    "object after it was associated with a Record object and" +
                    " persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CorrespondencePartUnit " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "CorrespondencePartUnit " +
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
                            " of type CorrespondencePartUnit"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_CORRESPONDENCE_PART_UNIT,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartUnitHateoas>
    createCorrespondencePartUnitAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "CorrespondencePartUnit with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "CorrespondencePartUnit",
                    description = "Incoming CorrespondencePartUnit object",
                    required = true)
            @RequestBody CorrespondencePartUnit correspondencePartUnit)
            throws NikitaException {
        CorrespondencePartUnitHateoas correspondencePartUnitHateoas =
                recordService.
                        createCorrespondencePartUnitAssociatedWithRecord(
                                systemID, correspondencePartUnit);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(correspondencePartUnitHateoas
                        .getEntityVersion().toString())
                .body(correspondencePartUnitHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-bygning
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-bygning/
    @Operation(summary = "Associates a Building (national identifier) with a" +
            " Record identified by systemID",
            description = "Returns the Record with the building associated " +
                    "with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = BUILDING +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = BUILDING +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_BUILDING,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<BuildingHateoas> addNIBuildingToRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Building with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "Building",
                    description = "building",
                    required = true)
            @RequestBody Building building)
            throws NikitaException {
        BuildingHateoas buildingHateoas =
                recordService.createBuildingAssociatedWithRecord(
                        systemID, building);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(buildingHateoas.getEntityVersion().toString())
                .body(buildingHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-matrikkel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-matrikkel/
    @Operation(summary = "Associates a CadastralUnit (national identifier) " +
            "with a Record identified by systemID",
            description = "Returns the Record with the CadastralUnit " +
                    "associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_CADASTRAL_UNIT,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CadastralUnitHateoas> addNICadastralUnitToRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "CadastralUnit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "CadastralUnit",
                    description = "CadastralUnit",
                    required = true)
            @RequestBody CadastralUnit cadastralUnit) throws NikitaException {
        CadastralUnitHateoas cadastralUnitHateoas =
                recordService.createCadastralUnitAssociatedWithRecord(
                        systemID, cadastralUnit);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(cadastralUnitHateoas.getEntityVersion().toString())
                .body(cadastralUnitHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-dnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-dnummer/
    @Operation(summary = "Associates a DNumber (national identifier) with a" +
            " Record identified by systemID",
            description = "Returns the Record with " +
                    "the DNumber associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = D_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = D_NUMBER +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_D_NUMBER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DNumberHateoas> addNIDNumberToRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "DNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "dNumber",
                    description = "DNumber",
                    required = true)
            @RequestBody DNumber dNumber) throws NikitaException {
        DNumberHateoas dNumberHateoas =
                recordService.createDNumberAssociatedWithRecord(
                        systemID, dNumber);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(dNumberHateoas.getEntityVersion().toString())
                .body(dNumberHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-plan
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-plan/
    @Operation(summary = "Associates a Plan (national identifier) with a" +
            " Record identified by systemID",
            description = "Returns the Record with the Plan associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = D_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = D_NUMBER +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PLAN,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PlanHateoas> addNIPlanToRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the Plan " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "plan",
                    description = "Plan",
                    required = true)
            @RequestBody Plan plan) throws NikitaException {
        PlanHateoas planHateoas =
                recordService.createPlanAssociatedWithRecord(systemID, plan);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(planHateoas.getEntityVersion().toString())
                .body(planHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-posisjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-posisjon/
    @Operation(summary = "Associates a Position (national identifier) with a" +
            " Record identified by systemID",
            description = "Returns the Record with the position associated " +
                    "with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = POSITION +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = POSITION +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_POSITION,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PositionHateoas> addNIPositionToRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Position with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "Position",
                    description = "position",
                    required = true)
            @RequestBody Position position)
            throws NikitaException {
        PositionHateoas positionHateoas =
                recordService.createPositionAssociatedWithRecord(
                        systemID, position);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(positionHateoas.getEntityVersion().toString())
                .body(positionHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-foedselsnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-foedselsnummer/
    @Operation(summary = "Associates a SocialSecurityNumber (national " +
            "identifier) with a Record identified by systemID",
            description = "Returns the Record with the socialSecurityNumber " +
                    "associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_SOCIAL_SECURITY_NUMBER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SocialSecurityNumberHateoas>
    addNISocialSecurityNumberToRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "SocialSecurityNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "SocialSecurityNumber",
                    description = "socialSecurityNumber",
                    required = true)
            @RequestBody SocialSecurityNumber socialSecurityNumber)
            throws NikitaException {
        SocialSecurityNumberHateoas socialSecurityNumberHateoas =
                recordService.createSocialSecurityNumberAssociatedWithRecord(
                        systemID, socialSecurityNumber);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(socialSecurityNumberHateoas.getEntityVersion().toString())
                .body(socialSecurityNumberHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-enhetsidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-enhetsidentifikator/
    @Operation(summary = "Associates a Unit (national identifier) with a " +
            "Record identified by systemID",
            description = "Returns the Record with the unit associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = NI_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = NI_UNIT +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_NI_UNIT,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<UnitHateoas> addNIUnitToRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Unit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "Unit",
                    description = "unit",
                    required = true)
            @RequestBody Unit unit)
            throws NikitaException {
        UnitHateoas unitHateoas =
                recordService.createUnitAssociatedWithRecord(
                        systemID, unit);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(unitHateoas.getEntityVersion().toString())
                .body(unitHateoas);
    }

    // Delete all Record
    // DELETE [contextPath][api]/arkivstruktur/registrering/
    @Operation(summary = "Deletes all Record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all Record"),
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
    public ResponseEntity<String> deleteAllRecord() {
        recordService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // API - All GET Requests (CRUD - READ)

    // Retrieve a Record identified by a systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/
    @Operation(summary = "Retrieves a single Record entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Record returned"),
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
    public ResponseEntity<RecordHateoas> findOneRecordbySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        Record record = recordService.findBySystemId(systemID);
        RecordHateoas recordHateoas = new RecordHateoas(record);
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(record.getVersion().toString())
                .body(recordHateoas);
    }

    // Retrieve all File associated with Record identified by a systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/mappe
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/mappe/
    @Operation(summary = "Retrieves a single File entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "File returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FILE)
    public ResponseEntity<FileHateoas> findParentFileByRecordSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return recordService.findFileAssociatedWithRecord(systemID);
    }

    // Retrieve the parent Series associated with the Record identified by the
    // given systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/arkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/
    @Operation(summary = "Retrieves a single Series entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Series returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SERIES)
    public ResponseEntity<SeriesHateoas> findParentSeriesByRecordSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return recordService.findSeriesAssociatedWithRecord(systemID);
    }

    // Retrieve the parent Class associated with the Record identified by the
    // given systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/klasse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/
    @Operation(summary = "Retrieves a single Class entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Class returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CLASS)
    public ResponseEntity<ClassHateoas> findParentClassByRecordSystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the class to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return recordService.findClassAssociatedWithRecord(systemID);
    }

    // Retrieve all Records
    // GET [contextPath][api]/arkivstruktur/registrering
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/
    @Operation(summary = "Retrieves multiple Record entities limited by " +
            "ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RecordHateoas found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping
    public ResponseEntity<RecordHateoas> findAllRecord(
            HttpServletRequest request) {
        RecordHateoas recordHateoas = new RecordHateoas(
                (List<INoarkEntity>) (List)
                        recordService.findByOwnedBy());
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordHateoas);
    }

    // Retrieve all secondary Series associated with a Record
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/referanseArkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/referanseArkivdel/
    @Operation(summary = "Retrieves all secondary Series associated with a " +
            "Record identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Series returned"),
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
            REFERENCE_SERIES)
    public ResponseEntity<String> findSecondarySeriesAssociatedWithRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Record to retrieve " +
                            "secondary Class for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-dokumentbeskrivelse
    @Operation(summary = "Create a DocumentDescription with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentDescription returned"),
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
            NEW_DOCUMENT_DESCRIPTION)
    public ResponseEntity<DocumentDescriptionHateoas>
    createDefaultDocumentDescription(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService.
                        generateDefaultDocumentDescription());
    }

    // Retrieve all DocumentDescriptions associated with a Record identified by systemId
    // GET [contextPath][api]/arkivstruktur/resgistrering/{systemId}/dokumentbeskrivelse
    @Operation(summary = "Retrieves a list of DocumentDescriptions associated" +
            " with a Record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentDescription returned"),
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
            DOCUMENT_DESCRIPTION)
    public ResponseEntity<DocumentDescriptionHateoas>
    findAllDocumentDescriptionAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        Record record = recordService.findBySystemId(systemID);
        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas(List.copyOf(
                record.getReferenceDocumentDescription()));
        documentDescriptionHateoasHandler.addLinks(
                documentDescriptionHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-bygning
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-bygning/
    @Operation(summary = "Associates a Building (national identifier) with a" +
            " Record identified by systemID",
            description = "Returns the Record with " +
                    "the building associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = BUILDING +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_BUILDING)
    public ResponseEntity<BuildingHateoas> getNIBuildingToRecordTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Building with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultBuilding());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-matrikkel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-matrikkel/
    @Operation(summary = "Associates a CadastralUnit (national identifier) " +
            "with a Record identified by systemID",
            description = "Returns the Record with the cadastralunit " +
                    "associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
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
            NEW_CADASTRAL_UNIT)
    public ResponseEntity<CadastralUnitHateoas>
    getNICadastralUnitToRecordTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "CadastralUnit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultCadastralUnit());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-dnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-dnummer/
    @Operation(summary = "Associates a DNumber (national identifier) with a" +
            " Record identified by systemID",
            description = "Returns the Record with the DNumber associated " +
                    "with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = D_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_D_NUMBER)
    public ResponseEntity<DNumberHateoas> getNIDNumberToRecordTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "DNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultDNumber());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-plan
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-plan/
    @Operation(summary = "Associates a Plan (national identifier) with a" +
            " Record identified by systemID",
            description = "Returns the Record with the Plan associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = D_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PLAN)
    public ResponseEntity<PlanHateoas> getNIPlanToRecordTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the Plan " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultPlan());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-posisjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-posisjon/
    @Operation(summary = "Associates a Position (national identifier) with a" +
            " Record identified by systemID",
            description = "Returns the Record with the position associated " +
                    "with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = POSITION +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_POSITION)
    public ResponseEntity<PositionHateoas> getNIPositionToRecordTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Position with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultPosition());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-foedselsnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-foedselsnummer/
    @Operation(summary = "Associates a SocialSecurityNumber (national " +
            "identifier) with a Record identified by systemID",
            description = "Returns the Record with the socialSecurityNumber " +
                    "associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
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
            NEW_SOCIAL_SECURITY_NUMBER)
    public ResponseEntity<SocialSecurityNumberHateoas>
    getNISocialSecurityNumberToRecordTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "SocialSecurityNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultSocialSecurityNumber());
    }

    // Add a Unit to a Record
    // GET [contextPath][api]/arkivstruktur/mappe/{systemId}/ny-enhetsidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-enhetsidentifikator/
    @Operation(summary = "Associates a Unit (national identifier) with a " +
            "Record identified by systemID",
            description = "Returns the Record with the unit associated with it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = NI_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_NI_UNIT)
    public ResponseEntity<UnitHateoas> getNIUnitToRecordTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Unit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultUnit());
    }

    // Delete a Record identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/registrering/{systemId}/
    @Operation(summary = "Delete a single Record entity identified by " +
            SYSTEM_ID)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Record deleted"),
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
    public ResponseEntity<String> deleteRecordBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        recordService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // API - All PUT Requests (CRUD - UPDATE)

    // Update a Record with given values
    // PUT [contextPath][api]/arkivstruktur/registrering/{systemId}
    @Operation(summary = "Updates a Record identified by a given systemId",
            description = "Returns the newly updated record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Record " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Record " +
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
                            " of type Record"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RecordHateoas> updateRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "Record",
                    description = "Incoming record object",
                    required = true)
            @RequestBody Record record) throws NikitaException {
        validateForUpdate(record);
        Record updatedRecord = recordService.handleUpdate(
                systemID, parseETAG(request.getHeader(ETAG)), record);
        RecordHateoas recordHateoas = new RecordHateoas(updatedRecord);
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, updatedRecord));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedRecord.getVersion().toString())
                .body(recordHateoas);
    }

    // Update a Record with given values
    // PATCH [contextPath][api]/arkivstruktur/registrering/{systemId}
    @Operation(summary = "Updates a Record identified by a given systemId",
            description = "Returns the newly updated record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Record OK"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Record"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PatchMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RecordHateoas> patchRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Record",
                    description = "Incoming record object",
                    required = true)
            @RequestBody PatchObjects patchObjects) throws NikitaException {
        return recordService.handleUpdate(systemID, patchObjects);
    }
}
