package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
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
        extends HateoasHandler
        implements IRecordHateoasHandler {

    public RecordHateoasHandler() {
    }

    @Override
    public void addEntityLinks(INikitaEntity entity,
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
    }

    @Override
    public void addEntityLinksOnTemplate(
            INikitaEntity entity,
            IHateoasNoarkObject hateoasNoarkObject) {
        addDocumentMedium(entity, hateoasNoarkObject);
    }


    @Override
    public void addComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + COMMENT + SLASH,
                REL_FONDS_STRUCTURE_COMMENT, false));
    }

    @Override
    public void addNewComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_COMMENT + SLASH,
                REL_FONDS_STRUCTURE_NEW_COMMENT, false));
    }

    @Override
    public void addStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + STORAGE_LOCATION + SLASH,
                REL_FONDS_STRUCTURE_STORAGE_LOCATION, false));
    }

    @Override
    public void addNewStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_STORAGE_LOCATION + SLASH,
                REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION, false));
    }

    @Override
    public void addCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE, false));
    }


    /**
     * Create a REL/HREF pair for the parent Series associated with the given
     * Record. Checks if the Record is actually associated with a Series.
     * <p>
     * "../hateoas-api/arkivstruktur/arkivdel/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addReferenceSeries(INikitaEntity entity,
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
     * "../hateoas-api/arkivstruktur/mappe/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/mappe/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addReferenceFile(INikitaEntity entity,
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
     * "../hateoas-api/arkivstruktur/klasse/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addReferenceClass(INikitaEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject) {
        Record record = getRecord(entity);
        if (record.getReferenceClass() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_CLASS + SLASH + record.getReferenceClass().getSystemId(),
                    REL_FONDS_STRUCTURE_CLASS));
        }
    }

    @Override
    public void addNewDocumentDescription(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_DOCUMENT_DESCRIPTION + SLASH,
                REL_FONDS_STRUCTURE_NEW_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addDocumentDescription(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + DOCUMENT_DESCRIPTION + SLASH,
                REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addNewReferenceSeries(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + REFERENCE_NEW_SERIES + SLASH,
                REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES, false));
    }

    @Override
    public void addNewCorrespondencePartPerson(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_CORRESPONDENCE_PART_PERSON + SLASH,
                REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_PERSON));
    }

    @Override
    public void addCorrespondencePart(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + CORRESPONDENCE_PART + SLASH,
                REL_FONDS_STRUCTURE_CORRESPONDENCE_PART, true));
    }

    @Override
    public void addNewCorrespondencePartUnit(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_CORRESPONDENCE_PART_UNIT + SLASH,
                REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_UNIT));
    }

    @Override
    public void addPart(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + PART + SLASH,
                REL_FONDS_STRUCTURE_PART, true));
    }

    @Override
    public void addNewPartPerson(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_PART_PERSON + SLASH,
                REL_FONDS_STRUCTURE_NEW_PART_PERSON));
    }

    @Override
    public void addNewPartUnit(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
		HREF_BASE_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_PART_UNIT + SLASH,
                REL_FONDS_STRUCTURE_NEW_PART_UNIT));
    }

    @Override
    public void addNewCorrespondencePartInternal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
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
     * "../hateoas-api/arkivstruktur/dokumentbeskrivelse/1234/forfatter"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/forfatter/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addAuthor(INikitaEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() +
                SLASH + AUTHOR + SLASH, REL_FONDS_STRUCTURE_AUTHOR, true));
    }

    /**
     * Create a REL/HREF pair to create a new Author object associated with
     * the given Record.
     * <p>
     * "../hateoas-api/arkivstruktur/dokumentbeskrivelse/1234/ny-forfatter"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-forfatter/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addNewAuthor(INikitaEntity entity,
                             IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemId() +
                SLASH + NEW_AUTHOR + SLASH, REL_FONDS_STRUCTURE_NEW_AUTHOR));
    }

    /**
     * Cast the INikitaEntity entity to a Record
     *
     * @param entity the Record
     * @return a Record object
     */
    private Record getRecord(@NotNull INikitaEntity entity) {
        return (Record) entity;
    }
}
