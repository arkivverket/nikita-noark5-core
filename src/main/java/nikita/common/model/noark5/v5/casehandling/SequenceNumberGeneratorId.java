package nikita.common.model.noark5.v5.casehandling;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

import static nikita.common.config.N5ResourceMappings.REFERENCE_ADMINISTRATIVE_UNIT_ENG;
import static nikita.common.config.N5ResourceMappings.YEAR_ENG;

@Embeddable
public class SequenceNumberGeneratorId
        implements Serializable {

    @ManyToOne
    @JoinColumn(name = REFERENCE_ADMINISTRATIVE_UNIT_ENG)
    private AdministrativeUnit referenceAdministrativeUnit;

    @Column(name = YEAR_ENG)
    private Integer year;

    public SequenceNumberGeneratorId() {
    }

    public SequenceNumberGeneratorId(
            Integer year, AdministrativeUnit referenceAdministrativeUnit) {
        this.year = year;
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return referenceAdministrativeUnit;
    }

    public void setReferenceAdministrativeUnit(
            AdministrativeUnit referenceAdministrativeUnit) {
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SequenceNumberGeneratorId)) return false;
        SequenceNumberGeneratorId that = (SequenceNumberGeneratorId) o;
        return Objects.equals(year, that.year) &&
                Objects.equals(referenceAdministrativeUnit,
                        that.referenceAdministrativeUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, referenceAdministrativeUnit);
    }
}
