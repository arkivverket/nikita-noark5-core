package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.Class;
import nikita.common.model.noark5.v4.ClassificationSystem;
import nikita.common.model.noark5.v4.hateoas.ClassHateoas;
import nikita.common.model.noark5.v4.hateoas.ClassificationSystemHateoas;

import javax.validation.constraints.NotNull;

public interface IClassificationSystemService {

	// -- All CREATE operations
    ClassificationSystemHateoas createNewClassificationSystem(
            @NotNull ClassificationSystem classificationSystem);

    ClassHateoas createClassAssociatedWithClassificationSystem(
            @NotNull String systemId, @NotNull Class klass);

	// -- All READ operations
    ClassificationSystemHateoas findSingleClassificationSystem(
            @NotNull String classificationSystemSystemId);

    ClassificationSystemHateoas findAllClassificationSystem();

    // All UPDATE operations
    ClassificationSystemHateoas handleUpdate(
            @NotNull String systemId,
            @NotNull Long version,
            @NotNull ClassificationSystem incomingClassificationSystem);

    // All DELETE operations
    void deleteEntity(@NotNull String classificationSystemSystemId);
}
