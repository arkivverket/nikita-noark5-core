package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IScreeningMetadataHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.HREF_BASE_FONDS_STRUCTURE;
import static nikita.common.config.Constants.SLASH;
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
                entity.getSystemId();
        hateoasNoarkObject.addLink(entity, new Link(selfHref,
                getRelSelfLink()));
        hateoasNoarkObject.addLink(entity, new Link(selfHref,
                entity.getBaseRel()));
    }
}
