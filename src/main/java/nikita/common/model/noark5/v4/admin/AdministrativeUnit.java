package nikita.common.model.noark5.v4.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v4.NoarkEntity;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.model.noark5.v4.casehandling.SequenceNumberGenerator;
import nikita.common.model.noark5.v4.hateoas.admin.AdministrativeUnitHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.admin.IAdministrativeUnitEntity;
import nikita.common.util.deserialisers.admin.AdministrativeUnitDeserializer;
import nikita.webapp.handlers.hateoas.admin.AdministrativeUnitHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nikita.common.config.Constants.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_NIKITA_ADMINISTRATIVE_UNIT)
@JsonDeserialize(using = AdministrativeUnitDeserializer.class)
@HateoasPacker(using = AdministrativeUnitHateoasHandler.class)
@HateoasObject(using = AdministrativeUnitHateoas.class)
@AttributeOverride(name = "id",
        column = @Column(name = PRIMARY_KEY_ADMINISTRATIVE_UNIT))
public class AdministrativeUnit
        extends NoarkEntity
        implements IAdministrativeUnitEntity {

    /**
     * M600 - opprettetDato (xs:dateTime)
     */
    @Column(name = "created_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private ZonedDateTime createdDate;

    /**
     * M601 - opprettetAv (xs:string)
     */
    @Column(name = "created_by")
    @Audited
    private String createdBy;

    /**
     * M602 - avsluttetDato (xs:dateTime)
     */
    @Column(name = "finalised_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private ZonedDateTime finalisedDate;

    /**
     * M603 - avsluttetAv (xs:string)
     */
    @Column(name = "finalised_by")
    @Audited
    private String finalisedBy;

    /**
     * M583 - kortnavn (xs:string)
     */
    @Column(name = "short_name")
    @Audited
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
    @OneToMany(mappedBy = REFERENCE_ADMINISTRATIVE_UNIT,
            cascade = CascadeType.ALL)
    private List<SequenceNumberGenerator>
            referenceSequenceNumberGenerator = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = TABLE_ADMINISTRATIVE_UNIT_JOIN_NIKITA_USER,
            joinColumns = {
                    @JoinColumn(name = FOREIGN_KEY_ADMINISTRATIVE_UNIT_PK,
                            referencedColumnName =
                                    PRIMARY_KEY_ADMINISTRATIVE_UNIT)},
            inverseJoinColumns = {@JoinColumn(name = FOREIGN_KEY_USER_PK,
                    referencedColumnName = PRIMARY_KEY_USER)})
    private Set<User> users = new HashSet<>();

    // Links to CaseFile
    @OneToMany(mappedBy = REFERENCE_ADMINISTRATIVE_UNIT)
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
    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public ZonedDateTime getFinalisedDate() {
        return finalisedDate;
    }

    @Override
    public void setFinalisedDate(ZonedDateTime finalisedDate) {
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

    public List<SequenceNumberGenerator> getReferenceSequenceNumberGenerator() {
        return referenceSequenceNumberGenerator;
    }

    public void setReferenceSequenceNumberGenerator(
            List<SequenceNumberGenerator> referenceSequenceNumberGenerator) {
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
    public String toString() {
        return "AdministrativeUnit{" + super.toString() +
                "createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
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
                .append(createdDate, rhs.createdDate)
                .append(createdBy, rhs.createdBy)
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
                .append(createdDate)
                .append(createdBy)
                .append(finalisedDate)
                .append(finalisedBy)
                .append(shortName)
                .append(administrativeUnitName)
                .append(administrativeUnitStatus)
                .toHashCode();
    }
}
