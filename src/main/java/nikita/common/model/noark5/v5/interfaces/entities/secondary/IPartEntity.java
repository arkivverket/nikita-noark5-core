package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.PartRole;

/**
 * Created by tsodring on 11/07/19.
 */
public interface IPartEntity
        extends INikitaEntity {

    PartRole getPartRole();

    void setPartRole(PartRole partRole);
}
