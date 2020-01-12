package nikita.common.model.noark5.v5.meeting;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.MEETING_PARTICIPANT;

@Entity
@Table(name = TABLE_MEETING_PARTICIPANT)
public class MeetingParticipant
        extends SystemIdEntity {

    /**
     * M372 - moetedeltakerNavn (xs:string)
     */
    @Column(name = "meeting_participant_name")
    @Audited
    private String meetingParticipantName;

    /**
     * M373 - moetedeltakerFunksjon (xs:string)
     */
    @Column(name = "meeting_participant_function")
    @Audited
    private String meetingParticipantFunction;

    // Link to MeetingFile
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = MEETING_PARTICIPANT_FILE_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private MeetingFile referenceMeetingFile;


    public String getMeetingParticipantName() {
        return meetingParticipantName;
    }

    public void setMeetingParticipantName(String meetingParticipantName) {
        this.meetingParticipantName = meetingParticipantName;
    }

    public String getMeetingParticipantFunction() {
        return meetingParticipantFunction;
    }

    public void setMeetingParticipantFunction(
            String meetingParticipantFunction) {
        this.meetingParticipantFunction = meetingParticipantFunction;
    }

    @Override
    public String getBaseTypeName() {
        return MEETING_PARTICIPANT;
    }

    public MeetingFile getReferenceMeetingFile() {
        return referenceMeetingFile;
    }

    public void setReferenceMeetingFile(MeetingFile referenceMeetingFile) {
        this.referenceMeetingFile = referenceMeetingFile;
    }

    @Override
    public String toString() {
        return "MeetingParticipant{" + super.toString() +
                "meetingParticipantFunction='" +
                meetingParticipantFunction + '\'' +
                ", meetingParticipantName='" + meetingParticipantName + '\'' +
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
        MeetingParticipant rhs = (MeetingParticipant) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(meetingParticipantFunction,
                        rhs.meetingParticipantFunction)
                .append(meetingParticipantName, rhs.meetingParticipantName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(meetingParticipantFunction)
                .append(meetingParticipantName)
                .toHashCode();
    }
}
