package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.PartRole;

/**
 * Created by tsodring on 11/07/19.
 */
public interface IPartEntity
        extends ISystemId {

    String getPartRoleCode();

    void setPartRoleCode(String partRoleCode);

    String getPartRoleCodeName();

    void setPartRoleCodeName(String partRoleCodeName);
}
