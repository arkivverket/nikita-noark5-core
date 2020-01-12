package nikita.common.model.noark5.v5.interfaces.entities;


import java.io.Serializable;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IFondsCreatorEntity
        extends ISystemId, Serializable {

    String getFondsCreatorId();

    void setFondsCreatorId(String fondsCreatorId);

    String getFondsCreatorName();

    void setFondsCreatorName(String fondsCreatorName);

    String getDescription();

    void setDescription(String description);
}
