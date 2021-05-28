package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.secondary.Screening;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IScreeningMetadataService {

    ScreeningMetadataHateoas createScreeningMetadata(
            @NotNull final Screening screening,
            @NotNull final Metadata screeningMetadata);

    ScreeningMetadataHateoas findAll();

    ScreeningMetadataHateoas findBySystemId(@NotNull final UUID systemId);

    ScreeningMetadataHateoas updateScreeningMetadataBySystemId(
            @NotNull final UUID systemId, @NotNull final Long parseETAG,
            @NotNull final Metadata screeningMetadata);

    void deleteScreeningMetadataBySystemId(@NotNull final UUID systemId);

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId);
}
