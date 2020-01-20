package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.model.noark5.v5.nationalidentifier.Unit;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
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

    // GET [contextPath][api]/casehandling/building/{systemId}
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
            @ApiParam(name = "systemID",
                    value = "systemID of the Building to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemId) {
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
            @ApiParam(name = "systemID",
                    value = "systemID of the position to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemId) {
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
            @ApiParam(name = "systemID",
                    value = "systemID of the unit to retrieve",
                    required = true)
            @PathVariable("systemID") final String systemId) {
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
            @ApiParam(name = "systemID",
                    value = "systemId of nationalIdentifierPerson to update",
                    required = true)
            @PathVariable("systemID") final String systemID,
            @ApiParam(name = "Building",
                    value = "Incoming nationalIdentifierPerson object",
                    required = true)
            @RequestBody Building building) throws NikitaException {
        validateForUpdate(building); // FIXME no-op

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
            @ApiParam(name = "systemID",
                    value = "systemId of position to update",
                    required = true)
            @PathVariable("systemID") final String systemID,
            @ApiParam(name = "Position",
                    value = "Incoming position object",
                    required = true)
            @RequestBody Position position) throws NikitaException {
        validateForUpdate(position); // FIXME no-op

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
            @ApiParam(name = "systemID",
                    value = "systemId of unit to update",
                    required = true)
            @PathVariable("systemID") final String systemID,
            @ApiParam(name = "Unit",
                    value = "Incoming unit object",
                    required = true)
            @RequestBody Unit unit) throws NikitaException {
        validateForUpdate(unit); // FIXME no-op

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
            @ApiResponse(code = 200, message = "Building deleted"),
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
        return ResponseEntity.status(HttpStatus.OK)
                .body("{\"status\" : \"Success\"}");
    }

    // Delete a position identified by systemid
    // DELETE [contextPath][api]/arkivstruktur/posisjon/{systemID}/
    @ApiOperation(value = "Deletes a single Position entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Position deleted"),
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
        return ResponseEntity.status(HttpStatus.OK)
                .body("{\"status\" : \"Success\"}");
    }

    // Delete a unit identified by systemid
    // DELETE [contextPath][api]/arkivstruktur/enhetsidentifikator/{systemID}/
    @ApiOperation(value = "Deletes a single Unit entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Unit deleted"),
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
        return ResponseEntity.status(HttpStatus.OK)
                .body("{\"status\" : \"Success\"}");
    }
}
