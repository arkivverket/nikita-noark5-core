package nikita.webapp.hateoas;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.hateoas.Link;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.interfaces.IBasicRecordHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add BasicRecordHateoas links with BasicRecord specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for various CRUD operations so keeping them
 * separate calls at the moment.
 */
// TODO : Find out about how to handle secondary entities. They should be returned embedded within the primary
//        entity, but updating / adding will require rel/hrefs
//        Temporarily removing displaying secondary entities, but leaving the addNew
//        Commenting out rather than deleting them because we are unsure if they are needed or not
//        It seems that secondary entities are generated as hateoas links if they have odata support
//        So we could reintroduce them when we get odata support
@Component("basicRecordHateoasHandler")
public class BasicRecordHateoasHandler extends RecordHateoasHandler implements IBasicRecordHateoasHandler {

    @Override
    public void addEntityLinks(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {

        super.addEntityLinks(entity, hateoasNoarkObject, outgoingAddress);
        //addStorageLocation(entity, hateoasNoarkObject, outgoingAddress);
        addNewStorageLocation(entity, hateoasNoarkObject, outgoingAddress);
        //addComment(entity, hateoasNoarkObject, outgoingAddress);
        addNewComment(entity, hateoasNoarkObject, outgoingAddress);
        //addAuthor(entity, hateoasNoarkObject, outgoingAddress);
        addNewAuthor(entity, hateoasNoarkObject, outgoingAddress);
        //addCrossReference(entity, hateoasNoarkObject, outgoingAddress);
        addNewCrossReference(entity, hateoasNoarkObject, outgoingAddress);
        //addKeyword(entity, hateoasNoarkObject, outgoingAddress);
        addNewKeyword(entity, hateoasNoarkObject, outgoingAddress);
    }

    @Override
    public void addAuthor(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + AUTHOR + SLASH, REL_FONDS_STRUCTURE_AUTHOR, false));
    }

    @Override
    public void addNewAuthor(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_AUTHOR + SLASH, REL_FONDS_STRUCTURE_NEW_AUTHOR, false));
    }

    @Override
    public void addComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + COMMENT + SLASH, REL_FONDS_STRUCTURE_COMMENT, false));
    }

    @Override
    public void addNewComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_COMMENT + SLASH, REL_FONDS_STRUCTURE_NEW_COMMENT, false));
    }

    @Override
    public void addStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + STORAGE_LOCATION + SLASH, REL_FONDS_STRUCTURE_STORAGE_LOCATION, false));
    }

    @Override
    public void addNewStorageLocation(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_STORAGE_LOCATION + SLASH, REL_FONDS_STRUCTURE_NEW_STORAGE_LOCATION,
                false));
    }

    @Override
    public void addKeyword(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + KEYWORD + SLASH, REL_FONDS_STRUCTURE_KEYWORD, false));
    }

    @Override
    public void addNewKeyword(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_KEYWORD + SLASH, REL_FONDS_STRUCTURE_NEW_KEYWORD, false));
    }

    @Override
    public void addCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + CROSS_REFERENCE + SLASH, REL_FONDS_STRUCTURE_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH +
                BASIC_RECORD + SLASH + entity.getSystemId() + SLASH + NEW_CROSS_REFERENCE + SLASH, REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE, false));
    }
}
