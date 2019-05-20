package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.BasicRecord;
import nikita.common.model.noark5.v4.File;
import nikita.common.model.noark5.v4.Record;
import nikita.common.model.noark5.v4.hateoas.ClassHateoas;
import nikita.common.model.noark5.v4.hateoas.FileHateoas;
import nikita.common.model.noark5.v4.hateoas.SeriesHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface IFileService {

    // -- All CREATE operations
    File createFile(File file);

    FileHateoas save(File file);

    Record createRecordAssociatedWithFile(String fileSystemId, Record record);

    BasicRecord createBasicRecordAssociatedWithFile(
            String fileSystemId, BasicRecord basicRecord);

    // -- All READ operations
    List<File> findAll();

    Optional<File> findById(Long id);

    File findBySystemId(String systemId);

    List<File> findByOwnedBy(String ownedBy);

    ResponseEntity<ClassHateoas>
    findClassAssociatedWithFile(@NotNull final String systemId);

    ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithFile(@NotNull final String systemId);

    // -- All UPDATE operations
    File handleUpdate(@NotNull String systemId,
                      @NotNull Long version,
                      @NotNull File file);

    // -- All DELETE operations
    void deleteEntity(@NotNull String systemId);

    FileHateoas generateDefaultFile();

    long deleteAllByOwnedBy();
}
