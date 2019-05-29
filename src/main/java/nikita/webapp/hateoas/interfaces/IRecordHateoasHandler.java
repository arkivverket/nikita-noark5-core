package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IRecordHateoasHandler extends IHateoasHandler {

    void addReferenceSeries(INikitaEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceFile(INikitaEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceClass(INikitaEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentDescription(INikitaEntity entity,
                                   IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentDescription(INikitaEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addNewReferenceSeries(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addClassified(INikitaEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewClassified(INikitaEntity entity,
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

    void addAuthor(INikitaEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addNewAuthor(INikitaEntity entity,
                      IHateoasNoarkObject hateoasNoarkObject);

    void addCrossReference(INikitaEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewCrossReference(INikitaEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addKeyword(INikitaEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewKeyword(INikitaEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);
}
