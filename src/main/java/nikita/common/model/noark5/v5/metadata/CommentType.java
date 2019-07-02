package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_COMMENT_TYPE;
import static nikita.common.config.N5ResourceMappings.COMMENT_TYPE;

// Noark 5v5 Merknadstype
@Entity
@Table(name = TABLE_COMMENT_TYPE)
public class CommentType extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return COMMENT_TYPE;
    }
}
