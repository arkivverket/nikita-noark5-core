package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v4.Fonds;
import nikita.common.model.noark5.v4.FondsCreator;
import nikita.common.model.noark5.v4.hateoas.FondsHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface IFondsCreatorService {

    // -- All CREATE operations
    FondsCreator createNewFondsCreator(FondsCreator fondsCreator);

    Fonds createFondsAssociatedWithFondsCreator(String fondsCreatorSystemId, Fonds fonds);

    // -- All READ operations
    Iterable<FondsCreator> findAll();

    List<FondsCreator> findByOwnedBy(String ownedBy);

    Optional<FondsCreator> findById(Long id);
    FondsCreator findBySystemId(String systemId);

    ResponseEntity<FondsHateoas> findFondsAssociatedWithFondsCreator(
            @NotNull final String systemId);

    // -- All UPDATE operations
    FondsCreator handleUpdate(@NotNull final String systemId,
                              @NotNull final Long version,
                              @NotNull final FondsCreator fondsCreator);

    // -- All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
