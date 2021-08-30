package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface ICorrespondencePartService {

    CorrespondencePartPersonHateoas updateCorrespondencePartPerson(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final CorrespondencePartPerson incomingCorrespondencePart);

    CorrespondencePartUnitHateoas updateCorrespondencePartUnit(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final CorrespondencePartUnit incomingCorrespondencePart);

    CorrespondencePartInternalHateoas updateCorrespondencePartInternal(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final CorrespondencePartInternal incomingCorrespondencePart);

    CorrespondencePartUnitHateoas createNewCorrespondencePartUnit(
            @NotNull final CorrespondencePartUnit correspondencePartUnit,
            @NotNull final RecordEntity record);

    CorrespondencePartInternalHateoas createNewCorrespondencePartInternal(
            @NotNull final CorrespondencePartInternal correspondencePartUnit,
            @NotNull final RecordEntity record);

    CorrespondencePartPersonHateoas createNewCorrespondencePartPerson(
            @NotNull final CorrespondencePartPerson correspondencePartPerson,
            @NotNull final RecordEntity record);

    CorrespondencePart findBySystemId(@NotNull final UUID systemId);

    CorrespondencePartInternalHateoas
    findCorrespondencePartInternalBySystemId(@NotNull final UUID systemId);

    CorrespondencePartPersonHateoas
    findCorrespondencePartPersonBySystemId(@NotNull final UUID systemId);

    CorrespondencePartUnitHateoas
    findCorrespondencePartUnitBySystemId(@NotNull final UUID systemId);

    // All DELETE operations
    void deleteAllByOwnedBy();

    void deleteCorrespondencePartUnit(@NotNull final UUID systemId);

    void deleteCorrespondencePartPerson(@NotNull final UUID systemId);

    void deleteCorrespondencePartInternal(@NotNull final UUID systemId);

    // All template operations
    CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            @NotNull final UUID recordSystemId);

    CorrespondencePartPersonHateoas generateDefaultCorrespondencePartPerson(
            @NotNull final UUID recordSystemId);

    CorrespondencePartInternalHateoas generateDefaultCorrespondencePartInternal(
            @NotNull final UUID recordSystemId);

    CorrespondencePartHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final PatchObjects patchObjects);
}
