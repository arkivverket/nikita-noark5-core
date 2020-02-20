package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import java.time.OffsetDateTime;

import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.Format;

// TODO check if this inheritence is ok.
public interface IConversionEntity
        extends ISystemId {

    OffsetDateTime getConvertedDate();

    void setConvertedDate(OffsetDateTime convertedDate);

    String getConvertedBy();

    void setConvertedBy(String convertedBy);

    Format getConvertedFromFormat();

    void setConvertedFromFormat(Format convertedFromFormat);

    Format getConvertedToFormat();

    void setConvertedToFormat(Format convertedToFormat);

    String getConversionTool();

    void setConversionTool(String conversionTool);

    String getConversionComment();

    void setConversionComment(String conversionComment);

    DocumentObject getReferenceDocumentObject();

    void setReferenceDocumentObject(DocumentObject referenceDocumentObject);
}
