package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ScreeningMetadata;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 16/03/18.
 */

public interface IScreeningMetadataService {

    MetadataHateoas createNewScreeningMetadata(
            ScreeningMetadata screeningMetadata);

    MetadataHateoas findAll();

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final ScreeningMetadata incomingScreeningMetadata);

    ScreeningMetadata generateDefaultScreeningMetadata();
}
