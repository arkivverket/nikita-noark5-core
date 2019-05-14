package nikita.webapp.service.interfaces.odata;

import nikita.common.model.noark5.v4.hateoas.HateoasNoarkObject;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface IODataService {

    ResponseEntity<HateoasNoarkObject>
    processODataQueryGet(HttpServletRequest request)
            throws Exception;

    ResponseEntity<HateoasNoarkObject>
    processODataQueryDelete(HttpServletRequest request)
            throws Exception;
}
