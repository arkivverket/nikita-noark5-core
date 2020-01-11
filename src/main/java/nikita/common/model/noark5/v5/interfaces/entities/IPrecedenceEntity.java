package nikita.common.model.noark5.v5.interfaces.entities;

import java.time.OffsetDateTime;

/**
 * Created by tsodring on 1/16/17.
 */
public interface IPrecedenceEntity extends ICreate, INoarkTitleDescriptionEntity, IFinalise {
    OffsetDateTime getPrecedenceDate();

    void setPrecedenceDate(OffsetDateTime precedenceDate);

    String getPrecedenceAuthority();

    void setPrecedenceAuthority(String precedenceAuthority);

    String getSourceOfLaw();

    void setSourceOfLaw(String sourceOfLaw);

    OffsetDateTime getPrecedenceApprovedDate();

    void setPrecedenceApprovedDate(OffsetDateTime precedenceApprovedDate);

    String getPrecedenceApprovedBy();

    void setPrecedenceApprovedBy(String precedenceApprovedBy);

    String getPrecedenceStatus();

    void setPrecedenceStatus(String precedenceStatus);
}
