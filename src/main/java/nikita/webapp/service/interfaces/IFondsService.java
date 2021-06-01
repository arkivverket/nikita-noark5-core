package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.secondary.StorageLocation;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IFondsService {

    // -- All CREATE operations
    FondsHateoas createNewFonds(@NotNull final Fonds fonds);

    SeriesHateoas createSeriesAssociatedWithFonds(
            @NotNull final UUID systemId,
            @NotNull final Series series);

    FondsHateoas createFondsAssociatedWithFonds(
            @NotNull final UUID systemId,
            @NotNull final Fonds childFonds);

    FondsCreatorHateoas createFondsCreatorAssociatedWithFonds(
            @NotNull final UUID systemId,
            @NotNull final FondsCreator fondsCreator);

    StorageLocationHateoas createStorageLocationAssociatedWithFonds(
            @NotNull final UUID systemId,
            @NotNull final StorageLocation storageLocation);

    // -- All READ operations

    FondsHateoas findSingleFonds(@NotNull final UUID systemId);

    FondsHateoas findAllFonds();

    FondsHateoas findAllChildren(@NotNull final UUID systemId);

    SeriesHateoas findSeriesAssociatedWithFonds(
            @NotNull final UUID systemId);

    FondsCreatorHateoas findFondsCreatorAssociatedWithFonds(
            @NotNull final UUID systemId);

    StorageLocationHateoas findStorageLocationAssociatedWithFonds(
            @NotNull final UUID systemID);

    SeriesHateoas generateDefaultSeries(@NotNull final UUID systemId);

    FondsHateoas generateDefaultFonds(@NotNull final UUID systemId);

    // All UPDATE operations
    FondsHateoas handleUpdate(@NotNull final UUID systemId,
                              @NotNull final Long version,
                              @NotNull final Fonds incomingFonds);

    // All DELETE operations
    void deleteEntity(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemId);
}
