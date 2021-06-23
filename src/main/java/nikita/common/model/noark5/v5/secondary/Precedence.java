package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.NoarkGeneralEntity;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPrecedenceEntity;
import nikita.common.model.noark5.v5.metadata.PrecedenceStatus;
import nikita.common.util.deserialisers.secondary.PrecedenceDeserializer;
import nikita.webapp.hateoas.secondary.PrecedenceHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_PRECEDENCE)
@JsonDeserialize(using = PrecedenceDeserializer.class)
@HateoasPacker(using = PrecedenceHateoasHandler.class)
@HateoasObject(using = PrecedenceHateoas.class)
@Indexed
public class Precedence
        extends NoarkGeneralEntity
        implements IPrecedenceEntity {

    /**
     * M111 - presedensDato (xs:date)
     */
    @Column(name = PRECEDENCE_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(PRECEDENCE_DATE)
    @GenericField
    private OffsetDateTime precedenceDate;

    /**
     * M311 - presedensHjemmel (xs:string)
     */
    @Column(name = PRECEDENCE_AUTHORITY_ENG)
    @Audited
    @JsonProperty(PRECEDENCE_AUTHORITY)
    @FullTextField
    private String precedenceAuthority;

    /**
     * M312 - rettskildefaktor (xs:string)
     */
    @Column(name = PRECEDENCE_SOURCE_OF_LAW_ENG)
    @Audited
    @JsonProperty(PRECEDENCE_SOURCE_OF_LAW)
    @FullTextField
    private String sourceOfLaw;

    /**
     * M628 - presedensGodkjentDato (xs:datetime)
     */
    @Column(name = PRECEDENCE_APPROVED_DATE_ENG)
    @Audited
    @JsonProperty(PRECEDENCE_APPROVED_DATE)
    @GenericField
    private OffsetDateTime precedenceApprovedDate;

    /**
     * M629 - presedensGodkjentAv (xs:string)
     */
    @Column(name = PRECEDENCE_APPROVED_BY_ENG)
    @Audited
    @JsonProperty(PRECEDENCE_APPROVED_BY)
    @KeywordField
    private String precedenceApprovedBy;

    /**
     * M?? referansePresedensGodkjentAv (xs:string)
     */
    @Column(name = PRECEDENCE_REFERENCE_APPROVED_BY_ENG)
    @Audited
    @JsonProperty(PRECEDENCE_REFERENCE_APPROVED_BY)
    @KeywordField
    private UUID referencePrecedenceApprovedBySystemID;

    /**
     * M056 - presedensStatus code (xs:string)
     */
    @Column(name = "precedence_status_code")
    @Audited
    private String precedenceStatusCode;

    /**
     * M056 - presedensStatus code name (xs:string)
     */
    @Column(name = "precedence_status_code_name")
    @Audited
    private String precedenceStatusCodeName;

    // Link to user to (if referencePrecedenceApprovedBySystemID refer
    // to existing user)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = PRECEDENCE_APPROVED_BY_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private User referencePrecedenceApprovedBy;

    // Links to RegistryEntry
    @ManyToMany(mappedBy = "referencePrecedence")
    private final Set<RegistryEntry> referenceRegistryEntry = new HashSet<>();

    // Links to CaseFiles
    @ManyToMany(mappedBy = "referencePrecedence")
    private final Set<CaseFile> referenceCaseFile = new HashSet<>();

    @Override
    public OffsetDateTime getPrecedenceDate() {
        return precedenceDate;
    }

    @Override
    public void setPrecedenceDate(OffsetDateTime precedenceDate) {
        this.precedenceDate = precedenceDate;
    }

    @Override
    public String getPrecedenceAuthority() {
        return precedenceAuthority;
    }

    @Override
    public void setPrecedenceAuthority(String precedenceAuthority) {
        this.precedenceAuthority = precedenceAuthority;
    }

    @Override
    public String getSourceOfLaw() {
        return sourceOfLaw;
    }

    @Override
    public void setSourceOfLaw(String sourceOfLaw) {
        this.sourceOfLaw = sourceOfLaw;
    }

    @Override
    public OffsetDateTime getPrecedenceApprovedDate() {
        return precedenceApprovedDate;
    }

    @Override
    public void setPrecedenceApprovedDate(OffsetDateTime precedenceApprovedDate) {
        this.precedenceApprovedDate = precedenceApprovedDate;
    }

    @Override
    public String getPrecedenceApprovedBy() {
        return precedenceApprovedBy;
    }

    @Override
    public void setPrecedenceApprovedBy(String precedenceApprovedBy) {
        this.precedenceApprovedBy = precedenceApprovedBy;
    }

    @Override
    public UUID getReferencePrecedenceApprovedBySystemID() {
        return referencePrecedenceApprovedBySystemID;
    }

    @Override
    public void setReferencePrecedenceApprovedBySystemID
            (UUID referencePrecedenceApprovedBySystemID) {
        this.referencePrecedenceApprovedBySystemID =
                referencePrecedenceApprovedBySystemID;
    }

    @Override
    public User getReferencePrecedenceApprovedBy() {
        return referencePrecedenceApprovedBy;
    }

    @Override
    public void setReferencePrecedenceApprovedBy
            (User referencePrecedenceApprovedBy) {
        this.referencePrecedenceApprovedBy = referencePrecedenceApprovedBy;
    }

    @Override
    public PrecedenceStatus getPrecedenceStatus() {
        if (null == precedenceStatusCode)
            return null;
        return new PrecedenceStatus(precedenceStatusCode,
                precedenceStatusCodeName);
    }

    @Override
    public void setPrecedenceStatus(PrecedenceStatus precedenceStatus) {
        if (null != precedenceStatus) {
            this.precedenceStatusCode = precedenceStatus.getCode();
            this.precedenceStatusCodeName = precedenceStatus.getCodeName();
        } else {
            this.precedenceStatusCode = null;
            this.precedenceStatusCodeName = null;
        }
    }

    @Override
    public String getBaseTypeName() {
        return PRECEDENCE;
    }

    @Override
    public String getBaseRel() {
        return REL_CASE_HANDLING_PRECEDENCE;
    }

    @Override
    public String getFunctionalTypeName() {
        return Constants.NOARK_CASE_HANDLING_PATH;
    }

    @Override
    public Set<RegistryEntry> getReferenceRegistryEntry() {
        return referenceRegistryEntry;
    }

    @Override
    public void addRegistryEntry(RegistryEntry registryEntry) {
        this.referenceRegistryEntry.add(registryEntry);
        registryEntry.getReferencePrecedence().add(this);
    }

    public void removeRegistryEntry(RegistryEntry registryEntry) {
        this.referenceRegistryEntry.remove(registryEntry);
        registryEntry.getReferencePrecedence().remove(this);
    }

    @Override
    public Set<CaseFile> getReferenceCaseFile() {
        return referenceCaseFile;
    }

    @Override
    public void addCaseFile(CaseFile caseFile) {
        this.referenceCaseFile.add(caseFile);
        caseFile.getReferencePrecedence().add(this);
    }

    @Override
    public void removeCaseFile(CaseFile caseFile) {
        this.referenceCaseFile.remove(caseFile);
        caseFile.getReferencePrecedence().remove(this);
    }

    @Override
    public String toString() {
        return "Precedence{" + super.toString() +
                "precedenceStatusCode='" + precedenceStatusCode + '\'' +
                "precedenceStatusCodeName='" + precedenceStatusCodeName + '\'' +
                ", precedenceApprovedBy='" + precedenceApprovedBy + '\'' +
                ", precedenceApprovedDate=" + precedenceApprovedDate +
                ", sourceOfLaw='" + sourceOfLaw + '\'' +
                ", precedenceAuthority='" + precedenceAuthority + '\'' +
                ", precedenceDate='" + precedenceDate + '\'' +
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
        Precedence rhs = (Precedence) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(precedenceStatusCode, rhs.precedenceStatusCode)
                .append(precedenceStatusCodeName, rhs.precedenceStatusCodeName)
                .append(precedenceApprovedBy, rhs.precedenceApprovedBy)
                .append(precedenceApprovedDate, rhs.precedenceApprovedDate)
                .append(sourceOfLaw, rhs.sourceOfLaw)
                .append(precedenceAuthority, rhs.precedenceAuthority)
                .append(precedenceDate, rhs.precedenceDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(precedenceStatusCode)
                .append(precedenceStatusCodeName)
                .append(precedenceApprovedBy)
                .append(precedenceApprovedDate)
                .append(sourceOfLaw)
                .append(precedenceAuthority)
                .append(precedenceDate)
                .toHashCode();
    }
}
