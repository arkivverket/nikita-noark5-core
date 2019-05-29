package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.interfaces.ISeriesHateoasHandler;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
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
public class SeriesHateoasHandler extends HateoasHandler
        implements ISeriesHateoasHandler {

    public SeriesHateoasHandler() {
    }

    @Override
    public void addEntityLinks(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
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
        addNewSeriesSuccessor(entity, hateoasNoarkObject);
        addSeriesPrecursor(entity, hateoasNoarkObject);
        addNewSeriesPrecursor(entity, hateoasNoarkObject);
        addFonds(entity, hateoasNoarkObject);
        addSeriesStatus(entity, hateoasNoarkObject);
        addClassified(entity, hateoasNoarkObject);
        addNewClassified(entity, hateoasNoarkObject);
        addDisposal(entity, hateoasNoarkObject);
        addNewDisposal(entity, hateoasNoarkObject);
        addDisposalUndertaken(entity, hateoasNoarkObject);
        addNewDisposalUndertaken(entity, hateoasNoarkObject);
        addDeletion(entity, hateoasNoarkObject);
        addNewDeletion(entity, hateoasNoarkObject);
        addScreening(entity, hateoasNoarkObject);
        addNewScreening(entity, hateoasNoarkObject);
        addNewStorageLocation(entity, hateoasNoarkObject);
        addListStorageLocation(entity, hateoasNoarkObject);
        addNewListStorageLocation(entity, hateoasNoarkObject);
    }

    @Override
    /**
     * Get a list of Series status values (GET)
     */
    public void addSeriesStatus(INikitaEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() +
                        HREF_METADATA_PATH + SERIES_STATUS,
                        REL_METADATA_SERIES_STATUS, true));
    }

    /**
     * Create a REL/HREF pair for the Series associated with the given Series
     * as the given Series successor. Checks if the Series actually has an
     * associated successor. In the example below 5431 is the systemID of the
     * successor Series object.
     * <p>
     * "../hateoas-api/arkivstruktur/arkiv/5431"
     * "https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/nestearkivdel/"
     *
     * @param entity             series
     * @param hateoasNoarkObject hateoasSeries
     */
    @Override
    public void addSeriesSuccessor(INikitaEntity entity,
                                   IHateoasNoarkObject hateoasNoarkObject) {
        Series series = getSeries(entity);
        if (series.getReferenceSuccessor() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_SERIES + series.getReferenceSuccessor().getSystemId(),
                    REL_FONDS_STRUCTURE_SUCCESSOR));
        }
    }

    @Override
    /**
     * Associate an existing Series (A) as the successor of another existing Series (B). (A) becomes the
     * successor to (B). A is identified first, B is identified through a ref link (PUT)
     */
    public void addNewSeriesSuccessor(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() +
                        HREF_BASE_SERIES + entity.getSystemId() + SLASH
                        + NEW_SERIES_SUCCESSOR + SLASH, REL_FONDS_STRUCTURE_NEW_SUCCESSOR,
                        false));
    }

    /**
     * Create a REL/HREF pair for the Series associated with the given Series
     * as the given Series precursor. Checks if the Series actually has an
     * associated precursor. In the example below 2345 is the systemID of the
     * precursor Series object.
     * <p>
     * "../hateoas-api/arkivstruktur/arkivdel/2345"
     * "https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/forrigearkivdel/"
     *
     * @param entity             series
     * @param hateoasNoarkObject hateoasSeries
     */
    @Override
    public void addSeriesPrecursor(INikitaEntity entity,
                                   IHateoasNoarkObject hateoasNoarkObject) {
        Series series = getSeries(entity);
        if (series.getReferencePrecursor() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_SERIES +
                            series.getReferencePrecursor().getSystemId(),
                            REL_FONDS_STRUCTURE_PRECURSOR));
        }
    }

    @Override
    /**
     * Associate an existing Series (A) as the precursor  of another existing Series (B). (A) becomes the
     * precursor to (B). A is identified first, B is identified through a ref link (PUT)
     */
    public void addNewSeriesPrecursor(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH +
                NEW_SERIES_PRECURSOR + SLASH, REL_FONDS_STRUCTURE_NEW_PRECURSOR,
                false));
    }

    @Override
    /**
     * Add a new registration to a Series (POST)
     */
    public void addNewRegistration(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (authorisation.canCreateRegistrationAttachedToSeries()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                    NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH +
                    NEW_RECORD + SLASH, REL_FONDS_STRUCTURE_NEW_REGISTRATION,
                    false));
        }
    }

    @Override
    /**
     * Add a new File to a Series (POST)
     */
    public void addNewFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (authorisation.canCreateFileAttachedToSeries()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                    NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH
                    + NEW_FILE + SLASH, REL_FONDS_STRUCTURE_NEW_FILE, false));
        }
    }

    @Override
    /**
     * Add a new CaseFile to a Series (POST)
     */
    public void addNewCaseFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (authorisation.canCreateFileAttachedToSeries()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                    NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH
                    + NEW_CASE_FILE + SLASH, REL_FONDS_STRUCTURE_NEW_CASE_FILE, false));
        }
    }

    @Override
    /**
     * Associate an existing ClassificationSystem as the precursor of an existing Series (PUT)
     */
    public void addNewClassificationSystem(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (authorisation.canCreateClassifcationSystemAttachedToSeries()) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                    NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES +
                    SLASH + entity.getSystemId() + SLASH + NEW_CLASSIFICATION_SYSTEM + SLASH,
                    REL_FONDS_STRUCTURE_NEW_CLASSIFICATION_SYSTEM, false));
        }
    }

    @Override
    /**
     * Get a list of Registration objects associated with a Series (paginated) (GET)
     */
    public void addRegistration(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES +
                SLASH + entity.getSystemId() + SLASH + REGISTRATION + SLASH, REL_FONDS_STRUCTURE_REGISTRATION,
                false));
    }

    @Override
    /**
     * Get a list of File objects associated with a Series (paginated) (GET)
     */
    public void addFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES +
                SLASH + entity.getSystemId() + SLASH + FILE + SLASH, REL_FONDS_STRUCTURE_FILE,
                false));
    }

    @Override
    /**
     * Get a list of CaseFile objects associated with a Series (paginated) (GET)
     */
    public void addCaseFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES +
                SLASH + entity.getSystemId() + SLASH + CASE_FILE + SLASH, REL_FONDS_STRUCTURE_CASE_FILE,
                false));
    }

    @Override
    /**
     * Get the ClassificationSystem associated with the Series (GET)
     */
    public void addClassificationSystem(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES +
                SLASH + entity.getSystemId() + SLASH + CLASSIFICATION_SYSTEM + SLASH,
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
     * "../hateoas-api/arkivstruktur/arkiv/1234"
     * "https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/arkiv/"
     *
     * @param entity             series
     * @param hateoasNoarkObject hateoasSeries
     */
    public void addFonds(INikitaEntity entity,
                         IHateoasNoarkObject hateoasNoarkObject) {
        Series series = getSeries(entity);
        if (series.getReferenceFonds() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_FONDS +
                            series.getReferenceFonds().getSystemId(),
                            REL_FONDS_STRUCTURE_FONDS));
        }
    }

    @Override
    /**
     * Get the Classified associated with the Series (GET)
     */
    public void addClassified(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH + CLASSIFIED
                + SLASH, REL_FONDS_STRUCTURE_CLASSIFIED, false));
    }

    @Override
    /**
     * Add a new Classified to a Series (POST)
     */
    public void addNewClassified(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH +
                NEW_CLASSIFIED + SLASH, REL_FONDS_STRUCTURE_NEW_CLASSIFIED, false));
    }

    @Override
    /**
     * Get the Disposal associated with the Series (GET)
     */
    public void addDisposal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH + DISPOSAL
                + SLASH, REL_FONDS_STRUCTURE_DISPOSAL, false));
    }

    @Override
    /**
     * Add a new Disposal to a Series (POST)
     */
    public void addNewDisposal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH + NEW_DISPOSAL
                + SLASH, REL_FONDS_STRUCTURE_NEW_DISPOSAL, false));
    }

    @Override
    /**
     * Get the DisposalUndertaken associated with the Series (GET)
     */
    public void addDisposalUndertaken(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH + DISPOSAL_UNDERTAKEN
                + SLASH, REL_FONDS_STRUCTURE_DISPOSAL_UNDERTAKEN, false));
    }

    @Override
    /**
     * Add a new DisposalUndertaken to a Series (POST)
     */
    public void addNewDisposalUndertaken(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH + NEW_DISPOSAL_UNDERTAKEN
                + SLASH, REL_FONDS_STRUCTURE_NEW_DISPOSAL_UNDERTAKEN, false));
    }

    @Override
    /**
     * Get the Deletion associated with the Series object (GET)
     */
    public void addDeletion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH + DELETION
                + SLASH, REL_FONDS_STRUCTURE_DELETION, false));
    }

    @Override
    /**
     * Add a new Deletion to a Series (POST)
     */
    public void addNewDeletion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH + NEW_DELETION
                + SLASH, REL_FONDS_STRUCTURE_NEW_DELETION, false));
    }

    @Override
    /**
     * Get the Screening associated with the Series (GET)
     */
    public void addScreening(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH + SCREENING
                + SLASH, REL_FONDS_STRUCTURE_SCREENING, false));
    }

    @Override
    /**
     * Add a new Screening to a Series (POST)
     */
    public void addNewScreening(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH + NEW_SCREENING
                + SLASH, REL_FONDS_STRUCTURE_NEW_SCREENING, false));
    }

    @Override
    /**
     * Get a list of StorageLocation associated with the Series  (GET)
     */
    public void addListStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH +
                STORAGE_LOCATION + SLASH, REL_FONDS_STRUCTURE_STORAGE_LOCATION, false));
    }

    @Override
    /**
     * Add a new StorageLocation to be associated with the Series (POST)
     */
    public void addNewStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH +
                NEW_STORAGE_LOCATION + SLASH, REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION, false));
    }

    @Override
    /**
     * Add a new list of StorageLocation to be associated with the Series (POST)
     */
    public void addNewListStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES + SLASH + entity.getSystemId() + SLASH +
                NEW_STORAGE_LOCATIONS + SLASH, REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION_LIST, false));
    }

    /**
     * Cast the INikitaEntity entity to a Series
     *
     * @param entity the Series
     * @return a Series object
     */
    private Series getSeries(@NotNull INikitaEntity entity) {
        return (Series) entity;
    }
}
