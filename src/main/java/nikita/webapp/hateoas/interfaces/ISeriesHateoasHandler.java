package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler for Series
 */
public interface ISeriesHateoasHandler 
        extends IHateoasHandler {

    void addNewRegistration(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewFile(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewCaseFile(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewClassificationSystem(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addSeriesSuccessor(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addSeriesPrecursor(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addRegistration(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addFile(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addCaseFile(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addClassificationSystem(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addFonds(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addSeriesStatus(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addNewStorageLocation(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addListStorageLocation(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

}
