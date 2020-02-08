package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.PartRole;

import java.util.List;

/**
 * Created by tsodring on 11/07/19.
 */
public interface IPartEntity
        extends ISystemId {

    PartRole getPartRole();

    void setPartRole(PartRole partRole);

    List<Record> getReferenceRecord();

    void setReferenceRecord(List<Record> referenceRecord);

    void addReferenceRecord(Record record);
}
