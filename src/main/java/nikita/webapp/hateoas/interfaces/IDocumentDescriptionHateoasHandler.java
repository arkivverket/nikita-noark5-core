package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IDocumentDescriptionHateoasHandler
        extends IHateoasHandler {

    void addRecord(INoarkEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentObject(INoarkEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentObject(INoarkEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addStorageLocation(INoarkEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewStorageLocation(INoarkEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addComment(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewComment(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentType(INoarkEntity entity,
                         IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentStatus(INoarkEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addPart(INoarkEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartPerson(INoarkEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartUnit(INoarkEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addAuthor(INoarkEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addNewAuthor(INoarkEntity entity,
                      IHateoasNoarkObject hateoasNoarkObject);
}

