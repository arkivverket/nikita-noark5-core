package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IDocumentDescriptionHateoasHandler
        extends IHateoasHandler {

    void addRecord(INikitaEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentObject(INikitaEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentObject(INikitaEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addDisposal(INikitaEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addNewDisposal(INikitaEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addDisposalUndertaken(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addNewDisposalUndertaken(INikitaEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject);

    void addDeletion(INikitaEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addNewDeletion(INikitaEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addScreening(INikitaEntity entity,
                      IHateoasNoarkObject hateoasNoarkObject);

    void addNewScreening(INikitaEntity entity,
                         IHateoasNoarkObject hateoasNoarkObject);

    void addStorageLocation(INikitaEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewStorageLocation(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addComment(INikitaEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewComment(INikitaEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentType(INikitaEntity entity,
                         IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentStatus(INikitaEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addPart(INikitaEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartPerson(INikitaEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartUnit(INikitaEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addAuthor(INikitaEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addNewAuthor(INikitaEntity entity,
                      IHateoasNoarkObject hateoasNoarkObject);
}

