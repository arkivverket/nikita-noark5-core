package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IBasicRecordHateoasHandler extends IRecordHateoasHandler {

    void addStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addAuthor(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewAuthor(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addKeyword(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewKeyword(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);
}
