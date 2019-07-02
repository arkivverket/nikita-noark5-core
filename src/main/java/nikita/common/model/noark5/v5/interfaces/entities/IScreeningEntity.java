package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IScreeningEntity extends Serializable {
    String getAccessRestriction();

    void setAccessRestriction(String accessRestriction);

    String getScreeningAuthority();

    void setScreeningAuthority(String screeningAuthority);

    String getScreeningMetadata();

    void setScreeningMetadata(String screeningMetadata);

    String getScreeningDocument();

    void setScreeningDocument(String screeningDocument);

    OffsetDateTime getScreeningExpiresDate();

    void setScreeningExpiresDate(OffsetDateTime screeningExpiresDate);

    String getScreeningDuration();

    void setScreeningDuration(String screeningDuration);
}
