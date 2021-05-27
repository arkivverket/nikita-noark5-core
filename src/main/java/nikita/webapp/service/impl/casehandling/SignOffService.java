package nikita.webapp.service.impl.casehandling;

import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.common.repository.n5v5.secondary.ISignOffRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.secondary.SignOffHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.casehandling.ISignOffService;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
public class SignOffService
        extends NoarkService
        implements ISignOffService {

    private final ISignOffRepository signOffRepository;
    private final SignOffHateoasHandler signOffHateoasHandler;

    public SignOffService(EntityManager entityManager,
                          ApplicationEventPublisher applicationEventPublisher,
                          IODataService odataService,
                          IPatchService patchService,
                          ISignOffRepository signOffRepository,
                          SignOffHateoasHandler signOffHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.signOffRepository = signOffRepository;
        this.signOffHateoasHandler = signOffHateoasHandler;
    }

    @Override
    public SignOffHateoas save(SignOff signOff) {
        return packAsHateoas(signOffRepository.save(signOff));
    }

    @Override
    public SignOffHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getSignOffOrThrow(systemId));
    }

    @Override
    public SignOff findSignOffBySystemId(@NotNull final UUID systemId) {
        return getSignOffOrThrow(systemId);
    }

    @Override
    @Transactional
    public SignOffHateoas updateSignOff(
            @NotNull final UUID signOffSystemId,
            @NotNull final SignOff incomingSignOff) {
        // TODO: Revisit the logic behind this. Very unsure about the actual
        // approach we should use.
        SignOff signOff = getSignOffOrThrow(signOffSystemId);
        signOff.setSignOffBy(incomingSignOff.getSignOffBy());
        signOff.setSignOffMethod(incomingSignOff.getSignOffMethod());
        return packAsHateoas(signOff);
    }

    // All DELETE methods

    /**
     * Delete a SignOff identified by the given systemId
     *
     * @param systemId The systemId of the SignOff object you wish to delete
     */
    @Override
    @Transactional
    public void deleteSignOff(@NotNull final UUID systemId) {
        SignOff signOff = getSignOffOrThrow(systemId);
        for (RegistryEntry registryEntry : signOff.getReferenceRegistryEntry()) {
            registryEntry.removeSignOff(signOff);
        }
        signOffRepository.delete(signOff);
    }

    // All template methods

    public SignOffHateoas generateDefaultSignOff(
            @NotNull final UUID systemId) {
        SignOff defaultSignOff = new SignOff();
        defaultSignOff.setSignOffDate(OffsetDateTime.now());
        defaultSignOff.setSignOffBy(getUser());
        defaultSignOff.setVersion(-1L, true);
        return packAsHateoas(defaultSignOff);
    }

    // All helper methods

    public SignOffHateoas packAsHateoas(SignOff signOff) {
        SignOffHateoas signOffHateoas =
                new SignOffHateoas(signOff);
        applyLinksAndHeader(signOffHateoas, signOffHateoasHandler);
        return signOffHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid SignOff back. If there is no
     * valid SignOff, an exception is thrown
     *
     * @param systemId systemId of the signOff to find.
     * @return the signOff
     */
    protected SignOff getSignOffOrThrow(@NotNull final UUID systemId) {
        SignOff signOff = signOffRepository.findBySystemId(systemId);
        if (signOff == null) {
            throw new NoarkEntityNotFoundException(INFO_CANNOT_FIND_OBJECT +
                    " SignOff, using systemId " + systemId);
        }
        return signOff;
    }
}
