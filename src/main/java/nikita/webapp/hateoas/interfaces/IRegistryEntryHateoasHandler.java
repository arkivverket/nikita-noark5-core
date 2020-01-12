package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IRegistryEntryHateoasHandler
        extends IRecordHateoasHandler {

    void addPrecedence(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewPrecedence(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addSignOff(ISystemId entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewSignOff(ISystemId entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentFlow(ISystemId entity,
                         IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentFlow(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    // Metadata entries
    void addRegistryEntryStatus(ISystemId entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addRegistryEntryType(ISystemId entity,
                              IHateoasNoarkObject hateoasNoarkObject);
}
