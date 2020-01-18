package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IRecordHateoasHandler
        extends IHateoasHandler {

    void addReferenceSeries(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceFile(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceClass(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentDescription(ISystemId entity,
                                   IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentDescription(ISystemId entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addNewReferenceSeries(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addStorageLocation(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewStorageLocation(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addComment(ISystemId entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewComment(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addCrossReference(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject);

    void addNewCrossReference(ISystemId entity,
                              IHateoasNoarkObject hateoasNoarkObject);

    void addPart(ISystemId entity,
                 IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartPerson(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewPartUnit(ISystemId entity,
                        IHateoasNoarkObject hateoasNoarkObject);

    void addCorrespondencePart(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject);

    void addNewCorrespondencePartPerson(ISystemId entity,
                                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewCorrespondencePartUnit(ISystemId entity,
                                      IHateoasNoarkObject hateoasNoarkObject);

    void addNewCorrespondencePartInternal(ISystemId entity,
                                          IHateoasNoarkObject hateoasNoarkObject);

    void addAuthor(ISystemId entity,
                   IHateoasNoarkObject hateoasNoarkObject);

    void addNewAuthor(ISystemId entity,
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
