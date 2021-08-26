package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IKeywordEntity;
import nikita.common.util.deserialisers.secondary.KeywordDeserializer;
import nikita.webapp.hateoas.secondary.KeywordHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_KEYWORD;
import static nikita.common.config.Constants.TABLE_KEYWORD;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_KEYWORD)
@JsonDeserialize(using = KeywordDeserializer.class)
@HateoasPacker(using = KeywordHateoasHandler.class)
@HateoasObject(using = KeywordHateoas.class)
@Indexed
public class Keyword
        extends SystemIdEntity
        implements IKeywordEntity {
    /**
     * M022 - noekkelord (xs:string)
     */
    @Column(name = KEYWORD_ENG_OBJECT, nullable = false)
    @Audited
    @JsonProperty(KEYWORD)
    @KeywordField
    private String keyword;

    // Links to Class
    @ManyToMany(mappedBy = REFERENCE_KEYWORD)
    private final Set<Class> referenceClass = new HashSet<>();
    // Links to File
    @ManyToMany(mappedBy = REFERENCE_KEYWORD)
    private final Set<File> referenceFile = new HashSet<>();
    // Links to Record
    @ManyToMany(mappedBy = REFERENCE_KEYWORD)
    private final Set<RecordEntity> referenceRecord = new HashSet<>();

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getBaseTypeName() {
        return KEYWORD;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_KEYWORD;
    }

    @Override
    public Set<Class> getReferenceClass() {
        return referenceClass;
    }

    @Override
    public void addReferenceClass(Class klass) {
        referenceClass.add(klass);
        klass.getReferenceKeyword().add(this);
    }

    @Override
    public void removeReferenceClass(Class klass) {
        referenceClass.remove(klass);
        klass.getReferenceKeyword().remove(this);
    }

    @Override
    public Set<File> getReferenceFile() {
        return referenceFile;
    }

    @Override
    public void addReferenceFile(File file) {
        referenceFile.add(file);
        file.getReferenceKeyword().add(this);
    }

    @Override
    public void removeReferenceFile(File file) {
        referenceFile.remove(file);
        file.getReferenceKeyword().remove(this);
    }

    @Override
    public Set<RecordEntity> getReferenceRecord() {
        return referenceRecord;
    }

    @Override
    public void addReferenceRecord(RecordEntity record) {
        referenceRecord.add(record);
        record.getReferenceKeyword().add(this);
    }

    @Override
    public void removeReferenceRecord(RecordEntity record) {
        referenceRecord.remove(record);
        record.getReferenceKeyword().remove(this);
    }

    @Override
    public String toString() {
        return "Keyword{" + super.toString() +
                "keyword='" + keyword + '\'' +
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
        Keyword rhs = (Keyword) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(keyword, rhs.keyword)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(keyword)
                .toHashCode();
    }
}
