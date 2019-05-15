package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.ClassificationSystem;
import nikita.common.model.noark5.v4.File;
import nikita.common.model.noark5.v4.Series;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.model.noark5.v4.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v4.hateoas.casehandling.CaseFileHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface ISeriesService {


    // -- All CREATE operations
    Series save(Series series);

    File createFileAssociatedWithSeries(String seriesSystemId, File file);

    CaseFile createCaseFileAssociatedWithSeries(String seriesSystemId,
                                                CaseFile caseFile);

    ClassificationSystemHateoas createClassificationSystem(
            String systemId,
            ClassificationSystem classificationSystem);

    // -- All READ operations
    List<Series> findAll();

    ResponseEntity<CaseFileHateoas>
    findCaseFilesBySeries(@NotNull String systemId);

    // id
    Optional<Series> findById(Long id);

    // systemId
    Series findBySystemId(String systemId);

    // ownedBy
    List<Series> findByOwnedBy(String ownedBy);

    // All UPDATE operations
    Series handleUpdate(@NotNull final String systemId,
                        @NotNull final Long version,
                        @NotNull final Series incomingSeries);

    // All DELETE operations
    int deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
