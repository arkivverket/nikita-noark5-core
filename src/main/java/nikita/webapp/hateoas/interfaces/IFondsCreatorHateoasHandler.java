package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler for FondsCreator
 */
public interface IFondsCreatorHateoasHandler extends IHateoasHandler {

    void addFonds(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewFonds(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

}
