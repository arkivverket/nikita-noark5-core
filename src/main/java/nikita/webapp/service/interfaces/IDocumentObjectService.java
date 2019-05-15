package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.DocumentObject;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface IDocumentObjectService {

    // -- File handling operations

    void storeAndCalculateChecksum(InputStream inputStream,
                                   DocumentObject documentObject)
            throws IOException;


    /**
     * Given an systemId for a documentObject, find the documentObject and
     * create a new documentObject with an archive version of the the
     * original documentObject
     *
     **/
    DocumentObject convertDocumentToPDF(String documentObjectSystemId)
            throws Exception;

    List<DocumentObject> findDocumentObjectByOwner();

    Resource loadAsResource(DocumentObject documentObject);

	// -- All CREATE operations
	DocumentObject save(DocumentObject documentObject);

    // -- ALL UPDATE operations
    DocumentObject update(DocumentObject documentObject);

	// -- All READ operations
    List<DocumentObject> findDocumentObjectByAnyColumn(String column,
                                                       String value);

	// id
    Optional<DocumentObject> findById(Long id);

	// systemId
    DocumentObject findBySystemId(String systemId);

	// All UPDATE operations
    DocumentObject handleUpdate(@NotNull final String systemId,
                                @NotNull final Long version,
                                @NotNull final DocumentObject documentObject);

	// All DELETE operations
	void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
