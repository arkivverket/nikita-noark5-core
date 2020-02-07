package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.MetadataSuperClass;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.common.util.exceptions.NoarkInvalidStructureException;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IMetadataSuperService;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

public abstract class MetadataSuperService
    extends NoarkService
    implements IMetadataSuperService {

    private static final Logger logger =
            LoggerFactory.getLogger(MetadataSuperService.class);

    protected IMetadataHateoasHandler metadataHateoasHandler;

    public MetadataSuperService(
                EntityManager entityManager,
                ApplicationEventPublisher applicationEventPublisher,
                IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    public MetadataSuperClass findValidMetadataOrThrow(
                String parent, String code, String codename) {
        if (null == code) {
            String entityname = "TODO unknown entity name";
            String info = entityname + " malformed, missing code.";
                logger.info(info);
                throw new NoarkInvalidStructureException(
                        info, parent, entityname);
        }
        MetadataSuperClass entity =
            (MetadataSuperClass) findMetadataByCodeOrThrow(code);
        if (null != codename &&
            ! entity.getCodeName().equals(codename)) {
            String entityname = entity.getBaseTypeName();
            String info = entityname + " code " + code + " and code name " +
                codename +  " did not match metadata catalog.";
                logger.info(info);
                throw new NoarkInvalidStructureException(
                        info, parent, entityname);
        }
        return entity;
    }
    public IMetadataEntity findMetadataByCodeOrThrow(@NotNull String code) {
        IMetadataEntity entity = findMetadataByCode(code);
        if (null == entity) {
            String info = "Unknown metadata code value '" + code + "'.";
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return entity;
    }

    public MetadataHateoas findByCode(@NotNull String code) {
        MetadataHateoas metadataHateoas =
            new MetadataHateoas(findMetadataByCodeOrThrow(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }
}
