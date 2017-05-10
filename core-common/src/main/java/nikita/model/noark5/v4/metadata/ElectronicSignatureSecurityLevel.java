package nikita.model.noark5.v4.metadata;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.config.N5ResourceMappings.ELECTRONIC_SIGNATURE_SECURITY_LEVEL;

// Noark 5v4 elektroniskSignaturSikkerhetsnivå
@Entity
@Table(name = "electronic_signature_security_level")
// Enable soft delete
// @SQLDelete(sql = "UPDATE electronic_signature_security_level SET deleted = true WHERE pk_electronic_signature_security_level_id  = ? and version = ?")
// @Where(clause = "deleted <> true")
@AttributeOverride(name = "id", column = @Column(name = "pk_electronic_signature_security_level_id"))
public class ElectronicSignatureSecurityLevel extends MetadataSuperClass {
    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return ELECTRONIC_SIGNATURE_SECURITY_LEVEL;
    }
}
