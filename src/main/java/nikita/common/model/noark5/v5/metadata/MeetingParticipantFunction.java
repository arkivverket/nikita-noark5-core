package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_MEETING_PARTICIPANT_FUNCTION;
import static nikita.common.config.N5ResourceMappings.MEETING_PARTICIPANT_FUNCTION;

// Noark 5v5 Møtedeltakerfunksjon
@Entity
@Table(name = TABLE_MEETING_PARTICIPANT_FUNCTION)
public class MeetingParticipantFunction
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public MeetingParticipantFunction() {
    }

    public MeetingParticipantFunction(String code, String codename) {
        super(code, codename);
    }

    public MeetingParticipantFunction(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return MEETING_PARTICIPANT_FUNCTION;
    }
}
