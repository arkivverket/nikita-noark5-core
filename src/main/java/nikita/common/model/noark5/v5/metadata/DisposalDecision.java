package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_DISPOSAL_DECISION;
import static nikita.common.config.Constants.TABLE_DISPOSAL_DECISION;
import static nikita.common.config.N5ResourceMappings.DISPOSAL_DECISION;

// Noark 5v5 Kassasjonsvedtak
@Entity
@Table(name = TABLE_DISPOSAL_DECISION)
public class DisposalDecision
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public DisposalDecision() {
    }

    public DisposalDecision(String code, String codename) {
        super(code, codename);
    }

    public DisposalDecision(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return DISPOSAL_DECISION;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_DISPOSAL_DECISION;
    }
}
