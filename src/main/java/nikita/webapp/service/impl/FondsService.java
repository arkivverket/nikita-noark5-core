package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.repository.n5v5.IFondsRepository;
import nikita.common.util.exceptions.NoarkEntityEditWhenClosedException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.common.util.exceptions.NoarkInvalidStructureException;
import nikita.webapp.hateoas.interfaces.IFondsHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.IFondsCreatorService;
import nikita.webapp.service.interfaces.IFondsService;
import nikita.webapp.service.interfaces.ISeriesService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IStorageLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.DatabaseConstants.DELETE_FONDS_STORAGE_LOCATION;
import static nikita.common.config.DatabaseConstants.DELETE_FROM_FONDS_FONDS_CREATOR;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;

@Service
public class FondsService
        extends NoarkService
        implements IFondsService {

    private static final Logger logger =
            LoggerFactory.getLogger(FondsService.class);

    private final IFondsRepository fondsRepository;
    private final ISeriesService seriesService;
    private final IMetadataService metadataService;
    private final IFondsCreatorService fondsCreatorService;
    private final IFondsHateoasHandler fondsHateoasHandler;
    private final IStorageLocationService storageLocationService;

    public FondsService(EntityManager entityManager,
                        ApplicationEventPublisher applicationEventPublisher,
                        IODataService odataService,
                        IPatchService patchService,
                        IFondsRepository fondsRepository,
                        ISeriesService seriesService,
                        IMetadataService metadataService,
                        IFondsCreatorService fondsCreatorService,
                        IFondsHateoasHandler fondsHateoasHandler,
                        IStorageLocationService storageLocationService) {
        super(entityManager, applicationEventPublisher, patchService,
                odataService);
        this.fondsRepository = fondsRepository;
        this.seriesService = seriesService;
        this.metadataService = metadataService;
        this.fondsCreatorService = fondsCreatorService;
        this.fondsHateoasHandler = fondsHateoasHandler;
        this.storageLocationService = storageLocationService;
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
    @Transactional
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
        return packAsHateoas(fondsRepository.save(fonds));
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
    @Transactional
    public FondsHateoas createFondsAssociatedWithFonds(
            @NotNull final UUID parentFondsSystemId,
            @NotNull final Fonds childFonds) {
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
     * @param systemId The systemId of the fonds object to associate a
     *                 Series with
     * @param series   The incoming Series object
     * @return the newly persisted series object wrapped as a SeriesHateoas
     * object
     */
    @Override
    @Transactional
    public SeriesHateoas createSeriesAssociatedWithFonds(
            @NotNull final UUID systemId,
            @NotNull final Series series) {
        Fonds fonds = getFondsOrThrow(systemId);
        seriesService.updateSeriesReferences(series);
        seriesService.checkSeriesStatusUponCreation(series);
        //checkFondsNotClosed(fonds);
        checkFondsDoesNotContainSubFonds(fonds);
        series.setReferenceFonds(fonds);
        return seriesService.save(series);
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
     * @param systemId     The systemId of the fonds object you wish to
     *                     associate the fondsCreator object with
     * @param fondsCreator incoming fondsCreator object with some values set
     * @return the newly persisted fondsCreator object wrapped as a
     * FondsCreatorHateoas object
     */
    @Override
    @Transactional
    public FondsCreatorHateoas createFondsCreatorAssociatedWithFonds(
            @NotNull final UUID systemId,
            @NotNull final FondsCreator fondsCreator) {
        Fonds fonds = getFondsOrThrow(systemId);
        //checkFondsNotClosed(fonds);
        fonds.addFondsCreator(fondsCreator);
        return fondsCreatorService.createNewFondsCreator(fondsCreator);
    }

    @Override
    @Transactional
    public StorageLocationHateoas createStorageLocationAssociatedWithFonds(
            UUID systemId, StorageLocation storageLocation) {
        Fonds fonds = getFondsOrThrow(systemId);
        return storageLocationService
                .createStorageLocationAssociatedWithFonds(
                        storageLocation, fonds);
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
     * @param systemId The systemId of the Fonds object that you want to
     *                 retrieve associated Series objects
     * @return the list of Series objects wrapped as a SeriesHateoas object
     */
    @Override
    public SeriesHateoas findSeriesAssociatedWithFonds(
            @NotNull final UUID systemId) {
        return (SeriesHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve a single Fonds objects from the database.
     *
     * @param systemId The systemId of the Fonds object you wish to
     *                 retrieve
     * @return the Fonds object wrapped as a FondsHateoas object
     */
    @Override
    public FondsHateoas findSingleFonds(@NotNull final UUID systemId) {
        return packAsHateoas(getFondsOrThrow(systemId));
    }

    /**
     * Retrieve a list of paginated Fonds objects associated from the database.
     *
     * @return the list of Fonds object wrapped as a FondsHateoas object
     */
    @Override
    public FondsHateoas findAllFonds() {
        return (FondsHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve a list of children fonds belonging to the fonds object
     * identified by systemId
     *
     * @param systemId The systemId of the Fonds object to retrieve its children
     * @return A FondsHateoas object containing the children fonds's
     */
    @Override
    public FondsHateoas findAllChildren(@NotNull final UUID systemId) {
        getFondsOrThrow(systemId);
        return (FondsHateoas) odataService.processODataQueryGet();
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
    public FondsCreatorHateoas findFondsCreatorAssociatedWithFonds(
            @NotNull final UUID systemId) {
        return (FondsCreatorHateoas) odataService.processODataQueryGet();
    }

    @Override
    public StorageLocationHateoas findStorageLocationAssociatedWithFonds(
            @NotNull final UUID systemID) {
        return (StorageLocationHateoas) odataService.processODataQueryGet();
    }

    /**
     * Generate a Default Series object that can be associated with the
     * identified Fonds.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the Fonds object you wish to
     *                 generate a default Series for
     * @return the Series object wrapped as a SeriesHateoas object
     */
    @Override
    public SeriesHateoas generateDefaultSeries(@NotNull final UUID systemId) {
        return seriesService.generateDefaultSeries(systemId);
    }

    /**
     * Generate a Default Fonds object that can be associated with the
     * identified Fonds. If systemId has a value, it is assumed you wish
     * to generate a sub-fonds.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the Fonds object you wish to
     *                 generate a default sub-fonds. Null if the Fonds
     *                 is not a sub-fonds.
     * @return the Fonds object wrapped as a FondsHateoas object
     */
    @Override
    public FondsHateoas generateDefaultFonds(@NotNull final UUID systemId) {
        Fonds defaultFonds = new Fonds();
        DocumentMedium documentMedium = (DocumentMedium)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (DOCUMENT_MEDIUM, DOCUMENT_MEDIUM_ELECTRONIC_CODE, null);
        defaultFonds.setDocumentMedium(documentMedium);
        defaultFonds.setVersion(-1L, true);
        return packAsHateoas(defaultFonds);
    }

    @Override
    public StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemId) {
        return storageLocationService.getDefaultStorageLocation(systemId);
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
     * @param systemId      The systemId of the fonds object to retrieve
     * @param version       The last known version number (derived from an ETAG)
     * @param incomingFonds The incoming fonds object
     */
    @Override
    @Transactional
    public FondsHateoas handleUpdate(@NotNull final UUID systemId,
                                     @NotNull final Long version,
                                     @NotNull Fonds incomingFonds) {

        Fonds existingFonds = getFondsOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingFonds, existingFonds);
        if (null != incomingFonds.getDocumentMedium()) {
            existingFonds.setDocumentMedium(
                    incomingFonds.getDocumentMedium());
        }
        existingFonds.setFondsStatus(incomingFonds.getFondsStatus());
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingFonds.setVersion(version);
        return packAsHateoas(existingFonds);
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
     * @param systemId The systemId of the fonds object to retrieve
     */
    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        Fonds fonds = getFondsOrThrow(systemId);
        // Disassociate any links between Fonds and FondsCreator
        disassociateForeignKeys(fonds, DELETE_FROM_FONDS_FONDS_CREATOR);
        // Disassociate any links between Fonds and StorageLocation
        disassociateForeignKeys(fonds, DELETE_FONDS_STORAGE_LOCATION);
        deleteEntity(fonds);
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        fondsRepository.deleteByOwnedBy(getUser());
    }

    // All HELPER operations

    public FondsHateoas packAsHateoas(Fonds fonds) {
        FondsHateoas fondsHateoas = new FondsHateoas(fonds);
        applyLinksAndHeader(fondsHateoas, fondsHateoasHandler);
        return fondsHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid Fonds back. If there is no valid
     * Fonds, a NoarkEntityNotFoundException exception is thrown
     *
     * @param systemId The systemId of the fonds object to retrieve
     * @return the fonds object
     */
    private Fonds getFondsOrThrow(@NotNull final UUID systemId) {
        Fonds fonds = fondsRepository.findBySystemId(systemId);
        if (fonds == null) {
            throw new NoarkEntityNotFoundException(INFO_CANNOT_FIND_OBJECT +
                    " Fonds, using systemId " + systemId);
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
                    ". Fonds with systemId " + fonds.getSystemId() +
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
            FondsStatus fondsStatus = (FondsStatus)
                    metadataService.findValidMetadata(fonds.getFondsStatus());
            fonds.setFondsStatus(fondsStatus);
        }
    }
}
