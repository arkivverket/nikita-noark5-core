package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IConversionEntity;
import nikita.common.model.noark5.v5.secondary.Conversion;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IConversionHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CONVERSION;
import static nikita.common.config.N5ResourceMappings.FORMAT;

/*
 * Used to add ConversionHateoas links with Conversion specific information
 */
@Component("conversionHateoasHandler")
public class ConversionHateoasHandler
        extends SystemIdHateoasHandler
        implements IConversionHateoasHandler {

    public ConversionHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {

        Conversion conversion = (Conversion) entity;
        String parentEntity =
            conversion.getReferenceDocumentObject().getBaseTypeName();
        String parentSystemId =
            conversion.getReferenceDocumentObject().getSystemId();
        String selfhref = getOutgoingAddress() +
            HREF_BASE_FONDS_STRUCTURE + SLASH + parentEntity + SLASH +
            parentSystemId + SLASH + CONVERSION + SLASH + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfhref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfhref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        Conversion conversion = (Conversion) entity;
        addDocumentObject(conversion, hateoasNoarkObject);
        addFormat(conversion, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate
        (ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        Conversion conversion = (Conversion) entity;
        addFormat(conversion, hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the parent DocumentObject associated
     * with the given Conversion
     * <p>
     * "../api/arkivstruktur/dokumentbeskrivelse/{systemId}"
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
                     HREF_BASE_DOCUMENT_OBJECT + SLASH +
                     conversion.getReferenceDocumentObject().getSystemId(),
                     REL_FONDS_STRUCTURE_DOCUMENT_OBJECT));
    }

    @Override
    public void addFormat(IConversionEntity conversion,
                          IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(conversion,
            new Link(getOutgoingAddress() + HREF_BASE_METADATA + SLASH + FORMAT,
                REL_METADATA_FORMAT, true));
    }
}
