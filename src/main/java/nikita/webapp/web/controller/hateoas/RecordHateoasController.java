package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_RECORD,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class RecordHateoasController
        extends NoarkController {

    private IDocumentDescriptionService documentDescriptionService;
    private IRecordService recordService;
    private IDocumentDescriptionHateoasHandler documentDescriptionHateoasHandler;
    private IRecordHateoasHandler recordHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public RecordHateoasController(IDocumentDescriptionService documentDescriptionService,
                                   IRecordService recordService,
                                   IDocumentDescriptionHateoasHandler
                                           documentDescriptionHateoasHandler,
                                   IRecordHateoasHandler recordHateoasHandler,
                                   ApplicationEventPublisher
                                           applicationEventPublisher) {
        this.documentDescriptionService = documentDescriptionService;
        this.recordService = recordService;
        this.documentDescriptionHateoasHandler = documentDescriptionHateoasHandler;
        this.recordHateoasHandler = recordHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Create a new DocumentDescription and associate it with the given Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-dokumentobjekt
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-dokumentobjekt/
    @ApiOperation(value = "Persists a DocumentDescription object associated with the given Record systemId",
            notes = "Returns the newly created DocumentDescription object after it was associated with a " +
                    "Record object and persisted to the database", response = DocumentDescriptionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentDescription " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 201, message = "DocumentDescription " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type DocumentDescription"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_DESCRIPTION,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentDescriptionHateoas>
    createDocumentDescriptionAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the documentDescription with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "documentDescription",
                    value = "Incoming documentDescription object",
                    required = true)
            @RequestBody DocumentDescription documentDescription)
            throws NikitaException {
        validateForCreate(documentDescription);
        DocumentDescriptionHateoas documentDescriptionHateoas =
                recordService.
                        createDocumentDescriptionAssociatedWithRecord(
                                systemID, documentDescription);
        final ResponseEntity<DocumentDescriptionHateoas> body =
                ResponseEntity.status(CREATED)
                        .allow(
                                getMethodsForRequestOrThrow(request.getServletPath()))
                        .eTag(documentDescriptionHateoas.getEntityVersion().toString())
                        .body(documentDescriptionHateoas);
        return body;
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/part
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/part/
    @ApiOperation(value = "Retrieves a list of Part associated with a Record",
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
    findAllPartAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.getPartAssociatedWithRecord(systemID));
    }

    // Retrieve all Authors associated with a Record identified by systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/forfatter
    @ApiOperation(value = "Retrieves a list of Authors associated with a " +
            "Record",
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
    findAllAuthorAssociatedWithRecord(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the Record to retrieve " +
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
    @ApiOperation(value = "Retrieves a list of CorrespondencePartHateoas associated with a Record",
            response = CorrespondencePartHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartUnit returned",
                    response = CorrespondencePartHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CORRESPONDENCE_PART)
    public ResponseEntity<CorrespondencePartHateoas>
    findAllCorrespondencePartUnitAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.getCorrespondencePartAssociatedWithRecord(
                                systemID));
    }

    // GET [contextPath][api]/sakarkiv/registrering/{systemId}/nasjonalidentifikator
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/nasjonalidentifikator/
    @ApiOperation(value = "Retrieves a list of NationalIdentifier associated with a File",
                  response = NationalIdentifierHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "NationalIdentifier returned",
                    response = NationalIdentifierHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NATIONAL_IDENTIFIER)
    public ResponseEntity<NationalIdentifierHateoas>
    findAllNIAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated File",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.getNationalIdentifierAssociatedWithRecord(systemID));
    }

    // Add a reference to a secondary Series associated with the Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-referanseArkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-referanseArkivdel/
    @ApiOperation(value = "Associates a secondary Series with a Record identified by systemID",
            notes = "Returns the Record after the secondary Series is successfully associated with it." +
                    "Note a secondary series allows a Record to be associated with another Series.",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = CLASS + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201, message = CLASS + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + CLASS),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_REFERENCE_SERIES,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String> addReferenceSeriesToRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the secondary Series with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "Series",
                    value = "series",
                    required = true)
            @RequestBody Series series) throws NikitaException {
        // applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
//        return ResponseEntity.status(CREATED)
//                .eTag(series.getVersion().toString())
//                .body(seriesHateoas);

        return errorResponse(NOT_IMPLEMENTED, API_MESSAGE_NOT_IMPLEMENTED);
    }

    // Create a new Author and associate it with the given Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-forfatter
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-forfatter/
    @ApiOperation(
            value = "Persists an author object associated with the given " +
                    "Record systemId",
            notes = "Returns the newly created author object after it was " +
                    "associated with a Record object and persisted to the " +
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
                            "Record"),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_AUTHOR,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<AuthorHateoas> addAuthorAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of the record to associate the author " +
                            "with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "author",
                    value = "Incoming author object",
                    required = true)
            @RequestBody Author author)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(recordService.associateAuthorWithRecord
                      (systemID, author));
    }

    // Create a suggested CorrespondencePartPerson (like a template) object with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-korrespondansepartperson
    @ApiOperation(value = "Suggests the contents of a new CorrespondencePart object",
            notes = "Returns a pre-filled CorrespondencePart object" +
                    " with values relevant for the logged-in user", response = CorrespondencePartPersonHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePart " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CorrespondencePartPersonHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CORRESPONDENCE_PART_PERSON)
    public ResponseEntity<CorrespondencePartPersonHateoas>
    getCorrespondencePartPersonTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.
                        generateDefaultCorrespondencePartPerson(systemID));
    }

    // Create a suggested CorrespondencePartUnit (like a template) object with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-korrespondansepartenhet
    @ApiOperation(value = "Suggests the contents of a new CorrespondencePart object",
            notes = "Returns a pre-filled CorrespondencePart object" +
                    " with values relevant for the logged-in user", response = CorrespondencePartUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePart " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CorrespondencePartUnitHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CORRESPONDENCE_PART_UNIT)
    public ResponseEntity<CorrespondencePartUnitHateoas>
    getCorrespondencePartUnitTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated Record",
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
    @ApiOperation(value = "Suggests the contents of a new Part object",
            notes = "Returns a pre-filled Part object" +
                    " with values relevant for the logged-in user",
            response = PartUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Part " +
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
    public ResponseEntity<PartUnitHateoas> getPartUnitTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the PartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID) throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.
                        generateDefaultPartUnit(systemID));
    }

    // Create a suggested PartUnit (like a template) object with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-partperson
    @ApiOperation(value = "Suggests the contents of a new Part object",
            notes = "Returns a pre-filled Part object" +
                    " with values relevant for the logged-in user", response = PartPersonHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Part " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_PERSON)
    public ResponseEntity<PartPersonHateoas> getPartPersonTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the PartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID) throws NikitaException {

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.
                        generateDefaultPartPerson(systemID));
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
                    value = "systemID of the record to retrieve " +
                            "associated Author",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.
                        generateDefaultAuthor(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-merknad
    @ApiOperation(value = "Create a Comment with default values",
                  response = Comment.class)
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
                .body(recordService.generateDefaultComment());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/merknad/
    @ApiOperation(value = "Retrieves all Comments associated with a Record identified by a systemId",
            response = CommentHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record returned", response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + COMMENT)
    public ResponseEntity<CommentHateoas> findAllCommentsAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                      value = "systemID of the record to retrieve comments for",
                      required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.getCommentAssociatedWithRecord(systemID));
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-merknad
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-merknad/
    @ApiOperation(value = "Associates a Comment with a Record identified by systemID",
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
    public ResponseEntity<CommentHateoas> addCommentToRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                      value = "systemId of Record to associate the Comment with",
                      required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Comment",
                      value = "comment",
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
    @ApiOperation(value = "Persists a CorrespondencePartPerson object associated with the given Record systemId",
            notes = "Returns the newly created CorrespondencePartPerson object after it was associated with a " +
                    "Record object and persisted to the database", response = CorrespondencePartPersonHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartPerson " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CorrespondencePartPersonHateoas.class),
            @ApiResponse(code = 201, message = "CorrespondencePartPerson " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CorrespondencePartPersonHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type CorrespondencePartPerson"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CORRESPONDENCE_PART_PERSON,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartPersonHateoas>
    createCorrespondencePartPersonAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the CorrespondencePartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "CorrespondencePartPerson",
                    value = "Incoming CorrespondencePartPerson object",
                    required = true)
            @RequestBody CorrespondencePartPerson correspondencePartPerson)
            throws NikitaException {

        CorrespondencePartPersonHateoas createdCorrespondencePartPerson =
                recordService.
                        createCorrespondencePartPersonAssociatedWithRecord(
                                systemID, correspondencePartPerson);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdCorrespondencePartPerson.getEntityVersion().toString())
                .body(createdCorrespondencePartPerson);
    }

    // Create a new Part and associate it with the given journalpost
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-partperson
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-partperson/
    @ApiOperation(value = "Persists a Part object associated with the given " +
            "Record systemId",
            notes = "Returns the newly created Part object after it was associated with a " +
                    "Record object and persisted to the database", response = PartPersonHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Part " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 201, message = "Part " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Part"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_PERSON,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartPersonHateoas>
    createPartPersonAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the Part with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "Part",
                    value = "Incoming Part object",
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
    @ApiOperation(value = "Persists a Part object associated with the given " +
            "Record systemId",
            notes = "Returns the newly created Part object after it was associated with a " +
                    "Record object and persisted to the database", response = PartUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Part " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 201, message = "Part " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Part"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PART_UNIT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartUnitHateoas>
    createPartUnitAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the Part with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "Part",
                    value = "Incoming Part object",
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

    // Create a new CorrespondencePartInternal and associate it with the given journalpost
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-korrespondansepartintern
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-korrespondansepartintern/
    @ApiOperation(value = "Persists a CorrespondencePartInternal object associated with the given Record systemId",
            notes = "Returns the newly created CorrespondencePartInternal object after it was associated with a " +
                    "Record object and persisted to the database", response = CorrespondencePartInternalHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartInternal " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CorrespondencePartInternalHateoas.class),
            @ApiResponse(code = 201, message = "CorrespondencePartInternal " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CorrespondencePartInternalHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type CorrespondencePartInternal"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CORRESPONDENCE_PART_INTERNAL,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartInternalHateoas> createCorrespondencePartInternalAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the CorrespondencePartInternal with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "CorrespondencePartInternal",
                    value = "Incoming CorrespondencePartInternal object",
                    required = true)
            @RequestBody CorrespondencePartInternal correspondencePartInternal)
            throws NikitaException {
/*
        CorrespondencePartInternalHateoas correspondencePartInternalHateoas =
                recordService.
                        createCorrespondencePartInternalAssociatedWithRecord(
                                systemID, correspondencePartInternal);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(correspondencePartInternalHateoas.getEntityVersion().toString())
                .body(correspondencePartInternalHateoas);*/
        return ResponseEntity.status(NOT_IMPLEMENTED).body(null);
    }

    // Create a new CorrespondencePartUnit and associate it with the given journalpost
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-korrespondansepartenhet
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-korrespondansepartenhet/
    @ApiOperation(value = "Persists a CorrespondencePartUnit object associated with the given Record systemId",
            notes = "Returns the newly created CorrespondencePartUnit object after it was associated with a " +
                    "Record object and persisted to the database", response = CorrespondencePartUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartUnit " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CorrespondencePartUnitHateoas.class),
            @ApiResponse(code = 201, message = "CorrespondencePartUnit " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CorrespondencePartUnitHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type CorrespondencePartUnit"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CORRESPONDENCE_PART_UNIT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartUnitHateoas> createCorrespondencePartUnitAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the CorrespondencePartUnit with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "CorrespondencePartUnit",
                    value = "Incoming CorrespondencePartUnit object",
                    required = true)
            @RequestBody CorrespondencePartUnit correspondencePartUnit)
            throws NikitaException {

        CorrespondencePartUnitHateoas correspondencePartUnitHateoas =
                recordService.
                        createCorrespondencePartUnitAssociatedWithRecord(
                                systemID, correspondencePartUnit);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(correspondencePartUnitHateoas.getEntityVersion().toString())
                .body(correspondencePartUnitHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-bygning
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-bygning/
    @ApiOperation(value = "Associates a Building (national identifier) with a" +
            " Record identified by systemID", notes = "Returns the Record with " +
            "the building associated with it", response = BuildingHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = BUILDING + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = BuildingHateoas.class),
            @ApiResponse(code = 201,
                    message = BUILDING +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = BuildingHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_BUILDING,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<BuildingHateoas> addNIBuildingToRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the Building with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Building",
                    value = "building",
                    required = true)
            @RequestBody Building building) throws NikitaException {
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
    @ApiOperation(value = "Associates a CadastralUnit (national identifier) with a" +
            " Record identified by systemID", notes = "Returns the Record with " +
            "the CadastralUnit associated with it", response = CadastralUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = CADASTRAL_UNIT + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CadastralUnitHateoas.class),
            @ApiResponse(code = 201,
                    message = CADASTRAL_UNIT +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CadastralUnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CADASTRAL_UNIT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CadastralUnitHateoas> addNICadastralUnitToRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the CadastralUnit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "CadastralUnit",
                    value = "CadastralUnit",
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
    @ApiOperation(value = "Associates a DNumber (national identifier) with a" +
            " Record identified by systemID", notes = "Returns the Record with " +
            "the DNumber associated with it", response = DNumberHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = D_NUMBER + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DNumberHateoas.class),
            @ApiResponse(code = 201,
                    message = D_NUMBER +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DNumberHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_D_NUMBER,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DNumberHateoas> addNIDNumberToRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the DNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "dNumber",
                    value = "DNumber",
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
    @ApiOperation(value = "Associates a Plan (national identifier) with a" +
            " Record identified by systemID", notes = "Returns the Record with " +
            "the Plan associated with it", response = PlanHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = D_NUMBER + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PlanHateoas.class),
            @ApiResponse(code = 201,
                    message = D_NUMBER +
			 API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PlanHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PLAN,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PlanHateoas> addNIPlanToRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the Plan with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "plan",
                    value = "Plan",
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
    @ApiOperation(value = "Associates a Position (national identifier) with a" +
            " Record identified by systemID",
            notes = "Returns the Record with the position associated with it",
            response = PositionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = POSITION +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PositionHateoas.class),
            @ApiResponse(code = 201,
                    message = POSITION +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PositionHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_POSITION,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PositionHateoas> addNIPositionToRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the " +
                            "Position with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Position",
                    value = "position",
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
    @ApiOperation(value = "Associates a SocialSecurityNumber (national identifier) with a" +
            " Record identified by systemID",
            notes = "Returns the Record with the socialSecurityNumber associated with it",
            response = SocialSecurityNumberHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SocialSecurityNumberHateoas.class),
            @ApiResponse(code = 201,
                    message = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = SocialSecurityNumberHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SOCIAL_SECURITY_NUMBER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SocialSecurityNumberHateoas> addNISocialSecurityNumberToRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the " +
                            "SocialSecurityNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "SocialSecurityNumber",
                    value = "socialSecurityNumber",
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
    @ApiOperation(value = "Associates a Unit (national identifier) with a " +
            "Record identified by systemID",
            notes = "Returns the Record with the unit associated with it",
            response = UnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = NI_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = UnitHateoas.class),
            @ApiResponse(code = 201,
                    message = NI_UNIT +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = UnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_NI_UNIT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<UnitHateoas> addNIUnitToRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the " +
                            "Unit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Unit",
                    value = "unit",
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
    @ApiOperation(value = "Deletes all Record", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all Record",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
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
    @ApiOperation(value = "Retrieves a single Record entity given a systemId", response = Record.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record returned", response = Record.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<RecordHateoas> findOneRecordbySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the record to retrieve",
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
    @ApiOperation(value = "Retrieves a single File entity given a systemId",
            response = FileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "File returned", response = FileHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + FILE)
    public ResponseEntity<FileHateoas> findParentFileByRecordSystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return recordService.findFileAssociatedWithRecord(systemID);
    }

    // Retrieve the parent Series associated with the Record identified by the
    // given systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/arkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/
    @ApiOperation(value = "Retrieves a single Series entity given a systemId",
            response = SeriesHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Series returned", response = SeriesHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SERIES)
    public ResponseEntity<SeriesHateoas> findParentSeriesByRecordSystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the series to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return recordService.findSeriesAssociatedWithRecord(systemID);
    }

    // Retrieve the parent Class associated with the Record identified by the
    // given systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/klasse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/
    @ApiOperation(value = "Retrieves a single Class entity given a systemId",
            response = ClassHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Class returned", response = ClassHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CLASS)
    public ResponseEntity<ClassHateoas> findParentClassByRecordSystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the class to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return recordService.findClassAssociatedWithRecord(systemID);
    }

    // Retrieve all Records
    // GET [contextPath][api]/arkivstruktur/registrering
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/
    @ApiOperation(value = "Retrieves multiple Record entities limited by ownership rights",
            notes = "The field skip tells how many Record rows of the result set to ignore (starting at 0), " +
                    "while top tells how many rows after skip to return. Note if the value of top is greater than " +
                    "system value nikita-noark5-core.pagination.maxPageSize, then " +
                    "nikita-noark5-core.pagination.maxPageSize is used.",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "RecordHateoas found", response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping
    public ResponseEntity<RecordHateoas> findAllRecord(
            HttpServletRequest request,
            @RequestParam(name = "top", required = false) Integer top,
            @RequestParam(name = "skip", required = false) Integer skip) {

        String ownedBy = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        RecordHateoas recordHateoas = new RecordHateoas((List<INoarkEntity>) (List)
                recordService.findByOwnedBy(ownedBy));
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordHateoas);
    }

    // Retrieve all secondary Series associated with a Record
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/referanseArkivdel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/referanseArkivdel/
    @ApiOperation(value = "Retrieves all secondary Series associated with a Record identified by a systemId",
            response = SeriesHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Series returned", response = SeriesHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + REFERENCE_SERIES)
    public ResponseEntity<String> findSecondarySeriesAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the Record to retrieve secondary Class for",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return errorResponse(NOT_IMPLEMENTED,
                API_MESSAGE_NOT_IMPLEMENTED);
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-dokumentbeskrivelse
    @ApiOperation(value = "Create a DocumentDescription with default values", response = DocumentDescriptionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentDescription returned", response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_DESCRIPTION)
    public ResponseEntity<DocumentDescriptionHateoas> createDefaultDocumentDescription(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionService.
                        generateDefaultDocumentDescription());
    }


    // Retrieve all DocumentDescriptions associated with a Record identified by systemId
    // GET [contextPath][api]/arkivstruktur/resgistrering/{systemId}/dokumentbeskrivelse
    @ApiOperation(value = "Retrieves a lit of DocumentDescriptions associated with a Record",
            response = DocumentDescriptionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentDescription returned", response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + DOCUMENT_DESCRIPTION)
    public ResponseEntity<DocumentDescriptionHateoas> findAllDocumentDescriptionAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        Record record = recordService.findBySystemId(systemID);
        if (record == null) {
            throw new NoarkEntityNotFoundException("Could not find File object with systemID " + systemID);
        }
        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas((List<INoarkEntity>) (List) record.getReferenceDocumentDescription());
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-bygning
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-bygning/
    @ApiOperation(value = "Associates a Building (national identifier) with a" +
            " Record identified by systemID", notes = "Returns the Record with " +
            "the building associated with it", response = BuildingHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = BUILDING + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = BuildingHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_BUILDING)
    public ResponseEntity<BuildingHateoas> getNIBuildingToRecordTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the Building with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultBuilding());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-matrikkel
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-matrikkel/
    @ApiOperation(value = "Associates a CadastralUnit (national identifier) with a" +
            " Record identified by systemID", notes = "Returns the Record with " +
            "the cadastralunit associated with it", response = CadastralUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = CADASTRAL_UNIT + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CadastralUnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CADASTRAL_UNIT)
    public ResponseEntity<CadastralUnitHateoas> getNICadastralUnitToRecordTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the CadastralUnit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultCadastralUnit());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-dnummer
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-dnummer/
    @ApiOperation(value = "Associates a DNumber (national identifier) with a" +
            " Record identified by systemID", notes = "Returns the Record with " +
            "the DNumber associated with it", response = DNumberHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = D_NUMBER + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DNumberHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_D_NUMBER)
    public ResponseEntity<DNumberHateoas> getNIDNumberToRecordTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the DNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultDNumber());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-plan
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-plan/
    @ApiOperation(value = "Associates a Plan (national identifier) with a" +
            " Record identified by systemID", notes = "Returns the Record with " +
            "the Plan associated with it", response = PlanHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = D_NUMBER + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PlanHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PLAN)
    public ResponseEntity<PlanHateoas> getNIPlanToRecordTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the Plan with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordService.generateDefaultPlan());
    }

    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-posisjon
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-posisjon/
    @ApiOperation(value = "Associates a Position (national identifier) with a" +
            " Record identified by systemID",
            notes = "Returns the Record with the position associated with it",
            response = PositionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = POSITION +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PositionHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_POSITION)
    public ResponseEntity<PositionHateoas> getNIPositionToRecordTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the " +
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
    @ApiOperation(value = "Associates a SocialSecurityNumber (national identifier) with a" +
            " Record identified by systemID",
            notes = "Returns the Record with the socialSecurityNumber associated with it",
            response = SocialSecurityNumberHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = SOCIAL_SECURITY_NUMBER +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SocialSecurityNumberHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SOCIAL_SECURITY_NUMBER)
    public ResponseEntity<SocialSecurityNumberHateoas> getNISocialSecurityNumberToRecordTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the " +
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
    @ApiOperation(value = "Associates a Unit (national identifier) with a " +
            "Record identified by systemID",
            notes = "Returns the Record with the unit associated with it",
            response = UnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = NI_UNIT +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = UnitHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_NI_UNIT)
    public ResponseEntity<UnitHateoas> getNIUnitToRecordTemplate(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Record to associate the " +
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
    @ApiOperation(value = "Delete a single Record entity identified by " +
            SYSTEM_ID, response = String.class)
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
    public ResponseEntity<String> deleteRecordBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the record to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        recordService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // API - All PUT Requests (CRUD - UPDATE)

    // Update a Record with given values
    // PUT [contextPath][api]/arkivstruktur/registrering/{systemId}
    @ApiOperation(value = "Updates a Record identified by a given systemId", notes = "Returns the newly updated record",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201, message = "Record " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Record"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RecordHateoas> updateRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Record",
                    value = "Incoming record object",
                    required = true)
            @RequestBody Record record) throws NikitaException {
        validateForUpdate(record);

        Record updatedRecord = recordService.handleUpdate(systemID, parseETAG(request.getHeader(ETAG)), record);
        RecordHateoas recordHateoas = new RecordHateoas(updatedRecord);
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, updatedRecord));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedRecord.getVersion().toString())
                .body(recordHateoas);
    }
}
