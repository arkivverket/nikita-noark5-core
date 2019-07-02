package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.NoarkEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CONVERSION;

@Entity
@Table(name = TABLE_CONVERSION)
public class Conversion
        extends NoarkEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M615 - konvertertDato (xs:dateTime)
     */
    @Column(name = "converted_date")
    @Audited
    private OffsetDateTime convertedDate;

    /**
     * M616 - konvertertAv (xs:string)
     */
    @Column(name = "converted_by")
    @Audited
    private String convertedBy;

    /**
     * M712 - konvertertFraFormat (xs:string)
     */
    @Column(name = "converted_from_format")
    @Audited
    private String convertedFromFormat;

    /**
     * M713 - konvertertTilFormat (xs:string)
     */
    @Column(name = "converted_to_format")
    @Audited
    private String convertedToFormat;

    /**
     * M714 - konverteringsverktoey (xs:string)
     */
    @Column(name = "conversion_tool")
    @Audited
    private String conversionTool;

    /**
     * M715 - konverteringskommentar (xs:string)
     */
    @Column(name = "conversion_comment")
    @Audited
    private String conversionComment;

    // Link to DocumentObject
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CONVERSION_DOCUMENT_OBJECT_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private DocumentObject referenceDocumentObject;

    public OffsetDateTime getConvertedDate() {
        return convertedDate;
    }

    public void setConvertedDate(OffsetDateTime convertedDate) {
        this.convertedDate = convertedDate;
    }

    public String getConvertedBy() {
        return convertedBy;
    }

    public void setConvertedBy(String convertedBy) {
        this.convertedBy = convertedBy;
    }

    public String getConvertedFromFormat() {
        return convertedFromFormat;
    }

    public void setConvertedFromFormat(String convertedFromFormat) {
        this.convertedFromFormat = convertedFromFormat;
    }

    public String getConvertedToFormat() {
        return convertedToFormat;
    }

    public void setConvertedToFormat(String convertedToFormat) {
        this.convertedToFormat = convertedToFormat;
    }

    public String getConversionTool() {
        return conversionTool;
    }

    public void setConversionTool(String conversionTool) {
        this.conversionTool = conversionTool;
    }

    public String getConversionComment() {
        return conversionComment;
    }

    public void setConversionComment(String conversionComment) {
        this.conversionComment = conversionComment;
    }

    @Override
    public String getBaseTypeName() {
        return CONVERSION;
    }

    public DocumentObject getReferenceDocumentObject() {
        return referenceDocumentObject;
    }

    public void setReferenceDocumentObject(
            DocumentObject referenceDocumentObject) {
        this.referenceDocumentObject = referenceDocumentObject;
    }

    @Override
    public String toString() {
        return "Conversion{" + super.toString() +
                ", convertedDate=" + convertedDate +
                ", convertedBy='" + convertedBy + '\'' +
                ", convertedFromFormat='" + convertedFromFormat + '\'' +
                ", convertedToFormat='" + convertedToFormat + '\'' +
                ", conversionTool='" + conversionTool + '\'' +
                ", conversionComment='" + conversionComment + '\'' +
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
        Conversion rhs = (Conversion) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(convertedDate, rhs.convertedDate)
                .append(convertedBy, rhs.convertedBy)
                .append(convertedFromFormat, rhs.convertedFromFormat)
                .append(convertedToFormat, rhs.convertedToFormat)
                .append(conversionTool, rhs.conversionTool)
                .append(conversionComment, rhs.conversionComment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(convertedDate)
                .append(convertedBy)
                .append(convertedFromFormat)
                .append(convertedToFormat)
                .append(conversionTool)
                .append(conversionComment)
                .toHashCode();
    }
}
