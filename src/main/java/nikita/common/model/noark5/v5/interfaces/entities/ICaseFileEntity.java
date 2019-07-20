package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.interfaces.IPart;
import nikita.common.model.noark5.v5.interfaces.IPrecedence;
import nikita.common.model.noark5.v5.metadata.CaseStatus;

import java.time.OffsetDateTime;

/**
 * Created by tsodring
 */
public interface ICaseFileEntity
        extends IFileEntity, IPrecedence, IPart {


    Integer getCaseYear();

    void setCaseYear(Integer caseYear);

    Integer getCaseSequenceNumber();

    void setCaseSequenceNumber(Integer caseSequenceNumber);

    OffsetDateTime getCaseDate();

    void setCaseDate(OffsetDateTime caseDate);

    String getCaseResponsible();

    void setCaseResponsible(String caseResponsible);

    String getRecordsManagementUnit();

    void setRecordsManagementUnit(String recordsManagementUnit);

    String getCaseStatusName();

    void setCaseStatusName(String caseStatusName);

    String getCaseStatusCode();

    void setCaseStatusCode(String caseStatusCode);

    OffsetDateTime getLoanedDate();

    void setLoanedDate(OffsetDateTime loanedDate);

    String getLoanedTo();

    void setLoanedTo(String loanedTo);

    CaseStatus getReferenceCaseFileStatus();
}
