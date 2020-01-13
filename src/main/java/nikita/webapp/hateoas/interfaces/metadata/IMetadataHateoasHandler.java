package nikita.webapp.hateoas.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;
import nikita.webapp.security.IAuthorisation;

/**
 * Created by tsodring on 4/3/17.
 */
public interface IMetadataHateoasHandler
        extends IHateoasHandler {

    void addSelfLink(IMetadataEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinks(IMetadataEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinksOnCreate(IMetadataEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinksOnTemplate(IMetadataEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject);

    void addEntityLinksOnRead(IMetadataEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addNewCode(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addCode(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
}
