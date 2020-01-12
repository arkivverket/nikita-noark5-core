package nikita.common.model.noark5.v5.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.SequenceNumberGenerator;
import nikita.common.model.noark5.v5.hateoas.admin.AdministrativeUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.admin.IAdministrativeUnitEntity;
import nikita.common.util.deserialisers.admin.AdministrativeUnitDeserializer;
import nikita.webapp.hateoas.admin.AdministrativeUnitHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_ADMINISTRATIVE_UNIT)
@JsonDeserialize(using = AdministrativeUnitDeserializer.class)
@HateoasPacker(using = AdministrativeUnitHateoasHandler.class)
@HateoasObject(using = AdministrativeUnitHateoas.class)
public class AdministrativeUnit
        extends SystemIdEntity
        implements IAdministrativeUnitEntity {

    /**
     * M602 - avsluttetDato (xs:dateTime)
     */
    @Column(name = FINALISED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(FINALISED_DATE)
    private OffsetDateTime finalisedDate;

    /**
     * M603 - avsluttetAv (xs:string)
     */
    @Column(name = FINALISED_BY_ENG)
    @Audited
    @JsonProperty(FINALISED_BY)
    private String finalisedBy;

    /**
     * M583 - kortnavn (xs:string)
     */
    @Column(name = SHORT_NAME_ENG)
    @Audited
    @JsonProperty(SHORT_NAME)
    private String shortName;

    /**
     * M583 - administrativEnhetNavn (xs:string)
     */
    @Column(name = "administrative_unit_name")
    @Audited
    private String administrativeUnitName;

    /**
     * M584 administrativEnhetsstatus (xs:string)
     */
    @Column(name = "administrative_unit_status")
    @Audited
    private String administrativeUnitStatus;

    // Used identify as a default
    @Column(name = "default_administrative_unit")
    @Audited
    private Boolean defaultAdministrativeUnit;

    // Links to SequenceNumberGenerator
    @OneToMany(mappedBy = "referenceAdministrativeUnit",
            cascade = ALL, orphanRemoval = true)
    private Set <SequenceNumberGenerator>
            referenceSequenceNumberGenerator = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = TABLE_ADMINISTRATIVE_UNIT_USER,
            joinColumns = {
                    @JoinColumn(
                            name = FOREIGN_KEY_ADMINISTRATIVE_UNIT_PK,
                            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)},
            inverseJoinColumns = {
                    @JoinColumn(name = FOREIGN_KEY_USER_PK,
                            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)})
    private Set<User> users = new HashSet<>();

    // Links to CaseFile
    @OneToMany(mappedBy = "referenceAdministrativeUnit", cascade = ALL)
    @JsonIgnore
    private List<CaseFile> referenceCaseFile = new ArrayList<>();

    /**
     * M585 referanseOverordnetEnhet (xs:string)
     */
    // Link to parent AdministrativeUnit
    @ManyToOne(fetch = FetchType.LAZY)
    private AdministrativeUnit referenceParentAdministrativeUnit;

    // Links to child AdministrativeUnit
    @OneToMany(mappedBy = "referenceParentAdministrativeUnit",
            fetch = FetchType.LAZY)
    private List<AdministrativeUnit> referenceChildAdministrativeUnit =
            new ArrayList<>();

    @Override
    public OffsetDateTime getFinalisedDate() {
        return finalisedDate;
    }

    @Override
    public void setFinalisedDate(OffsetDateTime finalisedDate) {
        this.finalisedDate = finalisedDate;
    }

    @Override
    public String getFinalisedBy() {
        return finalisedBy;
    }

    @Override
    public void setFinalisedBy(String finalisedBy) {
        this.finalisedBy = finalisedBy;
    }

    public List<CaseFile> getReferenceCaseFile() {
        return referenceCaseFile;
    }

    public void setReferenceCaseFile(List<CaseFile> referenceCaseFile) {
        this.referenceCaseFile = referenceCaseFile;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAdministrativeUnitName() {
        return administrativeUnitName;
    }

    public void setAdministrativeUnitName(String administrativeUnitName) {
        this.administrativeUnitName = administrativeUnitName;
    }

    public Set<SequenceNumberGenerator> getReferenceSequenceNumberGenerator() {
        return referenceSequenceNumberGenerator;
    }

    public void setReferenceSequenceNumberGenerator(
            Set<SequenceNumberGenerator> referenceSequenceNumberGenerator) {
        this.referenceSequenceNumberGenerator = referenceSequenceNumberGenerator;
    }

    public void addReferenceSequenceNumberGenerator(
            SequenceNumberGenerator sequenceNumberGenerator) {
        this.referenceSequenceNumberGenerator.add(sequenceNumberGenerator);
    }

    public String getAdministrativeUnitStatus() {
        return administrativeUnitStatus;
    }

    public void setAdministrativeUnitStatus(String administrativeUnitStatus) {
        this.administrativeUnitStatus = administrativeUnitStatus;
    }

    public AdministrativeUnit getParentAdministrativeUnit() {
        return referenceParentAdministrativeUnit;
    }

    public void setParentAdministrativeUnit(
            AdministrativeUnit referenceParentAdministrativeUnit) {
        this.referenceParentAdministrativeUnit =
                referenceParentAdministrativeUnit;
    }

    public Boolean getDefaultAdministrativeUnit() {
        return defaultAdministrativeUnit;
    }

    public void setDefaultAdministrativeUnit(
            Boolean defaultAdministrativeUnit) {
        this.defaultAdministrativeUnit = defaultAdministrativeUnit;
    }

    public List<AdministrativeUnit> getReferenceChildAdministrativeUnit() {
        return referenceChildAdministrativeUnit;
    }

    public void setReferenceChildAdministrativeUnit(
            List<AdministrativeUnit> referenceChildAdministrativeUnit) {
        this.referenceChildAdministrativeUnit = referenceChildAdministrativeUnit;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public AdministrativeUnit getReferenceParentAdministrativeUnit() {
        return referenceParentAdministrativeUnit;
    }

    public void setReferenceParentAdministrativeUnit(
            AdministrativeUnit referenceParentAdministrativeUnit) {
        this.referenceParentAdministrativeUnit =
                referenceParentAdministrativeUnit;
    }

    @Override
    public String getBaseTypeName() {
        return ADMINISTRATIVE_UNIT;
    }

    @Override
    public String getBaseRel() {
        return REL_ADMIN_ADMINISTRATIVE_UNIT;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_ADMINISTRATION_PATH;
    }

    @Override
    public String toString() {
        return "AdministrativeUnit{" + super.toString() +
                ", finalisedDate=" + finalisedDate +
                ", finalisedBy='" + finalisedBy + '\'' +
                ", shortName='" + shortName + '\'' +
                ", administrativeUnitName='" + administrativeUnitName + '\'' +
                ", administrativeUnitStatus='" + administrativeUnitStatus + '\'' +
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
        AdministrativeUnit rhs = (AdministrativeUnit) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(finalisedDate, rhs.finalisedDate)
                .append(finalisedBy, rhs.finalisedBy)
                .append(shortName, rhs.shortName)
                .append(administrativeUnitName, rhs.administrativeUnitName)
                .append(administrativeUnitStatus, rhs.administrativeUnitStatus)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(finalisedDate)
                .append(finalisedBy)
                .append(shortName)
                .append(administrativeUnitName)
                .append(administrativeUnitStatus)
                .toHashCode();
    }
}
