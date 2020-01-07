package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
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
    public void addEntityLinks(INikitaEntity entity,
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

    public void addEntityLinksOnTemplate(
            INikitaEntity entity,
            IHateoasNoarkObject hateoasNoarkObject) {
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        addRegistryEntryStatus(entity, hateoasNoarkObject);
        addRegistryEntryType(entity, hateoasNoarkObject);
    }

    @Override
    public void addPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + PRECEDENCE + SLASH,
                REL_FONDS_STRUCTURE_PRECEDENCE, false));
    }

    @Override
    public void addNewPrecedence(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + NEW_PRECEDENCE + SLASH,
                REL_FONDS_STRUCTURE_NEW_PRECEDENCE, false));
    }

    @Override
    public void addSignOff(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + SIGN_OFF + SLASH,
                REL_FONDS_STRUCTURE_SIGN_OFF, false));
    }

    @Override
    public void addNewSignOff(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + NEW_SIGN_OFF + SLASH,
                REL_FONDS_STRUCTURE_NEW_SIGN_OFF, false));
    }

    @Override
    public void addDocumentFlow(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + DOCUMENT_FLOW + SLASH,
                REL_FONDS_STRUCTURE_DOCUMENT_FLOW, false));
    }

    @Override
    public void addNewDocumentFlow(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY + SLASH + entity.getSystemId() + SLASH + NEW_DOCUMENT_FLOW + SLASH,
                REL_FONDS_STRUCTURE_NEW_DOCUMENT_FLOW, false));
    }

    // Metadata entries

    /**
     * Create a REL/HREF pair for the list of possible registryEntryStatus
     * (journalstatus) values
     * <p>
     * "../hateoas-api/arkivstruktur/metadata/journalstatus"
     * "http://rel.kxml.no/noark5/v5/api/metadata/journalstatus/"
     *
     * @param entity             registryEntry
     * @param hateoasNoarkObject hateoasRegistryEntry
     */
    @Override
    public void addRegistryEntryStatus(INikitaEntity entity,
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
     * "../hateoas-api/arkivstruktur/metadata/journalposttype"
     * "http://rel.kxml.no/noark5/v5/api/metadata/journalposttype/"
     *
     * @param entity             registryEntry
     * @param hateoasNoarkObject hateoasRegistryEntry
     */
    @Override
    public void addRegistryEntryType(INikitaEntity entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity,
                new Link(getOutgoingAddress() + HREF_BASE_METADATA + SLASH +
                        REGISTRY_ENTRY_TYPE,
                        REL_METADATA_REGISTRY_ENTRY_TYPE, true));
    }
}
