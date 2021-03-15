package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.interfaces.IBSM;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.PartRole;

import java.util.List;

public interface IPartEntity
        extends ISystemId, IBSM {
    PartRole getPartRole();

    void setPartRole(PartRole partRole);

    List<Record> getReferenceRecord();

    void setReferenceRecord(List<Record> referenceRecord);

    void addRecord(Record record);

    void removeRecord(Record record);
}
