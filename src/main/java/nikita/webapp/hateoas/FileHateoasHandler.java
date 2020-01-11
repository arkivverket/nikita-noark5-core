package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
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
        extends HateoasHandler
        implements IFileHateoasHandler {

    public FileHateoasHandler() {
    }

    @Override
    public void addEntityLinks(INoarkEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject) {

        // Add the child links
        addRecord(entity, hateoasNoarkObject);
        addNewRecord(entity, hateoasNoarkObject);
        // Add the parent links
        addSeries(entity, hateoasNoarkObject);
        // Add action links
        addEndFile(entity, hateoasNoarkObject);
        addExpandToCaseFile(entity, hateoasNoarkObject);
        //addExpandToMeetingFile(entity, hateoasNoarkObject);
        // Add the secondary entity links
        addPart(entity, hateoasNoarkObject);
        addNewPartPerson(entity, hateoasNoarkObject);
        addNewPartUnit(entity, hateoasNoarkObject);
        addComment(entity, hateoasNoarkObject);
        addNewComment(entity, hateoasNoarkObject);
        addSubFile(entity, hateoasNoarkObject);
        addNewSubFile(entity, hateoasNoarkObject);
        addCrossReference(entity, hateoasNoarkObject);
        addNewCrossReference(entity, hateoasNoarkObject);
        addClass(entity, hateoasNoarkObject);
        addNewClass(entity, hateoasNoarkObject);
        addReferenceSeries(entity, hateoasNoarkObject);
        addNewReferenceSeries(entity, hateoasNoarkObject);
        addReferenceSecondaryClassification(entity, hateoasNoarkObject);
        addNewReferenceSecondaryClassification(entity, hateoasNoarkObject);
        // Add national identifiers
        addNewBuilding(entity, hateoasNoarkObject);
        addNewCadastralUnit(entity, hateoasNoarkObject);
        addNewDNumber(entity, hateoasNoarkObject);
        addNewPlan(entity, hateoasNoarkObject);
        addNewPosition(entity, hateoasNoarkObject);
        addNewSocialSecurityNumber(entity, hateoasNoarkObject);
        addNewUnit(entity, hateoasNoarkObject);
        addBuilding(entity, hateoasNoarkObject);
        addCadastralUnit(entity, hateoasNoarkObject);
        addDNumber(entity, hateoasNoarkObject);
        addPlan(entity, hateoasNoarkObject);
        addPosition(entity, hateoasNoarkObject);
        addSocialSecurityNumber(entity, hateoasNoarkObject);
        addUnit(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(
            INoarkEntity entity,
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
     * "../hateoas-api/arkivstruktur/arkivdel/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/"
     *
     * @param entity             file
     * @param hateoasNoarkObject hateoasFile
     */
    @Override
    public void addSeries(INoarkEntity entity,
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
     * "../hateoas-api/arkivstruktur/klasse/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/"
     *
     * @param entity             file
     * @param hateoasNoarkObject hateoasFile
     */
    @Override
    public void addClass(INoarkEntity entity,
                         IHateoasNoarkObject hateoasNoarkObject) {
        File file = getFile(entity);
        if (file.getReferenceClass() != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_CLASS + SLASH + file.getReferenceClass().getSystemId(),
                    REL_FONDS_STRUCTURE_CLASS, true));
        }
    }

    @Override
    public void addEndFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + FILE_END + SLASH,
                REL_FONDS_STRUCTURE_END_FILE,
                false));
    }

    @Override
    public void addExpandToCaseFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + FILE_EXPAND_TO_CASE_FILE + SLASH,
                REL_FONDS_STRUCTURE_EXPAND_TO_CASE_FILE, false));
    }

    @Override
    public void addExpandToMeetingFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + FILE_EXPAND_TO_MEETING_FILE + SLASH,
                REL_FONDS_STRUCTURE_EXPAND_TO_MEETING_FILE, false));
    }

    @Override
    public void addRecord(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + RECORD + SLASH,
                REL_FONDS_STRUCTURE_RECORD, false));
    }

    @Override
    public void addNewRecord(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_RECORD + SLASH,
                REL_FONDS_STRUCTURE_NEW_RECORD, false));
    }

    @Override
    public void addComment(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + COMMENT + SLASH,
                REL_FONDS_STRUCTURE_COMMENT, false));
    }

    @Override
    public void addNewComment(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_COMMENT + SLASH,
                REL_FONDS_STRUCTURE_NEW_COMMENT, false));
    }

    @Override
    public void addParentFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        File file = getFile(entity).getReferenceParentFile();
        if (file != null) {
            hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                    HREF_BASE_FILE + SLASH + file.getSystemId(),
                    REL_FONDS_STRUCTURE_PARENT_FILE));
        }
    }

    @Override
    public void addSubFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + SUB_FILE + SLASH,
                REL_FONDS_STRUCTURE_SUB_FILE, false));
    }

    @Override
    public void addNewSubFile(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_FILE,
                REL_FONDS_STRUCTURE_NEW_FILE, false));
    }

    /**
     * Create a REL/HREF pair for the list of Part objects associated with the
     * given File.
     * <p>
     * "../hateoas-api/arkivstruktur/mappe/1234/part"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/part/"
     *
     * @param entity             file
     * @param hateoasNoarkObject hateoasFile
     */
    @Override
    public void addPart(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_FILE + SLASH +
                        entity.getSystemId() + SLASH + PART + SLASH,
                        REL_FONDS_STRUCTURE_PART, true));
    }

    @Override
    public void addNewPartPerson(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_FILE + SLASH +
                        entity.getSystemId() + SLASH + NEW_PART_PERSON + SLASH,
                        REL_FONDS_STRUCTURE_NEW_PART_PERSON));
    }

    @Override
    public void addNewPartUnit(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_FILE + SLASH +
                        entity.getSystemId() + SLASH + NEW_PART_UNIT + SLASH,
                        REL_FONDS_STRUCTURE_NEW_PART_UNIT));
    }

    @Override
    public void addCrossReference(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewCrossReference(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_CROSS_REFERENCE + SLASH,
                REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewClass(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_CLASS + SLASH,
                REL_FONDS_STRUCTURE_NEW_CLASS, false));
    }

    @Override
    public void addReferenceSeries(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + SERIES + SLASH,
                REL_FONDS_STRUCTURE_REFERENCE_SERIES, false));
    }

    @Override
    public void addNewReferenceSeries(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + SERIES + SLASH,
                REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES, false));
    }

    @Override
    public void addReferenceSecondaryClassification(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + SECONDARY_CLASSIFICATION + SLASH,
                REL_CASE_HANDLING_SECONDARY_CLASSIFICATION, false));
    }

    @Override
    public void addNewReferenceSecondaryClassification(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_SECONDARY_CLASSIFICATION_SYSTEM + SLASH,
                REL_CASE_HANDLING_NEW_SECONDARY_CLASSIFICATION, false));
    }

    public void addMetadataFileType(INoarkEntity entity,
                                    IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + FILE_TYPE,
                REL_METADATA_FILE_TYPE, false));
    }

    @Override
    public void addNewBuilding(INoarkEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_BUILDING, REL_FONDS_STRUCTURE_NEW_BUILDING));
    }

    @Override
    public void addNewCadastralUnit(INoarkEntity entity,
                                    IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_CADASTRAL_UNIT, REL_FONDS_STRUCTURE_NEW_CADASTRAL_UNIT));
    }

    @Override
    public void addNewDNumber(INoarkEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_D_NUMBER, REL_FONDS_STRUCTURE_NEW_D_NUMBER));
    }

    @Override
    public void addNewPlan(INoarkEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_PLAN, REL_FONDS_STRUCTURE_NEW_PLAN));
    }

    @Override
    public void addNewPosition(INoarkEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_POSITION, REL_FONDS_STRUCTURE_NEW_POSITION));
    }

    @Override
    public void addNewSocialSecurityNumber(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_SOCIAL_SECURITY_NUMBER,
                REL_FONDS_STRUCTURE_NEW_SOCIAL_SECURITY_NUMBER));
    }

    @Override
    public void addNewUnit(INoarkEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_NI_UNIT, REL_FONDS_STRUCTURE_NEW_NI_UNIT));
    }

    @Override
    public void addBuilding(INoarkEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                BUILDING, REL_FONDS_STRUCTURE_BUILDING));
    }

    @Override
    public void addCadastralUnit(INoarkEntity entity,
                                 IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                CADASTRAL_UNIT, REL_FONDS_STRUCTURE_CADASTRAL_UNIT));
    }

    @Override
    public void addDNumber(INoarkEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                D_NUMBER, REL_FONDS_STRUCTURE_D_NUMBER));
    }

    @Override
    public void addPlan(INoarkEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                PLAN, REL_FONDS_STRUCTURE_PLAN));
    }

    @Override
    public void addPosition(INoarkEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                POSITION, REL_FONDS_STRUCTURE_POSITION));
    }

    @Override
    public void addSocialSecurityNumber(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                SOCIAL_SECURITY_NUMBER,
                REL_FONDS_STRUCTURE_SOCIAL_SECURITY_NUMBER));
    }

    @Override
    public void addUnit(INoarkEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_FILE + SLASH + entity.getSystemId() + SLASH +
                NI_UNIT, REL_FONDS_STRUCTURE_NI_UNIT));
    }

    /**
     * Cast the INoarkEntity entity to a File
     *
     * @param entity the File
     * @return a File object
     */
    private File getFile(@NotNull INoarkEntity entity) {
        return (File) entity;
    }
}
