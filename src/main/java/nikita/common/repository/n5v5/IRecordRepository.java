package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.RecordEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRecordRepository extends
        PagingAndSortingRepository<RecordEntity, UUID> {

    List<RecordEntity> findAllByReferenceDocumentDescription(
            DocumentDescription documentDescription);

    RecordEntity findBySystemId(UUID systemId);

    long deleteByOwnedBy(String ownedBy);
}
