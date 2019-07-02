package nikita.common.model.noark5.v5.meeting;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.File;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.TABLE_MEETING_FILE;
import static nikita.common.config.N5ResourceMappings.MEETING_FILE;

@Entity
@Table(name = TABLE_MEETING_FILE)
@Inheritance(strategy = JOINED)
public class MeetingFile
        extends File {

    /**
     * M008 - moetenummer (xs:string)
     */
    @Column(name = "meeting_number")
    @Audited
    private String meetingNumber;

    /**
     * M370 - utvalg (xs:string)
     */
    @Column(name = "committee")
    @Audited
    private String committee;

    /**
     * M102 - moetedato (xs:date)
     */
    @Column(name = "loaned_date")
    @Audited
    private OffsetDateTime meetingDate;

    /**
     * M371 - moetested (xs:string)
     */
    @Column(name = "meeting_place")
    @Audited
    private String meetingPlace;

    /**
     * M221 - referanseForrigeMoete (xs:string)
     **/
    // Link to next Meeting
    @OneToOne(fetch = FetchType.LAZY)
    private MeetingFile referenceNextMeeting;

    /**
     * M222 - referanseNesteMoete (xs:string)
     **/

    // Link to previous Meeting
    // TODO: This links to id, not systemId. Fix!
    @OneToOne(fetch = FetchType.LAZY)
    private MeetingFile referencePreviousMeeting;

    // Links to MeetingParticipant
    @OneToMany(mappedBy = "referenceMeetingFile")
    private List<MeetingParticipant> referenceMeetingParticipant =
            new ArrayList<>();

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public String getCommittee() {
        return committee;
    }

    public void setCommittee(String committee) {
        this.committee = committee;
    }

    public OffsetDateTime getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(OffsetDateTime meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    @Override
    public String getBaseTypeName() {
        return MEETING_FILE;
    }

    public MeetingFile getReferenceNextMeeting() {
        return referenceNextMeeting;
    }

    public void setReferenceNextMeeting(MeetingFile referenceNextMeeting) {
        this.referenceNextMeeting = referenceNextMeeting;
    }

    public MeetingFile getReferencePreviousMeeting() {
        return referencePreviousMeeting;
    }

    public void setReferencePreviousMeeting(
            MeetingFile referencePreviousMeeting) {
        this.referencePreviousMeeting = referencePreviousMeeting;
    }

    public List<MeetingParticipant> getReferenceMeetingParticipant() {
        return referenceMeetingParticipant;
    }

    public void setReferenceMeetingParticipant(
            List<MeetingParticipant> referenceMeetingParticipant) {
        this.referenceMeetingParticipant = referenceMeetingParticipant;
    }

    @Override
    public String toString() {
        return "MeetingFile{" + super.toString() +
                "meetingNumber='" + meetingNumber + '\'' +
                ", committee='" + committee + '\'' +
                ", meetingDate=" + meetingDate +
                ", meetingPlace='" + meetingPlace + '\'' +
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
        MeetingFile rhs = (MeetingFile) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(committee, rhs.committee)
                .append(meetingNumber, rhs.meetingNumber)
                .append(meetingDate, rhs.meetingDate)
                .append(meetingPlace, rhs.meetingPlace)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(meetingPlace)
                .append(meetingNumber)
                .append(meetingDate)
                .append(committee)
                .toHashCode();
    }
}
