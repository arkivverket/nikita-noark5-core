package nikita.common.repository.n5v5.casehandling;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.casehandling.SequenceNumberGenerator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISequenceNumberGeneratorRepository
        extends CrudRepository<SequenceNumberGenerator, Long> {

    // -- All SAVE operations
    SequenceNumberGenerator save(SequenceNumberGenerator sequenceNumberGenerator);

    // -- All READ operations
    Optional<SequenceNumberGenerator> findById(Long id);

    Optional<SequenceNumberGenerator> findByReferenceAdministrativeUnitAndYear(
            AdministrativeUnit administrativeUnit, Integer year);
}
