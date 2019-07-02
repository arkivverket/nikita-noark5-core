package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_SIGN_OFF_METHOD;
import static nikita.common.config.N5ResourceMappings.SIGN_OFF_METHOD;

// Noark 5v5 Avskrivningsmaate
@Entity
@Table(name = TABLE_SIGN_OFF_METHOD)
public class SignOffMethod
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return SIGN_OFF_METHOD;
    }
}
