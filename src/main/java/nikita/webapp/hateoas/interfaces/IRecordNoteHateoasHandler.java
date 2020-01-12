package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 29/05/19.
 * <p>
 * Describe Hateoas links handler
 */
public interface IRecordNoteHateoasHandler
        extends IRecordHateoasHandler {

    void addDocumentFlow(ISystemId entity,
                         IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentFlow(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject);
}
