package nikita.common.model.noark5.v5.interfaces.entities;


import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDisposalEntity extends Serializable {
    String getDisposalDecision();

    void setDisposalDecision(String disposalDecision);

    String getDisposalAuthority();

    void setDisposalAuthority(String disposalAuthority);

    Integer getPreservationTime();

    void setPreservationTime(Integer preservationTime);

    OffsetDateTime getDisposalDate();

    void setDisposalDate(OffsetDateTime disposalDate);
}
