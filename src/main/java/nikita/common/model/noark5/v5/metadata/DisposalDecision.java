package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_DISPOSAL_DECISION;
import static nikita.common.config.N5ResourceMappings.DISPOSAL_DECISION;

// Noark 5v5 Kassasjonsvedtak
@Entity
@Table(name = TABLE_DISPOSAL_DECISION)
public class DisposalDecision
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return DISPOSAL_DECISION;
    }
}
