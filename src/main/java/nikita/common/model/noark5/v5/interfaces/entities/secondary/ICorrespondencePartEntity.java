package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;

import java.io.Serializable;

/**
 * Created by tsodring on 1/16/17.
 */
public interface ICorrespondencePartEntity
        extends INikitaEntity, Serializable {

    CorrespondencePartType getCorrespondencePartType();

    void setCorrespondencePartType(CorrespondencePartType correspondencePartType);
}
