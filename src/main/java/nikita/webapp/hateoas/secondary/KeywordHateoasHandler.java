package nikita.webapp.hateoas.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IKeywordEntity;
import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IKeywordHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.KEYWORD;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static nikita.common.config.ODataConstants.DOLLAR_FILTER;

/**
 * Used to add KeywordHateoas links with Keyword specific information
 */
@Component("keywordHateoasHandler")
public class KeywordHateoasHandler
        extends SystemIdHateoasHandler
        implements IKeywordHateoasHandler {

    public KeywordHateoasHandler() {
    }

    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        String selfhref = getOutgoingAddress() + HREF_BASE_FONDS_STRUCTURE +
                SLASH + KEYWORD + SLASH + entity.getSystemId();
        hateoasNoarkObject.addLink(entity,
                new Link(selfhref, getRelSelfLink()));
        hateoasNoarkObject.addLink(entity,
                new Link(selfhref, entity.getBaseRel()));
    }

    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
        addClass((IKeywordEntity) entity, hateoasNoarkObject);
        addRecord((IKeywordEntity) entity, hateoasNoarkObject);
        addFile((IKeywordEntity) entity, hateoasNoarkObject);
    }

    /**
     * Create a REL/HREF pair for the parent Record associated with the given
     * Keyword
     * registrering?$filter=noekkelord/systemID eq '{systemID}'
     * <p>
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/registrering/"
     *
     * @param keyword            Keyword
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addRecord(IKeywordEntity keyword,
                          IHateoasNoarkObject hateoasNoarkObject) {
        if (keyword.getReferenceRecord().size() > 0) {
            hateoasNoarkObject.addLink(keyword,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_RECORD + "?" + urlEncode(DOLLAR_FILTER) +
                            "=" + KEYWORD + SLASH + SYSTEM_ID +
                            urlEncode(" eq '" + keyword.getSystemId() + "'"),
                            REL_FONDS_STRUCTURE_RECORD));
        }
    }

    /**
     * Create a REL/HREF pair for the parent Class associated
     * with the given Keyword
     * <p>
     * klasse?$filter=noekkelord/systemID eq '{systemID}'
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/klasse/"
     *
     * @param keyword            The Keyword object
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addClass(IKeywordEntity keyword,
                         IHateoasNoarkObject hateoasNoarkObject) {
        if (keyword.getReferenceClass().size() > 0) {
            hateoasNoarkObject.addLink(keyword,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_CLASS + "?" + urlEncode(DOLLAR_FILTER) +
                            "=" + KEYWORD + SLASH + SYSTEM_ID +
                            urlEncode(" eq '" + keyword.getSystemId() + "'"),
                            REL_FONDS_STRUCTURE_CLASS));
        }
    }

    /**
     * Create a REL/HREF pair for the parent File associated
     * with the given Keyword
     * <p>
     * mappe?$filter=noekkelord/systemID eq '{systemID}'
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/mappe/"
     *
     * @param keyword            The Keyword object
     * @param hateoasNoarkObject hateoasDocumentObject
     */
    @Override
    public void addFile(IKeywordEntity keyword,
                        IHateoasNoarkObject hateoasNoarkObject) {
        if (keyword.getReferenceFile().size() > 0) {
            hateoasNoarkObject.addLink(keyword,
                    new Link(getOutgoingAddress() +
                            HREF_BASE_FILE + "?" + urlEncode(DOLLAR_FILTER) +
                            "=" + KEYWORD + SLASH + SYSTEM_ID +
                            urlEncode(" eq '" + keyword.getSystemId() + "'"),
                            REL_FONDS_STRUCTURE_FILE));
        }
    }
}
