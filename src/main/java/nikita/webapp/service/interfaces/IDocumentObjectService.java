package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ConversionHateoas;
import nikita.common.model.noark5.v5.secondary.Conversion;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.UUID;

public interface IDocumentObjectService {

    // -- All CREATE operations
    DocumentObjectHateoas save(@NotNull final DocumentObject documentObject);

    // -- All READ operations

    DocumentObjectHateoas generateDefaultDocumentObject();

    DocumentObjectHateoas findAll();

    ConversionHateoas generateDefaultConversion(@NotNull final UUID systemId);

    ConversionHateoas
    createConversionAssociatedWithDocumentObject(
            @NotNull final UUID systemId,
            @NotNull final Conversion conversion);

    ConversionHateoas
    findAllConversionAssociatedWithDocumentObject(@NotNull final UUID systemId);

    ConversionHateoas
    findConversionAssociatedWithDocumentObject(
            @NotNull final UUID systemId,
            @NotNull final UUID subSystemId);

    ConversionHateoas handleUpdateConversionBySystemId(
            @NotNull final UUID systemId,
            @NotNull final UUID subSystemId,
            @NotNull final Conversion conversion);

    // systemId
    DocumentObjectHateoas findBySystemId(@NotNull final UUID systemId);

    // All UPDATE operations
    DocumentObjectHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final DocumentObject documentObject);

    // All DELETE operations
    void deleteEntity(@NotNull final UUID systemId);

    long deleteAll();

    // -- File handling operations

    /**
     * Given an systemId for a documentObject, find the documentObject and
     * create a new documentObject with an archive version of the the
     * original documentObject
     **/
    DocumentObjectHateoas
    convertDocumentToPDF(@NotNull final UUID systemId);

    DocumentObjectHateoas
    handleIncomingFile(@NotNull final UUID systemId,
                       @NotNull final HttpServletRequest request) throws IOException;

    Resource loadAsResource(@NotNull final UUID systemId,
                            @NotNull final HttpServletRequest request,
                            @NotNull final HttpServletResponse response);

    /**
     * Delete a conversion object associated with the documentObject.
     *
     * @param systemId           UUID of the documentObject object
     * @param conversionSystemID UUID of the Conversion object
     */
    void deleteConversion(@NotNull final UUID systemId,
                          @NotNull final UUID conversionSystemID);
}
