package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.*;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface IRecordService {

    // -- All CREATE operations
    RecordHateoas save(Record record);

    Record create(Record record);

    DocumentDescriptionHateoas createDocumentDescriptionAssociatedWithRecord(
            String systemID, DocumentDescription documentDescription);

    // -- All READ operations
    List<Record> findAll();

    ResponseEntity<RecordHateoas>
    findByReferenceDocumentDescription(@NotNull final String systemId);

    ResponseEntity<FileHateoas>
    findFileAssociatedWithRecord(@NotNull final String systemId);

    ResponseEntity<ClassHateoas>
    findClassAssociatedWithRecord(@NotNull final String systemId);

    ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithRecord(@NotNull final String systemId);

    Optional<Record> findById(Long id);

    Record findBySystemId(String systemId);

    List<Record> findByOwnedBy(String ownedBy);

    // -- All UPDATE operations
    Record handleUpdate(@NotNull String systemId, @NotNull Long version, @NotNull Record record);

    // -- All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
