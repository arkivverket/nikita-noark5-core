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
public interface ISystemIdHateoasHandler
    extends  IHateoasHandler{

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

}
