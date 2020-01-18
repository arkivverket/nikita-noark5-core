package nikita.webapp.hateoas.nationalidentifier;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.IPositionHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring
 */
@Component
public class PositionHateoasHandler
        extends SystemIdHateoasHandler
        implements IPositionHateoasHandler {

    public PositionHateoasHandler() {
    }

    @Override
    public void addEntityLinks(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        addCoordinateSystem(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(
            ISystemId entity,
            IHateoasNoarkObject hateoasNoarkObject) {
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        addCoordinateSystem(entity, hateoasNoarkObject);
    }

    public void addCoordinateSystem(ISystemId entity,
                                    IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + COORDINATE_SYSTEM,
                REL_METADATA_COORDINATE_SYSTEM, false));
    }

}
