package nikita.webapp.service.impl.admin;

import nikita.common.model.noark5.v4.admin.AdministrativeUnit;
import nikita.common.model.noark5.v4.admin.User;
import nikita.common.repository.n5v4.admin.IAdministrativeUnitRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.interfaces.ISequenceNumberGeneratorService;
import nikita.webapp.service.interfaces.admin.IAdministrativeUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.Constants.SYSTEM;

/**
 * Service class for AdministrativeUnit
 * <p>
 * Note administrativeUnit are persisted within the transactional boundary of
 * SequenceNumberGeneratorService
 */
@Service
@Transactional
public class AdministrativeUnitService
        implements IAdministrativeUnitService {

    private static final Logger logger =
            LoggerFactory.getLogger(AdministrativeUnitService.class);
    private IAdministrativeUnitRepository administrativeUnitRepository;
    private ISequenceNumberGeneratorService numberGeneratorService;

    public AdministrativeUnitService(
            IAdministrativeUnitRepository administrativeUnitRepository,
            ISequenceNumberGeneratorService numberGeneratorService) {
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
        administrativeUnit.setSystemId(UUID.randomUUID().toString());
        administrativeUnit.setCreatedDate(ZonedDateTime.now());
        administrativeUnit.setCreatedBy(SYSTEM);
        administrativeUnit.setDeleted(false);
        administrativeUnit.setOwnedBy(SYSTEM);
        createSequenceNumberGenerator(administrativeUnit);
        return administrativeUnit;
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
        administrativeUnit.setSystemId(UUID.randomUUID().toString());
        administrativeUnit.setCreatedDate(ZonedDateTime.now());
        administrativeUnit.setCreatedBy(user.getCreatedBy());
        administrativeUnit.setDeleted(false);
        administrativeUnit.setOwnedBy(user.getCreatedBy());
        createSequenceNumberGenerator(administrativeUnit);
        return administrativeUnit;
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
    public AdministrativeUnit findBySystemId(String systemId) {
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
        administrativeUnitRepository.save(existingAdministrativeUnit);
        return administrativeUnitRepository.save(incomingAdministrativeUnit);
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
                        .findBySystemId(administrativeUnitSystemId);
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
     * @param administrativeUnit The administrativeUnit you want a
     *                           SequenceNumberGenerator for
     */
    private void createSequenceNumberGenerator(
            AdministrativeUnit administrativeUnit) {
        numberGeneratorService.
                createSequenceNumberGenerator(administrativeUnit);
    }
}
