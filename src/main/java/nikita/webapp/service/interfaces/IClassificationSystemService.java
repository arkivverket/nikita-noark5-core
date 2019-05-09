package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.Class;
import nikita.common.model.noark5.v4.ClassificationSystem;
import nikita.common.model.noark5.v4.hateoas.ClassHateoas;
import nikita.common.model.noark5.v4.hateoas.ClassificationSystemHateoas;

import javax.validation.constraints.NotNull;

public interface IClassificationSystemService {

	// -- All CREATE operations
    ClassificationSystemHateoas save(
            @NotNull ClassificationSystem classificationSystem,
            String outgoingAddress);

    ClassHateoas createClassAssociatedWithClassificationSystem(
            @NotNull String systemId, @NotNull Class klass,
            String outgoingAddress);

    ClassHateoas generateDefaultClass(@NotNull String classSystemId,
                                      String outgoingAddress);

    // -- All READ operations
    ClassificationSystemHateoas findSingleClassificationSystem(
            @NotNull String classificationSystemSystemId, String outgoingAddress);

    ClassificationSystemHateoas findAllClassificationSystem(String outgoingAddress);

    ClassHateoas findAllClassAssociatedWithClassificationSystem(
            @NotNull String classificationSystemSystemId, String outgoingAddress);

    // All UPDATE operations
    ClassificationSystemHateoas handleUpdate(
            @NotNull String systemId,
            @NotNull Long version,
            @NotNull ClassificationSystem incomingClassificationSystem,
            String outgoingAddress);

    // All DELETE operations
    void deleteEntity(@NotNull String classificationSystemSystemId);
}
