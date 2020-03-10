package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.repository.n5v5.IFondsCreatorRepository;
import nikita.common.repository.n5v5.IFondsRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IFondsHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IFondsCreatorService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.DatabaseConstants.DELETE_FROM_FONDS_CREATOR_FONDS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class FondsCreatorService
        extends NoarkService
        implements IFondsCreatorService {

    private static final Logger logger = LoggerFactory.
            getLogger(FondsCreatorService.class);
    private IFondsCreatorRepository fondsCreatorRepository;
    private IFondsRepository fondsRepository;
    private IMetadataService metadataService;
    private IFondsHateoasHandler fondsHateoasHandler;

    public FondsCreatorService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IFondsCreatorRepository fondsCreatorRepository,
            IFondsRepository fondsRepository,
            IMetadataService metadataService,
            IFondsHateoasHandler fondsHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.fondsCreatorRepository = fondsCreatorRepository;
        this.fondsRepository = fondsRepository;
        this.metadataService = metadataService;
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
    public FondsCreator createNewFondsCreator(FondsCreator fondsCreator) {
        return fondsCreatorRepository.save(fondsCreator);
    }

    @Override
    public Fonds createFondsAssociatedWithFondsCreator(
            String fondsCreatorSystemId, Fonds fonds) {
        FondsCreator fondsCreator =
                getFondsCreatorOrThrow(fondsCreatorSystemId);
        validateDocumentMedium(metadataService, fonds);
        FondsStatus fondsStatus = (FondsStatus)
            metadataService.findValidMetadataByEntityTypeOrThrow
                (FONDS_STATUS, FONDS_STATUS_OPEN_CODE, null);
        fonds.setFondsStatus(fondsStatus);
        setFinaliseEntityValues(fonds);
        fonds.getReferenceFondsCreator().add(fondsCreator);
        fondsCreator.getReferenceFonds().add(fonds);
        fondsRepository.save(fonds);
        return fonds;
    }

    @Override
    public Iterable<FondsCreator> findAll() {
        return fondsCreatorRepository.findAll();
    }

    @Override
    public List<FondsCreator> findByOwnedBy(String ownedBy) {
        return fondsCreatorRepository.findByOwnedBy(ownedBy);
    }

    @Override
    public FondsCreator findBySystemId(String systemId) {
        return getFondsCreatorOrThrow(systemId);
    }

    /**
     * Retrieve the list of FondsHateoas object associated with
     * the FondsCreator object identified by systemId
     *
     * @param systemId The systemId of the FondsCreator object to retrieve the
     *                 associated FondsHateoas
     * @return A FondsHateoas list packed as a ResponseEntity
     */
    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<FondsHateoas> findFondsAssociatedWithFondsCreator(
            @NotNull final String systemId) {
        FondsHateoas fondsHateoas =
                new FondsHateoas(
                        (List<INoarkEntity>) (List)
                                getFondsCreatorOrThrow(systemId).
                                        getReferenceFonds());
        fondsHateoasHandler.addLinks(fondsHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(fondsHateoas.getEntityVersion().toString())
                .body(fondsHateoas);
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
    public FondsCreator handleUpdate(@NotNull final String systemId,
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
        fondsCreatorRepository.save(existingFondsCreator);
        return existingFondsCreator;
    }

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String fondsCreatorSystemId) {
        FondsCreator fondsCreator =
                getFondsCreatorOrThrow(fondsCreatorSystemId);
        disassociateForeignKeys(fondsCreator,
                DELETE_FROM_FONDS_CREATOR_FONDS);
        deleteEntity(fondsCreator);
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return fondsCreatorRepository.deleteByOwnedBy(getUser());
    }

    // All HELPER operations
    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this,  you
     * will only ever get a valid FondsCreator back. If there is no valid
     * FondsCreator, an exception is thrown
     *
     * @param fondsCreatorSystemId systemID of the fondsCreator object to
     *                             retrive
     * @return the fondsCreator object
     */
    protected FondsCreator getFondsCreatorOrThrow(
            @NotNull String fondsCreatorSystemId) {
        FondsCreator fondsCreator = fondsCreatorRepository.
                findBySystemId(UUID.fromString(fondsCreatorSystemId));
        if (fondsCreator == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " FondsCreator, using systemId " + fondsCreatorSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return fondsCreator;
    }
}
