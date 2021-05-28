package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IDocumentFlowService {

    DocumentFlowHateoas associateDocumentFlowWithRegistryEntry
        (DocumentFlow documentFlow, RegistryEntry registryEntry);

    DocumentFlowHateoas associateDocumentFlowWithRecordNote
	(DocumentFlow documentFlow, RecordNote recordNote);

    DocumentFlowHateoas updateDocumentFlowBySystemId
            (@NotNull final UUID systemId, Long version, DocumentFlow incomingDocumentFlow);

    void deleteDocumentFlowBySystemId(@NotNull final UUID systemId);

    DocumentFlowHateoas findAll();

    DocumentFlowHateoas findBySystemId(UUID systemId);

    DocumentFlowHateoas generateDefaultDocumentFlow();

    void deleteDocumentFlow(@NotNull final DocumentFlow documentFlow);
}
