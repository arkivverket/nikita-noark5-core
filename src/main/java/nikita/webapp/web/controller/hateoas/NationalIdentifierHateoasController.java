package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.INationalIdentifierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class NationalIdentifierHateoasController
        extends NoarkController {

    private final INationalIdentifierService nationalIdentifierService;

    public NationalIdentifierHateoasController(
            INationalIdentifierService nationalIdentifierService) {
        this.nationalIdentifierService = nationalIdentifierService;
    }

    // API - All GET Requests (CRUD - READ)

    // GET [contextPath][api]/arkivstruktur/bygning/{systemID}
    @Operation(summary = "Retrieves a single Building entity given a systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Building returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = BUILDING + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<BuildingHateoas> findBuildingBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Building to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService.findBuildingBySystemId(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/matrikkel/{systemID}
    @Operation(summary = "Retrieves a single CadastralUnit entity given a " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CadastralUnit returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = CADASTRAL_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<CadastralUnitHateoas> findCadastralUnitBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the CadastralUnit to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService
                        .findCadastralUnitBySystemId(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/dnummer/{systemID}
    @Operation(summary = "Retrieves a single DNumber entity given a systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DNumber returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = D_NUMBER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<DNumberHateoas> findOneDNumberBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the DNumber to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService.findDNumberBySystemId(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/plan/{systemID}
    @Operation(summary = "Retrieves a single Plan entity given a systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Plan returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = PLAN + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PlanHateoas> findPlanBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Plan to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService.findPlanBySystemId(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/posisjon/{systemID}
    @Operation(summary = "Retrieves a single Position entity given a systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Position returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = POSITION + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PositionHateoas> findPositionBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the position to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService
                        .findPositionBySystemId(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/foedselsnummer/{systemID}
    @Operation(summary = "Retrieves a single SocialSecurityNumber entity " +
            "given a systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "SocialSecurityNumber returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SOCIAL_SECURITY_NUMBER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<SocialSecurityNumberHateoas>
    findOneSocialSecurityNumberBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the socialSecurityNumber to " +
                            "retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService
                        .findSocialSecurityNumberBySystemId(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/enhetsidentifikator/{systemID}
    @Operation(summary = "Retrieves a single Unit entity given a systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Unit returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = NI_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<UnitHateoas> findUnitBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the unit to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService.findUnitBySystemId(systemID));
    }

    // PUT [contextPath][api]/casehandling/building/{systemID}
    @Operation(summary = "Updates a Building identified by a given systemID",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Building " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Building " +
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
                            " of type Building"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = BUILDING + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<BuildingHateoas> updateBuilding(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of nationalIdentifierPerson to " +
                            "update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Building",
                    description = "Incoming nationalIdentifierPerson object",
                    required = true)
            @RequestBody Building building) throws NikitaException {
        validateForUpdate(building);
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService
                        .updateBuilding(systemID,
                                parseETAG(request.getHeader(ETAG)), building));
    }

    // PUT [contextPath][api]/casehandling/matrikkel/{systemID}
    @Operation(summary = "Updates a CadastralUnit identified by a given systemID",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CadastralUnit " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "CadastralUnit " +
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
                            " of type CadastralUnit"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = CADASTRAL_UNIT + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CadastralUnitHateoas> updateCadastralUnit(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of nationalIdentifierPerson to " +
                            "update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CadastralUnit",
                    description = "Incoming nationalIdentifierPerson object",
                    required = true)
            @RequestBody CadastralUnit cadastralUnit) throws NikitaException {
        validateForUpdate(cadastralUnit); // TODO no-op
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService
                        .updateCadastralUnit(systemID,
                                parseETAG(request.getHeader(ETAG)),
                                cadastralUnit));
    }

    // PUT [contextPath][api]/arkivstruktur/dnummer/{systemID}
    @Operation(summary = "Updates a DNumber identified by a given systemID",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "DNumber " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "DNumber " +
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
                            " of type DNumber"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = D_NUMBER + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DNumberHateoas> updateDNumber(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of nationalIdentifierPerson to " +
                            "update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "DNumber",
                    description = "Incoming nationalIdentifierPerson object",
                    required = true)
            @RequestBody DNumber dNumber) throws NikitaException {
        validateForUpdate(dNumber);
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService
                        .updateDNumber(systemID,
                                parseETAG(request.getHeader(ETAG)), dNumber));
    }

    // PUT [contextPath][api]/arkivstruktur/plan/{systemID}
    @Operation(summary = "Updates a Plan identified by a given systemID",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Plan " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Plan " +
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
                            " of type Plan"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = PLAN + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PlanHateoas> updatePlan(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of nationalIdentifierPerson to " +
                            "update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Plan",
                    description = "Incoming nationalIdentifierPerson object",
                    required = true)
            @RequestBody Plan plan) throws NikitaException {
        validateForUpdate(plan);
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService.updatePlan
                        (systemID, parseETAG(request.getHeader(ETAG)), plan));
    }

    // PUT [contextPath][api]/arkivstruktur/posisjon/{systemID}
    @Operation(summary = "Updates a Position identified by a given systemID",
            description = "Returns the newly updated position")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Position " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Position " +
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
                            " of type Position"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = POSITION + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PositionHateoas> updatePosition(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of position to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Position",
                    description = "Incoming position object",
                    required = true)
            @RequestBody Position position) throws NikitaException {
        validateForUpdate(position);
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService.updatePosition
                        (systemID, parseETAG(request.getHeader(ETAG)), position));
    }

    // PUT [contextPath][api]/arkivstruktur/foedselsnummer/{systemID}
    @Operation(summary = "Updates a SocialSecurityNumber identified by a " +
            "given systemID",
            description = "Returns the newly updated socialSecurityNumber")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "SocialSecurityNumber " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "SocialSecurityNumber " +
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
                            " of type SocialSecurityNumber"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SOCIAL_SECURITY_NUMBER + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SocialSecurityNumberHateoas>
    updateSocialSecurityNumber(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of socialSecurityNumber to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "SocialSecurityNumber",
                    description = "Incoming socialSecurityNumber object",
                    required = true)
            @RequestBody SocialSecurityNumber socialSecurityNumber)
            throws NikitaException {
        validateForUpdate(socialSecurityNumber);
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService.updateSocialSecurityNumber
                        (systemID, parseETAG(
                                request.getHeader(ETAG)), socialSecurityNumber));
    }

    // PUT [contextPath][api]/arkivstruktur/enhetsidentifikator/{systemID}
    @Operation(summary = "Updates a Unit identified by a given systemID",
            description = "Returns the newly updated unit")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Unit " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Unit " +
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
                            " of type Unit"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = NI_UNIT + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<UnitHateoas> updateUnit(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of unit to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "Unit",
                    description = "Incoming unit object",
                    required = true)
            @RequestBody Unit unit) throws NikitaException {
        validateForUpdate(unit);
        return ResponseEntity.status(OK)
                .body(nationalIdentifierService.updateUnit
                        (systemID, parseETAG(request.getHeader(ETAG)), unit));
    }

    // Delete a building identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/bygning/{systemID}/
    @Operation(summary = "Deletes a single Building entity identified by " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Building deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = BUILDING + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteBuilding(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the building to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        nationalIdentifierService.deleteBuilding(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a cadastralUnit identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/matrikkel/{systemID}/
    @Operation(summary = "Deletes a single CadastralUnit entity identified by" +
            " systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "CadastralUnit deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = CADASTRAL_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteCadastralUnit(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the cadastralUnit to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        nationalIdentifierService.deleteCadastralUnit(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a dnumber identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/dnummer/{systemID}/
    @Operation(summary = "Deletes a single DNumber entity identified by " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "DNumber deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = D_NUMBER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteDNumber(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the dnumber to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        nationalIdentifierService.deleteDNumber(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a plan identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/plan/{systemID}/
    @Operation(summary = "Deletes a single Plan entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Plan deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = PLAN + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deletePlan(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the plan to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        nationalIdentifierService.deletePlan(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a position identified by systemid
    // DELETE [contextPath][api]/arkivstruktur/posisjon/{systemID}/
    @Operation(summary = "Deletes a single Position entity identified by " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Position deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = POSITION + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deletePosition(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the position to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        nationalIdentifierService.deletePosition(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a socialSecurityNumber identified by systemid
    // DELETE [contextPath][api]/arkivstruktur/foedselsnummer/{systemID}/
    @Operation(summary = "Deletes a single SocialSecurityNumber entity " +
            "identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "SocialSecurityNumber deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = SOCIAL_SECURITY_NUMBER + SLASH +
            SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteSocialSecurityNumber(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the socialSecurityNumber to " +
                            "delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        nationalIdentifierService.deleteSocialSecurityNumber(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a unit identified by systemid
    // DELETE [contextPath][api]/arkivstruktur/enhetsidentifikator/{systemID}/
    @Operation(summary = "Deletes a single Unit entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Unit deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = NI_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteUnit(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the unit to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        nationalIdentifierService.deleteUnit(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
