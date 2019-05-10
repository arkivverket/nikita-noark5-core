package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.Fonds;
import nikita.common.model.noark5.v4.FondsCreator;
import nikita.common.repository.n5v4.IFondsCreatorRepository;
import nikita.common.repository.n5v4.IFondsRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.interfaces.IFondsCreatorService;
import nikita.webapp.util.NoarkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.STATUS_OPEN;

@Service
@Transactional
public class FondsCreatorService
        extends NoarkService
        implements IFondsCreatorService {

    private static final Logger logger = LoggerFactory.
            getLogger(FondsCreatorService.class);
    private IFondsCreatorRepository fondsCreatorRepository;
    private IFondsRepository fondsRepository;

    public FondsCreatorService(EntityManager entityManager,
                               ApplicationEventPublisher
                                       applicationEventPublisher,
                               IFondsCreatorRepository fondsCreatorRepository,
                               IFondsRepository fondsRepository) {
        super(entityManager, applicationEventPublisher);
        this.fondsCreatorRepository = fondsCreatorRepository;
        this.fondsRepository = fondsRepository;
    }

    // All CREATE operations

    /**
     * Persists a new fondsCreator object to the database. Some values are set in the incoming payload (e.g. title)
     * and some are set by the core. owner, createdBy, createdDate are automatically set by the core.
     *
     * @param fondsCreator fondsCreator object with some values set
     * @return the newly persisted fondsCreator object
     */
    @Override
    public FondsCreator createNewFondsCreator(FondsCreator fondsCreator) {
        NoarkUtils.NoarkEntity.Create.setNikitaEntityValues(fondsCreator);
        NoarkUtils.NoarkEntity.Create.setSystemIdEntityValues(fondsCreator);
        return fondsCreatorRepository.save(fondsCreator);
    }

    @Override
    public Fonds createFondsAssociatedWithFondsCreator(String fondsCreatorSystemId, Fonds fonds) {
        FondsCreator fondsCreator = getFondsCreatorOrThrow(fondsCreatorSystemId);
        NoarkUtils.NoarkEntity.Create.checkDocumentMediumValid(fonds);
        NoarkUtils.NoarkEntity.Create.setNoarkEntityValues(fonds);
        fonds.setFondsStatus(STATUS_OPEN);
        NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues(fonds);
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
    public Optional<FondsCreator> findById(Long id) {
        return fondsCreatorRepository.findById(id);
    }

    @Override
    public FondsCreator findBySystemId(String systemId) {
        return getFondsCreatorOrThrow(systemId);
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
        FondsCreator fondsCreator = getFondsCreatorOrThrow(fondsCreatorSystemId);
        // See issue for a description of why this code was written this way
        // https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues/82
        Query q = entityManager.createNativeQuery("DELETE FROM fonds_fonds_creator WHERE f_pk_fonds_creator_id = :id ;");
        q.setParameter("id", fondsCreator.getId());
        q.executeUpdate();

        entityManager.remove(fondsCreator);
        entityManager.flush();
        entityManager.clear();
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
                findBySystemId(fondsCreatorSystemId);
        if (fondsCreator == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " FondsCreator, using systemId " + fondsCreatorSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return fondsCreator;
    }
}
