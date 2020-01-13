package nikita.common.model.noark5.v5.hateoas;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

import java.util.List;

public interface IHateoasNoarkObject {
    List<Link> getLinks(INoarkEntity entity);

    List<INoarkEntity> getList();

    void addLink(INoarkEntity entity, Link link);

    void addSelfLink(Link selfLink);

    void addNextLink(Link selfLink);

    void addLink(Link selfLink);

    List<Link> getSelfLinks();

    boolean isSingleEntity();

    Long getEntityVersion();
}
