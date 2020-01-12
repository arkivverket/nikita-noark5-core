package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IConversionEntity;
import nikita.common.model.noark5.v5.secondary.Conversion;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IConversionHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CONVERSION;

/*
 * Used to add ConversionHateoas links with Conversion specific information
 */
@Component("conversionHateoasHandler")
public class ConversionHateoasHandler
        extends HateoasHandler
        implements IConversionHateoasHandler {

    public ConversionHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {

        String parentEntity = "";
        String parentSystemId = "";
        Conversion conversion = (Conversion) entity;
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + parentEntity + SLASH +
                parentSystemId + SLASH + CONVERSION + SLASH + entity.getSystemId(),
                getRelSelfLink()));

        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + parentEntity + SLASH +
                parentSystemId + SLASH + CONVERSION + SLASH + entity.getSystemId(),
                entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        Conversion conversion = (Conversion) entity;
        addDocumentObject(conversion, hateoasNoarkObject);
    }
    /**
     * Create a REL/HREF pair for the parent DocumentObject associated
     * with the given Conversion
     * <p>
     * "../hateoas-api/arkivstruktur/dokumentbeskrivelse/{systemId}"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/dokumentobject/"
     *
     * @param conversion         The Conversion object
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addDocumentObject(IConversionEntity conversion,
                                  IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(conversion,
            new Link(getOutgoingAddress() +
                     HREF_BASE_DOCUMENT_DESCRIPTION + SLASH +
                     conversion.getReferenceDocumentObject().getSystemId(),
                     REL_FONDS_STRUCTURE_DOCUMENT_OBJECT));
    }
}
