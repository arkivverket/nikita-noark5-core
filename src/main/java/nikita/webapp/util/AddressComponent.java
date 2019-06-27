package nikita.webapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.SLASH;

/**
 * Created by tsodring
 * <p>
 * Simple Component object that pulls the address to use on the outgoing
 * hateoas links from the incoming request. Created because we reuse this
 * code a number of places in the codebase.
 */
@Component
public class AddressComponent {

    @Value("${nikita.server.hateoas.publicAddress}")
    private String publicAddress;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * Get the outgoing address to use when generating links.
     * If we are not running behind a front facing server incoming requests
     * will not have X-Forward-* values set. In this case use the hardcoded
     * value from the properties file.
     * <p>
     * If X-Forward-*  values are set, then use them. At a minimum Host and
     * Proto must be set. If Port is also set use this to.
     *
     * @return the outgoing address
     */
    protected String getOutgoingAddress() {
        return getAddress() + contextPath + SLASH;
    }

    public String getAddress() {

        HttpServletRequest request = getRequest();
        String address = request.getHeader("X-Forwarded-Host");
        String protocol = request.getHeader("X-Forwarded-Proto");
        String port = request.getHeader("X-Forwarded-Port");

        if (address != null && protocol != null) {
            if (port != null) {
                return protocol + "://" + address + ":" + port;
            } else {
                return protocol + "://" + address;
            }
        } else {
            return publicAddress;
        }
    }

    public String getContextPath() {
        return contextPath;
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes())
                .getRequest();
    }
}
