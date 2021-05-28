package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.model.noark5.v5.metadata.ClassificationType;
import nikita.common.repository.n5v5.IClassificationSystemRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IClassificationSystemHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.IClassService;
import nikita.webapp.service.interfaces.IClassificationSystemService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;

/**
 * Service class for ClassificationSystem.
 * <p>
 * Provides basic CRUD functionality for ClassificationSystem using systemId.
 * <p>
 * Also supports CREATE for a (Noark Classification Class) Class.
 * <p>
 * All public methods return Hateoas objects
 */
@Service
public class ClassificationSystemService
        extends NoarkService
        implements IClassificationSystemService {

    private static final Logger logger = LoggerFactory.getLogger(
            ClassificationSystemService.class);

    private final IMetadataService metadataService;
    private final IClassService classService;
    private final IClassificationSystemRepository classificationSystemRepository;
    private final IClassificationSystemHateoasHandler
            classificationSystemHateoasHandler;

    public ClassificationSystemService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IMetadataService metadataService,
            IClassService classService,
            IClassificationSystemRepository classificationSystemRepository,
            IClassificationSystemHateoasHandler
                    classificationSystemHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.metadataService = metadataService;
        this.classService = classService;
        this.classificationSystemRepository = classificationSystemRepository;
        this.classificationSystemHateoasHandler =
                classificationSystemHateoasHandler;
    }
    // All CREATE operations

    /**
     * Persists a new classificationSystem object to the database. Some
     * values are set in the incoming payload (e.g. title) and some are set
     * by the core. owner, createdBy, createdDate are automatically set by
     * the core.
     *
     * @param classificationSystem classificationSystem object with some values
     *                             set
     * @return the newly persisted classificationSystem object wrapped as a
     * classificationSystemHateaos object
     */
    @Override
    @Transactional
    public ClassificationSystemHateoas save(
            final ClassificationSystem classificationSystem) {
        validateClassificationType(classificationSystem);
        setFinaliseEntityValues(classificationSystem);
        return packAsHateoas(classificationSystemRepository
                .save(classificationSystem));
    }

    /**
     * Persists a new classificationSystem object to the database, that is
     * first associated with a parent classificationSystem object. Some
     * values are set in the incoming payload (e.g. title) and some are set
     * by the core. owner, createdBy, createdDate are automatically set by
     * the core.
     * <p>
     * First we try to locate the parent. If the parent does not exist a
     * NoarkEntityNotFoundException exception is thrown
     *
     * @param classificationSystemSystemId The systemId of the
     *                                     classificationSystem
     * @param klass                        incoming class object with some values set
     * @return the newly persisted class object wrapped as classSystemHateaos
     * object
     */
    @Override
    @Transactional
    public ClassHateoas createClassAssociatedWithClassificationSystem(
            @NotNull final UUID classificationSystemSystemId,
            @NotNull final Class klass) {
        ClassificationSystem classificationSystem =
                getClassificationSystemOrThrow(classificationSystemSystemId);
        klass.setReferenceClassificationSystem(classificationSystem);
        return classService.save(klass);
    }

    /**
     * Generate a Default Class object that can be associated with the
     * identified ClassificationSystem. Note this object has not been persisted
     * to the core.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the
     *                 classificationSystem object you
     *                 wish to generate a default Class for
     * @return the Class object wrapped as a ClassHateoas object
     */
    @Override
    public ClassHateoas generateDefaultClass(
            @NotNull final UUID systemId) {
        return classService.generateDefaultClass(systemId);
    }
    // All READ operations

    /**
     * Retrieve a single ClassificationSystem objects from the database.
     *
     * @param systemId The systemId of the
     *                 ClassificationSystem  object you
     *                 wish to retrieve
     * @return the ClassificationSystem object wrapped as a
     * ClassificationSystemHateoas object
     */
    @Override
    public ClassificationSystemHateoas findSingleClassificationSystem(
            @NotNull final UUID systemId) {
        return packAsHateoas(getClassificationSystemOrThrow(systemId));
    }

    @Override
    public ClassificationSystemHateoas findAllClassificationSystem() {
        return (ClassificationSystemHateoas) odataService.processODataQueryGet();
    }

    @Override
    public ClassHateoas findAllClassAssociatedWithClassificationSystem(
            @NotNull final UUID systemId) {
        // Make sure the ClassificationSystem exists
        getClassificationSystemOrThrow(systemId);
        return (ClassHateoas) odataService.processODataQueryGet();
    }

    @Override
    public SeriesHateoas findSeriesAssociatedWithClassificationSystem(
            @NotNull final UUID systemId) {
        getClassificationSystemOrThrow(systemId);
        return (SeriesHateoas) odataService.processODataQueryGet();
    }
    // All UPDATE operations

    /**
     * Updates a ClassificationSystem object in the database. First we try to
     * locate the ClassificationSystem object. If the ClassificationSystem
     * object does not exist a NoarkEntityNotFoundException exception is
     * thrown that the caller has to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingClassificationSystem object to the
     * existingClassificationSystem object and the existingClassificationSystem
     * object will be persisted to the database when the transaction boundary
     * is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the ClassificationSystem
     * object from the database a NoarkConcurrencyException is thrown. Note.
     * <p>
     * This happens when the call to ClassificationSystem.setVersion() occurs.
     *
     * @param systemId                     The systemId of the
     *                                     classificationSystem object to
     *                                     retrieve
     * @param version                      The last known version number
     *                                     (derived from an ETAG)
     * @param incomingClassificationSystem The incoming classificationSystem
     *                                     object
     * @return the ClassificationSystem object wrapped as a
     * ClassificationSystemHateoas object
     */
    @Override
    @Transactional
    public ClassificationSystemHateoas
    handleUpdate(@NotNull final UUID systemId, @NotNull final Long version,
                 @NotNull final ClassificationSystem
                         incomingClassificationSystem) {
        ClassificationSystem existingClassificationSystem =
                getClassificationSystemOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingClassificationSystem,
                existingClassificationSystem);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingClassificationSystem.setVersion(version);
        return packAsHateoas(existingClassificationSystem);
    }

    // All DELETE operations
    @Override
    @Transactional
    public void deleteClassificationSystem(@NotNull final UUID systemId) {
        ClassificationSystem classificationSystem =
                getClassificationSystemOrThrow(systemId);
        for (Series series : classificationSystem.getReferenceSeries()) {
            series.removeClassificationSystem(classificationSystem);
        }
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        classificationSystemRepository.deleteByOwnedBy(getUser());
    }

    /**
     * Generate a Default ClassificationSystem object
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @return the ClassificationSystem object wrapped as a
     * ClassificationSystemHateoas object
     */
    @Override
    public ClassificationSystemHateoas generateDefaultClassificationSystem() {
        ClassificationSystem defaultClassificationSystem =
                new ClassificationSystem();
        defaultClassificationSystem.setVersion(-1L, true);
        return packAsHateoas(defaultClassificationSystem);
    }
    // All HELPER operations

    public ClassificationSystemHateoas packAsHateoas(
            ClassificationSystem classificationSystem) {
        ClassificationSystemHateoas classificationSystemHateoas =
                new ClassificationSystemHateoas(classificationSystem);
        applyLinksAndHeader(classificationSystemHateoas,
                classificationSystemHateoasHandler);
        return classificationSystemHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid ClassificationSystem back. If
     * there is no validClassificationSystem, an exception is thrown
     *
     * @param systemId systemId of the classificationSystem object to retrieve
     * @return the classificationSystem object
     */
    protected ClassificationSystem getClassificationSystemOrThrow(
            @NotNull final UUID systemId) {
        ClassificationSystem classificationSystem =
                classificationSystemRepository.findBySystemId(systemId);
        if (classificationSystem == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " ClassificationSystem, using systemId " + systemId;
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
        return classificationSystem;
    }

    private void validateClassificationType(
            ClassificationSystem classificationSystem) {
        if (null != classificationSystem.getClassificationType()) {
            ClassificationType classificationType = (ClassificationType)
                    metadataService.findValidMetadata(
                            classificationSystem.getClassificationType());
            classificationSystem
                    .setClassificationType(classificationType);
        }
    }
}
