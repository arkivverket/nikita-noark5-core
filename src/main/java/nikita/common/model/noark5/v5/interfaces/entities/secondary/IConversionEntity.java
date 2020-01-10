package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import java.time.OffsetDateTime;

import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

// FIXME check if this inheritence is ok.
public interface IConversionEntity
        extends INikitaEntity {

    OffsetDateTime getConvertedDate();

    void setConvertedDate(OffsetDateTime convertedDate);

    String getConvertedBy();

    void setConvertedBy(String convertedBy);

    String getConvertedFromFormat();

    void setConvertedFromFormat(String convertedFromFormat);

    String getConvertedToFormat();

    void setConvertedToFormat(String convertedToFormat);

    String getConversionTool();

    void setConversionTool(String conversionTool);

    String getConversionComment();

    void setConversionComment(String conversionComment);

    DocumentObject getReferenceDocumentObject();

    void setReferenceDocumentObject(DocumentObject referenceDocumentObject);
}
