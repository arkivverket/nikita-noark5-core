package nikita.webapp.hateoas.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPrecedenceEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IPrecedenceHateoasHandler extends IHateoasHandler {

    void addPrecedenceStatus(IPrecedenceEntity entity, IHateoasNoarkObject hateoasNoarkObject);
    void addCaseFile(IPrecedenceEntity entity, IHateoasNoarkObject hateoasNoarkObject);
    void addRegistryEntry(IPrecedenceEntity entity, IHateoasNoarkObject hateoasNoarkObject);

}
