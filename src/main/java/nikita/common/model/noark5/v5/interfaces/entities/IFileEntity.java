package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.interfaces.*;
import nikita.common.model.noark5.v5.secondary.Keyword;

public interface IFileEntity
        extends INoarkGeneralEntity, IDocumentMedium, IStorageLocation,
        IKeyword, IClassified, IDisposal, IScreening, IComment,
        ICrossReference, IPart, IBSM {
    String getFileId();

    void setFileId(String fileId);

    String getPublicTitle();

    void setPublicTitle(String publicTitle);

    void removeKeyword(Keyword keyword);
}
