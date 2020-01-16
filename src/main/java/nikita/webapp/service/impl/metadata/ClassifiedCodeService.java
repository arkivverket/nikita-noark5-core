package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
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

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CLASSIFIED_CODE;

/**
 * Created by tsodring on 11/03/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class ClassifiedCodeService
        extends MetadataSuperService
        implements IClassifiedCodeService {

    private static final Logger logger =
            LoggerFactory.getLogger(ClassifiedCodeService.class);

    private IClassifiedCodeRepository classifiedCodeRepository;

    public ClassifiedCodeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IClassifiedCodeRepository classifiedCodeRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.classifiedCodeRepository = classifiedCodeRepository;
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
                (List<IMetadataEntity>) (List)
                        classifiedCodeRepository.findAll(),
                CLASSIFIED_CODE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all ClassifiedCode that have a particular code.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of ClassifiedCode objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public ClassifiedCode findMetadataByCode(String code) {
        return classifiedCodeRepository.findByCode(code);
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
        classifiedCode.setCodeName(
                TEMPLATE_CLASSIFIED_CODE_NAME);

        return classifiedCode;
    }

    /**
     * Update a ClassifiedCode identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code       The code of the classifiedCode object you
     *                       wish to update
     * @param incomingClassifiedCode The updated classifiedCode object. Note
     *                               the values you are allowed to change are
     *                               copied from this object. This object is
     *                               not persisted.
     * @return the updated classifiedCode
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final ClassifiedCode incomingClassifiedCode) {

        ClassifiedCode existingClassifiedCode =
            (ClassifiedCode) findMetadataByCodeOrThrow(code);
        updateCodeAndDescription(incomingClassifiedCode, existingClassifiedCode);

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingClassifiedCode.setVersion(version);

        MetadataHateoas classifiedCodeHateoas = new MetadataHateoas(
                classifiedCodeRepository.save(existingClassifiedCode));

        metadataHateoasHandler.addLinks(classifiedCodeHateoas,
                new Authorisation());
        return classifiedCodeHateoas;
    }
}
