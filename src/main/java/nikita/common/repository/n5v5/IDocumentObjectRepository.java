package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDocumentObjectRepository extends
        PagingAndSortingRepository<DocumentObject, UUID> {

    DocumentObject findBySystemId(UUID systemId);

    long deleteByOwnedBy(String ownedBy);

    Long countByReferenceDocumentDescriptionAndVariantFormatCode(
            DocumentDescription documentDescription, String variantFormatCode);
}
