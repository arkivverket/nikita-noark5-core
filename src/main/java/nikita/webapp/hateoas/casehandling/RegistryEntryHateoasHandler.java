package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.hateoas.Link;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.BasicRecordHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRegistryEntryHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add BasicRecordHateoas links with BasicRecord specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for various CRUD operations so keeping them
 * separate calls at the moment.
 */
@Component("registryEntryHateoasHandler")
public class RegistryEntryHateoasHandler extends BasicRecordHateoasHandler implements IRegistryEntryHateoasHandler {

    @Override
    public void addEntityLinks(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {

        super.addEntityLinks(entity, hateoasNoarkObject, outgoingAddress);
        addElectronicSignature(entity, hateoasNoarkObject, outgoingAddress);
        addNewElectronicSignature(entity, hateoasNoarkObject, outgoingAddress);
        addPrecedence(entity, hateoasNoarkObject, outgoingAddress);
        addNewPrecedence(entity, hateoasNoarkObject, outgoingAddress);
        addSignOff(entity, hateoasNoarkObject, outgoingAddress);
        addNewSignOff(entity, hateoasNoarkObject, outgoingAddress);
        addDocumentFlow(entity, hateoasNoarkObject, outgoingAddress);
        addNewDocumentFlow(entity, hateoasNoarkObject, outgoingAddress);
        addCorrespondencePartPerson(entity, hateoasNoarkObject, outgoingAddress);
        addNewCorrespondencePartPerson(entity, hateoasNoarkObject, outgoingAddress);
        addCorrespondencePartUnit(entity, hateoasNoarkObject, outgoingAddress);
        addNewCorrespondencePartUnit(entity, hateoasNoarkObject, outgoingAddress);
        addCorrespondencePartInternal(entity, hateoasNoarkObject, outgoingAddress);
        addNewCorrespondencePartInternal(entity, hateoasNoarkObject, outgoingAddress);
    }

    @Override
    public void addElectronicSignature(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + ELECTRONIC_SIGNATURE + SLASH, REL_FONDS_STRUCTURE_ELECTRONIC_SIGNATURE, false));
    }

    @Override
    public void addNewElectronicSignature(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + NEW_ELECTRONIC_SIGNATURE + SLASH, REL_FONDS_STRUCTURE_NEW_ELECTRONIC_SIGNATURE, false));
    }

    @Override
    public void addPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + PRECEDENCE + SLASH, REL_FONDS_STRUCTURE_PRECEDENCE, false));
    }

    @Override
    public void addNewPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + NEW_PRECEDENCE + SLASH, REL_FONDS_STRUCTURE_NEW_PRECEDENCE, false));
    }

    @Override
    public void addSignOff(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + SIGN_OFF + SLASH, REL_FONDS_STRUCTURE_SIGN_OFF, false));
    }

    @Override
    public void addNewSignOff(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + NEW_SIGN_OFF + SLASH, REL_FONDS_STRUCTURE_NEW_SIGN_OFF, false));
    }

    @Override
    public void addDocumentFlow(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + DOCUMENT_FLOW + SLASH, REL_FONDS_STRUCTURE_DOCUMENT_FLOW, false));
    }

    @Override
    public void addNewDocumentFlow(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + NEW_DOCUMENT_FLOW + SLASH, REL_FONDS_STRUCTURE_NEW_DOCUMENT_FLOW, false));
    }

    @Override
    public void addCorrespondencePartPerson(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + CORRESPONDENCE_PART_PERSON + SLASH,
                REL_CASE_HANDLING_CORRESPONDENCE_PART_PERSON, false));
    }

    @Override
    public void addNewCorrespondencePartPerson(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + NEW_CORRESPONDENCE_PART_PERSON + SLASH,
                REL_CASE_HANDLING_NEW_CORRESPONDENCE_PART_PERSON, false));
    }

    @Override
    public void addCorrespondencePartUnit(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + CORRESPONDENCE_PART_UNIT + SLASH,
                REL_CASE_HANDLING_CORRESPONDENCE_PART_UNIT, false));
    }

    @Override
    public void addNewCorrespondencePartUnit(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + NEW_CORRESPONDENCE_PART_UNIT + SLASH,
                REL_CASE_HANDLING_NEW_CORRESPONDENCE_PART_UNIT, false));
    }

    @Override
    public void addCorrespondencePartInternal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + CORRESPONDENCE_PART_INTERNAL + SLASH,
                REL_CASE_HANDLING_CORRESPONDENCE_PART_INTERNAL, false));
    }

    @Override
    public void addNewCorrespondencePartInternal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + REGISTRY_ENTRY + SLASH +
                entity.getSystemId() + SLASH + NEW_CORRESPONDENCE_PART_INTERNAL + SLASH,
                REL_CASE_HANDLING_NEW_CORRESPONDENCE_PART_INTERNAL, false));
    }
}
