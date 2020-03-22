package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_ELECTRONIC_SIGNATURE_SECURITY_LEVEL;
import static nikita.common.config.Constants.TABLE_ELECTRONIC_SIGNATURE_SECURITY_LEVEL;
import static nikita.common.config.N5ResourceMappings.ELECTRONIC_SIGNATURE_SECURITY_LEVEL;

// Noark 5v5 elektroniskSignaturSikkerhetsnivå
@Entity
@Table(name = TABLE_ELECTRONIC_SIGNATURE_SECURITY_LEVEL)
public class ElectronicSignatureSecurityLevel
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public ElectronicSignatureSecurityLevel() {
    }

    public ElectronicSignatureSecurityLevel(String code, String codename) {
        super(code, codename);
    }

    public ElectronicSignatureSecurityLevel(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return ELECTRONIC_SIGNATURE_SECURITY_LEVEL;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_ELECTRONIC_SIGNATURE_SECURITY_LEVEL;
    }
}
