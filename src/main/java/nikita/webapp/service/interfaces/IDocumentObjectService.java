package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IDocumentObjectService {

    // -- All CREATE operations
    DocumentObject save(@NotNull DocumentObject documentObject);

    // -- All READ operations
    List<DocumentObject> findDocumentObjectByOwner();

    ResponseEntity<DocumentDescriptionHateoas>
    findAssociatedDocumentDescription(@NotNull String systemId);

    // systemId
    ResponseEntity<DocumentObjectHateoas> findBySystemId(
            @NotNull String systemId);

    // All UPDATE operations
    DocumentObject handleUpdate(@NotNull final String systemId,
                                @NotNull final Long version,
                                @NotNull final DocumentObject documentObject);

    // All DELETE operations
    int deleteEntity(@NotNull String systemId);

    long deleteAll();

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


    ResponseEntity<DocumentObjectHateoas>
    handleIncomingFile(@NotNull String systemId,
                       @NotNull HttpServletRequest request) throws IOException;

    Resource loadAsResource(@NotNull String systemId,
                            @NotNull HttpServletRequest request,
                            @NotNull HttpServletResponse response);

}
