package nikita.common.model.noark5.v5.md_other;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.md_other.BSMMetadataHateoas;
import nikita.common.util.deserialisers.BSMMetadataDeserialiser;
import nikita.webapp.hateoas.metadata.BSMMetadataHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import nikita.webapp.util.annotation.Updatable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

import static nikita.common.config.Constants.REL_METADATA_BSM;
import static nikita.common.config.Constants.TABLE_BSM_METDATA;
import static nikita.common.config.N5ResourceMappings.*;

// Noark 5v5 virksomhetsspesifikkeMetadata
// Registered additional metadata
@Entity
@Table(name = TABLE_BSM_METDATA)
@JsonDeserialize(using = BSMMetadataDeserialiser.class)
@HateoasPacker(using = BSMMetadataHateoasHandler.class)
@HateoasObject(using = BSMMetadataHateoas.class)
public class BSMMetadata
        extends SystemIdEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = NAME_ENG, unique = true, updatable = false, nullable = false)
    @JsonProperty(NAME)
    private String name;

    @Column(name = TYPE_ENG, nullable = false)
    @JsonProperty(TYPE)
    private String type;

    @Column(name = OUTDATED_ENG, nullable = false)
    @JsonProperty(OUTDATED)
    @Updatable
    private Boolean outdated = false;

    @Column(name = DESCRIPTION_ENG)
    @JsonProperty(DESCRIPTION)
    @Updatable
    private String description;

    @Column(name = SOURCE_ENG)
    @JsonProperty(SOURCE)
    @Updatable
    private String source;

    public BSMMetadata() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getOutdated() {
        return outdated;
    }

    public void setOutdated(Boolean outdated) {
        this.outdated = outdated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getBaseTypeName() {
        return BSM_DEF;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_BSM;
    }

    @Override
    public String toString() {
        return "BSMMetadata{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", outdated=" + outdated +
                ", description='" + description + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BSMMetadata that = (BSMMetadata) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(outdated, that.outdated) &&
                Objects.equals(description, that.description) &&
                Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, type, outdated,
                description, source);
    }
}
