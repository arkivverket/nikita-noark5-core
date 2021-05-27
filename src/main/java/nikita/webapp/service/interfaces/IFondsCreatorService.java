package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IFondsCreatorService {

    FondsCreatorHateoas createNewFondsCreator(
            @NotNull final FondsCreator fondsCreator);

    FondsHateoas createFondsAssociatedWithFondsCreator(
            @NotNull final UUID systemId,
            @NotNull final Fonds fonds);

    // -- All READ operations

    FondsCreatorHateoas findAll();

    FondsCreatorHateoas findBySystemId(
            @NotNull final UUID systemId);

    FondsHateoas findFondsAssociatedWithFondsCreator(
            @NotNull final UUID systemId);

    // -- All UPDATE operations
    FondsCreatorHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final FondsCreator fondsCreator);

    // -- All DELETE operations
    void deleteEntity(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    FondsCreatorHateoas generateDefaultFondsCreator();
}
