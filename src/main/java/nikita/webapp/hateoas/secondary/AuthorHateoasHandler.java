package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IAuthorEntity;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.webapp.hateoas.HateoasHandler;
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
        extends HateoasHandler
        implements IAuthorHateoasHandler {

    public AuthorHateoasHandler() {
    }

    @Override
    public void addSelfLink(INikitaEntity entity,
                            IHateoasNoarkObject hateoasNoarkObject) {

        String parentEntity = "";
        String parentSystemId = "";
        Author author = (Author) entity;
        if (author.getForDocumentDescription()) {
            parentEntity =
                    author.getReferenceDocumentDescription().getBaseTypeName();
            parentSystemId =
                    author.getReferenceDocumentDescription().getSystemId();
            addDocumentDescription(author, hateoasNoarkObject);
        }
        if (author.getForRecord()) {
            parentEntity =
                    author.getReferenceRecord().getBaseTypeName();
            parentSystemId =
                    author.getReferenceRecord().getSystemId();
            addRecord(author, hateoasNoarkObject);
        }

        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + parentEntity + SLASH +
                parentSystemId + SLASH + AUTHOR + SLASH + entity.getSystemId(),
                getRelSelfLink()));

        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + parentEntity + SLASH +
                parentSystemId + SLASH + AUTHOR + SLASH + entity.getSystemId(),
                entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(INikitaEntity entity,
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
     * "../hateoas-api/arkivstruktur/registrering/1234"
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
     * "../hateoas-api/arkivstruktur/dokumentbeskrivelse/1234"
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