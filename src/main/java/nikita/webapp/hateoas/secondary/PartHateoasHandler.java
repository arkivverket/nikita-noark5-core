package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IPartHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.HREF_BASE_METADATA;
import static nikita.common.config.Constants.REL_METADATA_PART_ROLE;
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.N5ResourceMappings.PART_ROLE;

/**
 * Created by tsodring on 11/07/19.
 * <p>
 * Used to add PartHateoas links with Part
 * specific information
 **/
@Component("partHateoasHandler")
public class PartHateoasHandler
        extends SystemIdHateoasHandler
        implements IPartHateoasHandler {

    @Override
    public void addEntityLinksOnTemplate(ISystemId entity,
                                         IHateoasNoarkObject
                                                 hateoasNoarkObject) {
        addPartRole(entity, hateoasNoarkObject);
    }

    @Override
    public void addPartRole(INoarkEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + PART_ROLE, REL_METADATA_PART_ROLE,
                false));
    }
}
