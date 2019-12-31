package nikita.webapp.web.controller.hateoas.casehandling;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.casehandling.DocumentFlow;
import nikita.common.model.noark5.v5.casehandling.Precedence;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.PrecedenceHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.IRegistryEntryHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class RegistryEntryHateoasController extends NoarkController {

    private final IRegistryEntryService registryEntryService;
    private final IRegistryEntryHateoasHandler registryEntryHateoasHandler;
    private final ICorrespondencePartService correspondencePartService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RegistryEntryHateoasController(
            IRegistryEntryService registryEntryService,
            IRegistryEntryHateoasHandler registryEntryHateoasHandler,
            ICorrespondencePartService correspondencePartService,
            ApplicationEventPublisher applicationEventPublisher) {
        this.registryEntryService = registryEntryService;
        this.registryEntryHateoasHandler = registryEntryHateoasHandler;
        this.correspondencePartService = correspondencePartService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)


    /*
    // Create a new Precedence and associate it with the given journalpost
    // POST [contextPath][api]/casehandling/journalpost/{systemId}/ny-presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-presedens/
    @ApiOperation(value = "Persists a Precedence object associated with the given Record systemId",
            notes = "Returns the newly created Precedence object after it was associated with a " +
                    "Record object and persisted to the database", response = PrecedenceHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Precedence " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PrecedenceHateoas.class),
            @ApiResponse(code = 201, message = "Precedence " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PrecedenceHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Precedence"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PRECEDENCE,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PrecedenceHateoas> createPrecedenceAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of record to associate the Precedence with.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @ApiParam(name = "Precedence",
                    value = "Incoming Precedence object",
                    required = true)
            @RequestBody Precedence Precedence)
            throws NikitaException {

        Precedence createdPrecedence =
                registryEntryService.createPrecedenceAssociatedWithRecord(systemID, Precedence);
        PrecedenceHateoas precedenceHateoas =
                new PrecedenceHateoas(createdPrecedence);
        precedenceHateoasHandler.addLinks(PrecedenceHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, createdPrecedence));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
            .header(ETAG, .getVersion().toString())
                .body(precedenceHateoas);
    }


    // Create a new SignOff and associate it with the given journalpost
    // POST [contextPath][api]/casehandling/journalpost/{systemId}/ny-avskrivning
    //  https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-avskrivning/
    @ApiOperation(value = "Persists a SignOff object associated with the given Record systemId",
            notes = "Returns the newly created SignOff object after it was associated with a " +
                    "Record object and persisted to the database", response = SignOffHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SignOff " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SignOffHateoas.class),
            @ApiResponse(code = 201, message = "SignOff " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = SignOffHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type SignOff"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_DESCRIPTION,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SignOffHateoas>
    createSignOffAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of record to associate the signOff with.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @ApiParam(name = "signOff",
                    value = "Incoming signOff object",
                    required = true)
            @RequestBody SignOff signOff)
            throws NikitaException {

        SignOff createdSignOff =
                recordService.createSignOffAssociatedWithRecord(systemID, signOff);
        SignOffHateoas signOffHateoas =
                new SignOffHateoas(createdSignOff);
        signOffHateoasHandler.addLinks(signOffHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, createdSignOff));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
             .header(ETAG, .getVersion().toString())
                .body(signOffHateoas);
    }
   */

    // Create a new CorrespondencePart and associate it with the given RegistryEntry
    // POST [contextPath][api]/casehandling/journalpost/{systemId}/ny-korrespondansepart
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-korrespondansepart/
    @ApiOperation(value = "Persists a DocumentObject associated with the given Record systemId",
            notes = "Returns the newly created DocumentObject after it was associated with a " +
                    "Record and persisted to the database. A DocumentObject should not be associated with both a " +
                    "Record and a DocumentDescription. Choose one!", response = DocumentObjectHateoas.class)
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

    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_OBJECT,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String>
    createDocumentObjectAssociatedWithRegistryEntry(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of record to associate the documentObject with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "documentObject",
                    value = "Incoming documentObject object",
                    required = true)
            @RequestBody DocumentObject documentObject)
            throws NikitaException {
        /*
        DocumentObjectHateoas documentObjectHateoas =
                new DocumentObjectHateoas(
                        recordService.createDocumentObjectAssociatedWithRecord(systemID,
                                documentObject));
        documentObjectHateoasHandler.addLinks(documentObjectHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, ));
           return ResponseEntity.status(HttpStatus.CREATED)
                .header(ETAG, .getVersion().toString())
                .body(documentObjectHateoas);
        */
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }

    // Retrieve all CorrespondencePartInternal associated with a RegistryEntry identified by systemId
    // GET [contextPath][api]/sakarkiv/journalpost/{systemId}/korrespondansepartperson
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/korrespondansepartperson/
    @ApiOperation(value = "Retrieves a list of CorrespondencePartInternals associated with a RegistryEntry",
            response = CorrespondencePartInternalHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartInternal returned",
                    response = CorrespondencePartInternalHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + CORRESPONDENCE_PART_INTERNAL)
    public ResponseEntity<CorrespondencePartInternalHateoas> findAllCorrespondencePartInternalAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable("systemID") final String systemID) {
/*
TODO: Temp disabled!
        List<CorrespondencePartInternal> correspondencePartInternal =
                registryEntryService.getCorrespondencePartInternalAssociatedWithRegistryEntry(systemID);
        CorrespondencePartInternalHateoas correspondencePartHateoas =
                new CorrespondencePartInternalHateoas((List<INikitaEntity>) (List) correspondencePartInternal);
        correspondencePartHateoasHandler.addLinksOnTemplate(correspondencePartHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(correspondencePartHateoas);
   */
        return null;
    }


    // Retrieve all SignOff associated with a RegistryEntry identified by systemId
    // GET [contextPath][api]/casehandling/journalpost/{systemId}/avskrivning
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/avskrivning/
    @ApiOperation(value = "Retrieves a list of SignOffs associated with a RegistryEntry",
            response = SignOff.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SignOff returned", response = SignOff.class), //SignOffHateoas
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SIGN_OFF)
    public ResponseEntity<String> findAllSignOffAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        /*  Record record = recordService.findBySystemId(UUID.fromString(systemId));
            if (record == null) {
            throw new NoarkEntityNotFoundException("Could not find File object with systemID " + systemID);
        }
        SignOffHateoas documentDescriptionHateoas = new
                SignOffHateoas((List<INikitaEntity>) (List)record.getReferenceSignOff()));
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionHateoas);
                */
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }

    // Retrieve all Precedence associated with a RegistryEntry identified by systemId
    // GET [contextPath][api]/casehandling/journalpost/{systemId}/presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/presedens/
    @ApiOperation(value = "Retrieves a list of Precedences associated with a RegistryEntry",
            response = Precedence.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Precedence returned", response = PrecedenceHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + PRECEDENCE)
    public ResponseEntity<String> findAllPrecedenceAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the registryEntry to retrieve associated Precedence",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        /*   Record record = recordService.findBySystemId(UUID.fromString(systemId));
        if (record == null) {
            throw new NoarkEntityNotFoundException("Could not find File object with systemID " + systemID);
        }
        PrecedenceHateoas documentDescriptionHateoas = new
                PrecedenceHateoas((List<INikitaEntity>) (List)record.getReferencePrecedence()));
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionHateoas);
                */
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }

    // Retrieve all DocumentFlow associated with a RegistryEntry identified by systemId
    // GET [contextPath][api]/casehandling/journalpost/{systemId}/dokumentflyt
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/dokumentflyt/
    @ApiOperation(value = "Retrieves a list of DocumentFlows associated with a RegistryEntry",
            response = DocumentFlow.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow returned", response = DocumentFlow.class), //DocumentFlowHateoas
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + DOCUMENT_FLOW)
    public ResponseEntity<String> findAllDocumentFlowAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable("systemID") final String systemID) {
        /*  Record record = recordService.findBySystemId(UUID.fromString(systemId));
            if (record == null) {
            throw new NoarkEntityNotFoundException("Could not find File object with systemID " + systemID);
        }
        DocumentFlowHateoas documentDescriptionHateoas = new
                DocumentFlowHateoas((List<INikitaEntity>) (List)record.getReferenceDocumentFlow()));
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(documentDescriptionHateoas);
                */
        return new ResponseEntity<>(API_MESSAGE_NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
    }


    // Retrieve a single registryEntry identified by systemId
    // GET [contextPath][api]/casehandling/journalpost/{systemID}
    @ApiOperation(value = "Retrieves a single RegistryEntry entity given a systemId", response = RegistryEntry.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "RegistryEntry returned", response = RegistryEntry.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<RegistryEntryHateoas> findOneRegistryEntrybySystemId(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the registryEntry to retrieve",
                    required = true)
            @PathVariable("systemID") final String registryEntrySystemId) {
        RegistryEntry registryEntry = registryEntryService.findBySystemId(registryEntrySystemId);

        RegistryEntryHateoas registryEntryHateoas = new
                RegistryEntryHateoas(registryEntry);
        registryEntryHateoasHandler.addLinks(registryEntryHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(registryEntry.getVersion().toString())
                .body(registryEntryHateoas);
    }

    // Get all registryEntry
    // GET [contextPath][api]/casehandling/journalpost/
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/journalpost/
    @ApiOperation(value = "Retrieves multiple RegistryEntry entities limited by ownership rights", notes = "The field skip" +
            "tells how many RegistryEntry rows of the result set to ignore (starting at 0), while  top tells how many rows" +
            " after skip to return. Note if the value of top is greater than system value " +
            " nikita-noark5-core.pagination.maxPageSize, then nikita-noark5-core.pagination.maxPageSize is used. ",
            response = RegistryEntryHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "RegistryEntry found",
                    response = RegistryEntryHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping
    public ResponseEntity<RegistryEntryHateoas> findAllRegistryEntry(
            HttpServletRequest request,
            @RequestParam(name = "top", required = false) Integer top,
            @RequestParam(name = "skip", required = false) Integer skip) {
        RegistryEntryHateoas registryEntryHateoas = new
                RegistryEntryHateoas((List<INikitaEntity>) (List)
                registryEntryService.findRegistryEntryByOwnerPaginated(top, skip));
        registryEntryHateoasHandler.addLinks(registryEntryHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryHateoas);
    }

    // Delete a Record identified by systemID
    // DELETE [contextPath][api]/casehandling/journalpost/{systemId}/
    @ApiOperation(value = "Deletes a single RegistryEntry entity identified by systemID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Parent entity (DocumentDescription or Record) returned", response = String.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteRecordBySystemId(HttpServletRequest request,
                                                         @ApiParam(name = "systemID",
                                                                 value = "systemID of the record to delete",
                                                                 required = true)
                                                         @PathVariable("systemID") final String systemID) {

        RegistryEntry registryEntry =
                registryEntryService.findBySystemId(systemID);
        registryEntryService.deleteEntity(systemID);
        applicationEventPublisher.publishEvent(new AfterNoarkEntityDeletedEvent(this, registryEntry));
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(CommonUtils.WebUtils.getSuccessStatusStringForDelete());
    }

    // Delete all RegistryEntry
    // DELETE [contextPath][api]/arkivstruktur/journalpost/
    @ApiOperation(value = "Deletes all RegistryEntry", response = Count.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all RegistryEntry",
                    response = Count.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<Count> deleteAllRegistryEntry() {
        return ResponseEntity.status(NO_CONTENT).
                body(new Count(registryEntryService.deleteAllByOwnedBy()));
    }

    // Update a RegistryEntry with given values
    // PUT [contextPath][api]/casehandling/journalpost/{systemId}
    @ApiOperation(value = "Updates a RegistryEntry identified by a given systemId", notes = "Returns the newly updated registryEntry",
            response = RegistryEntryHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "RegistryEntry " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = RegistryEntryHateoas.class),
            @ApiResponse(code = 201, message = "RegistryEntry " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RegistryEntryHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type RegistryEntry"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RegistryEntryHateoas> updateRegistryEntry(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of registryEntry to update",
                    required = true)
            @PathVariable("systemID") final String systemID,
            @ApiParam(name = "RegistryEntry",
                    value = "Incoming registryEntry object",
                    required = true)
            @RequestBody RegistryEntry registryEntry) throws NikitaException {
        validateForUpdate(registryEntry);

        RegistryEntry updatedRegistryEntry = registryEntryService.handleUpdate(systemID, parseETAG(request.getHeader(ETAG)), registryEntry);
        RegistryEntryHateoas registryEntryHateoas = new RegistryEntryHateoas(updatedRegistryEntry);
        registryEntryHateoasHandler.addLinks(registryEntryHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, updatedRegistryEntry));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedRegistryEntry.getVersion().toString())
                .body(registryEntryHateoas);
    }
}
