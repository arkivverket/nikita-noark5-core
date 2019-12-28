package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
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
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        addCorrespondencePartType(entity, hateoasNoarkObject);
    }

    @Override
    public void addCorrespondencePartType(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + CORRESPONDENCE_PART_TYPE,
                REL_METADATA_CORRESPONDENCE_PART_TYPE, false));
    }
}
