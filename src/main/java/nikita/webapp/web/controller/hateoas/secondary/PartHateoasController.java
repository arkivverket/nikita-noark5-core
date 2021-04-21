package nikita.webapp.web.controller.hateoas.secondary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.hateoas.secondary.PartHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.secondary.IPartHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.secondary.IPartService;
import nikita.webapp.web.controller.hateoas.NoarkController;
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
public class PartHateoasController
        extends NoarkController {

    private final IPartHateoasHandler partHateoasHandler;
    private final IPartService partService;

    public PartHateoasController(IPartHateoasHandler partHateoasHandler,
                                 IPartService partService) {
        this.partHateoasHandler = partHateoasHandler;
        this.partService = partService;
    }

    // API - All GET Requests (CRUD - READ)
    // GET [contextPath][api]/arkivstruktur/partperson/{systemId}
    @Operation(summary = "Retrieves a single PartPerson entity given a " +
            "systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "PartPerson returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = PART_PERSON + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PartPersonHateoas> findOnePartPersonBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the partPerson to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID partPersonSystemId) {
        PartPerson partPerson =
                (PartPerson) partService.findBySystemId(partPersonSystemId);
        PartPersonHateoas partPersonHateoas =
                new PartPersonHateoas(partPerson);
        partHateoasHandler.addLinks(partPersonHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partPerson.getVersion().toString())
                .body(partPersonHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/partenhet/{systemId}
    @Operation(summary = "Retrieves a single PartUnit entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "PartUnit returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = PART_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PartUnitHateoas> findOnePartUnitBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the partUnit to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID partUnitSystemId) {
        PartUnit partUnit =
                (PartUnit) partService.findBySystemId(partUnitSystemId);
        PartUnitHateoas partUnitHateoas = new PartUnitHateoas(partUnit);
        partHateoasHandler.addLinks(partUnitHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partUnit.getVersion().toString())
                .body(partUnitHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/partenhet/{systemId}
    @Operation(summary = "Updates a PartUnit identified by a given systemId",
            description = "Returns the newly updated partUnit")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "PartUnit " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "PartUnit " +
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
                            " of type PartUnit"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = PART_UNIT + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartUnitHateoas> updatePartUnit(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of partUnit to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "PartUnit",
                    description = "Incoming partUnit object",
                    required = true)
            @RequestBody PartUnit partUnit) throws NikitaException {
        validateForUpdate(partUnit);

        PartUnit updatedPartUnit = partService.updatePartUnit(systemID,
                parseETAG(request.getHeader(ETAG)), partUnit);
        PartUnitHateoas partUnitHateoas = new
                PartUnitHateoas(updatedPartUnit);
        partHateoasHandler.addLinks(partUnitHateoas, new Authorisation());
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedPartUnit.getVersion().toString())
                .body(partUnitHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/partperson/{systemId}
    @Operation(summary = "Updates a PartPerson identified by a given systemId",
            description = "Returns the newly updated partPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "PartPerson " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "PartPerson " +
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
                            " of type PartPerson"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = PART_PERSON + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartPersonHateoas> updatePartPerson(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of partPerson to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "PartPerson",
                    description = "Incoming partPerson object",
                    required = true)
            @RequestBody PartPerson partPerson) throws NikitaException {
        validateForUpdate(partPerson);
        PartPerson updatedPartPerson =
                partService.updatePartPerson(systemID,
                        parseETAG(request.getHeader(ETAG)), partPerson);
        PartPersonHateoas partPersonHateoas =
                new PartPersonHateoas(updatedPartPerson);
        partHateoasHandler.addLinks(partPersonHateoas, new Authorisation());
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedPartPerson.getVersion().toString())
                .body(partPersonHateoas);
    }

    // Update a Part with given values
    // PATCH [contextPath][api]/arkivstruktur/partperson/{systemId}
    @Operation(summary = "Updates a Part identified by a given systemId",
            description = "Returns the newly updated part")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Part OK"),
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
    @PatchMapping(value =
            SLASH + PART_PERSON + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartHateoas> patchPart(
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of part to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Part",
                    description = "Incoming part object",
                    required = true)
            @RequestBody PatchObjects patchObjects)
            throws NikitaException {
        return partService.handleUpdate(systemID, patchObjects);
    }

    // DELETE [contextPath][api]/arkivstruktur/partenhet/{systemID}/
    @Operation(summary = "Deletes a single PartUnit entity identified by " +
            SYSTEM_ID)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "PartUnit deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = PART_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deletePartUnit(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the partUnit to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        partService.deletePartUnit(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // DELETE [contextPath][api]/arkivstruktur/partperson/{systemID}/
    @Operation(summary = "Deletes a single PartPerson entity identified by " +
            SYSTEM_ID)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "PartPerson deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = PART_PERSON + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deletePartPerson(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the partPerson to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        partService.deletePartPerson(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
