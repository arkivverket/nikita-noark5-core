package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.ISystemIdHateoasHandler;
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
public class PrecedenceHateoasHandler
        extends SystemIdHateoasHandler
        implements IPrecedenceHateoasHandler {

    @Override
    public void addEntityLinks(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        // Metadata RELS
         /*addPrecedenceStatus(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addNewCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addNewPrecedence(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addPrecedence(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addNewRegistryEntry(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);
         addRegistryEntry(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject);*/
    }

    @Override
    public void addPrecedenceStatus(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addNewCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + entity.getBaseTypeName(),
                REL_METADATA_CASE_STATUS, false));
    }

    @Override
    public void addCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addNewPrecedence(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addPrecedence(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addNewRegistryEntry(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

    @Override
    public void addRegistryEntry(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        //hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
          //      NOARK_METADATA_PATH + SLASH + entity.getBaseTypeName(), REL_METADATA_, false));
    }

}
