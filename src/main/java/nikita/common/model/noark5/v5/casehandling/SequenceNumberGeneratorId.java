package nikita.common.model.noark5.v5.casehandling;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * SequenceNumberGeneratorId is used as a  @IdClass for
 * SequenceNumberGenerator. This is one way to handle the definition of a
 * composite key.
 */
@Embeddable
public class SequenceNumberGeneratorId
        implements Serializable {

    private Integer year;
    private Long referenceAdministrativeUnit;

    public SequenceNumberGeneratorId() {
    }

    public SequenceNumberGeneratorId(Integer year,
                                     Long referenceAdministrativeUnit) {
        this.year = year;
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getReferenceAdministrativeUnit() {
        return referenceAdministrativeUnit;
    }

    public void setReferenceAdministrativeUnit(Long referenceAdministrativeUnit) {
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
