package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add FileHateoas links with File specific information
 */
@Component("fileHateoasHandler")
public class FileHateoasHandler
        extends SystemIdHateoasHandler
        implements IFileHateoasHandler {

    public FileHateoasHandler() {
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        addFileLinks(entity, hateoasNoarkObject);

        addExpandToCaseFile(entity, hateoasNoarkObject);
        //addExpandToMeetingFile(entity, hateoasNoarkObject);
        addNewSubFile(entity, hateoasNoarkObject);
    }

    protected void addFileLinks(ISystemId entity,
                                IHateoasNoarkObject hateoasNoarkObject) {
        addEndFile(entity, hateoasNoarkObject);
        addSeries(entity, hateoasNoarkObject);
        addSubFile(entity, hateoasNoarkObject);
        addRecord(entity, hateoasNoarkObject);
        addNewRecord(entity, hateoasNoarkObject);
        addComment(entity, hateoasNoarkObject);
        addNewComment(entity, hateoasNoarkObject);
        addCrossReference(entity, hateoasNoarkObject);
        addNewCrossReference(entity, hateoasNoarkObject);
        addClass(entity, hateoasNoarkObject);
        addNewClass(entity, hateoasNoarkObject);
        addReferenceSeries(entity, hateoasNoarkObject);
        addNewReferenceSeries(entity, hateoasNoarkObject);
        addReferenceSecondaryClassification(entity, hateoasNoarkObject);
        addNewReferenceSecondaryClassification(entity, hateoasNoarkObject);

        addPart(entity, hateoasNoarkObject);
        addNewPartPerson(entity, hateoasNoarkObject);
        addNewPartUnit(entity, hateoasNoarkObject);

        addNewBuilding(entity, hateoasNoarkObject);
        addNewCadastralUnit(entity, hateoasNoarkObject);
        addNewDNumber(entity, hateoasNoarkObject);
        addNewPlan(entity, hateoasNoarkObject);
        addNewPosition(entity, hateoasNoarkObject);
        addNewSocialSecurityNumber(entity, hateoasNoarkObject);
        addNewUnit(entity, hateoasNoarkObject);
        addNationalIdentifier(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(
            ISystemId entity,
            IHateoasNoarkObject hateoasNoarkObject) {
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        addDocumentMedium(entity, hateoasNoarkObject);
        addMetadataFileType(entity, hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the parent Series associated with the given
     * File. Checks if the File is actually associated with a Series. Note every
     * File should actually be associated with a Series, but we are not doing
     * that check here.
     * <p>
     * "../api/arkivstruktur/arkivdel/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/"
     *
     * @param entity             file
     * @param hateoasNoarkObject hateoasFile
     */
    @Override
    public void addSeries(ISystemId entity,
                          IHateoasNoarkObject hateoasNoarkObject) {
        File file = getFile(entity);
        if (file.getReferenceSeries() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_SERIES + SLASH +
                            file.getReferenceSeries().getSystemId(),
                            REL_FONDS_STRUCTURE_SERIES, true));
        }
    }

    /**
     * Create a REL/HREF pair for the parent Class associated with the given
     * File. Checks if the File is actually associated with a Class.
     * <p>
     * "../api/arkivstruktur/klasse/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/"
     *
     * @param entity             file
     * @param hateoasNoarkObject hateoasFile
     */
    @Override
    public void addClass(ISystemId entity,
                         IHateoasNoarkObject hateoasNoarkObject) {
        File file = getFile(entity);
        if (file.getReferenceClass() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_CLASS + SLASH + file.getReferenceClass().getSystemId(),
                    REL_FONDS_STRUCTURE_CLASS, true));
        }
    }

    @Override
    public void addEndFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + FILE_END + SLASH,
                REL_FONDS_STRUCTURE_END_FILE,
                false));
    }

    @Override
    public void addExpandToCaseFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + FILE_EXPAND_TO_CASE_FILE + SLASH,
                REL_FONDS_STRUCTURE_EXPAND_TO_CASE_FILE, false));
    }

    @Override
    public void addExpandToMeetingFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + FILE_EXPAND_TO_MEETING_FILE + SLASH,
                REL_FONDS_STRUCTURE_EXPAND_TO_MEETING_FILE, false));
    }

    @Override
    public void addRecord(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + RECORD + SLASH,
                REL_FONDS_STRUCTURE_RECORD, false));
    }

    @Override
    public void addNewRecord(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_RECORD + SLASH,
                REL_FONDS_STRUCTURE_NEW_RECORD, false));
    }

    @Override
    public void addComment(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + COMMENT + SLASH,
                REL_FONDS_STRUCTURE_COMMENT, false));
    }

    @Override
    public void addNewComment(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_COMMENT + SLASH,
                REL_FONDS_STRUCTURE_NEW_COMMENT, false));
    }

    @Override
    public void addParentFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        File file = getFile(entity).getReferenceParentFile();
        if (file != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_FILE + SLASH + file.getSystemId(),
                    REL_FONDS_STRUCTURE_PARENT_FILE));
        }
    }

    @Override
    public void addSubFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + SUB_FILE + SLASH,
                REL_FONDS_STRUCTURE_SUB_FILE, false));
    }

    @Override
    public void addNewSubFile(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_FILE,
                REL_FONDS_STRUCTURE_NEW_FILE, false));
    }

    /**
     * Create a REL/HREF pair for the list of Part objects associated with the
     * given File.
     * <p>
     * "../api/arkivstruktur/mappe/1234/part"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/part/"
     *
     * @param entity             file
     * @param hateoasNoarkObject hateoasFile
     */
    @Override
    public void addPart(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_FILE + SLASH +
                        entity.getSystemId() + SLASH + PART + SLASH,
                        REL_FONDS_STRUCTURE_PART, true));
    }

    @Override
    public void addNewPartPerson(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_FILE + SLASH +
                        entity.getSystemId() + SLASH + NEW_PART_PERSON + SLASH,
                        REL_FONDS_STRUCTURE_NEW_PART_PERSON));
    }

    @Override
    public void addNewPartUnit(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_FILE + SLASH +
                        entity.getSystemId() + SLASH + NEW_PART_UNIT + SLASH,
                        REL_FONDS_STRUCTURE_NEW_PART_UNIT));
    }

    @Override
    public void addCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewCrossReference(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewClass(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_CLASS + SLASH,
                REL_FONDS_STRUCTURE_NEW_CLASS, false));
    }

    @Override
    public void addReferenceSeries(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + SERIES + SLASH,
                REL_FONDS_STRUCTURE_REFERENCE_SERIES, false));
    }

    @Override
    public void addNewReferenceSeries(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + SERIES + SLASH,
                REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES, false));
    }

    @Override
    public void addReferenceSecondaryClassification(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + SECONDARY_CLASSIFICATION + SLASH,
                REL_CASE_HANDLING_SECONDARY_CLASSIFICATION, false));
    }

    @Override
    public void addNewReferenceSecondaryClassification(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_SECONDARY_CLASSIFICATION_SYSTEM + SLASH,
                REL_CASE_HANDLING_NEW_SECONDARY_CLASSIFICATION, false));
    }

    public void addMetadataFileType(ISystemId entity,
                                    IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + FILE_TYPE,
                REL_METADATA_FILE_TYPE, false));
    }

    @Override
    public void addNewBuilding(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_BUILDING, REL_FONDS_STRUCTURE_NEW_BUILDING));
    }

    @Override
    public void addNewCadastralUnit(ISystemId entity,
                                    IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_CADASTRAL_UNIT, REL_FONDS_STRUCTURE_NEW_CADASTRAL_UNIT));
    }

    @Override
    public void addNewDNumber(ISystemId entity,
                              IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_D_NUMBER, REL_FONDS_STRUCTURE_NEW_D_NUMBER));
    }

    @Override
    public void addNewPlan(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_PLAN, REL_FONDS_STRUCTURE_NEW_PLAN));
    }

    @Override
    public void addNewPosition(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_POSITION, REL_FONDS_STRUCTURE_NEW_POSITION));
    }

    @Override
    public void addNewSocialSecurityNumber(
            ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_SOCIAL_SECURITY_NUMBER,
                REL_FONDS_STRUCTURE_NEW_SOCIAL_SECURITY_NUMBER));
    }

    @Override
    public void addNewUnit(ISystemId entity,
                           IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_NI_UNIT, REL_FONDS_STRUCTURE_NEW_NI_UNIT));
    }

    @Override
    public void addNationalIdentifier(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NATIONAL_IDENTIFIER,
                REL_FONDS_STRUCTURE_NATIONAL_IDENTIFIER));
    }

    /**
     * Cast the ISystemId entity to a File
     *
     * @param entity the File
     * @return a File object
     */
    private File getFile(@NotNull ISystemId entity) {
        return (File) entity;
    }
}
