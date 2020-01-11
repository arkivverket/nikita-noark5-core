package nikita.webapp.hateoas.interfaces.admin;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

/**
 * Created by tsodring on 5/18/17.
 */
public interface IAdministrativeUnitHateoasHandler extends IHateoasHandler {
    void addChildAdministrativeUnit(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
}
