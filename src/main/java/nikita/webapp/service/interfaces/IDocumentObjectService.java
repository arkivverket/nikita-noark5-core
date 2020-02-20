package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ConversionHateoas;
import nikita.common.model.noark5.v5.secondary.Conversion;
import org.springframework.core.io.Resource;

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

    DocumentObjectHateoas generateDefaultDocumentObject();

    DocumentObjectHateoas findDocumentObjectByOwner();

    ConversionHateoas generateDefaultConversion(String systemID);

    ConversionHateoas
    createConversionAssociatedWithDocumentObject(String systemID,
                                                 Conversion conversion);

    ConversionHateoas
    findAllConversionAssociatedWithDocumentObject(String systemId);

    ConversionHateoas
    findConversionAssociatedWithDocumentObject
        (String systemId, String subSystemId);

    ConversionHateoas handleUpdateConversionBySystemId
        (String systemId, String subSystemId, Conversion conversion);

    // systemId
    DocumentObjectHateoas findBySystemId(@NotNull String systemId);

    // All UPDATE operations
    DocumentObjectHateoas handleUpdate
        (@NotNull final String systemId, @NotNull final Long version,
         @NotNull final DocumentObject documentObject);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

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
    DocumentObjectHateoas
    convertDocumentToPDF(String documentObjectSystemId)
            throws Exception;


    DocumentObjectHateoas
    handleIncomingFile(@NotNull String systemId,
                       @NotNull HttpServletRequest request) throws IOException;

    Resource loadAsResource(@NotNull String systemId,
                            @NotNull HttpServletRequest request,
                            @NotNull HttpServletResponse response);

}
