package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_SIGN_OFF;
import static nikita.common.config.Constants.TABLE_SIGN_OFF;
import static nikita.common.config.N5ResourceMappings.SIGN_OFF;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;


@Entity
@Table(name = TABLE_SIGN_OFF)
public class SignOff
        extends NoarkEntity {

    /**
     * M617 - avskrivningsdato
     */
    @Column(name = "sign_off_date")
    @DateTimeFormat(iso = DATE)

    @Audited
    private OffsetDateTime signOffDate;

    /**
     * M618 - avskrevetAv
     */
    @Column(name = "sign_off_name")
    @Audited
    private String signOffBy;

    /**
     * M619 - avskrivningsmaate
     */
    @Column(name = "sign_off_method")
    @Audited
    private String signOffMethod;

    /**
     * M215 referanseAvskrivesAvJournalpost
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_record_id")

    private RegistryEntry referenceSignedOffRecord;
    /**
     * M??? - referanseAvskrivesAvKorrespondansepart
     * Note this is new to v5, I think. Missing Metatdata number
     */

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pk_correspondence_part_id")
    private CorrespondencePart referenceSignedOffCorrespondencePart;

    // Links to RegistryEnty
    @ManyToMany(mappedBy = "referenceSignOff")
    private List<RegistryEntry> referenceRecord = new ArrayList<>();

    public OffsetDateTime getSignOffDate() {
        return signOffDate;
    }

    public void setSignOffDate(OffsetDateTime signOffDate) {
        this.signOffDate = signOffDate;
    }

    public String getSignOffBy() {
        return signOffBy;
    }

    public void setSignOffBy(String signOffBy) {
        this.signOffBy = signOffBy;
    }

    public String getSignOffMethod() {
        return signOffMethod;
    }

    public void setSignOffMethod(String signOffMethod) {
        this.signOffMethod = signOffMethod;
    }

    @Override
    public String getBaseTypeName() {
        return SIGN_OFF;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_SIGN_OFF;
    }

    public RegistryEntry getReferenceSignedOffRecord() {
        return referenceSignedOffRecord;
    }

    public void setReferenceSignedOffRecord(
            RegistryEntry referenceSignedOffRecord) {
        this.referenceSignedOffRecord = referenceSignedOffRecord;
    }

    public CorrespondencePart getReferenceSignedOffCorrespondencePart() {
        return referenceSignedOffCorrespondencePart;
    }

    public void setReferenceSignedOffCorrespondencePart(
            CorrespondencePart referenceSignedOffCorrespondencePart) {
        this.referenceSignedOffCorrespondencePart =
                referenceSignedOffCorrespondencePart;
    }

    public List<RegistryEntry> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<RegistryEntry> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    @Override
    public String toString() {
        return "SignOff{" + super.toString() +
                "signOffMethod='" + signOffMethod + '\'' +
                ", signOffBy='" + signOffBy + '\'' +
                ", signOffDate=" + signOffDate +
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
        SignOff rhs = (SignOff) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(signOffMethod, rhs.signOffMethod)
                .append(signOffBy, rhs.signOffBy)
                .append(signOffDate, rhs.signOffDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(signOffMethod)
                .append(signOffBy)
                .append(signOffDate)
                .toHashCode();
    }
}
