package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.PartHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;
import nikita.common.model.noark5.v5.secondary.Part;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IPartService {

    PartPersonHateoas updatePartPerson(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final PartPerson incomingPart);

    PartUnitHateoas updatePartUnit(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final PartUnit incomingPart);

    PartUnitHateoas createNewPartUnit(
            @NotNull final PartUnit partUnit,
            @NotNull final Record record);

    PartHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final PatchObjects patchObjects);

    PartPersonHateoas createNewPartPerson(
            @NotNull final PartPerson partPerson,
            @NotNull final Record record);

    PartUnitHateoas createNewPartUnit(
            @NotNull final PartUnit partUnit,
            @NotNull final File file);

    PartPersonHateoas createNewPartPerson(
            @NotNull final PartPerson partPerson,
            @NotNull final File file);

    PartUnitHateoas createNewPartUnit(
            @NotNull final PartUnit partUnit,
            @NotNull final DocumentDescription documentDescription);

    PartPersonHateoas createNewPartPerson(
            @NotNull final PartPerson partPerson,
            @NotNull final DocumentDescription documentDescription);

    Part findBySystemId(@NotNull final UUID systemId);

    PartPersonHateoas
    findPartPersonBySystemId(@NotNull final UUID systemId);

    PartUnitHateoas
    findPartUnitBySystemId(@NotNull final UUID systemId);

    // All DELETE operations

    void deletePartUnit(@NotNull final UUID systemId);

    void deletePartPerson(@NotNull final UUID systemId);

    // All template operations
    PartUnitHateoas generateDefaultPartUnit(
            @NotNull final UUID systemId);

    PartPersonHateoas generateDefaultPartPerson(
            @NotNull final UUID systemId);
}
