package nikita.common.model.noark5.v4.interfaces.entities;

import java.time.ZonedDateTime;

/**
 * Created by tsodring on 1/16/17.
 */
public interface IPrecedenceEntity extends INoarkCreateEntity, INoarkTitleDescriptionEntity, INoarkFinaliseEntity {
    ZonedDateTime getPrecedenceDate();

    void setPrecedenceDate(ZonedDateTime precedenceDate);

    String getPrecedenceAuthority();

    void setPrecedenceAuthority(String precedenceAuthority);

    String getSourceOfLaw();

    void setSourceOfLaw(String sourceOfLaw);

    ZonedDateTime getPrecedenceApprovedDate();

    void setPrecedenceApprovedDate(ZonedDateTime precedenceApprovedDate);

    String getPrecedenceApprovedBy();

    void setPrecedenceApprovedBy(String precedenceApprovedBy);

    String getPrecedenceStatus();

    void setPrecedenceStatus(String precedenceStatus);
}
