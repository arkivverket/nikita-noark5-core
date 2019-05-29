package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface ICaseFileService {

    CaseFile save(@NotNull CaseFile caseFile);

    CaseFileHateoas saveHateoas(@NotNull CaseFile caseFile);

    RegistryEntry createRegistryEntryAssociatedWithCaseFile(
            @NotNull String fileSystemId, @NotNull RegistryEntry registryEntry);

    ResponseEntity<CaseFileHateoas> findAllCaseFileBySeries(@NotNull Series series);

    CaseFile findBySystemId(@NotNull String systemId);

    List<CaseFile> findCaseFileByOwnerPaginated(
            Integer top, Integer skip);
    // All UPDATE operations
    CaseFile handleUpdate(@NotNull final String systemId,
                          @NotNull final Long version,
                          @NotNull final CaseFile incomingCaseFile);

    CaseFileHateoas generateDefaultCaseFile();

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
