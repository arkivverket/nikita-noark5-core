package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.metadata.EventType;
import nikita.common.model.noark5.v5.SystemIdEntity;

import java.time.OffsetDateTime;

public interface IEventLogEntity
        extends IChangeLogEntity {

    EventType getEventType();

    void setEventType(EventType eventType);

    String getDescription();

    void setDescription(String description);

    OffsetDateTime getEventDate();

    void setEventDate(OffsetDateTime eventDate);

    SystemIdEntity getReferenceSystemIdEntity();

    void setReferenceSystemIdEntity(SystemIdEntity referenceSystemIdEntity);
}
