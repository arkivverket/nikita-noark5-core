package nikita.webapp.service.impl.admin;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.admin.AdministrativeUnitHateoas;
import nikita.common.repository.n5v5.admin.IAdministrativeUnitRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.admin.IAdministrativeUnitHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.ISequenceNumberGeneratorService;
import nikita.webapp.service.interfaces.admin.IAdministrativeUnitService;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

/**
 * Service class for AdministrativeUnit
 * <p>
 * Note administrativeUnit are persisted within the transactional boundary of
 * SequenceNumberGeneratorService
 */
@Service
public class AdministrativeUnitService
        extends NoarkService
        implements IAdministrativeUnitService {

    private final IAdministrativeUnitRepository administrativeUnitRepository;
    private final ISequenceNumberGeneratorService numberGeneratorService;
    private final IAdministrativeUnitHateoasHandler
            administrativeUnitHateoasHandler;

    public AdministrativeUnitService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IAdministrativeUnitRepository administrativeUnitRepository,
            ISequenceNumberGeneratorService numberGeneratorService,
            IAdministrativeUnitHateoasHandler
                    administrativeUnitHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService,
                odataService);
        this.administrativeUnitRepository = administrativeUnitRepository;
        this.numberGeneratorService = numberGeneratorService;
        this.administrativeUnitHateoasHandler =
                administrativeUnitHateoasHandler;
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
    @Transactional
    public AdministrativeUnitHateoas
    createNewAdministrativeUnitBySystem(AdministrativeUnit administrativeUnit) {
        createSequenceNumberGenerator(administrativeUnit);
        return packAsHateoas(administrativeUnitRepository
                .save(administrativeUnit));
    }

    /**
     * Persists a new administrativeUnit object to the database where
     * owned_by is set "system". It checks first to make sure that an
     * administrative unit with the given name does not exist. If it does exist
     * do nothing. This code should really only be called when creating some
     * basic data i nikita to make it operational for demo purposes.
     *
     * @param administrativeUnit administrativeUnit object with values set
     * @return the newly persisted administrativeUnit object
     */
    @Override
    @Transactional
    public void createNewAdministrativeUnitBySystemNoDuplicate(
            AdministrativeUnit administrativeUnit) {
        List<AdministrativeUnit> administrativeUnitList =
                administrativeUnitRepository.findByAdministrativeUnitName(
                        administrativeUnit.getAdministrativeUnitName());
        if (administrativeUnitList.isEmpty()) {
            createSequenceNumberGenerator(administrativeUnit);
            administrativeUnitRepository.save(administrativeUnit);
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
    @Transactional
    public AdministrativeUnitHateoas
    createNewAdministrativeUnitByUser(
            AdministrativeUnit administrativeUnit,
            User user) {
        administrativeUnit.setCreatedBy(user.getCreatedBy());
        administrativeUnit.setOwnedBy(user.getCreatedBy());
        createSequenceNumberGenerator(administrativeUnit);
        return packAsHateoas(
                administrativeUnitRepository.save(administrativeUnit));
    }

    // All READ methods

    /**
     * Retrieve all administrativeUnit
     *
     * @return list of all administrativeUnit
     */
    @Override
    public AdministrativeUnitHateoas findAll() {
        return (AdministrativeUnitHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve a single administrativeUnit identified by systemId
     *
     * @param systemId systemId of the administrativeUnit
     * @return the administrativeUnit
     */
    @Override
    public AdministrativeUnitHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(administrativeUnitRepository.findBySystemId(systemId));
    }

    /**
     * Update a particular administrativeUnit identified by the given systemId.
     *
     * @param incomingAdministrativeUnit administrativeUnit to update
     * @return the updated administrativeUnit
     */
    @Override
    @Transactional
    public AdministrativeUnitHateoas update(
            UUID systemId, Long version,
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
        return packAsHateoas(incomingAdministrativeUnit);
    }

    /**
     * Delete a administrativeUnit identified by the given systemId from the database.
     *
     * @param systemId systemId of the administrativeUnit to delete
     */
    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        deleteEntity(getAdministrativeUnitOrThrow(systemId));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        administrativeUnitRepository.deleteByOwnedBy(getUser());
    }

    @Override
    public AdministrativeUnitHateoas generateDefaultAdministrativeUnit() {
        AdministrativeUnit administrativeUnit = new AdministrativeUnit();
        administrativeUnit.setShortName("kortnavn på administrativtenhet");
        administrativeUnit.setAdministrativeUnitName(
                "Formell navn på administrativtenhet");
        administrativeUnit.setVersion(-1L, true);
        return packAsHateoas(administrativeUnit);
    }

    @Override
    public AdministrativeUnit findFirst() {
        return administrativeUnitRepository
                .findFirstByOrderByCreatedDateAsc();
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
        AdministrativeUnit administrativeUnit =
                administrativeUnitRepository.
                        findByUsersInAndDefaultAdministrativeUnit(
                                users, true);
        if (null != administrativeUnit) {
            return administrativeUnit;
        } else {
            throw new NoarkEntityNotFoundException(INFO_CANNOT_FIND_OBJECT +
                    " AdministrativeUnit associated with user " +
                    user.toString());
        }
    }

    // All HELPER methods

    public AdministrativeUnitHateoas packAsHateoas(
            @NotNull final AdministrativeUnit administrativeUnit) {
        AdministrativeUnitHateoas administrativeUnitHateoas =
                new AdministrativeUnitHateoas(administrativeUnit);
        applyLinksAndHeader(administrativeUnitHateoas, administrativeUnitHateoasHandler);
        return administrativeUnitHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid AdministrativeUnit back. If there is
     * no valid AdministrativeUnit, an exception is thrown
     *
     * @param systemId systemId of the administrativeUnit
     *                 to find
     * @return the administrativeUnit
     */
    protected AdministrativeUnit
    getAdministrativeUnitOrThrow(@NotNull final UUID systemId) {
        AdministrativeUnit administrativeUnit =
                administrativeUnitRepository
                        .findBySystemId(systemId);
        if (administrativeUnit == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " AdministrativeUnit, using systemId " +
                    systemId;
            throw new NoarkEntityNotFoundException(error);
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
    private void createSequenceNumberGenerator(
            AdministrativeUnit administrativeUnit) {
        numberGeneratorService.
                createSequenceNumberGenerator(administrativeUnit).
                getReferenceAdministrativeUnit();
    }
}
