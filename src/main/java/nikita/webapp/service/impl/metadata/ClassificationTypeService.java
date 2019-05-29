package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.ClassificationType;
import nikita.common.repository.n5v5.metadata.IClassificationTypeRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IClassificationTypeService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CLASSIFICATION_TYPE;

/**
 * Created by tsodring on 11/03/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class ClassificationTypeService
        extends NoarkService
        implements IClassificationTypeService {

    private static final Logger logger =
            LoggerFactory.getLogger(ClassificationTypeService.class);

    private IClassificationTypeRepository classificationTypeRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public ClassificationTypeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IClassificationTypeRepository classificationTypeRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.classificationTypeRepository = classificationTypeRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new ClassificationType object to the database.
     *
     * @param classificationType ClassificationType object with values set
     * @return the newly persisted ClassificationType object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewClassificationType(
            ClassificationType classificationType) {

        classificationType.setDeleted(false);
        classificationType.setOwnedBy(SecurityContextHolder.getContext().
                getAuthentication().getName());

        MetadataHateoas metadataHateoas = new MetadataHateoas(
                classificationTypeRepository.save(classificationType));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all ClassificationType objects
     *
     * @return list of ClassificationType objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        classificationTypeRepository.findAll(),
                CLASSIFICATION_TYPE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single ClassificationType object identified by systemId
     *
     * @param systemId systemId of the ClassificationType you wish to retrieve
     * @return single ClassificationType object wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                classificationTypeRepository
                        .findBySystemId(systemId));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all ClassificationType that have a given description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of ClassificationType objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        classificationTypeRepository
                                .findByDescription(description),
                CLASSIFICATION_TYPE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all ClassificationType that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of ClassificationType objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        classificationTypeRepository.findByCode(code),
                CLASSIFICATION_TYPE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default ClassificationType object
     *
     * @return the ClassificationType object wrapped as a
     * ClassificationTypeHateoas object
     */
    @Override
    public ClassificationType generateDefaultClassificationType() {

        ClassificationType classificationType = new ClassificationType();
        classificationType.setCode(TEMPLATE_CLASSIFICATION_TYPE_CODE);
        classificationType.setDescription(
                TEMPLATE_CLASSIFICATION_TYPE_DESCRIPTION);

        return classificationType;
    }

    /**
     * Update a ClassificationType identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId           The systemId of the classificationType
     *                           object you wish to update
     * @param incomingClassificationType The updated classificationType
     *                                   object. Note the values you are
     *                                   allowed to change are copied from
     *                                   this object. This object is not
     *                                   persisted.
     * @return the updated classificationType
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final ClassificationType incomingClassificationType) {

        ClassificationType existingClassificationType =
                getClassificationTypeOrThrow(systemId);
        updateCodeAndDescription(incomingClassificationType,
                existingClassificationType);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingClassificationType.setVersion(version);

        MetadataHateoas classificationTypeHateoas = new MetadataHateoas(
                classificationTypeRepository.save(existingClassificationType));

        metadataHateoasHandler.addLinks(classificationTypeHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this,
                existingClassificationType));
        return classificationTypeHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid ClassificationType object back. If
     * there is no ClassificationType object, a NoarkEntityNotFoundException
     * exception is thrown
     *
     * @param systemId The systemId of the ClassificationType object to retrieve
     * @return the ClassificationType object
     */
    private ClassificationType getClassificationTypeOrThrow(
            @NotNull String systemId) {
        ClassificationType classificationType =
                classificationTypeRepository.findBySystemId(systemId);
        if (classificationType == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " ClassificationType, using " + "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return classificationType;
    }
}
