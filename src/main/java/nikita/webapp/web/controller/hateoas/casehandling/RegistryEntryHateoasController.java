package nikita.webapp.web.controller.hateoas.casehandling;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
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
    @Operation(summary = "Create a new DocumentFlow and associate it with " +
            "the given RegistryEntry systemId",
            description = "Returns the newly created DocumentFlow after it " +
                    "was associated with a RegistryEntry and persisted to the" +
                    " database.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentFlow " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "DocumentFlow " +
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
                            " of type DocumentFlow"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_DOCUMENT_FLOW,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DocumentFlowHateoas>
    createDocumentFlowAssociatedWithRegistryEntry(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of registry entry to associate " +
                            "the document flow with.",
                    required = true)
            @PathVariable String systemID,
            @Parameter(name = "documentFlow",
                    description = "Incoming documentFlow object",
                    required = true)
            @RequestBody DocumentFlow documentFlow)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(registryEntryService
                        .associateDocumentFlowWithRegistryEntry(systemID,
                                documentFlow));
    }

    // Create a new SignOff and associate it with the given journalpost
    // POST [contextPath][api]/casehandling/journalpost/{systemId}/ny-avskrivning
    //  https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-avskrivning/
    @Operation(summary = "Persists a SignOff object associated with the given" +
            " Record systemId",
            description = "Returns the newly created SignOff object after it " +
                    "was associated with a Record object and persisted to the" +
                    " database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "SignOff " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "SignOff " +
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
                            " of type SignOff"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SIGN_OFF,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SignOffHateoas>
    createSignOffAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of registry entry to associate " +
                            "the signOff with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "signOff",
                    description = "Incoming signOff object",
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
    @Operation(summary = "Persists a Precedence object associated with the " +
            "given Record systemId",
            description = "Returns the newly created Precedence object after " +
                    "it was associated with a Record object and persisted to " +
                    "the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Precedence " +
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
                            " of type Precedence"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_PRECEDENCE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PrecedenceHateoas>
    createPrecedenceAssociatedWithRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of record to associate the " +
                            "Precedence with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "Precedence",
                    description = "Incoming Precedence object",
                    required = true)
            @RequestBody Precedence precedence)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(registryEntryService.createPrecedenceAssociatedWithRecord
                        (systemID, precedence));
    }


    // GET [contextPath][api]/arkivstruktur/journalpost/{systemId}/ny-dokumentflyt
    //  https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-dokumentflyt/
    @Operation(summary = "Create a DocumentFlow with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentFlow returned"),
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
            NEW_DOCUMENT_FLOW)
    public ResponseEntity<DocumentFlowHateoas> createDefaultDocumentFlow(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the registryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryService.
                        generateDefaultDocumentFlow(systemID));
    }

    // GET [contextPath][api]/sakarkiv/journalpost/{systemID}/avskrivning/{subSystemID}
    @Operation(summary = "Return a sign off related to the registryEntry " +
            "identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "SignOff returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SIGN_OFF +
            SLASH + SUB_SYSTEM_ID_PARAMETER,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SignOffHateoas>
    findAllSignOffAssociatedWithRegistryEntry(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the registryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "subSystemID",
                    description = "systemID of the SignOff",
                    required = true)
            @PathVariable("subSystemID") final String subSystemID) {
        return ResponseEntity.status(OK)
                .body(registryEntryService
                        .findSignOffAssociatedWithRegistryEntry
                                (systemID, subSystemID));
    }

    // GET [contextPath][api]/sakarkiv/journalpost/{systemId}/ny-avskrivning
    //  https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-avskrivning/
    @Operation(summary = "Create a SignOff with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "SignOff returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SIGN_OFF)
    public ResponseEntity<SignOffHateoas> createDefaultSignOff(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of record to associate the SignOff with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryService.generateDefaultSignOff(systemID));
    }


    // GET [contextPath][api]/sakarkiv/journalpost/{systemId}/ny-presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-presedens/
    @Operation(summary = "Create a Precedence with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_PRECEDENCE)
    public ResponseEntity<PrecedenceHateoas> createDefaultPrecedence(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of record to associate the " +
                            "Precedence with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryService.
                        generateDefaultPrecedence(systemID));
    }

    // GET [contextPath][api]/casehandling/journalpost/{systemId}/dokumentflyt
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/dokumentflyt/
    @Operation(summary = "Retrieve all DocumentFlow associated with a " +
            "RegistryEntry identified by systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DocumentFlow returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + DOCUMENT_FLOW)
    public ResponseEntity<DocumentFlowHateoas>
    findAllDocumentFlowAssociatedWithRegistryEntry(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated RegistryEntry",
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
    @Operation(summary = "Retrieves a list of SignOffs associated with a " +
            "RegistryEntry")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "SignOff returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SIGN_OFF)
    public ResponseEntity<SignOffHateoas> findAllSignOffAssociatedWithRecord(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the signOff to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryService
                        .findAllSignOffAssociatedWithRegistryEntry(systemID));
    }

    // GET [contextPath][api]/casehandling/journalpost/{systemId}/presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/presedens/
    @Operation(summary = "Retrieves a list of Precedences associated with a " +
            "RegistryEntry")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Precedence returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + PRECEDENCE)
    public ResponseEntity<PrecedenceHateoas> findAllPrecedenceForRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the registryEntry to retrieve " +
                            "associated Precedence",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity
                .status(OK)
                .body(registryEntryService.
                        findAllPrecedenceForRegistryEntry(systemID));
    }

    // Retrieve a single registryEntry identified by systemId
    // GET [contextPath][api]/casehandling/journalpost/{systemID}
    @Operation(summary = "Retrieves a single RegistryEntry entity given a " +
            "systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RegistryEntry returned"),
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
    public ResponseEntity<RegistryEntryHateoas> findOneRegistryEntryBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the registryEntry to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String registryEntrySystemId) {
        RegistryEntry registryEntry = registryEntryService
                .findBySystemId(registryEntrySystemId);
        RegistryEntryHateoas registryEntryHateoas = new
                RegistryEntryHateoas(registryEntry);
        registryEntryHateoasHandler.addLinks(
                registryEntryHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(registryEntry.getVersion().toString())
                .body(registryEntryHateoas);
    }

    // Get all registryEntry
    // GET [contextPath][api]/casehandling/journalpost/
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/journalpost/
    @Operation(summary = "Retrieves multiple RegistryEntry entities limited " +
            "by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RegistryEntry found"),
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
    public ResponseEntity<RegistryEntryHateoas> findAllRegistryEntry(
            HttpServletRequest request) {
        RegistryEntryHateoas registryEntryHateoas = new
                RegistryEntryHateoas((List<INoarkEntity>) (List)
                registryEntryService.findAllRegistryEntry());
        registryEntryHateoasHandler.addLinks(registryEntryHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(registryEntryHateoas);
    }

    // Delete a Record identified by systemID
    // DELETE [contextPath][api]/casehandling/journalpost/{systemId}/
    @Operation(summary = "Deletes a single RegistryEntry entity identified " +
            "by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Delete RegistryEntry object"),
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
        registryEntryService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete all RegistryEntry
    // DELETE [contextPath][api]/arkivstruktur/journalpost/
    @Operation(summary = "Deletes all RegistryEntry")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all RegistryEntry"),
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
    public ResponseEntity<String> deleteAllRegistryEntry() {
        registryEntryService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Update a RegistryEntry with given values
    // PUT [contextPath][api]/casehandling/journalpost/{systemId}
    @Operation(summary = "Updates a RegistryEntry identified by a given " +
            "systemId",
            description = "Returns the newly updated registryEntry")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "RegistryEntry " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "RegistryEntry " +
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
                            " of type RegistryEntry"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RegistryEntryHateoas> updateRegistryEntry(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of registryEntry to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "RegistryEntry",
                    description = "Incoming registryEntry object",
                    required = true)
            @RequestBody RegistryEntry registryEntry)
            throws NikitaException {
        validateForUpdate(registryEntry);
        RegistryEntry updatedRegistryEntry =
                registryEntryService
                        .handleUpdate(systemID,
                                parseETAG(request.getHeader(ETAG)),
                                registryEntry);
        RegistryEntryHateoas registryEntryHateoas =
                new RegistryEntryHateoas(updatedRegistryEntry);
        registryEntryHateoasHandler.addLinks(
                registryEntryHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, updatedRegistryEntry));
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedRegistryEntry.getVersion().toString())
                .body(registryEntryHateoas);
    }

    // PUT [contextPath][api]/sakarkiv/journalpost/{systemID}/avskrivning/{subSystemID}
    @Operation(summary = "Update a sign off related to the" +
            "registryEntry identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "SignOff returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SIGN_OFF +
            SLASH + SUB_SYSTEM_ID_PARAMETER,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SignOffHateoas>
    updateSignOffAssociatedWithRegistryEntry(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the registryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "subSystemID",
                    description = "systemID of the SignOff",
                    required = true)
            @PathVariable("subSystemID") final String subSystemID,
            @Parameter(name = "SignOff",
                    description = "Incoming signOff object",
                    required = true)
            @RequestBody SignOff signOff) {
        SignOffHateoas signOffHateoas =
                registryEntryService.handleUpdateSignOff(systemID,
                        subSystemID, parseETAG(request.getHeader(ETAG)),
                        signOff);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(signOffHateoas.getEntityVersion().toString())
                .body(signOffHateoas);
    }

    // DELETE [contextPath][api]/sakarkiv/journalpost/{systemID}/avskrivning/{subSystemID}
    @Operation(summary = "Delete a sign off related to the" +
            "registryEntry identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "SignOff object deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + SIGN_OFF +
            SLASH + SUB_SYSTEM_ID_PARAMETER,
            produces = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<String>
    deleteSignOffAssociatedWithRegistryEntry(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the registryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "subSystemID",
                    description = "systemID of the SignOff",
                    required = true)
            @PathVariable("subSystemID") final String subSystemID) {
        registryEntryService.deleteSignOff(systemID, subSystemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
