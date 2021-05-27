package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.repository.n5v5.IFondsCreatorRepository;
import nikita.common.repository.n5v5.IFondsRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IFondsCreatorHateoasHandler;
import nikita.webapp.hateoas.interfaces.IFondsHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.IFondsCreatorService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.DatabaseConstants.DELETE_FROM_FONDS_CREATOR_FONDS;
import static nikita.common.config.N5ResourceMappings.FONDS_STATUS;
import static nikita.common.config.N5ResourceMappings.FONDS_STATUS_OPEN_CODE;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;

@Service
public class FondsCreatorService
        extends NoarkService
        implements IFondsCreatorService {

    private final IFondsCreatorRepository fondsCreatorRepository;
    private final IFondsRepository fondsRepository;
    private final IMetadataService metadataService;
    private final IFondsCreatorHateoasHandler fondsCreatorHateoasHandler;
    private final IFondsHateoasHandler fondsHateoasHandler;

    public FondsCreatorService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IFondsCreatorRepository fondsCreatorRepository,
            IFondsRepository fondsRepository,
            IMetadataService metadataService,
            IFondsCreatorHateoasHandler fondsCreatorHateoasHandler,
            IFondsHateoasHandler fondsHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService,
                odataService);
        this.fondsCreatorRepository = fondsCreatorRepository;
        this.fondsRepository = fondsRepository;
        this.metadataService = metadataService;
        this.fondsCreatorHateoasHandler = fondsCreatorHateoasHandler;
        this.fondsHateoasHandler = fondsHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new fondsCreator object to the database. Some values are set
     * in the incoming payload (e.g. title) and some are set by the core.
     * owner, createdBy, createdDate are automatically set by the core.
     *
     * @param fondsCreator fondsCreator object with some values set
     * @return the newly persisted fondsCreator object
     */
    @Override
    @Transactional
    public FondsCreatorHateoas createNewFondsCreator(FondsCreator fondsCreator) {
        return packAsHateoas(fondsCreatorRepository.save(fondsCreator));
    }

    @Override
    @Transactional
    public FondsHateoas createFondsAssociatedWithFondsCreator(
            UUID systemId, Fonds fonds) {
        FondsCreator fondsCreator =
                getFondsCreatorOrThrow(systemId);
        validateDocumentMedium(metadataService, fonds);
        FondsStatus fondsStatus = (FondsStatus)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (FONDS_STATUS, FONDS_STATUS_OPEN_CODE, null);
        fonds.setFondsStatus(fondsStatus);
        setFinaliseEntityValues(fonds);
        fonds.addFondsCreator(fondsCreator);
        fondsCreator.addFonds(fonds);
        return packFondsAsHateoas(fondsRepository.save(fonds));
    }

    @Override
    public FondsCreatorHateoas findAll() {
        return (FondsCreatorHateoas) odataService.processODataQueryGet();
    }

    @Override
    public FondsCreatorHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getFondsCreatorOrThrow(systemId));
    }

    /**
     * Retrieve the list of FondsHateoas object associated with
     * the FondsCreator object identified by systemId
     *
     * @param systemId The systemId of the FondsCreator object to retrieve the
     *                 associated FondsHateoas
     * @return A FondsHateoas list
     */
    @Override
    public FondsHateoas findFondsAssociatedWithFondsCreator(
            @NotNull final UUID systemId) {
        return (FondsHateoas) odataService.processODataQueryGet();
    }

    // All UPDATE operations

    /**
     * Updates a FondsCreator object in the database. First we try to locate the
     * FondsCreator object. If the FondsCreator object does not exist a
     * NoarkEntityNotFoundException exception is thrown that the caller has
     * to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingFondsCreator object to the existingFondsCreator object and the
     * existingFondsCreator object will be persisted to the database when the
     * transaction boundary is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the FondsCreator object
     * from the database a NoarkConcurrencyException is thrown. Note. This
     * happens when the call to FondsCreator.setVersion() occurs.
     * <p>
     * Note: fondsCreatorName and fondsCreatorId are not nullable
     *
     * @param systemId             systemId of the incoming fondsCreator object
     * @param version              ETag version
     * @param incomingFondsCreator the incoming fondsCreator
     * @return the updated fondsCreator object after it is persisted
     */
    @Override
    @Transactional
    public FondsCreatorHateoas handleUpdate(@NotNull final UUID systemId,
                                            @NotNull final Long version,
                                            @NotNull final FondsCreator
                                                    incomingFondsCreator) {
        FondsCreator existingFondsCreator = getFondsCreatorOrThrow(systemId);
        // Here copy all the values you are allowed to copy ....
        existingFondsCreator.setDescription(
                incomingFondsCreator.getDescription());
        if (null != incomingFondsCreator.getFondsCreatorId()) {
            existingFondsCreator.setFondsCreatorId(
                    incomingFondsCreator.getFondsCreatorId());
        }
        if (null != incomingFondsCreator.getFondsCreatorName()) {
            existingFondsCreator.setFondsCreatorName(
                    incomingFondsCreator.getFondsCreatorName());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingFondsCreator.setVersion(version);
        return packAsHateoas(existingFondsCreator);
    }

    // All DELETE operations
    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        FondsCreator fondsCreator =
                getFondsCreatorOrThrow(systemId);
        disassociateForeignKeys(fondsCreator,
                DELETE_FROM_FONDS_CREATOR_FONDS);
        deleteEntity(fondsCreator);
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        fondsCreatorRepository.deleteByOwnedBy(getUser());
    }

    @Override
    public FondsCreatorHateoas generateDefaultFondsCreator() {
        FondsCreator fondsCreator = new FondsCreator();
        fondsCreator.setVersion(-1L, true);
        return packAsHateoas(fondsCreator);
    }

    public FondsCreatorHateoas packAsHateoas(FondsCreator fondsCreator) {
        FondsCreatorHateoas fondsCreatorHateoas =
                new FondsCreatorHateoas(fondsCreator);
        applyLinksAndHeader(fondsCreatorHateoas, fondsCreatorHateoasHandler);
        return fondsCreatorHateoas;
    }

    /**
     * Create a FondsHateoas object from a Fonds object and apply outgoing links
     * and header values.
     * Note: The method is required in this class so that we can avoid a
     * circular dependency on service classes (Fonds->FondsCreator->Fonds)
     *
     * @param fonds the fonds object
     * @return a FondsHateoas object
     */
    public FondsHateoas packFondsAsHateoas(Fonds fonds) {
        FondsHateoas fondsHateoas =
                new FondsHateoas(fonds);
        applyLinksAndHeader(fondsHateoas, fondsHateoasHandler);
        return fondsHateoas;
    }

    // All HELPER operations

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this,  you
     * will only ever get a valid FondsCreator back. If there is no valid
     * FondsCreator, an exception is thrown
     *
     * @param systemId systemId of the fondsCreator object to retrieve
     * @return the fondsCreator object
     */
    protected FondsCreator getFondsCreatorOrThrow(
            @NotNull final UUID systemId) {
        FondsCreator fondsCreator = fondsCreatorRepository.
                findBySystemId(systemId);
        if (fondsCreator == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " FondsCreator, using systemId " + systemId;
            throw new NoarkEntityNotFoundException(error);
        }
        return fondsCreator;
    }
}
