package nikita.webapp.hateoas.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IDocumentFlowEntity;
import nikita.webapp.hateoas.interfaces.IHateoasHandler;

public interface IDocumentFlowHateoasHandler
        extends IHateoasHandler {

    void addRecordNote(IDocumentFlowEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addRegistryEntry(IDocumentFlowEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addFlowStatus(IDocumentFlowEntity documentFlow,
                       IHateoasNoarkObject hateoasNoarkObject);
}
