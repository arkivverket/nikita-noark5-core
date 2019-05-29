package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

// Noark 5v5 format
@Entity
@Table(name = "format")
// Enable soft delete
// @SQLDelete(sql = "UPDATE format SET deleted = true WHERE pk_format_id = ? and version = ?")
// @Where(clause = "deleted <> true")
@AttributeOverride(name = "id", column = @Column(name = "pk_format_id"))
public class Format extends MetadataSuperClass {
    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.FORMAT;
    }
}
