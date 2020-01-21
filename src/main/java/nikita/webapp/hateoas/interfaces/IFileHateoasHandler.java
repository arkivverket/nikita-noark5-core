package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IFileHateoasHandler
        extends IHateoasHandler {

    void addSeries(ISystemId entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addEndFile(ISystemId entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addExpandToCaseFile(ISystemId entity,
                             IHateoasNoarkObject hateoasNoarkObject);

    void addExpandToMeetingFile(ISystemId entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addRecord(ISystemId entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addNewRecord(ISystemId entity,
                      IHateoasNoarkObject hateoasNoarkObject);

    void addComment(ISystemId entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewComment(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addParentFile(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addSubFile(ISystemId entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewSubFile(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addCrossReference(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewCrossReference(ISystemId entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addClass(ISystemId entity,
                  IHateoasNoarkObject hateoasNoarkObject);

    void addNewClass(ISystemId entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceSeries(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewReferenceSeries(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceSecondaryClassification(
            ISystemId entity,
            IHateoasNoarkObject hateoasNoarkObject);

    void addNewReferenceSecondaryClassification(
            ISystemId entity,
            IHateoasNoarkObject hateoasNoarkObject);

    void addPart(ISystemId entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartPerson(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartUnit(ISystemId entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    // Add national identifiers

    void addNewBuilding(ISystemId entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewCadastralUnit(ISystemId entity,
                             IHateoasNoarkObject hateoasNoarkObject);

    void addNewDNumber(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewPlan(ISystemId entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewPosition(ISystemId entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewSocialSecurityNumber(ISystemId entity,
                                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewUnit(ISystemId entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNationalIdentifier(ISystemId entity,
                     IHateoasNoarkObject hateoasNoarkObject);
}
