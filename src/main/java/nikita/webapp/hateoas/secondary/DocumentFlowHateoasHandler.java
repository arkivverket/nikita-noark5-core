package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IDocumentFlowEntity;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.ISystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IDocumentFlowHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_FLOW;
import static nikita.common.config.N5ResourceMappings.FLOW_STATUS;

@Component("documentFlowHateoasHandler")
public class DocumentFlowHateoasHandler
        extends SystemIdHateoasHandler
        implements IDocumentFlowHateoasHandler {

    public DocumentFlowHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfhref =  getOutgoingAddress() +
            HREF_BASE_DOCUMENT_FLOW + SLASH + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfhref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfhref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        DocumentFlow documentFlow = (DocumentFlow) entity;
        addRecordNote(documentFlow, hateoasNoarkObject);
        addRegistryEntry(documentFlow, hateoasNoarkObject);
        addFlowStatus(documentFlow, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate
        (ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        DocumentFlow documentFlow = (DocumentFlow) entity;
        addFlowStatus(documentFlow, hateoasNoarkObject);
    }

    @Override
    public void addRecordNote(IDocumentFlowEntity documentFlow,
                              IHateoasNoarkObject hateoasNoarkObject) {
        if (null != documentFlow.getReferenceRecordNote()) {
            hateoasNoarkObject.addLink(documentFlow,
                new Link(getOutgoingAddress() + HREF_BASE_RECORD_NOTE + SLASH +
                         documentFlow.getReferenceRecordNote().getSystemId(),
                         REL_CASE_HANDLING_RECORD_NOTE));
        }
    }

    @Override
    public void addRegistryEntry(IDocumentFlowEntity documentFlow,
                                 IHateoasNoarkObject hateoasNoarkObject) {
        if (null != documentFlow.getReferenceRegistryEntry()) {
            hateoasNoarkObject.addLink(documentFlow,
                new Link(getOutgoingAddress() +
                         HREF_BASE_REGISTRY_ENTRY + SLASH +
                         documentFlow.getReferenceRegistryEntry().getSystemId(),
                         REL_CASE_HANDLING_REGISTRY_ENTRY));
        }
    }
    @Override
    public void addFlowStatus(IDocumentFlowEntity documentFlow,
                          IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(documentFlow,
            new Link(getOutgoingAddress() + HREF_BASE_METADATA + SLASH + FLOW_STATUS,
                REL_METADATA_FLOW_STATUS, true));
    }
}
