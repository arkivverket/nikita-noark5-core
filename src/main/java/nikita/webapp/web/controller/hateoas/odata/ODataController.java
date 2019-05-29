package nikita.webapp.web.controller.hateoas.odata;

import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**")
    public ResponseEntity<HateoasNoarkObject>
    retrieveOData(HttpServletRequest request)
            throws Exception {
        return oDataService.processODataQueryGet(request);
    }

    @DeleteMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**")
    public ResponseEntity<Count>
    deleteViaOData(HttpServletRequest request)
            throws Exception {
        return oDataService.processODataQueryDelete(request);
    }
}
