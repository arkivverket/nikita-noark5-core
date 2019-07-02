package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_MEETING_REGISTRATION_TYPE;
import static nikita.common.config.N5ResourceMappings.MEETING_REGISTRATION_TYPE;

// Noark 5v5 Møteregistreringstype
@Entity
@Table(name = TABLE_MEETING_REGISTRATION_TYPE)
public class MeetingRegistrationType
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return MEETING_REGISTRATION_TYPE;
    }
}