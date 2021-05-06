package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.PrecedenceStatus;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.repository.n5v5.secondary.IPrecedenceRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IPrecedenceHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.IUserService;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.IPrecedenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.PRECEDENCE_APPROVED_BY;

@Service
public class PrecedenceService
        extends NoarkService
        implements IPrecedenceService {

    private static final Logger logger =
            LoggerFactory.getLogger(PrecedenceService.class);

    private final IMetadataService metadataService;
    private final IPrecedenceRepository precedenceRepository;
    private final IPrecedenceHateoasHandler precedenceHateoasHandler;
    private final IUserService userService;

    public PrecedenceService
            (EntityManager entityManager,
             ApplicationEventPublisher applicationEventPublisher,
             IPatchService patchService,
             IMetadataService metadataService,
             IPrecedenceRepository precedenceRepository,
             IPrecedenceHateoasHandler precedenceHateoasHandler,
             IUserService userService) {
        super(entityManager, applicationEventPublisher, patchService);
        this.precedenceRepository = precedenceRepository;
        this.precedenceHateoasHandler = precedenceHateoasHandler;
        this.userService = userService;
        this.metadataService = metadataService;
    }

    // All CREATE methods

    @Override
    @Transactional
    public PrecedenceHateoas createNewPrecedence(Precedence entity) {
        validatePrecedenceStatus(entity);
        User approvedBy = userService.validateUserReference
                (PRECEDENCE_APPROVED_BY, entity.getReferencePrecedenceApprovedBy(),
                        entity.getPrecedenceApprovedBy(),
                        entity.getReferencePrecedenceApprovedBySystemID());

        if (null != approvedBy) {
            entity.setPrecedenceApprovedBy(approvedBy.getUsername());
            entity.setReferencePrecedenceApprovedBySystemID(approvedBy.getSystemId());
            entity.setReferencePrecedenceApprovedBy(approvedBy);
        } // otherwise, accept value

        /* TODO check finalisedBy when ReferencePrecedenceFinalisedBy
        // is available.
        User finalisedBy = userService.validateUserReference
            (FINALISED_BY, entity.getReferencePrecedenceFinalisedBy(),
             entity.getPrecedenceFinalisedBy(),
             entity.getReferencePrecedenceFinalisedBySystemID());
        */

        PrecedenceHateoas precedenceHateoas =
                new PrecedenceHateoas(precedenceRepository.save(entity));
        precedenceHateoasHandler
                .addLinks(precedenceHateoas, new Authorisation());
        setOutgoingRequestHeader(precedenceHateoas);
        return precedenceHateoas;
    }

    // All READ methods

    @Override
    @SuppressWarnings("unchecked")
    public PrecedenceHateoas findAllByOwner() {
        PrecedenceHateoas precedenceHateoas =
                new PrecedenceHateoas((List<INoarkEntity>) (List)
                        precedenceRepository.findByOwnedBy(getUser()));
        precedenceHateoasHandler.addLinks(precedenceHateoas,
                new Authorisation());
        setOutgoingRequestHeader(precedenceHateoas);
        return precedenceHateoas;
    }

    @Override
    public PrecedenceHateoas findBySystemId(String precedenceSystemId) {
        PrecedenceHateoas precedenceHateoas =
                new PrecedenceHateoas(getPrecedenceOrThrow(precedenceSystemId));
        precedenceHateoasHandler.addLinks(precedenceHateoas,
                new Authorisation());
        setOutgoingRequestHeader(precedenceHateoas);
        return precedenceHateoas;
    }

    // All UPDATE methods

    @Override
    @Transactional
    public PrecedenceHateoas updatePrecedenceBySystemId
            (String systemId, Long version, Precedence incoming) {
        Precedence existing = getPrecedenceOrThrow(systemId);
        validatePrecedenceStatus(incoming);
        User approvedBy = userService.validateUserReference
                (PRECEDENCE_APPROVED_BY, incoming.getReferencePrecedenceApprovedBy(),
                        incoming.getPrecedenceApprovedBy(),
                        incoming.getReferencePrecedenceApprovedBySystemID());
        /* TODO use when getReferencePrecedenceFinalisedBy and friends
         * is available

        User finalisedBy = userService.validateUserReference
            (FINALISED_BY, incoming.getReferencePrecedenceFinalisedBy(),
             incoming.getPrecedenceFinalisedBy(),
             incoming.getReferencePrecedenceFinalisedBySystemID());
        */
        // Copy all the values you are allowed to copy ....
        existing.setTitle(incoming.getTitle());
        existing.setDescription(incoming.getDescription());
        existing.setPrecedenceDate(incoming.getPrecedenceDate());
        existing.setPrecedenceApprovedDate(incoming.getPrecedenceApprovedDate());
        if (null != approvedBy) {
            existing.setPrecedenceApprovedBy(approvedBy.getUsername());
            existing.setReferencePrecedenceApprovedBySystemID(approvedBy.getSystemId());
            existing.setReferencePrecedenceApprovedBy(approvedBy);
        } else {
            existing.setPrecedenceApprovedBy
                    (incoming.getPrecedenceApprovedBy());
            existing.setReferencePrecedenceApprovedBySystemID
                    (incoming.getReferencePrecedenceApprovedBySystemID());
            existing.setReferencePrecedenceApprovedBy(null);
        }
        existing.setPrecedenceAuthority(incoming.getPrecedenceAuthority());
        existing.setSourceOfLaw(incoming.getSourceOfLaw());
        existing.setPrecedenceStatus(incoming.getPrecedenceStatus());

        existing.setFinalisedDate(incoming.getFinalisedDate());
        existing.setFinalisedBy(incoming.getFinalisedBy());
        // TODO fix user reference lookup when NoarkGeneralEntity have
        // ReferenceFinalisedBy and friends.
        //existing.setReferenceFinalisedBy(incoming.getReferenceFinalisedBy());

        existing.setVersion(version);

        PrecedenceHateoas precedenceHateoas =
                new PrecedenceHateoas(precedenceRepository.save(existing));
        precedenceHateoasHandler.addLinks(precedenceHateoas,
                new Authorisation());
        setOutgoingRequestHeader(precedenceHateoas);
        return precedenceHateoas;
    }

    // All DELETE methods

    @Override
    @Transactional
    public void deletePrecedenceBySystemId(String systemID) {
        deleteEntity(getPrecedenceOrThrow(systemID));
    }

    @Override
    @Transactional
    public boolean deletePrecedenceIfNotEmpty(Precedence precedence) {
        if (precedence.getReferenceRegistryEntry().size() > 0) {
            return false;
        }
        if (precedence.getReferenceCaseFile().size() > 0) {
            return false;
        }
        precedenceRepository.delete(precedence);
        return true;
    }

    // All template methods

    public PrecedenceHateoas generateDefaultPrecedence() {
        Precedence template = new Precedence();
        template.setPrecedenceDate(OffsetDateTime.now());
        PrecedenceHateoas precedenceHateoas =
                new PrecedenceHateoas(template);
        precedenceHateoasHandler
                .addLinksOnTemplate(precedenceHateoas, new Authorisation());
        return precedenceHateoas;
    }

    // All helper methods

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, you
     * will only ever get a valid Precedence back. If there is no valid
     * Precedence, an exception is thrown
     *
     * @param precedenceSystemId systemID of the Precedence object to retrieve
     * @return the Precedence object
     */
    protected Precedence getPrecedenceOrThrow(
            @NotNull String precedenceSystemId) {
        Precedence precedence = precedenceRepository.
                findBySystemId(UUID.fromString(precedenceSystemId));
        if (precedence == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " Precedence, using systemId " + precedenceSystemId;
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
        return precedence;
    }

    private void validatePrecedenceStatus(Precedence incomingPrecedence) {
        if (null != incomingPrecedence.getPrecedenceStatus()) {
            PrecedenceStatus PrecedenceStatus =
                    (PrecedenceStatus) metadataService
                            .findValidMetadata(incomingPrecedence
                                    .getPrecedenceStatus());
            incomingPrecedence.setPrecedenceStatus(PrecedenceStatus);
        }
    }
}
