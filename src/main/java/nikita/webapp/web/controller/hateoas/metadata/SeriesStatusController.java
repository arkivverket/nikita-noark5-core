package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.metadata.ISeriesStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_METADATA,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class SeriesStatusController {

    private ISeriesStatusService seriesStatusService;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public SeriesStatusController(
            ISeriesStatusService seriesStatusService,
            IMetadataHateoasHandler metadataHateoasHandler) {
        this.seriesStatusService = seriesStatusService;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new arkivdelstatus
    // POST [contextPath][api]/metadata/arkivdelstatus/ny-arkivdelstatus
    @ApiOperation(value = "Persists a new SeriesStatus object",
            notes = "Returns the newly created SeriesStatus object after it " +
                    "is persisted to the database",
            response = SeriesStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "SeriesStatus " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SeriesStatus.class),
            @ApiResponse(code = 201,
                    message = "SeriesStatus " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = SeriesStatus.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = FONDS_STATUS + SLASH + NEW_FONDS_STATUS)
    public ResponseEntity<MetadataHateoas> createSeriesStatus(
            HttpServletRequest request,
            @RequestBody SeriesStatus seriesStatus)
            throws NikitaException {
        seriesStatusService.createNewSeriesStatus(seriesStatus);
        MetadataHateoas metadataHateoas = new MetadataHateoas(seriesStatus);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(seriesStatus.getVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all seriesStatus
    // GET [contextPath][api]/metadata/arkivdelstatus/
    @ApiOperation(value = "Retrieves all SeriesStatus ",
            response = SeriesStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SeriesStatus codes found",
                    response = SeriesStatus.class),
            @ApiResponse(code = 404, message = "No SeriesStatus found"),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SERIES_STATUS)
    @SuppressWarnings("unchecked")
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        MetadataHateoas metadataHateoas =
                new MetadataHateoas((List<INikitaEntity>)
                        (List) seriesStatusService.findAll(), SERIES_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // Retrieves a given seriesStatus identified by a code
    // GET [contextPath][api]/metadata/arkivdelstatus/{code}/
    @ApiOperation(value = "Gets seriesStatus identified by its code",
            notes = "Returns the requested " +
            " seriesStatus object", response = SeriesStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message =
                    "SeriesStatus " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SeriesStatus.class),
            @ApiResponse(code = 201, message = "SeriesStatus " +
                    API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = SeriesStatus.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value =
            SERIES_STATUS + SLASH + LEFT_PARENTHESIS + CODE +
                    RIGHT_PARENTHESIS + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {
        SeriesStatus seriesStatus =
                seriesStatusService.findByCode(code);
        MetadataHateoas metadataHateoas = new MetadataHateoas(seriesStatus);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(seriesStatus.getVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested seriesStatus(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-arkivdelstatus
    @ApiOperation(value = "Creates a suggested SeriesStatus",
            response = SeriesStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "SeriesStatus codes found",
                    response = SeriesStatus.class),
            @ApiResponse(code = 404,
                    message = "No SeriesStatus found"),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = NEW_SERIES_STATUS)
    public ResponseEntity<MetadataHateoas>
    getSeriesStatusTemplate(HttpServletRequest request) {
        SeriesStatus seriesStatus =
                seriesStatusService.findByCode(TEMPLATE_SERIES_STATUS_CODE);
        seriesStatus.setCode(TEMPLATE_SERIES_STATUS_CODE);
        MetadataHateoas metadataHateoas = new MetadataHateoas(seriesStatus);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a arkivdelstatus
    // PUT [contextPath][api]/metatdata/arkivdelstatus/
    @ApiOperation(value = "Updates a SeriesStatus object",
            notes = "Returns the newly updated SeriesStatus object after it " +
                    "is persisted to the database",
            response = SeriesStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SeriesStatus " +
                    API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = SeriesStatus.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value =
            SERIES_STATUS + SLASH + LEFT_PARENTHESIS + CODE +
                    RIGHT_PARENTHESIS + SLASH)
    public ResponseEntity<MetadataHateoas>
    updateSeriesStatus(@RequestBody SeriesStatus seriesStatus,
                       HttpServletRequest request)
            throws NikitaException {
        seriesStatusService.update(seriesStatus);
        MetadataHateoas metadataHateoas = new MetadataHateoas(seriesStatus);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
