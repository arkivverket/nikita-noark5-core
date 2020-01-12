package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler for ClassificationSystem
 */
public interface IClassificationSystemHateoasHandler
        extends IHateoasHandler {

    void addSeries(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSecondaryClassificationSystem(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewSecondaryClassificationSystem(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addClassificationType(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);
}
