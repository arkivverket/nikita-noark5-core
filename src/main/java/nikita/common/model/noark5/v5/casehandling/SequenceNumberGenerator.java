package nikita.common.model.noark5.v5.casehandling;

import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

import static nikita.common.config.Constants.TABLE_CASE_FILE_SEQUENCE;
import static nikita.common.config.N5ResourceMappings.*;

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
public class SequenceNumberGenerator
        implements Serializable {

    @EmbeddedId
    SequenceNumberGeneratorId sequenceNumberGeneratorId;

    @Column(name = CASE_FILE_SEQUENCE_NUMBER)
    private Integer sequenceNumber;

    @Column(name = RECORD_SEQUENCE_NUMBER)
    private Integer recordSequenceNumber;

    @Column(name = ADMINISTRATIVE_UNIT_NAME_ENG)
    @JsonProperty(ADMINISTRATIVE_UNIT_NAME)
    private String administrativeUnitName;

    public SequenceNumberGenerator() {
    }

    public SequenceNumberGenerator(
            SequenceNumberGeneratorId sequenceNumberGeneratorId) {
        this.sequenceNumberGeneratorId = sequenceNumberGeneratorId;
    }

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return sequenceNumberGeneratorId.getReferenceAdministrativeUnit();
    }

    public void setReferenceAdministrativeUnit(
            AdministrativeUnit referenceAdministrativeUnit) {
        this.sequenceNumberGeneratorId.setReferenceAdministrativeUnit(
                referenceAdministrativeUnit);
    }

    public Integer getYear() {
        return sequenceNumberGeneratorId.getYear();
    }

    public void setYear(Integer year) {
        sequenceNumberGeneratorId.setYear(year);
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
                .append(sequenceNumberGeneratorId.getYear(),
                        rhs.sequenceNumberGeneratorId.getYear())
                .append(sequenceNumberGeneratorId
                                .getReferenceAdministrativeUnit().getSystemId(),
                        rhs.getReferenceAdministrativeUnit().getSystemId())
                .append(sequenceNumber, rhs.sequenceNumber)
                .append(recordSequenceNumber, rhs.recordSequenceNumber)
	    .append(administrativeUnitName, rhs.administrativeUnitName)
	    .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(getReferenceAdministrativeUnit().getSystemId())
                .append(getYear())
                .append(sequenceNumber)
                .append(recordSequenceNumber)
	    .append(administrativeUnitName)
	    .toHashCode();
    }
}
