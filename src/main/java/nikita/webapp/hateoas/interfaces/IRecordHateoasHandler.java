package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IRecordHateoasHandler
        extends IHateoasHandler {

    void addReferenceSeries(INoarkEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceFile(INoarkEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceClass(INoarkEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentDescription(INoarkEntity entity,
                                   IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentDescription(INoarkEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addNewReferenceSeries(INoarkEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addStorageLocation(INoarkEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewStorageLocation(INoarkEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addComment(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewComment(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addCrossReference(INoarkEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewCrossReference(INoarkEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addPart(INoarkEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartPerson(INoarkEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartUnit(INoarkEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addCorrespondencePart(INoarkEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addNewCorrespondencePartPerson(INoarkEntity entity,
                                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewCorrespondencePartUnit(INoarkEntity entity,
                                      IHateoasNoarkObject hateoasNoarkObject);

    void addNewCorrespondencePartInternal(INoarkEntity entity,
                                          IHateoasNoarkObject hateoasNoarkObject);

    void addAuthor(INoarkEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addNewAuthor(INoarkEntity entity,
                      IHateoasNoarkObject hateoasNoarkObject);
}
