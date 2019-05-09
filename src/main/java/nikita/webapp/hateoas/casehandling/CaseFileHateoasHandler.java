package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.hateoas.Link;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.FileHateoasHandler;
import nikita.webapp.hateoas.interfaces.ICaseFileHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add CaseFileHateoas links with CaseFile specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for various CRUD operations so keeping them
 * separate calls at the moment.
 */
// TODO : Find out about how to handle secondary entities. They should be returned embedded within the primary
//        entity, but updating / adding will require rel/hrefs
//        Temporarily removing displaying secondary entities, but leaving the addNew
//        Commenting out rather than deleting them because we are unsure if they are needed or not
//        It seems that secondary entities are generated as hateoas links if they have odata support
//        So we could reintroduce them when we get odata support
@Component("caseFileHateoasHandler")
public class CaseFileHateoasHandler extends FileHateoasHandler implements ICaseFileHateoasHandler {

    @Override
    public void addEntityLinks(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        // Not calling  super.addEntityLinks(entity, hateoasNoarkObject, outgoingAddress);
        // because addExpandToCaseFile, addExpandToMeetingFile, addSubFile, addNewSubFile
        // are not applicable. Instead we invoke the methods directly here.
        // Methods from base class
        addEndFile(entity, hateoasNoarkObject, outgoingAddress);
        addRegistration(entity, hateoasNoarkObject, outgoingAddress);
        addNewRegistration(entity, hateoasNoarkObject, outgoingAddress);
        addBasicRecord(entity, hateoasNoarkObject, outgoingAddress);
        addNewBasicRecord(entity, hateoasNoarkObject, outgoingAddress);
        addComment(entity, hateoasNoarkObject, outgoingAddress);
        addNewComment(entity, hateoasNoarkObject, outgoingAddress);
        addCrossReference(entity, hateoasNoarkObject, outgoingAddress);
        addNewCrossReference(entity, hateoasNoarkObject, outgoingAddress);
        addClass(entity, hateoasNoarkObject, outgoingAddress);
        addNewClass(entity, hateoasNoarkObject, outgoingAddress);
        addReferenceSeries(entity, hateoasNoarkObject, outgoingAddress);
        addNewReferenceSeries(entity, hateoasNoarkObject, outgoingAddress);
        addReferenceSecondaryClassification(entity, hateoasNoarkObject, outgoingAddress);
        addNewReferenceSecondaryClassification(entity, hateoasNoarkObject, outgoingAddress);
        // Methods from this class
        //addClass(entity, hateoasNoarkObject, outgoingAddress);
        addNewPrecedence(entity, hateoasNoarkObject, outgoingAddress);
        //addPrecedence(entity, hateoasNoarkObject, outgoingAddress);
        addNewCaseParty(entity, hateoasNoarkObject, outgoingAddress);
        //addCaseParty(entity, hateoasNoarkObject, outgoingAddress);
        addNewCaseStatus(entity, hateoasNoarkObject, outgoingAddress);
        //addCaseStatus(entity, hateoasNoarkObject, outgoingAddress);
        addNewRegistryEntry(entity, hateoasNoarkObject, outgoingAddress);
        addRegistryEntry(entity, hateoasNoarkObject, outgoingAddress);
        //addSecondaryClassification(entity, hateoasNoarkObject, outgoingAddress);
    }

    @Override
    public void addNewClass(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_CLASS
                + SLASH, REL_CASE_HANDLING_NEW_CLASS,
                false));
    }

    @Override
    public void addClass(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH + CLASS +
                SLASH, REL_CASE_HANDLING_CLASS, false));
    }

    @Override
    public void addNewPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_PRECEDENCE
                + SLASH, REL_CASE_HANDLING_NEW_PRECEDENCE, false));
    }

    @Override
    public void addPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH +
                PRECEDENCE + SLASH, REL_CASE_HANDLING_PRECEDENCE, false));
    }

    @Override
    public void addNewCaseParty(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_CASE_PARTY
                + SLASH, REL_CASE_HANDLING_NEW_CASE_PARTY, false));
    }

    @Override
    public void addCaseParty(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH + CASE_PARTY
                + SLASH, REL_CASE_HANDLING_CASE_PARTY, false));
    }

    @Override
    public void addCaseStatus(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH + CASE_STATUS
                + SLASH, REL_METADATA_CASE_STATUS, false));
    }

    @Override
    public void addNewCaseStatus(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_CASE_STATUS
                + SLASH, REL_METADATA_CASE_STATUS, false));
    }

    @Override
    public void addSecondaryClassification(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH +
                SECONDARY_CLASSIFICATION + SLASH, REL_CASE_HANDLING_SECONDARY_CLASSIFICATION, false));
    }

    @Override
    public void addNewSecondaryClassification(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_SECONDARY_CLASSIFICATION + SLASH, REL_CASE_HANDLING_NEW_SECONDARY_CLASSIFICATION, false));
    }

    @Override
    public void addRegistryEntry(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH +
                REGISTRY_ENTRY + SLASH, REL_CASE_HANDLING_REGISTRY_ENTRY, false));
    }

    @Override
    public void addNewRegistryEntry(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_CASE_HANDLING_PATH + SLASH + CASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_REGISTRY_ENTRY + SLASH, REL_CASE_HANDLING_NEW_REGISTRY_ENTRY, false));
    }
}
