package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface INationalIdentifierRepository
    extends NoarkEntityRepository<NationalIdentifier, UUID> {
}
