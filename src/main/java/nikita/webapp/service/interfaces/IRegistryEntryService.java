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
            String systemID, CorrespondencePartPerson correspondencePart);

    CorrespondencePartInternalHateoas generateDefaultCorrespondencePartInternal(
            String registryEntrySystemId);

    CorrespondencePartPersonHateoas generateDefaultCorrespondencePartPerson(
            String registryEntrySystemId);

    CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            String registryEntrySystemId);

    CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartUnit correspondencePart);

    CorrespondencePartInternalHateoas
    createCorrespondencePartInternalAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartInternal correspondencePart);

    Precedence createPrecedenceAssociatedWithRecord(
            String registryEntrysystemID, Precedence precedence);

    // All find methods
    RegistryEntry findBySystemId(String systemId);

    List<RegistryEntry> findRegistryEntryByOwnerPaginated(Integer top, Integer skip);

    CorrespondencePartPersonHateoas
    getCorrespondencePartPersonAssociatedWithRegistryEntry(String systemID);

    CorrespondencePartInternalHateoas
    getCorrespondencePartInternalAssociatedWithRegistryEntry(String systemID);

    CorrespondencePartUnitHateoas
    getCorrespondencePartUnitAssociatedWithRegistryEntry(String systemID);

    // All UPDATE operations
    RegistryEntry handleUpdate(@NotNull final String systemId,
                               @NotNull final Long version,
                               @NotNull final RegistryEntry incomingRegistryEntry);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
