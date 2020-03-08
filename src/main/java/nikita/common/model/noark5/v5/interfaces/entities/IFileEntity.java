package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.interfaces.*;

/**
 * Created by tsodring
 */
public interface IFileEntity
        extends INoarkGeneralEntity, IDocumentMedium, IStorageLocation,
        IKeyword, IClassified, IDisposal, IScreening, IComment,
        ICrossReference {

    String getFileId();

    void setFileId(String fileId);

    String getPublicTitle();

    void setPublicTitle(String publicTitle);
}
