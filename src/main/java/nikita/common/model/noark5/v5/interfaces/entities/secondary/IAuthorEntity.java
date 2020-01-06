package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

public interface IAuthorEntity
        extends INikitaEntity {

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
