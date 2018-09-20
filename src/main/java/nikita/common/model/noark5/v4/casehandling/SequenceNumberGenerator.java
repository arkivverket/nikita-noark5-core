package nikita.common.model.noark5.v4.casehandling;

import nikita.common.config.Constants;
import nikita.common.model.noark5.v4.admin.AdministrativeUnit;

import javax.persistence.*;

/**
 * This is note a part of the Noark 5 data model, but a requirement in order
 * to provide sequenceNumbers. This is not part of the "official" data model
 */

@Entity
@Table(name = Constants.TABLE_CASE_FILE_SEQUENCE)
public class SequenceNumberGenerator {

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    AdministrativeUnit administrativeUnit;
    @Id
    @Column(name = "sequence_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;
    @Column(name = "administrative_unit_name")
    private String administrativeUnitName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer incrementByOne() {
        return sequenceNumber++;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return administrativeUnit;
    }

    public void setReferenceAdministrativeUnit(
            AdministrativeUnit administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
    }

    @Override
    public String toString() {
        return "SequenceNumberGenerator{" +
                "administrativeUnit='" + administrativeUnit + '\'' +
                ", year=" + year +
                ", sequenceNumber=" + sequenceNumber +
                '}';
    }
}
