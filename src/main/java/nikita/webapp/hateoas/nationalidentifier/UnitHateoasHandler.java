package nikita.webapp.hateoas.nationalidentifier;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.IUnitHateoasHandler;
import org.springframework.stereotype.Component;

/**
 * Created by tsodring
 */
@Component
public class UnitHateoasHandler
        extends SystemIdHateoasHandler
        implements IUnitHateoasHandler {

    public UnitHateoasHandler() {
    }

    @Override
    public void addEntityLinks(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
    }
}
