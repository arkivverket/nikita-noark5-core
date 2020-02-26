package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IDocumentFlowRepository
        extends CrudRepository<DocumentFlow, INoarkEntity> {

    DocumentFlow findBySystemId(UUID systemId);

    List<DocumentFlow> findByOwnedBy(String ownedBy);
}
