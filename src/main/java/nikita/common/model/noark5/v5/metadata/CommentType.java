package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_COMMENT_TYPE;
import static nikita.common.config.Constants.TABLE_COMMENT_TYPE;
import static nikita.common.config.N5ResourceMappings.COMMENT_TYPE;

// Noark 5v5 Merknadstype
@Entity
@Table(name = TABLE_COMMENT_TYPE)
public class CommentType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public CommentType() {
    }

    public CommentType(String code, String codename) {
        super(code, codename);
    }

    public CommentType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return COMMENT_TYPE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_COMMENT_TYPE;
    }
}
