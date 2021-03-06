package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
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
        addStorageLocation(entity, hateoasNoarkObject);
        addNewStorageLocation(entity, hateoasNoarkObject);
        //addComment(entity, hateoasNoarkObject);
        addNewComment(entity, hateoasNoarkObject);
        addComment(entity, hateoasNoarkObject);
        addNewKeyword(entity, hateoasNoarkObject);
        addKeyword(entity, hateoasNoarkObject);
        //addCrossReference(entity, hateoasNoarkObject);
        addNewCrossReference(entity, hateoasNoarkObject);
        addClassifiedCodeMetadata(entity, hateoasNoarkObject);
        addDocumentMedium(entity, hateoasNoarkObject);
        addAccessRestriction(entity, hateoasNoarkObject);
        addScreeningDocument(entity, hateoasNoarkObject);
        addScreeningMetadata(entity, hateoasNoarkObject);
        addScreeningMetadataLocal(entity, hateoasNoarkObject);
        addNewScreeningMetadataLocal(entity, hateoasNoarkObject);

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
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        addClassifiedCodeMetadata(entity, hateoasNoarkObject);
        addDocumentMedium(entity, hateoasNoarkObject);
        addAccessRestriction(entity, hateoasNoarkObject);
        addScreeningDocument(entity, hateoasNoarkObject);
        addScreeningMetadata(entity, hateoasNoarkObject);
    }

    @Override
    public void addComment(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + COMMENT + SLASH,
                REL_FONDS_STRUCTURE_COMMENT, false));
    }

    @Override
    public void addNewComment(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_COMMENT + SLASH,
                REL_FONDS_STRUCTURE_NEW_COMMENT));
    }

    @Override
    public void addKeyword(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (((RecordEntity) entity).getReferenceKeyword().size() > 0) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() +
                    SLASH + KEYWORD + SLASH, REL_FONDS_STRUCTURE_KEYWORD, true));
        }
    }

    @Override
    public void addNewKeyword(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_KEYWORD + SLASH,
                REL_FONDS_STRUCTURE_NEW_KEYWORD, false));
    }

    @Override
    public void addStorageLocation(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (((RecordEntity) entity).getReferenceStorageLocation().size() > 0) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() +
                    SLASH + STORAGE_LOCATION + SLASH,
                    REL_FONDS_STRUCTURE_STORAGE_LOCATION, true));
        }
    }

    @Override
    public void addNewStorageLocation(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() +
                SLASH + NEW_STORAGE_LOCATION + SLASH,
                REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION, false));
    }

    @Override
    public void addCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_CROSS_REFERENCE + SLASH,
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
        RecordEntity record = getRecord(entity);
        if (record.getReferenceSeries() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_SERIES + SLASH + record.getReferenceSeries().getSystemIdAsString(),
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
        RecordEntity record = getRecord(entity);
        if (record.getReferenceFile() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_FILE + SLASH +
                            record.getReferenceFile().getSystemIdAsString(),
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
        RecordEntity record = getRecord(entity);
        if (record.getReferenceClass() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_CLASS + SLASH + record.getReferenceClass().getSystemIdAsString(),
                    REL_FONDS_STRUCTURE_CLASS));
        }
    }

    @Override
    public void addNewDocumentDescription(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_DOCUMENT_DESCRIPTION + SLASH,
                REL_FONDS_STRUCTURE_NEW_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addDocumentDescription(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + DOCUMENT_DESCRIPTION + SLASH,
                REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addNewReferenceSeries(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + REFERENCE_NEW_SERIES + SLASH,
                REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES, false));
    }

    @Override
    public void addNewCorrespondencePartPerson(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_CORRESPONDENCE_PART_PERSON + SLASH,
                REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_PERSON));
    }

    @Override
    public void addCorrespondencePart(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + CORRESPONDENCE_PART + SLASH,
                REL_FONDS_STRUCTURE_CORRESPONDENCE_PART, true));
    }

    @Override
    public void addNewCorrespondencePartUnit(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_CORRESPONDENCE_PART_UNIT + SLASH,
                REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_UNIT));
    }

    @Override
    public void addPart(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + PART + SLASH,
                REL_FONDS_STRUCTURE_PART, true));
    }

    @Override
    public void addNewPartPerson(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_PART_PERSON + SLASH,
                REL_FONDS_STRUCTURE_NEW_PART_PERSON));
    }

    @Override
    public void addNewPartUnit(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_PART_UNIT + SLASH,
                REL_FONDS_STRUCTURE_NEW_PART_UNIT));
    }

    @Override
    public void addNewCorrespondencePartInternal(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        /*
        Temporary disabled as it causes problems for clients.
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FONDS_STRUCTURE + SLASH + RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_CORRESPONDENCE_PART_INTERNAL + SLASH,
                REL_FONDS_STRUCTURE_NEW_CORRESPONDENCE_PART_INTERNAL, false));

         */
    }

    /**
     * Create a REL/HREF pair to get the list of Author objects associated with
     * the given Record.
     * <p>
     * "../api/arkivstruktur/registrering/{systemID}/forfatter"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/forfatter/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addAuthor(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject) {
        if (((RecordEntity) entity).getReferenceAuthor().size() > 0) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() +
                    SLASH + AUTHOR + SLASH, REL_FONDS_STRUCTURE_AUTHOR, true));
        }
    }

    /**
     * Create a REL/HREF pair to create a new Author object associated with
     * the given Record.
     * <p>
     * "../api/arkivstruktur/registrering/{systemID}/ny-forfatter"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-forfatter/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addNewAuthor(ISystemId entity,
                             IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() +
                SLASH + NEW_AUTHOR + SLASH, REL_FONDS_STRUCTURE_NEW_AUTHOR));
    }

    @Override
    public void addNewBuilding(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_BUILDING,
                REL_FONDS_STRUCTURE_NEW_BUILDING));
    }

    @Override
    public void addNewCadastralUnit(ISystemId entity,
                                    IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_CADASTRAL_UNIT,
                REL_FONDS_STRUCTURE_NEW_CADASTRAL_UNIT));
    }

    @Override
    public void addNewDNumber(ISystemId entity,
                              IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_D_NUMBER,
                REL_FONDS_STRUCTURE_NEW_D_NUMBER));
    }

    @Override
    public void addNewPlan(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_PLAN,
                REL_FONDS_STRUCTURE_NEW_PLAN));
    }

    @Override
    public void addNewPosition(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_POSITION,
                REL_FONDS_STRUCTURE_NEW_POSITION));
    }

    @Override
    public void addNewSocialSecurityNumber(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_SOCIAL_SECURITY_NUMBER,
                REL_FONDS_STRUCTURE_NEW_SOCIAL_SECURITY_NUMBER));
    }

    @Override
    public void addNewUnit(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NEW_NI_UNIT,
                REL_FONDS_STRUCTURE_NEW_NI_UNIT));
    }

    @Override
    public void addNationalIdentifier(ISystemId entity,
                                      IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_RECORD + SLASH + entity.getSystemIdAsString() + SLASH + NATIONAL_IDENTIFIER,
                REL_FONDS_STRUCTURE_NATIONAL_IDENTIFIER));
    }


    @Override
    public void addClassifiedCodeMetadata(ISystemId entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + CLASSIFIED_CODE,
                REL_METADATA_CLASSIFIED_CODE));
    }

    @Override
    public void addAccessRestriction(ISystemId entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + ACCESS_RESTRICTION,
                REL_METADATA_ACCESS_RESTRICTION, false));
    }

    @Override
    public void addScreeningDocument(ISystemId entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + SCREENING_DOCUMENT,
                REL_METADATA_SCREENING_DOCUMENT, false));
    }

    @Override
    public void addScreeningMetadata(ISystemId entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + SCREENING_METADATA,
                REL_METADATA_SCREENING_METADATA));
    }

    @Override
    public void addScreeningMetadataLocal(ISystemId entity,
                                          IHateoasNoarkObject hateoasNoarkObject) {
        if (null != ((RecordEntity) entity).getReferenceScreening()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_FONDS_STRUCTURE + SLASH + RECORD + SLASH +
                    entity.getSystemId() + SLASH + SCREENING_METADATA,
                    REL_FONDS_STRUCTURE_SCREENING_METADATA));
        }
    }

    @Override
    public void addNewScreeningMetadataLocal(ISystemId entity,
                                             IHateoasNoarkObject hateoasNoarkObject) {
        if (null != ((RecordEntity) entity).getReferenceScreening()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_FONDS_STRUCTURE + SLASH + RECORD + SLASH +
                    entity.getSystemId() + SLASH + NEW_SCREENING_METADATA,
                    REL_FONDS_STRUCTURE_NEW_SCREENING_METADATA));
        }
    }

    /**
     * Cast the ISystemId entity to a Record
     *
     * @param entity the Record
     * @return a Record object
     */
    private RecordEntity getRecord(@NotNull ISystemId entity) {
        return (RecordEntity) entity;
    }
}
