package nikita.common.repository.n5v5.casehandling;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.casehandling.SequenceNumberGenerator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISequenceNumberGeneratorRepository
        extends CrudRepository<SequenceNumberGenerator, AdministrativeUnit> {

    // -- All SAVE operations
    SequenceNumberGenerator save(SequenceNumberGenerator sequenceNumberGenerator);

    Optional<SequenceNumberGenerator> findByReferenceAdministrativeUnitAndYear(
            AdministrativeUnit administrativeUnit, Integer year);
}
