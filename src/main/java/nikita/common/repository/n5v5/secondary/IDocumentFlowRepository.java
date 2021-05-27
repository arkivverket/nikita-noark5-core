package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDocumentFlowRepository
        extends PagingAndSortingRepository<DocumentFlow, UUID> {

    DocumentFlow findBySystemId(UUID systemId);
}
