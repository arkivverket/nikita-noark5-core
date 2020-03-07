package nikita.webapp.web.controller.hateoas.casehandling;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.IRegistryEntryHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class RegistryEntryHateoasController
        extends NoarkController {

    private final IRegistryEntryService registryEntryService;
    private final IRegistryEntryHateoasHandler registryEntryHateoasHandler;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RegistryEntryHateoasController(
            IRegistryEntryService registryEntryService,
            IRegistryEntryHateoasHandler registryEntryHateoasHandler,
            ApplicationEventPublisher applicationEventPublisher) {
        this.registryEntryService = registryEntryService;
        this.registryEntryHateoasHandler = registryEntryHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)

    // POST [contextPath][api]/casehandling/journalpost/{systemId}/ny-dokumentflyt
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-dokumentflyt/
    @ApiOperation(value = "Create a new DocumentFlow and associate it with " +
            "the given RegistryEntry systemId",
            notes = "Returns the newly created DocumentFlow after it " +
                    "was associated with a RegistryEntry and persisted to the " +
                    "database.",
            response = DocumentFlow.class) // DocumentFlowHateoas
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DocumentFlow.class), // DocumentFlowHateoas
            @ApiResponse(code = 201, message = "DocumentFlow " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DocumentFlow.class), // DocumentFlowHateoas
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type DocumentFlow"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_FLOW,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentFlowHateoas>
    createDocumentFlowAssociatedWithRegistryEntry(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of registry entry to associate the document flow with.",
                    required = true)
            @PathVariable String systemID,
            @ApiParam(name = "documentFlow",
                    value = "Incoming documentFlow object",
                    required = true)
            @RequestBody DocumentFlow documentFlow)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(registryEntryService.associateDocumentFlowWithRegistryEntry
                        (systemID, documentFlow));
    }

    // Create a new SignOff and associate it with the given journalpost
    // POST [contextPath][api]/casehandling/journalpost/{systemId}/ny-avskrivning
    //  https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-avskrivning/
    @ApiOperation(value = "Persists a SignOff object associated with the given Record systemId",
            notes = "Returns the newly created SignOff object after it " +
                    "was associated with a Record object and persisted to the database",
            response = SignOffHateoas.class)
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SIGN_OFF,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SignOffHateoas>
    createSignOffAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of registry entry to associate the signOff with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "signOff",
                    value = "Incoming signOff object",
                    required = true)
            @RequestBody SignOff signOff)
            throws NikitaException {
        SignOffHateoas signOffHateoas = registryEntryService
                .createSignOffAssociatedWithRegistryEntry(systemID, signOff);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(signOffHateoas.getEntityVersion().toString())
                .body(signOffHateoas);
    }

    // POST [contextPath][api]/sakarkiv/journalpost/{systemId}/ny-presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-presedens/
    @ApiOperation(value = "Persists a Precedence object associated with the given Record systemId",
            notes = "Returns the newly created Precedence object after " +
                    "it was associated with a Record object and persisted to " +
                    "the database",
            response = PrecedenceHateoas.class)
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the Precedence with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "Precedence",
                    value = "Incoming Precedence object",
                    required = true)
            @RequestBody Precedence precedence)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(registryEntryService.createPrecedenceAssociatedWithRecord
                        (systemID, precedence));
    }


    // GET [contextPath][api]/arkivstruktur/journalpost/{systemId}/ny-dokumentflyt
    //  https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-dokumentflyt/
    @ApiOperation(value = "Create a DocumentFlow with default values",
            response = DocumentFlow.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow returned",
                    response = DocumentFlow.class), // DocumentFlowHateoas
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_DOCUMENT_FLOW)
    public ResponseEntity<DocumentFlowHateoas> createDefaultDocumentFlow(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the registryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryService.
                        generateDefaultDocumentFlow(systemID));
    }

    // GET [contextPath][api]/sakarkiv/journalpost/{systemID}/avskrivning/{subSystemID}
    @ApiOperation(value = "Return a sign off related to the" +
            "registryEntry identified by a systemId",
            response = SignOffHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "SignOff returned",
                    response = SignOffHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SIGN_OFF + SLASH + SUB_SYSTEM_ID_PARAMETER,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SignOffHateoas>
    findAllSignOffAssociatedWithRegistryEntry(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the registryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "subSystemID",
                    value = "systemID of the SignOff",
                    required = true)
            @PathVariable("subSystemID") final String subSystemID)
            throws IOException {
        return ResponseEntity.status(OK)
                .body(registryEntryService
                        .findSignOffAssociatedWithRegistryEntry
                                (systemID, subSystemID));
    }

    // GET [contextPath][api]/sakarkiv/journalpost/{systemId}/ny-avskrivning
    //  https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-avskrivning/
    @ApiOperation(value = "Create a SignOff with default values",
            response = SignOffHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SignOff returned",
                    response = SignOffHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SIGN_OFF)
    public ResponseEntity<SignOffHateoas> createDefaultSignOff(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the SignOff with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryService.generateDefaultSignOff(systemID));
    }


    // GET [contextPath][api]/sakarkiv/journalpost/{systemId}/ny-presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-presedens/
    @ApiOperation(value = "Create a Precedence with default values",
            response = PrecedenceHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Precedence returned",
                    response = PrecedenceHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PRECEDENCE)
    public ResponseEntity<PrecedenceHateoas> createDefaultPrecedence(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of record to associate the Precedence with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryService.
                        generateDefaultPrecedence(systemID));
    }

    // GET [contextPath][api]/casehandling/journalpost/{systemId}/dokumentflyt
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/dokumentflyt/
    @ApiOperation(value = "Retrieve all DocumentFlow associated with a RegistryEntry identified by systemId",
            response = DocumentFlow.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DocumentFlow returned", response = DocumentFlow.class), //DocumentFlowHateoas
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + DOCUMENT_FLOW)
    public ResponseEntity<DocumentFlowHateoas> findAllDocumentFlowAssociatedWithRegistryEntry(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the file to retrieve associated RegistryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity
                .status(OK)
                .body(registryEntryService.
                        findAllDocumentFlowWithRegistryEntryBySystemId(
                                systemID));
    }


    // Retrieve all SignOff associated with a RegistryEntry identified by systemId
    // GET [contextPath][api]/sakarkiv/journalpost/{systemId}/avskrivning
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/avskrivning/
    @ApiOperation(value = "Retrieves a list of SignOffs associated with a RegistryEntry",
            response = SignOff.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SignOff returned",
                    response = SignOffHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SIGN_OFF)
    public ResponseEntity<SignOffHateoas> findAllSignOffAssociatedWithRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the signoff to retrieve associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryService
                        .findAllSignOffAssociatedWithRegistryEntry(systemID));
    }

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
    public ResponseEntity<PrecedenceHateoas> findAllPrecedenceForRecord(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the registryEntry to retrieve associated Precedence",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity
                .status(OK)
                .body(registryEntryService.
                        findAllPrecedenceForRegistryEntry(systemID));
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the registryEntry to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String registryEntrySystemId) {
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
                RegistryEntryHateoas((List<INoarkEntity>) (List)
                registryEntryService.findRegistryEntryByOwnerPaginated(top, skip));
        registryEntryHateoasHandler.addLinks(registryEntryHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryHateoas);
    }

    // Delete a Record identified by systemID
    // DELETE [contextPath][api]/casehandling/journalpost/{systemId}/
    @ApiOperation(value = "Deletes a single RegistryEntry entity identified " +
            "by systemID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Delete RegistryEntry object",
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
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the record to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        registryEntryService.deleteEntity(systemID);
        return ResponseEntity.status(OK)
                .body(DELETE_RESPONSE);
    }

    // Delete all RegistryEntry
    // DELETE [contextPath][api]/arkivstruktur/journalpost/
    @ApiOperation(value = "Deletes all RegistryEntry", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all RegistryEntry",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<String> deleteAllRegistryEntry() {
        registryEntryService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Update a RegistryEntry with given values
    // PUT [contextPath][api]/casehandling/journalpost/{systemId}
    @ApiOperation(value = "Updates a RegistryEntry identified by a given " +
            "systemId", notes = "Returns the newly updated registryEntry",
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
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of registryEntry to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
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

    // PUT [contextPath][api]/sakarkiv/journalpost/{systemID}/avskrivning/{subSystemID}
    @ApiOperation(value = "Update a sign off related to the" +
            "registryEntry identified by a systemId",
            response = SignOffHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "SignOff returned",
                    response = SignOffHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SIGN_OFF + SLASH + SUB_SYSTEM_ID_PARAMETER,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SignOffHateoas>
    updateSignOffAssociatedWithRegistryEntry(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the registryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "subSystemID",
                    value = "systemID of the SignOff",
                    required = true)
            @PathVariable("subSystemID") final String subSystemID,
            @ApiParam(name = "SignOff",
                    value = "Incoming signOff object",
                    required = true)
            @RequestBody SignOff signOff)
            throws IOException {
        SignOffHateoas signOffHateoas =
                registryEntryService.handleUpdateSignOff
                        (systemID, subSystemID, parseETAG(request.getHeader(ETAG)),
                                signOff);
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(signOffHateoas.getEntityVersion().toString())
                .body(signOffHateoas);
    }

    // DELETE [contextPath][api]/sakarkiv/journalpost/{systemID}/avskrivning/{subSystemID}
    @ApiOperation(value = "Delete a sign off related to the" +
            "registryEntry identified by a systemId",
            response = SignOffHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "SignOff object deleted",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SIGN_OFF +
            SLASH + SUB_SYSTEM_ID_PARAMETER,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String>
    deleteeSignOffAssociatedWithRegistryEntry(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the registryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "subSystemID",
                    value = "systemID of the SignOff",
                    required = true)
            @PathVariable("subSystemID") final String subSystemID) {
        registryEntryService.deleteSignOff(systemID, subSystemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
