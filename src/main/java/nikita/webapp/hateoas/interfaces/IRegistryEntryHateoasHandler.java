package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IRegistryEntryHateoasHandler extends IBasicRecordHateoasHandler {

    void addElectronicSignature(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewElectronicSignature(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addSignOff(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewSignOff(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addDocumentFlow(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewDocumentFlow(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addCorrespondencePartPerson(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addCorrespondencePartUnit(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addCorrespondencePartInternal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewCorrespondencePartPerson(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewCorrespondencePartUnit(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

    void addNewCorrespondencePartInternal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress);

}
