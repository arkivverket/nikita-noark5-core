package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add DocumentDescriptionHateoas links with DocumentDescription
 * specific information
 */
@Component("documentDescriptionHateoasHandler")
public class DocumentDescriptionHateoasHandler
        extends SystemIdHateoasHandler
        implements IDocumentDescriptionHateoasHandler {

    public DocumentDescriptionHateoasHandler() {
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {

        // links for primary entities
        addRecord(entity, hateoasNoarkObject);
        addDocumentObject(entity, hateoasNoarkObject);
        addNewDocumentObject(entity, hateoasNoarkObject);
        // links for secondary entities M:1
        addPart(entity, hateoasNoarkObject);
        addNewPartPerson(entity, hateoasNoarkObject);
        addNewPartUnit(entity, hateoasNoarkObject);
        // links for secondary entities 1:M
        addStorageLocation(entity, hateoasNoarkObject);
        addNewStorageLocation(entity, hateoasNoarkObject);
        addComment(entity, hateoasNoarkObject);
        addNewComment(entity, hateoasNoarkObject);
        addAuthor(entity, hateoasNoarkObject);
        addNewAuthor(entity, hateoasNoarkObject);
        // links for metadata entities
        addDocumentMedium(entity, hateoasNoarkObject);
        addDocumentType(entity, hateoasNoarkObject);
        addDocumentStatus(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(ISystemId entity,
                                         IHateoasNoarkObject hateoasNoarkObject) {
        addDocumentMedium(entity, hateoasNoarkObject);
        addDocumentType(entity, hateoasNoarkObject);
        addDocumentStatus(entity, hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the parent Record associated
     * with the given DocumentDescription
     * <p>
     * "../api/arkivstruktur/dokumentbeskrivelse/1234/registrering"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/"
     *
     * @param entity             documentDescription
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addRecord(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH +
                RECORD, REL_FONDS_STRUCTURE_RECORD, false));
    }

    @Override
    public void addComment(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH + COMMENT + SLASH,
                REL_FONDS_STRUCTURE_COMMENT, false));
    }

    @Override
    public void addNewComment(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH + NEW_COMMENT + SLASH,
                REL_FONDS_STRUCTURE_NEW_COMMENT, false));
    }

    @Override
    public void addStorageLocation(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH + STORAGE_LOCATION + SLASH,
                REL_FONDS_STRUCTURE_STORAGE_LOCATION, false));
    }

    @Override
    public void addNewStorageLocation(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH + NEW_STORAGE_LOCATION + SLASH,
                REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION, false));
    }

    @Override
    public void addDocumentObject(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH + DOCUMENT_OBJECT + SLASH,
                REL_FONDS_STRUCTURE_DOCUMENT_OBJECT, false));
    }

    @Override
    public void addNewDocumentObject(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH + NEW_DOCUMENT_OBJECT + SLASH,
                REL_FONDS_STRUCTURE_NEW_DOCUMENT_OBJECT, false));
    }

    @Override
    public void addDocumentType(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + DOCUMENT_STATUS,
                REL_METADATA_DOCUMENT_STATUS, false));
    }

    @Override
    public void addDocumentStatus(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + DOCUMENT_TYPE,
                REL_METADATA_DOCUMENT_TYPE, false));
    }

    /**
     * Create a REL/HREF pair for the list of Part objects associated with the
     * given DocumentDescription.
     * <p>
     * "../api/arkivstruktur/dokumentbeskrivelse/1234/part"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/part/"
     *
     * @param entity             documentDescription
     * @param hateoasNoarkObject hateoasDocumentDescription
     */
    @Override
    public void addPart(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH + PART + SLASH,
                REL_FONDS_STRUCTURE_PART, true));
    }

    @Override
    public void addNewPartPerson(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH + NEW_PART_PERSON + SLASH,
                REL_FONDS_STRUCTURE_NEW_PART_PERSON));
    }

    @Override
    public void addNewPartUnit(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() + SLASH + NEW_PART_UNIT + SLASH,
                REL_FONDS_STRUCTURE_NEW_PART_UNIT));
    }

    /**
     * Create a REL/HREF pair to get the list of Author objects associated with
     * the given DocumentDescription.
     * <p>
     * "../api/arkivstruktur/dokumentbeskrivelse/1234/forfatter"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/forfatter/"
     *
     * @param entity             documentDescription
     * @param hateoasNoarkObject hateoasDocumentDescription
     */
    @Override
    public void addAuthor(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() +
                SLASH + AUTHOR + SLASH, REL_FONDS_STRUCTURE_AUTHOR, true));
    }

    /**
     * Create a REL/HREF pair to create a new Author object associated with
     * the given DocumentDescription.
     * <p>
     * "../api/arkivstruktur/dokumentbeskrivelse/1234/ny-forfatter"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-forfatter/"
     *
     * @param entity             documentDescription
     * @param hateoasNoarkObject hateoasDocumentDescription
     */
    @Override
    public void addNewAuthor(ISystemId entity,
                             IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + entity.getSystemId() +
                SLASH + NEW_AUTHOR + SLASH, REL_FONDS_STRUCTURE_NEW_AUTHOR));
    }
}
