package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.webapp.security.IAuthorisation;
import org.springframework.stereotype.Component;

/**
 * Used to add self link to an empty list being returned
 */
@Component
public class EmptyListHateoasHandler
        extends HateoasHandler {

    public EmptyListHateoasHandler() {
    }

    @Override
    public void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                         IAuthorisation authorisation) {
        hateoasNoarkObject.addSelfLink(
                new Link(getRequestPathAndQueryString(), getRelSelfLink()));
    }

}
