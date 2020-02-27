package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.IFondsHateoasHandler;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add FondsHateoas links with Fonds specific information
 */
@Component
public class FondsHateoasHandler
        extends SystemIdHateoasHandler
        implements IFondsHateoasHandler {

    public FondsHateoasHandler() {
        super();
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        // link for object identity
        addFonds(entity, hateoasNoarkObject);
        // links for child entities
        addSeries(entity, hateoasNoarkObject);
        addNewSeries(entity, hateoasNoarkObject);
        addSubFonds(entity, hateoasNoarkObject);
        addNewSubFonds(entity, hateoasNoarkObject);
        // links for parent entities
        addParentFonds(entity, hateoasNoarkObject);
        addFondsCreator(entity, hateoasNoarkObject);
        addNewFondsCreator(entity, hateoasNoarkObject);
        // links for secondary entities
        // links for metadata entities
        addDocumentMedium(entity, hateoasNoarkObject);
        addFondsStatus(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnCreate(ISystemId entity,
                                       IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnRead(ISystemId entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(ISystemId entity,
                                         IHateoasNoarkObject hateoasNoarkObject) {
        addDocumentMedium(entity, hateoasNoarkObject);
        addFondsStatus(entity, hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the list of FondsCreator associated with the
     * given Fonds. Checks if the Fonds has any FondsCreator associated with
     * it.
     * <p>
     * "../api/arkivstruktur/arkiv/1234/arkivskaper"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivskaper/"
     *
     * @param entity             fonds
     * @param hateoasNoarkObject hateoasFonds
     */
    @Override
    public void addFondsCreator(ISystemId entity,
                                IHateoasNoarkObject hateoasNoarkObject) {
        if (getFonds(entity).getReferenceFondsCreator().size() > 0) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_FONDS + SLASH + entity.getSystemId() + SLASH + FONDS_CREATOR,
                    REL_FONDS_STRUCTURE_FONDS_CREATOR, true));
        }
    }

    /**
     * Create a REL/HREF pair for the list of Series associated with the
     * given Fonds. Checks if the Fonds has any Series associated with it.
     * <p>
     * "../api/arkivstruktur/arkiv/1234/arkivdel"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/"
     *
     * @param entity             fonds
     * @param hateoasNoarkObject hateoasFonds
     */
    @Override
    public void addSeries(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject) {
        if (getFonds(entity).getReferenceSeries() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_FONDS + SLASH + entity.getSystemId() + SLASH + SERIES,
                    REL_FONDS_STRUCTURE_SERIES, true));
        }
    }

    /**
     * Create a REL/HREF pair for the given Fonds associated. This is the
     * equivalent to the self rel, but in addition identifies the object type.
     * <p>
     * "../api/arkivstruktur/arkiv/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/"
     *
     * @param entity             fonds
     * @param hateoasNoarkObject hateoasFonds
     */
    @Override
    public void addFonds(ISystemId entity,
                         IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FONDS + SLASH + entity.getSystemId(),
                REL_FONDS_STRUCTURE_FONDS, true));
    }

    /**
     * Create a REL/HREF pair for the list of sub (fonds) associated with the
     * given Fonds. Checks if the Fonds is actually associated with any sub
     * fonds.
     * <p>
     * "../api/arkivstruktur/arkiv/1234/underarkiv"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/underarkiv/"
     *
     * @param entity             fonds
     * @param hateoasNoarkObject hateoasFonds
     */
    @Override
    public void addSubFonds(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FONDS + SLASH + entity.getSystemId() + SLASH + SUB_FONDS,
                REL_FONDS_STRUCTURE_SUB_FONDS, true));
    }

    /**
     * Create a REL/HREF pair to create a new Series associated with the
     * given Fonds. This link should only be generated if the user is
     * authorised to create a Series.
     * <p>
     * "../api/arkivstruktur/arkiv/1234/ny-arkivdel"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-arkivdel/"
     *
     * @param entity             fonds
     * @param hateoasNoarkObject hateoasFonds
     */
    @Override
    public void addNewSeries(ISystemId entity,
                             IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FONDS + SLASH + entity.getSystemId() + SLASH + NEW_SERIES,
                REL_FONDS_STRUCTURE_NEW_SERIES));
    }

    /**
     * Create a REL/HREF pair to create a new FondsCreator associated with the
     * given Fonds. This link should only be generated if the user is
     * authorised to create a FondsCreator.
     * <p>
     * "../api/arkivstruktur/arkiv/1234/ny-arkivskaper"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-arkivskaper/"
     *
     * @param entity             fonds
     * @param hateoasNoarkObject hateoasFonds
     */
    @Override
    public void addNewFondsCreator(ISystemId entity,
                                   IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FONDS + SLASH + entity.getSystemId() + SLASH +
                NEW_FONDS_CREATOR, REL_FONDS_STRUCTURE_NEW_FONDS_CREATOR));
    }

    /**
     * Create a REL/HREF pair to create a new (sub) Fonds associated with
     * the given Fonds. This link should only be generated if the user is
     * authorised to create a sub fonds.
     * <p>
     * "../api/arkivstruktur/arkiv/1234/ny-arkiv"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/ny-arkiv/"
     *
     * @param entity             fonds
     * @param hateoasNoarkObject hateoasFonds
     */
    @Override
    public void addNewSubFonds(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FONDS + SLASH + entity.getSystemId() + SLASH + NEW_FONDS,
                REL_FONDS_STRUCTURE_NEW_FONDS));
    }

    /**
     * Create a REL/HREF pair to get parent Fonds associated with
     * the given Fonds. Checks if parent fonds exists first.
     * <p>
     * "../api/arkivstruktur/arkiv/9856"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/forelderarkiv/"
     *
     * @param entity             fonds
     * @param hateoasNoarkObject hateoasFonds
     */
    @Override
    public void addParentFonds(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        Fonds fonds = getFonds(entity).getReferenceParentFonds();
        if (fonds != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_FONDS + SLASH + fonds.getSystemId(),
                    REL_FONDS_STRUCTURE_PARENT_FONDS));
        }
    }

    /**
     * Create a REL/HREF pair for the Series associated with the
     * given Fonds. Checks if the Fonds is actually associated with a
     * Series.
     * <p>
     * "../api/metadata/arkivstatus"
     * "https://rel.arkivverket.no/noark5/v5/api/metadata/arkivstatus/"
     *
     * @param entity             fonds
     * @param hateoasNoarkObject hateoasFonds
     */
    @Override
    public void addFondsStatus(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + FONDS_STATUS,
                REL_METADATA_FONDS_STATUS, true));
    }

    // Internal helper methods

    /**
     * Cast the ISystemId entity to a Fonds
     *
     * @param entity the Fonds
     * @return a Fonds object
     */
    private Fonds getFonds(@NotNull ISystemId entity) {
        return (Fonds) entity;
    }
}
