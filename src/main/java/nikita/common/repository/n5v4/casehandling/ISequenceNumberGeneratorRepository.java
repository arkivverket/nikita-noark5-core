package nikita.common.repository.n5v4.casehandling;

import nikita.common.model.noark5.v4.casehandling.SequenceNumberGenerator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISequenceNumberGeneratorRepository
        extends CrudRepository<SequenceNumberGenerator, Long> {

    // -- All SAVE operations
    SequenceNumberGenerator save(SequenceNumberGenerator caseFile);

    // -- All READ operations
    Optional<SequenceNumberGenerator> findById(Long id);

    Optional<SequenceNumberGenerator>
    findByAdministrativeUnitAndYear(String administrativeUnit, Integer
            year);
}
