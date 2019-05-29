package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;
import nikita.webapp.security.IAuthorisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.SELF;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_MEDIUM;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add Hateoas links with information
 */
@Component
public class HateoasHandler implements IHateoasHandler {

 private static final Logger logger =
            LoggerFactory.getLogger(HateoasHandler.class);

    protected IAuthorisation authorisation;

    @Value("${nikita.server.hateoas.publicAddress}")
    protected String publicAddress;

    @Value("${server.servlet.context-path}")
    protected String contextPath;

    public HateoasHandler() {
    }

    @Override
    public void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                         IAuthorisation authorisation) {
        this.authorisation = authorisation;

        Iterable<INikitaEntity> entities = hateoasNoarkObject.getList();
        for (INikitaEntity entity : entities) {
            addSelfLink(entity, hateoasNoarkObject);
            addEntityLinks(entity, hateoasNoarkObject);
        }
        // If hateoasNoarkObject is a list add a self link.
        // { "entity": [], "_links": [] }
        if (!hateoasNoarkObject.isSingleEntity()) {
            String url = this.getOutgoingAddress();
            Link selfLink = new Link(url, getRelSelfLink(), false);
            hateoasNoarkObject.addSelfLink(selfLink);
        }
    }

    @Override
    public void addLinksOnCreate(IHateoasNoarkObject hateoasNoarkObject,
                                 IAuthorisation authorisation) {
        addLinks(hateoasNoarkObject, authorisation);
    }

    @Override
    public void addLinksOnNew(IHateoasNoarkObject hateoasNoarkObject,
                              IAuthorisation authorisation) {
        this.authorisation = authorisation;

        Iterable<INikitaEntity> entities = hateoasNoarkObject.getList();
        for (INikitaEntity entity : entities) {
            addEntityLinksOnNew(entity, hateoasNoarkObject);
        }
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

        Iterable<INikitaEntity> entities = hateoasNoarkObject.getList();
        for (INikitaEntity entity : entities) {
            addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        }
    }

    @Override
    public void addSelfLink(INikitaEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String systemId = entity.getSystemId();
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + entity.getFunctionalTypeName() +
                SLASH + entity.getBaseTypeName() + SLASH + systemId + SLASH,
                getRelSelfLink(), false));
    }

    @Override
    public void addDocumentMedium(INikitaEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH +
                DOCUMENT_MEDIUM, REL_METADATA_DOCUMENT_MEDIUM, false));
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinks(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnCreate(INikitaEntity entity,
                                       IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnTemplate(INikitaEntity entity,
                                         IHateoasNoarkObject hateoasNoarkObject) {
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnNew(INikitaEntity entity,
                                    IHateoasNoarkObject hateoasNoarkObject) {
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnRead(INikitaEntity entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }

    protected String getRelSelfLink() {
        return SELF;
    }

    /**
     * Get the outgoing address to use when generating links.
     * If we are not running behind a front facing server incoming requests
     * will not have X-Forward-* values set. In this case use the hardcoded
     * value from the properties file.
     * <p>
     * If X-Forward-*  values are set, then use them. At a minimum Host and
     * Proto must be set. If Port is also set use this to.
     *
     * @return the outgoing address
     */
    protected String getOutgoingAddress() {
        RequestAttributes requestAttributes = RequestContextHolder.
                getRequestAttributes();
        HttpServletRequest request  =
                ((ServletRequestAttributes) requestAttributes).getRequest();

        if (request != null) {
            String address = request.getHeader("X-Forwarded-Host");
            String protocol = request.getHeader("X-Forwarded-Proto");
            String port = request.getHeader("X-Forwarded-Port");

            if (address != null && protocol != null) {
                if (port != null) {
                    return protocol + "://" + address + ":" + port +
                            contextPath + SLASH;
                } else {
                    return protocol + "://" + address + contextPath + SLASH;
                }
            }
        }
        return publicAddress + contextPath + SLASH;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}

