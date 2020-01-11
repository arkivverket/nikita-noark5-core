package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.metadata.ICorrespondencePartTypeService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class CorrespondencePartTypeController extends NoarkController {

    private ICorrespondencePartTypeService correspondencePartTypeService;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public CorrespondencePartTypeController(ICorrespondencePartTypeService correspondencePartTypeService,
                                            IMetadataHateoasHandler metadataHateoasHandler) {
        this.correspondencePartTypeService = correspondencePartTypeService;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new korrespondanseparttype
    // POST [contextPath][api]/metadata/ny-korrespondanseparttype
    @ApiOperation(value = "Persists a new CorrespondencePartType object", notes = "Returns the newly" +
            " created CorrespondencePartType object after it is persisted to the database", response = CorrespondencePartType.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartType " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CorrespondencePartType.class),
            @ApiResponse(code = 201, message = "CorrespondencePartType " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CorrespondencePartType.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted
    @PostMapping(value = NEW_CORRESPONDENCE_PART_TYPE)
    public ResponseEntity<MetadataHateoas> createCorrespondencePartType(
            HttpServletRequest request,
            @RequestBody CorrespondencePartType correspondencePartType)
            throws NikitaException {
        correspondencePartTypeService.createNewCorrespondencePartType(correspondencePartType);
        MetadataHateoas metadataHateoas = new MetadataHateoas(correspondencePartType);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(correspondencePartType.getVersion().toString())
                .body(metadataHateoas);
    }
    // API - All GET Requests (CRUD - READ)
    // Retrieves all correspondencePartType
    // GET [contextPath][api]/metadata/korrespondanseparttype/
    @ApiOperation(value = "Retrieves all CorrespondencePartType ", response = CorrespondencePartType.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartType codes found",
                    response = CorrespondencePartType.class),
            @ApiResponse(code = 404, message = "No CorrespondencePartType found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = CORRESPONDENCE_PART_TYPE)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INoarkEntity>)
                        (List) correspondencePartTypeService.findAll(),
                CORRESPONDENCE_PART_TYPE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // Retrieves a given correspondencePartType identified by a code
    // GET [contextPath][api]/metadata/korrespondanseparttype/{code}
    @ApiOperation(value = "Gets correspondencePartType identified by its code", notes = "Returns the requested " +
            " correspondencePartType object", response = CorrespondencePartType.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartType " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CorrespondencePartType.class),
            @ApiResponse(code = 201, message = "CorrespondencePartType " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CorrespondencePartType.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted

    @GetMapping(value = CORRESPONDENCE_PART_TYPE + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> findByCode(@PathVariable("kode") final String code,
                                                      HttpServletRequest request) {
        CorrespondencePartType correspondencePartType = correspondencePartTypeService.findByCode(code);
        MetadataHateoas metadataHateoas = new MetadataHateoas(correspondencePartType);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(correspondencePartType.getVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested correspondencePartType(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-korrespondanseparttype
    @ApiOperation(value = "Creates a suggested CorrespondencePartType", response = CorrespondencePartType.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartType codes found",
                    response = CorrespondencePartType.class),
            @ApiResponse(code = 404, message = "No CorrespondencePartType found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = NEW_CORRESPONDENCE_PART_TYPE)
    public ResponseEntity<MetadataHateoas> getCorrespondencePartTypeTemplate(HttpServletRequest request) {
        CorrespondencePartType correspondencePartType = new CorrespondencePartType();
        correspondencePartType.setCode(TEMPLATE_FONDS_STATUS_CODE);
        correspondencePartType.setCodeName(TEMPLATE_FONDS_STATUS_NAME);
        MetadataHateoas metadataHateoas = new MetadataHateoas(correspondencePartType);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a korrespondanseparttype
    // PUT [contextPath][api]/metatdata/korrespondanseparttype/
    @ApiOperation(value = "Updates a CorrespondencePartType object", notes = "Returns the newly" +
            " updated CorrespondencePartType object after it is persisted to the database", response = CorrespondencePartType.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartType " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CorrespondencePartType.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = CORRESPONDENCE_PART_TYPE + UNIT + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> updateCorrespondencePartTypeUnit(
            @RequestBody CorrespondencePartType correspondencePartType,
            HttpServletRequest request)
            throws NikitaException {
        CorrespondencePartType updatedCorrespondencePartType = correspondencePartTypeService.update(correspondencePartType);
        MetadataHateoas metadataHateoas = new MetadataHateoas(updatedCorrespondencePartType);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(correspondencePartType.getVersion().toString())
                .body(metadataHateoas);
    }

    // Delete a correspondencePartType identified by systemID
    // DELETE [contextPath][api]/sakarkiv/korrespondanseparttype/{kode}/
    @ApiOperation(value = "Deletes a single CorrespondencePartType entity identified by kode")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartType deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @DeleteMapping(value = SLASH + CODE_PARAMETER)
    public ResponseEntity<String> deletecorrespondencePartTypeByCode(
            @ApiParam(name = "kode",
                    value = "kode of the correspondencePartType to delete",
                    required = true)
            @PathVariable("kode") final String kode) {
        correspondencePartTypeService.deleteEntity(kode);
        return ResponseEntity.status(HttpStatus.OK)
                .body("{\"status\" : \"Success\"}");
    }
}
