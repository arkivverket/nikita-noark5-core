package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.IFondsCreatorHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.FONDS;
import static nikita.common.config.N5ResourceMappings.FONDS_CREATOR;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add FondsCreatorHateoas links with FondsCreator specific information
 */
@Component("fondsCreatorHateoasHandler")
public class FondsCreatorHateoasHandler
        extends SystemIdHateoasHandler
        implements IFondsCreatorHateoasHandler {

    @Override
    public void addEntityLinks(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        addFonds(entity, hateoasNoarkObject);
        addNewFonds(entity, hateoasNoarkObject);
    }

    @Override
    public void addFonds(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FONDS_STRUCTURE + SLASH + FONDS_CREATOR  + SLASH + entity.getSystemId() + SLASH + FONDS,
                REL_FONDS_STRUCTURE_FONDS, false));
    }

    @Override
    public void addNewFonds(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FONDS_STRUCTURE + SLASH + FONDS_CREATOR + SLASH + entity.getSystemId() + SLASH + NEW_FONDS,
                REL_FONDS_STRUCTURE_NEW_FONDS, false));
    }
}
