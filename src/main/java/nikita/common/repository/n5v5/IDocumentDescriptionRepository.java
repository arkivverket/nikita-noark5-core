package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDocumentDescriptionRepository extends
        PagingAndSortingRepository<DocumentDescription, UUID> {

    DocumentDescription findBySystemId(UUID systemId);

    Page<DocumentDescription> findByOwnedBy(String ownedBy, Pageable pageable);

    Long countByReferenceRecord(Record record);
}
