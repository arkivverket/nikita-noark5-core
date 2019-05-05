package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v4.casehandling.Precedence;
import nikita.common.model.noark5.v4.casehandling.RegistryEntry;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartUnitHateoas;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IRegistryEntryService {

    // All save methods
    RegistryEntry save(RegistryEntry registryEntry);

    CorrespondencePartPerson
    createCorrespondencePartPersonAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartPerson correspondencePart);

    CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            String registryEntrySystemId);

    CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartUnit correspondencePart);

    /*
    CorrespondencePartInternal createCorrespondencePartInternalAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartInternal correspondencePart);
*/
    Precedence createPrecedenceAssociatedWithRecord(
            String registryEntrysystemID, Precedence precedence);

    // All find methods
    RegistryEntry findBySystemId(String systemId);
    List<RegistryEntry> findRegistryEntryByOwnerPaginated(Integer top, Integer skip);

    List<CorrespondencePartPerson>
    getCorrespondencePartPersonAssociatedWithRegistryEntry(String systemID);

    /*
        List<CorrespondencePartInternal>
        getCorrespondencePartInternalAssociatedWithRegistryEntry(String systemID);
    */
    List<CorrespondencePartUnit>
    getCorrespondencePartUnitAssociatedWithRegistryEntry(String systemID);

    // All UPDATE operations
    RegistryEntry handleUpdate(@NotNull String systemId, @NotNull Long version,
                               @NotNull RegistryEntry incomingRegistryEntry);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);
}
