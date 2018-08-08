package nikita.common.model.noark5.v4.casehandling;

import nikita.common.config.Constants;

import javax.persistence.*;

/**
 * This is note a part of the Noark 5 data model, but a requirement in order
 * to provide seqenceNumbers
 */

@Entity
@Table(name = Constants.TABLE_CASE_FILE_SEQUENCE)
public class SequenceNumberGenerator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "administrative_unit")
    private String administrativeUnit;

    @Column(name = "year")
    private Integer year;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    public Integer incrementByOne() {
        return sequenceNumber++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdministrativeUnit() {
        return administrativeUnit;
    }

    public void setAdministrativeUnit(String administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
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

    @Override
    public String toString() {
        return "SequenceNumberGenerator{" +
                "id=" + id +
                ", administrativeUnit='" + administrativeUnit + '\'' +
                ", year=" + year +
                ", sequenceNumber=" + sequenceNumber +
                '}';
    }
}
