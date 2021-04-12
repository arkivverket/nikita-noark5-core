package nikita.webapp.service.impl.casehandling;

import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.common.repository.n5v5.secondary.ISignOffRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.secondary.SignOffHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.casehandling.ISignOffService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class SignOffService
        extends NoarkService
        implements ISignOffService {

    private static final Logger logger =
            LoggerFactory.getLogger(SignOffService.class);

    private final ISignOffRepository signOffRepository;
    private final SignOffHateoasHandler signOffHateoasHandler;
    private final IMetadataService metadataService;

    public SignOffService(EntityManager entityManager,
                          ApplicationEventPublisher applicationEventPublisher,
                          IPatchService patchService,
                          ISignOffRepository signOffRepository,
                          SignOffHateoasHandler signOffHateoasHandler,
                          IMetadataService metadataService) {
        super(entityManager, applicationEventPublisher, patchService);
        this.signOffRepository = signOffRepository;
        this.signOffHateoasHandler = signOffHateoasHandler;
        this.metadataService = metadataService;
    }

    @Override
    public ResponseEntity<SignOffHateoas> findBySystemId(@NotNull UUID systemId) {
        SignOffHateoas signOffHateoas = new
                SignOffHateoas(getSignOffOrThrow(systemId));
        signOffHateoasHandler.addLinks(signOffHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(signOffHateoas.getEntityVersion().toString())
                .body(signOffHateoas);
    }

    @Override
    public ResponseEntity<SignOffHateoas> updateSignOff(
            @NotNull UUID signOffSystemId, @NotNull SignOff incomingSignOff) {
        // TODO: Revisit the logic behind this. Very unsure about the actual
        // approach we should use.
        SignOff signOff = getSignOffOrThrow(signOffSystemId);
        signOff.setSignOffBy(incomingSignOff.getSignOffBy());
        signOff.setSignOffMethod(incomingSignOff.getSignOffMethod());
        SignOffHateoas signOffHateoas = new SignOffHateoas(signOff);
        signOffHateoasHandler.addLinks(signOffHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(signOffHateoas.getEntityVersion().toString())
                .body(signOffHateoas);
    }

    // All helper methods

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid SignOff back. If there is no
     * valid SignOff, an exception is thrown
     *
     * @param systemId systemId of the signOff to find.
     * @return the signOff
     */
    protected SignOff getSignOffOrThrow(@NotNull UUID systemId) {
        SignOff signOff = signOffRepository.findBySystemId(systemId);
        if (signOff == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " SignOff, using systemId " + systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return signOff;
    }
}
