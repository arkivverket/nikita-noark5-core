package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.hateoas.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.PartUnitHateoas;

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
