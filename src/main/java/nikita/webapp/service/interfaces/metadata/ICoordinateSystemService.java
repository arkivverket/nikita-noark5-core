package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.CoordinateSystem;

import javax.validation.constraints.NotNull;

public interface ICoordinateSystemService
    extends IMetadataSuperService {

    MetadataHateoas createNewCoordinateSystem(CoordinateSystem coordinateSystem);

    MetadataHateoas findAll();

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final CoordinateSystem incomingCoordinateSystem);

    CoordinateSystem generateDefaultCoordinateSystem();
}
