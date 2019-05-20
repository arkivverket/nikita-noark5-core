package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.Class;
import nikita.common.model.noark5.v4.ClassificationSystem;
import nikita.common.model.noark5.v4.hateoas.ClassHateoas;
import nikita.common.model.noark5.v4.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v4.hateoas.SeriesHateoas;
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
}
