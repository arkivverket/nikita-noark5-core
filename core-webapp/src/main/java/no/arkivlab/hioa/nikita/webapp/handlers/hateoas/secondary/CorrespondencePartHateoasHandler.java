package no.arkivlab.hioa.nikita.webapp.handlers.hateoas.secondary;

import nikita.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.model.noark5.v4.hateoas.Link;
import nikita.model.noark5.v4.interfaces.entities.INikitaEntity;
import no.arkivlab.hioa.nikita.webapp.handlers.hateoas.HateoasHandler;
import no.arkivlab.hioa.nikita.webapp.handlers.hateoas.interfaces.secondary.ICorrespondencePartHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.config.Constants.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add CorrespondencePartHateoas links with CorrespondencePart specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for various CRUD operations so keeping them
 * separate calls at the moment.
 */
@Component("correspondencePartHateoasHandler")
public class CorrespondencePartHateoasHandler extends HateoasHandler implements ICorrespondencePartHateoasHandler {

    @Override
    public void addEntityLinksOnNew(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        addCorrespondencePartType(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        addCorrespondencePartType(entity, hateoasNoarkObject);
    }

    @Override
    public void addCorrespondencePartType(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(contextPath + HATEOAS_API_PATH + SLASH +
                NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_CORRESPONDENCE_PART, false));
    }

    @Override
    public void addSelfLink(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        String systemId = entity.getSystemId();
        hateoasNoarkObject.addLink(entity, new Link(contextPath + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + entity.getBaseTypeName() + SLASH + systemId + SLASH,
                getRelSelfLink(), false));
    }
}
