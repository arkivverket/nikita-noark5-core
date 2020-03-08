package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.interfaces.*;

import java.time.OffsetDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IRecordEntity
        extends ICreate, INoarkEntity, ISystemId, IClassified, IScreening,
        IDisposal, IDocumentMedium, ITitleDescription,
        IStorageLocation, IKeyword, IComment, ICrossReference, IAuthor {

    OffsetDateTime getArchivedDate();

    void setArchivedDate(OffsetDateTime archivedDate);

    String getArchivedBy();

    void setArchivedBy(String archivedBy);

    String getRecordId();

    void setRecordId(String recordId);

    String getTitle();

    void setTitle(String title);

    String getPublicTitle();

    void setPublicTitle(String publicTitle);

    String getDescription();

    void setDescription(String description);
}
