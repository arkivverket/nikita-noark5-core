package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.secondary.CrossReference;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.ICrossReferenceHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;

/**
 * Used to add CrossReferenceHateoas links with CrossReference specific
 * information
 */
@Component("crossReferenceHateoasHandler")
public class CrossReferenceHateoasHandler
        extends SystemIdHateoasHandler
        implements ICrossReferenceHateoasHandler {

    public CrossReferenceHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfhref = getOutgoingAddress() + HREF_BASE_CROSS_REFERENCE +
                "/" + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                new Link(selfhref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                new Link(selfhref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        addRecord(entity, hateoasNoarkObject);
        addFile(entity, hateoasNoarkObject);
        addClass(entity, hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the Record associated with the given
     * CrossReference
     * https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/
     *
     * @param crossReference     CrossReference
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addRecord(ISystemId crossReference,
                          IHateoasNoarkObject hateoasNoarkObject) {
        if (null != ((CrossReference) crossReference).getReferenceRecord()) {
            hateoasNoarkObject.addLink(crossReference,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_RECORD + "/" +
                            ((CrossReference) crossReference)
                                    .getReferenceRecord().getSystemId(),
                            REL_FONDS_STRUCTURE_RECORD));
        }
    }

    /**
     * Create a REL/HREF pair for the Class associated with the given
     * CrossReference
     * https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/
     *
     * @param crossReference     The CrossReference object
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addClass(ISystemId crossReference,
                         IHateoasNoarkObject hateoasNoarkObject) {
        if (null != ((CrossReference) crossReference).getReferenceClass()) {
            hateoasNoarkObject.addLink(crossReference,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_CLASS + "/" +
                            ((CrossReference) crossReference)
                                    .getReferenceClass().getSystemId(),
                            REL_FONDS_STRUCTURE_CLASS));
        }
    }

    /**
     * Create a REL/HREF pair for the File associated with the given
     * CrossReference
     * https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/mappe/
     *
     * @param crossReference     The CrossReference object
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addFile(ISystemId crossReference,
                        IHateoasNoarkObject hateoasNoarkObject) {
        if (null != ((CrossReference) crossReference).getReferenceFile()) {
            hateoasNoarkObject.addLink(crossReference,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_FILE + "" +
                            ((CrossReference) crossReference)
                                    .getReferenceFile().getSystemId(),
                            REL_FONDS_STRUCTURE_FILE));
        }
    }
}
