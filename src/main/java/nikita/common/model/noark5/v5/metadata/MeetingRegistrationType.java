package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_MEETING_REGISTRATION_TYPE;
import static nikita.common.config.N5ResourceMappings.MEETING_REGISTRATION_TYPE;

// Noark 5v5 Møteregistreringstype
@Entity
@Table(name = TABLE_MEETING_REGISTRATION_TYPE)
public class MeetingRegistrationType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public MeetingRegistrationType() {
    }

    public MeetingRegistrationType(String code, String codename) {
        super(code, codename);
    }

    public MeetingRegistrationType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return MEETING_REGISTRATION_TYPE;
    }
}
