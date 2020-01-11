package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.webapp.hateoas.FileHateoasHandler;
import nikita.webapp.hateoas.interfaces.ICaseFileHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add CaseFileHateoas links with CaseFile specific information
 */

@Component("caseFileHateoasHandler")
public class CaseFileHateoasHandler
        extends FileHateoasHandler
        implements ICaseFileHateoasHandler {

    @Override
    public void addEntityLinks(INoarkEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        // Not calling  super.addEntityLinks(entity, hateoasNoarkObject);
        // because addExpandToCaseFile, addExpandToMeetingFile, addSubFile, addNewSubFile
        // are not applicable. Instead we invoke the methods directly here.
        // Methods from base class
        addEndFile(entity, hateoasNoarkObject);
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
        // Methods from this class
        //addClass(entity, hateoasNoarkObject);
        addNewPrecedence(entity, hateoasNoarkObject);
        //addPrecedence(entity, hateoasNoarkObject);
        addNewRegistryEntry(entity, hateoasNoarkObject);
        addRegistryEntry(entity, hateoasNoarkObject);
        addNewRecordNote(entity, hateoasNoarkObject);
        addRecordNote(entity, hateoasNoarkObject);
        //addSecondaryClassification(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(
            INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        // Get a list of case status values
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + CASE_STATUS + SLASH,
                REL_METADATA_CASE_STATUS));
    }

    @Override
    public void addNewClass(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_CLASS + SLASH,
                REL_CASE_HANDLING_NEW_CLASS, false));
    }

    @Override
    public void addClass(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + CLASS + SLASH,
                REL_CASE_HANDLING_CLASS, false));
    }

    @Override
    public void addNewPrecedence(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_PRECEDENCE + SLASH,
                REL_CASE_HANDLING_NEW_PRECEDENCE, false));
    }

    @Override
    public void addPrecedence(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + PRECEDENCE + SLASH,
                REL_CASE_HANDLING_PRECEDENCE, false));
    }

    @Override
    public void addSecondaryClassification(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + SECONDARY_CLASSIFICATION + SLASH,
                REL_CASE_HANDLING_SECONDARY_CLASSIFICATION, false));
    }

    @Override
    public void addNewSecondaryClassification(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_SECONDARY_CLASSIFICATION + SLASH,
                REL_CASE_HANDLING_NEW_SECONDARY_CLASSIFICATION, false));
    }

    @Override
    public void addRegistryEntry(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + REGISTRY_ENTRY + SLASH,
                REL_CASE_HANDLING_REGISTRY_ENTRY, false));
    }

    @Override
    public void addNewRegistryEntry(INoarkEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_REGISTRY_ENTRY + SLASH,
                REL_CASE_HANDLING_NEW_REGISTRY_ENTRY, false));
    }

    /**
     * Create a REL/HREF pair for the RecordNote associated with the
     * given CaseFile.
     * <p>
     * "../hateoas-api/arkivstruktur/sakarkiv/1234/arkivnotat"
     * "https://rel.arkivverket.no/noark5/v5/api/sakarkiv/arkivnotat/"
     *
     * @param entity             caseFile
     * @param hateoasNoarkObject hateoasCaseFile
     */
    @Override
    public void addRecordNote(INoarkEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
		HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + RECORD_NOTE,
                REL_CASE_HANDLING_RECORD_NOTE));
    }

    /**
     * Create a REL/HREF pair for the RecordNote associated with the
     * given CaseFile.
     * <p>
     * "../hateoas-api/arkivstruktur/sakarkiv/1234/ny-arkivnotat"
     * "https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-arkivnotat/"
     *
     * @param entity             caseFile
     * @param hateoasNoarkObject hateoasCaseFile
     */
    @Override
    public void addNewRecordNote(INoarkEntity entity,
                                 IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_FILE + SLASH + entity.getSystemId() + SLASH + NEW_RECORD_NOTE,
                REL_CASE_HANDLING_NEW_RECORD_NOTE));
    }
}
