package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDocumentDescriptionRepository extends
        PagingAndSortingRepository<DocumentDescription, UUID> {

    DocumentDescription findBySystemId(UUID systemId);

    Long countByReferenceRecord(Record record);

    long deleteByOwnedBy(String user);
}
