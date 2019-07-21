package nikita.webapp.hateoas.nationalidentifier;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.ISocialSecurityNumberHateoasHandler;
import org.springframework.stereotype.Component;

/**
 * Created by tsodring
 */
@Component
public class SocialSecurityNumberHateoasHandler
        extends HateoasHandler
        implements ISocialSecurityNumberHateoasHandler {

    public SocialSecurityNumberHateoasHandler() {
    }

    @Override
    public void addEntityLinks(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
    }
}
