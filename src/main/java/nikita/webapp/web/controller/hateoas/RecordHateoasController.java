package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v4.DocumentDescription;
import nikita.common.model.noark5.v4.Record;
import nikita.common.model.noark5.v4.Series;
import nikita.common.model.noark5.v4.hateoas.*;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v4.secondary.*;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = Constants.HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION,
        produces = {NOARK5_V4_CONTENT_TYPE_JSON, NOARK5_V4_CONTENT_TYPE_JSON_XML})
public class RecordHateoasController extends NoarkController {

    private IRecordService recordService;
    private IDocumentDescriptionHateoasHandler documentDescriptionHateoasHandler;
    private IRecordHateoasHandler recordHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public RecordHateoasController(IRecordService recordService,
                                   IDocumentDescriptionHateoasHandler
                                           documentDescriptionHateoasHandler,
                                   IRecordHateoasHandler recordHateoasHandler,
                                   ApplicationEventPublisher
                                           applicationEventPublisher) {
        this.recordService = recordService;
        this.documentDescriptionHateoasHandler = documentDescriptionHateoasHandler;
        this.recordHateoasHandler = recordHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Create a new DocumentDescription and associate it with the given Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-dokumentobjekt
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-dokumentobjekt/
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

    @RequestMapping(method = POST, value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS +
            SLASH + NEW_DOCUMENT_DESCRIPTION, consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<DocumentDescriptionHateoas>
    createDocumentDescriptionAssociatedWithRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
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
                ResponseEntity.status(HttpStatus.CREATED)
                        .allow(
                                getMethodsForRequestOrThrow(request.getServletPath()))
                        .eTag(documentDescriptionHateoas.getEntityVersion().toString())
                        .body(documentDescriptionHateoas);
        return body;
    }

    // Add a reference to a secondary Series associated with the Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-referanseArkivdel
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-referanseArkivdel/
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

    @RequestMapping(method = POST, value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS
            + SLASH + NEW_REFERENCE_SERIES, consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<String> addReferenceSeriesToRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemId of Record to associate the secondary Series with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "Series",
                    value = "series",
                    required = true)
            @RequestBody Series series) throws NikitaException {
        // applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .eTag(series.getVersion().toString())
//                .body(seriesHateoas);

        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }

    // Add a Classified (gradering) to a Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-gradering
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-gradering/
    @ApiOperation(value = "Associates a Classified with a Record identified by systemID",
            notes = "Returns the Record after the Classified is successfully associated with it." +
                    "Note a Record can only have one Classified. Update via PUT",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = CLASSIFIED + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201, message = CLASSIFIED + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + CLASSIFIED),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = POST, value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS
            + SLASH + NEW_CLASSIFIED, consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<String> addNewClassifiedToRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemId of Record to associate the Classified with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "Classified",
                    value = "classified",
                    required = true)
            @RequestBody Classified classified) throws NikitaException {
        //applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .eTag(classified.getVersion().toString())
//                .body(classifiedHateoas);
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }

    // Add a Disposal to a Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-kassasjon
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-kassasjon/
    @ApiOperation(value = "Associates a Disposal with a Record identified by systemID",
            notes = "Returns the Record after the Disposal is successfully associated with it." +
                    "Note a Record can only have one Disposal. Update via PUT",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = DISPOSAL + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201, message = DISPOSAL + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + DISPOSAL),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = POST, value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS
            + SLASH + NEW_DISPOSAL, consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<String> addNewDisposalToRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemId of Record to associate the Disposal with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "Disposal",
                    value = "disposal",
                    required = true)
            @RequestBody Disposal disposal) throws NikitaException {
        // applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
        //TODO: What do we return here? Record + comment? comment?
        //        return ResponseEntity.status(HttpStatus.CREATED)
//                .eTag(comment.getVersion().toString())
//                .body(commentHateoas);
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }

    // Add a screening (skjerming) to a Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-skjerming
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-skjerming/
    @ApiOperation(value = "Associates a Screening with a Record identified by systemID",
            notes = "Returns the Record after the Screening is successfully associated with it." +
                    "Note a Record can only have one Screening. Update via PUT",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SCREENING + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201, message = SCREENING + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + SCREENING),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = POST, value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS
            + SLASH + NEW_SCREENING, consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<String> addNewScreeningToRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemId of Record to associate the Screening with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "Screening",
                    value = "screening",
                    required = true)
            @RequestBody Screening screening) throws NikitaException {
        // applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
//        TODO: What do we return here? Record + screening? screening?
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .eTag(screening.getVersion().toString())
//                .body(screeningHateoas);
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }

    // Add a disposalUndertaken (utfoertKassasjon) to a Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-utfoertKassasjon
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-utfoertKassasjon/
    @ApiOperation(value = "Associates a DisposalUndertaken with a Record identified by systemID",
            notes = "Returns the Record after the DisposalUndertaken is successfully associated with it." +
                    "Note a Record can only have one DisposalUndertaken. Update via PUT",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = DISPOSAL_UNDERTAKEN + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201, message = DISPOSAL_UNDERTAKEN + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + DISPOSAL_UNDERTAKEN),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = POST, value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS
            + SLASH + NEW_DISPOSAL_UNDERTAKEN, consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<String> addNewDisposalUndertakenToRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemId of Record to associate the DisposalUndertaken with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "DisposalUndertaken",
                    value = "disposalUndertaken",
                    required = true)
            @RequestBody DisposalUndertaken disposalUndertaken) throws NikitaException {
        // applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
        //TODO: What do we return here? Record + disposalUndertaken? disposalUndertaken?
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .eTag(disposalUndertaken.getVersion().toString())
//                .body(disposalUndertakenHateoas);
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }

    // Add a Deletion  to a Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-sletting
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-sletting/
    @ApiOperation(value = "Associates a Deletion with a Record identified by systemID",
            notes = "Returns the Record after the Deletion is successfully associated with it." +
                    "Note a Record can only have one Deletion. Update via PUT",
            response = RecordHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = DELETION + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 201, message = DELETION + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type " + DELETION),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = POST, value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS
            + SLASH + NEW_DELETION, consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<String> addNewDeletionToRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemId of Record to associate the Deletion with",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "Deletion",
                    value = "deletion",
                    required = true)
            @RequestBody Deletion deletion) throws NikitaException {
        //TODO: This is more to carry out a deletion of files. You don't just add a deletion to Record
        //applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .eTag(deletion.getVersion().toString())
//                .body(deletionHateoas);
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }


    // Delete all Record
    // DELETE [contextPath][api]/arkivstruktur/registrering/
    @ApiOperation(value = "Deletes all Record", response = Count.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all Record",
                    response = Count.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<Count> deleteAllRecord() {
        return ResponseEntity.status(NO_CONTENT).
                body(new Count(recordService.deleteAllByOwnedBy()));
    }

    // API - All GET Requests (CRUD - READ)

    // Retrieve a Record identified by a systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/registrering/
    @ApiOperation(value = "Retrieves a single Record entity given a systemId", response = Record.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record returned", response = Record.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS, method = GET)
    public ResponseEntity<RecordHateoas> findOneRecordbySystemId(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemID of the record to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        Record record = recordService.findBySystemId(systemID);
        RecordHateoas recordHateoas = new RecordHateoas(record);
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(record.getVersion().toString())
                .body(recordHateoas);
    }

    // Retrieve all File associated with Record identified by a systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/mappe
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/mappe/
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
    @GetMapping(value = SYSTEM_ID_PARAMETER + SLASH + FILE)
    public ResponseEntity<FileHateoas> findParentFileByRecordSystemId(
            @ApiParam(name = "systemID",
                    value = "systemID of the file to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return recordService.findFileAssociatedWithRecord(systemID);
    }

    // Retrieve the parent Series associated with the Record identified by the
    // given systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/arkivdel
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/arkivdel/
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
    @GetMapping(value = SYSTEM_ID_PARAMETER + SLASH + SERIES)
    public ResponseEntity<SeriesHateoas> findParentSeriesByRecordSystemId(
            @ApiParam(name = "systemID",
                    value = "systemID of the series to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return recordService.findSeriesAssociatedWithRecord(systemID);
    }

    // Retrieve the parent Class associated with the Record identified by the
    // given systemId
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/klasse
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/klasse/
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
    @GetMapping(value = SYSTEM_ID_PARAMETER + SLASH + CLASS)
    public ResponseEntity<ClassHateoas> findParentClassByRecordSystemId(
            @ApiParam(name = "systemID",
                    value = "systemID of the class to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return recordService.findClassAssociatedWithRecord(systemID);
    }

    // Retrieve all Records
    // GET [contextPath][api]/arkivstruktur/registrering
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/registrering/
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

    @RequestMapping(method = GET)
    public ResponseEntity<RecordHateoas> findAllRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @RequestParam(name = "top", required = false) Integer top,
            @RequestParam(name = "skip", required = false) Integer skip) {

        String ownedBy = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        RecordHateoas recordHateoas = new RecordHateoas((List<INikitaEntity>) (List)
                recordService.findByOwnedBy(ownedBy));
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(recordHateoas);
    }

    // Retrieve all secondary Series associated with a Record
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/referanseArkivdel
    // http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/referanseArkivdel/
    @ApiOperation(value = "Retrieves all secondary Series associated with a Record identified by a systemId",
            response = SeriesHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Series returned", response = SeriesHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS + SLASH + REFERENCE_SERIES,
            method = GET)
    public ResponseEntity<String> findSecondarySeriesAssociatedWithRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemID of the Record to retrieve secondary Class for",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }

    // Create a DocumentDescription with default values
    // GET [contextPath][api]/arkivstruktur/resgistrering/{systemId}/ny-dokumentbeskrivelse
    @ApiOperation(value = "Create a DocumentDescription with default values", response = DocumentDescriptionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentDescription returned", response = DocumentDescriptionHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS + SLASH +
            NEW_DOCUMENT_DESCRIPTION, method = GET)
    public ResponseEntity<DocumentDescriptionHateoas> createDefaultDocumentDescription(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response) {

        DocumentDescription defaultDocumentDescription = new DocumentDescription();

        defaultDocumentDescription.setAssociatedWithRecordAs(MAIN_DOCUMENT);
        defaultDocumentDescription.setDocumentType(LETTER);
        defaultDocumentDescription.setDocumentStatus(DOCUMENT_STATUS_FINALISED);

        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas(defaultDocumentDescription);
        documentDescriptionHateoasHandler.addLinksOnNew(documentDescriptionHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionHateoas);
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

    @RequestMapping(value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS + SLASH +
            DOCUMENT_DESCRIPTION, method = GET)
    public ResponseEntity<DocumentDescriptionHateoas> findAllDocumentDescriptionAssociatedWithRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        Record record = recordService.findBySystemId(systemID);
        if (record == null) {
            throw new NoarkEntityNotFoundException("Could not find File object with systemID " + systemID);
        }
        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas((List<INikitaEntity>) (List) record.getReferenceDocumentDescription());
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionHateoas);
    }

    // Delete a Record identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/registrering/{systemId}/
    @ApiOperation(value = "Deletes a single Record entity identified by systemID", response = HateoasNoarkObject.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Parent entity (DocumentDescription or Record) returned", response = HateoasNoarkObject.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS,
            method = DELETE)
    public ResponseEntity<String> deleteRecordBySystemId(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemID of the record to delete",
                    required = true)
            @PathVariable("systemID") final String systemID) {

        Record record = recordService.findBySystemId(systemID);
      /*  NoarkEntity parentEntity = record.chooseParent();
        HateoasNoarkObject hateoasNoarkObject;
        if (parentEntity instanceof Series) {
            hateoasNoarkObject = new SeriesHateoas(parentEntity);
            seriesHateoasHandler.addLinks(hateoasNoarkObject, new Authorisation());
        }
        else if (parentEntity instanceof File) {
            hateoasNoarkObject = new FileHateoas(parentEntity);
            fileHateoasHandler.addLinks(hateoasNoarkObject, new Authorisation());
        }
        else if (parentEntity instanceof Class) {
            hateoasNoarkObject = new ClassHateoas(parentEntity);
            classHateoasHandler.addLinks(hateoasNoarkObject, new Authorisation());
        }
        else {
            throw new nikita.webapp.util.exceptions.NikitaException("Internal error. Could not process"
                    + request.getRequestURI());
        } */
        recordService.deleteEntity(systemID);
        applicationEventPublisher.publishEvent(new AfterNoarkEntityDeletedEvent(this, record));
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body("{\"status\": \"deleted\"}");
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

    @RequestMapping(value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS, method = PUT, consumes = {NOARK5_V4_CONTENT_TYPE_JSON})
    public ResponseEntity<RecordHateoas> updateRecord(
            final UriComponentsBuilder uriBuilder, HttpServletRequest request, final HttpServletResponse response,
            @ApiParam(name = "systemID",
                    value = "systemId of record to update",
                    required = true)
            @PathVariable("systemID") final String systemID,
            @ApiParam(name = "Record",
                    value = "Incoming record object",
                    required = true)
            @RequestBody Record record) throws NikitaException {
        validateForUpdate(record);

        Record updatedRecord = recordService.handleUpdate(systemID, parseETAG(request.getHeader(ETAG)), record);
        RecordHateoas recordHateoas = new RecordHateoas(updatedRecord);
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, updatedRecord));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedRecord.getVersion().toString())
                .body(recordHateoas);
    }
}
