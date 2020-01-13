package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IFondsHateoasHandler
        extends IHateoasHandler {

    void addFondsCreator(ISystemId entity, 
                         IHateoasNoarkObject hateoasNoarkObject);

    void addSeries(ISystemId entity, 
                   IHateoasNoarkObject hateoasNoarkObject);

    void addFonds(ISystemId entity, 
                  IHateoasNoarkObject hateoasNoarkObject);

    void addParentFonds(ISystemId entity, 
                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewSubFonds(ISystemId entity, 
                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewFondsCreator(ISystemId entity, 
                            IHateoasNoarkObject hateoasNoarkObject);

    void addSubFonds(ISystemId entity, 
                     IHateoasNoarkObject hateoasNoarkObject);

    void addFondsStatus(ISystemId entity, 
                        IHateoasNoarkObject hateoasNoarkObject);

    void addNewSeries(ISystemId entity, 
                      IHateoasNoarkObject hateoasNoarkObject);
}
