package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

import java.time.OffsetDateTime;

public interface IChangeLogEntity
        extends ISystemId {

    String getReferenceArchiveUnit();

    void setReferenceArchiveUnit(String referenceArchiveUnit);

    String getReferenceMetadata();

    void setReferenceMetadata(String referenceMetadata);

    OffsetDateTime getChangedDate();

    void setChangedDate(OffsetDateTime changedDate);

    String getChangedBy();

    void setChangedBy(String changedBy);

    String getReferenceChangedBy();

    void setReferenceChangedBy(String referenceChangedBy);

    String getOldValue();

    void setOldValue(String oldValue);

    String getNewValue();

    void setNewValue(String newValue);
}
