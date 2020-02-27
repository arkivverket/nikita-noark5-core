package nikita.webapp.service.interfaces.casehandling;

import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
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

    DocumentFlowHateoas findAllDocumentFlowWithRecordNoteBySystemId
        (String systemID);

    DocumentFlowHateoas associateDocumentFlowWithRecordNote
        (String systemId, DocumentFlow documentFlow);

    // All UPDATE operations
    ResponseEntity<RecordNoteHateoas> handleUpdate(
            @NotNull final String systemId,
            @NotNull final RecordNote incomingRecordNote);

    // All DELETE operations
    ResponseEntity<String> deleteEntity(@NotNull final String systemId);

    ResponseEntity<String> deleteAllByOwnedBy();

    ResponseEntity<RecordNoteHateoas> generateDefaultRecordNote(
            @NotNull final String caseFilSystemId);

    DocumentFlowHateoas generateDefaultDocumentFlow(String systemID);
}
