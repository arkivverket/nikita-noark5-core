package nikita.webapp.service.impl;

import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.repository.n5v5.other.IBSMMetadataRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.IBSMService;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static nikita.common.config.N5ResourceMappings.BSM_DEF;

/**
 * Service class for businessSpecificMetadata (virksomhetsspesifikkeMetadata).
 */
@Service
public class BSMService
        extends NoarkService
        implements IBSMService {

    private static final Logger logger =
            LoggerFactory.getLogger(BSMService.class);

    private final IBSMMetadataRepository metadataRepository;

    public BSMService(EntityManager entityManager,
                      ApplicationEventPublisher applicationEventPublisher,
                      IODataService odataService,
                      IPatchService patchService,
                      IBSMMetadataRepository metadataRepository) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.metadataRepository = metadataRepository;
    }

    @Override
    public Optional<BSMMetadata> findBSMByName(@NotNull String name) {
        return metadataRepository.findByName(name);
    }

    /**
     * Validate a list of BSMBase objects.Currently we are only checking that
     * the name is registered
     *
     * @param referenceBSMBase the list of BSMBase objects
     */
    @Override
    public void validateBSMList(@NotNull List<BSMBase> referenceBSMBase) {
        for (BSMBase bsmBase : referenceBSMBase) {
            checkBSMNameRegistered(bsmBase);
        }
    }

    /**
     * Check that the name of the BSMBase object is registered
     *
     * @param bsmBase The BSMBase object with a name to check
     */
    private void checkBSMNameRegistered(BSMBase bsmBase) {
        if (metadataRepository.findByName(
                bsmBase.getValueName()).isEmpty()) {
            String error = bsmBase.getValueName() + " is not a registered" +
                    BSM_DEF + " name";
            logger.error(error);
            throw new NikitaMalformedInputDataException(error);
        }
    }
}
