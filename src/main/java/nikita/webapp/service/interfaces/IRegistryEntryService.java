package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.Precedence;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IRegistryEntryService {

    // All save methods
    RegistryEntry save(RegistryEntry registryEntry);

    Precedence createPrecedenceAssociatedWithRecord(
            String registryEntrysystemID, Precedence precedence);

    SignOffHateoas
    createSignOffAssociatedWithRegistryEntry(String systemId,
                                             SignOff signOff);

    SignOffHateoas
    findAllSignOffAssociatedWithRegistryEntry(String systemId);

    SignOffHateoas
    findSignOffAssociatedWithRegistryEntry(String systemId,
                                           String subSystemId);

    // All find methods
    RegistryEntry findBySystemId(String systemId);

    List<RegistryEntry> findRegistryEntryByOwnerPaginated(Integer top, Integer skip);

    ResponseEntity<RegistryEntryHateoas> findAllRegistryEntryByCaseFile(
            CaseFile caseFile);


    // All UPDATE operations
    RegistryEntry handleUpdate(@NotNull final String systemId,
                               @NotNull final Long version,
                               @NotNull final RegistryEntry incomingRegistryEntry);

    SignOffHateoas
    handleUpdateSignOff(@NotNull final String systemID,
                        @NotNull final String signOffSystemID,
                        @NotNull final Long version,
                        @NotNull final SignOff incomingSignOff);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();

    void deleteSignOff(@NotNull String systemId,
                       @NotNull String signOffSystemId);

    ResponseEntity<RegistryEntryHateoas> generateDefaultRegistryEntry(
            @NotNull final String caseFileSystemId);

    SignOffHateoas generateDefaultSignOff
	(@NotNull final String registryEntrySystemId);
}
