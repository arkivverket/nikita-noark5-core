package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler for Class
 */
public interface IClassHateoasHandler extends IHateoasHandler {

    void addRegistration(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewRegistration(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewCaseFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addClassificationSystem(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addParentClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSubClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addAccessRestriction(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addDisposalDecision(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

}
