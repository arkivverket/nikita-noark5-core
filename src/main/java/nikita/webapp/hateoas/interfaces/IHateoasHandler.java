package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.security.IAuthorisation;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IHateoasHandler {

    void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                  IAuthorisation authorisation);

    void addLinksOnCreate(IHateoasNoarkObject hateoasNoarkObject,
                          IAuthorisation authorisation);

    void addLinksOnTemplate(IHateoasNoarkObject hateoasNoarkObject,
                            IAuthorisation authorisation);

    void addLinksOnRead(IHateoasNoarkObject hateoasNoarkObject,
                        IAuthorisation authorisation);

    void addLinksOnUpdate(IHateoasNoarkObject hateoasNoarkObject,
                          IAuthorisation authorisation);

    void addLinksOnDelete(IHateoasNoarkObject hateoasNoarkObject,
                          IAuthorisation authorisation);

    void addSelfLink(ISystemId entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinks(ISystemId entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinksOnCreate(ISystemId entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinksOnTemplate(ISystemId entity,
                                  IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinksOnRead(ISystemId entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentMedium(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject);


    // The following are required to give @Value during reflection
    void setPublicAddress(String publicAddress);

    void setContextPath(String contextPath);
}
