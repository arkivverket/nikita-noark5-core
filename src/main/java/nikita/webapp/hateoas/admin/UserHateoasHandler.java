package nikita.webapp.hateoas.admin;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.hateoas.Link;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.interfaces.admin.IUserHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
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
        extends HateoasHandler
        implements IUserHateoasHandler {

    @Override
    public void addEntityLinks(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        addAdministrativeUnit(entity, hateoasNoarkObject, outgoingAddress);
        addNewAdministrativeUnit(entity, hateoasNoarkObject, outgoingAddress);
    }

    @Override
    public void addEntityLinksOnCreate(INikitaEntity entity,
                                       IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        addEntityLinks(entity, hateoasNoarkObject, outgoingAddress);
    }

    @Override
    public void addEntityLinksOnRead(INikitaEntity entity,
                                     IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        addEntityLinks(entity, hateoasNoarkObject, outgoingAddress);
    }

    public void addNewAdministrativeUnit(INikitaEntity entity,
                                         IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity,
                new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                        NOARK_ADMINISTRATION_PATH + SLASH + entity.getSystemId() + SLASH +
                        NEW_ADMINISTRATIVE_UNIT + SLASH,
                        REL_ADMIN_NEW_ADMINISTRATIVE_UNIT, false));
    }

    public void addAdministrativeUnit(INikitaEntity entity,
                                      IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity,
                new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                        NOARK_ADMINISTRATION_PATH + SLASH + entity
                        .getSystemId() + SLASH + ADMINISTRATIVE_UNIT +
                        SLASH, REL_ADMIN_ADMINISTRATIVE_UNIT, false));
    }
}
