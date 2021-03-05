package nikita.webapp.web.controller.hateoas.casehandling;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.secondary.ICorrespondencePartHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class CorrespondencePartHateoasController
        extends NoarkController {

    private final ICorrespondencePartHateoasHandler
            correspondencePartHateoasHandler;
    private final ICorrespondencePartService correspondencePartService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CorrespondencePartHateoasController(
            ICorrespondencePartHateoasHandler correspondencePartHateoasHandler,
            ICorrespondencePartService correspondencePartService,
            ApplicationEventPublisher applicationEventPublisher) {
        this.correspondencePartHateoasHandler =
                correspondencePartHateoasHandler;
        this.correspondencePartService = correspondencePartService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All GET Requests (CRUD - READ)
    // Get a CorrespondencePartPerson identified by systemID
    // GET [contextPath][api]/casehandling/korrespondansepartperson/{systemId}
    @Operation(summary = "Retrieves a single CorrespondencePartPerson entity " +
            "given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CorrespondencePartPerson returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = CORRESPONDENCE_PART_PERSON + SLASH +
            SYSTEM_ID_PARAMETER)
    public ResponseEntity<CorrespondencePartPersonHateoas>
    findOneCorrespondencePartPersonBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the correspondencePartPerson " +
                            "to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String correspondencePartPersonSystemId) {
        CorrespondencePartPerson correspondencePartPerson =
                (CorrespondencePartPerson) correspondencePartService
                        .findBySystemId(correspondencePartPersonSystemId);
        CorrespondencePartPersonHateoas correspondencePartPersonHateoas =
                new CorrespondencePartPersonHateoas(correspondencePartPerson);
        correspondencePartHateoasHandler.addLinks(
                correspondencePartPersonHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(correspondencePartPerson.getVersion().toString())
                .body(correspondencePartPersonHateoas);
    }

    // Get a CorrespondencePartInternal identified by systemID
    // GET [contextPath][api]/casehandling/korrespondansepartintern/{systemId}
    @Operation(summary = "Retrieves a single CorrespondencePartInternal " +
            "entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CorrespondencePartInternal returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = CORRESPONDENCE_PART_INTERNAL + SLASH +
            SYSTEM_ID_PARAMETER)
    public ResponseEntity<CorrespondencePartInternalHateoas>
    findOneCorrespondencePartInternalBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the correspondencePartInternal" +
                            " to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String correspondencePartInternalSystemId) {
        CorrespondencePartInternal correspondencePartInternal =
                (CorrespondencePartInternal)
                        correspondencePartService.findBySystemId(
                                correspondencePartInternalSystemId);
        CorrespondencePartInternalHateoas correspondencePartInternalHateoas =
                new CorrespondencePartInternalHateoas(
                        correspondencePartInternal);
        correspondencePartHateoasHandler
                .addLinks(correspondencePartInternalHateoas,
                        new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(correspondencePartInternal.getVersion().toString())
                .body(correspondencePartInternalHateoas);
    }

    // Get a CorrespondencePartPerson identified by systemID
    // GET [contextPath][api]/casehandling/korrespondansepartenhet/{systemId}
    @Operation(summary = "Retrieves a single CorrespondencePartUnit entity " +
            "given a systemId")
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
    @GetMapping(value = CORRESPONDENCE_PART_UNIT + SLASH +
            SYSTEM_ID_PARAMETER)
    public ResponseEntity<CorrespondencePartUnitHateoas>
    findOneCorrespondencePartUnitBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the correspondencePartUnit to " +
                            "retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String correspondencePartUnitSystemId) {
        CorrespondencePartUnit correspondencePartUnit =
                (CorrespondencePartUnit) correspondencePartService
                        .findBySystemId(correspondencePartUnitSystemId);
        CorrespondencePartUnitHateoas correspondencePartUnitHateoas =
                new CorrespondencePartUnitHateoas(correspondencePartUnit);
        correspondencePartHateoasHandler.addLinks(
                correspondencePartUnitHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(correspondencePartUnit.getVersion().toString())
                .body(correspondencePartUnitHateoas);
    }

    // Update a CorrespondencePartUnit with given values
    // PUT [contextPath][api]/casehandling/korrespondansepartenhet/{systemId}
    @Operation(summary = "Updates a CorrespondencePartUnit identified by a " +
            "given systemId",
            description = "Returns the newly updated correspondencePartUnit")
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
    @PutMapping(value = CORRESPONDENCE_PART_UNIT + SLASH +
            SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartUnitHateoas>
    updateCorrespondencePartUnit(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of correspondencePartUnit to " +
                            "update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "CorrespondencePartUnit",
                    description = "Incoming correspondencePartUnit object",
                    required = true)
            @RequestBody CorrespondencePartUnit correspondencePartUnit)
            throws NikitaException {
        validateForUpdate(correspondencePartUnit);
        CorrespondencePartUnit updatedCorrespondencePartUnit =
                correspondencePartService.updateCorrespondencePartUnit(
                        systemID, parseETAG(request.getHeader(ETAG)),
                        correspondencePartUnit);
        CorrespondencePartUnitHateoas correspondencePartUnitHateoas = new
                CorrespondencePartUnitHateoas(updatedCorrespondencePartUnit);
        correspondencePartHateoasHandler.addLinks(correspondencePartUnitHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this,
                        updatedCorrespondencePartUnit));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedCorrespondencePartUnit.getVersion().toString())
                .body(correspondencePartUnitHateoas);
    }

    // Update a CorrespondencePartPerson with given values
    // PUT [contextPath][api]/casehandling/korrespondansepartperson/{systemId}
    @Operation(summary = "Updates a CorrespondencePartPerson identified by a " +
            "given systemId",
            description = "Returns the newly updated correspondencePartPerson")
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
    @PutMapping(value = CORRESPONDENCE_PART_PERSON + SLASH +
            SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartPersonHateoas>
    updateCorrespondencePartPerson(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of correspondencePartPerson to " +
                            "update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "CorrespondencePartPerson",
                    description = "Incoming correspondencePartPerson object",
                    required = true)
            @RequestBody CorrespondencePartPerson correspondencePartPerson)
            throws NikitaException {
        validateForUpdate(correspondencePartPerson);

        CorrespondencePartPerson updatedCorrespondencePartPerson =
                correspondencePartService
                        .updateCorrespondencePartPerson(systemID,
                                parseETAG(request.getHeader(ETAG)),
                                correspondencePartPerson);
        CorrespondencePartPersonHateoas correspondencePartPersonHateoas =
                new CorrespondencePartPersonHateoas(
                        updatedCorrespondencePartPerson);
        correspondencePartHateoasHandler.addLinks(
                correspondencePartPersonHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this,
                        updatedCorrespondencePartPerson));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedCorrespondencePartPerson.getVersion().toString())
                .body(correspondencePartPersonHateoas);
    }

    // Update a CorrespondencePartInternal with given values
    // PUT [contextPath][api]/casehandling/korrespondansepartintern/{systemId}
    @Operation(summary = "Updates a CorrespondencePartInternal identified by " +
            "a given systemId",
            description = "Returns the newly updated " +
                    "correspondencePartInternal")
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
    @PutMapping(value = CORRESPONDENCE_PART_INTERNAL + SLASH +
            SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartInternalHateoas>
    updateCorrespondencePartInternal(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of correspondencePartInternal to " +
                            "update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "CorrespondencePartInternal",
                    description = "Incoming correspondencePartInternal object",
                    required = true)
            @RequestBody CorrespondencePartInternal correspondencePartInternal)
            throws NikitaException {
        validateForUpdate(correspondencePartInternal);
        CorrespondencePartInternal updatedCorrespondencePartInternal =
                correspondencePartService
                        .updateCorrespondencePartInternal(systemID,
                                parseETAG(request.getHeader(ETAG)),
                                correspondencePartInternal);
        CorrespondencePartInternalHateoas correspondencePartInternalHateoas =
                new CorrespondencePartInternalHateoas(
                        updatedCorrespondencePartInternal);
        correspondencePartHateoasHandler.addLinks(
                correspondencePartInternalHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this,
                        updatedCorrespondencePartInternal));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedCorrespondencePartInternal.getVersion().toString())
                .body(correspondencePartInternalHateoas);
    }

    // Update a CorrespondencePart with given values
    // PATCH [contextPath][api]/arkivstruktur/korrespondansepartperson/{systemId}
    @Operation(summary = "Updates a CorrespondencePart identified by a given" +
            " systemId",
            description = "Returns the newly updated correspondencePart")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CorrespondencePart OK"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +

                            " of type CorrespondencePart"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PatchMapping(value =
            SLASH + CORRESPONDENCE_PART_PERSON + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CorrespondencePartHateoas> patchCorrespondencePart(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of correspondencePart to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CorrespondencePart",
                    description = "Incoming correspondencePart object",
                    required = true)
            @RequestBody PatchObjects patchObjects) throws NikitaException {
        return correspondencePartService.handleUpdate(systemID, patchObjects);
    }

    // Delete a correspondencePartUnit identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/korrespondansepartenhet/{systemID}/
    @Operation(summary = "Deletes a single CorrespondencePartUnit entity " +
            "identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "CorrespondencePartUnit deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = CORRESPONDENCE_PART_UNIT + SLASH +
            SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteCorrespondencePartUnit(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the correspondencePartUnit to " +
                            "delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        correspondencePartService.deleteCorrespondencePartUnit(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a correspondencePartPerson identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/korrespondansepartperson/{systemID}/
    @Operation(summary = "Deletes a single CorrespondencePartPerson entity " +
            "identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "CorrespondencePartPerson deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value =
            CORRESPONDENCE_PART_PERSON + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteCorrespondencePartPerson(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the correspondencePartPerson " +
                            "to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        correspondencePartService.deleteCorrespondencePartPerson(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a correspondencePartInternal identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/korrespondansepartintern/{systemID}/
    @Operation(summary = "Deletes a single CorrespondencePartInternal entity" +
            " identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "CorrespondencePartInternal deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value =
            CORRESPONDENCE_PART_INTERNAL + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteCorrespondencePartInternal(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the correspondencePartInternal" +
                            " to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        correspondencePartService.deleteCorrespondencePartInternal(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
