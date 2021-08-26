package nikita.webapp.service.interfaces.casehandling;

import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteExpansionHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IRecordNoteService {

    // All save methods
    RecordNoteHateoas save(@NotNull final RecordNote recordNote);

    RecordNoteHateoas expandRecordToRecordNote(
            @NotNull final RecordEntity record,
            @NotNull final PatchMerge patchMerge);

    // All find methods
    RecordNoteHateoas findBySystemId(@NotNull final UUID systemId);

    RecordNoteHateoas findAll();

    DocumentFlowHateoas findAllDocumentFlowWithRecordNoteBySystemId(
            @NotNull final UUID systemId);

    DocumentFlowHateoas associateDocumentFlowWithRecordNote(
            @NotNull final UUID systemId,
            @NotNull final DocumentFlow documentFlow);

    // All UPDATE operations
    RecordNoteHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final RecordNote incomingRecordNote);

    // All DELETE operations
    String deleteEntity(@NotNull final UUID systemId);

    String deleteAllByOwnedBy();

    RecordNoteHateoas generateDefaultRecordNote(
            @NotNull final UUID systemId);

    DocumentFlowHateoas generateDefaultDocumentFlow(
            @NotNull final UUID systemId);

    RecordNoteExpansionHateoas generateDefaultExpandedRecordNote(
            @NotNull final UUID systemId);
}
