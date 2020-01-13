package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Format;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 15/02/18.
 */

public interface IFormatService
    extends IMetadataSuperService {

    MetadataHateoas createNewFormat(Format format);

    MetadataHateoas findAll();

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final Format incomingFormat);

    Format generateDefaultFormat();
}
