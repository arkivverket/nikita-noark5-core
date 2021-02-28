package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ISignOffEntity;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.ISignOffHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.config.ODataConstants.DOLLAR_FILTER;

/*
 * Used to add SignOffHateoas links with SignOff specific information
 */
@Component("signOffHateoasHandler")
public class SignOffHateoasHandler
        extends SystemIdHateoasHandler
        implements ISignOffHateoasHandler {

    public SignOffHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfHref = getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + SIGN_OFF + SLASH +
                entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                new Link(selfHref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                new Link(selfHref, entity.getBaseRel()));
        hateoasNoarkObject.addLink(entity,
                new Link(selfHref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                new Link(selfHref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        SignOff signOff = (SignOff) entity;
        addRegistryEntry(signOff, hateoasNoarkObject);
        addReferenceSignedOffRegistryEntry(signOff, hateoasNoarkObject);
        addReferenceSignedOffCorrespondenceParty(signOff, hateoasNoarkObject);
        addSignOffMethod(signOff, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate
            (ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        SignOff signOff = (SignOff) entity;
        addSignOffMethod(signOff, hateoasNoarkObject);
    }

    /**
     * journalpost?$filter=avskrivning/systemID eq '7f000101-7309-1658-8173-09a829470034'
     * or URL encoded:
     * http://localhost:8092/noark5v5/api/sakarkiv/journalpost?$filter%3Davskrivning%2FsystemID+eq+%277f000101-7309-1658-8173-09a829470034%27
     *
     * @param entity
     * @param hateoasNoarkObject
     */
    @Override
    public void addRegistryEntry(ISignOffEntity entity,
                                 IHateoasNoarkObject hateoasNoarkObject) {

        String val = getOutgoingAddress() + HREF_BASE_CASE_HANDLING +
                SLASH + REGISTRY_ENTRY + "?" + urlEncode(DOLLAR_FILTER) + "=" +
                SIGN_OFF + SLASH + SYSTEM_ID +
                urlEncode(" eq '" + entity.getSystemId() + "'");

        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_CASE_HANDLING +
                        SLASH + REGISTRY_ENTRY + "?" + urlEncode(DOLLAR_FILTER)
                        + "=" + SIGN_OFF + SLASH + SYSTEM_ID +
                        urlEncode(" eq '" + entity.getSystemId() + "'"),
                        REL_CASE_HANDLING_REGISTRY_ENTRY));
    }

    @Override
    public void addReferenceSignedOffRegistryEntry(ISignOffEntity entity,
                                                   IHateoasNoarkObject hateoasNoarkObject) {
        if (null != entity.getReferenceSignedOffRecord()) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH +
                            entity.getReferenceSignedOffRecord().getSystemId(),
                            REL_CASE_HANDLING_SIGN_OFF_REFERENCE_RECORD));
        }
    }

    @Override
    public void addReferenceSignedOffCorrespondenceParty
            (ISignOffEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        if (null != entity.getReferenceSignedOffCorrespondencePart()) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_CASE_HANDLING + SLASH + CORRESPONDENCE_PART + SLASH + entity.getReferenceSignedOffCorrespondencePart().getSystemId(),
                            REL_FONDS_STRUCTURE_CORRESPONDENCE_PART));
        }
    }

    @Override
    public void addSignOffMethod(ISignOffEntity entity,
                                 IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + SIGN_OFF_METHOD,
                REL_METADATA_SIGN_OFF_METHOD, false));
    }
}
