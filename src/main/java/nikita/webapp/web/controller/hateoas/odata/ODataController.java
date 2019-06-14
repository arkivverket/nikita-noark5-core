package nikita.webapp.web.controller.hateoas.odata;

import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v4.hateoas.HateoasNoarkObject;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.SLASH;


@RestController
@RequestMapping(value = "odata")
@SuppressWarnings("unchecked")
public class ODataController {

    private final Logger logger =
            LoggerFactory.getLogger(ODataController.class);

    private final IODataService oDataService;

    public ODataController(IODataService oDataService) {
        this.oDataService = oDataService;
    }

    // Handles the following
    //?$filter= ...
    //?$expand=merknad&$filter= ....
    @GetMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "?filter=**")
    public ResponseEntity<HateoasNoarkObject>
    retrieveOData(HttpServletRequest request)
            throws Exception {
        return oDataService.processODataQueryGet(request);
    }

    // $ref?$id
    ///odata/arkivstruktur/dokumentobjekt/$ref?id=hello
    @GetMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**/$ref?$id**")
    public ResponseEntity<HateoasNoarkObject>
    handleRefGet(HttpServletRequest request)
            throws Exception {
        logger.info("Request has following query string: " + request.getQueryString());
        logger.info("Request has following URL: " + request.getRequestURL());
        return null;
    }

    @PutMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**/$ref?$id**")
    public ResponseEntity<HateoasNoarkObject>
    handleRefPut(HttpServletRequest request)
            throws Exception {
        return null;
    }

    @PostMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**/$ref?$id**")
    public ResponseEntity<HateoasNoarkObject>
    handleRefPost(HttpServletRequest request)
            throws Exception {
        return null;
    }

    @DeleteMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**")
    public ResponseEntity<Count>
    deleteViaOData(HttpServletRequest request)
            throws Exception {
        return oDataService.processODataQueryDelete(request);
    }
}
