package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.RecordHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRegistryEntryHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add RegistryEntryHateoas links with RegistryEntry specific
 * information
 */
@Component("registryEntryHateoasHandler")
public class RegistryEntryHateoasHandler
        extends RecordHateoasHandler
        implements IRegistryEntryHateoasHandler {

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        super.addEntityLinks(entity, hateoasNoarkObject);


        addPrecedence(entity, hateoasNoarkObject);
        addNewPrecedence(entity, hateoasNoarkObject);
        addSignOff(entity, hateoasNoarkObject);
        addNewSignOff(entity, hateoasNoarkObject);
        addDocumentFlow(entity, hateoasNoarkObject);
        addNewDocumentFlow(entity, hateoasNoarkObject);
        addRegistryEntryStatus(entity, hateoasNoarkObject);
        addRegistryEntryType(entity, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate(
            ISystemId entity,
            IHateoasNoarkObject hateoasNoarkObject) {
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        addRegistryEntryStatus(entity, hateoasNoarkObject);
        addRegistryEntryType(entity, hateoasNoarkObject);
    }

    @Override
    public void addPrecedence(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + PRECEDENCE + SLASH,
                REL_CASE_HANDLING_PRECEDENCE, false));
    }

    @Override
    public void addNewPrecedence(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + NEW_PRECEDENCE + SLASH,
                REL_CASE_HANDLING_NEW_PRECEDENCE, false));
    }

    @Override
    public void addSignOff(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + SIGN_OFF + SLASH,
                REL_CASE_HANDLING_SIGN_OFF, false));
    }

    @Override
    public void addNewSignOff(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + NEW_SIGN_OFF + SLASH,
                REL_CASE_HANDLING_NEW_SIGN_OFF, false));
    }

    @Override
    public void addDocumentFlow(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + DOCUMENT_FLOW + SLASH,
                REL_CASE_HANDLING_DOCUMENT_FLOW, false));
    }

    @Override
    public void addNewDocumentFlow(ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + NEW_DOCUMENT_FLOW + SLASH,
                REL_CASE_HANDLING_NEW_DOCUMENT_FLOW, false));
    }

    // Metadata entries

    /**
     * Create a REL/HREF pair for the list of possible registryEntryStatus
     * (journalstatus) values
     * <p>
     * "../api/arkivstruktur/metadata/journalstatus"
     * "http://rel.kxml.no/noark5/v5/api/metadata/journalstatus/"
     *
     * @param entity             registryEntry
     * @param hateoasNoarkObject hateoasRegistryEntry
     */
    @Override
    public void addRegistryEntryStatus(ISystemId entity,
                                       IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_METADATA + SLASH +
                        REGISTRY_ENTRY_STATUS,
                        REL_METADATA_REGISTRY_ENTRY_STATUS, true));
    }

    /**
     * Create a REL/HREF pair for the list of possible registryEntryType
     * (journalposttype) values
     * <p>
     * "../api/arkivstruktur/metadata/journalposttype"
     * "http://rel.kxml.no/noark5/v5/api/metadata/journalposttype/"
     *
     * @param entity             registryEntry
     * @param hateoasNoarkObject hateoasRegistryEntry
     */
    @Override
    public void addRegistryEntryType(ISystemId entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_METADATA + SLASH +
                        REGISTRY_ENTRY_TYPE,
                        REL_METADATA_REGISTRY_ENTRY_TYPE, true));
    }
}
