package nikita.webapp.service.impl.admin;

import nikita.common.model.noark5.v4.admin.AdministrativeUnit;
import nikita.common.model.noark5.v4.admin.User;
import nikita.common.model.noark5.v4.casehandling.SequenceNumberGenerator;
import nikita.common.repository.n5v4.admin.IAdministrativeUnitRepository;
import nikita.common.repository.n5v4.casehandling.ISequenceNumberGeneratorRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.interfaces.admin.IAdministrativeUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.Constants.SYSTEM;

@Service
@Transactional
public class AdministrativeUnitService implements IAdministrativeUnitService {

    private static final Logger logger =
            LoggerFactory.getLogger(AdministrativeUnitService.class);
    private IAdministrativeUnitRepository administrativeUnitRepository;
    private ISequenceNumberGeneratorRepository sequenceNumberGeneratorRepository;

    public AdministrativeUnitService(
            IAdministrativeUnitRepository administrativeUnitRepository,
            ISequenceNumberGeneratorRepository sequenceNumberGeneratorRepository) {
        this.administrativeUnitRepository = administrativeUnitRepository;
        this.sequenceNumberGeneratorRepository = sequenceNumberGeneratorRepository;
    }

    // All CREATE operations

    /**
     * Persists a new administrativeUnit object to the database.
     *
     * @param administrativeUnit administrativeUnit object with values set
     * @return the newly persisted administrativeUnit object
     */
    @Override
    public AdministrativeUnit
    createNewAdministrativeUnitBySystem(AdministrativeUnit administrativeUnit) {
        administrativeUnit.setSystemId(UUID.randomUUID().toString());
        administrativeUnit.setCreatedDate(new Date());
        administrativeUnit.setCreatedBy(SYSTEM);
        administrativeUnit.setDeleted(false);
        administrativeUnit.setOwnedBy(SYSTEM);
        createSequenceNumberGenerator(administrativeUnit);
        return administrativeUnitRepository.save(administrativeUnit);
    }

    @Override
    public AdministrativeUnit
    createNewAdministrativeUnitByUser(
            AdministrativeUnit administrativeUnit,
            User user) {
        administrativeUnit.setSystemId(UUID.randomUUID().toString());
        administrativeUnit.setCreatedDate(new Date());
        administrativeUnit.setCreatedBy(user.getCreatedBy());
        administrativeUnit.setDeleted(false);
        administrativeUnit.setOwnedBy(user.getCreatedBy());
        createSequenceNumberGenerator(administrativeUnit);
        return administrativeUnitRepository.save(administrativeUnit);
    }

    // All READ operations

    /**
     * retrieve all administrativeUnit
     *
     * @return
     */
    @Override
    public List<AdministrativeUnit> findAll() {
        return administrativeUnitRepository.findAll();
    }

    // find by systemId

    /**
     * retrieve a single administrativeUnit identified by systemId
     *
     * @param systemId
     * @return
     */
    @Override
    public AdministrativeUnit findBySystemId(String systemId) {
        return administrativeUnitRepository.findBySystemId(systemId);
    }

    /**
     * update a particular administrativeUnit. <br>
     *
     * @param incomingAdministrativeUnit
     * @return the updated administrativeUnit
     */
    @Override
    public AdministrativeUnit update(String systemId, Long version,
                                     AdministrativeUnit incomingAdministrativeUnit) {
        AdministrativeUnit existingAdministrativeUnit = getAdministrativeUnitOrThrow(systemId);
        // Here copy all the values you are allowed to copy ....
        if (null != existingAdministrativeUnit.getAdministrativeUnitName()) {
            existingAdministrativeUnit.setAdministrativeUnitName(incomingAdministrativeUnit.getAdministrativeUnitName());
        }
        if (null != existingAdministrativeUnit.getShortName()) {
            existingAdministrativeUnit.setShortName(incomingAdministrativeUnit.getShortName());
        }
        existingAdministrativeUnit.setVersion(version);
        administrativeUnitRepository.save(existingAdministrativeUnit);
        return administrativeUnitRepository.save(incomingAdministrativeUnit);
    }

    // All HELPER operations

    /**
     * Internal helper method. Rather than having a find and try catch in multiple methods, we have it here once.
     * If you call this, be aware that you will only ever get a valid AdministrativeUnit back. If there is no valid
     * AdministrativeUnit, an exception is thrown
     *
     * @param administrativeUnitSystemId
     * @return
     */
    protected AdministrativeUnit getAdministrativeUnitOrThrow(@NotNull String administrativeUnitSystemId) {
        AdministrativeUnit administrativeUnit = administrativeUnitRepository.findBySystemId(administrativeUnitSystemId);
        if (administrativeUnit == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " AdministrativeUnit, using systemId " + administrativeUnitSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return administrativeUnit;
    }

    private void createSequenceNumberGenerator(
            AdministrativeUnit administrativeUnit) {

        SequenceNumberGenerator sequenceNumberGenerator =
                new SequenceNumberGenerator();
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();

        administrativeUnit.setSequenceNumberGenerator(sequenceNumberGenerator);
        sequenceNumberGenerator.setAdministrativeUnitName(
                administrativeUnit.getAdministrativeUnitName());
        sequenceNumberGenerator.setReferenceAdministrativeUnit(
                administrativeUnit);
        sequenceNumberGenerator.setYear(year);
        sequenceNumberGenerator.setSequenceNumber(1);
        sequenceNumberGeneratorRepository.save(sequenceNumberGenerator);
    }
}
