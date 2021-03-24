package nikita.common.model.noark5.bsm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.model.noark5.v5.secondary.Part;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Inheritance(strategy = JOINED)
@Table(name = TABLE_BSM_BASE)
public class BSMBase {

    /**
     * M001 - systemID (xs:string)
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {@Parameter(
                    name = "uuid_gen_strategy_class",
                    value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    @Column(name = SYSTEM_ID, updatable = false, nullable = false)
    @Type(type = "uuid-char")
    private UUID systemId;

    @Audited
    @Column
    protected String dataType;

    @Audited
    @Column
    protected String valueName;

    @Audited
    @Column
    protected String valueNamespace;

    @Column
    private String stringValue;

    @Column
    private Boolean booleanValue;

    @Column
    // Do not rename this variable as it is used for BSM query generation
    private OffsetDateTime offsetdatetimeValue;

    @Column
    private Double doubleValue;

    @Column
    private Integer integerValue;

    @Column
    private String uriValue;

    @CreatedBy
    @Column(name = OWNED_BY)
    @JsonProperty
    @Audited
    private String ownedBy;

    /**
     * M600 - opprettetDato (xs:dateTime)
     */
    @CreatedDate
    @Column(name = CREATED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(CREATED_DATE)
    private OffsetDateTime createdDate;

    /**
     * M601 - opprettetAv (xs:string)
     */
    @CreatedBy
    @Column(name = CREATED_BY_ENG)
    @Audited
    @JsonProperty(CREATED_BY)
    private String createdBy;

    /**
     * M??? - oppdatertDato (xs:dateTime)
     */
    @LastModifiedDate
    @Column(name = LAST_MODIFIED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @JsonProperty(LAST_MODIFIED_DATE)
    private OffsetDateTime lastModifiedDate;

    /**
     * M??? - oppdatertAv (xs:string)
     */
    @LastModifiedBy
    @Column(name = LAST_MODIFIED_BY_ENG)
    @JsonProperty(LAST_MODIFIED_BY)
    private String lastModifiedBy;

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

    public UUID getSystemId() {
        return systemId;
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

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
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

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return referenceAdministrativeUnit;
    }

    public void setReferenceAdministrativeUnit(AdministrativeUnit referenceAdministrativeUnit) {
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }
}
