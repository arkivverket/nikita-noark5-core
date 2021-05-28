package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IClassificationSystemService {

    // -- All CREATE operations
    ClassificationSystemHateoas save(
            @NotNull final ClassificationSystem classificationSystem);

    ClassHateoas createClassAssociatedWithClassificationSystem(
            @NotNull final UUID systemId, @NotNull final Class klass);

    ClassHateoas generateDefaultClass(@NotNull final UUID systemId);

    // -- All READ operations
    ClassificationSystemHateoas findSingleClassificationSystem(
            @NotNull final UUID systemId);

    ClassificationSystemHateoas findAllClassificationSystem();

    ClassHateoas findAllClassAssociatedWithClassificationSystem(
            @NotNull final UUID systemId);

    SeriesHateoas findSeriesAssociatedWithClassificationSystem(
            @NotNull final UUID systemId);

    // All UPDATE operations
    ClassificationSystemHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final ClassificationSystem incomingClassificationSystem);

    // All DELETE operations
    void deleteClassificationSystem(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    ClassificationSystemHateoas generateDefaultClassificationSystem();
}
