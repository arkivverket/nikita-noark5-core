package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit;
import nikita.common.model.noark5.v5.nationalidentifier.DNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Plan;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Unit;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.CadastralUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.DNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PlanHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.SocialSecurityNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.UnitHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.nationalidentifier.INationalIdentifierHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.INationalIdentifierService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class NationalIdentifierHateoasController
        extends NoarkController {

    private INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler;
    private INationalIdentifierService nationalIdentifierService;

    public NationalIdentifierHateoasController(INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler,
                                               INationalIdentifierService nationalIdentifierService) {
        this.nationalIdentifierHateoasHandler = nationalIdentifierHateoasHandler;
        this.nationalIdentifierService = nationalIdentifierService;
    }

    // API - All GET Requests (CRUD - READ)

    // GET [contextPath][api]/arkivstruktur/bygning/{systemId}
    @ApiOperation(value = "Retrieves a single Building entity given a systemId",
            response = Building.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Building returned",
                    response = Building.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = BUILDING + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<BuildingHateoas> findOneBuildingBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the Building to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        Building building =
                (Building) nationalIdentifierService.findBySystemId(systemId);
        BuildingHateoas buildingHateoas =
                new BuildingHateoas(building);
        nationalIdentifierHateoasHandler
            .addLinks(buildingHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(building.getVersion().toString())
                .body(buildingHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/matrikkel/{systemId}
    @ApiOperation(value = "Retrieves a single CadastralUnit entity given a systemId",
            response = CadastralUnit.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CadastralUnit returned",
                    response = CadastralUnit.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = CADASTRAL_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<CadastralUnitHateoas> findOneCadastralUnitBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the CadastralUnit to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        CadastralUnit cadastralUnit =
                (CadastralUnit) nationalIdentifierService.findBySystemId(systemId);
        CadastralUnitHateoas cadastralUnitHateoas =
                new CadastralUnitHateoas(cadastralUnit);
        nationalIdentifierHateoasHandler
            .addLinks(cadastralUnitHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(cadastralUnit.getVersion().toString())
                .body(cadastralUnitHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/dnummer/{systemId}
    @ApiOperation(value = "Retrieves a single DNumber entity given a systemId",
            response = DNumber.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DNumber returned",
                    response = DNumber.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = D_NUMBER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<DNumberHateoas> findOneDNumberBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the DNumber to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        DNumber dNumber =
                (DNumber) nationalIdentifierService.findBySystemId(systemId);
        DNumberHateoas dNumberHateoas =
                new DNumberHateoas(dNumber);
        nationalIdentifierHateoasHandler
            .addLinks(dNumberHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(dNumber.getVersion().toString())
                .body(dNumberHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/plan/{systemId}
    @ApiOperation(value = "Retrieves a single Plan entity given a systemId",
            response = Plan.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Plan returned",
                    response = Plan.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = PLAN + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PlanHateoas> findOnePlanBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the Plan to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        Plan plan =
                (Plan) nationalIdentifierService.findBySystemId(systemId);
        PlanHateoas planHateoas =
                new PlanHateoas(plan);
        nationalIdentifierHateoasHandler
            .addLinks(planHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(plan.getVersion().toString())
                .body(planHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/posisjon/{systemId}
    @ApiOperation(value = "Retrieves a single Position entity given a systemId",
            response = Position.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Position returned",
                    response = Position.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = POSITION + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PositionHateoas> findOnePositionBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the position to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        Position position =
                (Position) nationalIdentifierService.findBySystemId(systemId);
        PositionHateoas positionHateoas = new PositionHateoas(position);
        nationalIdentifierHateoasHandler
            .addLinks(positionHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(position.getVersion().toString())
                .body(positionHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/foedselsnummer/{systemId}
    @ApiOperation(value = "Retrieves a single SocialSecurityNumber entity given a systemId",
            response = SocialSecurityNumber.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SocialSecurityNumber returned",
                    response = SocialSecurityNumber.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SOCIAL_SECURITY_NUMBER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<SocialSecurityNumberHateoas> findOneSocialSecurityNumberBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the socialSecurityNumber to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        SocialSecurityNumber socialSecurityNumber =
                (SocialSecurityNumber) nationalIdentifierService.findBySystemId(systemId);
        SocialSecurityNumberHateoas socialSecurityNumberHateoas = new SocialSecurityNumberHateoas(socialSecurityNumber);
        nationalIdentifierHateoasHandler
            .addLinks(socialSecurityNumberHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(socialSecurityNumber.getVersion().toString())
                .body(socialSecurityNumberHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/enhetsidentifikator/{systemId}
    @ApiOperation(value = "Retrieves a single Unit entity given a systemId",
            response = Unit.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Unit returned",
                    response = Unit.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = NI_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<UnitHateoas> findOneUnitBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the unit to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        Unit unit =
                (Unit) nationalIdentifierService.findBySystemId(systemId);
        UnitHateoas unitHateoas = new UnitHateoas(unit);
        nationalIdentifierHateoasHandler
            .addLinks(unitHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(unit.getVersion().toString())
                .body(unitHateoas);
    }

    // PUT [contextPath][api]/casehandling/building/{systemId}
    @ApiOperation(value = "Updates a Building identified by a given systemId",
            notes = "Returns the newly updated nationalIdentifierPerson",
            response = BuildingHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Building " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = BuildingHateoas.class),
            @ApiResponse(code = 201, message = "Building " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = BuildingHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Building"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = BUILDING + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<BuildingHateoas> updateBuilding(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of nationalIdentifierPerson to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Building",
                    value = "Incoming nationalIdentifierPerson object",
                    required = true)
            @RequestBody Building building) throws NikitaException {
        validateForUpdate(building); // TODO no-op

        Building updatedBuilding =
            nationalIdentifierService.updateBuilding
            (systemID, parseETAG(request.getHeader(ETAG)), building);
        BuildingHateoas buildingHateoas = new BuildingHateoas(updatedBuilding);
        nationalIdentifierHateoasHandler
            .addLinks(buildingHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedBuilding.getVersion().toString())
                .body(buildingHateoas);
    }

    // PUT [contextPath][api]/casehandling/matrikkel/{systemId}
    @ApiOperation(value = "Updates a CadastralUnit identified by a given systemId",
            notes = "Returns the newly updated nationalIdentifierPerson",
            response = CadastralUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CadastralUnit " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CadastralUnitHateoas.class),
            @ApiResponse(code = 201, message = "CadastralUnit " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CadastralUnitHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type CadastralUnit"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = CADASTRAL_UNIT + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CadastralUnitHateoas> updateCadastralUnit(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of nationalIdentifierPerson to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "CadastralUnit",
                    value = "Incoming nationalIdentifierPerson object",
                    required = true)
            @RequestBody CadastralUnit cadastralUnit) throws NikitaException {
        validateForUpdate(cadastralUnit); // TODO no-op

        CadastralUnit updatedCadastralUnit =
            nationalIdentifierService.updateCadastralUnit
            (systemID, parseETAG(request.getHeader(ETAG)), cadastralUnit);
        CadastralUnitHateoas cadastralUnitHateoas = new CadastralUnitHateoas(updatedCadastralUnit);
        nationalIdentifierHateoasHandler
            .addLinks(cadastralUnitHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedCadastralUnit.getVersion().toString())
                .body(cadastralUnitHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/dnummer/{systemId}
    @ApiOperation(value = "Updates a DNumber identified by a given systemId",
            notes = "Returns the newly updated nationalIdentifierPerson",
            response = DNumberHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DNumber " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = DNumberHateoas.class),
            @ApiResponse(code = 201, message = "DNumber " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = DNumberHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type DNumber"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = D_NUMBER + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<DNumberHateoas> updateDNumber(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of nationalIdentifierPerson to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "DNumber",
                    value = "Incoming nationalIdentifierPerson object",
                    required = true)
            @RequestBody DNumber dNumber) throws NikitaException {
        validateForUpdate(dNumber); // TODO no-op

        DNumber updatedDNumber =
            nationalIdentifierService.updateDNumber
            (systemID, parseETAG(request.getHeader(ETAG)), dNumber);
        DNumberHateoas dNumberHateoas = new DNumberHateoas(updatedDNumber);
        nationalIdentifierHateoasHandler
            .addLinks(dNumberHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedDNumber.getVersion().toString())
                .body(dNumberHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/plan/{systemId}
    @ApiOperation(value = "Updates a Plan identified by a given systemId",
            notes = "Returns the newly updated nationalIdentifierPerson",
            response = PlanHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Plan " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PlanHateoas.class),
            @ApiResponse(code = 201, message = "Plan " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PlanHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Plan"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = PLAN + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PlanHateoas> updatePlan(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of nationalIdentifierPerson to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Plan",
                    value = "Incoming nationalIdentifierPerson object",
                    required = true)
            @RequestBody Plan plan) throws NikitaException {
        validateForUpdate(plan); // TODO no-op

        Plan updatedPlan =
            nationalIdentifierService.updatePlan
            (systemID, parseETAG(request.getHeader(ETAG)), plan);
        PlanHateoas planHateoas = new PlanHateoas(updatedPlan);
        nationalIdentifierHateoasHandler
            .addLinks(planHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedPlan.getVersion().toString())
                .body(planHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/posisjon/{systemId}
    @ApiOperation(value = "Updates a Position identified by a given systemId",
            notes = "Returns the newly updated position",
            response = PositionHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Position " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PositionHateoas.class),
            @ApiResponse(code = 201, message = "Position " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PositionHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Position"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = POSITION + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PositionHateoas> updatePosition(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of position to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Position",
                    value = "Incoming position object",
                    required = true)
            @RequestBody Position position) throws NikitaException {
        validateForUpdate(position); // TODO no-op

        Position updatedPosition = nationalIdentifierService.updatePosition
            (systemID, parseETAG(request.getHeader(ETAG)), position);
        PositionHateoas positionHateoas = new PositionHateoas(updatedPosition);
        nationalIdentifierHateoasHandler
            .addLinks(positionHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedPosition.getVersion().toString())
                .body(positionHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/foedselsnummer/{systemId}
    @ApiOperation(value = "Updates a SocialSecurityNumber identified by a given systemId",
            notes = "Returns the newly updated socialSecurityNumber",
            response = SocialSecurityNumberHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SocialSecurityNumber " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SocialSecurityNumberHateoas.class),
            @ApiResponse(code = 201, message = "SocialSecurityNumber " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = SocialSecurityNumberHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type SocialSecurityNumber"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = SOCIAL_SECURITY_NUMBER + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SocialSecurityNumberHateoas> updateSocialSecurityNumber(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of socialSecurityNumber to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "SocialSecurityNumber",
                    value = "Incoming socialSecurityNumber object",
                    required = true)
            @RequestBody SocialSecurityNumber socialSecurityNumber) throws NikitaException {
        validateForUpdate(socialSecurityNumber); // TODO no-op

        SocialSecurityNumber updatedSocialSecurityNumber = nationalIdentifierService.updateSocialSecurityNumber
            (systemID, parseETAG(request.getHeader(ETAG)), socialSecurityNumber);
        SocialSecurityNumberHateoas socialSecurityNumberHateoas = new SocialSecurityNumberHateoas(updatedSocialSecurityNumber);
        nationalIdentifierHateoasHandler
            .addLinks(socialSecurityNumberHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedSocialSecurityNumber.getVersion().toString())
                .body(socialSecurityNumberHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/enhetsidentifikator/{systemId}
    @ApiOperation(value = "Updates a Unit identified by a given systemId",
            notes = "Returns the newly updated unit",
            response = UnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Unit " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = UnitHateoas.class),
            @ApiResponse(code = 201, message = "Unit " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = UnitHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Unit"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = NI_UNIT + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<UnitHateoas> updateUnit(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of unit to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Unit",
                    value = "Incoming unit object",
                    required = true)
            @RequestBody Unit unit) throws NikitaException {
        validateForUpdate(unit); // TODO no-op

        Unit updatedUnit = nationalIdentifierService.updateUnit
            (systemID, parseETAG(request.getHeader(ETAG)), unit);
        UnitHateoas unitHateoas = new UnitHateoas(updatedUnit);
        nationalIdentifierHateoasHandler
            .addLinks(unitHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedUnit.getVersion().toString())
                .body(unitHateoas);
    }

    // Delete a building identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/bygning/{systemID}/
    @ApiOperation(value = "Deletes a single Building entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Building deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = BUILDING + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteBuilding(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the building to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        nationalIdentifierService.deleteBuilding(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a cadastralUnit identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/matrikkel/{systemID}/
    @ApiOperation(value = "Deletes a single CadastralUnit entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "CadastralUnit deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = CADASTRAL_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteCadastralUnit(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the cadastralUnit to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        nationalIdentifierService.deleteCadastralUnit(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a dnumber identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/dnummer/{systemID}/
    @ApiOperation(value = "Deletes a single DNumber entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "DNumber deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = D_NUMBER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteDNumber(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the dnumber to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        nationalIdentifierService.deleteDNumber(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a plan identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/plan/{systemID}/
    @ApiOperation(value = "Deletes a single Plan entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Plan deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = PLAN + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deletePlan(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the plan to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        nationalIdentifierService.deletePlan(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a position identified by systemid
    // DELETE [contextPath][api]/arkivstruktur/posisjon/{systemID}/
    @ApiOperation(value = "Deletes a single Position entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Position deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = POSITION + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deletePosition(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the position to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        nationalIdentifierService.deletePosition(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a socialSecurityNumber identified by systemid
    // DELETE [contextPath][api]/arkivstruktur/foedselsnummer/{systemID}/
    @ApiOperation(value = "Deletes a single SocialSecurityNumber entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "SocialSecurityNumber deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SOCIAL_SECURITY_NUMBER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteSocialSecurityNumber(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the socialSecurityNumber to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        nationalIdentifierService.deleteSocialSecurityNumber(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete a unit identified by systemid
    // DELETE [contextPath][api]/arkivstruktur/enhetsidentifikator/{systemID}/
    @ApiOperation(value = "Deletes a single Unit entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Unit deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = NI_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteUnit(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the unit to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        nationalIdentifierService.deleteUnit(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
