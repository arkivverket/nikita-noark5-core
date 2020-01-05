package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IFileHateoasHandler
        extends IHateoasHandler {

    void addSeries(INikitaEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addEndFile(INikitaEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addExpandToCaseFile(INikitaEntity entity,
                             IHateoasNoarkObject hateoasNoarkObject);

    void addExpandToMeetingFile(INikitaEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addRecord(INikitaEntity entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addNewRecord(INikitaEntity entity,
                      IHateoasNoarkObject hateoasNoarkObject);

    void addComment(INikitaEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewComment(INikitaEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addParentFile(INikitaEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addSubFile(INikitaEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewSubFile(INikitaEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addCrossReference(INikitaEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewCrossReference(INikitaEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addClass(INikitaEntity entity,
                  IHateoasNoarkObject hateoasNoarkObject);

    void addNewClass(INikitaEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceSeries(INikitaEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewReferenceSeries(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceSecondaryClassification(
            INikitaEntity entity,
            IHateoasNoarkObject hateoasNoarkObject);

    void addNewReferenceSecondaryClassification(
            INikitaEntity entity,
            IHateoasNoarkObject hateoasNoarkObject);

    void addPart(INikitaEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartPerson(INikitaEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartUnit(INikitaEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    // Add national identifiers

    void addNewBuilding(INikitaEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewCadastralUnit(INikitaEntity entity,
                             IHateoasNoarkObject hateoasNoarkObject);

    void addNewDNumber(INikitaEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewPlan(INikitaEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewPosition(INikitaEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewSocialSecurityNumber(INikitaEntity entity,
                                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewUnit(INikitaEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addBuilding(INikitaEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addCadastralUnit(INikitaEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addDNumber(INikitaEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addPlan(INikitaEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addPosition(INikitaEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addSocialSecurityNumber(INikitaEntity entity,
                                 IHateoasNoarkObject hateoasNoarkObject);

    void addUnit(INikitaEntity entity,
                 IHateoasNoarkObject hateoasNoarkObject);
}
