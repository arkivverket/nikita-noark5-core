package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.DocumentFlow;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDocumentFlow {
    List<DocumentFlow> getReferenceDocumentFlow();

    void setReferenceDocumentFlow(List<DocumentFlow> documentFlow);

    void addReferenceDocumentFlow(DocumentFlow documentFlow);
}
