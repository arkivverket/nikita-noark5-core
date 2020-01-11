package nikita.webapp.hateoas.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IPrecedenceHateoasHandler extends IHateoasHandler {

    void addPrecedenceStatus(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
    void addNewCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
    void addCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
    void addNewPrecedence(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
    void addPrecedence(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
    void addNewRegistryEntry(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
    void addRegistryEntry(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

}
