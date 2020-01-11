package nikita.webapp.hateoas.nationalidentifier;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.IPlanHateoasHandler;
import org.springframework.stereotype.Component;

/**
 * Created by tsodring
 */
@Component
public class PlanHateoasHandler
        extends HateoasHandler
        implements IPlanHateoasHandler {

    public PlanHateoasHandler() {
    }

    @Override
    public void addEntityLinks(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
    }
}
