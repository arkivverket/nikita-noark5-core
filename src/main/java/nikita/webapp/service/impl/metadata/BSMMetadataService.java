package nikita.webapp.service.impl.metadata;

import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.hateoas.metadata.BSMMetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.repository.n5v5.other.IBSMMetadataRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IBSMMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IBSMMetadataService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
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
import static nikita.common.config.N5ResourceMappings.BSM_DEF;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Service class for BSMMetadata. Note this is the actual class that is used to
 * handle the retrieval, creation, deletion and updating of BSM Metadata.
 * This class does not handle adding BSM metdata to entities e.g. File. See
 * FileService to see how to add BSM metadata to a file.
 */
@Service
@Transactional
public class BSMMetadataService
        extends NoarkService
        implements IBSMMetadataService {

    private static final Logger logger =
            LoggerFactory.getLogger(BSMMetadataService.class);
    private final IBSMMetadataHateoasHandler hateoasHandler;
    private final IBSMMetadataRepository metadataRepository;

    public BSMMetadataService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IBSMMetadataHateoasHandler hateoasHandler,
            IBSMMetadataRepository metadataRepository) {
        super(entityManager, applicationEventPublisher);
        this.hateoasHandler = hateoasHandler;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public ResponseEntity<BSMMetadataHateoas> find(UUID systemID) {
        BSMMetadata bsmMetadata = getBSMMetadataOrThrow(systemID);
        BSMMetadataHateoas bsmMetadataHateoas = new BSMMetadataHateoas(bsmMetadata);
        hateoasHandler.addLinks(bsmMetadataHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, bsmMetadata));
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(bsmMetadataHateoas.getEntityVersion().toString())
                .body(bsmMetadataHateoas);
    }

    @Override
    public ResponseEntity<BSMMetadataHateoas> save(
            @NotNull BSMMetadata bsmMetadata) {
        BSMMetadataHateoas bsmMetadataHateoas =
                new BSMMetadataHateoas(metadataRepository.save(bsmMetadata));
        hateoasHandler.addLinks(bsmMetadataHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, bsmMetadata));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(bsmMetadataHateoas.getEntityVersion().toString())
                .body(bsmMetadataHateoas);
    }

    @Override
    public ResponseEntity<BSMMetadataHateoas> handleUpdate(
            @NotNull UUID systemID, @NotNull PatchObjects patchObjects) {
        BSMMetadata bsmMetadata =
                (BSMMetadata) handlePatch(systemID, patchObjects);
        BSMMetadataHateoas bsmMetadataHateoas =
                new BSMMetadataHateoas(bsmMetadata);
        hateoasHandler.addLinks(bsmMetadataHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, bsmMetadata));
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(bsmMetadataHateoas.getEntityVersion().toString())
                .body(bsmMetadataHateoas);
    }

    @Override
    public Object handleUpdateRfc7396(
            @NotNull UUID systemID, @NotNull PatchMerge patchMerge) {
        BSMMetadata bsmMetadata = getBSMMetadataOrThrow(systemID);
        handlePatchMerge(bsmMetadata, patchMerge);
        BSMMetadataHateoas bsmMetadataHateoas =
                new BSMMetadataHateoas(bsmMetadata);
        hateoasHandler.addLinks(bsmMetadataHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, bsmMetadata));
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(bsmMetadataHateoas.getEntityVersion().toString())
                .body(bsmMetadataHateoas);
    }

    @Override
    public void deleteEntity(@NotNull UUID systemId) {
        deleteEntity(getBSMMetadataOrThrow(systemId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<BSMMetadataHateoas> findAll() {
        BSMMetadataHateoas bsmMetadataHateoas =
                new BSMMetadataHateoas(
                        (List<INoarkEntity>) (List)
                                metadataRepository.findAll(), BSM_DEF);
        hateoasHandler.addLinks(bsmMetadataHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(bsmMetadataHateoas.getEntityVersion().toString())
                .body(bsmMetadataHateoas);
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, be aware
     * that you will only ever get a valid BSMMetadata back. If there is no valid
     * BSMMetadata, an exception is thrown
     *
     * @param systemId systemId of the bsmMetadata object you are looking for
     * @return the newly found bsmMetadata object or null if it does not exist
     */
    public BSMMetadata getBSMMetadataOrThrow(@NotNull UUID systemId) {
        Optional<BSMMetadata> bsmMetadata =
                metadataRepository.findById(systemId);
        if (bsmMetadata.isEmpty()) {
            String info = INFO_CANNOT_FIND_OBJECT + " BSMMetadata, using systemId " +
                    systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        } else {
            return bsmMetadata.get();
        }
    }
}
