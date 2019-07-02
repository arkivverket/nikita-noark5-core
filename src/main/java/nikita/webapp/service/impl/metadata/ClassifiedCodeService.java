package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.ClassifiedCode;
import nikita.common.repository.n5v5.metadata.IClassifiedCodeRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IClassifiedCodeService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CLASSIFIED_CODE;

/**
 * Created by tsodring on 11/03/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class ClassifiedCodeService
        extends NoarkService
        implements IClassifiedCodeService {

    private static final Logger logger =
            LoggerFactory.getLogger(ClassifiedCodeService.class);

    private IClassifiedCodeRepository classifiedCodeRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public ClassifiedCodeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IClassifiedCodeRepository classifiedCodeRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.classifiedCodeRepository = classifiedCodeRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new ClassifiedCode object to the database.
     *
     * @param classifiedCode ClassifiedCode object with values set
     * @return the newly persisted ClassifiedCode object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewClassifiedCode(
            ClassifiedCode classifiedCode) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                classifiedCodeRepository.save(classifiedCode));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all ClassifiedCode objects
     *
     * @return list of ClassifiedCode objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        classifiedCodeRepository.findAll(),
                CLASSIFIED_CODE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single ClassifiedCode object identified by systemId
     *
     * @param systemId systemId of the ClassifiedCode you wish to retrieve
     * @return single ClassifiedCode object wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                classifiedCodeRepository
                        .findBySystemId(UUID.fromString(systemId)));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all ClassifiedCode that have a given description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of ClassifiedCode objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        classifiedCodeRepository
                                .findByDescription(description),
                CLASSIFIED_CODE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all ClassifiedCode that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of ClassifiedCode objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        classifiedCodeRepository.findByCode(code),
                CLASSIFIED_CODE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default ClassifiedCode object
     *
     * @return the ClassifiedCode object wrapped as a
     * ClassifiedCodeHateoas object
     */
    @Override
    public ClassifiedCode generateDefaultClassifiedCode() {

        ClassifiedCode classifiedCode = new ClassifiedCode();
        classifiedCode.setCode(TEMPLATE_CLASSIFIED_CODE_CODE);
        classifiedCode.setDescription(
                TEMPLATE_CLASSIFIED_CODE_DESCRIPTION);

        return classifiedCode;
    }

    /**
     * Update a ClassifiedCode identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId       The systemId of the classifiedCode object you
     *                       wish to update
     * @param incomingClassifiedCode The updated classifiedCode object. Note
     *                               the values you are allowed to change are
     *                               copied from this object. This object is
     *                               not persisted.
     * @return the updated classifiedCode
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final ClassifiedCode incomingClassifiedCode) {

        ClassifiedCode existingClassifiedCode =
                getClassifiedCodeOrThrow(systemId);
        updateCodeAndDescription(incomingClassifiedCode, existingClassifiedCode);

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingClassifiedCode.setVersion(version);

        MetadataHateoas classifiedCodeHateoas = new MetadataHateoas(
                classifiedCodeRepository.save(existingClassifiedCode));

        metadataHateoasHandler.addLinks(classifiedCodeHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this,
                existingClassifiedCode));
        return classifiedCodeHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid ClassifiedCode object back. If
     * there is no ClassifiedCode object, a NoarkEntityNotFoundException
     * exception is thrown
     *
     * @param systemId The systemId of the ClassifiedCode object to retrieve
     * @return the ClassifiedCode object
     */
    private ClassifiedCode getClassifiedCodeOrThrow(
            @NotNull String systemId) {
        ClassifiedCode classifiedCode =
                classifiedCodeRepository.findBySystemId(UUID.fromString(systemId));
        if (classifiedCode == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " ClassifiedCode, using " + "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return classifiedCode;
    }
}
