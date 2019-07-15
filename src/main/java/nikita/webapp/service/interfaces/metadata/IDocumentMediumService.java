package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.metadata.DocumentMedium;

/**
 * Created by tsodring on 3/9/17.
 */
public interface IDocumentMediumService {

    DocumentMedium createNewDocumentMedium(DocumentMedium documentMedium);

    Iterable<DocumentMedium> findAll();

    DocumentMedium update(DocumentMedium documentMedium);

    DocumentMedium findByCode(String code);
}
