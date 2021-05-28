package nikita.webapp.service.interfaces;

import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryExpansionHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.model.noark5.v5.secondary.SignOff;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IRegistryEntryService {

    // All save methods
    RegistryEntryHateoas save(@NotNull final RegistryEntry registryEntry);

    RegistryEntryHateoas expandRecordToRegistryEntry(
            @NotNull final Record record,
            @NotNull final PatchMerge patchMerge);

    PrecedenceHateoas createPrecedenceAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final Precedence precedence);

    SignOffHateoas
    createSignOffAssociatedWithRegistryEntry(
            @NotNull final UUID systemId,
            @NotNull final SignOff signOff);

    SignOffHateoas findAllSignOffAssociatedWithRegistryEntry(
            @NotNull final UUID systemId);

    SignOffHateoas findSignOffAssociatedWithRegistryEntry(
            @NotNull final UUID systemId,
            @NotNull final UUID signOffSystemId);

    DocumentFlowHateoas associateDocumentFlowWithRegistryEntry(
            @NotNull final UUID systemId,
            @NotNull final DocumentFlow documentFlow);

    // All find methods

    RegistryEntryHateoas findBySystemId(@NotNull final UUID systemId);

    /**
     * @return Page of RegistryEntry objects the user owns
     */
    RegistryEntryHateoas findAllRegistryEntry();

    DocumentFlowHateoas findAllDocumentFlowWithRegistryEntryBySystemId(
            @NotNull final UUID systemId);

    PrecedenceHateoas findAllPrecedenceForRegistryEntry(
            @NotNull final UUID systemId);

    // All UPDATE operations
    RegistryEntryHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final RegistryEntry incomingRegistryEntry);

    SignOffHateoas
    handleUpdateSignOff(
            @NotNull final UUID systemId,
            @NotNull final UUID signOffSystemID,
            @NotNull final Long version,
            @NotNull final SignOff incomingSignOff);

    // All DELETE operations
    void deleteEntity(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    void deleteSignOff(@NotNull final UUID systemId,
                       @NotNull final UUID signOffSystemId);

    PrecedenceHateoas generateDefaultPrecedence(
            @NotNull final UUID systemId);

    DocumentFlowHateoas generateDefaultDocumentFlow(
            @NotNull final UUID systemId);

    RegistryEntryHateoas generateDefaultRegistryEntry(
            @NotNull final UUID systemId);

    SignOffHateoas generateDefaultSignOff(
            @NotNull final UUID systemId);

    RegistryEntryExpansionHateoas generateDefaultExpandedRegistryEntry(
            @NotNull final UUID systemId);
}
