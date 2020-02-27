package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.repository.n5v5.metadata.IMetadataRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.common.util.exceptions.NoarkNotAcceptableException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nikita.common.config.Constants.*;
import static nikita.common.config.ErrorMessagesConstants.*;
import static nikita.common.config.MetadataConstants.METADATA_PACKAGE;
import static org.springframework.http.HttpStatus.*;

/**
 * 'Super' class for all metadata entities to reduce the amount of
 * boilerplate code in the codebase.
 * <p>
 * Note to make this class work seamlessly with updates to metadata entities,
 * reflection is used here to map the (Norwegian) entity type used in the URL
 * to the actual Spring repository. The map of entity type -> Repository is
 * stored in the variable repositoryMap and is populated when nikita starts up.
 */
@Service
@Transactional
public class MetadataService
        extends NoarkService
        implements IMetadataService {

    private static final Logger logger =
            LoggerFactory.getLogger(MetadataService.class);
    private IMetadataHateoasHandler metadataHateoasHandler;
    private Map<String, IMetadataRepository> repositoryMap = new HashMap<>();
    private Pattern pattern = Pattern.compile(".*" + HATEOAS_API_PATH + SLASH +
            NOARK_METADATA_PATH + "/(?:[n][y][\\-])?(\\w+).*");

    public MetadataService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IMetadataHateoasHandler metadataHateoasHandler,
            WebApplicationContext webAppContext)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException {
        super(entityManager, applicationEventPublisher);
        this.metadataHateoasHandler = metadataHateoasHandler;
        buildMapping(webAppContext);
    }

    /**
     * Update the Metadata object belonging to the entity type that is derived
     * from the request associated with the current thread.
     * If the URL contains e.g. ../metadata/tilgangsrestriksjon/B/ then
     * the Metadata corresponding to 'B' is retrieved. The object is
     * automatically saved by hibernate if there is a change in state when the
     * transaction is over.
     *
     * @param incomingMetadata incoming metadata object
     * @return the updated metadata object
     */
    @Override
    public ResponseEntity<MetadataHateoas> updateMetadataEntity(
            @NotNull final String code,
            @NotNull final Metadata incomingMetadata) {
        IMetadataEntity existingMetadata =
                findMetadataByCodeOrThrow(getEntityTypeFromRequest(), code);
        existingMetadata.setCode(incomingMetadata.getCode());
        existingMetadata.setCodeName(incomingMetadata.getCodeName());
        existingMetadata.setInactive(incomingMetadata.getInactive());
        return packSingleResult(existingMetadata, OK);
    }

    /**
     * Create an incoming Metadata object belonging to the entity type that is
     * derived from the request associated with the current thread.
     * If the URL contains e.g. ../metadata/ny-tilgangsrestriksjon then the
     * the MetadataRepository corresponding to tilgangsrestriksjon
     * (AccessRestriction) is retrieved. The object is then saved
     *
     * @param incomingMetadata incoming metadata object
     * @return the updated metadata object
     */
    @Override
    public ResponseEntity<MetadataHateoas> createNewMetadataEntity(
            @NotNull final IMetadataEntity incomingMetadata)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        // Get the entity class name that the underlying HTTP request
        // is using e.g
        //  .../metadata/ny-tilgangskategori returns AccessCategory
        String metadataEntityName = getEntityTypeFromRequest();
        IMetadataRepository metadataRepository =
                getMetadataRepositoryByEntityTypeOrThrow(
                        metadataEntityName);

        // DefaultRepositoryMetadata is a default implementation of
        // RepositoryMetadata and can be used to inspect generic types of
        // Repository to find out about domain and id class
        DefaultRepositoryMetadata defaultRepositoryMetadata =
                new DefaultRepositoryMetadata(metadataRepository.getClass()
                        .getInterfaces()[0]);

        // Get the class that the repository is using e.g. AccessCategory
        Class<?> metadataClass = defaultRepositoryMetadata.getDomainType();

        // Create an instance of the class the repository uses
        Metadata metadata = (Metadata) metadataClass
                .getDeclaredConstructor().newInstance();

        // Set the entity values
        metadata.setCode(incomingMetadata.getCode());
        metadata.setCodeName(incomingMetadata.getCodeName());
        metadata.setInactive(incomingMetadata.getInactive());
        return packSingleResult((IMetadataEntity)
                metadataRepository.save(metadata), CREATED);
    }

    /**
     * The API spec says that it should be possible to generate a template of
     * an object. It does not make sense to generate a template with default
     * values for a metadata entity so we simply return {}. The code is left
     * here as there may come a requirement in the future that says that the
     * endpoint returns eg. {'kode': null}
     *
     * @return an empty JSON object
     */
    @Override
    public ResponseEntity<String> generateTemplateMetadata() {
        return ResponseEntity.status(OK)
                .body("{}");
    }

    /**
     * Delete a given metadata entity identified by code. The entity type
     * that is derived from the request associated with the current thread is
     * used to get the correct repository. Throw 404 if it is not possible to
     * find the metadata entity associated with the code.
     *
     * @param code The code of the metadata object to delete
     * @return nothing, but set the status to 204 No Content
     */
    @Override
    public ResponseEntity<String> deleteMetadataEntity(
            @NotNull final String code) {
        String entityType = getEntityTypeFromRequest();
        IMetadataRepository metadataRepository =
                getMetadataRepositoryByEntityTypeOrThrow(entityType);
        Metadata metadata = (Metadata) metadataRepository.findByCode(code);
        if (metadata != null) {
            metadataRepository.delete(metadata);
            return ResponseEntity.status(NO_CONTENT)
                    .body(DELETE_RESPONSE);
        }
        String errorMessage = METADATA_ENTITY_MISSING + entityType;
        logger.error(errorMessage);
        throw new NoarkEntityNotFoundException(errorMessage);
    }

    /**
     * Check that the (code, codename) pair are correct. Given a particular
     * code, make sure that the associated codename is valid.
     * <p>
     * Note: This method is deprecated and will be replaced by:
     * IMetadataEntity findValidMetadataByEntityTypeOrThrow(
     * String entityClass, IMetadataEntity metadataEntity)
     * Avoid using this method in new code.
     * NikitaMalformedInputDataException (400 Bad Request) is thrown if the
     * code is null or the two codename values do not match.
     * <p>
     *
     * @param entityType the type of Metadata object e.g. tilgangsrestriksjon
     * @param code        The code value of the Metadata object e.g. B
     * @param codename    The codename value of the Metadata object e.g.
     *                    'Begrenset etter sikkerhetsinstruksen'
     * @return The Metadata object corresponding to the code
     */
    @Override
    @Deprecated
    public IMetadataEntity findValidMetadataByEntityTypeOrThrow(
            @NotNull final String entityType, @NotNull final String code,
            @NotNull final String codename) {
        if (null == code) {
            String errorMessage = entityType + " malformed, missing code.";
            logger.error(errorMessage);
            throw new NikitaMalformedInputDataException(errorMessage);
        }
        IMetadataEntity entity = findMetadataByCodeOrThrow(entityType, code);
        if (null != codename && !entity.getCodeName().equals(codename)) {
            String errorMessage = entityType + " code " + code + " and code " +
                    "name " + codename + " did not match metadata catalog.";
            logger.error(errorMessage);
            throw new NikitaMalformedInputDataException(errorMessage);
        }
        return entity;
    }

    /**
     * Check that the (code, codename) pair are correct. Given a particular
     * code, make sure that the associated codename is valid.
     * <p>
     * NikitaMalformedInputDataException (400 Bad Request) is thrown if the
     * code is null or the two codename values do not match.
     * <p>
     *
     * @param entityType     the type of Metadata object e.g. tilgangsrestriksjon
     * @param metadataEntity the Metadata object e.g. B
     *                       '('B', Begrenset etter sikkerhetsinstruksen')
     * @return The Metadata object corresponding to the code
     */
    @Override
    public IMetadataEntity findValidMetadataByEntityTypeOrThrow(
            @NotNull final String entityType,
            @NotNull final IMetadataEntity metadataEntity) {
        return findValidMetadataByEntityTypeOrThrow(entityType,
                metadataEntity.getCode(), metadataEntity.getCodeName());
    }

    /**
     * @param entityType The type of entity you wish to retrieve
     *                   e.g. tilgangsrestriksjon
     * @param code       The code of the object you wish to retrieve
     * @return The Metadata object corresponding to the code
     */
    @Override
    public IMetadataEntity
    findMetadataByCodeOrThrow(@NotNull final String entityType,
                              @NotNull final String code) {
        IMetadataRepository metadataRepository =
                getMetadataRepositoryByEntityTypeOrThrow(entityType);
        IMetadataEntity metadata = (IMetadataEntity)
                metadataRepository.findByCode(code);
        if (metadata != null) {
            return metadata;
        } else {
            String errorMessage = METADATA_ENTITY_MISSING + entityType;
            logger.error(errorMessage);
            throw new NoarkEntityNotFoundException(errorMessage);
        }
    }

    /**
     * Find a given Metadata object identified by code and pack it as a
     * MetadataHateoas object. It uses the entity type that is derived from
     * the request associated with the current thread is used to get the
     * correct repository.
     *
     * @param code The code of the Metadata object to retrieve
     * @return the metadata object packed as a MetadataHateoas object
     */
    @Override
    public ResponseEntity<MetadataHateoas>
    findMetadataByCodeOrThrow(@NotNull final String code) {
        return packSingleResult(
                findMetadataByCodeOrThrow(getEntityTypeFromRequest(), code), OK);
    }

    /**
     * Get a list of all Metadata objects belonging to the entity type that is
     * taken from the request associated with the current thread.
     *
     * @return the list of metadata objects packed as a MetadataHateoas object
     */
    @Override
    public ResponseEntity<MetadataHateoas> findAll() {
        String entityType = getEntityTypeFromRequest();
        IMetadataRepository md = getMetadataRepository(
                getEntityTypeFromRequest());
        return packListResult((List<IMetadataEntity>) md.findAll(), entityType);
    }

    /**
     * Find and return the correct MetadataRepository corresponding to the
     * entity name passed in
     *
     * @param entityName the name of the entity type to retrieve e.g.
     *                   tilgangsrestriksjon
     * @return the correct IMetadataRepository for the current request
     */
    private IMetadataRepository<? extends Metadata, String>
    getMetadataRepositoryByEntityTypeOrThrow(@NotNull final String entityName) {
        IMetadataRepository metadataRepository = repositoryMap.get(entityName);
        if (metadataRepository != null) {
            return metadataRepository;
        }
        String errorMessage = METADATA_ENTITY_CLASS_MISSING + entityName;
        logger.error(errorMessage);
        throw new NoarkEntityNotFoundException(errorMessage);
    }

    /**
     * Internal helper method to pack any metadata object as a MetadataHateoas
     * object and to set ALLOWS and ETAG headers.
     *
     * @param entity The entity to pack
     * @param status The HTTP status to use e.g. 200, 201
     * @return the entity packed as a MetadataHateoas object
     */
    private ResponseEntity<MetadataHateoas> packSingleResult(
            @NotNull final IMetadataEntity entity,
            @NotNull final HttpStatus status) {
        MetadataHateoas metadataHateoas =
                new MetadataHateoas(entity);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        setOutgoingRequestHeader(metadataHateoas);
        return ResponseEntity.status(status)
                .body(metadataHateoas);
    }

    /**
     * Internal helper method to pack a list of metadata objects as a
     * MetadataHateoas object and to set ALLOWS header.
     *
     * @param list       The entity to pack
     * @param entityType The entity type
     * @return the entity packed as a MetadataHateoas object
     */
    private ResponseEntity<MetadataHateoas> packListResult(
            @NotNull final List<IMetadataEntity> list,
            @NotNull final String entityType) {
        MetadataHateoas metadataHateoas =
                new MetadataHateoas(list, entityType);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        setOutgoingRequestHeaderList();
        return ResponseEntity.status(OK)
                .body(metadataHateoas);
    }

    /**
     * If the repository cannot be found, we assume the client has asked for
     * something that nikita does not understand and will return a 400 Bad
     * Request.
     *
     * @param entityType URL name of the metadata entity type
     * @return the correct metadataRepository or an exception is thrown.
     */
    private IMetadataRepository getMetadataRepository(
            @NotNull final String entityType) {
        IMetadataRepository repository = repositoryMap.get(entityType);
        if (repository == null) {
            String errorMessage = "Could not find a repository for type: " +
                    entityType;
            logger.error(errorMessage);
            throw new NoarkNotAcceptableException(errorMessage);
        }
        return repository;
    }

    /**
     * Get entity type  from underlying HTTP request applicable to
     * this thread. e.g. if the original HTTP request was
     * ../metadata/ny-tilgangsrestriksjon
     * then this will return 'tilgangsrestriksjon'
     *
     * @return the entity name
     */
    private String getEntityTypeFromRequest() {
        String path = getServletPath();
        Matcher matcher = pattern.matcher(path);
        if (!matcher.matches()) {
            String errorMessage = METADATA_ENTITY_CLASS_MISSING + path;
            logger.error(errorMessage);
            throw new NikitaMisconfigurationException(errorMessage);
        }
        return matcher.group(1);
    }

    /**
     * Retrieve a list classes marked with @Entity annotation from the
     * metadata package. For each entity, make a note of its getBaseTypeName and
     * map baseTypeName to the correct repository. Examples include:
     * <p>
     * graderingskode -> IClassifiedCodeRepository
     * partrolle -> IPartRoleRepository
     * variantformat -> IVariantFormatRepository
     * <p>
     * An instance of each class is instantiated sp that it is possible to
     * get the baseTypeName
     *
     * @param webAppContext WebApplicationContext for access to the list of
     *                      repositories
     * @throws ClassNotFoundException    Cannot find the entity
     * @throws NoSuchMethodException     Constructor does not exist
     * @throws IllegalAccessException    Not allowed access
     * @throws InvocationTargetException failure with reflection or
     *                                   instantiation
     * @throws InstantiationException    Problem with instantiation
     */
    private void buildMapping(
            @NotNull final WebApplicationContext webAppContext)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException {
        Repositories repositories = new Repositories(webAppContext);
        Reflections ref = new Reflections(METADATA_PACKAGE);
        Set<Class<?>> entities =
                ref.getTypesAnnotatedWith(Table.class);

        for (Class<?> entity : entities) {
            // Create instance of the class
            Metadata metadataEntity =
                    (Metadata) Class.forName(entity.getName())
                            .getConstructor()
                            .newInstance();
            // Find correct repositoryOpt
            Optional<Object> repositoryOpt =
                    repositories.getRepositoryFor(
                            metadataEntity.getClass());

            if (repositoryOpt.isPresent()) {
                IMetadataRepository metadataRepository =
                        ((IMetadataRepository)
                                repositoryOpt.get());
                // map e.g. variantformat -> IVariantFormatRepository
                repositoryMap.put(metadataEntity.getBaseTypeName(),
                        metadataRepository);
            } else {
                logger.error(ERROR_MAPPING_METADATA);
                throw new NikitaMisconfigurationException(
                        ERROR_MAPPING_METADATA);
            }
        }
    }
}
