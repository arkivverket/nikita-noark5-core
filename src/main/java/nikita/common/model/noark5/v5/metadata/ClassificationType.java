package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

// Noark 5v5 Klassifikasjonstype
@Entity
@Table(name = "classification_type")
// Enable soft delete
// @SQLDelete(sql = "UPDATE classification_type SET deleted = true WHERE pk_classification_type_id = ? and version = ?")
// @Where(clause = "deleted <> true")
@AttributeOverride(name = "id", column = @Column(name = "pk_classification_type_id"))
public class ClassificationType extends MetadataSuperClass {
    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.CLASSIFICATION_TYPE;
    }
}
