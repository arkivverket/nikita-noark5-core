package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler for ClassificationSystem
 */
public interface IClassificationSystemHateoasHandler extends IHateoasHandler {

    void addSeries(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addClass(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewClass(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSecondaryClassificationSystem(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewSecondaryClassificationSystem(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addClassificationType(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
}
