package nikita.common.model.noark5.v4.interfaces.entities;

import java.io.Serializable;

/**
 * Created by tsodring on 1/16/17.
 */
public interface ICrossReferenceEntity
        extends Serializable, INikitaEntity {

    String getFromSystemId();

    void setFromSystemId(String fromSystemId);

    String getToSystemId();

    void setToSystemId(String toSystemId);

    String getReferenceType();

    void setReferenceType(String toSystemId);
}
