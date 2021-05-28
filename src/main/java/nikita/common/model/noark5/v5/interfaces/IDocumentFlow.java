package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.DocumentFlow;

import java.util.List;

public interface IDocumentFlow {
    List<DocumentFlow> getReferenceDocumentFlow();
    void addDocumentFlow(DocumentFlow documentFlow);
    void removeDocumentFlow(DocumentFlow documentFlow);
}
