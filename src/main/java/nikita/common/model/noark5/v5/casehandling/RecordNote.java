package nikita.common.model.noark5.v5.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IRecordNoteEntity;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.util.deserialisers.casehandling.RecordNoteDeserializer;
import nikita.webapp.hateoas.casehandling.RecordNoteHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.RECORD_NOTE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_RECORD_NOTE)
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = RecordNoteDeserializer.class)
@HateoasPacker(using = RecordNoteHateoasHandler.class)
@HateoasObject(using = RecordNoteHateoas.class)
public class RecordNote
        extends Record
        implements IRecordNoteEntity {

    /**
     * M103 - dokumentetsDato (xs:date)
     */
    @Column(name = "document_date")
    @DateTimeFormat(iso = DATE)
    @Audited
    private OffsetDateTime documentDate;

    /**
     * M104 - mottattDato (xs:dateTime)
     */
    @Column(name = "received_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private OffsetDateTime receivedDate;

    /**
     * M105 - sendtDato (xs:dateTime)
     */
    @Column(name = "sent_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private OffsetDateTime sentDate;

    /**
     * M109 - forfallsdato (xs:date)
     */
    @Column(name = "due_date")
    @DateTimeFormat(iso = DATE)
    @Audited
    private OffsetDateTime dueDate;

    /**
     * M110 - offentlighetsvurdertDato (xs:date)
     */
    @Column(name = "freedom_assessment_date")
    @DateTimeFormat(iso = DATE)
    @Audited
    private OffsetDateTime freedomAssessmentDate;

    /**
     * M304 - antallVedlegg (xs:integer)
     */
    @Column(name = "number_of_attachments")
    @Audited
    private Integer numberOfAttachments;

    /**
     * M106 - utlaantDato (xs:date)
     */
    @Column(name = "loaned_date")
    @DateTimeFormat(iso = DATE)
    @Audited
    private OffsetDateTime loanedDate;

    /**
     * M309 - utlaantTil (xs:string)
     */
    @Column(name = "loaned_to")
    @Audited
    private String loanedTo;

    // Links to DocumentFlow
    @OneToMany(mappedBy = "referenceRecordNote")
    private List<DocumentFlow> referenceDocumentFlow = new ArrayList<>();


    public OffsetDateTime getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(OffsetDateTime documentDate) {
        this.documentDate = documentDate;
    }

    public OffsetDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(OffsetDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public OffsetDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(OffsetDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public OffsetDateTime getFreedomAssessmentDate() {
        return freedomAssessmentDate;
    }

    public void setFreedomAssessmentDate(OffsetDateTime freedomAssessmentDate) {
        this.freedomAssessmentDate = freedomAssessmentDate;
    }

    public Integer getNumberOfAttachments() {
        return numberOfAttachments;
    }

    public void setNumberOfAttachments(Integer numberOfAttachments) {
        this.numberOfAttachments = numberOfAttachments;
    }

    public OffsetDateTime getLoanedDate() {
        return loanedDate;
    }

    public void setLoanedDate(OffsetDateTime loanedDate) {
        this.loanedDate = loanedDate;
    }

    public String getLoanedTo() {
        return loanedTo;
    }

    public void setLoanedTo(String loanedTo) {
        this.loanedTo = loanedTo;
    }

    @Override
    public List<DocumentFlow> getReferenceDocumentFlow() {
        return referenceDocumentFlow;
    }

    @Override
    public void setReferenceDocumentFlow(
            List<DocumentFlow> referenceDocumentFlow) {
        this.referenceDocumentFlow = referenceDocumentFlow;
    }

    @Override
    public void addReferenceDocumentFlow(DocumentFlow referenceDocumentFlow) {
        this.referenceDocumentFlow.add(referenceDocumentFlow);
    }

    @Override
    public String getBaseTypeName() {
        return RECORD_NOTE;
    }

    @Override
    public String getBaseRel() {
        return REL_CASE_HANDLING_RECORD_NOTE;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_CASE_HANDLING_PATH;
    }

    @Override
    public String toString() {
        return super.toString() + " RecordNote{" + super.toString() +
                ", loanedTo=" + loanedTo +
                ", loanedDate=" + loanedDate +
                ", numberOfAttachments=" + numberOfAttachments +
                ", freedomAssessmentDate=" + freedomAssessmentDate +
                ", dueDate=" + dueDate +
                ", sentDate=" + sentDate +
                ", receivedDate=" + receivedDate +
                ", documentDate=" + documentDate +
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
        RecordNote rhs = (RecordNote) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(loanedTo, rhs.loanedTo)
                .append(loanedDate, rhs.loanedDate)
                .append(numberOfAttachments, rhs.numberOfAttachments)
                .append(freedomAssessmentDate, rhs.freedomAssessmentDate)
                .append(dueDate, rhs.dueDate)
                .append(sentDate, rhs.sentDate)
                .append(receivedDate, rhs.receivedDate)
                .append(documentDate, rhs.documentDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(loanedTo)
                .append(loanedDate)
                .append(numberOfAttachments)
                .append(freedomAssessmentDate)
                .append(dueDate)
                .append(sentDate)
                .append(receivedDate)
                .append(documentDate)
                .toHashCode();
    }
}
