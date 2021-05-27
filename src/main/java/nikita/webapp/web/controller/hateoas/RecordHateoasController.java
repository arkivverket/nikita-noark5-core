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
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.util.error.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_RECORD,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class RecordHateoasController
        extends NoarkController {

    private final IDocumentDescriptionService documentDescriptionService;
    private final IRecordService recordService;

    public RecordHateoasController(
            IDocumentDescriptionService documentDescriptionService,
            IRecordService recordService) {
        this.documentDescriptionService = documentDescriptionService;
        this.recordService = recordService;
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "documentDescription with.",
                    required = true)
            @PathVariable UUID systemID,
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
                .body(documentDescriptionHateoas);
    }

    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/skjermingmetadata/
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/skjermingmetadata/
    @Operation(summary = "Create a ScreeningMetadata associated with a Record" +
            "identified by the given systemId",
            description = "Returns the newly updated ScreeningMetadata")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "ScreeningMetadata " +
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
                            " of type ScreeningMetadata"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_SCREENING_METADATA,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ScreeningMetadataHateoas>
    createScreeningMetadataBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of File to associate " +
                            "ScreeningMetadata with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "ScreeningMetadata",
                    description = "Incoming ScreeningMetadata object",
                    required = true)
            @RequestBody final Metadata screeningMetadata)
            throws NikitaException {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                recordService.createScreeningMetadataAssociatedWithRecord(
                        systemID, screeningMetadata);
        return ResponseEntity.status(CREATED)
                .body(screeningMetadataHateoas);
    }

    // Create a StorageLocation
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-oppbevaringssted
    @Operation(summary = "Get a default StorageLocation object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "StorageLocation returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value =
            SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_STORAGE_LOCATION)
    public ResponseEntity<StorageLocationHateoas> createStorageLocation(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "StorageLocation",
                    description = "Incoming storageLocation",
                    required = true)
            @RequestBody StorageLocation storageLocation)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(recordService
                        .createStorageLocationAssociatedWithRecord(
                                systemID, storageLocation));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.getPartAssociatedWithRecord(systemID));
    }


    // Retrieve all ScreeningMetadata associated with the Screening of a Record
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/skjermingmetadata
    @Operation(summary = "Retrieves all ScreeningMetadata associated with the" +
            " Screening object of a Record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value =
            SLASH + SYSTEM_ID_PARAMETER + SLASH + SCREENING_METADATA)
    public ResponseEntity<ScreeningMetadataHateoas>
    getScreeningMetadataAssociatedWithRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to retrieve " +
                            "screening metadata",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService
                        .getScreeningMetadataAssociatedWithRecord(systemID));
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
            @PathVariable(SYSTEM_ID) final UUID systemID) {
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated File",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
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
    public ResponseEntity<ApiError> addReferenceSeriesToRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "secondary Series with",
                    required = true)
            @PathVariable UUID systemID,
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
            @PathVariable UUID systemID,
            @Parameter(name = "author",
                    description = "Incoming author object",
                    required = true)
            @RequestBody Author author)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(recordService.associateAuthorWithRecord
                        (systemID, author));
    }

    // Create a new Keyword and associate it with the given Record
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-noekkelord
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-noekkelord/
    @Operation(
            summary = "Persists an keyword object associated with the given " +
                    "Record systemId",
            description = "Returns the newly created keyword object after it " +
                    "was associated with a Record object and persisted to the" +
                    " database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Keyword " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Keyword " +
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
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_KEYWORD,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<KeywordHateoas> addKeywordAssociatedWithRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to associate the " +
                            "keyword with.",
                    required = true)
            @PathVariable UUID systemID,
            @Parameter(name = "keyword",
                    description = "Incoming keyword object",
                    required = true)
            @RequestBody Keyword keyword)
            throws NikitaException {
        KeywordHateoas keywordHateoas = recordService
                .createKeywordAssociatedWithRecord(systemID, keyword);
        return ResponseEntity.status(CREATED)
                .body(keywordHateoas);
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.
                        generateDefaultCorrespondencePartPerson(systemID));
    }


    // Create a default ScreeningMetadata
    // GET [contextPath][api]/arkivstruktur/registreringr/{systemId}/ny-skjermingmetadata
    @Operation(summary = "Get a default ScreeningMetadata object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value =
            SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_SCREENING_METADATA)
    public ResponseEntity<ScreeningMetadataHateoas>
    getDefaultScreeningMetadata(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the documentDescription",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.getDefaultScreeningMetadata(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "PartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID) throws NikitaException {
        return ResponseEntity.status(OK)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "PartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(recordService.
                        generateDefaultPartPerson(systemID));
    }

    // Create a default StorageLocation
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-oppbevaringssted
    @Operation(summary = "Get a default StorageLocation object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "StorageLocation returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value =
            SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_STORAGE_LOCATION)
    public ResponseEntity<StorageLocationHateoas>
    getDefaultStorageLocation(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.getDefaultStorageLocation(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to retrieve " +
                            "associated Author",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Record to generate default" +
                            " Comment for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultComment(systemID));
    }

    // Create a Keyword with default values
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-noekkelord
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-noekkelord/
    @Operation(summary = "Create a Keyword with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Keyword returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_KEYWORD)
    public ResponseEntity<KeywordHateoas> createDefaultKeyword(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultKeyword(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to retrieve " +
                            "comments for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Comment with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Comment",
                    description = "comment",
                    required = true)
            @RequestBody Comment comment) throws NikitaException {
        CommentHateoas commentHateoas = recordService
                .createCommentAssociatedWithRecord(systemID, comment);
        return ResponseEntity.status(CREATED)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "CorrespondencePartPerson with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "CorrespondencePartPerson",
                    description = "Incoming CorrespondencePartPerson object",
                    required = true)
            @RequestBody CorrespondencePartPerson correspondencePartPerson)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(recordService.
                        createCorrespondencePartPersonAssociatedWithRecord(
                                systemID, correspondencePartPerson));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the Part " +
                            "with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the Part " +
                            "with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
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
            @PathVariable(SYSTEM_ID) UUID systemID,
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of record to associate the " +
                            "CorrespondencePartUnit with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Building with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Building",
                    description = "building",
                    required = true)
            @RequestBody Building building)
            throws NikitaException {
        BuildingHateoas buildingHateoas =
                recordService.createBuildingAssociatedWithRecord(
                        systemID, building);
        return ResponseEntity.status(CREATED)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "CadastralUnit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CadastralUnit",
                    description = "CadastralUnit",
                    required = true)
            @RequestBody CadastralUnit cadastralUnit) throws NikitaException {
        CadastralUnitHateoas cadastralUnitHateoas =
                recordService.createCadastralUnitAssociatedWithRecord(
                        systemID, cadastralUnit);
        return ResponseEntity.status(CREATED)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "DNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "dNumber",
                    description = "DNumber",
                    required = true)
            @RequestBody DNumber dNumber) throws NikitaException {
        DNumberHateoas dNumberHateoas =
                recordService.createDNumberAssociatedWithRecord(
                        systemID, dNumber);
        return ResponseEntity.status(CREATED)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the Plan " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "plan",
                    description = "Plan",
                    required = true)
            @RequestBody Plan plan) throws NikitaException {
        PlanHateoas planHateoas =
                recordService.createPlanAssociatedWithRecord(systemID, plan);
        return ResponseEntity.status(CREATED)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Position with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Position",
                    description = "position",
                    required = true)
            @RequestBody Position position)
            throws NikitaException {
        PositionHateoas positionHateoas =
                recordService.createPositionAssociatedWithRecord(
                        systemID, position);
        return ResponseEntity.status(CREATED)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "SocialSecurityNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "SocialSecurityNumber",
                    description = "socialSecurityNumber",
                    required = true)
            @RequestBody SocialSecurityNumber socialSecurityNumber)
            throws NikitaException {
        SocialSecurityNumberHateoas socialSecurityNumberHateoas =
                recordService.createSocialSecurityNumberAssociatedWithRecord(
                        systemID, socialSecurityNumber);
        return ResponseEntity.status(CREATED)
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Unit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Unit",
                    description = "unit",
                    required = true)
            @RequestBody Unit unit)
            throws NikitaException {
        UnitHateoas unitHateoas =
                recordService.createUnitAssociatedWithRecord(
                        systemID, unit);
        return ResponseEntity.status(CREATED)
                .body(unitHateoas);
    }

    // Create a CrossReference
    // POST [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-kryssreferanse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-kryssreferanse/
    @Operation(summary = "Create a CrossReference associated with a File " +
            "identified by the given systemId",
            description = "Returns the newly updated CrossReference")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CrossReference " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "CrossReference " +
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
                            " of type CrossReference"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_CROSS_REFERENCE,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CrossReferenceHateoas>
    createCrossReferenceBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of File to associate " +
                            "CrossReference with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CrossReference",
                    description = "Incoming CrossReference object",
                    required = true)
            @RequestBody CrossReference crossReference)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(recordService
                        .createCrossReferenceAssociatedWithRecord(
                                systemID, crossReference));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the record to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.findBySystemId(systemID));
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
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.findFileAssociatedWithRecord(systemID));
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
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.findSeriesAssociatedWithRecord(systemID));
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
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.findClassAssociatedWithRecord(systemID));
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
    public ResponseEntity<RecordHateoas> findAllRecord() {
        return ResponseEntity.status(OK)
                .body(recordService.findAll());
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
    public ResponseEntity<ApiError> findSecondarySeriesAssociatedWithRecord(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Record to retrieve " +
                            "secondary Class for",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(documentDescriptionService.
                        generateDefaultDocumentDescription(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService
                        .getDocumentDescriptionAssociatedWithRecord(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Building with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultBuilding(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "CadastralUnit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultCadastralUnit(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "DNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultDNumber(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the Plan " +
                            "with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultPlan(systemID));
    }


    // Get a CrossReference template
    // GET [contextPath][api]/arkivstruktur/registrering/{systemId}/ny-kryssreferanse
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-kryssreferanse
    @Operation(summary = "Get a default CrossReference")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CrossReference returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_CROSS_REFERENCE)
    public ResponseEntity<CrossReferenceHateoas> getDefaultCrossReference(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the file to retrieve " +
                            "associated Record",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(recordService.getDefaultCrossReference(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Position with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultPosition(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "SocialSecurityNumber with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(recordService
                        .generateDefaultSocialSecurityNumber(systemID));
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
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Record to associate the " +
                            "Unit with",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(recordService.generateDefaultUnit(systemID));
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
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        recordService.deleteRecord(systemID);
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
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Record",
                    description = "Incoming record object",
                    required = true)
            @RequestBody Record record) throws NikitaException {
        validateForUpdate(record);
        return ResponseEntity.status(OK)
                .body(recordService.handleUpdate(
                        systemID, parseETAG(request.getHeader(ETAG)), record));
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
        return ResponseEntity.status(OK)
                .body(recordService.handleUpdate(systemID, patchObjects));
    }
}
