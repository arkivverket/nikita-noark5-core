package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IRegistryEntryHateoasHandler extends IRecordHateoasHandler {

    void addPrecedence(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewPrecedence(INoarkEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addSignOff(INoarkEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewSignOff(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentFlow(INoarkEntity entity,
                         IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentFlow(INoarkEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    // Metadata entries
    void addRegistryEntryStatus(INoarkEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addRegistryEntryType(INoarkEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);
}
