package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.ConversionHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IConversionEntity;
import nikita.common.model.noark5.v5.metadata.Format;
import nikita.common.util.deserialisers.secondary.ConversionDeserializer;
import nikita.webapp.hateoas.secondary.ConversionHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_CONVERSION)
@JsonDeserialize(using = ConversionDeserializer.class)
@HateoasPacker(using = ConversionHateoasHandler.class)
@HateoasObject(using = ConversionHateoas.class)
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
     * M??? - konvertertFraFormat code (xs:string)
     */
    @Column(name = "converted_from_format_code")
    @Audited
    private String convertedFromFormatCode;

    /**
     * M712 - konvertertFraFormat code name (xs:string)
     */
    @Column(name = "converted_from_format_code_name")
    @Audited
    private String convertedFromFormatCodeName;

    /**
     * M??? - konvertertTilFormat code (xs:string)
     */
    @Column(name = "converted_to_format_code")
    @Audited
    private String convertedToFormatCode;

    /**
     * M713 - konvertertTilFormat code name (xs:string)
     */
    @Column(name = "converted_to_format_code_name")
    @Audited
    private String convertedToFormatCodeName;

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
    public Format getConvertedFromFormat() {
        if (null == convertedFromFormatCode)
            return null;
        Format convertedFromFormat = new Format();
        convertedFromFormat.setCode(convertedFromFormatCode);
        convertedFromFormat.setCodeName(convertedFromFormatCodeName);
        return convertedFromFormat;
    }

    @Override
    public void setConvertedFromFormat(Format convertedFromFormat) {
        if (null != convertedFromFormat) {
            this.convertedFromFormatCode = convertedFromFormat.getCode();
            this.convertedFromFormatCodeName = convertedFromFormat.getCodeName();
        } else {
            this.convertedFromFormatCode = null;
            this.convertedFromFormatCodeName = null;
        }
    }

    @Override
    public Format getConvertedToFormat() {
        if (null == convertedToFormatCode)
            return null;
        Format convertedToFormat = new Format();
        convertedToFormat.setCode(convertedToFormatCode);
        convertedToFormat.setCodeName(convertedToFormatCodeName);
        return convertedToFormat;
    }

    @Override
    public void setConvertedToFormat(Format convertedToFormat) {
        if (null != convertedToFormat) {
            this.convertedToFormatCode = convertedToFormat.getCode();
            this.convertedToFormatCodeName = convertedToFormat.getCodeName();
        } else {
            this.convertedToFormatCode = null;
            this.convertedToFormatCodeName = null;
        }
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
                ", convertedFromFormatCode='" + convertedFromFormatCode + '\'' +
                ", convertedFromFormatCodeName='" + convertedFromFormatCodeName + '\'' +
                ", convertedToFormatCode='" + convertedToFormatCode + '\'' +
                ", convertedToFormatCodeName='" + convertedToFormatCodeName + '\'' +
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
                .append(convertedFromFormatCode, rhs.convertedFromFormatCode)
                .append(convertedFromFormatCodeName, rhs.convertedFromFormatCodeName)
                .append(convertedToFormatCodeName, rhs.convertedToFormatCodeName)
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
                .append(convertedFromFormatCode)
                .append(convertedFromFormatCodeName)
                .append(convertedToFormatCode)
                .append(convertedToFormatCodeName)
                .append(conversionTool)
                .append(conversionComment)
                .toHashCode();
    }
}
