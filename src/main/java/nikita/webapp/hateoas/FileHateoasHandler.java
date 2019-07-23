package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
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
    public void addEntityLinks(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject) {

        // Add the child links
        addRecord(entity, hateoasNoarkObject);
        addNewRecord(entity, hateoasNoarkObject);
        // Add the parent links
        addSeries(entity, hateoasNoarkObject);
        // Add action links
        addEndFile(entity, hateoasNoarkObject);
        addExpandToCaseFile(entity, hateoasNoarkObject);
        addExpandToMeetingFile(entity, hateoasNoarkObject);
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
    public void addSeries(INikitaEntity entity,
                          IHateoasNoarkObject hateoasNoarkObject) {
        File file = getFile(entity);
        if (file.getReferenceSeries() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_SERIES +
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
    public void addClass(INikitaEntity entity,
                         IHateoasNoarkObject hateoasNoarkObject) {
        File file = getFile(entity);
        if (file.getReferenceClass() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_CLASS +
                            file.getReferenceClass().getSystemId(),
                            REL_FONDS_STRUCTURE_CLASS, true));
        }
    }

    @Override
    public void addEndFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH + FILE_END
                + SLASH, REL_FONDS_STRUCTURE_END_FILE,
                false));
    }

    @Override
    public void addExpandToCaseFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                FILE_EXPAND_TO_CASE_FILE + SLASH, REL_FONDS_STRUCTURE_EXPAND_TO_CASE_FILE, false));
    }

    @Override
    public void addExpandToMeetingFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                FILE_EXPAND_TO_MEETING_FILE + SLASH, REL_FONDS_STRUCTURE_EXPAND_TO_MEETING_FILE, false));
    }

    @Override
    public void addRecord(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH + RECORD
                + SLASH, REL_FONDS_STRUCTURE_RECORD, false));
    }

    @Override
    public void addNewRecord(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH + NEW_RECORD
                + SLASH, REL_FONDS_STRUCTURE_NEW_RECORD, false));
    }

    @Override
    public void addComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                COMMENT + SLASH, REL_FONDS_STRUCTURE_COMMENT, false));
    }

    @Override
    public void addNewComment(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_COMMENT + SLASH, REL_FONDS_STRUCTURE_NEW_COMMENT, false));
    }

    @Override
    public void addSubFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                SUB_FILE + SLASH, REL_FONDS_STRUCTURE_SUB_FILE, false));
    }

    @Override
    public void addNewSubFile(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_FILE + SLASH, REL_FONDS_STRUCTURE_NEW_FILE, false));
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
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_FILE +
                        entity.getSystemId() + SLASH + PART + SLASH,
                        REL_FONDS_STRUCTURE_PART, true));
    }

    @Override
    public void addNewPartPerson(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_FILE +
                        entity.getSystemId() + SLASH + NEW_PART_PERSON + SLASH,
                        REL_FONDS_STRUCTURE_NEW_PART_PERSON));
    }

    @Override
    public void addNewPartUnit(
            INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_FILE +
                        entity.getSystemId() + SLASH + NEW_PART_UNIT + SLASH,
                        REL_FONDS_STRUCTURE_NEW_PART_UNIT));
    }

    @Override
    public void addCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                CROSS_REFERENCE + SLASH, REL_FONDS_STRUCTURE_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewCrossReference(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_CROSS_REFERENCE + SLASH, REL_FONDS_STRUCTURE_NEW_CROSS_REFERENCE, false));
    }

    @Override
    public void addNewClass(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH + NEW_REFERENCE_CLASS
                + SLASH, REL_FONDS_STRUCTURE_NEW_CLASS, false));
    }

    @Override
    public void addReferenceSeries(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH + SERIES
                + SLASH, REL_FONDS_STRUCTURE_REFERENCE_SERIES, false));
    }

    @Override
    public void addNewReferenceSeries(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                SERIES + SLASH, REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES,
                false));
    }

    @Override
    public void addReferenceSecondaryClassification(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                SECONDARY_CLASSIFICATION + SLASH, REL_FONDS_STRUCTURE_SECONDARY_CLASSIFICATION, false));
    }

    @Override
    public void addNewReferenceSecondaryClassification(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE + SLASH + entity.getSystemId() + SLASH +
                NEW_SECONDARY_CLASSIFICATION_SYSTEM + SLASH, REL_FONDS_STRUCTURE_NEW_SECONDARY_CLASSIFICATION, false));
    }

    /**
     * Cast the INikitaEntity entity to a File
     *
     * @param entity the File
     * @return a File object
     */
    private File getFile(@NotNull INikitaEntity entity) {
        return (File) entity;
    }
}
