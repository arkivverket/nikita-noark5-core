package nikita.common.model.noark5.v5.interfaces;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDocumentMedium {
    // Same as in INoarkEntity, to make it available for validateDocumentMedium
    String getBaseTypeName();

    String getDocumentMediumCode();

    void setDocumentMediumCode(String documentMediumCode);

    String getDocumentMediumCodeName();

    void setDocumentMediumCodeName(String documentMediumCodeName);
}
