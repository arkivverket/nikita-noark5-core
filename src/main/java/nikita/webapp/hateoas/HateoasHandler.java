package nikita.webapp.hateoas;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.hateoas.Link;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;
import nikita.webapp.security.IAuthorisation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.SELF;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_MEDIUM;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add Hateoas links with information
 *
 */
@Component
public class HateoasHandler implements IHateoasHandler {

    protected IAuthorisation authorisation;

    @Value("${nikita.server.hateoas.publicAddress}")
    protected String contextPath;

    public HateoasHandler(String contextPath) {
        this.contextPath = contextPath;
    }

    public HateoasHandler() {
    }

    @Override
    public void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                         IAuthorisation authorisation, String outgoingAddress) {
        this.authorisation = authorisation;

        Iterable<INikitaEntity> entities = hateoasNoarkObject.getList();
        for (INikitaEntity entity : entities) {
            addSelfLink(entity, hateoasNoarkObject, outgoingAddress);
            addEntityLinks(entity, hateoasNoarkObject, outgoingAddress);
        }
        // If hateoasNoarkObject is a list add a self link.
        // { "entity": [], "_links": [] }
        if (!hateoasNoarkObject.isSingleEntity()) {
            String url = this.contextPath;
            Link selfLink = new Link(url, getRelSelfLink(), false);
            hateoasNoarkObject.addSelfLink(selfLink);
        }
    }

    @Override
    public void addLinksOnCreate(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation, String outgoingAddress) {
        addLinks(hateoasNoarkObject, authorisation, outgoingAddress);
    }

    @Override
    public void addLinksOnNew(IHateoasNoarkObject hateoasNoarkObject,
                              IAuthorisation authorisation, String outgoingAddress) {
        this.authorisation = authorisation;

        Iterable<INikitaEntity> entities = hateoasNoarkObject.getList();
        for (INikitaEntity entity : entities) {
            addEntityLinksOnNew(entity, hateoasNoarkObject, outgoingAddress);
        }
    }

    @Override
    public void addLinksOnRead(IHateoasNoarkObject hateoasNoarkObject,
                               IAuthorisation authorisation, String outgoingAddress) {
        addLinks(hateoasNoarkObject, authorisation, outgoingAddress);
    }

    @Override
    public void addLinksOnUpdate(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation, String outgoingAddress) {
        addLinks(hateoasNoarkObject, authorisation, outgoingAddress);
    }

    @Override
    public void addLinksOnDelete(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation, String outgoingAddress) {
        addLinks(hateoasNoarkObject, authorisation, outgoingAddress);
    }

    @Override
    public void addLinksOnTemplate(IHateoasNoarkObject hateoasNoarkObject,
                                   IAuthorisation authorisation, String outgoingAddress) {
        this.authorisation = authorisation;

        Iterable<INikitaEntity> entities = hateoasNoarkObject.getList();
        for (INikitaEntity entity : entities) {
            addEntityLinksOnTemplate(entity, hateoasNoarkObject, outgoingAddress);
        }
    }

    @Override
    public void addSelfLink(INikitaEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        String systemId = entity.getSystemId();
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress +
                HATEOAS_API_PATH + SLASH + entity.getFunctionalTypeName() +
                SLASH + entity.getBaseTypeName() + SLASH + systemId + SLASH,
                getRelSelfLink(), false));
    }

    @Override
    public void addDocumentMedium(INikitaEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress +
                HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH +
                DOCUMENT_MEDIUM, REL_METADATA_DOCUMENT_MEDIUM, false));
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinks(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnCreate(INikitaEntity entity,
                                       IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        addEntityLinks(entity, hateoasNoarkObject, outgoingAddress);
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnTemplate(INikitaEntity entity,
                                         IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnNew(INikitaEntity entity,
                                    IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnRead(INikitaEntity entity,
                                     IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        addEntityLinks(entity, hateoasNoarkObject, outgoingAddress);
    }

    protected String getRelSelfLink() {
        return SELF;
    }

}

