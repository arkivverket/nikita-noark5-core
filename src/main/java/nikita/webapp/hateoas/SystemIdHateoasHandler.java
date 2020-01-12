package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.ISystemIdHateoasHandler;
import nikita.webapp.security.IAuthorisation;

import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_MEDIUM;
import static org.apache.poi.hslf.record.RecordTypes.List;

public class SystemIdHateoasHandler
        extends HateoasHandler
        implements ISystemIdHateoasHandler {


    /**
     * Create a self link and self pointing entity link. Allows a client to be
     * able to identify the entity type. Described in:
     * <p>
     * https://github.com/arkivverket/noark5-tjenestegrensesnitt-standard/blob/master/kapitler/06-konsepter_og_prinsipper.md#identifisere-entitetstype
     *
     * @param entity             The Noark entity
     * @param hateoasNoarkObject The Hateoas Noark Object
     */
    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + entity.getFunctionalTypeName() +
                SLASH + entity.getBaseTypeName() + SLASH + entity.getSystemId()
                + SLASH, getRelSelfLink()));

        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + entity.getFunctionalTypeName() +
                SLASH + entity.getBaseTypeName() + SLASH + entity.getSystemId()
                + SLASH, entity.getBaseRel()));
    }

    @Override
    public void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                         IAuthorisation authorisation) {
        this.authorisation = authorisation;

        Iterable<ISystemId> entities = (List<ISystemId>) (List)
        hateoasNoarkObject.getList();
        for (ISystemId entity : entities) {
            addSelfLink(entity, hateoasNoarkObject);
            addEntityLinks(entity, hateoasNoarkObject);
        }
        // If hateoasNoarkObject is a list add a self link.
        // { "entity": [], "_links": [] }
        if (!hateoasNoarkObject.isSingleEntity()) {
            String url = getRequestPathAndQueryString();
            Link selfLink = new Link(url, getRelSelfLink(), false);
            hateoasNoarkObject.addSelfLink(selfLink);
        }
    }

    @Override
    public void addDocumentMedium(ISystemId entity,
                                  IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + DOCUMENT_MEDIUM,
                REL_METADATA_DOCUMENT_MEDIUM, false));
    }


    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnCreate(ISystemId entity,
                                       IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnTemplate(ISystemId entity,
                                         IHateoasNoarkObject hateoasNoarkObject) {
    }

    @Override
    public void addLinksOnTemplate(IHateoasNoarkObject hateoasNoarkObject,
                                   IAuthorisation authorisation) {
        this.authorisation = authorisation;

        Iterable<ISystemId> entities = (List<ISystemId>) (List)
                hateoasNoarkObject.getList();
        for (ISystemId entity : entities) {
            addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        }
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnRead(ISystemId entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }
}
