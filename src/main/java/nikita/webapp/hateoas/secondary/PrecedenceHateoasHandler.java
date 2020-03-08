package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPrecedenceEntity;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.ISystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IPrecedenceHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.PRECEDENCE_STATUS;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add PrecedenceHateoas links with Precedence specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for various CRUD operations so keeping them
 * separate calls at the moment.
 */
@Component("precedenceHateoasHandler")
public class PrecedenceHateoasHandler
        extends SystemIdHateoasHandler
        implements IPrecedenceHateoasHandler {

    @Override
    public void addEntityLinks
        (ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        IPrecedenceEntity precedence = (IPrecedenceEntity) entity;
        addPrecedenceStatus(precedence, hateoasNoarkObject);
        // TODO figure out relations to add more case files and record notes
        // TODO add relation to list case files and record notes
        //addCaseFile(precedence, hateoasNoarkObject);
        //addRegistryEntry(precedence, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate
        (ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        IPrecedenceEntity precedence = (IPrecedenceEntity) entity;
        addPrecedenceStatus(precedence, hateoasNoarkObject);
    }

    @Override
    public void addPrecedenceStatus
        (IPrecedenceEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
            new Link(getOutgoingAddress() + HREF_BASE_METADATA + SLASH + PRECEDENCE_STATUS,
                REL_METADATA_PRECEDENCE_STATUS, true));
    }

    @Override
    public void addCaseFile(IPrecedenceEntity precedence,
                            IHateoasNoarkObject hateoasNoarkObject) {
        /*
        if (null != precedence.getReferenceCaseFile()) {
            hateoasNoarkObject.addLink(precedence,
                new Link(getOutgoingAddress() +
                         HREF_BASE_CASE_FILE + SLASH +
                         precedence.getReferenceCaseFile().getSystemId(),
                         REL_CASE_HANDLING_CASE_FILE));
        }
        */
    }

    @Override
    public void addRegistryEntry(IPrecedenceEntity precedence,
                                 IHateoasNoarkObject hateoasNoarkObject) {
        /*
        if (null != precedence.getReferenceRegistryEntry()) {
            hateoasNoarkObject.addLink(precedence,
                new Link(getOutgoingAddress() +
                         HREF_BASE_REGISTRY_ENTRY + SLASH +
                         precedence.getReferenceRegistryEntry().getSystemId(),
                         REL_CASE_HANDLING_REGISTRY_ENTRY));
        }
        */
    }
}
