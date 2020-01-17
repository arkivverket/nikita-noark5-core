package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.repository.n5v5.metadata.IFondsStatusRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IFondsStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.*;

@Service
@Transactional
public class FondsStatusService
        extends MetadataSuperService
        implements IFondsStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(FondsStatusService.class);

    private IFondsStatusRepository fondsStatusRepository;

    public FondsStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IFondsStatusRepository fondsStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.fondsStatusRepository = fondsStatusRepository;
    }

    // All CREATE operations

    /**
     * Persists a new fondsStatus object to the database.
     *
     * @param fondsStatus fondsStatus object with values set
     * @return the newly persisted fondsStatus object
     */
    @Override
    public FondsStatus createNewFondsStatus(FondsStatus fondsStatus) {
        return fondsStatusRepository.save(fondsStatus);
    }

    // All READ operations

    /**
     * retrieve all fondsStatus
     *
     * @return
     */
    @Override
    public List<IMetadataEntity> findAll() {
        return (List<IMetadataEntity>) (List)
                fondsStatusRepository.findAll();
    }


    /**
     * retrieve all fondsStatus that have a particular code. <br>
     * This will be replaced by OData search.
     *
     * @param code
     * @return
     */
    @Override
    public FondsStatus findMetadataByCode(String code) {
        return fondsStatusRepository.findByCode(code);
    }

    /**
     * update a particular fondsStatus. <br>
     *
     * @param fondsStatus
     * @return the updated fondsStatus
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final FondsStatus incomingFondsStatus) {
        FondsStatus existingFondsStatus =
            (FondsStatus) findMetadataByCodeOrThrow(code);
        updateCodeAndDescription(incomingFondsStatus, existingFondsStatus);
        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingFondsStatus.setVersion(version);

        MetadataHateoas fondsStatusHateoas = new MetadataHateoas
            (fondsStatusRepository.save(existingFondsStatus));

        metadataHateoasHandler.addLinks(fondsStatusHateoas, new Authorisation());
        return fondsStatusHateoas;
    }
}
