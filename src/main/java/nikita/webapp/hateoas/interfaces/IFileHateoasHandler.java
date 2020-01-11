package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IFileHateoasHandler
        extends IHateoasHandler {

    void addSeries(INoarkEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addEndFile(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addExpandToCaseFile(INoarkEntity entity,
                             IHateoasNoarkObject hateoasNoarkObject);

    void addExpandToMeetingFile(INoarkEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addRecord(INoarkEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addNewRecord(INoarkEntity entity,
                      IHateoasNoarkObject hateoasNoarkObject);

    void addComment(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewComment(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addParentFile(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addSubFile(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewSubFile(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addCrossReference(INoarkEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewCrossReference(INoarkEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addClass(INoarkEntity entity,
                  IHateoasNoarkObject hateoasNoarkObject);

    void addNewClass(INoarkEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceSeries(INoarkEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewReferenceSeries(INoarkEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceSecondaryClassification(
            INoarkEntity entity,
            IHateoasNoarkObject hateoasNoarkObject);

    void addNewReferenceSecondaryClassification(
            INoarkEntity entity,
            IHateoasNoarkObject hateoasNoarkObject);

    void addPart(INoarkEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartPerson(INoarkEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartUnit(INoarkEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    // Add national identifiers

    void addNewBuilding(INoarkEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewCadastralUnit(INoarkEntity entity,
                             IHateoasNoarkObject hateoasNoarkObject);

    void addNewDNumber(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewPlan(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewPosition(INoarkEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewSocialSecurityNumber(INoarkEntity entity,
                                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewUnit(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addBuilding(INoarkEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addCadastralUnit(INoarkEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addDNumber(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addPlan(INoarkEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addPosition(INoarkEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addSocialSecurityNumber(INoarkEntity entity,
                                 IHateoasNoarkObject hateoasNoarkObject);

    void addUnit(INoarkEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);
}
