package nikita.common.model.noark5.v4.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v4.NoarkEntity;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.model.noark5.v4.casehandling.SequenceNumberGenerator;
import nikita.common.model.noark5.v4.interfaces.entities.admin.IAdministrativeUnitEntity;
import nikita.common.util.deserialisers.admin.AdministrativeUnitDeserializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;

import static nikita.common.config.Constants.*;

@Entity
@Table(name = "nikita_administrative_unit")
@JsonDeserialize(using = AdministrativeUnitDeserializer.class)
@AttributeOverride(name = "id",
        column = @Column(name = PRIMARY_KEY_ADMINISTRATIVE_UNIT))
public class AdministrativeUnit
        extends NoarkEntity
        implements IAdministrativeUnitEntity {

    /**
     * M600 - opprettetDato (xs:dateTime)
     */
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Audited
    private Date createdDate;

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
    @Temporal(TemporalType.TIMESTAMP)
    @Audited
    private Date finalisedDate;

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

    @OneToOne(mappedBy = "administrativeUnit", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private SequenceNumberGenerator sequenceNumberGenerator;

    @ManyToMany
    @JoinTable(
            name = "administrative_unit_nikita_user",
            joinColumns = {
                    @JoinColumn(name = FOREIGN_KEY_ADMINISTRATIVE_UNIT_PK,
                            referencedColumnName = PRIMARY_KEY_ADMINISTRATIVE_UNIT)},
            inverseJoinColumns = {@JoinColumn(name = FOREIGN_KEY_USER_PK,
                    referencedColumnName = PRIMARY_KEY_USER)})
    private Set<User> users = new HashSet<>();

    // Links to CaseFile
    @OneToMany(mappedBy = "referenceAdministrativeUnit")
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
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Date createdDate) {
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
    public Date getFinalisedDate() {
        return finalisedDate;
    }

    @Override
    public void setFinalisedDate(Date finalisedDate) {
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

    public SequenceNumberGenerator getSequenceNumberGenerator() {
        return sequenceNumberGenerator;
    }

    public void setSequenceNumberGenerator(
            SequenceNumberGenerator sequenceNumberGenerator) {
        this.sequenceNumberGenerator = sequenceNumberGenerator;
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
