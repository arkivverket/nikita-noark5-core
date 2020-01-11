package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface ICaseFileHateoasHandler
        extends IHateoasHandler {

    void addNewClass(INoarkEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject);

    void addClass(INoarkEntity entity,
                  IHateoasNoarkObject hateoasNoarkObject);

    void addNewPrecedence(INoarkEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addPrecedence(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);

    void addSecondaryClassification(INoarkEntity entity,
                                    IHateoasNoarkObject hateoasNoarkObject);

    void addNewSecondaryClassification(INoarkEntity entity,
                                       IHateoasNoarkObject hateoasNoarkObject);

    void addNewRegistryEntry(INoarkEntity entity,
                             IHateoasNoarkObject hateoasNoarkObject);

    void addRegistryEntry(INoarkEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addNewRecordNote(INoarkEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addRecordNote(INoarkEntity entity,
                       IHateoasNoarkObject hateoasNoarkObject);
}
