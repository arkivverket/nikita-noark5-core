package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

public interface IAuthorEntity
        extends INoarkEntity {

    String getAuthor();

    void setAuthor(String author);

    Record getReferenceRecord();

    void setReferenceRecord(Record referenceRecord);

    DocumentDescription getReferenceDocumentDescription();

    void setReferenceDocumentDescription(
            DocumentDescription referenceDocumentDescription);

    Boolean getForDocumentDescription();

    Boolean getForRecord();
}
