package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class solves the problem of adding Hateoas links to Noark entities
 * before they are published by the core.
 * <p>
 * A HateoasNoarkObject is used for serializing Noark objects that have a
 * systemId. A Controller will populate the entityList with the results of a
 * query to the service layer.  There are classes that traverses this
 * list of entities and populates corresponding entity HashMap entry with
 * Hateoas links.
 * <p>
 * See the subclasses for details of who the serializer is that reuses the
 * HashMap.
 * <p>
 * Scalability with the HashMap (1000's of entries) isn't an issue because
 * pagination is used in the core
 */
@JsonSerialize(using = HateoasSerializer.class)
public class HateoasNoarkObject
        implements IHateoasNoarkObject {

    /**
     * A Map of noark entities -> Hateoas links e.g fonds with Hateoas links
     */
    private final Map<INoarkEntity, ArrayList<Link>> hateoasMap =
            new HashMap<>();
    /**
     * If the Hateoas object is a list of results, then the list needs its own
     * Hateoas Link to self because currently we're only adding self link
     */
    private final List<Link> selfLinks = new ArrayList<>();
    private final NikitaPage page;
    /**
     * When a List<INoarkEntity> is in use, we make a note of the entityType
     * e.g is it a Fonds or a File or any other type.
     */
    private String entityType;
    /**
     * Whether or not the Hateoas object contains a single entity or a list of
     * entities. For simplicity a list is used even if the query
     * generated a single result. Makes coding other places easier
     */
    private boolean singleEntity = true;

    public HateoasNoarkObject() {
        page = new NikitaPage();
    }

    public HateoasNoarkObject(INoarkEntity entity) {
        page = new NikitaPage();
        if (entity != null) {
            page.getEntityList().add(entity);
        }
    }

    @Deprecated
    /*
     * Note: This constructor MUST not be used going forward. The plan is to
     * switch out a List approach with a Page approach and force all lists of
     * results to use a NikitaPage object that supports count, top and skip
     */
    public HateoasNoarkObject(List<INoarkEntity> entityList,
                              String entityType) {
        this.entityType = entityType;
        page = new NikitaPage(entityList);
        singleEntity = false;
    }

    public HateoasNoarkObject(NikitaPage page,
                              String entityType) {
        this.entityType = entityType;
        this.page = page;
        singleEntity = false;
    }

    @Override
    public List<Link> getLinks(INoarkEntity entity) {
        return hateoasMap.get(entity);
    }

    @Override
    public List<INoarkEntity> getList() {
        return page.getEntityList();
    }

    @Override
    public void addLink(INoarkEntity entity, Link link) {
        hateoasMap.computeIfAbsent(entity, k -> new ArrayList<>()).add(link);
    }

    @Override
    public void addSelfLink(Link selfLink) {
        selfLinks.add(selfLink);
    }

    @Override
    public void addNextLink(Link selfLink) {
        selfLinks.add(selfLink);
    }

    @Override
    public void addLink(Link selfLink) {
        selfLinks.add(selfLink);
    }

    @Override
    public List<Link> getSelfLinks() {
        return selfLinks;
    }

    @Override
    public boolean isSingleEntity() {
        return singleEntity;
    }

    public String getEntityType() {
        return entityType;
    }

    @Override
    public Long getEntityVersion() {
        if (isSingleEntity()
                && !page.getEntityList().isEmpty()
                && page.getEntityList().get(0) != null)
            return page.getEntityList().get(0).getVersion();
        else
            return -1L;
    }

    @Override
    public NikitaPage getPage() {
        return page;
    }

    @Override
    public long getCount() {
        return page.getCount();
    }
}
