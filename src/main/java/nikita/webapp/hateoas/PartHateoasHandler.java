package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.interfaces.IPartHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.HREF_BASE_METADATA;
import static nikita.common.config.Constants.REL_METADATA_PART_ROLE;
import static nikita.common.config.N5ResourceMappings.PART_ROLE;

/**
 * Created by tsodring on 11/07/19.
 * <p>
 * Used to add PartHateoas links with Part
 * specific information
 **/
@Component("partHateoasHandler")
public class PartHateoasHandler
        extends HateoasHandler
        implements IPartHateoasHandler {

    @Override
    public void addEntityLinksOnTemplate(INikitaEntity entity,
                                         IHateoasNoarkObject
                                                 hateoasNoarkObject) {
        addPartRole(entity, hateoasNoarkObject);
    }

    @Override
    public void addPartRole(INikitaEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + PART_ROLE, REL_METADATA_PART_ROLE,
                false));
    }
}