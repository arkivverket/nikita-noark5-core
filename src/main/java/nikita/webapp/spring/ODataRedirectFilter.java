package nikita.webapp.spring;

import nikita.common.util.exceptions.NikitaMisconfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static nikita.common.config.Constants.*;


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

    private final Logger log =
            LoggerFactory.getLogger(ODataRedirectFilter.class);

    public ODataRedirectFilter() {
        log.info("ODataRedirectFilter init");
    }

    /**
     * If the request contains a OData specific
     *
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Map<String, String[]> map = request.getParameterMap();

        //then you just access the reversedMap however you like...
        for (Map.Entry entry : map.entrySet()) {
            log.info(entry.getKey() + ", " + entry.getValue());
        }

        String queryString = request.getQueryString();
        String urlString = request.getRequestURL().toString();

        if (null != request.getQueryString() &&
                (urlString.contains("ref") && queryString.contains("id="))) {
            // DO this with a regex??
            //ref%3F%24id
            String path = ODATA_PATH +
                    getEntity(request.getRequestURL()) + "%24ref%3F%24" +
                    queryString.substring(3);
            RequestDispatcher requestDispatcher = request.
                    getRequestDispatcher(path);

            if (requestDispatcher == null) {
                throw new NikitaMisconfigurationException(
                        "Unable to redirect request [" +
                                request.getRequestURL() + "/" +
                                queryString + "] for OData " +
                                "processing");
            }
            requestDispatcher.include(request, response);
            return;
        } else if (null != request.getQueryString() &&
                (queryString.contains("filter")
                        || queryString.contains("skip")
                        || queryString.contains("top"))) {
            String path = ODATA_PATH +
                    getEntity(request.getRequestURL());
            RequestDispatcher requestDispatcher = request.
                    getRequestDispatcher(path);

            if (requestDispatcher == null) {
                throw new NikitaMisconfigurationException(
                        "Unable to redirect request [" +
                                request.getRequestURL() + "/" +
                                queryString + "] for OData " +
                                "processing");
            }
            requestDispatcher.include(request, response);
            return;
        }
        chain.doFilter(req, res);
    }


    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    /**
     * Code should be able to adapt to any changes in the host / context path.
     *
     * Has to handle straight forward $filter but also a /$ref?$id
     * @param url
     * @return
     */
    private String getEntity(StringBuffer url) {
        int entityStart = url.lastIndexOf(
                NOARK_FONDS_STRUCTURE_PATH + SLASH);
        int entityEnd = url.lastIndexOf(SLASH);

        if (entityStart != entityEnd) {
// +1 because you want the slash
            return url.substring(entityStart + ODATA_OFFSET_LENGTH, entityEnd + 1);
        } else
            return url.substring(entityStart + ODATA_OFFSET_LENGTH);
    }
}
