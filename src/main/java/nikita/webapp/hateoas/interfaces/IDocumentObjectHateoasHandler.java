package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IDocumentObjectHateoasHandler extends IHateoasHandler {

    void addDocumentDescription(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addConversion(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewConversion(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceDocumentFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addConvertFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addVariantFormat(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addFormat(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
}

