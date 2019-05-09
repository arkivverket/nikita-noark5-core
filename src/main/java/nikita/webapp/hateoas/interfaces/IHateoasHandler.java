package nikita.webapp.hateoas.interfaces;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.security.IAuthorisation;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Describe Hateoas links handler
 */
public interface IHateoasHandler {

    void addLinks(IHateoasNoarkObject hateoasNoarkObject,
                  IAuthorisation authorisation,
                  String outgoingAddress);

    void addLinksOnCreate(IHateoasNoarkObject hateoasNoarkObject,
                          IAuthorisation authorisation,
                          String outgoingAddress);

    void addLinksOnTemplate(IHateoasNoarkObject hateoasNoarkObject,
                            IAuthorisation authorisation,
                            String outgoingAddress);

    void addLinksOnRead(IHateoasNoarkObject hateoasNoarkObject,
                        IAuthorisation authorisation,
                        String outgoingAddress);

    void addLinksOnUpdate(IHateoasNoarkObject hateoasNoarkObject,
                          IAuthorisation authorisation,
                          String outgoingAddress);

    void addLinksOnDelete(IHateoasNoarkObject hateoasNoarkObject,
                          IAuthorisation authorisation,
                          String outgoingAddress);

    void addLinksOnNew(IHateoasNoarkObject hateoasNoarkObject,
                       IAuthorisation authorisation,
                       String outgoingAddress);

    void addSelfLink(INikitaEntity entity,
                     IHateoasNoarkObject hateoasNoarkObject,
                     String outgoingAddress);

    void addEntityLinks(INikitaEntity entity,
                        IHateoasNoarkObject hateoasNoarkObject,
                        String outgoingAddress);

    void addEntityLinksOnCreate(INikitaEntity entity,
                                IHateoasNoarkObject hateoasNoarkObject,
                                String outgoingAddress);

    void addEntityLinksOnTemplate(INikitaEntity entity,
                                  IHateoasNoarkObject hateoasNoarkObject,
                                  String outgoingAddress);

    void addEntityLinksOnRead(INikitaEntity entity,
                              IHateoasNoarkObject hateoasNoarkObject,
                              String outgoingAddress);

    void addEntityLinksOnNew(INikitaEntity entity,
                             IHateoasNoarkObject hateoasNoarkObject,
                             String outgoingAddress);

    void addDocumentMedium(INikitaEntity entity,
                           IHateoasNoarkObject hateoasNoarkObject,
                           String outgoingAddress);
}
