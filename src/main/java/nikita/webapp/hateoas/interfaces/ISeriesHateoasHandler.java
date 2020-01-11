package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler for Series
 */
public interface ISeriesHateoasHandler extends IHateoasHandler {

    void addNewRegistration(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewClassificationSystem(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSeriesSuccessor(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewSeriesSuccessor(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSeriesPrecursor(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewSeriesPrecursor(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addRegistration(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addClassificationSystem(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addFonds(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSeriesStatus(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewStorageLocation(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addListStorageLocation(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

}
