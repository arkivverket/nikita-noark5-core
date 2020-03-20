package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_MEETING_FILE_TYPE;
import static nikita.common.config.N5ResourceMappings.MEETING_FILE_TYPE;

// Noark 5v5 Møtesakstype
@Entity
@Table(name = TABLE_MEETING_FILE_TYPE)
public class MeetingFileType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public MeetingFileType() {
    }

    public MeetingFileType(String code, String codename) {
        super(code, codename);
    }

    public MeetingFileType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return MEETING_FILE_TYPE;
    }
}
