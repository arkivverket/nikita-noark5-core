package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IFondsHateoasHandler extends IHateoasHandler {

    void addFondsCreator(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addSeries(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addFonds(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewSubFonds(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewFondsCreator(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addSubFonds(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addFondsStatus(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewFonds(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewSeries(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

}
