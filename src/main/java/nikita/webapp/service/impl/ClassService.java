package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.repository.n5v5.IClassRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IClassHateoasHandler;
import nikita.webapp.hateoas.interfaces.IClassificationSystemHateoasHandler;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IClassService;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;
import static org.springframework.http.HttpStatus.OK;

/**
 * Service class for Class
 * <p>
 * Provides basic CRUD functionality for Class using systemID.
 * <p>
 * Also supports CREATE for a (Noark Classification Class) Class.
 * <p>
 * All public methods return Hateoas objects
 */
@Service
@Transactional
public class ClassService
        extends NoarkService
        implements IClassService {

    private static final Logger logger =
            LoggerFactory.getLogger(ClassService.class);
    private IClassRepository classRepository;
    private IFileService fileService;
    private ICaseFileService caseFileService;
    private IRecordService recordService;
    private IClassHateoasHandler classHateoasHandler;
    private IClassificationSystemHateoasHandler
            classificationSystemHateoasHandler;
    private IFileHateoasHandler fileHateoasHandler;
    private IRecordHateoasHandler recordHateoasHandler;

    public ClassService(EntityManager entityManager,
                        ApplicationEventPublisher applicationEventPublisher,
                        IClassRepository classRepository,
                        IFileService fileService,
                        ICaseFileService caseFileService,
                        IRecordService recordService,
                        IClassHateoasHandler classHateoasHandler,
                        IClassificationSystemHateoasHandler
                                classificationSystemHateoasHandler,
                        IFileHateoasHandler fileHateoasHandler,
                        IRecordHateoasHandler recordHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.classRepository = classRepository;
        this.fileService = fileService;
        this.caseFileService = caseFileService;
        this.recordService = recordService;
        this.classHateoasHandler = classHateoasHandler;
        this.classificationSystemHateoasHandler =
                classificationSystemHateoasHandler;
        this.fileHateoasHandler = fileHateoasHandler;
        this.recordHateoasHandler = recordHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new class object to the database. Some values are set in the
     * incoming payload (e.g. title) while some are set by the core.
     * owner, createdBy, createdDate are automatically set by the core.
     *
     * @param klass The class object object with some values set
     * @return the newly persisted class object wrapped as a classHateaos object
     */
    @Override
    public ClassHateoas save(Class klass) {
        setFinaliseEntityValues(klass);
        ClassHateoas classHateoas = new
                ClassHateoas(classRepository.save(klass));
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(
                        this, klass));
        return classHateoas;
    }

    /**
     * Persists a new class object to the database as a sub-class to an
     * existing class object. Some values are set in the incoming  payload
     * (e.g. title) while some are set by the core.  owner, createdBy,
     * createdDate are automatically set by the core.
     *
     * @param parentClassSystemId systemId of the parent object to connect this
     *                            class as a child to
     * @param klass               The class object object with some values set
     * @return the newly persisted class object wrapped as a classHateaos object
     */
    @Override
    public ClassHateoas createClassAssociatedWithClass(
            String parentClassSystemId, Class klass) {
        klass.setReferenceParentClass(getClassOrThrow(parentClassSystemId));
        return save(klass);
    }

    /**
     * Persists a new File object that is associated with the identified
     * Class object to the database. Some values are set in the incoming
     * payload (e.g. title) while some are set by the core.  owner, createdBy,
     * createdDate are automatically set by the core.
     *
     * @param classSystemId systemId of the Class object to associate this
     *                      File object to
     * @param file          The File object object with some values set
     * @return the newly persisted File object wrapped as a FileHateaos object
     */
    @Override
    public FileHateoas createFileAssociatedWithClass(
            String classSystemId, File file) {
        file.setReferenceClass(getClassOrThrow(classSystemId));
        return fileService.save(file);
    }

    /**
     * Persists a new CaseFile object that is associated with the identified
     * Class object to the database. Some values are set in the incoming
     * payload (e.g. title) while some are set by the core.  owner, createdBy,
     * createdDate are automatically set by the core.
     *
     * @param classSystemId systemId of the Class object to associate this
     *                      CaseFile object to
     * @param caseFile      The CaseFile object object with some values set
     * @return the newly persisted CaseFile object wrapped as a CaseFileHateaos
     * object
     */
    @Override
    public CaseFileHateoas createCaseFileAssociatedWithClass(
            String classSystemId, CaseFile caseFile) {
        caseFile.setReferenceClass(getClassOrThrow(classSystemId));
        return caseFileService.saveHateoas(caseFile);
    }

    /**
     * Generate a Default Class object that can be associated with the
     * identified Class. Note this object has not been persisted to the core.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param classSystemId The systemId of the class object you wish to
     *                      generate a default class for
     * @return the Class object wrapped as a ClassHateoas object
     */
    @Override
    public ClassHateoas generateDefaultSubClass(
            @NotNull String classSystemId) {

        Class defaultClass = new Class();
        ClassHateoas classHateoas = new ClassHateoas(defaultClass);
        classHateoasHandler.addLinksOnTemplate(classHateoas, new Authorisation());
        return classHateoas;
    }

    /**
     * Persists a new Record object that is associated with the identified
     * Class object to the database. Some values are set in the incoming
     * payload (e.g. title) while some are set by the core.  owner, createdBy,
     * createdDate are automatically set by the core.
     *
     * @param classSystemId systemId of the Class object to associate this
     *                      Record object to
     * @param record        The Record object object with some values set
     * @return the newly persisted Record object wrapped as a RecordHateaos
     * object
     */
    @Override
    public ResponseEntity<RecordHateoas> createRecordAssociatedWithClass(
            String classSystemId, Record record) {
        record.setReferenceClass(getClassOrThrow(classSystemId));
        return recordService.save(record);
    }

    // All READ operations

    /**
     * Retrieve all class objects the user owns.
     *
     * @param ownedBy identifier og logged-in user
     * @return ClassHateoas object containing a list of Class objects
     */
    @Override
    @SuppressWarnings("unchecked")
    public ClassHateoas findAll(@NotNull String ownedBy) {
        ClassHateoas classHateoas = new
                ClassHateoas((List<INoarkEntity>)
                (List) classRepository.findByOwnedBy(ownedBy));
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        return classHateoas;
    }

    /**
     * Retrieve a single class object identified by systemId
     * <p>
     * Note: This method can never return a null value.
     *
     * @param classSystemId The systemId of the Class object to retrieve
     * @return A ClassHateoas object containing the class
     */
    @Override
    public ClassHateoas findSingleClass(@NotNull String classSystemId) {
        ClassHateoas classHateoas = new
                ClassHateoas(getClassOrThrow(classSystemId));
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        return classHateoas;
    }

    /**
     * Retrieve a list of children class belonging to the class object
     * identified by systemId
     *
     * @param classSystemId The systemId of the Class object to retrieve its
     *                      children
     * @return A ClassHateoas object containing the children class's
     */
    @Override
    @SuppressWarnings("unchecked")
    public ClassHateoas findAllChildren(@NotNull String classSystemId) {
        ClassHateoas classHateoas = new
                ClassHateoas((List<INoarkEntity>)
                (List) getClassOrThrow(classSystemId).getReferenceChildClass());
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        return classHateoas;
    }

    /**
     * Retrieve the parent Class object associated with the Class object
     * identified by systemId
     *
     * @param systemId The systemId of the Class object to retrieve its parent
     *                 Class
     * @return A ClassHateoas object packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<ClassHateoas>
    findClassAssociatedWithClass(@NotNull final String systemId) {
        ClassHateoas classHateoas = new ClassHateoas(
                getClassOrThrow(systemId).getReferenceParentClass());
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(classHateoas.getEntityVersion().toString())
                .body(classHateoas);
    }


    /**
     * Retrieve the ClassificationSystemHateoas object associated with the
     * Class object identified by systemId
     *
     * @param systemId The systemId of the Class object to retrieve the
     *                 associated ClassificationSystemHateoas
     * @return A ClassificationSystemHateoas object packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<ClassificationSystemHateoas>
    findClassificationSystemAssociatedWithClass(@NotNull final String systemId) {
        ClassificationSystemHateoas classificationSystemHateoas =
                new ClassificationSystemHateoas(
                        getClassOrThrow(systemId).getReferenceClassificationSystem());
        classificationSystemHateoasHandler.addLinks(classificationSystemHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(classificationSystemHateoas.getEntityVersion().toString())
                .body(classificationSystemHateoas);
    }

    @Override
    public ResponseEntity<FileHateoas>
    findAllFileAssociatedWithClass(@NotNull final String systemId) {
        Class existingClass = getClassOrThrow(systemId);
        FileHateoas fileHateoas =
            new FileHateoas(
                (List<INoarkEntity>)(List)  existingClass.getReferenceFile()
                              );
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(fileHateoas);
    }

    @Override
    public ResponseEntity<RecordHateoas>
    findAllRecordAssociatedWithClass(@NotNull final String systemId) {
        Class existingClass = getClassOrThrow(systemId);
        RecordHateoas recordHateoas =
            new RecordHateoas(
                (List<INoarkEntity>)(List) existingClass.getReferenceRecord()
                              );
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(recordHateoas);
    }

    // All UPDATE operations

    /**
     * Updates a Class object in the database. First we try to locate the
     * Class object. If the Class object does not exist a
     * NoarkEntityNotFoundException exception is thrown that the caller has
     * to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingClass object to the existingClass object and the existingClass
     * object will be persisted to the database when the transaction boundary
     * is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the Class object from
     * the database a NoarkConcurrencyException is thrown. Note. This happens
     * when the call to Class.setVersion() occurs.
     * *
     *
     * @param systemId      systemId of the incoming class object
     * @param version       ETag version
     * @param incomingClass the contents to update
     * @return the updated Class object as a ClassHateoas object
     */
    @Override
    public ClassHateoas handleUpdate(
            @NotNull final String systemId, @NotNull final Long version,
            @NotNull final Class incomingClass) {
        Class existingClass = getClassOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingClass, existingClass);
        classRepository.save(existingClass);

        ClassHateoas classHateoas = new
                ClassHateoas(classRepository.save(
                classRepository.save(existingClass)));
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(
                        this, existingClass));
        return classHateoas;
    }

    // All DELETE operations

    /**
     * delete a Class entity with given classSystemId
     *
     * Note: When deleting, observe the following behaviour if there are
     * multiple parents. If a Class object has both a Class and
     * ClassificationSystem object as parent, return the object at the
     * closest level. In this case it is Class.
     *
     * Note: This method can return null! But it is unlikely that it will
     *
     * @param classSystemId systemId of the Class object to delete
     */
    @Override
    public void deleteEntity(@NotNull String classSystemId) {
        deleteEntity(getClassOrThrow(classSystemId));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return classRepository.deleteByOwnedBy(getUser());
    }

    // All HELPER operations

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once.  If you call this, be aware
     * that you will only ever get a valid Class back. If there is no valid
     * Class, an exception is thrown
     *
     * @param classSystemId systemId of the class object you are looking for
     * @return the newly found class object or null if it does not exist
     */
    protected Class getClassOrThrow(@NotNull String classSystemId) {
        Optional<Class> klass =
                classRepository.
                        findBySystemId(UUID.fromString(classSystemId));
        if (!klass.isPresent()) {
            String info = INFO_CANNOT_FIND_OBJECT + " Class, using systemId " +
                    classSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return klass.get();
    }
}
