package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_ELECTRONIC_SIGNATURE_VERIFIED;
import static nikita.common.config.N5ResourceMappings.ELECTRONIC_SIGNATURE_VERIFIED;

// Noark 5v5 elektroniskSignaturVerifisert
@Entity
@Table(name = TABLE_ELECTRONIC_SIGNATURE_VERIFIED)
public class ElectronicSignatureVerified
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return ELECTRONIC_SIGNATURE_VERIFIED;
    }
}
