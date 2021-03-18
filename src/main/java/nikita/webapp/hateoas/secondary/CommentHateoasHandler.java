package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICommentEntity;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.ICommentHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.config.ODataConstants.DOLLAR_FILTER;

/*
 * Used to add CommentHateoas links with Comment specific information
 */
@Component("commentHateoasHandler")
public class CommentHateoasHandler
        extends SystemIdHateoasHandler
        implements ICommentHateoasHandler {

    public CommentHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfHref = getOutgoingAddress() +
                HREF_BASE_FONDS_STRUCTURE + SLASH + COMMENT + SLASH +
                entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                new Link(selfHref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                new Link(selfHref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        Comment comment = (Comment) entity;
        addDocumentDescription(comment, hateoasNoarkObject);
        addRecord(comment, hateoasNoarkObject);
        addFile(comment, hateoasNoarkObject);
        addCommentType(comment, hateoasNoarkObject);
    }

    @Override
    public void addEntityLinksOnTemplate
            (ISystemId entity, IHateoasNoarkObject hateoasNoarkObject) {
        Comment comment = (Comment) entity;
        addCommentType(comment, hateoasNoarkObject);
    }

    @Override
    // This is an OData query that should give a list of File objects that
    // have an association to this comment
    // noark5v5/arkivstruktur/mappe?$filtermerknad/system_id eq 'SYSTEMID'
    public void addFile(ICommentEntity comment,
                        IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(comment,
                new Link(getOutgoingAddress() + HREF_BASE_FILE +
                        "?" + urlEncode(DOLLAR_FILTER) + "=" +
                        COMMENT + SLASH + SYSTEM_ID +
                        urlEncode(" eq '" + comment.getSystemId() + "'"),
                        REL_FONDS_STRUCTURE_FILE));
    }

    @Override
    // This is an OData query that should give a list of Record objects that
    // have an association to this comment
    // noark5v5/arkivstruktur/registrering?$filter=contains(merknad/system_id, 'SYSTEMID')
    public void addRecord(ICommentEntity comment,
                          IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(comment,
                new Link(getOutgoingAddress() + HREF_BASE_FILE +
                        "?" + urlEncode(DOLLAR_FILTER) + "=" +
                        COMMENT + SLASH + SYSTEM_ID +
                        urlEncode(" eq '" + comment.getSystemId() + "'"),
                        REL_FONDS_STRUCTURE_RECORD));
    }

    @Override
    // This is an OData query that should give a list of DocumentDescription
    // objects that have an association to this comment
    // noark5v5/arkivstruktur/dokumentbeskrivelse?$filter=contains(merknad/system_id, 'SYSTEMID')
    public void addDocumentDescription(ICommentEntity comment,
                                       IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(comment,
                new Link(getOutgoingAddress() +
                        HREF_BASE_DOCUMENT_DESCRIPTION +
                        "?" + urlEncode(DOLLAR_FILTER) + "=" +
                        COMMENT + SLASH + SYSTEM_ID +
                        urlEncode(" eq '" + comment.getSystemId() + "'"),
                        REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION));
    }

    @Override
    public void addCommentType(ICommentEntity comment,
                               IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(comment,
                new Link(getOutgoingAddress() +
                        HREF_BASE_METADATA + SLASH + COMMENT_TYPE,
                        REL_METADATA_COMMENT_TYPE, true));
    }
}
