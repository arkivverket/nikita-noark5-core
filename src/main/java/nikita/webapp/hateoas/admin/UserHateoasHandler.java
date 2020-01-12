package nikita.webapp.hateoas.admin;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.admin.IUserHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.USER;
import static nikita.common.config.N5ResourceMappings.ADMINISTRATIVE_UNIT;
import static nikita.common.config.N5ResourceMappings.NEW_ADMINISTRATIVE_UNIT;

/**
 * Created by tsodring on 06/08/17.
 * <p>
 * Used to add UserHateoas links with User specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for
 * various CRUD operations so keeping them separate calls at the moment.
 */
@Component("userHateoasHandler")
public class UserHateoasHandler
        extends SystemIdHateoasHandler
        implements IUserHateoasHandler {

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        addAdministrativeUnit(entity, hateoasNoarkObject);
        addNewAdministrativeUnit(entity, hateoasNoarkObject);
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

    public void addNewAdministrativeUnit(ISystemId entity,
                                         IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_ADMIN + SLASH + USER + SLASH + entity.getSystemId() + SLASH + NEW_ADMINISTRATIVE_UNIT + SLASH,
                REL_ADMIN_NEW_ADMINISTRATIVE_UNIT, false));
    }

    public void addAdministrativeUnit(ISystemId entity,
                                      IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
		HREF_BASE_ADMIN + SLASH + USER + SLASH + entity.getSystemId() + SLASH + ADMINISTRATIVE_UNIT + SLASH,
                REL_ADMIN_ADMINISTRATIVE_UNIT, false));
    }
}
