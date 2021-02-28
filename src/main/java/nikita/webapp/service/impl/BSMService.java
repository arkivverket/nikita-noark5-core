package nikita.webapp.service.impl;

import nikita.webapp.service.interfaces.IBSMService;
import nikita.webapp.service.interfaces.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * Service class for businessSpecificMetadata (virksomhetsspesifikkeMetadata).
 */
@Service
public class BSMService
        extends NoarkService
        implements IBSMService {

    private static final Logger logger =
            LoggerFactory.getLogger(BSMService.class);

    private IFileService fileService;

    public BSMService(EntityManager entityManager,
                      ApplicationEventPublisher applicationEventPublisher,
                      IFileService fileService) {
        super(entityManager, applicationEventPublisher);
        this.fileService = fileService;
    }
}
