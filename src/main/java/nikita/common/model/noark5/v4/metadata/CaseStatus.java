package nikita.common.model.noark5.v4.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.PRIMARY_KEY_CASE_FILE_STATUS;

// Noark 5v4 saksstatus
@Entity
@Table(name = "case_status")
// Enable soft delete
// @SQLDelete(sql = "UPDATE case_status SET deleted = true WHERE pk_case_status_id = ? and version = ?")
// @Where(clause = "deleted <> true")
@AttributeOverride(name = "id",
        column = @Column(name = PRIMARY_KEY_CASE_FILE_STATUS))
public class CaseStatus extends MetadataSuperClass {
    private static final long serialVersionUID = 1L;

    // Used identify as a default
    @Column(name = "default_case_status")
    @Audited
    private Boolean defaultCaseStatus;

    // Links to CaseFile
    @OneToMany(mappedBy = "referenceCaseFileStatus")
    @JsonIgnore
    private List<CaseFile> referenceCaseFile = new ArrayList<>();

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.CASE_STATUS;
    }

    public Boolean getDefaultCaseStatus() {
        return defaultCaseStatus;
    }

    public void setDefaultCaseStatus(Boolean defaultCaseStatus) {
        this.defaultCaseStatus = defaultCaseStatus;
    }
}
