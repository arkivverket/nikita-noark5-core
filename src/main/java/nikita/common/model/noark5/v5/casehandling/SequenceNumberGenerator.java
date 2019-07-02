package nikita.common.model.noark5.v5.casehandling;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;

/**
 * A sequence number generator is required to automatically fill in values for
 * mappeId/fileId in nikita. A sequence normally takes the following form
 * <p>
 * 2019/0001 where 2019 is year and 0001 is the sequence a file object is
 * created per year.
 * <p>
 * This is not part of the official Noark 5 data model, but a requirement in
 * order to provide sequenceNumbers. It needs to be associated with the Noark
 * 5 domain model to work.
 */
@Entity
@Table(name = TABLE_CASE_FILE_SEQUENCE)
@IdClass(SequenceNumberGeneratorId.class)
public class SequenceNumberGenerator
        implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = FOREIGN_KEY_ADMINISTRATIVE_UNIT_PK,
            insertable = false, updatable = false)
    private AdministrativeUnit referenceAdministrativeUnit;

    @Id
    @Column(name = "year")
    private Integer year;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @Column(name = "administrative_unit_name")
    private String administrativeUnitName;

    public SequenceNumberGenerator() {
    }

    public SequenceNumberGenerator(
            Integer year,
            AdministrativeUnit referenceAdministrativeUnit) {
        this.year = year;
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return referenceAdministrativeUnit;
    }

    public void setReferenceAdministrativeUnit(
            AdministrativeUnit referenceAdministrativeUnit) {
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer incrementByOne() {
        return sequenceNumber++;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getAdministrativeUnitName() {
        return administrativeUnitName;
    }

    public void setAdministrativeUnitName(String administrativeUnitName) {
        this.administrativeUnitName = administrativeUnitName;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof SequenceNumberGenerator)) return false;
        SequenceNumberGenerator that = (SequenceNumberGenerator) o;
        return Objects.equals(year, that.year) &&
                Objects.equals(referenceAdministrativeUnit,
                        that.referenceAdministrativeUnit) &&
                Objects.equals(sequenceNumber, that.sequenceNumber) &&
                Objects.equals(administrativeUnitName,
                        that.administrativeUnitName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, referenceAdministrativeUnit,
                sequenceNumber, administrativeUnitName);
    }

    @Override
    public String toString() {
        return "SequenceNumberGenerator{" +
                ", sequenceNumber=" + sequenceNumber +
                '}';
    }
}
