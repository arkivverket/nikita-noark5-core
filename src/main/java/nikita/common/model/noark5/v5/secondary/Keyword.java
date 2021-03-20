package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.SystemIdEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import static nikita.common.config.Constants.TABLE_KEYWORD;
import static nikita.common.config.N5ResourceMappings.KEYWORD;

@Entity
@Table(name = TABLE_KEYWORD)
public class Keyword
        extends SystemIdEntity {

    /**
     * M022 - noekkelord (xs:string)
     */
    @Column(name = "keyword")
    @Audited
    private String keyword;

    // Links to Class
    @ManyToMany(mappedBy = "referenceKeyword")
    private Set<Class> referenceClass = new HashSet<>();

    // Links to File
    @ManyToMany(mappedBy = "referenceKeyword")
    private Set<File> referenceFile = new HashSet<>();

    // Links to Record
    @ManyToMany(mappedBy = "referenceKeyword")
    private Set<Record> referenceRecord = new HashSet<>();

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
        return KEYWORD; // TODO, should it have a relation key?
    }

    public Set<Class> getReferenceClass() {
        return referenceClass;
    }

    public void addClass(Class klass) {
        referenceClass.add(klass);
        klass.getReferenceKeyword().add(this);
    }

    public Set<File> getReferenceFile() {
        return referenceFile;
    }

    public void addFile(File file) {
        referenceFile.add(file);
        file.getReferenceKeyword().add(this);
    }

    public Set<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void addRecord(Record record) {
        referenceRecord.add(record);
        record.getReferenceKeyword().add(this);
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
