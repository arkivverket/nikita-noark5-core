package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IRegistryEntryHateoasHandler extends IRecordHateoasHandler {

    void addPrecedence(INikitaEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewPrecedence(INikitaEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addSignOff(INikitaEntity entity,
                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewSignOff(INikitaEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addDocumentFlow(INikitaEntity entity,
                         IHateoasNoarkObject hateoasNoarkObject);

    void addNewDocumentFlow(INikitaEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject);

    // Metadata entries
    void addRegistryEntryStatus(INikitaEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject);

    void addRegistryEntryType(INikitaEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject);
}
