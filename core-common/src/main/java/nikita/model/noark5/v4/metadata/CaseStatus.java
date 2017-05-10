package nikita.model.noark5.v4.metadata;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.config.N5ResourceMappings.CASE_STATUS;

// Noark 5v4 saksstatus
@Entity
@Table(name = "case_status")
// Enable soft delete
// @SQLDelete(sql = "UPDATE case_status SET deleted = true WHERE pk_case_status_id = ? and version = ?")
// @Where(clause = "deleted <> true")
@AttributeOverride(name = "id", column = @Column(name = "pk_case_status_id"))
public class CaseStatus extends MetadataSuperClass {
    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return CASE_STATUS;
    }
}
