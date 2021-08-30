package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.interfaces.IBSM;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.PartRole;

import java.util.Set;

public interface IPartEntity
        extends ISystemId, IBSM {
    PartRole getPartRole();

    void setPartRole(PartRole partRole);

    Set<DocumentDescription> getReferenceDocumentDescription();

    void addDocumentDescription(DocumentDescription documentDescription);

    void removeDocumentDescription(DocumentDescription documentDescription);

    Set<RecordEntity> getReferenceRecordEntity();

    void addRecordEntity(RecordEntity record);

    void removeRecordEntity(RecordEntity record);
}
