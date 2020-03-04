package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
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
        extends SystemIdHateoasHandler
        implements IClassHateoasHandler {

    public ClassHateoasHandler() {
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {

        // links for primary entities
        addClass(entity, hateoasNoarkObject);
        addNewClass(entity, hateoasNoarkObject);
        addRegistration(entity, hateoasNoarkObject);
        addNewRegistration(entity, hateoasNoarkObject);
        addFile(entity, hateoasNoarkObject);
        addNewFile(entity, hateoasNoarkObject);
        addNewCaseFile(entity, hateoasNoarkObject);
        addClassificationSystem(entity, hateoasNoarkObject);
        addParentClass(entity, hateoasNoarkObject);
        addSubClass(entity, hateoasNoarkObject);
        // links for secondary entities (non-embeddable)
        addCrossReference(entity, hateoasNoarkObject);
        addNewCrossReference(entity, hateoasNoarkObject);
        // links for metadata entities
        addAccessRestriction(entity,hateoasNoarkObject);
        addDisposalDecision(entity,hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(ISystemId entity,
                                         IHateoasNoarkObject hateoasNoarkObject) {
        addAccessRestriction(entity,hateoasNoarkObject);
        addDisposalDecision(entity,hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the parent ClassificationSystem associated
     * with the given Class. Checks if the Class is actually associated with
     * a ClassificationSystem. Note every Class should actually be associated
     * with a ClassificationSystem, but we are not doing that check here.
     * <p>
     * "../api/arkivstruktur/klassifikasjonssystem/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/"
     *
     * @param entity             class
     * @param hateoasNoarkObject hateoasClass
     */
    @Override
    public void addClassificationSystem(ISystemId entity,
                                        IHateoasNoarkObject hateoasNoarkObject) {
        Class klass = getClass(entity);
        if (klass.getReferenceClassificationSystem() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_CLASSIFICATION_SYSTEM + SLASH + klass.getReferenceClassificationSystem().getSystemId(),
                    REL_FONDS_STRUCTURE_CLASSIFICATION_SYSTEM));
        }
    }

    /**
     * Create a REL/HREF pair for the parent Class associated with the given
     * Class. Checks if the Class is actually associated with a Class.
     * <p>
     * "../api/arkivstruktur/klasse/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/"
     *
     * @param entity             class
     * @param hateoasNoarkObject hateoasClass
     */
    @Override
    public void addClass(ISystemId entity,
                         IHateoasNoarkObject hateoasNoarkObject) {
        Class klass = getClass(entity);
        if (klass.getReferenceParentClass() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_CLASS + SLASH + klass.getReferenceParentClass().getSystemId(),
                    REL_FONDS_STRUCTURE_CLASS, true));
        }
    }

    @Override
    public void addNewClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CLASS + SLASH + entity.getSystemId() + SLASH + NEW_CLASS + SLASH,
                REL_FONDS_STRUCTURE_NEW_CLASS, false));
    }

    @Override
    public void addParentClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        Class cls = getClass(entity).getReferenceParentClass();
        if (cls != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_CLASS + SLASH + cls.getSystemId(),
                    REL_FONDS_STRUCTURE_PARENT_CLASS, false));
        }
    }

    @Override
    public void addSubClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CLASS + SLASH + entity.getSystemId() + SLASH + SUB_CLASS + SLASH,
                REL_FONDS_STRUCTURE_SUB_CLASS, false));
    }

    @Override
    public void addRegistration(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CLASS + SLASH + entity.getSystemId() + SLASH + RECORD + SLASH,
                REL_FONDS_STRUCTURE_RECORD, false));
    }

    @Override
    public void addNewRegistration(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CLASS + SLASH + entity.getSystemId() + SLASH + NEW_RECORD + SLASH,
                REL_FONDS_STRUCTURE_NEW_RECORD, false));
    }

    @Override
    public void addFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CLASS + SLASH + entity.getSystemId() + SLASH + FILE + SLASH,
                REL_FONDS_STRUCTURE_FILE, false));
    }

    @Override
    public void addNewFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CLASS + SLASH + entity.getSystemId() + SLASH + NEW_FILE + SLASH,
                REL_FONDS_STRUCTURE_NEW_FILE, false));
    }

    @Override
    public void addNewCaseFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CLASS + SLASH + entity.getSystemId() + SLASH + NEW_CASE_FILE + SLASH,
                REL_CASE_HANDLING_NEW_CASE_FILE, false));
    }

    @Override
    public void addCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CLASS + SLASH + entity.getSystemId() + SLASH + CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CLASS + SLASH + entity.getSystemId() + SLASH + NEW_CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE, false));
    }

    @Override
    public void addAccessRestriction(ISystemId entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + ACCESS_RESTRICTION,
                REL_METADATA_ACCESS_RESTRICTION, false));
    }

    @Override
    public void addDisposalDecision(ISystemId entity,
                                    IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + DISPOSAL_DECISION,
                REL_METADATA_DISPOSAL_DECISION, false));
    }

    /**
     * Cast the ISystemId entity to a Class
     *
     * @param entity the Class
     * @return a Class object
     */
    private Class getClass(@NotNull ISystemId entity) {
        return (Class) entity;
    }
}
