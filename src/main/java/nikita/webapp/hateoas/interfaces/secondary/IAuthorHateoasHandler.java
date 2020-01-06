package nikita.webapp.hateoas.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IAuthorEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

public interface IAuthorHateoasHandler
        extends IHateoasHandler {

    void addRecord(IAuthorEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentDescription(IAuthorEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject);
}
