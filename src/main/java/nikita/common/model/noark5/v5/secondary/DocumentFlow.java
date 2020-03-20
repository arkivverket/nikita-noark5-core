package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IDocumentFlowEntity;
import nikita.common.model.noark5.v5.metadata.FlowStatus;
import nikita.common.util.deserialisers.secondary.DocumentFlowDeserializer;
import nikita.webapp.hateoas.secondary.DocumentFlowHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_DOCUMENT_FLOW)
@JsonDeserialize(using = DocumentFlowDeserializer.class)
@HateoasPacker(using = DocumentFlowHateoasHandler.class)
@HateoasObject(using = DocumentFlowHateoas.class)
public class DocumentFlow
        extends SystemIdEntity
        implements IDocumentFlowEntity  {

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
     * M??? flytStatus code (xs:string)
     */
    @Column(name = "flow_status_code")
    @Audited
    private String flowStatusCode;

    /**
     * M663 flytStatus code name (xs:string)
     */
    @Column(name = "flow_status_code_name")
    @Audited
    private String flowStatusCodeName;

    /**
     * M664 flytMerknad (xs:string)
     */
    @Column(name = DOCUMENT_FLOW_FLOW_COMMENT_ENG)
    @Audited
    @JsonProperty(DOCUMENT_FLOW_FLOW_COMMENT)
    private String flowComment;

    /**
     * M?? referanseflytTil (xs:string)
     */
    @Column(name = DOCUMENT_FLOW_REFERENCE_FLOW_TO_ENG)
    @Audited
    @JsonProperty(DOCUMENT_FLOW_REFERENCE_FLOW_TO)
    private UUID referenceFlowToSystemID;

    /**
     * M?? referanseflytFra (xs:string)
     */
    @Column(name = DOCUMENT_FLOW_REFERENCE_FLOW_FROM_ENG)
    @Audited
    @JsonProperty(DOCUMENT_FLOW_REFERENCE_FLOW_FROM)
    private UUID referenceFlowFromSystemID;

    // Link to user to (if referenceFlowToSystemID refer to existing user)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = DOCUMENT_FLOW_FLOW_TO_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private User referenceFlowTo;

    // Link to user from (if referenceFlowFromSystemID refer to existing user)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = DOCUMENT_FLOW_FLOW_FROM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private User referenceFlowFrom;

    // Link to RegistryEntry
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = DOCUMENT_FLOW_REGISTRY_ENTRY_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private RegistryEntry referenceRegistryEntry;

    // Link to RecordNode
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = DOCUMENT_FLOW_RECORD_NOTE_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private RecordNote referenceRecordNote;

    @Override
    public String getFlowTo() {
        return flowTo;
    }

    @Override
    public void setFlowTo(String flowTo) {
        this.flowTo = flowTo;
    }

    @Override
    public String getFlowFrom() {
        return flowFrom;
    }

    @Override
    public void setFlowFrom(String flowFrom) {
        this.flowFrom = flowFrom;
    }

    @Override
    public OffsetDateTime getFlowReceivedDate() {
        return flowReceivedDate;
    }

    @Override
    public void setFlowReceivedDate(OffsetDateTime flowReceivedDate) {
        this.flowReceivedDate = flowReceivedDate;
    }

    @Override
    public OffsetDateTime getFlowSentDate() {
        return flowSentDate;
    }

    @Override
    public void setFlowSentDate(OffsetDateTime flowSentDate) {
        this.flowSentDate = flowSentDate;
    }

    @Override
    public FlowStatus getFlowStatus() {
        if (null == flowStatusCode)
            return null;
        return new FlowStatus(flowStatusCode, flowStatusCodeName);
    }

    @Override
    public void setFlowStatus(FlowStatus flowStatus) {
        if (null != flowStatus) {
            this.flowStatusCode = flowStatus.getCode();
            this.flowStatusCodeName = flowStatus.getCodeName();
        } else {
            this.flowStatusCode = null;
            this.flowStatusCodeName = null;
        }
    }

    @Override
    public String getFlowComment() {
        return flowComment;
    }

    @Override
    public void setFlowComment(String flowComment) {
        this.flowComment = flowComment;
    }

    @Override
    public UUID getReferenceFlowToSystemID() {
	return referenceFlowToSystemID;
    }

    @Override
    public void setReferenceFlowToSystemID(UUID referenceFlowToSystemID) {
	this.referenceFlowToSystemID = referenceFlowToSystemID;
    }

    @Override
    public UUID getReferenceFlowFromSystemID() {
	return referenceFlowFromSystemID;
    }

    @Override
    public void setReferenceFlowFromSystemID(UUID referenceFlowFromSystemID) {
	this.referenceFlowFromSystemID = referenceFlowFromSystemID;
    }

    @Override
    public User getReferenceFlowTo() {
	return referenceFlowTo;
    }

    @Override
    public void setReferenceFlowTo(User referenceFlowTo) {
	this.referenceFlowTo = referenceFlowTo;
    }

    @Override
    public User getReferenceFlowFrom() {
	return referenceFlowFrom;
    }

    @Override
    public void setReferenceFlowFrom(User referenceFlowFrom) {
	this.referenceFlowFrom = referenceFlowFrom;
    }

    @Override
    public RegistryEntry getReferenceRegistryEntry() {
        return referenceRegistryEntry;
    }

    @Override
    public void setReferenceRegistryEntry(RegistryEntry referenceRegistryEntry) {
        this.referenceRegistryEntry = referenceRegistryEntry;
    }

    @Override
    public RecordNote getReferenceRecordNote() {
        return referenceRecordNote;
    }

    @Override
    public void setReferenceRecordNote(RecordNote referenceRecordNote) {
        this.referenceRecordNote = referenceRecordNote;
    }

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_FLOW;
    }

    @Override
    public String getBaseRel() {
        return REL_CASE_HANDLING_DOCUMENT_FLOW;
    }

    @Override
    public String getFunctionalTypeName() {
        return Constants.NOARK_CASE_HANDLING_PATH;
    }

    @Override
    public String toString() {
        return "Flow{" + super.toString() +
                ", flowComment='" + flowComment + '\'' +
                ", flowStatusCode='" + flowStatusCode + '\'' +
                ", flowStatusCodeName='" + flowStatusCodeName + '\'' +
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
                .append(flowStatusCode, rhs.flowStatusCode)
                .append(flowStatusCodeName, rhs.flowStatusCodeName)
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
                .append(flowStatusCode)
                .append(flowStatusCodeName)
                .append(flowSentDate)
                .append(flowReceivedDate)
                .append(flowFrom)
                .append(flowTo)
                .toHashCode();
    }
}
