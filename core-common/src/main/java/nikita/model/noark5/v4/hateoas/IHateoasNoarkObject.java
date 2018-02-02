package nikita.model.noark5.v4.hateoas;

import nikita.model.noark5.v4.interfaces.entities.INikitaEntity;

import java.util.AbstractCollection;
import java.util.Set;

public interface IHateoasNoarkObject {
    Set<Link> getLinks(INikitaEntity entity);

    AbstractCollection<INikitaEntity> getList();

    void addLink(INikitaEntity entity, Link link);

    void addSelfLink(Link selfLink);

    void addLink(Link selfLink);

    Set<Link> getSelfLinks();

    boolean isSingleEntity();
}
