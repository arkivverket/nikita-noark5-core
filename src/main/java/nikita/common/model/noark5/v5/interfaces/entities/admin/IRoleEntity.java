package nikita.common.model.noark5.v5.interfaces.entities.admin;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 5/23/17.
 */
public interface IRoleEntity extends INoarkEntity {
    String getRole();

    void setRole(String role);

    String getAccessCategory();

    void setAccessCategory(String accessCategory);

    String getReferenceEntity();

    void setReferenceEntity(String referenceEntity);

    String getAccessRestriction();

    void setAccessRestriction(String accessRestriction);

    Boolean getRead();

    void setRead(Boolean read);

    Boolean getCreate();

    void setCreate(Boolean create);

    Boolean getUpdate();

    void setUpdate(Boolean update);

    Boolean getDelete();

    void setDelete(Boolean delete);

}
