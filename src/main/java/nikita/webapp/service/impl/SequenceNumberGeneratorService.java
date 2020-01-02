package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.casehandling.SequenceNumberGenerator;
import nikita.common.repository.n5v5.casehandling.ISequenceNumberGeneratorRepository;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.ISequenceNumberGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Service class for SequenceNumberGenerator
 * <p>
 * Returns the next sequence number for a given administrativeUnit. Will also
 * create a new SequenceNumberGenerator for an adminstrativeUnit when a new
 * year changes.
 */
@Service
@Transactional
public class SequenceNumberGeneratorService
        implements ISequenceNumberGeneratorService {

    private static final Logger logger =
            LoggerFactory.getLogger(SequenceNumberGenerator.class);

    private ISequenceNumberGeneratorRepository numberGeneratorRepository;

    public SequenceNumberGeneratorService(
            ISequenceNumberGeneratorRepository numberGeneratorRepository) {
        this.numberGeneratorRepository = numberGeneratorRepository;
    }

    /**
     * Get the next sequence number for a given administrativeUnit. If no
     * sequence number exists, attempt to create a sequenceNumberGenerator.
     * If that fails throw an Exception. This will auto increment the
     * sequenceNumberGenerator by 1.
     *
     * @param administrativeUnit administrativeUnit that you want sequence
     *                           number for
     * @return the next sequence number
     */
    public Integer getNextCaseFileSequenceNumber(
            AdministrativeUnit administrativeUnit) {
        Integer currentYear = OffsetDateTime.now().getYear();

        Optional<SequenceNumberGenerator> nextSequenceOptional =
                numberGeneratorRepository
                        .findByReferenceAdministrativeUnitAndYear(
                                administrativeUnit, currentYear);

        if (nextSequenceOptional.isPresent()) {
            return getNextCaseFileSequence(nextSequenceOptional.get());
        } else {
            return generateNewSequenceGenerator(administrativeUnit, currentYear);
        }
    }

    /**
     * Get the next sequence number for a given administrativeUnit. If no
     * sequence number exists, attempt to create a sequenceNumberGenerator.
     * If that fails throw an Exception. This will auto increment the
     * sequenceNumberGenerator by 1.
     *
     * @param administrativeUnit administrativeUnit that you want sequence
     *                           number for
     * @return the next sequence number
     */
    public Integer getNextRecordSequenceNumber(
            AdministrativeUnit administrativeUnit) {
        Integer currentYear = OffsetDateTime.now().getYear();

        Optional<SequenceNumberGenerator> nextSequenceOptional =
                numberGeneratorRepository
                        .findByReferenceAdministrativeUnitAndYear(
                                administrativeUnit, currentYear);

        if (nextSequenceOptional.isPresent()) {
            return getNextRecordSequence(nextSequenceOptional.get());
        } else {
            return generateNewRecordSequenceGenerator(administrativeUnit, currentYear);
        }
    }

    /**
     * create a sequenceNumberGenerator object.
     *
     * @param administrativeUnit administrativeUnit
     * @return the newly persisted SequenceNumberGenerator
     */
    @Override
    public SequenceNumberGenerator createSequenceNumberGenerator(
            @NotNull AdministrativeUnit administrativeUnit) {

        SequenceNumberGenerator sequenceNumberGenerator =
                new SequenceNumberGenerator(LocalDate.now().getYear(),
                        administrativeUnit);

        sequenceNumberGenerator.setAdministrativeUnitName(
                administrativeUnit.getAdministrativeUnitName());
        sequenceNumberGenerator.setCaseFileSequenceNumber(1);
        sequenceNumberGenerator.setRecordSequenceNumber(1);
        sequenceNumberGenerator
                .setReferenceAdministrativeUnit(administrativeUnit);
        administrativeUnit.
                addReferenceSequenceNumberGenerator(sequenceNumberGenerator);
        return sequenceNumberGenerator;
    }

    /**
     * Retrieve the sequence number and increment by 1 so next call will be
     * +1
     *
     * @param nextSequence The SequenceNumberGenerator from the database
     * @return the next available sequence number
     */
    private Integer getNextCaseFileSequence(
            SequenceNumberGenerator nextSequence) {
        Integer sequenceNumber = nextSequence.getCaseFileSequenceNumber();
        // increment and save the value
        nextSequence.incrementCaseFileByOne();
        numberGeneratorRepository.save(nextSequence);
        return sequenceNumber;
    }

    /**
     * Retrieve the sequence number and increment by 1 so next call will be
     * +1
     *
     * @param nextSequence The SequenceNumberGenerator from the database
     * @return the next available sequence number
     */
    private Integer getNextRecordSequence(
            SequenceNumberGenerator nextSequence) {
        Integer sequenceNumber = nextSequence.getRecordSequenceNumber();
        // increment and save the value
        nextSequence.incrementRecordByOne();
        numberGeneratorRepository.save(nextSequence);
        return sequenceNumber;
    }

    private Integer generateNewSequenceGenerator(
            AdministrativeUnit administrativeUnit, Integer currentYear) {

        SequenceNumberGenerator sequenceNumberGenerator =
                createSequenceNumberGenerator(administrativeUnit);

        if (sequenceNumberGenerator != null) {
            return sequenceNumberGenerator.getCaseFileSequenceNumber();
        } else {
            logger.error("Error missing sequencenumber " +
                    "generator for " + administrativeUnit + " and year " +
                    currentYear.toString());
            throw new NikitaException("Error missing sequencenumber " +
                    "generator for " + administrativeUnit + " and year " +
                    currentYear.toString());
        }
    }

    private Integer generateNewRecordSequenceGenerator(
            AdministrativeUnit administrativeUnit, Integer currentYear) {

        SequenceNumberGenerator sequenceNumberGenerator =
                createSequenceNumberGenerator(administrativeUnit);

        if (sequenceNumberGenerator != null) {
            return getNextRecordSequence(sequenceNumberGenerator);
        } else {
            logger.error("Error missing sequencenumber " +
                    "generator for " + administrativeUnit + " and year " +
                    currentYear.toString());
            throw new NikitaException("Error missing sequencenumber " +
                    "generator for " + administrativeUnit + " and year " +
                    currentYear.toString());
        }
    }
}
