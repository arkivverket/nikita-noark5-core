package nikita.webapp.spring;

import nikita.common.util.exceptions.NikitaMisconfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
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
                map.containsKey(DOLLAR_ORDER_BY) ||
                map.containsKey(DOLLAR_COUNT))) {
            String urlVal = sanitiseUrlForOData(urlString);
            RequestDispatcher requestDispatcher = request.
                    getRequestDispatcher(urlVal);
            if (requestDispatcher == null) {
                String message = "Unable to forward request [" +
                        request.getRequestURL() + "/" +
                        request.getQueryString() + "] for OData processing";
                logger.error(message);
                throw new NikitaMisconfigurationException(message);
            }
            requestDispatcher.include(request, res);
            return;
        }
        chain.doFilter(req, res);
    }
}
