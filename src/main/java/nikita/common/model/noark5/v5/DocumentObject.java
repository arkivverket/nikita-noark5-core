package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.interfaces.IConversion;
import nikita.common.model.noark5.v5.interfaces.IElectronicSignature;
import nikita.common.model.noark5.v5.interfaces.entities.ICreate;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.Format;
import nikita.common.model.noark5.v5.metadata.VariantFormat;
import nikita.common.model.noark5.v5.secondary.Conversion;
import nikita.common.model.noark5.v5.secondary.ElectronicSignature;
import nikita.common.util.deserialisers.DocumentObjectDeserializer;
import nikita.webapp.hateoas.DocumentObjectHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_DOCUMENT_OBJECT,
        indexes = {
                @Index(name = "index_filname",
                        columnList = "original_filename"),
                @Index(name = "index_owned_by",
                        columnList = "owned_by")})
@JsonDeserialize(using = DocumentObjectDeserializer.class)
@HateoasPacker(using = DocumentObjectHateoasHandler.class)
@HateoasObject(using = DocumentObjectHateoas.class)
public class DocumentObject
        extends SystemIdEntity
        implements ICreate, IElectronicSignature, IConversion {

    /**
     * M005 - versjonsnummer (xs:integer)
     **/
    @NotNull
    @Column(name = DOCUMENT_OBJECT_VERSION_NUMBER_ENG, nullable = false)
    @Audited
    private Integer versionNumber;

    /**
     * M??? - variantformat code (xs:string)
     */
    @NotNull
    @Column(name = "variant_format_code", nullable = false)
    @Audited
    private String variantFormatCode;

    /**
     * M700 - variantformat code name (xs:string)
     */
    @NotNull
    @Column(name = "variant_format_code_name", nullable = false)
    @Audited
    private String variantFormatCodeName;

    /**
     * M??? - format code (xs:string)
     */
    @Column(name = "format_code")
    @Audited
    private String formatCode;

    /**
     * M701 - format code name (xs:string)
     */
    @Column(name = "format_code_name")
    @Audited
    private String formatCodeName;

    /**
     * M702 - formatDetaljer (xs:string)
     */
    @Column(name = DOCUMENT_OBJECT_FORMAT_DETAILS_ENG)
    @Audited
    private String formatDetails;

    /**
     * M600 - opprettetDato (xs:dateTime)
     */
    @Column(name = CREATED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private OffsetDateTime createdDate;

    /**
     * M601 - opprettetAv (xs:string)
     */
    @Column(name = CREATED_BY_ENG)
    @Audited
    private String createdBy;

    /**
     * M218 - referanseDokumentfil (xs:string)
     */
    @Column(name = DOCUMENT_OBJECT_REFERENCE_DOCUMENT_FILE_ENG)
    @Audited
    private String referenceDocumentFile;

    /**
     * M705 - sjekksum (xs:string)
     */
    @Column(name = DOCUMENT_OBJECT_CHECKSUM_ENG)
    @Audited
    private String checksum;

    /**
     * M706 - sjekksumAlgoritme (xs:string)
     */
    @Column(name = DOCUMENT_OBJECT_CHECKSUM_ALGORITHM_ENG)
    @Audited
    private String checksumAlgorithm;

    /**
     * M707 - filstoerrelse (xs:string)
     */
    @Column(name = DOCUMENT_OBJECT_FILE_SIZE_ENG)
    @Audited
    private Long fileSize;

    /**
     * M??? - filnavn (xs:string)
     */
    @Column(name = DOCUMENT_OBJECT_FILE_NAME_ENG)
    @Audited
    private String originalFilename;

    /**
     * M??? - mimeType (xs:string)
     */
    @Column(name = DOCUMENT_OBJECT_MIME_TYPE_ENG)
    @Audited
    private String mimeType;

    // Link to DocumentDescription
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = DOCUMENT_OBJECT_DOCUMENT_DESCRIPTION_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private DocumentDescription referenceDocumentDescription;

    // Links to Conversion
    @OneToMany(mappedBy = "referenceDocumentObject")
    private List<Conversion> referenceConversion = new ArrayList<>();

    // Link to ElectronicSignature
    @OneToOne
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID)
    private ElectronicSignature referenceElectronicSignature;

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public VariantFormat getVariantFormat() {
        if (null == variantFormatCode)
            return null;
        return new VariantFormat(variantFormatCode, variantFormatCodeName);
    }

    public void setVariantFormat(VariantFormat variantFormat) {
        if (null != variantFormat) {
            this.variantFormatCode = variantFormat.getCode();
            this.variantFormatCodeName = variantFormat.getCodeName();
        } else {
            this.variantFormatCode = null;
            this.variantFormatCodeName = null;
        }
    }

   public Format getFormat() {
        if (null == formatCode)
            return null;
        return new Format(formatCode, formatCodeName);
    }

    public void setFormat(Format format) {
        if (null != format) {
            this.formatCode = format.getCode();
            this.formatCodeName = format.getCodeName();
        } else {
            this.formatCode = null;
            this.formatCodeName = null;
        }
    }

    public String getFormatDetails() {
        return formatDetails;
    }

    public void setFormatDetails(String formatDetails) {
        this.formatDetails = formatDetails;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getReferenceDocumentFile() {
        return referenceDocumentFile;
    }

    public void setReferenceDocumentFile(String referenceDocumentFile) {
        this.referenceDocumentFile = referenceDocumentFile;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    public void setChecksumAlgorithm(String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_OBJECT;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_DOCUMENT_OBJECT;
    }

    public DocumentDescription getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            DocumentDescription referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public List<Conversion> getReferenceConversion() {
        return referenceConversion;
    }

    public void setReferenceConversion(List<Conversion> referenceConversion) {
        this.referenceConversion = referenceConversion;
    }

    public void addReferenceConversion(Conversion referenceConversion) {
        this.referenceConversion.add(referenceConversion);
    }

    public ElectronicSignature getReferenceElectronicSignature() {
        return referenceElectronicSignature;
    }

    public void setReferenceElectronicSignature(
            ElectronicSignature referenceElectronicSignature) {
        this.referenceElectronicSignature = referenceElectronicSignature;
    }

    @Override
    public String toString() {
        return "DocumentObject{" + super.toString() +
                ", fileSize=" + fileSize +
                ", checksumAlgorithm='" + checksumAlgorithm + '\'' +
                ", checksum='" + checksum + '\'' +
                ", referenceDocumentFile='" + referenceDocumentFile + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", formatDetails='" + formatDetails + '\'' +
                ", formatCode='" + formatCode + '\'' +
                ", formatCodeName='" + formatCodeName + '\'' +
                ", variantFormatCode='" + variantFormatCode + '\'' +
                ", variantFormatCodeName='" + variantFormatCodeName + '\'' +
                ", versionNumber=" + versionNumber +
                ", mimeType=" + mimeType +
                ", originalFilename=" + originalFilename +
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
        DocumentObject rhs = (DocumentObject) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(fileSize, rhs.fileSize)
                .append(checksumAlgorithm, rhs.checksumAlgorithm)
                .append(checksum, rhs.checksum)
                .append(referenceDocumentFile, rhs.referenceDocumentFile)
                .append(createdBy, rhs.createdBy)
                .append(createdDate, rhs.createdDate)
                .append(formatDetails, rhs.formatDetails)
                .append(formatCode, rhs.formatCode)
                .append(formatCodeName, rhs.formatCodeName)
                .append(variantFormatCode, rhs.variantFormatCode)
                .append(variantFormatCodeName, rhs.variantFormatCodeName)
                .append(versionNumber, rhs.versionNumber)
                .append(mimeType, rhs.mimeType)
                .append(originalFilename, rhs.originalFilename)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(fileSize)
                .append(checksumAlgorithm)
                .append(checksum)
                .append(referenceDocumentFile)
                .append(createdBy)
                .append(createdDate)
                .append(formatDetails)
                .append(formatCode)
                .append(formatCodeName)
                .append(variantFormatCode)
                .append(variantFormatCodeName)
                .append(versionNumber)
                .append(mimeType)
                .append(originalFilename)
                .toHashCode();
    }
}
