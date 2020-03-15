package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.ChangeLog;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.IChangeLogEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.IChangeLogHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CHANGE_LOG;

/*
 * Used to add ChangeLogHateoas links with ChangeLog specific information
 */
@Component("changeLogHateoasHandler")
public class ChangeLogHateoasHandler
        extends SystemIdHateoasHandler
        implements IChangeLogHateoasHandler {

    public ChangeLogHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfHref = getOutgoingAddress() +
            HREF_BASE_LOGGING + SLASH + CHANGE_LOG + SLASH + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfHref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfHref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        ChangeLog changeLog = (ChangeLog) entity;
        addReferenceArchiveUnitLink(changeLog, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate
        (ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        ChangeLog changeLog = (ChangeLog) entity;
    }

    protected void addReferenceArchiveUnitLink
        (ChangeLog changeLog, IHateoasNoarkObject hateoasNoarkObject) {
        SystemIdEntity refentity = changeLog.getReferenceArchiveUnit();
        if (null != refentity) {
            String relkey =  refentity.getBaseRel();
            String section = HREF_BASE_FONDS_STRUCTURE;
            // TODO Avoid hack to return correct href
            if (-1 != relkey.indexOf(NOARK_CASE_HANDLING_PATH)) {
                section = HREF_BASE_CASE_HANDLING;
            } else if (-1 != relkey.indexOf(NOARK_ADMINISTRATION_PATH)) {
                section = HREF_BASE_ADMIN;
            }
            String refentityhref = getOutgoingAddress()
                + section + SLASH + refentity.getBaseTypeName()
                + SLASH + refentity.getSystemId();
            hateoasNoarkObject.addLink
                (changeLog, new Link(refentityhref, refentity.getBaseRel()));
        }
    }
}
