package nikita.common.model.noark5.v5.casehandling;

import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.interfaces.entities.ICaseFileEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.CaseStatus;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.secondary.*;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static nikita.common.config.Constants.NOARK_CASE_HANDLING_PATH;
import static nikita.common.config.Constants.REL_CASE_HANDLING_CASE_FILE;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * A lot of methods are implemented, just to be in compliance with the
 * ICaseFileEntity interfaces. They will never have a meaningful
 * implementation in this class as the class is just a dummy place holder so
 * the serialiser can create a proper CaseFile like payload.
 */
public class CaseFileExpansion
        implements ICaseFileEntity {

    /**
     * M011 - saksaar (xs:integer)
     */
    @JsonProperty(CASE_YEAR)
    private Integer caseYear;

    /**
     * M012 - sakssekvensnummer (xs:integer)
     */
    @JsonProperty(CASE_SEQUENCE_NUMBER)
    private Integer caseSequenceNumber;

    /**
     * M100 - saksdato (xs:date)
     */
    @DateTimeFormat(iso = DATE_TIME)
    @JsonProperty(CASE_DATE)
    private OffsetDateTime caseDate;

    /**
     * M306 - saksansvarlig (xs:string)
     */
    @JsonProperty(CASE_RESPONSIBLE)
    private String caseResponsible;

    /**
     * M308 - journalenhet (xs:string)
     */
    @JsonProperty(CASE_RECORDS_MANAGEMENT_UNIT)
    private String recordsManagementUnit;

    /**
     * M??? - saksstatus kode (xs:string)
     */
    @NotNull
    @Column(name = CASE_STATUS_CODE_ENG, nullable = false)
    @JsonProperty(CASE_STATUS_CODE)
    @Audited
    private String caseStatusCode;
    /**
     * M??? - saksstatus name (xs:string)
     */
    @Column(name = CASE_STATUS_CODE_NAME_ENG)
    @JsonProperty(CASE_STATUS_CODE_NAME)
    @Audited
    private String caseStatusCodeName;

    // Link to AdministrativeUnit
    private AdministrativeUnit referenceAdministrativeUnit;

    public CaseFileExpansion() {
    }

    public Integer getCaseYear() {
        return caseYear;
    }

    public void setCaseYear(Integer caseYear) {
        this.caseYear = caseYear;
    }

    public Integer getCaseSequenceNumber() {
        return caseSequenceNumber;
    }

    public void setCaseSequenceNumber(Integer caseSequenceNumber) {
        this.caseSequenceNumber = caseSequenceNumber;
    }

    public OffsetDateTime getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(OffsetDateTime caseDate) {
        this.caseDate = caseDate;
    }

    public String getCaseResponsible() {
        return caseResponsible;
    }

    public void setCaseResponsible(String caseResponsible) {
        this.caseResponsible = caseResponsible;
    }

    public String getRecordsManagementUnit() {
        return recordsManagementUnit;
    }

    public void setRecordsManagementUnit(String recordsManagementUnit) {
        this.recordsManagementUnit = recordsManagementUnit;
    }

    @Override
    public CaseStatus getCaseStatus() {
        if (null == caseStatusCode)
            return null;
        return new CaseStatus(caseStatusCode, caseStatusCodeName);
    }

    @Override
    public void setCaseStatus(CaseStatus caseStatus) {
        if (null != caseStatus) {
            this.caseStatusCode = caseStatus.getCode();
            this.caseStatusCodeName = caseStatus.getCodeName();
        } else {
            this.caseStatusCode = null;
            this.caseStatusCodeName = null;
        }
    }

    @Override
    public String getBaseTypeName() {
        return CASE_FILE;
    }

    @Override
    public String getBaseRel() {
        return REL_CASE_HANDLING_CASE_FILE;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_CASE_HANDLING_PATH;
    }

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return referenceAdministrativeUnit;
    }

    public void setReferenceAdministrativeUnit(
            AdministrativeUnit referenceAdministrativeUnit) {
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    @Override
    public OffsetDateTime getLoanedDate() {
        return null;
    }

    @Override
    public void setLoanedDate(OffsetDateTime loanedDate) {
    }

    @Override
    public String getLoanedTo() {
        return null;
    }

    @Override
    public void setLoanedTo(String loanedTo) {
    }

    @Override
    public List<BSMBase> getReferenceBSMBase() {
        return null;
    }

    @Override
    public void addBSMBase(BSMBase bSMBase) {

    }

    @Override
    public void removeBSMBase(BSMBase bSMBase) {

    }

    @Override
    public Classified getReferenceClassified() {
        return null;
    }

    @Override
    public void setReferenceClassified(Classified classified) {

    }

    @Override
    public Set<Comment> getReferenceComment() {
        return null;
    }

    @Override
    public void addComment(Comment comment) {

    }

    @Override
    public List<CrossReference> getReferenceCrossReference() {
        return null;
    }

    @Override
    public void addCrossReference(CrossReference crossReference) {

    }

    @Override
    public void removeKeyword(Keyword keyword) {

    }

    @Override
    public void removeCrossReference(CrossReference crossReference) {

    }

    @Override
    public Disposal getReferenceDisposal() {
        return null;
    }

    @Override
    public void setReferenceDisposal(Disposal disposal) {

    }

    @Override
    public DocumentMedium getDocumentMedium() {
        return null;
    }

    @Override
    public void setDocumentMedium(DocumentMedium documentMediumCode) {

    }

    @Override
    public Set<Keyword> getReferenceKeyword() {
        return null;
    }

    @Override
    public void addKeyword(Keyword keyword) {

    }

    @Override
    public Set<Part> getReferencePart() {
        return null;
    }

    @Override
    public void addPart(Part part) {

    }

    @Override
    public void removePart(Part part) {

    }

    @Override
    public Set<Precedence> getReferencePrecedence() {
        return null;
    }

    @Override
    public void addPrecedence(Precedence precedence) {

    }

    @Override
    public void removePrecedence(Precedence precedence) {
    }

    @Override
    public Screening getReferenceScreening() {
        return null;
    }

    @Override
    public void setReferenceScreening(Screening screening) {

    }

    @Override
    public Set<StorageLocation> getReferenceStorageLocation() {
        return null;
    }

    @Override
    public void addReferenceStorageLocation(StorageLocation storageLocation) {

    }

    @Override
    public void removeReferenceStorageLocation(StorageLocation storageLocation) {

    }


    @Override
    public OffsetDateTime getCreatedDate() {
        return null;
    }

    @Override
    public void setCreatedDate(OffsetDateTime createdDate) {

    }

    @Override
    public String getCreatedBy() {
        return null;
    }

    @Override
    public void setCreatedBy(String createdBy) {

    }

    @Override
    public String getFileId() {
        return null;
    }

    @Override
    public void setFileId(String fileId) {

    }

    @Override
    public String getPublicTitle() {
        return null;
    }

    @Override
    public void setPublicTitle(String publicTitle) {

    }

    @Override
    public OffsetDateTime getFinalisedDate() {
        return null;
    }

    @Override
    public void setFinalisedDate(OffsetDateTime FinalisedDate) {

    }

    @Override
    public String getFinalisedBy() {
        return null;
    }

    @Override
    public void setFinalisedBy(String FinalisedBy) {

    }

    @Override
    public OffsetDateTime getLastModifiedDate() {
        return null;
    }

    @Override
    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {

    }

    @Override
    public String getLastModifiedBy() {
        return null;
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {

    }

    @Override
    public String getOwnedBy() {
        return null;
    }

    @Override
    public void setOwnedBy(String ownedBy) {

    }

    @Override
    public Long getVersion() {
        return null;
    }

    @Override
    public void setVersion(Long version) {

    }

    @Override
    public void setVersion(Long version, Boolean override) {

    }

    @Override
    public void createReference(INoarkEntity entity, String referenceType) {

    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public String getIdentifierType() {
        return null;
    }

    @Override
    public UUID getSystemId() {
        return null;
    }

    @Override
    public void setSystemId(UUID systemId) {

    }

    @Override
    public String getSystemIdAsString() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void addReferenceBSMBase(List<BSMBase> bSMBase) {
    }

    @Override
    public String toString() {
        return super.toString() + " CaseFile{" +
                "caseStatusCode='" + caseStatusCode + '\'' +
                ", caseStatusCodeName='" + caseStatusCodeName + '\'' +
                ", recordsManagementUnit='" + recordsManagementUnit + '\'' +
                ", caseResponsible='" + caseResponsible + '\'' +
                ", caseDate=" + caseDate +
                ", caseSequenceNumber=" + caseSequenceNumber +
                ", caseYear=" + caseYear +
                '}';
    }
}
