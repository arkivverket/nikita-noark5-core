package nikita.common.repository.n5v5.casehandling;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRecordNoteRepository extends
        PagingAndSortingRepository<RecordNote, String> {

    RecordNote findBySystemId(UUID systemId);

    List<RecordNote> findByOwnedBy(String ownedBy);

    List<RecordNote> findByReferenceFile(File file);

    long deleteByOwnedBy(String ownedBy);

    long countByOwnedBy(String ownedBy);
}
