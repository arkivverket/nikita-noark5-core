package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IConversionEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_CONVERSION)
public class Conversion
        extends SystemIdEntity
        implements IConversionEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M615 - konvertertDato (xs:dateTime)
     */
    @Column(name = CONVERTED_DATE_ENG)
    @Audited
    @JsonProperty(CONVERTED_DATE)
    private OffsetDateTime convertedDate;

    /**
     * M616 - konvertertAv (xs:string)
     */
    @Column(name = CONVERTED_BY_ENG)
    @Audited
    @JsonProperty(CONVERTED_BY)
    private String convertedBy;

    /**
     * M712 - konvertertFraFormat (xs:string)
     */
    @Column(name = CONVERTED_FROM_FORMAT_ENG)
    @Audited
    @JsonProperty(CONVERTED_FROM_FORMAT)
    private String convertedFromFormat;

    /**
     * M713 - konvertertTilFormat (xs:string)
     */
    @Column(name = CONVERTED_TO_FORMAT_ENG)
    @Audited
    @JsonProperty(CONVERTED_TO_FORMAT)
    private String convertedToFormat;

    /**
     * M714 - konverteringsverktoey (xs:string)
     */
    @Column(name = CONVERSION_TOOL_ENG)
    @Audited
    @JsonProperty(CONVERSION_TOOL)
    private String conversionTool;

    /**
     * M715 - konverteringskommentar (xs:string)
     */
    @Column(name = CONVERSION_COMMENT_ENG)
    @Audited
    @JsonProperty(CONVERSION_COMMENT)
    private String conversionComment;

    // Link to DocumentObject
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CONVERSION_DOCUMENT_OBJECT_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private DocumentObject referenceDocumentObject;

    @Override
    public OffsetDateTime getConvertedDate() {
        return convertedDate;
    }

    @Override
    public void setConvertedDate(OffsetDateTime convertedDate) {
        this.convertedDate = convertedDate;
    }

    @Override
    public String getConvertedBy() {
        return convertedBy;
    }

    @Override
    public void setConvertedBy(String convertedBy) {
        this.convertedBy = convertedBy;
    }

    @Override
    public String getConvertedFromFormat() {
        return convertedFromFormat;
    }

    @Override
    public void setConvertedFromFormat(String convertedFromFormat) {
        this.convertedFromFormat = convertedFromFormat;
    }

    @Override
    public String getConvertedToFormat() {
        return convertedToFormat;
    }

    @Override
    public void setConvertedToFormat(String convertedToFormat) {
        this.convertedToFormat = convertedToFormat;
    }

    @Override
    public String getConversionTool() {
        return conversionTool;
    }

    @Override
    public void setConversionTool(String conversionTool) {
        this.conversionTool = conversionTool;
    }

    @Override
    public String getConversionComment() {
        return conversionComment;
    }

    @Override
    public void setConversionComment(String conversionComment) {
        this.conversionComment = conversionComment;
    }

    @Override
    public String getBaseTypeName() {
        return CONVERSION;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_CONVERSION;
    }

    @Override
    public DocumentObject getReferenceDocumentObject() {
        return referenceDocumentObject;
    }

    @Override
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
