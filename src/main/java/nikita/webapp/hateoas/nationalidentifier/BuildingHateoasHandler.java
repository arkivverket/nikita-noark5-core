package nikita.webapp.hateoas.nationalidentifier;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.nationalidentifier.NationalIdentifierHateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.IBuildingHateoasHandler;
import org.springframework.stereotype.Component;

/**
 * Created by tsodring
 */
@Component
public class BuildingHateoasHandler
        extends NationalIdentifierHateoasHandler
        implements IBuildingHateoasHandler {

    public BuildingHateoasHandler() {
    }

    @Override
    public void addEntityLinks(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
    }
}
