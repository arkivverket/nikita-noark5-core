package nikita.common.model.noark5.v5.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.TABLE_NATIONAL_IDENTIFIER;

@Entity
@Table(name = TABLE_NATIONAL_IDENTIFIER)
@Inheritance(strategy = JOINED)
//@JsonDeserialize(using = NationalIdentifierDeserializer.class)
//@HateoasPacker(using = NationalIdentifierHateoasHandler.class)
//@HateoasObject(using = NationalIdentifierHateoas.class)
@Audited
public class NationalIdentifier
        extends NoarkEntity {
}
