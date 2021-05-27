package nikita.webapp.service.impl.metadata;

import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.hateoas.metadata.BSMMetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.repository.n5v5.other.IBSMMetadataRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IBSMMetadataHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IBSMMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.List.copyOf;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.BSM_DEF;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestAsListOrThrow;
import static org.springframework.http.HttpHeaders.ALLOW;

/**
 * Service class for BSMMetadata. Note this is the actual class that is used to
 * handle the retrieval, creation, deletion and updating of BSM Metadata.
 * This class does not handle adding BSM metdata to entities e.g. File. See
 * FileService to see how to add BSM metadata to a file.
 */
@Service
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
            IODataService odataService,
            IPatchService patchService,
            IBSMMetadataHateoasHandler hateoasHandler,
            IBSMMetadataRepository metadataRepository) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.hateoasHandler = hateoasHandler;
        this.metadataRepository = metadataRepository;
    }

    // All CREATE methods

    @Override
    @Transactional
    public BSMMetadataHateoas save(@NotNull final BSMMetadata bsmMetadata) {
        return packAsHateoas(metadataRepository.save(bsmMetadata));
    }

    // All READ methods

    @Override
    public BSMMetadataHateoas find(@NotNull final UUID systemId) {
        return packAsHateoas(getBSMMetadataOrThrow(systemId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public BSMMetadataHateoas findAll() {
        NikitaPage page = new
                NikitaPage(copyOf((List<INoarkEntity>) (List)
                metadataRepository.findAll()));
        setOutgoingRequestHeaderList();
        return new BSMMetadataHateoas(page, BSM_DEF);
    }
    // All UPDATE methods


    @Override
    public BSMMetadataHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final BSMMetadata incomingBsmMetadata) {
        BSMMetadata bsmMetadata = getBSMMetadataOrThrow(systemId);
        bsmMetadata.setDescription(incomingBsmMetadata.getDescription());
        bsmMetadata.setName(incomingBsmMetadata.getName());
        bsmMetadata.setOutdated(incomingBsmMetadata.getOutdated());
        bsmMetadata.setSource(incomingBsmMetadata.getSource());
        bsmMetadata.setType(incomingBsmMetadata.getType());
        bsmMetadata.setVersion(version);
        return packAsHateoas(bsmMetadata);
    }

    @Override
    @Transactional
    public BSMMetadataHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final PatchObjects patchObjects) {
        return packAsHateoas((BSMMetadata) handlePatch(systemId, patchObjects));
    }

    @Override
    @Transactional
    public Object handleUpdateRfc7396(
            @NotNull final UUID systemId, @NotNull PatchMerge patchMerge) {
        BSMMetadata bsmMetadata = getBSMMetadataOrThrow(systemId);
        handlePatchMerge(bsmMetadata, patchMerge);
        return packAsHateoas(bsmMetadata);
    }

    // All DELETE methods

    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        deleteEntity(getBSMMetadataOrThrow(systemId));
    }

    // All helper methods

    /**
     * Set the outgoing ALLOW header
     */
    protected void setOutgoingRequestHeaderList() {
        HttpServletResponse response = ((ServletRequestAttributes)
                Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes()))
                .getResponse();
        HttpServletRequest request =
                ((ServletRequestAttributes)
                        RequestContextHolder
                                .getRequestAttributes()).getRequest();
        response.addHeader(ALLOW, getMethodsForRequestAsListOrThrow(
                request.getServletPath()));
    }

    public BSMMetadataHateoas packAsHateoas(
            @NotNull final BSMMetadata bsmMetadata) {
        BSMMetadataHateoas bsmMetadataHateoas =
                new BSMMetadataHateoas(bsmMetadata);
        applyLinksAndHeader(bsmMetadataHateoas, hateoasHandler);
        return bsmMetadataHateoas;
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
    public BSMMetadata getBSMMetadataOrThrow(@NotNull final UUID systemId) {
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
