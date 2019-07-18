package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.interfaces.IClassHateoasHandler;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add ClassHateoas links with Class specific information
 */
@Component("classHateoasHandler")
public class ClassHateoasHandler
        extends HateoasHandler
        implements IClassHateoasHandler {

    public ClassHateoasHandler() {
    }

    @Override
    public void addEntityLinks(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {

        // links for primary entities
        addClass(entity, hateoasNoarkObject);
        addNewClass(entity, hateoasNoarkObject);
        addRegistration(entity, hateoasNoarkObject);
        addNewRegistration(entity, hateoasNoarkObject);
        addFile(entity, hateoasNoarkObject);
        addNewFile(entity, hateoasNoarkObject);
        addClassificationSystem(entity, hateoasNoarkObject);
        addParentClass(entity, hateoasNoarkObject);
        addSubClass(entity, hateoasNoarkObject);
        // links for secondary entities (non-embeddable)
        addKeyword(entity, hateoasNoarkObject);
        addNewKeyword(entity, hateoasNoarkObject);
        addCrossReference(entity, hateoasNoarkObject);
        addNewCrossReference(entity, hateoasNoarkObject);
        // links for secondary entities (embeddable)
        addNewClassified(entity, hateoasNoarkObject);
        addNewDisposal(entity, hateoasNoarkObject);
        addNewDisposalUndertaken(entity, hateoasNoarkObject);
        addNewDeletion(entity, hateoasNoarkObject);
        addNewScreening(entity, hateoasNoarkObject);
        // links for metadata entities
        // Class has no metadata entities
    }

    /**
     * Create a REL/HREF pair for the parent ClassificationSystem associated
     * with the given Class. Checks if the Class is actually associated with
     * a ClassificationSystem. Note every Class should actually be associated
     * with a ClassificationSystem, but we are not doing that check here.
     * <p>
     * "../hateoas-api/arkivstruktur/klassifikasjonssystem/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/"
     *
     * @param entity             class
     * @param hateoasNoarkObject hateoasClass
     */
    @Override
    public void addClassificationSystem(INikitaEntity entity,
                                        IHateoasNoarkObject hateoasNoarkObject) {
        Class klass = getClass(entity);
        if (klass.getReferenceClassificationSystem() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_CLASSIFICATION_SYSTEM +
                            klass.getReferenceClassificationSystem().getSystemId(),
                            REL_FONDS_STRUCTURE_CLASSIFICATION_SYSTEM));
        }
    }

    /**
     * Create a REL/HREF pair for the parent Class associated with the given
     * Class. Checks if the Class is actually associated with a Class.
     * <p>
     * "../hateoas-api/arkivstruktur/klasse/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/"
     *
     * @param entity             class
     * @param hateoasNoarkObject hateoasClass
     */
    @Override
    public void addClass(INikitaEntity entity,
                         IHateoasNoarkObject hateoasNoarkObject) {
        Class klass = getClass(entity);
        if (klass.getReferenceParentClass() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_CLASS +
                            klass.getReferenceParentClass().getSystemId(),
                            REL_FONDS_STRUCTURE_CLASS, true));
        }
    }

    @Override
    public void addNewClass(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + NEW_SUB_CLASS +
                SLASH, REL_FONDS_STRUCTURE_NEW_SUB_CLASS, false));
    }

    @Override
    public void addParentClass(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + PARENT_CLASS +
                SLASH, REL_FONDS_STRUCTURE_PARENT_CLASS, false));
    }

    @Override
    public void addSubClass(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + SUB_CLASS +
                SLASH, REL_FONDS_STRUCTURE_SUB_CLASS, false));
    }

    @Override
    public void addRegistration(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + RECORD
                + SLASH, REL_FONDS_STRUCTURE_RECORD,
                false));
    }

    @Override
    public void addNewRegistration(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + NEW_RECORD
                + SLASH, REL_FONDS_STRUCTURE_NEW_RECORD,
                false));
    }

    @Override
    public void addFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + FILE +
                SLASH, REL_FONDS_STRUCTURE_FILE,
                false));
    }

    @Override
    public void addNewFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + NEW_FILE +
                SLASH, REL_FONDS_STRUCTURE_NEW_FILE,
                false));
    }

    @Override
    public void addNewClassified(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + NEW_CLASSIFIED
                + SLASH, REL_FONDS_STRUCTURE_NEW_CLASSIFIED, false));
    }

    @Override
    public void addNewDisposal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + NEW_DISPOSAL
                + SLASH, REL_FONDS_STRUCTURE_NEW_DISPOSAL, false));
    }

    @Override
    public void addNewDisposalUndertaken(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH +
                NEW_DISPOSAL_UNDERTAKEN + SLASH, REL_FONDS_STRUCTURE_NEW_DISPOSAL_UNDERTAKEN, false));
    }

    @Override
    public void addNewDeletion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + NEW_DELETION
                + SLASH, REL_FONDS_STRUCTURE_NEW_DELETION, false));
    }


    @Override
    public void addNewScreening(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + NEW_SCREENING
                + SLASH, REL_FONDS_STRUCTURE_NEW_SCREENING, false));
    }

    @Override
    public void addKeyword(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + KEYWORD
                + SLASH, REL_FONDS_STRUCTURE_KEYWORD, false));
    }

    @Override
    public void addNewKeyword(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH + NEW_KEYWORD
                + SLASH, REL_FONDS_STRUCTURE_NEW_KEYWORD, false));
    }

    @Override
    public void addCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH +
                CROSS_REFERENCE + SLASH, REL_FONDS_STRUCTURE_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS + SLASH + entity.getSystemId() + SLASH +
                NEW_CROSS_REFERENCE + SLASH, REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE, false));
    }

    /**
     * Cast the INikitaEntity entity to a Class
     *
     * @param entity the Class
     * @return a Class object
     */
    private Class getClass(@NotNull INikitaEntity entity) {
        return (Class) entity;
    }
}
