package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;
import nikita.webapp.security.IAuthorisation;
import nikita.webapp.util.AddressComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.HATEOASConstants.SELF;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add Hateoas links with information
 */
@Component
public class HateoasHandler
        implements IHateoasHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(HateoasHandler.class);

    protected IAuthorisation authorisation;
    private AddressComponent addressComponent;

    @Value("${nikita.server.hateoas.publicAddress}")
    protected String publicAddress;

    @Value("${server.servlet.context-path}")
    protected String contextPath;

    public HateoasHandler() {
    }

    @Override
    public void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                         IAuthorisation authorisation) {
    }

    @Override
    public void addLinksOnCreate(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation) {
        addLinks(hateoasNoarkObject, authorisation);
    }

    @Override
    public void addLinksOnTemplate(IHateoasNoarkObject hateoasNoarkObject,
                                   IAuthorisation authorisation) {
    }

    @Override
    public void addLinksOnRead(IHateoasNoarkObject hateoasNoarkObject,
                               IAuthorisation authorisation) {
        addLinks(hateoasNoarkObject, authorisation);
    }

    @Override
    public void addLinksOnUpdate(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation) {
        addLinks(hateoasNoarkObject, authorisation);
    }

    @Override
    public void addLinksOnDelete(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation) {
        addLinks(hateoasNoarkObject, authorisation);
    }

    protected String getRelSelfLink() {
        return SELF;
    }

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
        RequestAttributes requestAttributes = RequestContextHolder.
                getRequestAttributes();

        if (requestAttributes != null) {
            HttpServletRequest request =
                    ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request != null) {
                String address = request.getHeader("X-Forwarded-Host");
                String protocol = request.getHeader("X-Forwarded-Proto");
                String port = request.getHeader("X-Forwarded-Port");

                if (address != null && protocol != null) {
                    if (port != null) {
                        return protocol + "://" + address + ":" + port +
                                contextPath + SLASH;
                    } else {
                        return protocol + "://" + address + contextPath + SLASH;
                    }
                }
            }
        } else {
            logger.warn("Request is null. This likely means we are serving " +
                    "from localhost. Make sure this is intended!");
        }
        return publicAddress + contextPath + SLASH;
    }

    protected String getRequestPathAndQueryString() {
        RequestAttributes requestAttributes = RequestContextHolder.
                getRequestAttributes();

        String path = "";
        if (requestAttributes != null) {
            HttpServletRequest request =
                    ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request != null) {
                String servletPath = request.getServletPath();
                // In test profile, we are missing a servlet path, however the
                // path is retrievable UrlPathHelper
                if (null == servletPath || servletPath.isEmpty()) {
                    servletPath = new UrlPathHelper()
                            .getPathWithinApplication(request);
                }
                String queryString = request.getQueryString();
                if (null != queryString) {
                    path = servletPath + "?" + queryString;
                } else {
                    path = servletPath;
                }
            }
        }
        String outgoingAddress = getOutgoingAddress();
        // Take away the last slash as the servlet path starts with a slash
        return outgoingAddress.substring(0, outgoingAddress.length() - 1) + path;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    protected String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
