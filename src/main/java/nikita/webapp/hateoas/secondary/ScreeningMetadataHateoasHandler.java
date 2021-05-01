package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IScreeningMetadataHateoasHandler;
import nikita.webapp.security.IAuthorisation;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.SCREENING_METADATA;

@Component
public class ScreeningMetadataHateoasHandler
        extends SystemIdHateoasHandler
        implements IScreeningMetadataHateoasHandler {

    public ScreeningMetadataHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfHref = getOutgoingAddress() +
                HREF_BASE_FONDS_STRUCTURE + SLASH + SCREENING_METADATA + SLASH +
                entity.getSystemId() + SLASH;
        hateoasNoarkObject.addLink(entity, new Link(selfHref,
                getRelSelfLink()));
        hateoasNoarkObject.addLink(entity, new Link(selfHref,
                entity.getBaseRel()));
    }

    @Override
    public void addLinksOnTemplate(
            IHateoasNoarkObject hateoasNoarkObject, IAuthorisation authorisation) {
        hateoasNoarkObject.addLink(new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + SCREENING_METADATA,
                REL_METADATA_SCREENING_METADATA));
    }
}
