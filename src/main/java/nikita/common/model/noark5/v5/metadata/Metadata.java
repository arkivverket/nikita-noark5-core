package nikita.common.model.noark5.v5.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.deserialisers.metadata.MetadataDeserializer;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.hateoas.metadata.MetadataHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;
import static nikita.common.config.Constants.NOARK_METADATA_PATH;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 3/23/17.
 */
@Entity
@Inheritance(strategy = TABLE_PER_CLASS)
@JsonDeserialize(using = MetadataDeserializer.class)
@HateoasPacker(using = MetadataHateoasHandler.class)
@HateoasObject(using = MetadataHateoas.class)
public class Metadata
        extends NoarkEntity
        implements IMetadataEntity {

    /**
     * M??? - kode (xs:string)
     */
    @Id
    @Column(name = CODE_ENG)
    @Audited
    @JsonProperty(CODE)
    protected String code;

    /**
     * M??? - inaktiv (xs:boolean)
     */
    @Column(name = "inactive")
    @Audited
    @JsonProperty(CODE_INACTIVE)
    protected Boolean inactive = false;

    /**
     * M??? - kodenavn (xs:string)
     */
    @Column(name = CODE_NAME_ENG)
    @Audited
    @JsonProperty(CODE_NAME)
    protected String codeName;

    public Metadata() {
    }

    public Metadata(String code, String codename) {
        setCode(code);
        setCodeName(codename);
    }

    public Metadata(String code) {
        setCode(code);
        setCodeName(null);
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

    @Override
    public Boolean getInactive() {
        return inactive;
    }

    @Override
    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }


    @Override
    public String toString() {
        return "Metadata{" +
                "code='" + code + '\'' +
                ", inactive='" + inactive + '\'' +
                ", codeName='" + codeName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        Metadata rhs = (Metadata) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(code, rhs.code)
                .append(codeName, rhs.codeName)
                .append(inactive, rhs.inactive)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, codeName, inactive);
    }

    @Override
    public String getBaseTypeName() {
        return "Metadata";
    }

    @Override
    public String getBaseRel() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return getCode();
    }

    @Override
    public String getIdentifierType() {
        return CODE;
    }

    // All Metadata entities belong to "metadata".
    // All children pick up the value from here
    @Override
    public String getFunctionalTypeName() {
        return NOARK_METADATA_PATH;
    }

    @Override
    public void setBaseTypeName(String baseTypeName) {

    }

    @Override
    public void setBaseRel(String baseRel) {

    }

    @Override
    public String setFunctionalTypeName(String functionalTypeName) {
        return null;
    }

    public void createReference(@NotNull INoarkEntity entity,
                                @NotNull String referenceType) {
        // I really should be overridden. Currently throwing an Exception if I
        // am not overridden as nikita is unable to process this
        throw new NikitaMalformedInputDataException("Error when trying to " +
                "create a reference between entities");
    }
}
