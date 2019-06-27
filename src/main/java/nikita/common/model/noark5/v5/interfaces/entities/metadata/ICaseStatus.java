package nikita.common.model.noark5.v5.interfaces.entities.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;

public interface ICaseStatus extends IMetadataEntity {

    Boolean getDefaultCaseStatus();

    void setDefaultCaseStatus(Boolean defaultCaseStatus);
}
