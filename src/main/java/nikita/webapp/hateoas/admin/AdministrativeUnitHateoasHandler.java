package nikita.webapp.hateoas.admin;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.admin.IAdministrativeUnitHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add AdministrativeUnitHateoas links with AdministrativeUnit
 * specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for
 * various CRUD operations so keeping them
 * separate calls at the moment.
 */
@Component("administrativeUnitHateoasHandler")
public class AdministrativeUnitHateoasHandler
        extends SystemIdHateoasHandler
        implements IAdministrativeUnitHateoasHandler {

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        addUser(entity, hateoasNoarkObject);
        addAdministrativeUnit(entity, hateoasNoarkObject);
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
    public void addEntityLinksOnTemplate(
            ISystemId entity,
            IHateoasNoarkObject hateoasNoarkObject) {
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        addEntityLinks(entity, hateoasNoarkObject);
    }

    public void addChildAdministrativeUnit(
            ISystemId entity,
            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_ADMIN + SLASH + ADMINISTRATIVE_UNIT + SLASH + entity.getSystemId() + SLASH + NEW_ADMINISTRATIVE_UNIT + SLASH,
                REL_ADMIN_ADMINISTRATIVE_UNIT, false));
    }

    public void addAdministrativeUnit(INoarkEntity entity,
                                      IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_ADMIN + SLASH + ADMINISTRATIVE_UNIT,
                REL_ADMIN_USER, true));
    }

    public void addUser(INoarkEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_ADMIN + SLASH + USER,
                REL_ADMIN_USER, true));
    }
}
