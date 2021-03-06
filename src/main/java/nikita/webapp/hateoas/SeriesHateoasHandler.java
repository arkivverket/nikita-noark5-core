package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.ISeriesHateoasHandler;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Used to add SeriesHateoas links with Series specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for various CRUD operations so keeping
 * possibility of separate calls for CRUD functions, at the moment. See HateoasHandler
 * <p>
 * Some of these will require ROLE_RECORD_KEEPER  e.g. screening etc., others will readable for all users.
 * <p>
 * StorageLocation should only be possible if documentMedium is not electronic
 * <p>
 * StorageLocation supports addOne, addAll, findAll
 */
@Component("seriesHateoasHandler")
public class SeriesHateoasHandler
        extends SystemIdHateoasHandler
        implements ISeriesHateoasHandler {

    public SeriesHateoasHandler() {
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        addClassifiedCodeMetadata(entity, hateoasNoarkObject);
        addDeletionType(entity, hateoasNoarkObject);
        addDocumentMedium(entity, hateoasNoarkObject);
        addNewRegistration(entity, hateoasNoarkObject);
        addNewFile(entity, hateoasNoarkObject);
        addNewCaseFile(entity, hateoasNoarkObject);
        addNewClassificationSystem(entity, hateoasNoarkObject);
        addRegistration(entity, hateoasNoarkObject);
        addFile(entity, hateoasNoarkObject);
        addCaseFile(entity, hateoasNoarkObject);
        addClassificationSystem(entity, hateoasNoarkObject);
        addSeriesSuccessor(entity, hateoasNoarkObject);
        addSeriesPrecursor(entity, hateoasNoarkObject);
        addFonds(entity, hateoasNoarkObject);
        addSeriesStatus(entity, hateoasNoarkObject);
        addNewStorageLocation(entity, hateoasNoarkObject);
        addStorageLocation(entity, hateoasNoarkObject);
        addAccessRestriction(entity, hateoasNoarkObject);
        addScreeningDocument(entity, hateoasNoarkObject);
        addScreeningMetadata(entity, hateoasNoarkObject);
        addScreeningMetadataLocal(entity, hateoasNoarkObject);
        addNewScreeningMetadataLocal(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(
            ISystemId entity,
            IHateoasNoarkObject hateoasNoarkObject) {
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        addClassifiedCodeMetadata(entity, hateoasNoarkObject);
        addDeletionType(entity, hateoasNoarkObject);
        addDocumentMedium(entity, hateoasNoarkObject);
        addSeriesStatus(entity, hateoasNoarkObject);
        addAccessRestriction(entity, hateoasNoarkObject);
        addScreeningDocument(entity, hateoasNoarkObject);
        addScreeningMetadata(entity, hateoasNoarkObject);
    }

    @Override
    /**
     * Get a list of Series status values (GET)
     */
    public void addSeriesStatus(ISystemId entity,
                                IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() +
                        HREF_BASE_METADATA + SLASH + SERIES_STATUS,
                        REL_METADATA_SERIES_STATUS, true));
    }

    /**
     * Create a REL/HREF pair for the Series associated with the given Series
     * as the given Series successor. Checks if the Series actually has an
     * associated successor. In the example below 5431 is the systemID of the
     * successor Series object.
     * <p>
     * "../api/arkivstruktur/arkivdel/5431"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/nestearkivdel/"
     *
     * @param entity             series
     * @param hateoasNoarkObject hateoasSeries
     */
    @Override
    public void addSeriesSuccessor(ISystemId entity,
                                   IHateoasNoarkObject hateoasNoarkObject) {
        Series series = getSeries(entity);
        if (series.getReferenceSuccessor() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_SERIES + SLASH + series.getReferenceSuccessor().getSystemId(),
                    REL_FONDS_STRUCTURE_SUCCESSOR));
        }
    }

    /**
     * Create a REL/HREF pair for the Series associated with the given Series
     * as the given Series precursor. Checks if the Series actually has an
     * associated precursor. In the example below 2345 is the systemID of the
     * precursor Series object.
     * <p>
     * "../api/arkivstruktur/arkivdel/2345"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/forrigearkivdel/"
     *
     * @param entity             series
     * @param hateoasNoarkObject hateoasSeries
     */
    @Override
    public void addSeriesPrecursor(ISystemId entity,
                                   IHateoasNoarkObject hateoasNoarkObject) {
        Series series = getSeries(entity);
        if (series.getReferencePrecursor() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_SERIES + SLASH + series.getReferencePrecursor().getSystemId(),
                    REL_FONDS_STRUCTURE_PRECURSOR));
        }
    }

    @Override
    /**
     * Add a new registration to a Series (POST)
     */
    public void addNewRegistration(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (authorisation.canCreateRegistrationAttachedToSeries()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_SERIES + SLASH + entity.getSystemId() + SLASH + NEW_RECORD + SLASH,
                    REL_FONDS_STRUCTURE_NEW_RECORD,
                    false));
        }
    }

    @Override
    /**
     * Add a new File to a Series (POST)
     */
    public void addNewFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (authorisation.canCreateFileAttachedToSeries()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_SERIES + SLASH + entity.getSystemId() + SLASH + NEW_FILE + SLASH,
                    REL_FONDS_STRUCTURE_NEW_FILE, false));
        }
    }

    @Override
    /**
     * Add a new CaseFile to a Series (POST)
     */
    public void addNewCaseFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (authorisation.canCreateFileAttachedToSeries()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_SERIES + SLASH + entity.getSystemId() + SLASH + NEW_CASE_FILE + SLASH,
                    REL_CASE_HANDLING_NEW_CASE_FILE, false));
        }
    }

    @Override
    /**
     * Associate an existing ClassificationSystem as the precursor of an existing Series (PUT)
     */
    public void addNewClassificationSystem(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (authorisation.canCreateClassifcationSystemAttachedToSeries()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_SERIES + SLASH + entity.getSystemId() + SLASH + NEW_CLASSIFICATION_SYSTEM + SLASH,
                    REL_FONDS_STRUCTURE_NEW_CLASSIFICATION_SYSTEM, false));
        }
    }

    @Override
    /**
     * Get a list of Registration objects associated with a Series (paginated) (GET)
     */
    public void addRegistration(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_SERIES + SLASH + entity.getSystemId() + SLASH + RECORD + SLASH,
                REL_FONDS_STRUCTURE_RECORD,
                false));
    }

    @Override
    /**
     * Get a list of File objects associated with a Series (paginated) (GET)
     */
    public void addFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_SERIES + SLASH + entity.getSystemId() + SLASH + FILE + SLASH,
                REL_FONDS_STRUCTURE_FILE,
                false));
    }

    @Override
    /**
     * Get a list of CaseFile objects associated with a Series (paginated) (GET)
     */
    public void addCaseFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_SERIES + SLASH + entity.getSystemId() + SLASH + CASE_FILE + SLASH,
                REL_CASE_HANDLING_CASE_FILE,
                false));
    }

    @Override
    /**
     * Get the ClassificationSystem associated with the Series (GET)
     */
    public void addClassificationSystem(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_SERIES + SLASH + entity.getSystemId() + SLASH + CLASSIFICATION_SYSTEM + SLASH,
                REL_FONDS_STRUCTURE_CLASSIFICATION_SYSTEM, false));

    }

    @Override
    /**
     * Create a REL/HREF pair for the Fonds associated with the
     * given Series. Checks if the Fonds is actually associated with a
     * Series. It should not be possible to have a series without a parent
     * Fonds. Note as this has to exist, we return a link to the actual parent
     * identified by its systemId
     * <p>
     * "../api/arkivstruktur/arkiv/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/"
     *
     * @param entity             series
     * @param hateoasNoarkObject hateoasSeries
     */
    public void addFonds(ISystemId entity,
                         IHateoasNoarkObject hateoasNoarkObject) {
        Series series = getSeries(entity);
        if (series.getReferenceFonds() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_FONDS + SLASH +
                            series.getReferenceFonds().getSystemId(),
                            REL_FONDS_STRUCTURE_FONDS));
        }
    }

    @Override
    public void addStorageLocation(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (((Series) entity).getReferenceStorageLocation().size() > 0) {
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
    public void addDeletionType(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + DELETION_TYPE,
                REL_METADATA_DELETION_TYPE, false));
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
        if (null != ((Series) entity).getReferenceScreening()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_FONDS_STRUCTURE + SLASH + SERIES + SLASH +
                    entity.getSystemId() + SLASH + SCREENING_METADATA,
                    REL_FONDS_STRUCTURE_SCREENING_METADATA));
        }
    }

    @Override
    public void addNewScreeningMetadataLocal(ISystemId entity,
                                             IHateoasNoarkObject hateoasNoarkObject) {
        if (null != ((Series) entity).getReferenceScreening()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_FONDS_STRUCTURE + SLASH + SERIES + SLASH +
                    entity.getSystemId() + SLASH + NEW_SCREENING_METADATA,
                    REL_FONDS_STRUCTURE_NEW_SCREENING_METADATA));
        }
    }

    /**
     * Cast the ISystemId entity to a Series
     *
     * @param entity the Series
     * @return a Series object
     */
    private Series getSeries(@NotNull ISystemId entity) {
        return (Series) entity;
    }
}
