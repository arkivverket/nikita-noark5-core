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

        SignOff signOff = (SignOff) entity;
        String parentEntity =
            signOff.getReferenceRecord().getBaseTypeName();
        String parentSystemId =
            signOff.getReferenceRecord().getSystemId();
        String selfhref = getOutgoingAddress() +
            HREF_BASE_CASE_HANDLING + SLASH + parentEntity + SLASH +
            parentSystemId + SLASH + SIGN_OFF + SLASH + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfhref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfhref, entity.getBaseRel()));
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

    @Override
    public void addRegistryEntry(ISignOffEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
            new Link(getOutgoingAddress() +
                     HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getReferenceRecord().getSystemId(),
                     REL_CASE_HANDLING_REGISTRY_ENTRY));
    }

    @Override
    public void addReferenceSignedOffRegistryEntry(ISignOffEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject) {
        if (null != entity.getReferenceSignedOffRecord()) {
            hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() +
                     HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getReferenceSignedOffRecord().getSystemId(),
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
