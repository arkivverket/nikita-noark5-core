package nikita.common.repository.n5v5.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.PrecedenceStatus;

import java.util.List;

public interface IPrecedenceStatusRepository
        extends MetadataRepository<PrecedenceStatus, String> {
    List<PrecedenceStatus> findAll();
}
