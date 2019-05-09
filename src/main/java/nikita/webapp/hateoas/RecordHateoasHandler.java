package nikita.webapp.hateoas;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.hateoas.Link;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add RecordHateoas links with Record specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for various CRUD operations so keeping them
 * separate calls at the moment.
 */
@Component("recordHateoasHandler")
public class RecordHateoasHandler extends HateoasHandler implements IRecordHateoasHandler {

    public RecordHateoasHandler(String contextPath) {
        super(contextPath);
    }

    public RecordHateoasHandler() {
    }

    @Override
    public void addEntityLinks(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {

        addReferenceSeries(entity, hateoasNoarkObject, outgoingAddress);
        addNewDocumentDescription(entity, hateoasNoarkObject, outgoingAddress);
        addDocumentDescription(entity, hateoasNoarkObject, outgoingAddress);
        addNewDocumentObject(entity, hateoasNoarkObject, outgoingAddress);
        addDocumentObject(entity, hateoasNoarkObject, outgoingAddress);
        addNewReferenceSeries(entity, hateoasNoarkObject, outgoingAddress);
        addClassified(entity, hateoasNoarkObject, outgoingAddress);
        addNewClassified(entity, hateoasNoarkObject, outgoingAddress);
        addDisposal(entity, hateoasNoarkObject, outgoingAddress);
        addNewDisposal(entity, hateoasNoarkObject, outgoingAddress);
        addDisposalUndertaken(entity, hateoasNoarkObject, outgoingAddress);
        addNewDisposalUndertaken(entity, hateoasNoarkObject, outgoingAddress);
        addDeletion(entity, hateoasNoarkObject, outgoingAddress);
        addNewDeletion(entity, hateoasNoarkObject, outgoingAddress);
        addScreening(entity, hateoasNoarkObject, outgoingAddress);
        addNewScreening(entity, hateoasNoarkObject, outgoingAddress);
    }

    @Override
    public void addReferenceSeries(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH + 
                REFERENCE_SERIES + SLASH, REL_FONDS_STRUCTURE_REFERENCE_SERIES, false));
    }

    @Override
    public void addNewDocumentDescription(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH + 
                NEW_DOCUMENT_DESCRIPTION + SLASH, REL_FONDS_STRUCTURE_NEW_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addDocumentDescription(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                DOCUMENT_DESCRIPTION + SLASH, REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addNewDocumentObject(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_DOCUMENT_OBJECT + SLASH, REL_FONDS_STRUCTURE_NEW_DOCUMENT_OBJECT, false));
    }

    @Override
    public void addDocumentObject(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH
                + DOCUMENT_OBJECT + SLASH, REL_FONDS_STRUCTURE_DOCUMENT_OBJECT, false));
    }

    @Override
    public void addNewReferenceSeries(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                REFERENCE_NEW_SERIES + SLASH, REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES, false));
    }

    @Override
    public void addClassified(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                CLASSIFIED + SLASH, REL_FONDS_STRUCTURE_CLASSIFIED, false));
    }

    @Override
    public void addNewClassified(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_CLASSIFIED + SLASH, REL_FONDS_STRUCTURE_NEW_CLASSIFIED, false));
    }

    @Override
    public void addDisposal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                DISPOSAL + SLASH, REL_FONDS_STRUCTURE_DISPOSAL, false));
    }

    @Override
    public void addNewDisposal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_DISPOSAL + SLASH, REL_FONDS_STRUCTURE_NEW_DISPOSAL, false));
    }

    @Override
    public void addDisposalUndertaken(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                DISPOSAL_UNDERTAKEN + SLASH, REL_FONDS_STRUCTURE_DISPOSAL_UNDERTAKEN, false));
    }

    @Override
    public void addNewDisposalUndertaken(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_DISPOSAL_UNDERTAKEN + SLASH, REL_FONDS_STRUCTURE_NEW_DISPOSAL_UNDERTAKEN, false));
    }

    @Override
    public void addDeletion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                DELETION + SLASH, REL_FONDS_STRUCTURE_DELETION, false));
    }

    @Override
    public void addNewDeletion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_DELETION + SLASH, REL_FONDS_STRUCTURE_NEW_DELETION, false));
    }

    @Override
    public void addScreening(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                SCREENING + SLASH, REL_FONDS_STRUCTURE_SCREENING, false));
    }

    @Override
    public void addNewScreening(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_SCREENING + SLASH, REL_FONDS_STRUCTURE_NEW_SCREENING, false));
    }
}
