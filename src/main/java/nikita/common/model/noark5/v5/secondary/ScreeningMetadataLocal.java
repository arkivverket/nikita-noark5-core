package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.webapp.hateoas.secondary.ScreeningMetadataHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Note. There are two "ScreeningMetadata" tables in nikitas Noark
 * implementation. ScreeningMetadata is defined as a Metadata table with
 * associated RELS/HREF, in much the same way the other Metadata classes are
 * implemented. However, ScreeningMetadata is also in a 0:m relationship with
 * e.g., Screening. This means the traditional approach (0:1) where the
 * code/codename are copied into the associated object will not work and
 * ScreeningMetadata needs its own table. This object is called
 * ScreeningMetadataLocal.
 * ScreeningMetadata stores data in the table md_screening_metadata
 * ScreeningMetadataLocal stores data in the table as_screening_metadata
 */
// Noark 5v5 skjermingmetadata
@Entity
@Table(name = TABLE_FONDS_STRUCTURE_SCREENING_METADATA)
@HateoasPacker(using = ScreeningMetadataHateoasHandler.class)
@HateoasObject(using = ScreeningMetadataHateoas.class)
public class ScreeningMetadataLocal
        extends SystemIdEntity
        implements IMetadataEntity {

    /**
     * M??? - kode (xs:string)
     */
    @Column(name = CODE_ENG)
    @Audited
    @JsonProperty(CODE)
    protected String code;
    /**
     * M??? - kodenavn (xs:string)
     */
    @Column(name = CODE_NAME_ENG)
    @Audited
    @JsonProperty(CODE_NAME)
    protected String codeName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = SCREENING_SCREENING_METADATA_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Screening referenceScreening;

    public ScreeningMetadataLocal() {
    }

    public ScreeningMetadataLocal(Metadata metadata) {
        this.code = metadata.getCode();
        this.codeName = metadata.getCodeName();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getCodeName() {
        return codeName;
    }

    @Override
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    /**
     * This method will always return the randomly chosen value false as
     * it only implements IMetadataEntity so that we can reuse the
     * serialisation code for Metadata entities. This class is in a
     * unique position that it is in a 0:m relationship with Screening so
     * it cannot be implemented the same as the other Metadata classes
     *
     * @return null
     */
    @Override
    public Boolean getInactive() {
        return false;
    }

    /**
     * This method will always ignore the value sent in from calling it as
     * it only implements IMetadataEntity so that we can reuse the
     * serialisation code for Metadata entities. This class is in a
     * unique position that it is in a 0:m relationship with Screening so
     * it cannot be implemented the same as the other Metadata classes
     */
    @Override
    public void setInactive(Boolean inactive) {

    }

    public Screening getReferenceScreening() {
        return referenceScreening;
    }

    public void setReferenceScreening(Screening referenceScreening) {
        this.referenceScreening = referenceScreening;
    }

    @Override
    public String getBaseTypeName() {
        return SCREENING_METADATA;
    }

    /**
     * This method will always ignore the value sent in from calling it as
     * it only implements IMetadataEntity so that we can reuse the
     * serialisation code for Metadata entities. This class is in a
     * unique position that it is in a 0:m relationship with Screening so
     * it cannot be implemented the same as the other Metadata classes
     */
    @Override
    public void setBaseTypeName(String baseTypeName) {
    }

    /**
     * This method will always ignore the value sent in from calling it as
     * it only implements IMetadataEntity so that we can reuse the
     * serialisation code for Metadata entities. This class is in a
     * unique position that it is in a 0:m relationship with Screening so
     * it cannot be implemented the same as the other Metadata classes
     */
    @Override
    public String setFunctionalTypeName(String functionalTypeName) {
        return null;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_SCREENING_METADATA;
    }

    /**
     * This method will always ignore the value sent in from calling it as
     * it only implements IMetadataEntity so that we can reuse the
     * serialisation code for Metadata entities. This class is in a
     * unique position that it is in a 0:m relationship with Screening so
     * it cannot be implemented the same as the other Metadata classes
     */
    @Override
    public void setBaseRel(String baseRel) {
    }
}
