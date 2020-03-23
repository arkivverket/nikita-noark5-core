package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.SystemIdEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface IChangeLogEntity
        extends ISystemId {

    UUID getReferenceArchiveUnitSystemId();

    void setReferenceArchiveUnitSystemId(UUID referenceArchiveUnitSystemId);

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

    SystemIdEntity getReferenceArchiveUnit();

    void setReferenceArchiveUnit(SystemIdEntity referenceSystemIdEntity);
}
