package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IDocumentObjectHateoasHandler extends IHateoasHandler {

    void addDocumentDescription(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addConversion(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewConversion(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceDocumentFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addConvertFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addVariantFormat(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);

    void addFormat(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject);
}

