package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
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

    void addSelfLink(INoarkEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinks(INoarkEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinksOnCreate(INoarkEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinksOnTemplate(INoarkEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinksOnRead(INoarkEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentMedium(INoarkEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);


    // The following are required to give @Value during reflection
    void setPublicAddress(String publicAddress);

    void setContextPath(String contextPath);
}
