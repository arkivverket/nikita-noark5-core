package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import nikita.webapp.security.IAuthorisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nikita.common.config.Constants.NOARK_BASE_REL;
import static nikita.common.config.Constants.SLASH;

/**
 * Used to add self link to an empty list being returned
 */
@Component
public class EmptyListHateoasHandler
        extends HateoasHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(EmptyListHateoasHandler.class);

    private Pattern pattern = Pattern.compile(".*\\/(\\w+)\\/(\\w+)\\/?$");

    public EmptyListHateoasHandler() {
    }

    @Override
    public void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                         IAuthorisation authorisation) {
        hateoasNoarkObject.addSelfLink(
                new Link(getRequestPathAndQueryString(), getRelSelfLink()));
        hateoasNoarkObject.addSelfLink(
                new Link(getRequestPathAndQueryString(), getRel()));
    }

    private String getRel() {
        RequestAttributes requestAttributes = RequestContextHolder.
                getRequestAttributes();
        HttpServletRequest request =
                ((ServletRequestAttributes) requestAttributes).getRequest();
        String rel = NOARK_BASE_REL;
        if (request != null) {
            String path = new UrlPathHelper().getPathWithinApplication(request);
            Matcher matcher = pattern.matcher(path);
            if (!matcher.matches()) {
                String errorMessage = "Unable to create rel from " + path;
                logger.error(errorMessage);
                throw new NikitaMisconfigurationException(errorMessage);
            }
            rel += matcher.group(1) + SLASH + matcher.group(2) + SLASH;
        }
        return rel;
    }
}
