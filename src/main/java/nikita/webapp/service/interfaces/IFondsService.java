package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.Fonds;
import nikita.common.model.noark5.v4.FondsCreator;
import nikita.common.model.noark5.v4.Series;
import nikita.common.model.noark5.v4.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v4.hateoas.FondsHateoas;
import nikita.common.model.noark5.v4.hateoas.SeriesHateoas;

import javax.validation.constraints.NotNull;

public interface IFondsService  {

    // -- All CREATE operations
    FondsHateoas createNewFonds(Fonds fonds);

    SeriesHateoas createSeriesAssociatedWithFonds(String fondsSystemId, Series
            series);

    FondsHateoas createFondsAssociatedWithFonds(String parentFondsSystemId,
                                                Fonds childFonds);

    FondsCreatorHateoas createFondsCreatorAssociatedWithFonds(String fondsSystemId,
                                                              FondsCreator fondsCreator);

    // -- All READ operations
    FondsCreatorHateoas findFondsCreatorAssociatedWithFonds(String systemId);

    // TODO: Finish implementing this. I think StorageLocation is not an own
    // but rather an embedded object
    //StorageLocationHateoas findStorageLocationAssociatedWithFonds(String systemId);
    FondsHateoas findSingleFonds(String fondsSystemId);

    FondsHateoas findFondsByOwnerPaginated(Integer top, Integer skip);

    SeriesHateoas findSeriesAssociatedWithFonds(String fondsSystemId);

    SeriesHateoas generateDefaultSeries(String fondsSystemId);

    FondsHateoas generateDefaultFonds(String fondsSystemId);

    // All UPDATE operations
    FondsHateoas handleUpdate(@NotNull String systemId, @NotNull Long version,
                              @NotNull Fonds incomingFonds);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
