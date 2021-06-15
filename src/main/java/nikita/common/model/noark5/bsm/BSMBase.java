package nikita.common.model.noark5.bsm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.model.noark5.v5.hateoas.md_other.BSMBaseHateoas;
import nikita.common.model.noark5.v5.secondary.Part;
import nikita.common.util.deserialisers.BSMDeserialiser;
import nikita.webapp.hateoas.metadata.BSMBaseHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;

@Entity
@Inheritance(strategy = JOINED)
@Table(name = TABLE_BSM_BASE)
@JsonDeserialize(using = BSMDeserialiser.class)
@HateoasPacker(using = BSMBaseHateoasHandler.class)
@HateoasObject(using = BSMBaseHateoas.class)
public class BSMBase
        extends SystemIdEntity {

    @Audited
    @Column(nullable = false)
    protected String dataType;

    @Audited
    @Column
    protected String valueName;

    @Audited
    @Column
    protected String valueNamespace;

    @Column(length = 10000)
    private String stringValue;

    @Column
    private Boolean booleanValue;

    /*
     * This value is used to indicate whether or not the actual value BSMBase
     * refers to is null or not. When parsing an OData query for null, it is
     * not possible to identify which of the values (booleanValue, stringValue
     * etc) is the correct one to check null against. This is like a
     * compile time/runtime issue. Therefore when setting a BSMBase value to
     * null, the following flag must be set to true.
     */
    @Column
    private Boolean isNullValue = false;

    @Column
    // Do not rename this variable as it is used for BSM query generation
    private OffsetDateTime offsetdatetimeValue;

    @Column
    private Double doubleValue;

    @Column
    private Integer integerValue;

    @Column
    private String uriValue;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = BSM_FILE_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private File referenceFile;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = BSM_RECORD_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Record referenceRecord;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = BSM_PART_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Part referencePart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = BSM_CORRESPONDENCE_PART_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private CorrespondencePart referenceCorrespondencePart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = BSM_DOCUMENT_DESCRIPTION_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private DocumentDescription referenceDocumentDescription;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = BSM_ADMINISTRATIVE_UNIT_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private AdministrativeUnit referenceAdministrativeUnit;

    public BSMBase(String valueName, String stringValue) {
        this.valueName = valueName;
        this.stringValue = stringValue;
        this.dataType = TYPE_STRING;
    }

    public BSMBase(String valueName, Boolean booleanValue) {
        this.valueName = valueName;
        this.booleanValue = booleanValue;
        this.dataType = TYPE_BOOLEAN;
    }

    public BSMBase(String valueName, OffsetDateTime offsetdatetimeValue,
                   Boolean isDate) {
        this.valueName = valueName;
        this.offsetdatetimeValue = offsetdatetimeValue;
        if (isDate) {
            this.dataType = TYPE_DATE;
        } else {
            this.dataType = TYPE_DATE_TIME;
        }
    }

    public BSMBase(String valueName, OffsetDateTime offsetdatetimeValue) {
        this.valueName = valueName;
        this.offsetdatetimeValue = offsetdatetimeValue;
        this.dataType = TYPE_DATE_TIME;
    }

    public BSMBase(String valueName, Double doubleValue) {
        this.valueName = valueName;
        this.doubleValue = doubleValue;
        this.dataType = TYPE_DOUBLE;
    }

    public BSMBase(String valueName, Integer integerValue) {
        this.valueName = valueName;
        this.integerValue = integerValue;
        this.dataType = TYPE_INTEGER;
    }

    // Create basic object, add later what type of object you want
    // to contains. Need to seperate string URI from string value
    public BSMBase(String valueName) {
        this.valueName = valueName;
    }

    public BSMBase() {
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValueNamespace() {
        return valueNamespace;
    }

    public void setValueNamespace(String valueNamespace) {
        this.valueNamespace = valueNamespace;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
        this.dataType = TYPE_STRING;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
        this.dataType = TYPE_BOOLEAN;
    }

    public Boolean getNullValue() {
        return isNullValue;
    }

    public void setNullValue(Boolean nullValue) {
        isNullValue = nullValue;
    }

    public OffsetDateTime getDateTimeValue() {
        return offsetdatetimeValue;
    }

    public void setDateTimeValue(OffsetDateTime offsetdatetimeValue) {
        this.offsetdatetimeValue = offsetdatetimeValue;
        this.dataType = TYPE_DATE_TIME;
    }

    public void setDateTimeValue(OffsetDateTime offsetdatetimeValue,
                                 Boolean isDate) {
        this.offsetdatetimeValue = offsetdatetimeValue;
        if (isDate) {
            this.dataType = TYPE_DATE;
        } else {
            this.dataType = TYPE_DATE_TIME;
        }
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
        this.dataType = TYPE_DOUBLE;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
        this.dataType = TYPE_INTEGER;
    }

    public String getUriValue() {
        return uriValue;
    }

    public void setUriValue(String uriValue) {
        this.uriValue = uriValue;
        this.dataType = TYPE_URI;
    }

    public File getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(File referenceFile) {
        this.referenceFile = referenceFile;
    }

    public Record getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(Record referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public Part getReferencePart() {
        return referencePart;
    }

    public void setReferencePart(Part referencePart) {
        this.referencePart = referencePart;
    }

    public CorrespondencePart getReferenceCorrespondencePart() {
        return referenceCorrespondencePart;
    }

    public void setReferenceCorrespondencePart(CorrespondencePart referenceCorrespondencePart) {
        this.referenceCorrespondencePart = referenceCorrespondencePart;
    }

    public DocumentDescription getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(DocumentDescription referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return referenceAdministrativeUnit;
    }

    public void setReferenceAdministrativeUnit(AdministrativeUnit referenceAdministrativeUnit) {
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_BSM;
    }

    @Override
    public String toString() {
        return "BSMBase{" +
                "dataType='" + dataType + '\'' +
                ", valueName='" + valueName + '\'' +
                ", valueNamespace='" + valueNamespace + '\'' +
                ", stringValue='" + stringValue + '\'' +
                ", booleanValue=" + booleanValue +
                ", isNullValue=" + isNullValue +
                ", offsetdatetimeValue=" + offsetdatetimeValue +
                ", doubleValue=" + doubleValue +
                ", integerValue=" + integerValue +
                ", uriValue='" + uriValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BSMBase bsmBase = (BSMBase) o;
        return Objects.equals(dataType, bsmBase.dataType) &&
                Objects.equals(valueName, bsmBase.valueName) &&
                Objects.equals(valueNamespace, bsmBase.valueNamespace) &&
                Objects.equals(stringValue, bsmBase.stringValue) &&
                Objects.equals(booleanValue, bsmBase.booleanValue) &&
                Objects.equals(isNullValue, bsmBase.isNullValue) &&
                Objects.equals(offsetdatetimeValue, bsmBase.offsetdatetimeValue) &&
                Objects.equals(doubleValue, bsmBase.doubleValue) &&
                Objects.equals(integerValue, bsmBase.integerValue) &&
                Objects.equals(uriValue, bsmBase.uriValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataType, valueName,
                valueNamespace, stringValue, booleanValue, isNullValue,
                offsetdatetimeValue, doubleValue, integerValue, uriValue);
    }
}
