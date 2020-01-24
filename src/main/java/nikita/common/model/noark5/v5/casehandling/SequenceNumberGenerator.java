package nikita.common.model.noark5.v5.casehandling;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

import static nikita.common.config.Constants.FOREIGN_KEY_ADMINISTRATIVE_UNIT_PK;
import static nikita.common.config.Constants.TABLE_CASE_FILE_SEQUENCE;

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

    @Column(name = "case_file_sequence_number")
    private Integer sequenceNumber;

    @Column(name = "record_sequence_number")
    private Integer recordSequenceNumber;

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

    public Integer incrementCaseFileByOne() {
        return sequenceNumber++;
    }

    public Integer incrementRecordByOne() {
        return recordSequenceNumber++;
    }

    public Integer getCaseFileSequenceNumber() {
        return sequenceNumber;
    }

    public void setCaseFileSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getRecordSequenceNumber() {
        return recordSequenceNumber;
    }

    public void setRecordSequenceNumber(Integer recordSequenceNumber) {
        this.recordSequenceNumber = recordSequenceNumber;
    }

    public String getAdministrativeUnitName() {
        return administrativeUnitName;
    }

    public void setAdministrativeUnitName(String administrativeUnitName) {
        this.administrativeUnitName = administrativeUnitName;
    }

    @Override
    public String toString() {
        return "SequenceNumberGenerator{" +
                ", sequenceNumber=" + sequenceNumber +
                ", administrativeUnitName=" + administrativeUnitName +
                '}';
    }

    @Override
    public boolean equals (Object other) {
        if (other == null) return false;
        if (this == other) return true;
        if (!(other instanceof SequenceNumberGenerator)) return false;
        SequenceNumberGenerator rhs = (SequenceNumberGenerator) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(year, rhs.year)
                .append(sequenceNumber, rhs.sequenceNumber)
                .append(recordSequenceNumber, rhs.recordSequenceNumber)
	    .append(administrativeUnitName, rhs.administrativeUnitName)
	    .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(year)
                .append(sequenceNumber)
                .append(recordSequenceNumber)
	    .append(administrativeUnitName)
	    .toHashCode();
    }
}
