package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface ICaseFileHateoasHandler
        extends IHateoasHandler {

    void addNewClass(ISystemId entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addClass(ISystemId entity,
                  IHateoasNoarkObject hateoasNoarkObject);

    void addNewPrecedence(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addPrecedence(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addSecondaryClassification(ISystemId entity,
                                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewSecondaryClassification(ISystemId entity,
                                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewRegistryEntry(ISystemId entity,
                             IHateoasNoarkObject hateoasNoarkObject);

    void addRegistryEntry(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewRecordNote(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addRecordNote(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewSubCaseFile(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject);
}
