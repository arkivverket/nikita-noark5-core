package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IPartHateoasHandler
        extends IHateoasHandler {

    void addPartRole(INoarkEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);
}
