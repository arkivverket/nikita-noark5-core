package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_FILE_TYPE;
import static nikita.common.config.Constants.TABLE_FILE_TYPE;
import static nikita.common.config.N5ResourceMappings.FILE_TYPE;

// Noark 5v5 mappetype
@Entity
@Table(name = TABLE_FILE_TYPE)
public class FileType
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return FILE_TYPE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_FILE_TYPE;
    }
}
