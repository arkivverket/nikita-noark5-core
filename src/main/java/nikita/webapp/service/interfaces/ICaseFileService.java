package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.Series;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.model.noark5.v4.casehandling.RegistryEntry;
import nikita.common.model.noark5.v4.hateoas.casehandling.CaseFileHateoas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface ICaseFileService {

    CaseFile save(@NotNull CaseFile caseFile);

    RegistryEntry createRegistryEntryAssociatedWithCaseFile(
            @NotNull String fileSystemId, @NotNull RegistryEntry registryEntry);

    List<CaseFile> findAllCaseFileBySeries(@NotNull Series series);

    Page<CaseFile> findByReferenceSeries(@NotNull Series series, Pageable page);

    CaseFile findBySystemId(@NotNull String systemId);

    List<CaseFile> findCaseFileByOwnerPaginated(
            Integer top, Integer skip);
    // All UPDATE operations
    CaseFile handleUpdate(@NotNull String systemId, @NotNull Long version,
                          @NotNull CaseFile incomingCaseFile);

    CaseFileHateoas generateDefaultCaseFile();

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);
}
