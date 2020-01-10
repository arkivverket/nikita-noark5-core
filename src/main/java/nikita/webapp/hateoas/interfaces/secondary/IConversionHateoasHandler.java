package nikita.webapp.hateoas.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IConversionEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

public interface IConversionHateoasHandler
        extends IHateoasHandler {

    void addDocumentObject(IConversionEntity conversion,
                           IHateoasNoarkObject hateoasNoarkObject);

}
