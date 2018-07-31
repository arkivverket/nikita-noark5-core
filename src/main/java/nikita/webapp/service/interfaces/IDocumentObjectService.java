package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.DocumentObject;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface IDocumentObjectService {

    // -- File handling operations
    void init();

    void storeAndCalculateChecksum(InputStream inputStream,
                                   DocumentObject documentObject)
            throws IOException;


    byte[] convertDocumentToPDF(InputStream inputStream) throws Exception;

    /**
     * Given an systemId for a documentObject, find the documentObject and
     * create a new documentObject with an archive version of the the
     * original documentObject
     *
     **/
    DocumentObject convertDocumentToPDF(String documentObjectSystemId)
            throws Exception;

    Path load(String filename);

    Resource loadAsResource(DocumentObject documentObject);

	// -- All CREATE operations
	DocumentObject save(DocumentObject documentObject);

    // -- ALL UPDATE operations
    DocumentObject update(DocumentObject documentObject);

	// -- All READ operations
    List<DocumentObject> findDocumentObjectByAnyColumn(String column,
                                                       String value);

    List<DocumentObject> findAll();

	// id
    Optional<DocumentObject> findById(Long id);

	// systemId
    DocumentObject findBySystemId(String systemId);

	// ownedBy
	List<DocumentObject> findByOwnedBy(String ownedBy);

	// All UPDATE operations
    DocumentObject handleUpdate(@NotNull String systemId, @NotNull Long version,
                                @NotNull DocumentObject documentObject);

	// All DELETE operations
	void deleteEntity(@NotNull String systemId);
}
