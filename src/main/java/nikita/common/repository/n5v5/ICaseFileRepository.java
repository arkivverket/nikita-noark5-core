package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.casehandling.CaseFile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICaseFileRepository
        extends PagingAndSortingRepository<CaseFile, UUID> {

    CaseFile findBySystemId(UUID systemId);

    long deleteByOwnedBy(String ownedBy);
}
