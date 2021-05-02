package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.metadata.ScreeningMetadata;
import nikita.common.model.noark5.v5.secondary.Screening;
import nikita.common.model.noark5.v5.secondary.ScreeningMetadataLocal;
import nikita.common.repository.n5v5.secondary.IScreeningMetadataLocalRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IScreeningMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.IScreeningMetadataService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

import static java.util.List.copyOf;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateScreeningMetadata;

@Service
public class ScreeningMetadataService
        extends NoarkService
        implements IScreeningMetadataService {

    private final IMetadataService metadataService;
    private final IScreeningMetadataLocalRepository repository;
    private final IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler;

    public ScreeningMetadataService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IPatchService patchService,
            IMetadataService metadataService,
            IScreeningMetadataLocalRepository repository,
            IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService);
        this.metadataService = metadataService;
        this.repository = repository;
        this.screeningMetadataHateoasHandler = screeningMetadataHateoasHandler;
    }

    @Override
    @Transactional
    public ScreeningMetadataHateoas createScreeningMetadata(
            Screening screening, Metadata metadata) {
        ScreeningMetadata screeningMetadata = new ScreeningMetadata(
                metadata.getCode(), metadata.getCodeName());
        validateScreeningMetadata(metadataService, screeningMetadata);

        ScreeningMetadataLocal screeningMetadataLocal =
                new ScreeningMetadataLocal(screeningMetadata);
        screening.addReferenceScreeningMetadata(screeningMetadataLocal);
        repository.save(screeningMetadataLocal);
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(screeningMetadataLocal);
        screeningMetadataHateoasHandler.addLinks(screeningMetadataHateoas,
                new Authorisation());
        return screeningMetadataHateoas;
    }

    @Override
    public ScreeningMetadataHateoas getDefaultScreeningMetadata(UUID systemId) {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(new ScreeningMetadataLocal());
        screeningMetadataHateoasHandler.addLinksOnTemplate(
                screeningMetadataHateoas, new Authorisation());
        return screeningMetadataHateoas;
    }

    @Override
    public ScreeningMetadataHateoas findAllByOwner() {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(copyOf(repository
                        .findByOwnedBy(getUser())));
        screeningMetadataHateoasHandler.addLinks(screeningMetadataHateoas,
                new Authorisation());
        return screeningMetadataHateoas;
    }

    @Override
    public ScreeningMetadataHateoas findBySystemId(UUID systemId) {
        ScreeningMetadataLocal screeningMetadata =
                getScreeningMetadataLocalOrThrow(systemId);
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(screeningMetadata);
        screeningMetadataHateoasHandler.addLinks(screeningMetadataHateoas,
                new Authorisation());
        return screeningMetadataHateoas;
    }

    @Override
    @Transactional
    public ScreeningMetadataHateoas updateScreeningMetadataBySystemId(
            UUID systemId, Long etag,
            Metadata incomingScreeningMetadata) {
        ScreeningMetadata screeningMetadata = new ScreeningMetadata(
                incomingScreeningMetadata.getCode(),
                incomingScreeningMetadata.getCodeName());
        validateScreeningMetadata(metadataService, screeningMetadata);

        ScreeningMetadataLocal screeningMetadataLocal =
                getScreeningMetadataLocalOrThrow(systemId);
        screeningMetadataLocal.setCode(incomingScreeningMetadata.getCode());
        screeningMetadataLocal.setCodeName(incomingScreeningMetadata.getCodeName());
        screeningMetadataLocal.setVersion(etag);
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(screeningMetadataLocal);
        screeningMetadataHateoasHandler.addLinks(screeningMetadataHateoas,
                new Authorisation());
        return screeningMetadataHateoas;
    }

    @Override
    @Transactional
    public void deleteScreeningMetadataBySystemId(UUID systemId) {
        ScreeningMetadataLocal screeningMetadataLocal =
                getScreeningMetadataLocalOrThrow(systemId);
        screeningMetadataLocal.getReferenceScreening()
                .getReferenceScreeningMetadata().remove(screeningMetadataLocal);
        screeningMetadataLocal.setReferenceScreening(null);
        repository.deleteById(systemId);
    }

    /*
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, be aware
     * that you will only ever get a valid ScreeningMetadataLocal back. If
     * there is no valid ScreeningMetadataLocal, an exception is thrown
     *
     * @param systemId of the screeningMetadataLocal object you are looking for
     * @return the newly found screeningMetadataLocal object
     */
    private ScreeningMetadataLocal getScreeningMetadataLocalOrThrow(
            @NotNull UUID systemId) {
        Optional<ScreeningMetadataLocal> screeningMetadata =
                repository.findBySystemId(systemId);
        if (screeningMetadata.isPresent()) {
            return screeningMetadata.get();
        } else {
            String error = "Could not find ScreeningMetadataLocal object with" +
                    " systemID " + systemId;
            throw new NoarkEntityNotFoundException(error);
        }
    }
}
