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
    FondsHateoas createNewFonds(Fonds fonds, String outgoingAddress);

    SeriesHateoas createSeriesAssociatedWithFonds(String fondsSystemId, Series
            series, String outgoingAddress);

    FondsHateoas createFondsAssociatedWithFonds(String parentFondsSystemId,
                                                Fonds childFonds, String outgoingAddress);

    FondsCreatorHateoas createFondsCreatorAssociatedWithFonds(String fondsSystemId,
                                                              FondsCreator fondsCreator, String outgoingAddress);

    // -- All READ operations
    FondsCreatorHateoas findFondsCreatorAssociatedWithFonds(String systemId, String outgoingAddress);

    // TODO: Finish implementing this. I think StorageLocation is not an own
    // but rather an embedded object
    //StorageLocationHateoas findStorageLocationAssociatedWithFonds(String systemId);
    FondsHateoas findSingleFonds(String fondsSystemId, String outgoingAddress);

    FondsHateoas findFondsByOwnerPaginated(Integer top, Integer skip, String outgoingAddress);

    SeriesHateoas findSeriesAssociatedWithFonds(String fondsSystemId, String outgoingAddress);

    SeriesHateoas generateDefaultSeries(String fondsSystemId, String outgoingAddress);

    FondsHateoas generateDefaultFonds(String fondsSystemId, String outgoingAddress);

    // All UPDATE operations
    FondsHateoas handleUpdate(@NotNull String systemId, @NotNull Long version,
                              @NotNull Fonds incomingFonds, String outgoingAddress);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);
}
