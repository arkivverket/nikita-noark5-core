package nikita.common.model.noark5.v5.hateoas;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

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
public class HateoasNoarkObject implements IHateoasNoarkObject {

    /**
     * A list of noark entities comprising a result set from a query. Using a
     * List to get implements Iterator and Collection.
     */
    private List<INoarkEntity> entityList = new ArrayList<>();

    /**
     * When a List<INoarkEntity> is in use, we make a note of the entityType
     * e.g is it a Fonds or a File or any other type.
     */
    private String entityType;

    /**
     * A Map of noark entities -> Hateoas links e.g fonds with Hateoas links
     */
    private Map<INoarkEntity, ArrayList<Link>> hateoasMap = new HashMap<>();

    /**
     * If the Hateoas object is a list of results, then the list needs its own
     * Hateoas Link to self because currently we're only adding self link
     */
    private List<Link> selfLinks = new ArrayList<>();

    /**
     * Whether or not the Hateoas object contains a single entity or a list of
     * entities. For simplicity a list is used even if the query
     * generated a single result. Makes coding other places easier
     */
    private boolean singleEntity = true;

    public HateoasNoarkObject() {
    }

    public HateoasNoarkObject(INoarkEntity entity) {
        if (entity != null) {
            entityList.add(entity);
        }
    }

    public HateoasNoarkObject(List<INoarkEntity> entityList,
                              String entityType) {
        this.entityList.addAll(entityList);
        this.entityType = entityType;
        singleEntity = false;
    }

    @Override
    public List<Link> getLinks(INoarkEntity entity) {
        return hateoasMap.get(entity);
    }

    @Override
    public List<INoarkEntity> getList() {
        return entityList;
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
	    && ! entityList.isEmpty()
	    && entityList.get(0) != null)
            return entityList.get(0).getVersion();
        else
            return Long.valueOf(-1);
    }
}
