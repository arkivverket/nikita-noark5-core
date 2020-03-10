package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IAuthorEntity;
import nikita.common.util.deserialisers.secondary.AuthorDeserializer;
import nikita.webapp.hateoas.secondary.AuthorHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.AUTHOR;

@Entity
@Table(name = TABLE_CONTACT_AUTHOR)
@JsonDeserialize(using = AuthorDeserializer.class)
@HateoasPacker(using = AuthorHateoasHandler.class)
@HateoasObject(using = AuthorHateoas.class)
public class Author
        extends SystemIdEntity
        implements IAuthorEntity {

    @ManyToOne
    @JoinColumn(name = FOREIGN_KEY_RECORD_PK)
    private Record referenceRecord;

    @ManyToOne
    @JoinColumn(name = FOREIGN_KEY_DOCUMENT_DESCRIPTION_PK)
    private DocumentDescription referenceDocumentDescription;

    /**
     * M024 - forfatter (xs:string)
     */
    @Column(name = "author")
    @Audited
    private String author;

    /**
     * Used to identify if the current author is associated  with a document
     * description. This can be used to save a potential lookup in the database.
     */
    @Column(name = "is_for_document_description")
    @Audited
    private Boolean isForDocumentDescription = false;

    /**
     * Used to identify if the current author is associated  with a record
     * This can be used to save a potential lookup in the database.
     */
    @Column(name = "is_for_record")
    @Audited
    private Boolean isForRecord = false;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Record getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(Record referenceRecord) {
        this.referenceRecord = referenceRecord;
        isForRecord = true;
    }

    public DocumentDescription getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            DocumentDescription referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
        isForDocumentDescription = true;
    }

    public Boolean getForDocumentDescription() {
        return isForDocumentDescription;
    }

    public Boolean getForRecord() {
        return isForRecord;
    }

    @Override
    public String getBaseTypeName() {
        return AUTHOR;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_AUTHOR;
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
