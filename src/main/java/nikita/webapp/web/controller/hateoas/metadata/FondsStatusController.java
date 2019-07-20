package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.metadata.IFondsStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CODE;
import static nikita.common.config.N5ResourceMappings.FONDS_STATUS;

@RestController
@RequestMapping(value = Constants.HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH,
        produces = {NOARK5_V5_CONTENT_TYPE_JSON, NOARK5_V5_CONTENT_TYPE_JSON_XML})
public class FondsStatusController {

    private IFondsStatusService fondsStatusService;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public FondsStatusController(IFondsStatusService fondsStatusService,
                                 IMetadataHateoasHandler metadataHateoasHandler) {
        this.fondsStatusService = fondsStatusService;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new arkivstatus
    // POST [contextPath][api]/metadata/arkivstatus/ny-arkivstatus
    @ApiOperation(value = "Persists a new FondsStatus object", notes = "Returns the newly" +
            " created FondsStatus object after it is persisted to the database", response = FondsStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsStatus " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FondsStatus.class),
            @ApiResponse(code = 201, message = "FondsStatus " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FondsStatus.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted

    @RequestMapping(method = RequestMethod.POST, value = FONDS_STATUS + SLASH + NEW_FONDS_STATUS)
    public ResponseEntity<MetadataHateoas> createFondsStatus(
            HttpServletRequest request,
            @RequestBody FondsStatus fondsStatus)
            throws NikitaException {
        fondsStatusService.createNewFondsStatus(fondsStatus);
        MetadataHateoas metadataHateoas = new MetadataHateoas(fondsStatus);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fondsStatus.getVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all fondsStatus
    // GET [contextPath][api]/metadata/arkivstatus/
    @ApiOperation(value = "Retrieves all FondsStatus ", response = FondsStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsStatus codes found",
                    response = FondsStatus.class),
            @ApiResponse(code = 404, message = "No FondsStatus found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = RequestMethod.GET, value = FONDS_STATUS)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                fondsStatusService.findAll(),
                FONDS_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // Retrieves a given fondsStatus identified by a code
    // GET [contextPath][api]/metadata/arkivstatus/{code}/
    @ApiOperation(value = "Gets fondsStatus identified by its code", notes = "Returns the requested " +
            " fondsStatus object", response = FondsStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsStatus " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FondsStatus.class),
            @ApiResponse(code = 201, message = "FondsStatus " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FondsStatus.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted

    @RequestMapping(value = FONDS_STATUS + SLASH + LEFT_PARENTHESIS + CODE + RIGHT_PARENTHESIS + SLASH, method = RequestMethod.GET)
    public ResponseEntity<MetadataHateoas> findByCode(@PathVariable("kode") final String code,
                                                      HttpServletRequest request) {
        FondsStatus fondsStatus = fondsStatusService.findByCode(code);
        MetadataHateoas metadataHateoas = new MetadataHateoas(fondsStatus);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fondsStatus.getVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested fondsStatus(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-arkivstatus
    @ApiOperation(value = "Creates a suggested FondsStatus", response = FondsStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsStatus codes found",
                    response = FondsStatus.class),
            @ApiResponse(code = 404, message = "No FondsStatus found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = RequestMethod.GET, value = NEW_FONDS_STATUS)
    public ResponseEntity<MetadataHateoas> getFondsStatusTemplate(HttpServletRequest request) {
        FondsStatus fondsStatus = new FondsStatus();
        fondsStatus.setCode(TEMPLATE_FONDS_STATUS_CODE);
        fondsStatus.setCodeName(TEMPLATE_FONDS_STATUS_NAME);
        MetadataHateoas metadataHateoas = new MetadataHateoas(fondsStatus);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a arkivstatus
    // PUT [contextPath][api]/metatdata/arkivstatus/
    @ApiOperation(value = "Updates a FondsStatus object", notes = "Returns the newly" +
            " updated FondsStatus object after it is persisted to the database", response = FondsStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsStatus " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FondsStatus.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = RequestMethod.PUT, value = FONDS_STATUS + SLASH + FONDS_STATUS)
    public ResponseEntity<MetadataHateoas> updateFondsStatus(@RequestBody FondsStatus fondsStatus,
                                                             HttpServletRequest request)
            throws NikitaException {
        fondsStatusService.update(fondsStatus);
        MetadataHateoas metadataHateoas = new MetadataHateoas(fondsStatus);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
