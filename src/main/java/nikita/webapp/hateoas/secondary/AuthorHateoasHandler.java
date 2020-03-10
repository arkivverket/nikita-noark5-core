package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IAuthorEntity;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IAuthorHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.AUTHOR;

/**
 * Created by tsodring on 2020/01/06.
 * <p>
 * Used to add AuthorHateoas links with Author specific information
 */
@Component("authorHateoasHandler")
public class AuthorHateoasHandler
        extends SystemIdHateoasHandler
        implements IAuthorHateoasHandler {

    public AuthorHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfhref =  getOutgoingAddress() +
            HREF_BASE_FONDS_STRUCTURE + SLASH + AUTHOR + SLASH + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfhref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                                   new Link(selfhref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        Author author = (Author) entity;
        if (author.getForDocumentDescription()) {
            addDocumentDescription(author, hateoasNoarkObject);
        }
        if (author.getForRecord()) {
            addRecord(author, hateoasNoarkObject);
        }
    }

    /**
     * Create a REL/HREF pair for the parent Record associated with the given
     * Author
     * <p>
     * "../api/arkivstruktur/registrering/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/"
     *
     * @param author             Author
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addRecord(IAuthorEntity author,
                          IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(author,
                new Link(getOutgoingAddress() + HREF_BASE_RECORD + SLASH +
                        author.getReferenceRecord().getSystemId(),
                        REL_FONDS_STRUCTURE_RECORD));
    }

    /**
     * Create a REL/HREF pair for the parent DocumentDescription associated
     * with the given Author
     * <p>
     * "../api/arkivstruktur/dokumentbeskrivelse/1234"
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/dokumentbeskrivelse/"
     *
     * @param author             The Author object
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addDocumentDescription(IAuthorEntity author,
                                       IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(author,
                new Link(getOutgoingAddress() +
                        HREF_BASE_DOCUMENT_DESCRIPTION + SLASH +
                        author.getReferenceDocumentDescription().getSystemId(),
                        REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION));
    }
}
