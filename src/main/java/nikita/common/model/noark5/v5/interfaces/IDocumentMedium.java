package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.metadata.DocumentMedium;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDocumentMedium {
    // Same as in INoarkEntity, to make it available for validateDocumentMedium
    String getBaseTypeName();

    DocumentMedium getDocumentMedium();

    void setDocumentMedium(DocumentMedium documentMediumCode);
}
