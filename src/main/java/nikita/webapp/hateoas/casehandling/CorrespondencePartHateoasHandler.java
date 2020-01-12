package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.ICorrespondencePartHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_TYPE;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add CorrespondencePartHateoas links with CorrespondencePart
 * specific information
 **/
@Component("correspondencePartHateoasHandler")
public class CorrespondencePartHateoasHandler
        extends HateoasHandler
        implements ICorrespondencePartHateoasHandler {

    @Override
    public void addEntityLinksOnTemplate(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        addCorrespondencePartType(entity, hateoasNoarkObject);
    }

    @Override
    public void addCorrespondencePartType(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + CORRESPONDENCE_PART_TYPE,
                REL_METADATA_CORRESPONDENCE_PART_TYPE, false));
    }
}
