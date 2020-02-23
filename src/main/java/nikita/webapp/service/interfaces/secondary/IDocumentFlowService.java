package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;

public interface IDocumentFlowService {

    DocumentFlowHateoas associateDocumentFlowWithRegistryEntry
        (DocumentFlow documentFlow, RegistryEntry registryEntry);

    DocumentFlowHateoas associateDocumentFlowWithRecordNote
	(DocumentFlow documentFlow, RecordNote recordNote);

    DocumentFlowHateoas updateDocumentFlowBySystemId
	(String systemId, Long version, DocumentFlow incomingDocumentFlow);

    void deleteDocumentFlowBySystemId(String systemID);

    DocumentFlowHateoas findAllByOwner();

    DocumentFlowHateoas findBySystemId(String precedenceSystemId);

    DocumentFlowHateoas generateDefaultDocumentFlow();
}
