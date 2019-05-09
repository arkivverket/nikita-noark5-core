package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IDocumentDescriptionHateoasHandler extends IHateoasHandler {

    void addRecord(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addDocumentObject(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewDocumentObject(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addClassified(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewClassified(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addDisposal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewDisposal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addDisposalUndertaken(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewDisposalUndertaken(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addDeletion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewDeletion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addScreening(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewScreening(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addAuthor(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewAuthor(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addDocumentType(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addDocumentStatus(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

}

