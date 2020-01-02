package nikita.webapp.service.impl.admin;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.repository.n5v5.admin.IAdministrativeUnitRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.ISequenceNumberGeneratorService;
import nikita.webapp.service.interfaces.admin.IAdministrativeUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.*;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

/**
 * Service class for AdministrativeUnit
 * <p>
 * Note administrativeUnit are persisted within the transactional boundary of
 * SequenceNumberGeneratorService
 */
@Service
@Transactional
public class AdministrativeUnitService
        extends NoarkService
        implements IAdministrativeUnitService {

    private static final Logger logger =
            LoggerFactory.getLogger(AdministrativeUnitService.class);
    private IAdministrativeUnitRepository administrativeUnitRepository;
    private ISequenceNumberGeneratorService numberGeneratorService;

    public AdministrativeUnitService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IAdministrativeUnitRepository administrativeUnitRepository,
            ISequenceNumberGeneratorService numberGeneratorService) {
        super(entityManager, applicationEventPublisher);
        this.administrativeUnitRepository = administrativeUnitRepository;
        this.numberGeneratorService = numberGeneratorService;
    }

    // All CREATE operations

    /**
     * Persists a new administrativeUnit object to the database where
     * owned_by is set "system".
     *
     * Note administrativeUnit is saved within the transactional boundary of
     * createSequenceNumberGenerator
     *
     * @param administrativeUnit administrativeUnit object with values set
     * @return the newly persisted administrativeUnit object
     */
    @Override
    public AdministrativeUnit
    createNewAdministrativeUnitBySystem(AdministrativeUnit administrativeUnit) {
        createSequenceNumberGenerator(administrativeUnit);
        return administrativeUnitRepository.save(administrativeUnit);
    }

    /**
     * Persists a new administrativeUnit object to the database where
     * owned_by is set "system". It checks first to make sure that an
     * administrative unit with the given name does not exist. If it does exist
     * do nothing. This code should really only be called when creating some
     * basic data i nikita to make it operational for demo purposes.
     * <p>
     * Note administrativeUnit is saved within the transactional boundary of
     * createSequenceNumberGenerator
     *
     * @param administrativeUnit administrativeUnit object with values set
     * @return the newly persisted administrativeUnit object
     */
    @Override
    public AdministrativeUnit
    createNewAdministrativeUnitBySystemNoDuplicate(
            AdministrativeUnit administrativeUnit) {
        List<AdministrativeUnit> administrativeUnitList =
                administrativeUnitRepository.findByAdministrativeUnitName(
                        administrativeUnit.getAdministrativeUnitName());
        if (administrativeUnitList.size() == 0) {
            createSequenceNumberGenerator(administrativeUnit);
            return administrativeUnitRepository.save(administrativeUnit);
        } else {
            return null;
        }
    }

    /**
     * Persists a new administrativeUnit object to the database where
     * owned_by is set to username of the person logged in.
     *
     * Note administrativeUnit is saved within the transactional boundary of
     * createSequenceNumberGenerator
     *
     * @param administrativeUnit administrativeUnit object with values set
     * @return the newly persisted administrativeUnit object
     */

    @Override
    public AdministrativeUnit
    createNewAdministrativeUnitByUser(
            AdministrativeUnit administrativeUnit,
            User user) {
        administrativeUnit.setCreatedBy(user.getCreatedBy());
        administrativeUnit.setOwnedBy(user.getCreatedBy());
        createSequenceNumberGenerator(administrativeUnit);
        return administrativeUnitRepository.save(administrativeUnit);
    }

    // All READ methods

    /**
     * Retrieve all administrativeUnit
     *
     * @return list of all administrativeUnit
     */
    @Override
    public List<AdministrativeUnit> findAll() {
        return administrativeUnitRepository.findAll();
    }

    // find by systemId

    /**
     * Retrieve a single administrativeUnit identified by systemId
     *
     * @param systemId systemId of the administrativeUnit
     * @return the administrativeUnit
     */
    @Override
    public AdministrativeUnit findBySystemId(UUID systemId) {
        return administrativeUnitRepository.findBySystemId(systemId);
    }

    /**
     * Update a particular administrativeUnit identified by the given systemId.
     *
     * @param incomingAdministrativeUnit administrativeUnit to update
     * @return the updated administrativeUnit
     */
    @Override
    public AdministrativeUnit update(
            String systemId, Long version,
            AdministrativeUnit incomingAdministrativeUnit) {
        AdministrativeUnit existingAdministrativeUnit =
                getAdministrativeUnitOrThrow(systemId);
        // Here copy all the values you are allowed to copy ....
        if (null != existingAdministrativeUnit.getAdministrativeUnitName()) {
            existingAdministrativeUnit.setAdministrativeUnitName(
                    incomingAdministrativeUnit.getAdministrativeUnitName());
        }
        if (null != existingAdministrativeUnit.getShortName()) {
            existingAdministrativeUnit.setShortName(
                    incomingAdministrativeUnit.getShortName());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingAdministrativeUnit.setVersion(version);
        return administrativeUnitRepository.save(incomingAdministrativeUnit);
    }

    /**
     * Delete a administrativeUnit identified by the given systemID from the database.
     *
     * @param systemId systemId of the administrativeUnit to delete
     */
    @Override
    public void deleteEntity(@NotNull String systemId) {
        deleteEntity(getAdministrativeUnitOrThrow(systemId));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return administrativeUnitRepository.deleteByOwnedBy(getUser());
    }

    @Override
    public Optional<AdministrativeUnit> findFirst() {
        return administrativeUnitRepository.findFirstByOrderByCreatedDateAsc();
    }

    /**
     * Find the administrativeUnit identified for the  given user or throw a
     * NoarkEntityNotFoundException. Note this method  will return a non-null
     * administrativeUnit. An exception is thrown otherwise.
     *
     * @param user the user you want to retrieve an associated
     *             administrativeUnit
     * @return the administrativeUnit
     */
    public AdministrativeUnit getAdministrativeUnitOrThrow(User user) {

        Set<User> users = new HashSet<>();
        users.add(user);
        Optional<AdministrativeUnit> administrativeUnit =
                administrativeUnitRepository.
                        findByUsersInAndDefaultAdministrativeUnit(
                                users, true);
        if (administrativeUnit.isPresent()) {
            return administrativeUnit.get();
        } else {
            String info =
                    INFO_CANNOT_FIND_OBJECT +
                            " AdministrativeUnit associated with user " +
                            user.toString();
            logger.warn(info);
            throw new NoarkEntityNotFoundException(info);
        }
    }

    // All HELPER methods

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid AdministrativeUnit back. If there is
     * no valid AdministrativeUnit, an exception is thrown
     *
     * @param administrativeUnitSystemId systemId of the administrativeUnit
     *                                   to find
     * @return the administrativeUnit
     */
    protected AdministrativeUnit
    getAdministrativeUnitOrThrow(@NotNull String administrativeUnitSystemId) {
        AdministrativeUnit administrativeUnit =
                administrativeUnitRepository
                        .findBySystemId(
                                UUID.fromString(administrativeUnitSystemId));
        if (administrativeUnit == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " AdministrativeUnit, using systemId " +
                    administrativeUnitSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return administrativeUnit;
    }

    /**
     * Create a SequenceNumberGenerator object to be associated with this
     * administrativeUnit.
     *
     * Explain why returning getReferenceAdministrativeUnit
     * @param administrativeUnit The administrativeUnit you want a
     *                           SequenceNumberGenerator for
     */
    private AdministrativeUnit createSequenceNumberGenerator(
            AdministrativeUnit administrativeUnit) {
        return numberGeneratorService.
                createSequenceNumberGenerator(administrativeUnit).
                getReferenceAdministrativeUnit();
    }
}
