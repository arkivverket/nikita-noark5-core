package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
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
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_COMMENT;
import static nikita.common.config.Constants.TABLE_COMMENT;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_COMMENT)
@JsonDeserialize(using = CommentDeserializer.class)
@HateoasPacker(using = CommentHateoasHandler.class)
@HateoasObject(using = CommentHateoas.class)
@Indexed
public class Comment
        extends SystemIdEntity
        implements ICommentEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M310 - merknadstekst (xs:string)
     */
    @Column(name = COMMENT_TEXT_ENG, length = 8192)
    @JsonProperty(COMMENT_TEXT)
    @Audited
    @FullTextField
    private String commentText;

    /**
     * M??? - merknadstype code (xs:string)
     */
    @Column(name = COMMENT_TYPE_CODE_ENG)
    @JsonProperty(COMMENT_TYPE_CODE)
    @Audited
    private String commentTypeCode;

    /**
     * M084 - merknadstype code name (xs:string)
     */
    @Column(name = COMMENT_TYPE_CODE_NAME_ENG)
    @JsonProperty(COMMENT_TYPE_CODE_NAME)
    @Audited
    private String commentTypeCodeName;

    /**
     * M611 - merknadsdato (xs:dateTime)
     */
    @Column(name = COMMENT_TIME_ENG)
    @JsonProperty(COMMENT_TIME)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @GenericField
    private OffsetDateTime commentDate;

    /**
     * M612 - merknadRegistrertAv (xs:string)
     */
    @Column(name = COMMENT_REGISTERED_BY_ENG)
    @JsonProperty(COMMENT_REGISTERED_BY)
    @Audited
    @KeywordField
    private String commentRegisteredBy;

    // Link to File
    @ManyToMany(mappedBy = REFERENCE_COMMENT)
    private final Set<File> referenceFile = new HashSet<>();

    // Links to Record
    @ManyToMany(mappedBy = REFERENCE_COMMENT)
    private final Set<RecordEntity> referenceRecord = new HashSet<>();

    // Link to DocumentDescription
    @ManyToMany(mappedBy = REFERENCE_COMMENT)
    private final Set<DocumentDescription> referenceDocumentDescription =
            new HashSet<>();

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public CommentType getCommentType() {
        if (null == commentTypeCode)
            return null;
        return new CommentType(commentTypeCode, commentTypeCodeName);
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

    public Set<File> getReferenceFile() {
        return referenceFile;
    }

    public void addFile(File file) {
        this.referenceFile.add(file);
    }

    public void removeFile(File file) {
        this.referenceFile.remove(file);
        file.getReferenceComment().remove(this);
    }

    public Set<RecordEntity> getReferenceRecord() {
        return referenceRecord;
    }

    public void addRecord(RecordEntity record) {
        this.referenceRecord.add(record);
    }

    public void removeRecord(RecordEntity record) {
        this.referenceRecord.remove(record);
        record.getReferenceComment().remove(this);
    }

    public Set<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void addDocumentDescription(
            DocumentDescription documentDescription) {
        this.referenceDocumentDescription.add(documentDescription);
    }

    public void removeDocumentDescription(
            DocumentDescription documentDescription) {
        this.referenceDocumentDescription.remove(documentDescription);
        documentDescription.getReferenceComment().remove(this);
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
