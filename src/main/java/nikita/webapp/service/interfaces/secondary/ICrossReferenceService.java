package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.CrossReferenceHateoas;
import nikita.common.model.noark5.v5.secondary.CrossReference;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface ICrossReferenceService {

    CrossReferenceHateoas createCrossReferenceAssociatedWithFile(
            @NotNull final CrossReference crossReference,
            @NotNull final File file);

    CrossReferenceHateoas createCrossReferenceAssociatedWithRecord(
            @NotNull final CrossReference crossReference,
            @NotNull final Record record);

    CrossReferenceHateoas createCrossReferenceAssociatedWithClass(
            @NotNull final CrossReference crossReference,
            @NotNull final Class klass);

    CrossReferenceHateoas findBySystemId(@NotNull final UUID systemId);

    CrossReferenceHateoas findAllByOwner();

    void deleteCrossReferenceBySystemId(@NotNull final UUID systemId);

    CrossReferenceHateoas getDefaultCrossReference();

    CrossReferenceHateoas updateCrossReferenceBySystemId(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final CrossReference crossReference);
}
