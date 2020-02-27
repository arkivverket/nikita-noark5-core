package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add RecordHateoas links with Record specific information
 */
@Component("recordHateoasHandler")
public class RecordHateoasHandler
        extends SystemIdHateoasHandler
        implements IRecordHateoasHandler {

    public RecordHateoasHandler() {
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        // Add the child links
        addDocumentDescription(entity, hateoasNoarkObject);
        addNewDocumentDescription(entity, hateoasNoarkObject);
        // Add the parent links
        addReferenceSeries(entity, hateoasNoarkObject);
        addReferenceFile(entity, hateoasNoarkObject);
        addReferenceClass(entity, hateoasNoarkObject);
        // Add the secondary entity links
        // Part
        addPart(entity, hateoasNoarkObject);
        addNewPartPerson(entity, hateoasNoarkObject);
        addNewPartUnit(entity, hateoasNoarkObject);
        // CorrespondencePart
        addCorrespondencePart(entity, hateoasNoarkObject);
        addNewCorrespondencePartPerson(entity, hateoasNoarkObject);
        addNewCorrespondencePartUnit(entity, hateoasNoarkObject);
        addNewCorrespondencePartInternal(entity, hateoasNoarkObject);
        //addStorageLocation(entity, hateoasNoarkObject);
        addNewStorageLocation(entity, hateoasNoarkObject);
        //addComment(entity, hateoasNoarkObject);
        addNewComment(entity, hateoasNoarkObject);
        //addCrossReference(entity, hateoasNoarkObject);
        addNewCrossReference(entity, hateoasNoarkObject);
        // Add national identifiers
        addNewBuilding(entity, hateoasNoarkObject);
        addNewCadastralUnit(entity, hateoasNoarkObject);
        addNewDNumber(entity, hateoasNoarkObject);
        addNewPlan(entity, hateoasNoarkObject);
        addNewPosition(entity, hateoasNoarkObject);
        addNewSocialSecurityNumber(entity, hateoasNoarkObject);
        addNewUnit(entity, hateoasNoarkObject);
        addNationalIdentifier(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(
            ISystemId entity,
            IHateoasNoarkObject hateoasNoarkObject) {
        addDocumentMedium(entity, hateoasNoarkObject);
    }


    @Override
    public void addComment(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + COMMENT + SLASH,
                REL_FONDS_STRUCTURE_COMMENT, false));
    }

    @Override
    public void addNewComment(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_COMMENT + SLASH,
                REL_FONDS_STRUCTURE_NEW_COMMENT, false));
    }

    @Override
    public void addStorageLocation(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + STORAGE_LOCATION + SLASH,
                REL_FONDS_STRUCTURE_STORAGE_LOCATION, false));
    }

    @Override
    public void addNewStorageLocation(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_STORAGE_LOCATION + SLASH,
                REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION, false));
    }

    @Override
    public void addCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE, false));
    }


    /**
     * Create a REL/HREF pair for the parent Series associated with the given
     * Record. Checks if the Record is actually associated with a Series.
     * <p>
     * "../api/arkivstruktur/arkivdel/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addReferenceSeries(ISystemId entity,
                                   IHateoasNoarkObject hateoasNoarkObject) {
        Record record = getRecord(entity);
        if (record.getReferenceSeries() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_SERIES + SLASH + record.getReferenceSeries().getSystemId(),
                    REL_FONDS_STRUCTURE_SERIES));
        }
    }

    /**
     * Create a REL/HREF pair for the parent File associated with the given
     * Record. Checks if the Record is actually associated with a File.
     * <p>
     * "../api/arkivstruktur/mappe/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/mappe/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addReferenceFile(ISystemId entity,
                                 IHateoasNoarkObject hateoasNoarkObject) {
        Record record = getRecord(entity);
        if (record.getReferenceFile() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_FILE + SLASH +
                            record.getReferenceFile().getSystemId(),
                            REL_FONDS_STRUCTURE_FILE));
        }
    }

    /**
     * Create a REL/HREF pair for the parent Class associated with the given
     * Record. Checks if the Record is actually associated with a Class.
     * <p>
     * "../api/arkivstruktur/klasse/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addReferenceClass(ISystemId entity,
                                  IHateoasNoarkObject hateoasNoarkObject) {
        Record record = getRecord(entity);
        if (record.getReferenceClass() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_CLASS + SLASH + record.getReferenceClass().getSystemId(),
                    REL_FONDS_STRUCTURE_CLASS));
        }
    }

    @Override
    public void addNewDocumentDescription(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_DOCUMENT_DESCRIPTION + SLASH,
                REL_FONDS_STRUCTURE_NEW_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addDocumentDescription(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + DOCUMENT_DESCRIPTION + SLASH,
                REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addNewReferenceSeries(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + REFERENCE_NEW_SERIES + SLASH,
                REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES, false));
    }

    @Override
    public void addNewCorrespondencePartPerson(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_CORRESPONDENCE_PART_PERSON + SLASH,
                REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_PERSON));
    }

    @Override
    public void addCorrespondencePart(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + CORRESPONDENCE_PART + SLASH,
                REL_FONDS_STRUCTURE_CORRESPONDENCE_PART, true));
    }

    @Override
    public void addNewCorrespondencePartUnit(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_CORRESPONDENCE_PART_UNIT + SLASH,
                REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_UNIT));
    }

    @Override
    public void addPart(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + PART + SLASH,
                REL_FONDS_STRUCTURE_PART, true));
    }

    @Override
    public void addNewPartPerson(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_PART_PERSON + SLASH,
                REL_FONDS_STRUCTURE_NEW_PART_PERSON));
    }

    @Override
    public void addNewPartUnit(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_PART_UNIT + SLASH,
                REL_FONDS_STRUCTURE_NEW_PART_UNIT));
    }

    @Override
    public void addNewCorrespondencePartInternal(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        /*
        Temporary disabled as it causes problems for clients.
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FONDS_STRUCTURE + SLASH + RECORD + SLASH + entity.getSystemId() + SLASH + NEW_CORRESPONDENCE_PART_INTERNAL + SLASH,
                REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_INTERNAL, false));

         */
    }

    /**
     * Create a REL/HREF pair to get the list of Author objects associated with
     * the given Record.
     * <p>
     * "../api/arkivstruktur/dokumentbeskrivelse/1234/forfatter"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/forfatter/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addAuthor(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() +
                SLASH + AUTHOR + SLASH, REL_FONDS_STRUCTURE_AUTHOR, true));
    }

    /**
     * Create a REL/HREF pair to create a new Author object associated with
     * the given Record.
     * <p>
     * "../api/arkivstruktur/dokumentbeskrivelse/1234/ny-forfatter"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-forfatter/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addNewAuthor(ISystemId entity,
                             IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() +
                SLASH + NEW_AUTHOR + SLASH, REL_FONDS_STRUCTURE_NEW_AUTHOR));
    }

    @Override
    public void addNewBuilding(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_BUILDING,
                REL_FONDS_STRUCTURE_NEW_BUILDING));
    }

    @Override
    public void addNewCadastralUnit(ISystemId entity,
                                    IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_CADASTRAL_UNIT,
                REL_FONDS_STRUCTURE_NEW_CADASTRAL_UNIT));
    }

    @Override
    public void addNewDNumber(ISystemId entity,
                              IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_D_NUMBER,
                REL_FONDS_STRUCTURE_NEW_D_NUMBER));
    }

    @Override
    public void addNewPlan(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_PLAN,
                REL_FONDS_STRUCTURE_NEW_PLAN));
    }

    @Override
    public void addNewPosition(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_POSITION,
                REL_FONDS_STRUCTURE_NEW_POSITION));
    }

    @Override
    public void addNewSocialSecurityNumber(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_SOCIAL_SECURITY_NUMBER,
                REL_FONDS_STRUCTURE_NEW_SOCIAL_SECURITY_NUMBER));
    }

    @Override
    public void addNewUnit(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_NI_UNIT,
                REL_FONDS_STRUCTURE_NEW_NI_UNIT));
    }

    @Override
    public void addNationalIdentifier(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NATIONAL_IDENTIFIER,
                REL_FONDS_STRUCTURE_NATIONAL_IDENTIFIER));
    }

    /**
     * Cast the ISystemId entity to a Record
     *
     * @param entity the Record
     * @return a Record object
     */
    private Record getRecord(@NotNull ISystemId entity) {
        return (Record) entity;
    }
}
