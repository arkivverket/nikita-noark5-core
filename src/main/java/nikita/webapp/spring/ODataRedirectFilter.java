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

        if (null != request.getQueryString() &&
                (request.getQueryString().contains("filter")
                        || request.getQueryString().contains("skip")
                        || request.getQueryString().contains("top"))) {
            String path = ODATA_PATH +
                    getEntity(request.getRequestURL());
            RequestDispatcher requestDispatcher = request.
                    getRequestDispatcher(path);

            if (requestDispatcher == null) {
                throw new NikitaMisconfigurationException(
                        "Unable to redirect request [" +
                        request.getRequestURL() + "/" +
                                request.getQueryString()+ "] for OData " +
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
     * @param url
     * @return
     */
    private String getEntity(StringBuffer url) {
        int entityStart = url.lastIndexOf(
                NOARK_FONDS_STRUCTURE_PATH + SLASH);
        return url.substring(entityStart + ODATA_OFFSET_LENGTH);
    }
}
