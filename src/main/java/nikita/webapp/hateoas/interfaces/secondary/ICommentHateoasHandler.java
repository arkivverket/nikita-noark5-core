package nikita.webapp.hateoas.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICommentEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

public interface ICommentHateoasHandler
        extends IHateoasHandler {

    void addCommentType(ICommentEntity comment,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addFile(ICommentEntity comment,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addRecord(ICommentEntity comment,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentDescription(ICommentEntity comment,
                                IHateoasNoarkObject hateoasNoarkObject);

}
