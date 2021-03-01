package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static nikita.common.config.Constants.REL_METADATA_FILE_TYPE;
import static nikita.common.config.Constants.TABLE_FILE_TYPE;
import static nikita.common.config.N5ResourceMappings.FILE_TYPE;

// Noark 5v5 mappetype
@Entity
@Inheritance(strategy = SINGLE_TABLE)
@Table(name = TABLE_FILE_TYPE)
public class FileType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public FileType() {
    }

    public FileType(String code, String codename) {
        super(code, codename);
    }

    public FileType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return FILE_TYPE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_FILE_TYPE;
    }
}
