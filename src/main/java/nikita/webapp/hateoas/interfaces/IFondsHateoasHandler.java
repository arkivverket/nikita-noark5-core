package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IFondsHateoasHandler extends IHateoasHandler {

    void addFondsCreator(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSeries(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addFonds(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addParentFonds(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewSubFonds(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewFondsCreator(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSubFonds(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addFondsStatus(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewSeries(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
}
