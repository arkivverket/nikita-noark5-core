package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;

/**
 * Created by tsodring on 1/16/17.
 */
public interface ICorrespondencePartEntity
        extends INoarkEntity {

    String getCorrespondencePartTypeCode();

    void setCorrespondencePartTypeCode(String correspondencePartTypeCode);

    String getCorrespondencePartTypeCodeName();

    void setCorrespondencePartTypeCodeName(
            String correspondencePartTypeCodeName);

    CorrespondencePartType getCorrespondencePartType();

    void setCorrespondencePartType(
            CorrespondencePartType correspondencePartType);

}
