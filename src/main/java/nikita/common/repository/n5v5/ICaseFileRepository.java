package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICaseFileRepository
        extends PagingAndSortingRepository<CaseFile, UUID> {

    // -- All SAVE operations
    CaseFile save(CaseFile caseFile);

    // -- All READ operations
    // systemId
    CaseFile findBySystemId(UUID systemId);

    List<CaseFile> findByReferenceSeries(Series series);

    Page<CaseFile> findByReferenceSeries(Series series, Pageable page);

    long deleteByOwnedBy(String ownedBy);
}
