package nikita.common.model.noark5.v5.secondary;

import org.hibernate.annotations.Type;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * AuthorId is used as a  @IdClass for Author.
 */
@Embeddable
public class AuthorId
        implements Serializable {

    @Type(type = "uuid-char")
    private UUID referenceRecord;

    @Type(type = "uuid-char")
    private UUID referenceDocumentDescription;

    private String author;

    public AuthorId() {
    }

    public AuthorId(String author, UUID referenceRecord,
                    UUID referenceDocumentDescription) {
        this.author = author;
        this.referenceRecord = referenceRecord;
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public UUID getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(UUID referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public UUID getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            UUID referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorId)) return false;
        AuthorId that = (AuthorId) o;
        return Objects.equals(author, that.author) &&
                Objects.equals(referenceRecord,
                        that.referenceRecord) &&
                Objects.equals(referenceDocumentDescription,
                        that.referenceDocumentDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, referenceRecord,
                referenceDocumentDescription);
    }
}

