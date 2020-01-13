package nikita.common.model.noark5.v5.nationalidentifier;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;

@Entity
@Table(name = TABLE_NATIONAL_IDENTIFIER)
@Inheritance(strategy = JOINED)
//@JsonDeserialize(using = NationalIdentifierDeserializer.class)
//@HateoasPacker(using = NationalIdentifierHateoasHandler.class)
//@HateoasObject(using = NationalIdentifierHateoas.class)
@Audited
public class NationalIdentifier
        extends SystemIdEntity {

    @ManyToOne
    @JoinColumn(name = NATIONAL_IDENTIFIER_FILE_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private File referenceFile;

    @ManyToOne
    @JoinColumn(name = NATIONAL_IDENTIFIER_RECORD_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Record referenceRecord;

    public File getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(File referenceFile) {
        this.referenceFile = referenceFile;
    }

    public Record getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(Record referenceRecord) {
        this.referenceRecord = referenceRecord;
    }
}
