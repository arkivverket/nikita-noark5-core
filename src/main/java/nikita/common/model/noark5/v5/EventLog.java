package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.EventLogHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IEventLogEntity;
import nikita.common.model.noark5.v5.metadata.EventType;
import nikita.common.util.deserialisers.EventLogDeserializer;
import nikita.webapp.hateoas.EventLogHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_EVENT_LOG)
@JsonDeserialize(using = EventLogDeserializer.class)
@HateoasPacker(using = EventLogHateoasHandler.class)
@HateoasObject(using = EventLogHateoas.class)
public class EventLog
    extends ChangeLog
    implements IEventLogEntity
{

    private static final long serialVersionUID = 1L;

    /**
     * M??? - hendelsetype code (xs:string)
     */
    @Column(name = "event_log_event_type_code")
    @Audited
    private String eventTypeCode;

    /**
     * M??? - hendelsetype code name (xs:string)
     */
    @Column(name = "event_log_event_type_code_name")
    @Audited
    private String eventTypeCodeName;

    /**
     * M021 - beskrivelse (xs:string)
     */
    @Column(name = DESCRIPTION_ENG, length = DESCRIPTION_LENGTH)
    @Audited
    @JsonProperty(DESCRIPTION)
    private String description;

    /**
     * M??? - hendelseDato (xs:datetime)
     */
    @Column(name = EVENT_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(EVENT_DATE)
    private OffsetDateTime eventDate;

    @Override
    public EventType getEventType() {
        if (null == eventTypeCode)
            return null;
        return new EventType(eventTypeCode, eventTypeCodeName);
    }

    @Override
    public void setEventType(EventType eventType) {
        if (null != eventType) {
            this.eventTypeCode = eventType.getCode();
            this.eventTypeCodeName = eventType.getCodeName();
        } else {
            this.eventTypeCode = null;
            this.eventTypeCodeName = null;
        }
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public OffsetDateTime getEventDate() {
        return eventDate;
    }

    @Override
    public void setEventDate(OffsetDateTime eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public String getBaseTypeName() {
        return EVENT_LOG;
    }

    @Override
    public String getBaseRel() {
        return REL_LOGGING_EVENT_LOG;
    }

    @Override
    public String toString() {
        return "EventLog{" + super.toString() +
                ", eventTypeCode='" + eventTypeCode + '\'' +
                ", eventTypeCodeName='" + eventTypeCodeName + '\'' +
                ", description='" + description + '\'' +
                ", eventDate='" + eventDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        EventLog rhs = (EventLog) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(eventTypeCode, rhs.eventTypeCode)
                .append(eventTypeCodeName, rhs.eventTypeCodeName)
                .append(description, rhs.description)
                .append(eventDate, rhs.eventDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(eventTypeCode)
                .append(eventTypeCodeName)
                .append(description)
                .append(eventDate)
                .toHashCode();
    }
}
