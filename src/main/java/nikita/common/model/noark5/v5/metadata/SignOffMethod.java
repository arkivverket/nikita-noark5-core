package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_SIGN_OFF_METHOD;
import static nikita.common.config.Constants.TABLE_SIGN_OFF_METHOD;
import static nikita.common.config.N5ResourceMappings.SIGN_OFF_METHOD;

// Noark 5v5 Avskrivningsmaate
@Entity
@Table(name = TABLE_SIGN_OFF_METHOD)
public class SignOffMethod
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public SignOffMethod() {
    }

    public SignOffMethod(String code, String codename) {
        super(code, codename);
    }

    public SignOffMethod(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return SIGN_OFF_METHOD;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_SIGN_OFF_METHOD;
    }
}
