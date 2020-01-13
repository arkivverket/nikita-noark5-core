package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IDocumentDescriptionHateoasHandler
        extends IHateoasHandler {

    void addRecord(ISystemId entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentObject(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentObject(ISystemId entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addStorageLocation(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewStorageLocation(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addComment(ISystemId entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewComment(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentType(ISystemId entity,
                         IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentStatus(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addPart(ISystemId entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartPerson(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartUnit(ISystemId entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addAuthor(ISystemId entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addNewAuthor(ISystemId entity,
                      IHateoasNoarkObject hateoasNoarkObject);
}

