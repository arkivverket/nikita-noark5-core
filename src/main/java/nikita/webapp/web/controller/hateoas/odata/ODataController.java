package nikita.webapp.web.controller.hateoas.odata;

import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.ODataConstants.*;

/**
 * RestController for OData queries. Rather than having OData handling
 * littered amongst the other RestControllers (for maintainability purposes,
 * a nightmare to update small changes), OData handling is taken care of here
 * in one place. The following OData syntax elements are currently supported
 * $filter, $top, $skip, $orderby
 * <p>
 * This class is really just a landing point and all the magic happens in
 * ODataService and related parsing classes.
 * <p>
 * This endpoint should not be discoverable as an endpoint a client can
 * interact with as it should only accept internal forwards from
 * ODataRedirectFilter doFilter.
 * <p>
 * The basic idea is that the e.g. the following request:
 * <p>
 * http://localhost:8092/noark5v4/api/arkivstruktur/arkiv?%24filter
 * =contains%28tittel%2C%20%27bravo%27%29
 * <p>
 * is converted to:
 * <p>
 * /odata/arkivstruktur/arkiv?%24filter=contains%28tittel%2C%20%27bravo%27%29
 * <p>
 * and the query is resolved here.
 * <p>
 * This class is a trade-off of sorts as making changes to the Controllers
 * across the codebase requires a lot of effort and there is no simple
 * spring-based OData implementation that can be used. The OData parsing
 * needs to mature so currently it makes sense to try an centralise OData
 * handling as far as possible within the repo and perhaps later we can
 * decentralise it across all the controllers.
 */
@RestController
@RequestMapping(value = "/odata")
public class ODataController {

    private final IODataService oDataService;

    public ODataController(IODataService oDataService) {
        this.oDataService = oDataService;
    }

    /**
     * Handles any OData GET request that contains the following as a
     * query parameter $filter, $top, $skip, $orderby
     * <p>
     * $filter, $top, $skip, $orderby er requires here as elective parameters
     * to have an identified landing point for the parameters. That is why there
     * is a @SuppressWarnings("unused") here.
     *
     * @param request The incoming request
     * @param filter  The $filter clause
     * @param top     $top, how many records to return
     * @param skip    $skip, avoid the the x number of records
     * @param orderBy order by this attribute
     * @return the result of the query wrapped as a Hateaos object
     * @throws Exception, parsing, SQL exception
     */
    @SuppressWarnings("unused")
    @GetMapping(value = "**")
    public ResponseEntity<HateoasNoarkObject>
    retrieveOData(HttpServletRequest request,
                  @RequestParam(value = DOLLAR_FILTER, required = false)
                          String filter,
                  @RequestParam(value = DOLLAR_TOP, required = false)
                          String top,
                  @RequestParam(value = DOLLAR_SKIP, required = false)
                          String skip,
                  @RequestParam(value = DOLLAR_ORDER_BY, required = false)
                          String orderBy)
            throws Exception {
        return oDataService.processODataQueryGet(request);
    }

    /**
     * Handles any OData DELETE request that contains the following as a
     * query parameter $filter, $top, $skip, $orderby
     * <p>
     * $filter, $top, $skip, $orderby er requires here as elective parameters
     * to have an identified landing point for the parameters. That is why there
     * is a @SuppressWarnings("unused") here.
     *
     * @param request The incoming request
     * @param filter  The $filter clause
     * @param top     $top, how many records to return
     * @param skip    $skip, avoid the the x number of records
     * @param orderBy order by this attribute
     * @return the result of the query wrapped as a Hateaos object
     * @throws Exception, parsing, SQL exception
     */
    @SuppressWarnings("unused")
    @DeleteMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "/**",
            params = "!" + DOLLAR_ID)
    public ResponseEntity<String>
    deleteViaOData(HttpServletRequest request,
                   @RequestParam(value = DOLLAR_FILTER, required = false)
                           String filter,
                   @RequestParam(value = DOLLAR_TOP, required = false)
                           String top,
                   @RequestParam(value = DOLLAR_SKIP, required = false)
                           String skip,
                   @RequestParam(value = DOLLAR_ORDER_BY, required = false)
                           String orderBy)
            throws Exception {
        return oDataService.processODataQueryDelete(request);
    }
}
