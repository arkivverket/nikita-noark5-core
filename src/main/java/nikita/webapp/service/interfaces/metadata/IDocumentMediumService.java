package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;

import java.util.List;

/**
 * Created by tsodring on 3/9/17.
 */
public interface IDocumentMediumService
    extends IMetadataSuperService {

    DocumentMedium createNewDocumentMedium(DocumentMedium documentMedium);

    List<IMetadataEntity> findAll();

    DocumentMedium update(DocumentMedium documentMedium);
}
