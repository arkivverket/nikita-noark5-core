package nikita.webapp.hateoas.metadata;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.metadata.IBSMHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.HREF_BASE_FONDS_STRUCTURE;
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.N5ResourceMappings.BSM_DEF;

@Component
public class BSMBaseHateoasHandler
        extends SystemIdHateoasHandler
        implements IBSMHateoasHandler {

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfhref = getOutgoingAddress() +
                HREF_BASE_FONDS_STRUCTURE + SLASH + BSM_DEF + SLASH + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                new Link(selfhref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                new Link(selfhref, entity.getBaseRel()));
    }
}
