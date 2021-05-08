package nikita.webapp.hateoas.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IKeywordEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

public interface IKeywordHateoasHandler
        extends IHateoasHandler {

    void addRecord(IKeywordEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addFile(IKeywordEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addClass(IKeywordEntity entity,
                  IHateoasNoarkObject hateoasNoarkObject);
}
