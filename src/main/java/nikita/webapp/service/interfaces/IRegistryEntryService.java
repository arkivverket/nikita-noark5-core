package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.Precedence;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IRegistryEntryService {

    // All save methods
    RegistryEntry save(RegistryEntry registryEntry);

    Precedence createPrecedenceAssociatedWithRecord(
            String registryEntrysystemID, Precedence precedence);

    // All find methods
    RegistryEntry findBySystemId(String systemId);

    List<RegistryEntry> findRegistryEntryByOwnerPaginated(Integer top, Integer skip);

    ResponseEntity<RegistryEntryHateoas> findAllRegistryEntryByCaseFile(
            CaseFile caseFile);


    // All UPDATE operations
    RegistryEntry handleUpdate(@NotNull final String systemId,
                               @NotNull final Long version,
                               @NotNull final RegistryEntry incomingRegistryEntry);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();

    ResponseEntity<RegistryEntryHateoas> generateDefaultRegistryEntry(
            @NotNull final String caseFileSystemId);
}
