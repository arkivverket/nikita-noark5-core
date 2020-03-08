package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.secondary.Precedence;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface ICaseFileService {

    CaseFile save(@NotNull CaseFile caseFile);

    CaseFileHateoas saveHateoas(@NotNull CaseFile caseFile);

    PrecedenceHateoas createPrecedenceAssociatedWithFile(
            String caseFileSystemID, Precedence precedence);

    RegistryEntry createRegistryEntryAssociatedWithCaseFile(
            @NotNull String fileSystemId, @NotNull RegistryEntry registryEntry);

    ResponseEntity<RecordNoteHateoas> createRecordNoteToCaseFile(
            @NotNull String fileSystemId, @NotNull RecordNote RecordNote);

    ResponseEntity<CaseFileHateoas> findAllCaseFileBySeries(@NotNull Series series);

    CaseFile findBySystemId(@NotNull String systemId);

    List<CaseFile> findCaseFileByOwnerPaginated(
            Integer top, Integer skip);

    PrecedenceHateoas findAllPrecedenceForCaseFile(
            @NotNull final String systemID);

    ResponseEntity<RecordNoteHateoas> findAllRecordNoteToCaseFile(
            @NotNull final String caseFileSystemId);

    ResponseEntity<RegistryEntryHateoas> findAllRegistryEntryToCaseFile(
            @NotNull final String caseFileSystemId);

    // All UPDATE operations
    CaseFile handleUpdate(@NotNull final String systemId,
                          @NotNull final Long version,
                          @NotNull final CaseFile incomingCaseFile);

    CaseFileHateoas generateDefaultCaseFile();

    ResponseEntity<RecordNoteHateoas> generateDefaultRecordNote(
            @NotNull final String caseFileSystemId);

    ResponseEntity<RegistryEntryHateoas> generateDefaultRegistryEntry(
            @NotNull final String caseFileSystemId);

    PrecedenceHateoas generateDefaultPrecedence(String systemID);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
