package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_MEETING_PARTICIPANT_FUNCTION;
import static nikita.common.config.N5ResourceMappings.MEETING_PARTICIPANT_FUNCTION;

// Noark 5v5 Møtedeltakerfunksjon
@Entity
@Table(name = TABLE_MEETING_PARTICIPANT_FUNCTION)
public class MeetingParticipantFunction
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return MEETING_PARTICIPANT_FUNCTION;
    }
}
