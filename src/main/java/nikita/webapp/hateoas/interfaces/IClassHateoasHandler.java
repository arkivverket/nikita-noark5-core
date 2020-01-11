package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler for Class
 */
public interface IClassHateoasHandler extends IHateoasHandler {

    void addRegistration(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewRegistration(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addClassificationSystem(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addParentClass(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSubClass(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addClass(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewClass(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addCrossReference(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewCrossReference(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addAccessRestriction(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addDisposalDecision(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

}
