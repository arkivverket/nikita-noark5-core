package nikita.common.model.noark5.v5.secondary;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.BasicRecord;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ICommentEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = "comment")
// Enable soft delete of Comment
// @SQLDelete(sql="UPDATE comment SET deleted = true WHERE pk_comment_id = ? and version = ?")
// @Where(clause="deleted <> true")
@AttributeOverride(name = "id", column = @Column(name = "pk_comment_id"))
public class Comment
        extends NoarkEntity
        implements ICommentEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M310 - merknadstekst (xs:string)
     */
    @Column(name = "comment_text")
    @Audited
    private String commentText;

    /**
     * M084 - merknadstype (xs:string)
     */
    @Column(name = "comment_type")
    @Audited
    private String commentType;

    /**
     * M611 - merknadsdato (xs:dateTime)
     */
    @Column(name = "comment_time")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private ZonedDateTime commentDate;

    /**
     * M612 - merknadRegistrertAv (xs:string)
     */
    @Column(name = "comment_registered_by")
    @Audited
    private String commentRegisteredBy;

    // Link to File
    @ManyToMany(mappedBy = "referenceComment")
    private List<File> referenceFile = new ArrayList<>();

    // Links to BasicRecord
    @ManyToMany(mappedBy = "referenceComment")
    private List<BasicRecord> referenceRecord = new ArrayList<>();

    // Link to DocumentDescription
    @ManyToMany(mappedBy = "referenceComment")
    private List<DocumentDescription> referenceDocumentDescription = new ArrayList<>();

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public ZonedDateTime getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(ZonedDateTime commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentRegisteredBy() {
        return commentRegisteredBy;
    }

    public void setCommentRegisteredBy(String commentRegisteredBy) {
        this.commentRegisteredBy = commentRegisteredBy;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.COMMENT;
    }

    public List<File> getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
    }

    public List<BasicRecord> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<BasicRecord> referenceRecord) {
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
        return "Comment{" + super.toString() +
                ", commentText='" + commentText + '\'' +
                ", commentType='" + commentType + '\'' +
                ", commentDate=" + commentDate +
                ", commentRegisteredBy='" + commentRegisteredBy + '\'' +
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
        Comment rhs = (Comment) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(commentText, rhs.commentText)
                .append(commentType, rhs.commentType)
                .append(commentDate, rhs.commentDate)
                .append(commentRegisteredBy, rhs.commentRegisteredBy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(commentText)
                .append(commentType)
                .append(commentDate)
                .append(commentRegisteredBy)
                .toHashCode();
    }
}
