package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.interfaces.IDocumentFlow;

import java.time.OffsetDateTime;

public interface IRecordNoteEntity
        extends IRecordEntity, IDocumentFlow {

    OffsetDateTime getDocumentDate();

    void setDocumentDate(OffsetDateTime documentDate);

    OffsetDateTime getReceivedDate();

    void setReceivedDate(OffsetDateTime receivedDate);

    OffsetDateTime getSentDate();

    void setSentDate(OffsetDateTime sentDate);

    OffsetDateTime getDueDate();

    void setDueDate(OffsetDateTime dueDate);

    OffsetDateTime getFreedomAssessmentDate();

    void setFreedomAssessmentDate(OffsetDateTime freedomAssessmentDate);

    Integer getNumberOfAttachments();

    void setNumberOfAttachments(Integer numberOfAttachments);

    OffsetDateTime getLoanedDate();

    void setLoanedDate(OffsetDateTime loanedDate);

    String getLoanedTo();

    void setLoanedTo(String loanedTo);

}
