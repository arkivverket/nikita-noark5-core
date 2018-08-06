package nikita.common.model.noark5.v4.casehandling.secondary;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v4.interfaces.entities.casehandling.ICorrespondencePartInternalEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "correspondence_part_internal")
//@JsonDeserialize(using = CorrespondencePartPersonInternal.class)
@AttributeOverride(name = "id",
        column = @Column(name = "pk_correspondence_part_internal_id"))
public class CorrespondencePartInternal
        extends CorrespondencePart
        implements ICorrespondencePartInternalEntity {


    /**
     * M305 - administrativEnhet (xs:string)
     */
    @Column(name = "administrative_unit")
    @Audited
    private String administrativeUnit;


    //@ManyToOne
    //private AdministrativeUnit referenceAdministrativeUnit;

    /**
     * M307 - saksbehandler (xs:string)
     */
    @Column(name = "case_handler")
    @Audited
    private String caseHandler;


    /*
  TODO: Temp disabled!
    // Links to RegistryEntry
    @ManyToMany(mappedBy = "referenceCorrespondencePartInternal")
    private List<RegistryEntry> referenceRegistryEntry = new ArrayList<>();
*/
    public String getAdministrativeUnit() {
        return administrativeUnit;
    }

    public void setAdministrativeUnit(String administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
    }
//
//    public AdministrativeUnit getReferenceAdministrativeUnit() {
//        return referenceAdministrativeUnit;
//    }
//
//    public void setReferenceAdministrativeUnit(AdministrativeUnit referenceAdministrativeUnit) {
//        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
//    }

    public String getCaseHandler() {
        return caseHandler;
    }

    public void setCaseHandler(String caseHandler) {
        this.caseHandler = caseHandler;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.CORRESPONDENCE_PART_UNIT;
    }

    /*
      TODO: Temp disabled!
        @Override
        public List<RegistryEntry> getReferenceRegistryEntry() {
            return referenceRegistryEntry;
        }

        @Override
        public void setReferenceRegistryEntry(List<RegistryEntry> referenceRegistryEntry) {
            this.referenceRegistryEntry = referenceRegistryEntry;
        }
    */
    @Override
    public String toString() {
        return "CorrespondencePartInternal{" + super.toString() +
                "administrativeUnit='" + administrativeUnit + '\'' +
                ", caseHandler='" + caseHandler + '\'' +
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
        CorrespondencePartInternal rhs = (CorrespondencePartInternal) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(administrativeUnit, rhs.administrativeUnit)
                .append(caseHandler, rhs.caseHandler)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(administrativeUnit)
                .append(caseHandler)
                .toHashCode();
    }
}
