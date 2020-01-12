package nikita.webapp.hateoas.nationalidentifier;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.IPositionHateoasHandler;
import org.springframework.stereotype.Component;

/**
 * Created by tsodring
 */
@Component
public class PositionHateoasHandler
        extends HateoasHandler
        implements IPositionHateoasHandler {

    public PositionHateoasHandler() {
    }

    @Override
    public void addEntityLinks(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
    }
}
