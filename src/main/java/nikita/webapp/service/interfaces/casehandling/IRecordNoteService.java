package nikita.webapp.service.interfaces.casehandling;

import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public interface IRecordNoteService {

    // All save methods
    ResponseEntity<RecordNoteHateoas> save(
            @NotNull final RecordNote recordNote);

    // All find methods
    ResponseEntity<RecordNoteHateoas> findBySystemId(
            @NotNull final String systemId);

    ResponseEntity<RecordNoteHateoas> findAllByOwner();

    ResponseEntity<RecordNoteHateoas> findAllRecordNoteByCaseFile(
            CaseFile caseFile);

    // All UPDATE operations
    ResponseEntity<RecordNoteHateoas> handleUpdate(
            @NotNull final String systemId,
            @NotNull final RecordNote incomingRecordNote);

    // All DELETE operations
    ResponseEntity<Count> deleteEntity(@NotNull final String systemId);

    ResponseEntity<Count> deleteAllByOwnedBy();

    ResponseEntity<RecordNoteHateoas> generateDefaultRecordNote(
            @NotNull final String caseFilSystemId);
}
