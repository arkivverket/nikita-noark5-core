package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_SCREENING_DOCUMENT;
import static nikita.common.config.Constants.TABLE_SCREENING_DOCUMENT;
import static nikita.common.config.N5ResourceMappings.SCREENING_DOCUMENT;

// Noark 5v5 Skjermingdokument
@Entity
@Table(name = TABLE_SCREENING_DOCUMENT)
public class ScreeningDocument
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public ScreeningDocument() {
    }

    public ScreeningDocument(String code, String codename) {
        super(code, codename);
    }

    public ScreeningDocument(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return SCREENING_DOCUMENT;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_SCREENING_DOCUMENT;
    }
}
