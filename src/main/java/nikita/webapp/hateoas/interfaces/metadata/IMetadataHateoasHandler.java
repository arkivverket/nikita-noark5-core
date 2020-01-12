package nikita.webapp.hateoas.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

/**
 * Created by tsodring on 4/3/17.
 */
public interface IMetadataHateoasHandler
        extends IHateoasHandler {

    void addNewCode(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addCode(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
}
