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
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IPartService {

    PartPerson updatePartPerson(
            @NotNull UUID systemId, @NotNull Long version,
            @NotNull PartPerson incomingPart);

    PartUnit updatePartUnit(
            @NotNull UUID systemId, @NotNull Long version,
            @NotNull PartUnit incomingPart);

    PartUnitHateoas createNewPartUnit(
            @NotNull PartUnit partUnit,
            @NotNull Record record);

    ResponseEntity<PartHateoas>
    handleUpdate(UUID systemID, PatchObjects patchObjects);

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

    Part findBySystemId(@NotNull UUID systemId);

    // All DELETE operations

    void deletePartUnit(@NotNull UUID systemId);

    void deletePartPerson(@NotNull UUID systemId);

    // All template operations
    PartUnitHateoas generateDefaultPartUnit(
            String recordSystemId);

    PartPersonHateoas generateDefaultPartPerson(
            String recordSystemId);
}
