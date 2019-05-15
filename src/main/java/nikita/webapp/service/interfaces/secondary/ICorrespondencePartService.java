package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v4.casehandling.RegistryEntry;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePart;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartUnitHateoas;

import javax.validation.constraints.NotNull;

public interface ICorrespondencePartService {

    CorrespondencePartPerson updateCorrespondencePartPerson(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CorrespondencePartPerson incomingCorrespondencePart);

    CorrespondencePartUnit updateCorrespondencePartUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CorrespondencePartUnit incomingCorrespondencePart);

    CorrespondencePartInternal updateCorrespondencePartInternal(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CorrespondencePartInternal incomingCorrespondencePart);

    CorrespondencePartUnitHateoas createNewCorrespondencePartUnit(
            @NotNull CorrespondencePartUnit correspondencePartUnit,
            @NotNull RegistryEntry registryEntry);

    CorrespondencePartInternalHateoas createNewCorrespondencePartInternal(
            @NotNull CorrespondencePartInternal correspondencePartUnit,
            @NotNull RegistryEntry registryEntry);

    CorrespondencePartPersonHateoas createNewCorrespondencePartPerson(
            @NotNull CorrespondencePartPerson correspondencePartPerson,
            @NotNull RegistryEntry registryEntry);

    CorrespondencePart findBySystemId(@NotNull String systemID);

    // All DELETE operations
    long deleteAllByOwnedBy();

    void deleteCorrespondencePartUnit(@NotNull String systemID);

    void deleteCorrespondencePartPerson(@NotNull String systemID);

    void deleteCorrespondencePartInternal(@NotNull String systemID);

    // All template operations
    CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            String registryEntrySystemId);

    CorrespondencePartPersonHateoas generateDefaultCorrespondencePartPerson(
            String registryEntrySystemId);

    CorrespondencePartInternalHateoas generateDefaultCorrespondencePartInternal(
            String registryEntrySystemId);


}
