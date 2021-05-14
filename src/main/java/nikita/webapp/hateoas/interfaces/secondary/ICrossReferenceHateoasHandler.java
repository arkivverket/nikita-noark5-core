package nikita.webapp.hateoas.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

public interface ICrossReferenceHateoasHandler
        extends IHateoasHandler {

    void addRecord(ISystemId entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addFile(ISystemId entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addClass(ISystemId entity,
                  IHateoasNoarkObject hateoasNoarkObject);
}
