package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v4.casehandling.Precedence;
import nikita.common.model.noark5.v4.casehandling.RegistryEntry;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartUnitHateoas;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IRegistryEntryService {

    // All save methods
    RegistryEntry save(RegistryEntry registryEntry);

    CorrespondencePartPersonHateoas
    createCorrespondencePartPersonAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartPerson correspondencePart, String outgoingAddress);

    CorrespondencePartInternalHateoas generateDefaultCorrespondencePartInternal(
            String registryEntrySystemId, String outgoingAddress);

    CorrespondencePartPersonHateoas generateDefaultCorrespondencePartPerson(
            String registryEntrySystemId, String outgoingAddress);

    CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            String registryEntrySystemId, String outgoingAddress);

    CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartUnit correspondencePart, String outgoingAddress);

    CorrespondencePartInternalHateoas
    createCorrespondencePartInternalAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartInternal correspondencePart, String outgoingAddress);

    Precedence createPrecedenceAssociatedWithRecord(
            String registryEntrysystemID, Precedence precedence);

    // All find methods
    RegistryEntry findBySystemId(String systemId);

    List<RegistryEntry> findRegistryEntryByOwnerPaginated(Integer top, Integer skip);

    CorrespondencePartPersonHateoas
    getCorrespondencePartPersonAssociatedWithRegistryEntry(String systemID, String outgoingAddress);

    CorrespondencePartInternalHateoas
    getCorrespondencePartInternalAssociatedWithRegistryEntry(String systemID, String outgoingAddress);

    CorrespondencePartUnitHateoas
    getCorrespondencePartUnitAssociatedWithRegistryEntry(String systemID, String outgoingAddress);

    // All UPDATE operations
    RegistryEntry handleUpdate(@NotNull String systemId, @NotNull Long version,
                               @NotNull RegistryEntry incomingRegistryEntry, String outgoingAddress);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);
}
