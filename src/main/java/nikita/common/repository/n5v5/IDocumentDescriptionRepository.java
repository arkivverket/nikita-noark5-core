package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IDocumentDescriptionRepository extends
        PagingAndSortingRepository<DocumentDescription, UUID> {

    // -- All SAVE operations
    @Override
    DocumentDescription save(DocumentDescription documentDescription);

    // -- All READ operations
    @Override
    List<DocumentDescription> findAll();

    // systemId
    DocumentDescription findBySystemId(UUID systemId);

    // ownedBy
    List<DocumentDescription> findByOwnedBy(String ownedBy);

    long deleteByOwnedBy(String ownedBy);

    Long countByReferenceRecord(Record record);
}
