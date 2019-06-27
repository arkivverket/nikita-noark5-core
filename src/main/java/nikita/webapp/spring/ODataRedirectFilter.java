package nikita.webapp.spring;

import nikita.common.util.exceptions.NikitaMisconfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;

import static nikita.common.config.ODataConstants.*;
import static nikita.common.util.CommonUtils.WebUtils.sanitiseUrlForOData;

/**
 * Created by tsodring on 05/13/19.
 * <p>
 * Used to redirect any OData requests (using query parameters) to OData
 * specific controller.
 * <p>
 * This is done to simplify the code at the CRUD controllers. If all controllers
 * have to have this code implemented, there will be a lot of duplicate code
 * and messier to read code logic
 * <p>
 * Note, this filter has to run after the security filter
 */
@Component
@Order
public class ODataRedirectFilter
        implements Filter {

    private final Logger logger =
            LoggerFactory.getLogger(ODataRedirectFilter.class);

    public ODataRedirectFilter() {
        logger.info("ODataRedirectFilter init");
    }

    /**
     * If the request contains OData specific query parameters, forward the
     * request to the OData controller. In this case, we are checking for the
     * presence of the following query parameters $filter, $top, $skip,
     * $orderby, In addition if the URL contains $ref and the query parameter
     * contains $id the request is forwarded to the OData controller.
     *
     * @param req   the incoming request
     * @param res   the response object
     * @param chain the filter chain
     * @throws IOException      network exception
     * @throws ServletException servlet processing exception
     */
    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String urlString = request.getRequestURL().toString();
        Map<String, String[]> map = request.getParameterMap();
        // Check to see that this is a regular OData request that should
        // be forwarded to the OData endpoint
        if ((map.containsKey(DOLLAR_FILTER) ||
                map.containsKey(DOLLAR_TOP) ||
                map.containsKey(DOLLAR_SKIP) ||
                map.containsKey(DOLLAR_ORDER_BY)) ||
                (map.containsKey(DOLLAR_ID) &&
                        urlString.contains(DOLLAR_REF_URL))) {
            RequestDispatcher requestDispatcher = request.
                    getRequestDispatcher(
                            sanitiseUrlForOData(urlString));
            if (requestDispatcher == null) {
                String message = "Unable to forward request [" +
                        request.getRequestURL() + "/" +
                        request.getQueryString() + "] for OData processing";
                logger.error(message);
                throw new NikitaMisconfigurationException(message);
            }
            requestDispatcher.
                    include(new ODataFilteredRequest(request), res);
            return;
        }
        chain.doFilter(req, res);
    }

    /**
     * When handling OData requests, we need to update the request so that the
     * query parameter does not contain '//' as in http://.... as the use of
     * '//' is a potential security issue. It is not possible to update the
     * query parameter of the incoming request as modifying the parameter
     * would not truly represent what the client sent. Therefore
     * HttpServletRequest does not have a setParameter method. A solution as
     * discussed in the following SO post details the approach we use here:
     * <p>
     * https://stackoverflow.com/questions/1413129/modify-request-parameter-with-servlet-filter
     * <p>
     * That is, to use the HttpServletRequestWrapper class, which allows you
     * to wrap one request with another and subsequently subclassing the
     * original request, overriding the getParameter method to return the
     * sanitized value. This subclassed object is passed to the chain.doFilter
     * instead of the original request.
     */
    public static class ODataFilteredRequest
            extends HttpServletRequestWrapper {

        private ODataFilteredRequest(ServletRequest request) {
            super((HttpServletRequest) request);
        }

        @Override
        public String getParameter(String paramName) {
            return sanitiseUrlForOData(super.getParameter(paramName));
        }

        @Override
        public String[] getParameterValues(String paramName) {
            String[] values = super.getParameterValues(paramName);
            for (int index = 0; index < values.length; index++) {
                values[index] = sanitiseUrlForOData(values[index]);
            }
            return values;
        }

        @Override
        public String getQueryString() {
            return sanitiseUrlForOData(super.getQueryString());
        }

        public String getURLSanitised() {
            return sanitiseUrlForOData(super.getRequestURL().toString());
        }
    }
}
