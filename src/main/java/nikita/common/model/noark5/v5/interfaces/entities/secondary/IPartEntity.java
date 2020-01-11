package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.PartRole;

/**
 * Created by tsodring on 11/07/19.
 */
public interface IPartEntity
        extends INoarkEntity {

    String getPartTypeCode();

    void setPartTypeCode(String partTypeCode);

    String getPartTypeCodeName();

    void setPartTypeCodeName(String partTypeCodeName);

    PartRole getPartRole();

    void setPartRole(PartRole partRole);
}
