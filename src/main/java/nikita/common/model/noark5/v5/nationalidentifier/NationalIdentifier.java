package nikita.common.model.noark5.v5.nationalidentifier;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.NationalIdentifierHateoas;
import nikita.webapp.hateoas.nationalidentifier.NationalIdentifierHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;

@Entity
@Table(name = TABLE_NATIONAL_IDENTIFIER)
@Inheritance(strategy = JOINED)
@HateoasPacker(using = NationalIdentifierHateoasHandler.class)
@HateoasObject(using = NationalIdentifierHateoas.class)
@Audited
@Indexed
public class NationalIdentifier
        extends SystemIdEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = NATIONAL_IDENTIFIER_FILE_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private File referenceFile;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = NATIONAL_IDENTIFIER_RECORD_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private RecordEntity referenceRecordEntity;

    public File getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(File referenceFile) {
        this.referenceFile = referenceFile;
    }

    public RecordEntity getReferenceRecordEntity() {
        return referenceRecordEntity;
    }

    public void setReferenceRecord(RecordEntity referenceRecordEntity) {
        this.referenceRecordEntity = referenceRecordEntity;
    }
}
