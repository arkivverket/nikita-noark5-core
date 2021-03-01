package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static nikita.common.config.Constants.TABLE_MEETING_REGISTRATION_STATUS;
import static nikita.common.config.N5ResourceMappings.MEETING_REGISTRATION_STATUS;

// Noark 5v5 Møteregistreringsstatus
@Entity
@Inheritance(strategy = SINGLE_TABLE)
@Table(name = TABLE_MEETING_REGISTRATION_STATUS)
public class MeetingRegistrationStatus
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public MeetingRegistrationStatus() {
    }

    public MeetingRegistrationStatus(String code, String codename) {
        super(code, codename);
    }

    public MeetingRegistrationStatus(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return MEETING_REGISTRATION_STATUS;
    }
}
