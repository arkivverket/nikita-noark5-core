package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IFondsCreatorEntity;
import nikita.common.util.deserialisers.FondsCreatorDeserializer;
import nikita.webapp.hateoas.FondsCreatorHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_FONDS_CREATOR)
@JsonDeserialize(using = FondsCreatorDeserializer.class)
@HateoasPacker(using = FondsCreatorHateoasHandler.class)
@HateoasObject(using = FondsCreatorHateoas.class)
@Indexed
public class FondsCreator
        extends SystemIdEntity
        implements IFondsCreatorEntity {

    /**
     * M006 - arkivskaperID (xs:string)
     */
    @NotNull
    @Column(name = FONDS_CREATOR_ID_ENG, nullable = false)
    @Audited
    @JsonProperty(FONDS_CREATOR_ID)
    @KeywordField
    private String fondsCreatorId;

    /**
     * M023 - arkivskaperNavn (xs:string)
     */
    @NotNull
    @Column(name = FONDS_CREATOR_NAME_ENG, nullable = false)
    @Audited
    @JsonProperty(FONDS_CREATOR_NAME)
    @FullTextField
    private String fondsCreatorName;

    /**
     * M021 - beskrivelse (xs:string)
     */
    @Column(name = DESCRIPTION_ENG, length = DESCRIPTION_LENGTH)
    @Audited
    @JsonProperty(DESCRIPTION)
    @FullTextField
    private String description;

    // Links to Fonds
    @ManyToMany(mappedBy = "referenceFondsCreator")
    private Set<Fonds> referenceFonds = new HashSet<>();

    public String getFondsCreatorId() {
        return fondsCreatorId;
    }

    public void setFondsCreatorId(String fondsCreatorId) {
        this.fondsCreatorId = fondsCreatorId;
    }

    public String getFondsCreatorName() {
        return fondsCreatorName;
    }

    public void setFondsCreatorName(String fondsCreatorName) {
        this.fondsCreatorName = fondsCreatorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getBaseTypeName() {
        return FONDS_CREATOR;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_FONDS_CREATOR;
    }

    public Set<Fonds> getReferenceFonds() {
        return referenceFonds;
    }

    public void addFonds(Fonds fonds) {
        this.referenceFonds.add(fonds);
        fonds.getReferenceFondsCreator().add(this);
    }

    public void removeFonds(Fonds fonds) {
        this.referenceFonds.remove(fonds);
        fonds.getReferenceFondsCreator().remove(this);
    }

    @Override
    public String toString() {
        return "FondsCreator{" + super.toString() +
                ", fondsCreatorId='" + fondsCreatorId + '\'' +
                ", fondsCreatorName='" + fondsCreatorName + '\'' +
                ", description='" + description + '\'' +
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
        FondsCreator rhs = (FondsCreator) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(fondsCreatorId, rhs.fondsCreatorId)
                .append(fondsCreatorName, rhs.fondsCreatorName)
                .append(description, rhs.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(fondsCreatorId)
                .append(fondsCreatorName)
                .append(description)
                .toHashCode();
    }
}
