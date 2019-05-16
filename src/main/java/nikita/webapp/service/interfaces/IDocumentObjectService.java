package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v4.DocumentObject;
import nikita.common.model.noark5.v4.hateoas.DocumentObjectHateoas;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    ResponseEntity<DocumentObjectHateoas>
    convertDocumentToPDF(String documentObjectSystemId)
            throws Exception;

    List<DocumentObject> findDocumentObjectByOwner();

    Resource loadAsResource(@NotNull String systemId,
                            @NotNull HttpServletRequest request,
                            @NotNull HttpServletResponse response);
	// -- All CREATE operations
	DocumentObject save(DocumentObject documentObject);

    // -- ALL UPDATE operations
    DocumentObject update(DocumentObject documentObject);

	// -- All READ operations
    List<DocumentObject> findDocumentObjectByAnyColumn(String column,
                                                       String value);

    ResponseEntity<DocumentObjectHateoas>
    handleIncomingFile(@NotNull String systemId,
                       @NotNull HttpServletRequest request) throws IOException;

	// systemId
    ResponseEntity<DocumentObjectHateoas> findBySystemId(
            @NotNull String systemId);

	// All UPDATE operations
    DocumentObject handleUpdate(@NotNull final String systemId,
                                @NotNull final Long version,
                                @NotNull final DocumentObject documentObject);

	// All DELETE operations
    int deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
