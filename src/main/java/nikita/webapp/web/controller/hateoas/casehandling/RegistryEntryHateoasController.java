package nikita.webapp.web.controller.hateoas.casehandling;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.webapp.hateoas.interfaces.IRegistryEntryHateoasHandler;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_INTERNAL;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)

public class RegistryEntryHateoasController extends NoarkController {

    private final IRegistryEntryService registryEntryService;
    private final IRegistryEntryHateoasHandler registryEntryHateoasHandler;
    private final ICorrespondencePartService correspondencePartService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RegistryEntryHateoasController(
            IRegistryEntryService registryEntryService,
            IRegistryEntryHateoasHandler registryEntryHateoasHandler,
            ICorrespondencePartService correspondencePartService,
            ApplicationEventPublisher applicationEventPublisher) {
        this.registryEntryService = registryEntryService;
        this.registryEntryHateoasHandler = registryEntryHateoasHandler;
        this.correspondencePartService = correspondencePartService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Retrieve all CorrespondencePartInternal associated with a RegistryEntry identified by systemId
    // GET [contextPath][api]/sakarkiv/journalpost/{systemId}/korrespondansepartintern
    // https://rel.arkivverket.no/noark5/v5/api/sakarkiv/korrespondansepartintenrn/
    @ApiOperation(value = "Retrieves a list of CorrespondencePartInternals associated with a RegistryEntry",
            response = CorrespondencePartInternalHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "CorrespondencePartInternal returned",
                    response = CorrespondencePartInternalHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS + SLASH +
            CORRESPONDENCE_PART_INTERNAL, method = GET)
    public ResponseEntity<CorrespondencePartInternalHateoas>
    findAllCorrespondencePartPersonlAssociatedWithRecord(
            @ApiParam(name = "systemID",
                    value = "systemID of the file to retrieve associated Record",
                    required = true)
            @PathVariable("systemID") final String systemID) {
/*
TODO: Temp disabled!
        List<CorrespondencePartInternal> correspondencePartInternal =
                registryEntryService.getCorrespondencePartInternalAssociatedWithRegistryEntry(systemID);
        CorrespondencePartInternalHateoas correspondencePartHateoas =
                new CorrespondencePartInternalHateoas((List<INikitaEntity>) (List) correspondencePartInternal);
        correspondencePartHateoasHandler.addLinksOnTemplate(correspondencePartHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(correspondencePartHateoas);
   */
        return null;
    }
}
