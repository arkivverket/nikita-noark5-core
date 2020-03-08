package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.metadata.AccessRestriction;
import nikita.common.model.noark5.v5.metadata.ScreeningDocument;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IScreeningEntity extends Serializable {
    AccessRestriction getAccessRestriction();

    void setAccessRestriction(AccessRestriction accessRestriction);

    String getScreeningAuthority();

    void setScreeningAuthority(String screeningAuthority);

    String getScreeningMetadata();

    void setScreeningMetadata(String screeningMetadata);

    ScreeningDocument getScreeningDocument();

    void setScreeningDocument(ScreeningDocument screeningDocument);

    OffsetDateTime getScreeningExpiresDate();

    void setScreeningExpiresDate(OffsetDateTime screeningExpiresDate);

    Integer getScreeningDuration();

    void setScreeningDuration(Integer screeningDuration);
}
