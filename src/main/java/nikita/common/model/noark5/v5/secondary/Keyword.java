package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.Class;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

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
    private List<Class> referenceClass = new ArrayList<>();

    // Links to File
    @ManyToMany(mappedBy = "referenceKeyword")
    private List<File> referenceFile = new ArrayList<>();

    // Links to Record
    @ManyToMany(mappedBy = "referenceKeyword")
    private List<Record> referenceRecord = new ArrayList<>();

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

    public List<Class> getReferenceClass() {
        return referenceClass;
    }

    public void setReferenceClass(List<Class> referenceClass) {
        this.referenceClass = referenceClass;
    }

    public List<File> getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
    }

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
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
