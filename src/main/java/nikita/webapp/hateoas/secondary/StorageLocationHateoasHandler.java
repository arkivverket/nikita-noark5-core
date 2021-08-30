package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IStorageLocationHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.STORAGE_LOCATION;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static nikita.common.config.ODataConstants.DOLLAR_FILTER;

/**
 * Used to add StorageLocationHateoas links with StorageLocation specific
 * information
 */
@Component("storageLocationHateoasHandler")
public class StorageLocationHateoasHandler
        extends SystemIdHateoasHandler
        implements IStorageLocationHateoasHandler {

    public StorageLocationHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfhref = getOutgoingAddress() + HREF_BASE_FONDS_STRUCTURE +
                SLASH + STORAGE_LOCATION + SLASH + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                new Link(selfhref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                new Link(selfhref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        addFonds(entity, hateoasNoarkObject);
        addSeries(entity, hateoasNoarkObject);
        addFile(entity, hateoasNoarkObject);
        addRecordEntity(entity, hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the parent Fonds associated
     * with the given StorageLocation
     * <p>
     * arkiv?$filter=oppbevaringssted/systemID eq '{systemID}'
     * https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/
     *
     * @param storageLocation    The StorageLocation object
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addFonds(ISystemId storageLocation,
                         IHateoasNoarkObject hateoasNoarkObject) {
        if (((StorageLocation) storageLocation).getReferenceFonds().size() > 0) {
            hateoasNoarkObject.addLink(storageLocation,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_FONDS + "?" + urlEncode(DOLLAR_FILTER) +
                            "=" + STORAGE_LOCATION + SLASH + SYSTEM_ID +
                            urlEncode(" eq '" + storageLocation.getSystemId() + "'"),
                            REL_FONDS_STRUCTURE_FONDS));
        }
    }

    /**
     * Create a REL/HREF pair for the parent Series associated
     * with the given StorageLocation
     * <p>
     * arkivdel?$filter=oppbevaringssted/systemID eq '{systemID}'
     * https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/
     *
     * @param storageLocation    The StorageLocation object
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addSeries(ISystemId storageLocation,
                          IHateoasNoarkObject hateoasNoarkObject) {
        if (((StorageLocation) storageLocation).getReferenceSeries().size() > 0) {
            hateoasNoarkObject.addLink(storageLocation,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_SERIES + "?" + urlEncode(DOLLAR_FILTER) +
                            "=" + STORAGE_LOCATION + SLASH + SYSTEM_ID +
                            urlEncode(" eq '" + storageLocation.getSystemId() + "'"),
                            REL_FONDS_STRUCTURE_SERIES));
        }
    }

    /**
     * Create a REL/HREF pair for the parent File associated
     * with the given StorageLocation
     * <p>
     * mappe?$filter=oppbevaringssted/systemID eq '{systemID}'
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/mappe/"
     *
     * @param storageLocation    The StorageLocation object
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addFile(ISystemId storageLocation,
                        IHateoasNoarkObject hateoasNoarkObject) {
        if (((StorageLocation) storageLocation).getReferenceFile().size() > 0) {
            hateoasNoarkObject.addLink(storageLocation,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_FILE + "?" + urlEncode(DOLLAR_FILTER) +
                            "=" + STORAGE_LOCATION + SLASH + SYSTEM_ID +
                            urlEncode(" eq '" + storageLocation.getSystemId() + "'"),
                            REL_FONDS_STRUCTURE_FILE));
        }
    }

    /**
     * Create a REL/HREF pair for the parent Record associated with the given
     * StorageLocation
     * registrering?$filter=oppbevaringssted/systemID eq '{systemID}'
     * <p>
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/"
     *
     * @param storageLocation    StorageLocation
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addRecordEntity(ISystemId storageLocation,
                                IHateoasNoarkObject hateoasNoarkObject) {
        if (((StorageLocation) storageLocation).getReferenceRecordEntity().size() > 0) {
            hateoasNoarkObject.addLink(storageLocation,
                    new Link(getOutgoingAddress() +
                            "?" + urlEncode(DOLLAR_FILTER) +
                            "=" + STORAGE_LOCATION + SLASH + SYSTEM_ID +
                            urlEncode(" eq '" + storageLocation.getSystemId() + "'"),
                            REL_FONDS_STRUCTURE_RECORD));
        }
    }
}
