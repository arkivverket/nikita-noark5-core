package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;

/**
 *
 */
public interface IMetadataEntity extends INikitaEntity, Serializable {

    String getCode();

    void setCode(String code);

    String getDescription();

    void setDescription(String description);

}
