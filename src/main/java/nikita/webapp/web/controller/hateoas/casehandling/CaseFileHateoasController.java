package nikita.webapp.web.controller.hateoas.casehandling;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.ICaseFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRegistryEntryHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = HREF_BASE_CASE_FILE,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class CaseFileHateoasController
        extends NoarkController {

    private ICaseFileService caseFileService;
    private ICaseFileHateoasHandler caseFileHateoasHandler;
    private IRegistryEntryHateoasHandler registryEntryHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public CaseFileHateoasController(ICaseFileService caseFileService,
                                     ICaseFileHateoasHandler caseFileHateoasHandler,
                                     IRegistryEntryHateoasHandler registryEntryHateoasHandler,
                                     ApplicationEventPublisher applicationEventPublisher) {
        this.caseFileService = caseFileService;
        this.caseFileHateoasHandler = caseFileHateoasHandler;
        this.registryEntryHateoasHandler = registryEntryHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Create a RecordNote entity
    // POST [contextPath][api]/sakarkiv/{systemId}/ny-journalpost
    @ApiOperation(value = "Persists a RecordNote object associated with the " +
            "given Series systemId", notes = "Returns the newly created " +
            "record object after it was associated with a File object and " +
            "persisted to the database",
            response = RecordNoteHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "RecordNote " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = RecordNoteHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_RECORD_NOTE,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RecordNoteHateoas> createRecordNoteAssociatedWithFile(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to associate the recordNote with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "RecordNote",
                    value = "Incoming recordNote object",
                    required = true)
            @RequestBody RecordNote recordNote) {
        return caseFileService.createRecordNoteToCaseFile(systemID, recordNote);
    }

    // Create a RegistryEntry entity
    // POST [contextPath][api]/sakarkiv/{systemId}/ny-journalpost
    @ApiOperation(value = "Persists a RegistryEntry object associated with the given Series systemId",
            notes = "Returns the newly created record object after it was associated with a File object and " +
                    "persisted to the database", response = RegistryEntryHateoas.class)
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_REGISTRY_ENTRY,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<RegistryEntryHateoas> createRegistryEntryAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to associate the record with",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "RegistryEntry",
                    value = "Incoming registryEntry object",
                    required = true)
            @RequestBody RegistryEntry registryEntry) {
        RegistryEntry createdRegistryEntry =
                caseFileService.
                        createRegistryEntryAssociatedWithCaseFile(systemID,
                                registryEntry);
        RegistryEntryHateoas registryEntryHateoas = new RegistryEntryHateoas(createdRegistryEntry);
        registryEntryHateoasHandler.addLinks(registryEntryHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, createdRegistryEntry));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdRegistryEntry.getVersion().toString())
                .body(registryEntryHateoas);
    }

    // POST [contextPath][api]/sakarkiv/saksmappe/{systemId}/ny-presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-presedens/
    @ApiOperation(value = "Persists a Precedence object associated with the given File systemId",
            notes = "Returns the newly created Precedence object after " +
                    "it was associated with a File object and persisted to " +
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
    public ResponseEntity<PrecedenceHateoas> createPrecedenceAssociatedWithFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of file to associate the Precedence with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "Precedence",
                    value = "Incoming Precedence object",
                    required = true)
            @RequestBody Precedence precedence)
            throws NikitaException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(caseFileService.createPrecedenceAssociatedWithFile
                        (systemID, precedence));
    }


    // API - All GET Requests (CRUD - READ)

    // Create a RegistryEntry object with default values
    // GET [contextPath][api]/sakarkiv/mappe/{systemID}/ny-journalpost
    @ApiOperation(value = "Create a RegistryEntry with default values",
            response = RegistryEntryHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "RegistryEntry returned",
                    response = RegistryEntryHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_REGISTRY_ENTRY)
    public ResponseEntity<RegistryEntryHateoas> createDefaultRegistryEntry(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the caseFile to retrieve a template " +
                            "RegistryEntry",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return caseFileService.generateDefaultRegistryEntry(systemID);
    }

    // Create a RecordNote object with default values
    // GET [contextPath][api]/sakarkiv/mappe/{systemID}/ny-arkivnotat
    @ApiOperation(value = "Create a RecordNote with default values",
            response = RecordNoteHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "RecordNote returned",
                    response = RecordNoteHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_RECORD_NOTE)
    public ResponseEntity<RecordNoteHateoas> createDefaultRecordNote(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the caseFile to retrieve a template " +
                            "RecordNote",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return caseFileService.generateDefaultRecordNote(systemID);
    }

    // Retrieve a single casefile identified by systemId
    // GET [contextPath][api]/sakarkiv/saksmappe/{systemID}
    @ApiOperation(value = "Retrieves a single CaseFile entity given a systemId", response = CaseFile.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CaseFile returned", response = CaseFile.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<CaseFileHateoas> findOneCaseFilebySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the caseFile to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String caseFileSystemId) {
        CaseFile caseFile = caseFileService.findBySystemId(caseFileSystemId);
        if (caseFile == null) {
            throw new NoarkEntityNotFoundException(caseFileSystemId);
        }
        CaseFileHateoas caseFileHateoas = new
                CaseFileHateoas(caseFile);
        caseFileHateoasHandler.addLinks(caseFileHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(caseFile.getVersion().toString())
                .body(caseFileHateoas);
    }

    // Retrieve all RegistryEntry associated with a casefile identified by systemId
    // GET [contextPath][api]/sakarkiv/saksmappe/{systemID}/journalpost
    @ApiOperation(value = "Retrieves all RegistryEntry associated with a " +
            "CaseFile identified by systemId",
            response = RegistryEntryHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "RegistryEntry list returned",
                    response = RegistryEntryHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + REGISTRY_ENTRY)
    public ResponseEntity<RegistryEntryHateoas>
    findRegistryEntryToCaseFileBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the caseFile to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return caseFileService.findAllRegistryEntryToCaseFile(systemID);
    }

    // Retrieve all RecordNote associated with a casefile identified by systemId
    // GET [contextPath][api]/sakarkiv/saksmappe/{systemID}/journalpost
    @ApiOperation(value = "Retrieves all RecordNote associated with a " +
            "CaseFile identified by systemId",
            response = RecordNoteHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "RecordNote list returned",
                    response = RecordNoteHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + RECORD_NOTE)
    public ResponseEntity<RecordNoteHateoas>
    findRecordNoteToCaseFileBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the caseFile to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return caseFileService.findAllRecordNoteToCaseFile(systemID);
    }

    @ApiOperation(value = "Retrieves multiple CaseFile entities limited by ownership rights", notes = "The field skip" +
            "tells how many CaseFile rows of the result set to ignore (starting at 0), while  top tells how many rows" +
            " after skip to return. Note if the value of top is greater than system value " +
            " nikita-noark5-core.pagination.maxPageSize, then nikita-noark5-core.pagination.maxPageSize is used. ",
            response = CaseFileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CaseFile list found",
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping
    public ResponseEntity<CaseFileHateoas> findAllCaseFile(
            HttpServletRequest request,
            @RequestParam(name = "top", required = false) Integer top,
            @RequestParam(name = "skip", required = false) Integer skip) {

        CaseFileHateoas caseFileHateoas = new
                CaseFileHateoas((List<INoarkEntity>) (List)
                caseFileService.findCaseFileByOwnerPaginated(top, skip));

        caseFileHateoasHandler.addLinks(caseFileHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(caseFileHateoas);
    }
    
    // GET [contextPath][api]/sakarkiv/saksmappe/{systemID}/presedens
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/presedens/
    @ApiOperation(value = "Retrieves all Precedence associated with a " +
            "CaseFile identified by systemId",
            response = PrecedenceHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Precedence list returned",
                    response = PrecedenceHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + PRECEDENCE)
    public ResponseEntity<PrecedenceHateoas>
    findPrecedenceForCaseFileBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the caseFile to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(caseFileService.
                        findAllPrecedenceForCaseFile(systemID));
    }

    // GET [contextPath][api]/sakarkiv/saksmappe/{systemId}/ny-presedens
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
                    value = "systemId of file to associate the Precedence with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(caseFileService.
                        generateDefaultPrecedence(systemID));
    }

    // Delete a CaseFile identified by systemID
    // DELETE [contextPath][api]/sakarkiv/saksmappe/{systemId}/
    @ApiOperation(value = "Deletes a single CaseFile entity identified by " +
            SYSTEM_ID, response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "CaseFile deleted", response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteCaseFileBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the caseFile to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        caseFileService.deleteEntity(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete all CaseFile
    // DELETE [contextPath][api]/sakarkiv/saksmappe/
    @ApiOperation(value = "Deletes all CaseFile",
                  response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all CaseFile",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping
    public ResponseEntity<String> deleteAllCaseFile() {
        caseFileService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Update a CaseFile with given values
    // PUT [contextPath][api]/sakarkiv/saksmappe/{systemId}
    @ApiOperation(value = "Updates a CaseFile identified by a given systemId", notes = "Returns the newly updated caseFile",
            response = CaseFileHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CaseFile " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 201, message = "CaseFile " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CaseFileHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type CaseFile"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CaseFileHateoas> updateCaseFile(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of caseFile to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "CaseFile",
                    value = "Incoming caseFile object",
                    required = true)
            @RequestBody CaseFile caseFile) throws NikitaException {
        validateForUpdate(caseFile);

        CaseFile updatedCaseFile = caseFileService.handleUpdate(systemID, parseETAG(request.getHeader(ETAG)), caseFile);
        CaseFileHateoas caseFileHateoas = new CaseFileHateoas(updatedCaseFile);
        caseFileHateoasHandler.addLinks(caseFileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, updatedCaseFile));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedCaseFile.getVersion().toString())
                .body(caseFileHateoas);
    }
}
/*

    // TODO: Consider gathering all the missing fields as a string and returning all in one go
    // But the developer of the client should know what's required!
    public void checkForObligatoryCaseFileValues(CaseFile caseFile) {

        if (caseFile.getFileId() == null) {
            throw new NikitaMalformedInputDataException("The saksmappe you tried to create is malformed. The "
                    + "mappeID field is mandatory, and you have submitted an empty value.");
        }
        if (caseFile.getCaseDate() == null) {
            throw new NikitaMalformedInputDataException("The saksmappe you tried to create is malformed. The "
                    + "saksDato field is mandatory, and you have submitted an empty value.");
        }
        if (caseFile.getAdministrativeUnit() == null) {
            throw new NikitaMalformedInputDataException("The saksmappe you tried to create is malformed. The "
                    + "field administrativEnhet is mandatory, and you have submitted an empty value.");
        }
        if (caseFile.getCaseResponsible() == null) {
            throw new NikitaMalformedInputDataException("The saksmappe  you tried to create is malformed. The "
                    + "saksansvarlig field is mandatory, and you have submitted an empty value.");
        }
        if (caseFile.getCaseStatus() == null) {
            throw new NikitaMalformedInputDataException("The saksmappe you tried to create is malformed. The "
                    + "saksstatus field is mandatory, and you have submitted an empty value.");
        }
    }
 @Override

public void checkForObligatoryNoarkValues(INoarkGeneralEntity noarkEntity) {
    if (noarkEntity.getTitle() == null) {
        throw new NikitaMalformedInputDataException("The saksmappe you tried to create is malformed. The "
                + "tittel field is mandatory, and you have submitted an empty value.");
    }
}
 */
