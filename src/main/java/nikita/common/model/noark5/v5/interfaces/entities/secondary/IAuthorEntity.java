package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

public interface IAuthorEntity
        extends INoarkEntity {

    String getAuthor();

    void setAuthor(String author);

    RecordEntity getReferenceRecordEntity();

    void setReferenceRecord(RecordEntity referenceRecordEntity);

    DocumentDescription getReferenceDocumentDescription();

    void setReferenceDocumentDescription(
            DocumentDescription referenceDocumentDescription);

    Boolean getForDocumentDescription();

    Boolean getForRecord();
}
