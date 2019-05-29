package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IDocumentObjectHateoasHandler extends IHateoasHandler {

    void addDocumentDescription(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addConversion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewConversion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addElectronicSignature(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addNewElectronicSignature(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceDocumentFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addVariantFormat(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addFormat(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);
}

