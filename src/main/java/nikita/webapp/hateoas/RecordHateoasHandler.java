package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add RecordHateoas links with Record specific information
 */
@Component("recordHateoasHandler")
public class RecordHateoasHandler
        extends HateoasHandler
        implements IRecordHateoasHandler {

    public RecordHateoasHandler() {
    }

    @Override
    public void addEntityLinks(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        // Add the child links
        addDocumentDescription(entity, hateoasNoarkObject);
        addNewDocumentDescription(entity, hateoasNoarkObject);
        // Add the parent links
        addReferenceSeries(entity, hateoasNoarkObject);
        addReferenceFile(entity, hateoasNoarkObject);
        addReferenceClass(entity, hateoasNoarkObject);
        // Add the secondary entity links
        addClassified(entity, hateoasNoarkObject);
        addNewClassified(entity, hateoasNoarkObject);
        addDisposal(entity, hateoasNoarkObject);
        addNewDisposal(entity, hateoasNoarkObject);
        addDisposalUndertaken(entity, hateoasNoarkObject);
        addNewDisposalUndertaken(entity, hateoasNoarkObject);
        addDeletion(entity, hateoasNoarkObject);
        addNewDeletion(entity, hateoasNoarkObject);
        addScreening(entity, hateoasNoarkObject);
        addNewScreening(entity, hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the parent Series associated with the given
     * Record. Checks if the Record is actually associated with a Series.
     * <p>
     * "../hateoas-api/arkivstruktur/arkivdel/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addReferenceSeries(INikitaEntity entity,
                                   IHateoasNoarkObject hateoasNoarkObject) {
        Record record = getRecord(entity);
        if (record.getReferenceSeries() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_SERIES +
                            record.getReferenceSeries().getSystemId(),
                            REL_FONDS_STRUCTURE_SERIES, true));
        }
    }

    /**
     * Create a REL/HREF pair for the parent File associated with the given
     * Record. Checks if the Record is actually associated with a File.
     * <p>
     * "../hateoas-api/arkivstruktur/mappe/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/mappe/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addReferenceFile(INikitaEntity entity,
                                 IHateoasNoarkObject hateoasNoarkObject) {
        Record record = getRecord(entity);
        if (record.getReferenceFile() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_FILE +
                            record.getReferenceFile().getSystemId(),
                            REL_FONDS_STRUCTURE_FILE, true));
        }
    }

    /**
     * Create a REL/HREF pair for the parent Class associated with the given
     * Record. Checks if the Record is actually associated with a Class.
     * <p>
     * "../hateoas-api/arkivstruktur/klasse/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/"
     *
     * @param entity             record
     * @param hateoasNoarkObject hateoasRecord
     */
    @Override
    public void addReferenceClass(INikitaEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject) {
        Record record = getRecord(entity);
        if (record.getReferenceClass() != null) {
            hateoasNoarkObject.addLink(entity,
                    new Link(getOutgoingAddress() + HREF_BASE_CLASS +
                            record.getReferenceClass().getSystemId(),
                            REL_FONDS_STRUCTURE_CLASS, true));
        }
    }

    @Override
    public void addNewDocumentDescription(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_DOCUMENT_DESCRIPTION + SLASH, REL_FONDS_STRUCTURE_NEW_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addDocumentDescription(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                DOCUMENT_DESCRIPTION + SLASH, REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION, false));
    }

    @Override
    public void addNewReferenceSeries(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                REFERENCE_NEW_SERIES + SLASH, REL_FONDS_STRUCTURE_NEW_REFERENCE_SERIES, false));
    }

    @Override
    public void addClassified(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                CLASSIFIED + SLASH, REL_FONDS_STRUCTURE_CLASSIFIED, false));
    }

    @Override
    public void addNewClassified(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_CLASSIFIED + SLASH, REL_FONDS_STRUCTURE_NEW_CLASSIFIED, false));
    }

    @Override
    public void addDisposal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                DISPOSAL + SLASH, REL_FONDS_STRUCTURE_DISPOSAL, false));
    }

    @Override
    public void addNewDisposal(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_DISPOSAL + SLASH, REL_FONDS_STRUCTURE_NEW_DISPOSAL, false));
    }

    @Override
    public void addDisposalUndertaken(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                DISPOSAL_UNDERTAKEN + SLASH, REL_FONDS_STRUCTURE_DISPOSAL_UNDERTAKEN, false));
    }

    @Override
    public void addNewDisposalUndertaken(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_DISPOSAL_UNDERTAKEN + SLASH, REL_FONDS_STRUCTURE_NEW_DISPOSAL_UNDERTAKEN, false));
    }

    @Override
    public void addDeletion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                DELETION + SLASH, REL_FONDS_STRUCTURE_DELETION, false));
    }

    @Override
    public void addNewDeletion(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_DELETION + SLASH, REL_FONDS_STRUCTURE_NEW_DELETION, false));
    }

    @Override
    public void addScreening(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                SCREENING + SLASH, REL_FONDS_STRUCTURE_SCREENING, false));
    }

    @Override
    public void addNewScreening(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + REGISTRATION + SLASH + entity.getSystemId() + SLASH +
                NEW_SCREENING + SLASH, REL_FONDS_STRUCTURE_NEW_SCREENING, false));
    }

    /**
     * Cast the INikitaEntity entity to a Record
     *
     * @param entity the Record
     * @return a Record object
     */
    private Record getRecord(@NotNull INikitaEntity entity) {
        return (Record) entity;
    }
}
