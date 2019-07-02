package nikita.common.model.noark5.v5.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nikita.common.config.Constants.TABLE_CASE_STATUS;
import static nikita.common.config.N5ResourceMappings.CASE_STATUS;

// Noark 5v5 saksstatus
@Entity
@Table(name = TABLE_CASE_STATUS)
public class CaseStatus
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    // Used identify as a default
    @Column(name = "case_status")
    @Audited
    private Boolean caseStatus;

    // Links to CaseFile
    @OneToMany(mappedBy = "referenceCaseFileStatus")
    @JsonIgnore
    private List<CaseFile> referenceCaseFile = new ArrayList<>();

    @Override
    public String getBaseTypeName() {
        return CASE_STATUS;
    }

    public Boolean getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Boolean caseStatus) {
        this.caseStatus = caseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseStatus)) return false;
        if (!super.equals(o)) return false;
        CaseStatus that = (CaseStatus) o;
        return Objects.equals(caseStatus, that.caseStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), caseStatus);
    }
}
