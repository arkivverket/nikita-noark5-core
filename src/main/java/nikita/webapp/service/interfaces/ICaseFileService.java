package nikita.webapp.service.interfaces;

import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileExpansionHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.secondary.Precedence;

import javax.validation.constraints.NotNull;
import java.util.UUID;


public interface ICaseFileService {

    CaseFileHateoas save(@NotNull final CaseFile caseFile);

    CaseFileHateoas expandFileAsCaseFileHateoas(
            @NotNull final File file,
            @NotNull final PatchMerge patchMerge);

    PrecedenceHateoas createPrecedenceAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final Precedence precedence);

    RegistryEntryHateoas createRegistryEntryAssociatedWithCaseFile(
            @NotNull final UUID systemId,
            @NotNull final RegistryEntry registryEntry);

    RecordNoteHateoas createRecordNoteToCaseFile(
            @NotNull final UUID systemId,
            @NotNull final RecordNote RecordNote);

    CaseFileHateoas findBySystemId(@NotNull final UUID systemId);

    CaseFileHateoas findAll();

    PrecedenceHateoas findAllPrecedenceForCaseFile(
            @NotNull final UUID systemId);

    RecordNoteHateoas findAllRecordNoteToCaseFile(
            @NotNull final UUID systemId);

    RegistryEntryHateoas findAllRegistryEntryToCaseFile(
            @NotNull final UUID systemId);

    // All UPDATE operations
    CaseFileHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final CaseFile incomingCaseFile);

    CaseFileHateoas generateDefaultCaseFile();

    RecordNoteHateoas generateDefaultRecordNote(
            @NotNull final UUID systemId);

    RegistryEntryHateoas generateDefaultRegistryEntry(
            @NotNull final UUID caseFileSystemId);

    PrecedenceHateoas generateDefaultPrecedence(
            @NotNull final UUID systemId);

    CaseFileExpansionHateoas generateDefaultExpandedCaseFile();

    // All DELETE operations

    void deleteEntity(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    CaseFileHateoas createCaseFileToCaseFile(
            @NotNull final UUID systemId,
            @NotNull final CaseFile caseFile);
}
