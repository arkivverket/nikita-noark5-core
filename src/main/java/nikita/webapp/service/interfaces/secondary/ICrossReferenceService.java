package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
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
            @NotNull final RecordEntity record);

    CrossReferenceHateoas createCrossReferenceAssociatedWithClass(
            @NotNull final CrossReference crossReference,
            @NotNull final Class klass);

    CrossReferenceHateoas findBySystemId(@NotNull final UUID systemId);

    CrossReferenceHateoas findAll();

    void deleteCrossReferenceBySystemId(@NotNull final UUID systemId);

    CrossReferenceHateoas getDefaultCrossReference(
            @NotNull final UUID systemId);

    CrossReferenceHateoas updateCrossReferenceBySystemId(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final CrossReference crossReference);
}
