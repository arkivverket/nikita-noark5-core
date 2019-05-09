package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.ScreeningMetadata;

/**
 * Created by tsodring on 16/03/18.
 */

public interface IScreeningMetadataService {

    MetadataHateoas createNewScreeningMetadata(
            ScreeningMetadata screeningMetadata, String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 ScreeningMetadata screeningMetadata,
                                 String outgoingAddress);

    ScreeningMetadata generateDefaultScreeningMetadata();
}
