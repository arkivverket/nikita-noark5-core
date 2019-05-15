package nikita.common.repository.n5v4;

import nikita.common.model.noark5.v4.Series;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICaseFileRepository
        extends PagingAndSortingRepository<CaseFile, Long> {

    // -- All SAVE operations
    CaseFile save(CaseFile caseFile);

    // -- All READ operations
    // systemId
    CaseFile findBySystemId(String systemId);

    List<CaseFile> findByReferenceSeries(Series series);

    Page<CaseFile> findByReferenceSeries(Series series, Pageable page);

    long deleteByOwnedBy(String ownedBy);
}
