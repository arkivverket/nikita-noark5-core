package nikita.common.model.noark5.v5.hateoas;

import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

import java.util.List;

public interface IHateoasNikitaObject {
    List<Link> getLinks(INikitaEntity entity);

    List<INikitaEntity> getList();

    void addLink(INikitaEntity entity, Link link);

    void addSelfLink(Link selfLink);

    void addNextLink(Link selfLink);

    void addLink(Link selfLink);

    List<Link> getSelfLinks();

    boolean isSingleEntity();

    Long getEntityVersion();
}
