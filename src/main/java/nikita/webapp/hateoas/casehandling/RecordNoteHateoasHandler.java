package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.RecordHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordNoteHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_FLOW;

/**
 * Created by tsodring on 29/05/19.
 * <p>
 * Used to add RecordNoteHateoas links with RecordNote specific information
 */
@Component("recordNoteHateoasHandler")
public class RecordNoteHateoasHandler
        extends RecordHateoasHandler
        implements IRecordNoteHateoasHandler {

    @Override
    public void addEntityLinks(INikitaEntity entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        super.addEntityLinks(entity, hateoasNoarkObject);
        addDocumentFlow(entity, hateoasNoarkObject);
        addNewDocumentFlow(entity, hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the DocumentFlow associated with the
     * given RecordNote.
     * <p>
     * "../hateoas-api/arkivstruktur/arkivnotat/1234/dokumentflyt"
     * "https://rel.arkivverket.no/noark5/v5/api/sakarkiv/dokumentflyt/"
     *
     * @param entity             recordNote
     * @param hateoasNoarkObject hateoasRecordNote
     */
    @Override
    public void addDocumentFlow(INikitaEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_DOCUMENT_FLOW +
                        entity.getSystemId() + SLASH + DOCUMENT_FLOW,
                        REL_FONDS_STRUCTURE_DOCUMENT_FLOW));
    }

    /**
     * Create a REL/HREF pair to create a new DocumentFlow associated with the
     * given RecordNote.
     * <p>
     * "../hateoas-api/arkivstruktur/arkivnotat/1234/ny-dokumentflyt"
     * "https://rel.arkivverket.no/noark5/v5/api/sakarkiv/ny-dokumentflyt/"
     *
     * @param entity             recordNote
     * @param hateoasNoarkObject hateoasRecordNote
     */
    @Override
    public void addNewDocumentFlow(INikitaEntity entity,
                                   IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_DOCUMENT_FLOW +
                        entity.getSystemId() + SLASH + NEW_DOCUMENT_FLOW,
                        REL_FONDS_STRUCTURE_NEW_DOCUMENT_FLOW));
    }
}