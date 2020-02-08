package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;

/**
 * Created by tsodring on 1/16/17.
 */
public interface ICorrespondencePartEntity
        extends ISystemId {

    CorrespondencePartType getCorrespondencePartType();

    void setCorrespondencePartType(
            CorrespondencePartType correspondencePartType);

}
