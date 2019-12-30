package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRecordRepository extends
        PagingAndSortingRepository<Record, UUID> {

    // -- All SAVE operations
    @Override
    Record save(Record record);

    // -- All READ operations
    @Override
    List<Record> findAll();

    List<Record> findAllByReferenceDocumentDescription(
            DocumentDescription documentDescription);

    // systemId
    Record findBySystemId(UUID systemId);

    // ownedBy
    List<Record> findByOwnedBy(String ownedBy);

    long deleteByOwnedBy(String ownedBy);
}
