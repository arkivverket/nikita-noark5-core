package nikita.common.model.noark5.v5.casehandling.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICorrespondencePartInternalEntity;
import nikita.common.util.deserialisers.casehandling.CorrespondencePartInternalDeserializer;
import nikita.webapp.hateoas.casehandling.CorrespondencePartInternalHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_CORRESPONDENCE_PART_INTERNAL;
import static nikita.common.config.Constants.TABLE_CORRESPONDENCE_PART_INTERNAL;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_INTERNAL;
import static nikita.common.config.N5ResourceMappings.ADMINISTRATIVE_UNIT_FIELD_ENG;

@Entity
@Table(name = TABLE_CORRESPONDENCE_PART_INTERNAL)
@JsonDeserialize(using = CorrespondencePartInternalDeserializer.class)
@HateoasPacker(using = CorrespondencePartInternalHateoasHandler.class)
@HateoasObject(using = CorrespondencePartInternalHateoas.class)
public class CorrespondencePartInternal
        extends CorrespondencePart
        implements ICorrespondencePartInternalEntity {

    /**
     * M305 - administrativEnhet (xs:string)
     */
    @Column(name = ADMINISTRATIVE_UNIT_FIELD_ENG)
    @Audited
    private String administrativeUnit;

    /**
     * M307 - saksbehandler (xs:string)
     */
    @Column(name = "case_handler")
    @Audited
    private String caseHandler;

    @ManyToOne
    private AdministrativeUnit referenceAdministrativeUnit;

    @ManyToOne
    private User user;

    public String getAdministrativeUnit() {
        return administrativeUnit;
    }

    public void setAdministrativeUnit(String administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
    }

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return referenceAdministrativeUnit;
    }

    public void setReferenceAdministrativeUnit(
            AdministrativeUnit referenceAdministrativeUnit) {
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    public String getCaseHandler() {
        return caseHandler;
    }

    public void setCaseHandler(String caseHandler) {
        this.caseHandler = caseHandler;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getBaseTypeName() {
        return CORRESPONDENCE_PART_INTERNAL;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_CORRESPONDENCE_PART_INTERNAL;
    }

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
