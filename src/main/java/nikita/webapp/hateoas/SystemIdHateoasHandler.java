package nikita.webapp.hateoas;

import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.webapp.hateoas.interfaces.ISystemIdHateoasHandler;
import nikita.webapp.security.IAuthorisation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_MEDIUM;
import static nikita.common.config.ODataConstants.NEXT;
import static nikita.common.config.ODataConstants.PREVIOUS;

@Component
public class SystemIdHateoasHandler
        extends HateoasHandler
        implements ISystemIdHateoasHandler {

    private final Pattern patternTop = Pattern.compile("\\$top=[0-9]+");
    private final Pattern patternSkip = Pattern.compile("\\$skip=[0-9]+");

    /**
     * Create a self link and self pointing entity link. Allows a client to be
     * able to identify the entity type. Described in:
     * <p>
     * https://github.com/arkivverket/noark5-tjenestegrensesnitt-standard/blob/master/kapitler/06-konsepter_og_prinsipper.md#identifisere-entitetstype
     *
     * @param entity             The Noark entity
     * @param hateoasNoarkObject The Hateoas Noark Object
     */
    @Override
    public void addSelfLink(ISystemId entity,
                            IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + entity.getFunctionalTypeName() +
                SLASH + entity.getBaseTypeName() + SLASH + entity.getSystemIdAsString()
                + SLASH, getRelSelfLink()));

        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HATEOAS_API_PATH + SLASH + entity.getFunctionalTypeName() +
                SLASH + entity.getBaseTypeName() + SLASH + entity.getSystemIdAsString()
                + SLASH, entity.getBaseRel()));
    }

    @Override
    public void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                         IAuthorisation authorisation) {
        this.authorisation = authorisation;
        if (!hateoasNoarkObject.isSingleEntity()) {
            List<INoarkEntity> entities = hateoasNoarkObject.getList();

            for (INoarkEntity entity : entities) {
                addSelfLink((ISystemId) entity, hateoasNoarkObject);
                addEntityLinks((ISystemId) entity, hateoasNoarkObject);
            }

            String url = getRequestPathAndQueryString();
            Link selfLink = new Link(url, getRelSelfLink(), false);
            hateoasNoarkObject.addSelfLink(selfLink);
            NikitaPage page = hateoasNoarkObject.getPage();
            int top = page.getTop();
            int skip = page.getSkip();
            long count = page.getCount();
            addPaginationPrevious(hateoasNoarkObject, top, skip);
            addPaginationNext(hateoasNoarkObject, top, skip, count);
        } else {
            Iterable<ISystemId> entities = (List<ISystemId>) (List)
                    hateoasNoarkObject.getList();
            for (ISystemId entity : entities) {
                addSelfLink(entity, hateoasNoarkObject);
                addEntityLinks(entity, hateoasNoarkObject);
            }
            // If hateoasNoarkObject is a list add a self link.
            // { "entity": [], "_links": [] }
            if (!hateoasNoarkObject.isSingleEntity()) {
                String url = getRequestPathAndQueryString();
                Link selfLink = new Link(url, getRelSelfLink(), false);
                hateoasNoarkObject.addSelfLink(selfLink);
            }
        }
    }

    @Override
    public void addDocumentMedium(ISystemId entity,
                                  IHateoasNoarkObject hateoasNoarkObject) {
        hateoasNoarkObject.addLink(entity, new Link(getOutgoingAddress() +
                HREF_BASE_METADATA + SLASH + DOCUMENT_MEDIUM,
                REL_METADATA_DOCUMENT_MEDIUM, false));
    }


    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinks(ISystemId entity,
                               IHateoasNoarkObject hateoasNoarkObject) {
    }

    public void addPaginationPrevious(IHateoasNoarkObject hateoasNoarkObject,
                                      long top, long skip) {
        // Do not add PREVIOUS link if nothing to add. This is determined by
        // subtracting skip from top and checking that it is greater than 0
        if (skip != 0 && top - skip >= 0) {
            String url = getRequestPathAndQueryString();
            long actualSkip = top - skip > 0 ? top - skip : 0;
            url = replaceOrAddURLForTop(url, top);
            url = replaceOrAddURLForSkip(url, actualSkip);
            Link previous = new Link(url, PREVIOUS);
            hateoasNoarkObject.addLink(previous);
        }
    }

    public void addPaginationNext(IHateoasNoarkObject hateoasNoarkObject,
                                  int top, int skip, long count) {
        // Do not add NEXT link if nothing to add. This is determined by
        // adding skip + top and checking that it is less than the row count
        if (skip + top < count) {
            String url = getRequestPathAndQueryString();
            url = replaceOrAddURLForTop(url, top);
            url = replaceOrAddURLForSkip(url, skip + top);
            Link next = new Link(url, NEXT);
            hateoasNoarkObject.addLink(next);
        }
    }

    private String replaceOrAddURLForTop(String url, long top) {
        // Do not print $top=10 as 10 is default value
        if (top == 10) {
            // replace &\$top=[0-9]+ with ""
            url = url.replaceAll("&\\$top=[0-9]+", "");
            // but & might not be there so also replace \$top=[0-9]+ if it is
            // there
            return url.replaceAll("\\$top=[0-9]+", "");
        }
        if (patternTop.matcher(url).find()) {
            return url.replaceAll("\\$top=[0-9]+", "\\$top=" + top);
        } else {
            return url + "&$top=" + top;
        }
    }

    private String replaceOrAddURLForSkip(String url, long skip) {
        // Do not print $skip=0 as 0 is default value
        if (skip == 0) {
            // replace &\$skip=[0-9]+ with ""
            url = url.replaceAll("&\\$skip=[0-9]+", "");
            // but & might not be there so also replace \$skip=[0-9]+ if it is
            // there
            return url.replaceAll("\\$skip=[0-9]+", "");
        }
        if (patternSkip.matcher(url).find()) {
            return url.replaceAll("\\$skip=[0-9]+", "\\$skip=" + skip);
        } else {
            return url + "&$skip=" + skip;
        }
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnCreate(ISystemId entity,
                                       IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnTemplate(ISystemId entity,
                                         IHateoasNoarkObject hateoasNoarkObject) {
    }

    @Override
    public void addLinksOnTemplate(IHateoasNoarkObject hateoasNoarkObject,
                                   IAuthorisation authorisation) {
        this.authorisation = authorisation;

        Iterable<ISystemId> entities = (List<ISystemId>) (List)
                hateoasNoarkObject.getList();
        for (ISystemId entity : entities) {
            addEntityLinksOnTemplate(entity, hateoasNoarkObject);
        }
    }

    // Sub class should handle this, empty links otherwise!
    @Override
    public void addEntityLinksOnRead(ISystemId entity,
                                     IHateoasNoarkObject hateoasNoarkObject) {
        addEntityLinks(entity, hateoasNoarkObject);
    }
}
