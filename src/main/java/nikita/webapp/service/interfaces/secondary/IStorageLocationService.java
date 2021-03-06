package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.secondary.StorageLocation;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IStorageLocationService {

    StorageLocationHateoas createStorageLocationAssociatedWithFonds(
            @NotNull final StorageLocation storageLocation,
            @NotNull final Fonds fonds);

    StorageLocationHateoas createStorageLocationAssociatedWithSeries(
            @NotNull final StorageLocation storageLocation,
            @NotNull final Series series);

    StorageLocationHateoas createStorageLocationAssociatedWithFile(
            @NotNull final StorageLocation storageLocation,
            @NotNull final File file);

    StorageLocationHateoas createStorageLocationAssociatedWithRecord(
            @NotNull final StorageLocation storageLocation,
            @NotNull final RecordEntity record);

    StorageLocationHateoas findBySystemId(@NotNull final UUID systemId);

    StorageLocationHateoas findAll();

    StorageLocationHateoas updateStorageLocationBySystemId(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final StorageLocation incomingStorageLocation);

    void deleteStorageLocationBySystemId(@NotNull final UUID systemId);

    StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemId);
}
