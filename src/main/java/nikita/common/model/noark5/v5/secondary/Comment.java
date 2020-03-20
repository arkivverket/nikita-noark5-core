package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICommentEntity;
import nikita.common.model.noark5.v5.metadata.CommentType;
import nikita.common.util.deserialisers.secondary.CommentDeserializer;
import nikita.webapp.hateoas.secondary.CommentHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.COMMENT;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_COMMENT)
@JsonDeserialize(using = CommentDeserializer.class)
@HateoasPacker(using = CommentHateoasHandler.class)
@HateoasObject(using = CommentHateoas.class)
public class Comment
        extends SystemIdEntity
        implements ICommentEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M310 - merknadstekst (xs:string)
     */
    @Column(name = "comment_text")
    @Audited
    private String commentText;

    /**
     * M??? - merknadstype code (xs:string)
     */
    @Column(name = "comment_type_code")
    @Audited
    private String commentTypeCode;

    /**
     * M084 - merknadstype code name (xs:string)
     */
    @Column(name = "comment_type_code_name")
    @Audited
    private String commentTypeCodeName;

    /**
     * M611 - merknadsdato (xs:dateTime)
     */
    @Column(name = "comment_time")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private OffsetDateTime commentDate;

    /**
     * M612 - merknadRegistrertAv (xs:string)
     */
    @Column(name = "comment_registered_by")
    @Audited
    private String commentRegisteredBy;

    // Link to File
    @ManyToOne
    @JoinColumn(name = FOREIGN_KEY_FILE_PK)
    private File referenceFile;

    // Links to Record
    @ManyToOne
    @JoinColumn(name = FOREIGN_KEY_RECORD_PK)
    private Record referenceRecord;

    // Link to DocumentDescription
    @ManyToOne
    @JoinColumn(name = FOREIGN_KEY_DOCUMENT_DESCRIPTION_PK)
    private DocumentDescription referenceDocumentDescription;

    /**
     * Used to identify if the current comment is associated  with a file
     * This can be used to save a potential lookup in the database.
     */
    @Column(name = "is_for_file")
    @Audited
    private Boolean isForFile = false;

    /**
     * Used to identify if the current comment is associated  with a record
     * This can be used to save a potential lookup in the database.
     */
    @Column(name = "is_for_record")
    @Audited
    private Boolean isForRecord = false;

    /**
     * Used to identify if the current comment is associated  with a document
     * description. This can be used to save a potential lookup in the database.
     */
    @Column(name = "is_for_document_description")
    @Audited
    private Boolean isForDocumentDescription = false;

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public CommentType getCommentType() {
        if (null == commentTypeCode)
            return null;
        return new CommentType(commentTypeCode,commentTypeCodeName);
    }

    public void setCommentType(CommentType commentType) {
        if (null != commentType) {
            this.commentTypeCode = commentType.getCode();
            this.commentTypeCodeName = commentType.getCodeName();
        } else {
            this.commentTypeCode = null;
            this.commentTypeCodeName = null;
        }
    }

    public OffsetDateTime getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(OffsetDateTime commentDate) {
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
        return COMMENT;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_COMMENT;
    }

    public File getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(File referenceFile) {
        this.referenceFile = referenceFile;
        isForFile = true;
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

    public void setReferenceDocumentDescription
        (DocumentDescription referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
        isForDocumentDescription = true;
    }

    public Boolean getForFile() {
        return isForFile;
    }

    public Boolean getForRecord() {
        return isForRecord;
    }

    public Boolean getForDocumentDescription() {
        return isForDocumentDescription;
    }

    @Override
    public String toString() {
        return "Comment{" + super.toString() +
                ", commentText='" + commentText + '\'' +
                ", commentTypeCode='" + commentTypeCode + '\'' +
                ", commentTypeCodeName='" + commentTypeCodeName + '\'' +
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
                .append(commentTypeCode, rhs.commentTypeCode)
                .append(commentTypeCodeName, rhs.commentTypeCodeName)
                .append(commentDate, rhs.commentDate)
                .append(commentRegisteredBy, rhs.commentRegisteredBy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(commentText)
                .append(commentTypeCode)
                .append(commentTypeCodeName)
                .append(commentDate)
                .append(commentRegisteredBy)
                .toHashCode();
    }
}
