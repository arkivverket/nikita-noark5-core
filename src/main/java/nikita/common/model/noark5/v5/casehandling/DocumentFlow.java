package nikita.common.model.noark5.v5.casehandling;

import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_DOCUMENT_FLOW)
public class DocumentFlow
        extends SystemIdEntity {

    /**
     * M660 flytTil (xs:string)
     */
    @Column(name = DOCUMENT_FLOW_FLOW_TO_ENG)
    @Audited
    @JsonProperty(DOCUMENT_FLOW_FLOW_TO)
    private String flowTo;

    /**
     * M665 flytFra  (xs:string)
     */
    @Column(name = DOCUMENT_FLOW_FLOW_FROM_ENG)
    @Audited
    @JsonProperty(DOCUMENT_FLOW_FLOW_FROM)
    private String flowFrom;

    /**
     * M661 - flytMottattDato (xs:dateTime)
     */
    @Column(name = DOCUMENT_FLOW_FLOW_RECEIVED_DATE_ENG)
    @Audited
    @JsonProperty(DOCUMENT_FLOW_FLOW_RECEIVED_DATE)
    private OffsetDateTime flowReceivedDate;

    /**
     * M662 flytSendtDato (xs:dateTime)
     */
    @Column(name = DOCUMENT_FLOW_FLOW_SENT_DATE_ENG)
    @Audited
    @JsonProperty(DOCUMENT_FLOW_FLOW_SENT_DATE)
    private OffsetDateTime flowSentDate;

    /**
     * M663 flytStatus (xs:string)
     */
    // TODO convert to metadata value
    @Column(name = "flow_status")
    @Audited
    private String flowStatus;

    /**
     * M664 flytMerknad (xs:string)
     */
    @Column(name = DOCUMENT_FLOW_FLOW_COMMENT_ENG)
    @Audited
    @JsonProperty(DOCUMENT_FLOW_FLOW_COMMENT)
    private String flowComment;

    // Link to Series
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = WORK_FLOW_REGISTRY_ENTRY_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private RegistryEntry referenceRegistryEntry;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = WORK_FLOW_RECORD_NOTE_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private RecordNote referenceRecordNote;

    public String getFlowTo() {
        return flowTo;
    }

    public void setFlowTo(String flowTo) {
        this.flowTo = flowTo;
    }

    public String getFlowFrom() {
        return flowFrom;
    }

    public void setFlowFrom(String flowFrom) {
        this.flowFrom = flowFrom;
    }

    public OffsetDateTime getFlowReceivedDate() {
        return flowReceivedDate;
    }

    public void setFlowReceivedDate(OffsetDateTime flowReceivedDate) {
        this.flowReceivedDate = flowReceivedDate;
    }

    public OffsetDateTime getFlowSentDate() {
        return flowSentDate;
    }

    public void setFlowSentDate(OffsetDateTime flowSentDate) {
        this.flowSentDate = flowSentDate;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getFlowComment() {
        return flowComment;
    }

    public void setFlowComment(String flowComment) {
        this.flowComment = flowComment;
    }

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_FLOW;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_DOCUMENT_FLOW;
    }

    @Override
    public String getFunctionalTypeName() {
        return Constants.NOARK_CASE_HANDLING_PATH;
    }

    public RegistryEntry getReferenceRegistryEntry() {
        return referenceRegistryEntry;
    }

    public void setReferenceRegistryEntry(RegistryEntry referenceRegistryEntry) {
        this.referenceRegistryEntry = referenceRegistryEntry;
    }

    public RecordNote getReferenceRecordNote() {
        return referenceRecordNote;
    }

    public void setReferenceRecordNote(RecordNote referenceRecordNote) {
        this.referenceRecordNote = referenceRecordNote;
    }

    @Override
    public String toString() {
        return "Flow{" + super.toString() +
                ", flowComment='" + flowComment + '\'' +
                ", flowStatus='" + flowStatus + '\'' +
                ", flowSentDate=" + flowSentDate +
                ", flowReceivedDate=" + flowReceivedDate +
                ", flowFrom='" + flowFrom + '\'' +
                ", flowTo='" + flowTo + '\'' +
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
        DocumentFlow rhs = (DocumentFlow) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(flowComment, rhs.flowComment)
                .append(flowStatus, rhs.flowStatus)
                .append(flowSentDate, rhs.flowSentDate)
                .append(flowReceivedDate, rhs.flowReceivedDate)
                .append(flowFrom, rhs.flowFrom)
                .append(flowTo, rhs.flowTo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(flowComment)
                .append(flowStatus)
                .append(flowSentDate)
                .append(flowReceivedDate)
                .append(flowFrom)
                .append(flowTo)
                .toHashCode();
    }
}
