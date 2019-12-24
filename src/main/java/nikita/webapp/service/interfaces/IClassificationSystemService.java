package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public interface IClassificationSystemService {

	// -- All CREATE operations
    ClassificationSystemHateoas save(
            @NotNull ClassificationSystem classificationSystem);

    ClassHateoas createClassAssociatedWithClassificationSystem(
            @NotNull String systemId, @NotNull Class klass);

    ClassHateoas generateDefaultClass(@NotNull String classSystemId);

    // -- All READ operations
    ClassificationSystemHateoas findSingleClassificationSystem(
            @NotNull String classificationSystemSystemId);

    ClassificationSystemHateoas findAllClassificationSystem();

    ClassHateoas findAllClassAssociatedWithClassificationSystem(
            @NotNull String classificationSystemSystemId);

    ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithClassificationSystem(
            @NotNull final String systemId);

    // All UPDATE operations
    ClassificationSystemHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final ClassificationSystem incomingClassificationSystem);

    // All DELETE operations
    void deleteEntity(@NotNull String classificationSystemSystemId);

    long deleteAllByOwnedBy();

    ClassificationSystemHateoas generateDefaultClassificationSystem();
}
