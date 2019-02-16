package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.Class;
import nikita.common.model.noark5.v4.ClassificationSystem;
import nikita.common.model.noark5.v4.hateoas.ClassHateoas;
import nikita.common.model.noark5.v4.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.repository.n5v4.IClassificationSystemRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IClassHateoasHandler;
import nikita.webapp.hateoas.interfaces.IClassificationSystemHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IClassService;
import nikita.webapp.service.interfaces.IClassificationSystemService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setNoarkEntityValues;

/**
 * Service class for ClassificationSystem.
 * <p>
 * Provides basic CRUD functionality for ClassificationSystem using systemID.
 * <p>
 * Also supports CREATE for a (Noark Classification Class) Class.
 * <p>
 * All public methods return Hateoas objects
 */

@Service
@Transactional
public class ClassificationSystemService
        implements IClassificationSystemService {

    private static final Logger logger = LoggerFactory.getLogger(
            ClassificationSystemService.class);

    private IClassService classService;
    private IClassificationSystemRepository classificationSystemRepository;
    private IClassificationSystemHateoasHandler
            classificationSystemHateoasHandler;
    private IClassHateoasHandler classHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public ClassificationSystemService(IClassService classService,
                                       IClassificationSystemRepository
                                               classificationSystemRepository,
                                       IClassificationSystemHateoasHandler
                                               classificationSystemHateoasHandler,
                                       IClassHateoasHandler classHateoasHandler,
                                       ApplicationEventPublisher
                                               applicationEventPublisher) {
        this.classService = classService;
        this.classificationSystemRepository = classificationSystemRepository;
        this.classificationSystemHateoasHandler =
                classificationSystemHateoasHandler;
        this.classHateoasHandler = classHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
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
    public ClassificationSystemHateoas save(
            ClassificationSystem classificationSystem) {
        setNoarkEntityValues(classificationSystem);
        setFinaliseEntityValues(classificationSystem);
        ClassificationSystemHateoas classificationSystemHateoas = new
                ClassificationSystemHateoas(
                classificationSystemRepository.save(
                        classificationSystem));
        classificationSystemHateoasHandler.addLinks(classificationSystemHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(
                        this, classificationSystem));
        return classificationSystemHateoas;
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
    public ClassHateoas createClassAssociatedWithClassificationSystem(
            @NotNull String classificationSystemSystemId,
            @NotNull Class klass) {
        ClassificationSystem classificationSystem =
                getClassificationSystemOrThrow(classificationSystemSystemId);
        klass.setReferenceClassificationSystem(classificationSystem);
        return classService.save(klass);
    }

    // All READ operations

    /**
     * Retrieve a single ClassificationSystem objects from the database.
     *
     * @param classificationSystemSystemId The systemId of the
     *                                     ClassificationSystem  object you
     *                                     wish to retrieve
     * @return the ClassificationSystem object wrapped as a
     * ClassificationSystemHateoas object
     */
    @Override
    public ClassificationSystemHateoas findSingleClassificationSystem(
            @NotNull String classificationSystemSystemId) {
        ClassificationSystem classificationSystem =
                getClassificationSystemOrThrow(classificationSystemSystemId);

        ClassificationSystemHateoas classificationSystemHateoas = new
                ClassificationSystemHateoas(
                classificationSystemRepository.
                        findBySystemId(classificationSystemSystemId));
        classificationSystemHateoasHandler.addLinks(classificationSystemHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityEvent(this, classificationSystem));
        return classificationSystemHateoas;
    }


    @Override
    @SuppressWarnings("unchecked")
    public ClassificationSystemHateoas findAllClassificationSystem() {
        ClassificationSystemHateoas classificationSystemHateoas = new
                ClassificationSystemHateoas((List<INikitaEntity>)
                (List) classificationSystemRepository.findAll());
        classificationSystemHateoasHandler.addLinks(classificationSystemHateoas,
                new Authorisation());
        return classificationSystemHateoas;
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
    public ClassificationSystemHateoas
    handleUpdate(@NotNull String systemId, @NotNull Long version,
                 @NotNull ClassificationSystem incomingClassificationSystem) {
        ClassificationSystem existingClassificationSystem =
                getClassificationSystemOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        if (null != incomingClassificationSystem.getDescription()) {
            existingClassificationSystem.setDescription(
                    incomingClassificationSystem.getDescription());
        }
        if (null != incomingClassificationSystem.getTitle()) {
            existingClassificationSystem.setTitle(
                    incomingClassificationSystem.getTitle());
        }
        existingClassificationSystem.setVersion(version);
        classificationSystemRepository.save(existingClassificationSystem);
        ClassificationSystemHateoas classificationSystemHateoas = new
                ClassificationSystemHateoas(existingClassificationSystem);
        classificationSystemHateoasHandler.addLinks(classificationSystemHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this,
                        existingClassificationSystem));
        return classificationSystemHateoas;
    }

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String classificationSystemSystemId) {
        ClassificationSystem classificationSystem =
                getClassificationSystemOrThrow(classificationSystemSystemId);
        classificationSystemRepository.delete(classificationSystem);
    }

    // All HELPER operations

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid ClassificationSystem back. If
     * there is no validClassificationSystem, an exception is thrown
     *
     * @param classificationSystemSystemId systemId of the classificationSystem
     *                                     object to retrieve
     * @return the classificationSystem object
     */
    protected ClassificationSystem getClassificationSystemOrThrow(
            @NotNull String classificationSystemSystemId) {
        ClassificationSystem classificationSystem =
                classificationSystemRepository.
                        findBySystemId(classificationSystemSystemId);
        if (classificationSystem == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " ClassificationSystem, using systemId " +
                    classificationSystemSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return classificationSystem;
    }
}
