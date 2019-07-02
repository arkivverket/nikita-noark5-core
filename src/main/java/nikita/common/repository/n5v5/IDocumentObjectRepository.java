package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IDocumentObjectRepository extends
        PagingAndSortingRepository<DocumentObject, Long> {

    // -- All SAVE operations
    @Override
    DocumentObject save(DocumentObject documentObject);

    // -- All READ operations
    @Override
    List<DocumentObject> findAll();

    // id
    Optional<DocumentObject> findById(Long id);

    // systemId
    DocumentObject findBySystemId(UUID systemId);

    // ownedBy
    List<DocumentObject> findByOwnedBy(String ownedBy);

    long deleteByOwnedBy(String ownedBy);

    Long countByReferenceDocumentDescriptionAndVariantFormat(
            DocumentDescription documentDescription, String variantFormat);
}
