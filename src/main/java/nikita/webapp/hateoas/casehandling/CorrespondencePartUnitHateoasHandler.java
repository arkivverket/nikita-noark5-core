package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.hateoas.Link;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_TYPE;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add CorrespondencePartHateoas links with CorrespondencePart
 * specific information
 */
@Component("correspondencePartUnitHateoasHandler")
public class CorrespondencePartUnitHateoasHandler
        extends CorrespondencePartHateoasHandler {

    @Override
    public void addEntityLinksOnTemplate(INikitaEntity entity,
                                         IHateoasNoarkObject
                                                 hateoasNoarkObject, String outgoingAddress) {
        addCorrespondencePartType(entity, hateoasNoarkObject, outgoingAddress);
    }

    public void addAdministrativeUnit(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress +
                HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH +
                CORRESPONDENCE_PART_TYPE, REL_METADATA_CORRESPONDENCE_PART_TYPE,
                false));
    }

    public void addUser(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress +
                HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH +
                CORRESPONDENCE_PART_TYPE, REL_METADATA_CORRESPONDENCE_PART_TYPE,
                false));
    }

}
