package nikita.common.model.noark5.v5.secondary;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.Record;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
// Enable soft delete of Author
// @SQLDelete(sql="UPDATE author SET deleted = true WHERE pk_author_id = ? and version = ?")
// @Where(clause="deleted <> true")
@AttributeOverride(name = "id", column = @Column(name = "pk_author_id"))
public class Author extends NoarkEntity {

    /**
     * M024 - forfatter (xs:string)
     */
    @Column(name = "author")
    @Audited
    private String author;
    // Links to Records
    @ManyToMany(mappedBy = "referenceAuthor")
    private List<Record> referenceRecord = new ArrayList<>();
    // Links to DocumentDescriptions
    @ManyToMany(mappedBy = "referenceAuthor")
    private List<DocumentDescription> referenceDocumentDescription = new ArrayList<>();

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.AUTHOR;
    }

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public List<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(List<DocumentDescription> referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    @Override
    public String toString() {
        return "Author{" + super.toString() +
                "author='" + author + '\'' +
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
        Author rhs = (Author) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(author, rhs.author)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(author)
                .toHashCode();
    }
}
