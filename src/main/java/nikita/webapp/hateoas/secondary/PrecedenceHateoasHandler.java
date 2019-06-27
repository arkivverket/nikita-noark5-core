package nikita.webapp.handlers.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IPrecedenceHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add PrecedenceHateoas links with Precedence specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for various CRUD operations so keeping them
 * separate calls at the moment.
 */
@Component("precedenceHateoasHandler")
public class PrecedenceHateoasHandler extends HateoasHandler implements IPrecedenceHateoasHandler {

    @Override
    public void addEntityLinks(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        // Metadata RELS
         /*addPrecedenceStatus(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addNewCaseFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addCaseFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addNewPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addNewRegistryEntry(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addRegistryEntry(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject);*/
    }

    @Override
    public void addPrecedenceStatus(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addNewCaseFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_CASE_STATUS, false));
    }

    @Override
    public void addCaseFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addNewPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addNewRegistryEntry(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addRegistryEntry(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

}
