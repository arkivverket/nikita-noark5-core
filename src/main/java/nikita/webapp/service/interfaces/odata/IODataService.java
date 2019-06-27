package nikita.webapp.service.interfaces.odata;

import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v4.hateoas.HateoasNoarkObject;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface IODataService {

    ResponseEntity<HateoasNoarkObject>
    processODataQueryGet(HttpServletRequest request)
            throws Exception;

    ResponseEntity<Count>
    processODataQueryDelete(HttpServletRequest request)
            throws Exception;

    ResponseEntity<HateoasNoarkObject>
    processODataRefRequestUpdate(HttpServletRequest request)
            throws UnsupportedEncodingException;

    ResponseEntity<HateoasNoarkObject>
    processODataRefRequestCreate(HttpServletRequest request)
            throws UnsupportedEncodingException;

    ResponseEntity<HateoasNoarkObject>
    processODataRefRequestDelete(HttpServletRequest request)
            throws UnsupportedEncodingException;

}
