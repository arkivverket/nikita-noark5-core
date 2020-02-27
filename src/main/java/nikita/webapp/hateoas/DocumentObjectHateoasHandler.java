package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.IDocumentObjectHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add DocumentObjectHateoas links with DocumentObject specific
 * information
 */
@Component("documentObjectHateoasHandler")
public class DocumentObjectHateoasHandler
        extends SystemIdHateoasHandler
        implements IDocumentObjectHateoasHandler {

    public DocumentObjectHateoasHandler() {
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {

        // links for primary entities
        addDocumentDescription(entity, hateoasNoarkObject);
        // links for secondary entities
        addConversion(entity, hateoasNoarkObject);
        addNewConversion(entity, hateoasNoarkObject);
        addReferenceDocumentFile(entity, hateoasNoarkObject);
        // links for metadata entities
        addVariantFormat(entity, hateoasNoarkObject);
        addFormat(entity, hateoasNoarkObject);
        addConvertFile(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(ISystemId entity,
                                         IHateoasNoarkObject hateoasNoarkObject) {
        addVariantFormat(entity, hateoasNoarkObject);
    }

    @Override
    public void addConversion(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_OBJECT + SLASH + entity.getSystemId() + SLASH + CONVERSION + SLASH,
                REL_FONDS_STRUCTURE_CONVERSION, false));
    }

    @Override
    public void addNewConversion(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_OBJECT + SLASH + entity.getSystemId() + SLASH + NEW_CONVERSION + SLASH,
                REL_FONDS_STRUCTURE_NEW_CONVERSION, false));
    }

    /**
     * Create a REL/HREF pair for the parent documentDescription associated
     * with the given DocumentObject
     * <p>
     * "../api/arkivstruktur/dokumentbeskrivelse/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/"
     *
     * @param entity             documentObject
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addDocumentDescription(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_DESCRIPTION + SLASH + getDocumentDescriptionSystemId(entity),
                REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION));
    }

    @Override
    public void addReferenceDocumentFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_OBJECT + SLASH + entity.getSystemId() + SLASH + REFERENCE_FILE + SLASH,
                REL_FONDS_STRUCTURE_DOCUMENT_FILE, false));
    }

    @Override
    public void addConvertFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_DOCUMENT_OBJECT + SLASH + entity.getSystemId() + SLASH + CONVERT_FILE,
                REL_FONDS_STRUCTURE_CONVERT_FILE, false));
    }

    @Override
    public void addVariantFormat(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + VARIANT_FORMAT,
                REL_METADATA_VARIANT_FORMAT, false));
    }

    @Override
    public void addFormat(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + FORMAT,
                REL_METADATA_FORMAT, false));
    }

    // Internal helper methods

    /**
     * Cast the ISystemId entity to a DocumentObject and retrieve the
     * systemId of the associated DocumentDescription
     *
     * @param entity the DocumentObject
     * @return systemId of the associated DocumentDescription
     */
    private String getDocumentDescriptionSystemId(ISystemId entity) {
        if (((DocumentObject) entity).getReferenceDocumentDescription()
                != null) {
            return ((DocumentObject) entity).getReferenceDocumentDescription()
                    .getSystemId();
        }
        return null;
    }
}
