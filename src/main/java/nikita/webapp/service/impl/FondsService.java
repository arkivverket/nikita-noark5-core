package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.common.repository.n5v5.IFondsRepository;
import nikita.common.util.exceptions.NoarkEntityEditWhenClosedException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.common.util.exceptions.NoarkInvalidStructureException;
import nikita.webapp.hateoas.interfaces.IFondsCreatorHateoasHandler;
import nikita.webapp.hateoas.interfaces.IFondsHateoasHandler;
import nikita.webapp.hateoas.interfaces.ISeriesHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IFondsCreatorService;
import nikita.webapp.service.interfaces.IFondsService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.DatabaseConstants.DELETE_FONDS_STORAGE_LOCATION;
import static nikita.common.config.DatabaseConstants.DELETE_FROM_FONDS_FONDS_CREATOR;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class FondsService
        extends NoarkService
        implements IFondsService {

    private static final Logger logger =
            LoggerFactory.getLogger(FondsService.class);

    private IFondsRepository fondsRepository;
    private SeriesService seriesService;
    private IMetadataService metadataService;
    private IFondsCreatorService fondsCreatorService;
    private IFondsHateoasHandler fondsHateoasHandler;
    private ISeriesHateoasHandler seriesHateoasHandler;
    private IFondsCreatorHateoasHandler fondsCreatorHateoasHandler;

    public FondsService(EntityManager entityManager,
                        ApplicationEventPublisher applicationEventPublisher,
                        IFondsRepository fondsRepository,
                        SeriesService seriesService,
                        IMetadataService metadataService,
                        IFondsCreatorService fondsCreatorService,
                        IFondsHateoasHandler fondsHateoasHandler,
                        ISeriesHateoasHandler seriesHateoasHandler,
                        IFondsCreatorHateoasHandler
                                fondsCreatorHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.fondsRepository = fondsRepository;
        this.seriesService = seriesService;
        this.metadataService = metadataService;
        this.fondsCreatorService = fondsCreatorService;
        this.entityManager = entityManager;
        this.fondsHateoasHandler = fondsHateoasHandler;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.fondsCreatorHateoasHandler = fondsCreatorHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new fonds object to the database. Some values are set in the
     * incoming payload (e.g. title) and some are set by the core.
     * owner, createdBy, createdDate are automatically set by the core.
     *
     * @param fonds fonds object with some values set
     * @return the newly persisted fonds object wrapped as a fondsHateaos object
     */
    @Override
    public FondsHateoas createNewFonds(@NotNull Fonds fonds) {
        validateDocumentMedium(metadataService, fonds);
        if (null == fonds.getFondsStatus()) {
            FondsStatus fondsStatus = (FondsStatus)
                metadataService.findValidMetadataByEntityTypeOrThrow
                    (FONDS_STATUS, FONDS_STATUS_OPEN_CODE, null);
            fonds.setFondsStatus(fondsStatus);
        }
        checkFondsStatusUponCreation(fonds);
        setFinaliseEntityValues(fonds);
        FondsHateoas fondsHateoas = new FondsHateoas(fondsRepository.save(fonds));
        fondsHateoasHandler.addLinks(fondsHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityCreatedEvent(this, fonds));
        return fondsHateoas;
    }

    /**
     * Persists a new fonds object to the database, that is first associated
     * with a parent fonds object. Some values are set in the incoming
     * payload (e.g. title) and some are set by the core. owner, createdBy,
     * createdDate are automatically set by the core.
     * <p>
     * First we try to locate the parent. If the parent does not exist a
     * NoarkEntityNotFoundException exception is thrown
     *
     * @param childFonds          incoming fonds object with some values set
     * @param parentFondsSystemId The systemId of the parent fonds
     * @return the newly persisted fonds object
     */
    @Override
    public FondsHateoas createFondsAssociatedWithFonds(
            @NotNull String parentFondsSystemId,
            @NotNull Fonds childFonds) {

        Fonds parentFonds = getFondsOrThrow(parentFondsSystemId);
        checkFondsDoesNotContainSeries(parentFonds);
        childFonds.setReferenceParentFonds(parentFonds);
        return createNewFonds(childFonds);
    }

    /**
     * Persists a new Series object to the database. Some values are set in
     * the incoming payload (e.g. title) and some are set by the core. owner,
     * createdBy, createdDate are automatically set by the core.
     * <p>
     * First we try to locate the fonds to associate the Series with. If the
     * fonds does not exist a NoarkEntityNotFoundException exception is
     * thrown. Then we check that the fonds does not have children fonds. If it
     * does an NoarkInvalidStructureException exception is thrown. After that
     * we check that the Fonds object is not already closed.
     *
     * @param fondsSystemId The systemId of the fonds object to associate a
     *                      Series with
     * @param series        The incoming Series object
     * @return the newly persisted series object wrapped as a SeriesHateoas
     * object
     */
    @Override
    public SeriesHateoas createSeriesAssociatedWithFonds(
            @NotNull String fondsSystemId,
            @NotNull Series series) {

        Fonds fonds = getFondsOrThrow(fondsSystemId);

        seriesService.updateSeriesReferences(series);
        seriesService.checkSeriesStatusUponCreation(series);
        checkFondsNotClosed(fonds);
        checkFondsDoesNotContainSubFonds(fonds);

        series.setReferenceFonds(fonds);
        SeriesHateoas seriesHateoas = new SeriesHateoas(seriesService.save
                (series));
        seriesHateoasHandler.addLinks(seriesHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityCreatedEvent(this, fonds));
        return seriesHateoas;
    }

    /**
     * Persists a new FondsCreator object to the database. Some values are set
     * in the incoming payload (e.g. fonds_creator_name) and some are set by
     * the core. owner,createdBy, createdDate are automatically set by the core.
     * <p>
     * First we try to locate the fonds to associate the FondsCreator with. If
     * the fonds does not exist a NoarkEntityNotFoundException exception is
     * thrown. After that we check that the Fonds object is not already closed.
     *
     * @param fondsSystemId The systemId of the fonds object you wish to
     *                      associate the fondsCreator object with
     * @param fondsCreator  incoming fondsCreator object with some values set
     * @return the newly persisted fondsCreator object wrapped as a
     * FondsCreatorHateoas object
     */
    @Override
    public FondsCreatorHateoas createFondsCreatorAssociatedWithFonds(
            @NotNull String fondsSystemId,
            @NotNull FondsCreator fondsCreator) {

        Fonds fonds = getFondsOrThrow(fondsSystemId);
        checkFondsNotClosed(fonds);

        fondsCreatorService.createNewFondsCreator(fondsCreator);

        // add references to objects in both directions
        fondsCreator.addFonds(fonds);
        fonds.getReferenceFondsCreator().add(fondsCreator);

        // create the hateoas object with links
        FondsCreatorHateoas fondsCreatorHateoas = new FondsCreatorHateoas
                (fondsCreator);
        fondsCreatorHateoasHandler.addLinks(fondsCreatorHateoas, new
                Authorisation());

        return fondsCreatorHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of Series objects associated with a given Fonds
     * from the database. First we try to locate the Fonds object. If the
     * Fonds object does not exist a NoarkEntityNotFoundException exception
     * is thrown that the caller has to deal with.
     * <p>
     * If any Series objects exist, they are wrapped in a SeriesHateoas
     * object and returned to the caller.
     *
     * @param fondsSystemId The systemId of the Fonds object that you want to
     *                      retrieve associated Series objects
     * @return the list of Series objects wrapped as a SeriesHateoas object
     *
     */
    @Override
    public SeriesHateoas findSeriesAssociatedWithFonds(
            @NotNull String fondsSystemId) {

        Fonds fonds = getFondsOrThrow(fondsSystemId);
        SeriesHateoas seriesHateoas = new
                SeriesHateoas((List<INoarkEntity>)
                (List) fonds.getReferenceSeries());

        seriesHateoasHandler.addLinks(seriesHateoas,
                new Authorisation());
        return seriesHateoas;
    }

    /**
     * Retrieve a list of StorageLocation objects associated with a given Fonds
     * from the database. First we try to locate the Fonds object. If the
     * Fonds object does not exist a NoarkEntityNotFoundException exception
     * is thrown that the caller has to deal with.
     *
     * If any StorageLocation objects exist, they are wrapped in a
     * StorageLocationHateoas object and returned to the caller.
     *
     * @param fondsSystemId The systemId of the Fonds object that you want to
     *                      retrieve associated StorageLocation objects
     *
     * @return the newly persisted fondsCreator object wrapped as a
     * StorageLocationHateoas object
     *
     */
    /*@Override
    TODO: Finish implementing this.
    public StorageLocationHateoas findStorageLocationAssociatedWithFonds(
            @NotNull String fondsSystemId) {

        Fonds fonds = getFondsOrThrow(fondsSystemId);

        StorageLocationHateoas stroageLocationHateoas = new
                StorageLocationHateoas((List<INoarkEntity>)
                (List) fonds.getReferenceStorageLocation());
        fondsCreatorHateoasHandler.addLinks(stroageLocationHateoas,
                new Authorisation());
        return stroageLocationHateoas;
    } */

    /**
     * Retrieve a single Fonds objects from the database.
     *
     * @param fondsSystemId The systemId of the Fonds object you wish to
     *                      retrieve
     * @return the Fonds object wrapped as a FondsHateoas object
     */
    @Override
    public FondsHateoas findSingleFonds(String fondsSystemId) {
        Fonds existingFonds = getFondsOrThrow(fondsSystemId);

        FondsHateoas fondsHateoas = new FondsHateoas(fondsRepository.
                save(existingFonds));
        fondsHateoasHandler.addLinks(fondsHateoas, new Authorisation());
        return fondsHateoas;
    }

    /**
     * Retrieve a list of paginated Fonds objects associated from the database.
     *
     * @param top  how many results you want to retrieve
     * @param skip how many rows of results yo uwant to skip over
     * @return the list of Fonds object wrapped as a FondsCreatorHateoas object
     */
    @Override
    public FondsHateoas findFondsByOwnerPaginated(Integer top, Integer skip) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Fonds> criteriaQuery = criteriaBuilder.createQuery(Fonds.class);
        Root<Fonds> from = criteriaQuery.from(Fonds.class);
        CriteriaQuery<Fonds> select = criteriaQuery.select(from);

        criteriaQuery.where(criteriaBuilder.equal(from.get("ownedBy"), getUser()));
        TypedQuery<Fonds> typedQuery = entityManager.createQuery(select);

        FondsHateoas fondsHateoas = new
                FondsHateoas((List<INoarkEntity>)
                (List) typedQuery.getResultList());
        fondsHateoasHandler.addLinks(fondsHateoas, new Authorisation());
        return fondsHateoas;
    }

    /**
     * Retrieve a list of children fonds belonging to the fonds object
     * identified by systemId
     *
     * @param systemId The systemId of the Fonds object to retrieve its children
     * @return A FondsHateoas object containing the children fonds's
     */
    @Override
    @SuppressWarnings("unchecked")
    public FondsHateoas findAllChildren(@NotNull String systemId) {
        FondsHateoas fondsHateoas = new
                FondsHateoas((List<INoarkEntity>)
                (List) getFondsOrThrow(systemId).getReferenceChildFonds());
        fondsHateoasHandler.addLinks(fondsHateoas, new Authorisation());
        return fondsHateoas;
    }

    /**
     * Retrieve a list of FondsCreator objects associated with a given Fonds
     * from the database. First we try to locate the Fonds object. If the
     * Fonds object does not exist a NoarkEntityNotFoundException exception
     * is thrown that the caller has to deal with.
     * <p>
     * If any FondsCreator objects exist, they are wrapped in a
     * FondsCreatorHateoas object and returned to the caller.
     *
     * @param systemId The systemId of the Fonds object that you want to
     *                 retrieve associated FondsCreator objects
     * @return the fondsCreator objects wrapped as a FondsCreatorHateoas object
     */
    @Override
    public ResponseEntity<FondsCreatorHateoas>
    findFondsCreatorAssociatedWithFonds(@NotNull final String systemId) {
        FondsCreatorHateoas fondsCreatorHateoas =
                new FondsCreatorHateoas(
                        (List<INoarkEntity>) (List)
                                getFondsOrThrow(systemId).
                                        getReferenceFondsCreator());
        fondsCreatorHateoasHandler.addLinks(fondsCreatorHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(fondsCreatorHateoas);
    }

    /**
     * Generate a Default Series object that can be associated with the
     * identified Fonds.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param fondsSystemId The systemId of the Fonds object you wish to
     *                      generate a default Series for
     * @return the Series object wrapped as a SeriesHateoas object
     */
    @Override
    public SeriesHateoas generateDefaultSeries(@NotNull String fondsSystemId) {
        Series defaultSeries = new Series();
        SeriesStatus seriesStatus = (SeriesStatus)
            metadataService.findValidMetadataByEntityTypeOrThrow
                (SERIES_STATUS, SERIES_STATUS_ACTIVE_CODE, null);
        defaultSeries.setSeriesStatus(seriesStatus);
        DocumentMedium documentMedium = (DocumentMedium)
            metadataService.findValidMetadataByEntityTypeOrThrow
                (DOCUMENT_MEDIUM, DOCUMENT_MEDIUM_ELECTRONIC_CODE, null);
        defaultSeries.setDocumentMedium(documentMedium);
        SeriesHateoas seriesHateoas = new
                SeriesHateoas(defaultSeries);
        seriesHateoasHandler.addLinksOnTemplate(seriesHateoas, new Authorisation());
        return seriesHateoas;
    }

    /**
     * Generate a Default Fonds object that can be associated with the
     * identified Fonds. If fondsSystemId has a value, it is assumed you wish
     * to generate a sub-fonds.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param fondsSystemId The systemId of the Fonds object you wish to
     *                      generate a default sub-fonds. Null if the Fonds
     *                      is not a sub-fonds.
     * @return the Fonds object wrapped as a FondsHateoas object
     */
    @Override
    public FondsHateoas generateDefaultFonds(String fondsSystemId) {
        Fonds defaultFonds = new Fonds();
        DocumentMedium documentMedium = (DocumentMedium)
            metadataService.findValidMetadataByEntityTypeOrThrow
                (DOCUMENT_MEDIUM, DOCUMENT_MEDIUM_ELECTRONIC_CODE, null);
        defaultFonds.setDocumentMedium(documentMedium);
        FondsHateoas fondsHateoas = new FondsHateoas(defaultFonds);
        fondsHateoasHandler.addLinksOnTemplate(fondsHateoas, new Authorisation());
        return fondsHateoas;
    }

    // All UPDATE operations

    /**
     * Updates a Fonds object in the database. First we try to locate the
     * Fonds object. If the Fonds object does not exist a
     * NoarkEntityNotFoundException exception is thrown that the caller has
     * to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingFonds object to the existingFonds object and the existingFonds
     * object will be persisted to the database when the transaction boundary
     * is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the Fonds object from
     * the database a NoarkConcurrencyException is thrown. Note. This happens
     * when the call to Fonds.setVersion() occurs.
     *
     * @param fondsSystemId The systemId of the fonds object to retrieve
     * @param version       The last known version number (derived from an ETAG)
     * @param incomingFonds The incoming fonds object
     */
    @Override
    public FondsHateoas handleUpdate(@NotNull String fondsSystemId,
                                     @NotNull Long version,
                                     @NotNull Fonds incomingFonds) {

        Fonds existingFonds = getFondsOrThrow(fondsSystemId);

        // Copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingFonds, existingFonds);
        if (null != incomingFonds.getDocumentMedium()) {
            existingFonds.setDocumentMedium(
                incomingFonds.getDocumentMedium());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingFonds.setVersion(version);

        FondsHateoas fondsHateoas = new FondsHateoas(fondsRepository.
                save(existingFonds));
        fondsHateoasHandler.addLinks(fondsHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, existingFonds));
        return fondsHateoas;
    }

    // All DELETE operations

    /**
     * Deletes a Fonds object from the database. First we try to locate the
     * Fonds object. If the Fonds object does not exist a
     * NoarkEntityNotFoundException exception is thrown that the caller has
     * to deal with. Note that as there is a @ManyToMany relationship between
     * Fonds and FondsCreator with a join table, we first have to
     * disassociate the link between Fonds and FondsCreator or we hit a
     * foreign key constraint issue. The same applies for Fonds and
     * StorageLocation.
     * <p>
     * In order to minimise problems that could be caused with table and
     * column names changing, constants are used to define relevant column
     * and table names.
     * <p>
     * The approach is is discussed in a nikita gitlab issue
     * https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues/82
     *
     * @param fondsSystemId The systemId of the fonds object to retrieve
     */
    @Override
    public void deleteEntity(@NotNull String fondsSystemId) {
        Fonds fonds = getFondsOrThrow(fondsSystemId);
        // Disassociate any links between Fonds and FondsCreator
        disassociateForeignKeys(fonds, DELETE_FROM_FONDS_FONDS_CREATOR);
        // Disassociate any links between Fonds and StorageLocation
        disassociateForeignKeys(fonds, DELETE_FONDS_STORAGE_LOCATION);
        deleteEntity(fonds);
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return fondsRepository.deleteByOwnedBy(getUser());
    }

    // All HELPER operations

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid Fonds back. If there is no valid
     * Fonds, a NoarkEntityNotFoundException exception is thrown
     *
     * @param fondsSystemId The systemId of the fonds object to retrieve
     * @return the fonds object
     */
    private Fonds getFondsOrThrow(@NotNull String fondsSystemId) {
        Fonds fonds = fondsRepository.findBySystemId(UUID.fromString(fondsSystemId));
        if (fonds == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " Fonds, using systemId " +
                    fondsSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return fonds;
    }

    /**
     * Internal helper method. Check to make sure the fonds object does not
     * have a status of 'finalised'. If the check fails, a
     * NoarkEntityEditWhenClosedException exception is thrown that the caller
     * has to deal with.
     *
     * @param fonds The fonds object
     */
    private void checkFondsNotClosed(@NotNull Fonds fonds) {
        if (null != fonds.getFondsStatus() &&
                FONDS_STATUS_CLOSED_CODE.equals(
                        fonds.getFondsStatus().getCode())) {
            String info = INFO_CANNOT_ASSOCIATE_WITH_CLOSED_OBJECT +
                    ". Fonds with fondsSystemId " + fonds.getSystemId() +
                    " has status code " + FONDS_STATUS_CLOSED_CODE;
            logger.info(info);
            throw new NoarkEntityEditWhenClosedException(info);
        }
    }

    /**
     * Internal helper method. Check to make sure the fonds object does not
     * have any sub-fonds. This is useful when e.g. adding a Series to a fonds
     * .You must make sure that the fonds object does not have any child
     * fonds objects.
     * <p>
     * If the check fails, a NoarkInvalidStructureException exception is
     * thrown that the caller has to deal with.
     *
     * @param fonds The fonds object
     */
    private void checkFondsDoesNotContainSubFonds(@NotNull Fonds fonds) {
        if (fonds.getReferenceChildFonds() != null &&
                fonds.getReferenceChildFonds().size() > 0) {
            String info = INFO_INVALID_STRUCTURE + " Cannot associate a " +
                    "Series with a Fonds that already has sub-fonds " +
                    fonds.getSystemId();
            logger.info(info);
            throw new NoarkInvalidStructureException(info, "Fonds", "Series");
        }
    }

    /**
     * Internal helper method. Check to make sure the fonds object does not
     * have any Series associated with it. This is useful when e.g. adding a
     * sub-fonds to a fonds. You must make sure that the fonds object
     * does not have any Series associated with it.
     * <p>
     * If the check fails, a NoarkInvalidStructureException exception is
     * thrown that the caller has to deal with.
     *
     * @param fonds The fonds object
     */
    private void checkFondsDoesNotContainSeries(@NotNull Fonds fonds) {
        if (fonds.getReferenceSeries() != null &&
                fonds.getReferenceSeries().size() > 0) {
            String info = INFO_INVALID_STRUCTURE + " Cannot associate a " +
                    "(sub) Fonds with a Fonds that already has a Series " +
                    "associated with it!" + fonds.getSystemId();
            logger.info(info);
            throw new NoarkInvalidStructureException(info, "Fonds", "Series");
        }
    }

    /**
     * Internal helper method. Verify the FondsStatus code provided is
     * in the metadata catalog, and copy the code name associated with
     * the code from the metadata catalog into the Fonds.
     * <p>
     * If the FondsStatus code is unknown, a
     * NoarkEntityNotFoundException exception is thrown that the
     * caller has to deal with.  If the code and code name provided do
     * not match the entries in the metadata catalog, a
     * NoarkInvalidStructureException exception is thrown.
     *
     * @param fonds The fonds object
     */
    private void checkFondsStatusUponCreation(Fonds fonds) {
        if (fonds.getFondsStatus() != null) {
            FondsStatus fondsStatus = (FondsStatus) metadataService
                    .findValidMetadataByEntityTypeOrThrow(
                            FONDS_STATUS,
                            fonds.getFondsStatus());
            fonds.setFondsStatus(fondsStatus);
        }
    }
}
