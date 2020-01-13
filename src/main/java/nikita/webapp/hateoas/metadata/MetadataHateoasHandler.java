package nikita.webapp.hateoas.metadata;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.IAuthorisation;
import org.springframework.stereotype.Component;

import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_MEDIUM;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add hateoas links for metadata entities with specific information
 */
@Component()
public class MetadataHateoasHandler
        extends HateoasHandler
        implements IMetadataHateoasHandler {

    @Override
    public void addSelfLink(IMetadataEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject) {

        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + entity.getFunctionalTypeName() +
                SLASH + entity.getBaseTypeName() + SLASH + entity.getCode() +
                SLASH, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + entity.getFunctionalTypeName() +
                SLASH + entity.getBaseTypeName() + SLASH + entity.getCode() +
                SLASH, entity.getBaseRel()));
    }

    @Override
    public void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                         IAuthorisation authorisation) {
        this.authorisation = authorisation;

        Iterable<IMetadataEntity> entities = (List <IMetadataEntity>)
                (List) hateoasNoarkObject.getList();
        for (IMetadataEntity entity : entities) {
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
    public void addEntityLinks(
            IMetadataEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        addCode(entity, hateoasNoarkObject);
    }

    @Override
    public void addLinksOnCreate(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation) {
        addLinks(hateoasNoarkObject, authorisation);
    }

    @Override
    public void addLinksOnRead(IHateoasNoarkObject hateoasNoarkObject,
                               IAuthorisation authorisation) {
        addLinks(hateoasNoarkObject, authorisation);
    }

    @Override
    public void addLinksOnUpdate(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation) {
        addLinks(hateoasNoarkObject, authorisation);
    }

    @Override
    public void addLinksOnDelete(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation) {
        addLinks(hateoasNoarkObject, authorisation);
    }

    @Override
    public void addLinksOnTemplate(IHateoasNoarkObject hateoasNoarkObject,
                                   IAuthorisation authorisation) {
        this.authorisation = authorisation;
        Iterable<IMetadataEntity> entities =
                (List<IMetadataEntity>) (List) hateoasNoarkObject.getList();
        for (IMetadataEntity entity : entities) {
            addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        }
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnCreate(IMetadataEntity entity,
                                       IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnTemplate(
            IMetadataEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnRead(IMetadataEntity entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }

    @Override
    public void addCode(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        String code = ((IMetadataEntity) entity).getCode();
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + entity.getBaseTypeName() + SLASH +
                code + SLASH,
                REL_METADATA + entity.getBaseTypeName() + SLASH));
    }
    @Override
    public void addNewCode(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + NEW + DASH +
                entity.getBaseTypeName(),NIKITA_CONFORMANCE_REL + NEW +
                DASH + entity.getBaseTypeName() + SLASH));
    }
}
