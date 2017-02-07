package no.arkivlab.hioa.nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.config.Constants;
import nikita.model.noark5.v4.FondsCreator;
import nikita.model.noark5.v4.hateoas.FondsCreatorHateoas;
import nikita.util.exceptions.NikitaException;
import no.arkivlab.hioa.nikita.webapp.service.interfaces.IFondsCreatorService;
import no.arkivlab.hioa.nikita.webapp.service.interfaces.ISeriesService;
import no.arkivlab.hioa.nikita.webapp.util.exceptions.NoarkEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static nikita.config.Constants.*;
import static nikita.config.N5ResourceMappings.FONDS_CREATOR;
import static nikita.config.N5ResourceMappings.SYSTEM_ID;

@RestController
@RequestMapping(value = Constants.HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH,
        produces = {NOARK5_V4_CONTENT_TYPE})
public class FondsCreatorHateoasController {

    @Autowired
    IFondsCreatorService fondsCreatorService;

    @Autowired
    ISeriesService seriesService;

    // API - All POST Requests (CRUD - CREATE)

    @ApiOperation(value = "Persists a FondsCreator object", notes = "Returns the newly" +
            " created FondsCreator object after it is persisted to the database", response = FondsCreatorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsCreator " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FondsCreatorHateoas.class),
            @ApiResponse(code = 201, message = "FondsCreator " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FondsCreatorHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type FondsCreator"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @Timed
    @RequestMapping(method = RequestMethod.POST, value = NEW_FONDS_CREATOR, consumes = {NOARK5_V4_CONTENT_TYPE})
    public ResponseEntity<FondsCreatorHateoas> createFondsCreator(
            @ApiParam(name = "FondsCreator",
                    value = "Incoming FondsCreator object",
                    required = true)
            @RequestBody FondsCreator FondsCreator) throws NikitaException {
        FondsCreator createdFondsCreator = fondsCreatorService.createNewFondsCreator(FondsCreator);
        FondsCreatorHateoas FondsCreatorHateoas = new FondsCreatorHateoas(createdFondsCreator);
        return new ResponseEntity<>(FondsCreatorHateoas, HttpStatus.CREATED);
    }

    // API - All GET Requests (CRUD - READ)
    @ApiOperation(value = "Retrieves a single FondsCreator entity given a systemId", response = FondsCreator.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsCreator returned", response = FondsCreator.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @Timed
    @RequestMapping(value = FONDS_CREATOR + SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS,
            method = RequestMethod.GET)
    public ResponseEntity<FondsCreatorHateoas> findOne(
            @ApiParam(name = "systemId",
                    value = "systemId of FondsCreator to retrieve.",
                    required = true)
            @PathVariable("systemID") final String FondsCreatorSystemId) {
        FondsCreator FondsCreator = fondsCreatorService.findBySystemId(FondsCreatorSystemId);
        if (FondsCreator == null) {
            throw new NoarkEntityNotFoundException("Could not find FondsCreator object with systemID " + FondsCreatorSystemId);
        }
        FondsCreatorHateoas FondsCreatorHateoas = new FondsCreatorHateoas(FondsCreator);
        return new ResponseEntity<>(FondsCreatorHateoas, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Retrieves multiple FondsCreator entities limited by ownership rights", notes = "The field skip" +
            "tells how many FondsCreator rows of the result set to ignore (starting at 0), while  top tells how many rows" +
            " after skip to return. Note if the value of top is greater than system value " +
            " nikita-noark5-core.pagination.maxPageSize, then nikita-noark5-core.pagination.maxPageSize is used. ",
            response = FondsCreatorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsCreator found",
                    response = FondsCreatorHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @Timed
    @RequestMapping(method = RequestMethod.GET, value = FONDS_CREATOR + SLASH)
    public ResponseEntity<FondsCreatorHateoas> findAllFondsCreator(
            @RequestParam(name = "top", required = false) Integer top,
            @RequestParam(name = "skip", required = false) Integer skip) {
        FondsCreatorHateoas fondsCreatorHateoas = new
                FondsCreatorHateoas(fondsCreatorService.findFondsCreatorByOwnerPaginated(top, skip));
        return new ResponseEntity<>(fondsCreatorHateoas, HttpStatus.OK);
    }
}
