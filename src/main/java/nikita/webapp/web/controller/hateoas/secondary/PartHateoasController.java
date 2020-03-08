package nikita.webapp.web.controller.hateoas.secondary;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.secondary.IPartHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.secondary.IPartService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class PartHateoasController
        extends NoarkController {

    private IPartHateoasHandler partHateoasHandler;
    private IPartService partService;

    public PartHateoasController(IPartHateoasHandler partHateoasHandler,
                                 IPartService partService) {
        this.partHateoasHandler = partHateoasHandler;
        this.partService = partService;
    }

    // API - All GET Requests (CRUD - READ)

    // GET [contextPath][api]/arkivstruktur/partperson/{systemId}
    @ApiOperation(value = "Retrieves a single PartPerson entity given a systemId",
            response = PartPerson.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PartPerson returned",
                    response = PartPerson.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = PART_PERSON + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PartPersonHateoas> findOnePartPersonBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the partPerson to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String partPersonSystemId) {
        PartPerson partPerson =
                (PartPerson) partService.findBySystemId(partPersonSystemId);
        PartPersonHateoas partPersonHateoas =
                new PartPersonHateoas(partPerson);
        partHateoasHandler.addLinks(partPersonHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partPerson.getVersion().toString())
                .body(partPersonHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/partenhet/{systemId}
    @ApiOperation(value = "Retrieves a single PartUnit entity given a systemId",
            response = PartUnit.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PartUnit returned",
                    response = PartUnit.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = PART_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<PartUnitHateoas> findOnePartUnitBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the partUnit to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String partUnitSystemId) {
        PartUnit partUnit =
                (PartUnit) partService.findBySystemId(partUnitSystemId);
        PartUnitHateoas partUnitHateoas = new PartUnitHateoas(partUnit);
        partHateoasHandler.addLinks(partUnitHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(partUnit.getVersion().toString())
                .body(partUnitHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/partenhet/{systemId}
    @ApiOperation(value = "Updates a PartUnit identified by a given systemId",
            notes = "Returns the newly updated partUnit",
            response = PartUnitHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PartUnit " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 201, message = "PartUnit " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartUnitHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type PartUnit"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = PART_UNIT + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartUnitHateoas> updatePartUnit(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of partUnit to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "PartUnit",
                    value = "Incoming partUnit object",
                    required = true)
            @RequestBody PartUnit partUnit) throws NikitaException {
        validateForUpdate(partUnit);

        PartUnit updatedPartUnit =
                partService.updatePartUnit(systemID,
                                           parseETAG(request.getHeader(ETAG)),
                                           partUnit);
        PartUnitHateoas partUnitHateoas = new
                PartUnitHateoas(updatedPartUnit);
        partHateoasHandler.addLinks(partUnitHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedPartUnit.getVersion().toString())
                .body(partUnitHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/partperson/{systemId}
    @ApiOperation(value = "Updates a PartPerson identified by a given systemId",
            notes = "Returns the newly updated partPerson",
            response = PartPersonHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PartPerson " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 201, message = "PartPerson " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartPersonHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type PartPerson"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = PART_PERSON + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<PartPersonHateoas> updatePartPerson(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of partPerson to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "PartPerson",
                    value = "Incoming partPerson object",
                    required = true)
            @RequestBody PartPerson partPerson) throws NikitaException {
        validateForUpdate(partPerson);

        PartPerson updatedPartPerson =
                partService.updatePartPerson(systemID,
                        parseETAG(request.getHeader(ETAG)), partPerson);
        PartPersonHateoas partPersonHateoas =
                new PartPersonHateoas(updatedPartPerson);
        partHateoasHandler.addLinks(partPersonHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedPartPerson.getVersion().toString())
                .body(partPersonHateoas);
    }

    // DELETE [contextPath][api]/arkivstruktur/partenhet/{systemID}/
    @ApiOperation(value = "Deletes a single PartUnit entity identified by " +
            SYSTEM_ID)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "PartUnit deleted"),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = PART_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deletePartUnit(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the partUnit to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        partService.deletePartUnit(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // DELETE [contextPath][api]/arkivstruktur/partperson/{systemID}/
    @ApiOperation(value = "Deletes a single PartPerson entity identified by " +
            SYSTEM_ID)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "PartPerson deleted"),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = PART_PERSON + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deletePartPerson(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the partPerson to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        partService.deletePartPerson(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
