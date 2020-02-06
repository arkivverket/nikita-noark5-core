package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.secondary.Part;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.model.noark5.v5.hateoas.secondary.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;

import javax.validation.constraints.NotNull;

public interface IPartService {

    PartPerson updatePartPerson(
            @NotNull String systemId, @NotNull Long version,
            @NotNull PartPerson incomingPart);

    PartUnit updatePartUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull PartUnit incomingPart);

    PartUnitHateoas createNewPartUnit(
            @NotNull PartUnit partUnit,
            @NotNull Record record);

    PartPersonHateoas createNewPartPerson(
            @NotNull PartPerson partPerson,
            @NotNull Record record);

    PartUnitHateoas createNewPartUnit(
            @NotNull PartUnit partUnit,
            @NotNull File file);

    PartPersonHateoas createNewPartPerson(
            @NotNull PartPerson partPerson,
            @NotNull File file);

    PartUnitHateoas createNewPartUnit(
            @NotNull PartUnit partUnit,
            @NotNull DocumentDescription documentDescription);

    PartPersonHateoas createNewPartPerson(
            @NotNull PartPerson partPerson,
            @NotNull DocumentDescription documentDescription);

    Part findBySystemId(@NotNull String systemID);

    // All DELETE operations

    void deletePartUnit(@NotNull String systemID);

    void deletePartPerson(@NotNull String systemID);

    // All template operations
    PartUnitHateoas generateDefaultPartUnit(
            String recordSystemId);

    PartPersonHateoas generateDefaultPartPerson(
            String recordSystemId);
}
