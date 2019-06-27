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
import static nikita.common.config.ODataConstants.*;

/**
 * Created by tsodring
 * <p>
 * RestController for OData queries. Rather than having OData handling
 * littered amongst the other RestControllers (for maintainability purposes,
 * a nightmare to update small changes), OData handling is taken care of here
 * in one place. The following OData syntax elements are currently supported
 * $filter, $top, $skip, $orderby and $ref.
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
 * http://localhost:8092/noark5v4/hateoas-api/arkivstruktur/arkiv?%24filter
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
 * <p>
 * Note: When it comes to the use of $ref, it is still a little unclear how
 * it is intended to be used in the standard. So this portion of the code is
 * very immature.
 */
@RestController
@RequestMapping(value = "odata")
public class ODataController {

    private final Logger logger =
            LoggerFactory.getLogger(ODataController.class);

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
    @GetMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**",
            params = "!" + DOLLAR_ID)
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
     * The endpoint handles requests like the following:
     * <p>
     * odata/arkivstruktur/mappe/a71a3188-48ea-442d-a27a-1f5649bf87ea/ny
     * -kryssreferanse/%24ref?24id%3D%2Farkivstruktur%2Fbasisregistrering%2F245ff5c7-c74b-4e92-89f5-78ab0ed6b50d
     * <p>
     * Note: In nikita $id should be a http address. spring causes security
     * exceptions if '//' is part of a URL request as a query parameter as it
     * sees the '//' as a potential security issue. This can be overcome by
     * tinkering with StrictHttpFirewall, but we'd rather not open nikita for
     * potential exploits so we leave StrictHttpFirewall as it is. Rather the
     * ODataRedirectFilter.doFilter does the same conversion to the URL as
     * part of $id, avoiding the whole issue.
     * <p>
     * Note: It is currently a little unclear in the Noark API standard, how
     * $ref should be used, so this code is likely to change as it becomes
     * clearer.
     * <p>
     * Note: $ref itself is not a query parameter, but part of the request URL
     * as it comes before the ?$id part. This can be a little confusing at times
     * but once aware of it, it is OK. It has the following syntax: ../$ref?$id
     *
     * @param request The incoming request
     * @param id      A link to the object you want to do something with
     * @return The object if applicable
     * @throws Exception, parsing or SQL exceptions
     */
    @GetMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**")
    public ResponseEntity<HateoasNoarkObject>
    handleRefGet(HttpServletRequest request,
                 @RequestParam(value = DOLLAR_ID) String id)
            throws Exception {
        logger.info("Request has following id query parameter: " + id);
        logger.info("Request has following query string: " + request.getQueryString());
        logger.info("Request has following URL: " + request.getRequestURL());
        return null;
    }

    /**
     * Update the references between to entities
     * <p>
     * The endpoint handles requests like the following:
     * <p>
     * odata/arkivstruktur/mappe/a71a3188-48ea-442d-a27a-1f5649bf87ea
     * /kryssreferanse/%24ref?24id%3D%2Farkivstruktur%2Fbasisregistrering
     * %2F245ff5c7-c74b-4e92-89f5-78ab0ed6b50d
     * <p>
     * Note: In nikita $id should be a http address. spring causes security
     * exceptions if '//' is part of a URL request as a query parameter as it
     * sees the '//' as a potential security issue. This can be overcome by
     * tinkering with StrictHttpFirewall, but we'd rather not open nikita for
     * potential exploits so we leave StrictHttpFirewall as it is. Rather the
     * ODataRedirectFilter.doFilter does the same conversion to the URL as
     * part of $id, avoiding the whole issue.
     * <p>
     * Note: It is currently a little unclear in the Noark API standard, how
     * $ref should be used, so this code is likely to change as it becomes
     * clearer.
     * <p>
     * Note: $ref itself is not a query parameter, but part of the request URL
     * as it comes before the ?$id part. This can be a little confusing at times
     * but once aware of it, it is OK. It has the following syntax: ../$ref?$id
     *
     * @param request The incoming request
     * @param id      A link to the object you want to do something update
     * @return The object if applicable
     * @throws Exception, parsing or SQL exceptions
     */
    @PutMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**")
    public ResponseEntity<HateoasNoarkObject>
    handleRefPut(HttpServletRequest request,
                 @RequestParam(value = DOLLAR_ID) String id)
            throws Exception {
        logger.info("Request has following id query parameter: " + id);
        logger.info("Request has following query string: " + request.getQueryString());
        logger.info("Request has following URL: " + request.getRequestURL());
        return oDataService.processODataRefRequestUpdate(request);
    }

    /**
     * Create a reference between to entities
     * <p>
     * The endpoint handles requests like the following:
     * <p>
     * odata/arkivstruktur/mappe/a71a3188-48ea-442d-a27a-1f5649bf87ea
     * /kryssreferanse/%24ref?24id%3D%2Farkivstruktur%2Fbasisregistrering
     * %2F245ff5c7-c74b-4e92-89f5-78ab0ed6b50d
     * <p>
     * Note: In nikita $id should be a http address. spring causes security
     * exceptions if '//' is part of a URL request as a query parameter as it
     * sees the '//' as a potential security issue. This can be overcome by
     * tinkering with StrictHttpFirewall, but we'd rather not open nikita for
     * potential exploits so we leave StrictHttpFirewall as it is. Rather the
     * ODataRedirectFilter.doFilter does the same conversion to the URL as
     * part of $id, avoiding the whole issue.
     * <p>
     * Note: It is currently a little unclear in the Noark API standard, how
     * $ref should be used, so this code is likely to change as it becomes
     * clearer.
     * <p>
     * Note: $ref itself is not a query parameter, but part of the request URL
     * as it comes before the ?$id part. This can be a little confusing at times
     * but once aware of it, it is OK. It has the following syntax: ../$ref?$id
     *
     * @param request The incoming request
     * @param id      A link to the object you want to do something with
     * @return The object if applicable
     * @throws Exception, parsing or SQL exceptions
     */
    @PostMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**")
    public ResponseEntity<HateoasNoarkObject>
    handleRefPost(HttpServletRequest request,
                  @RequestParam(value = DOLLAR_ID, required = false) String id)
            throws Exception {
        logger.info("Request has following id query parameter: " + id);
        logger.info("Request has following query string: " + request.getQueryString());
        logger.info("Request has following URL: " + request.getRequestURL());
        return oDataService.processODataRefRequestCreate(request);
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
    public ResponseEntity<Count>
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

    /**
     * Update the references between to entities
     * <p>
     * The endpoint handles requests like the following:
     * <p>
     * odata/arkivstruktur/mappe/a71a3188-48ea-442d-a27a-1f5649bf87ea
     * /kryssreferanse/%24ref?24id%3D%2Farkivstruktur%2Fbasisregistrering
     * %2F245ff5c7-c74b-4e92-89f5-78ab0ed6b50d
     * <p>
     * Note: In nikita $id should be a http address. spring causes security
     * exceptions if '//' is part of a URL request as a query parameter as it
     * sees the '//' as a potential security issue. This can be overcome by
     * tinkering with StrictHttpFirewall, but we'd rather not open nikita for
     * potential exploits so we leave StrictHttpFirewall as it is. Rather the
     * ODataRedirectFilter.doFilter does the same conversion to the URL as
     * part of $id, avoiding the whole issue.
     * <p>
     * Note: It is currently a little unclear in the Noark API standard, how
     * $ref should be used, so this code is likely to change as it becomes
     * clearer.
     * <p>
     * Note: $ref itself is not a query parameter, but part of the request URL
     * as it comes before the ?$id part. This can be a little confusing at times
     * but once aware of it, it is OK. It has the following syntax: ../$ref?$id
     *
     * @param request The incoming request
     * @param id      A link to the object you want to do something update
     * @return The object if applicable
     * @throws Exception, parsing or SQL exceptions
     */
    @DeleteMapping(value = NOARK_FONDS_STRUCTURE_PATH + SLASH + "**")
    public ResponseEntity<HateoasNoarkObject>
    handleRefDelet(HttpServletRequest request,
                   @RequestParam(value = DOLLAR_ID) String id)
            throws Exception {
        logger.info("Request has following id query parameter: " + id);
        logger.info("Request has following query string: " + request.getQueryString());
        logger.info("Request has following URL: " + request.getRequestURL());
        return oDataService.processODataRefRequestDelete(request);
    }
}
