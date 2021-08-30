package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.CrossReferenceHateoas;
import nikita.common.model.noark5.v5.interfaces.ICrossReference;
import nikita.common.model.noark5.v5.secondary.CrossReference;
import nikita.common.repository.n5v5.ISystemIdEntityRepository;
import nikita.common.repository.n5v5.secondary.ICrossReferenceRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import nikita.common.util.exceptions.NoarkConflictException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.ICrossReferenceHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.ICrossReferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.ErrorMessagesConstants.CROSS_REFERENCE_BAD_SYSTEM_ID;
import static nikita.common.config.ErrorMessagesConstants.CROSS_REFERENCE_DUPLICATE;
import static nikita.common.config.N5ResourceMappings.*;

@Service
public class CrossReferenceService
        extends NoarkService
        implements ICrossReferenceService {

    private static final Logger logger =
            LoggerFactory.getLogger(CrossReferenceService.class);

    private final ISystemIdEntityRepository systemIdEntityRepository;
    private final ICrossReferenceRepository crossReferenceRepository;
    private final ICrossReferenceHateoasHandler crossReferenceHateoasHandler;

    public CrossReferenceService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            ISystemIdEntityRepository systemIdEntityRepository,
            ICrossReferenceRepository crossReferenceRepository,
            ICrossReferenceHateoasHandler crossReferenceHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.systemIdEntityRepository = systemIdEntityRepository;
        this.crossReferenceRepository = crossReferenceRepository;
        this.crossReferenceHateoasHandler = crossReferenceHateoasHandler;
    }

    // All CREATE methods

    @Override
    @Transactional
    public CrossReferenceHateoas createCrossReferenceAssociatedWithFile(
            @NotNull final CrossReference crossReference,
            @NotNull final File file) {
        return createCrossReference(file, crossReference, FILE, FILE_ENG);
    }

    @Override
    @Transactional
    public CrossReferenceHateoas createCrossReferenceAssociatedWithClass(
            @NotNull final CrossReference crossReference,
            @NotNull final Class klass) {
        return createCrossReference(klass, crossReference, CLASS, CLASS_ENG);
    }

    @Override
    @Transactional
    public CrossReferenceHateoas createCrossReferenceAssociatedWithRecord(
            @NotNull final CrossReference crossReference,
            @NotNull final RecordEntity record) {
        return createCrossReference(record, crossReference, RECORD, RECORD_ENG);
    }

    private CrossReferenceHateoas createCrossReference(
            @NotNull final ICrossReference entity,
            @NotNull final CrossReference crossReference,
            @NotNull final String entityType,
            @NotNull final String fromReferenceType) {
        validateIncomingCrossReference(crossReference);
        String type = getReferenceTypeIfOK(crossReference, entityType);
        crossReference.setReferenceType(getReferenceType(type));
        crossReference.setFromReferenceType(fromReferenceType);
        entity.addCrossReference(crossReference);
        return packAsHateoas(crossReferenceRepository.save(crossReference));
    }

    // All READ methods

    @Override
    public CrossReferenceHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getCrossReferenceOrThrow(systemId));
    }

    @Override
    public CrossReferenceHateoas findAll() {
        return (CrossReferenceHateoas) odataService.processODataQueryGet();
    }

    // All UPDATE methods

    @Override
    public CrossReferenceHateoas updateCrossReferenceBySystemId(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final CrossReference incomingCrossReference) {
        CrossReference existingCrossReference =
                getCrossReferenceOrThrow(systemId);
        String type = getReferenceTypeIfOK(incomingCrossReference,
                getEntityType(existingCrossReference));
        existingCrossReference.setReferenceType(
                // File / Record / Class
                getReferenceType(type));
        existingCrossReference.setVersion(version);
        validateNotDuplicate(incomingCrossReference);
        existingCrossReference.setToSystemId(
                incomingCrossReference.getToSystemId());
        return packAsHateoas(existingCrossReference);
    }

    // All DELETE methods

    @Override
    @Transactional
    public void deleteCrossReferenceBySystemId(@NotNull final UUID systemId) {
        CrossReference crossReference = getCrossReferenceOrThrow(systemId);
        if (null != crossReference.getReferenceClass()) {
            crossReference.getReferenceClass()
                    .removeCrossReference(crossReference);
        }
        if (null != crossReference.getReferenceFile()) {
            crossReference.getReferenceFile()
                    .removeCrossReference(crossReference);
        }
        if (null != crossReference.getReferenceRecordEntity()) {
            crossReference.getReferenceRecordEntity()
                    .removeCrossReference(crossReference);
        }
        crossReferenceRepository.delete(crossReference);
    }

    // All template methods

    @Override
    public CrossReferenceHateoas getDefaultCrossReference(
            @NotNull final UUID systemId) {
        CrossReference suggestedCrossReference = new CrossReference();
        suggestedCrossReference.setVersion(-1L, true);
        return packAsHateoas(suggestedCrossReference);
    }

    // All helper methods

    private String getReferenceType(@NotNull final String baseType) {
        switch (baseType) {
            case FILE:
                return REFERENCE_TO_FILE;
            case RECORD:
                return REFERENCE_TO_REGISTRATION;
            case CLASS:
                return REFERENCE_TO_CLASS;
        }
        throw new NikitaMalformedInputDataException("When " +
                "working on a CrossReference nikita came across a" +
                " referenceType that " + baseType + " cannot be processed");

    }

    private String getEntityType(@NotNull final CrossReference crossReference) {
        if (null != crossReference.getReferenceType() &&
                !crossReference.getReferenceType().isBlank()) {
            switch (crossReference.getFromReferenceType()) {
                case FILE_ENG:
                    return FILE;
                case CLASS_ENG:
                    return CLASS;
                case RECORD_ENG:
                    return RECORD;
            }
        } else {
            throw new NikitaMalformedInputDataException(crossReference + " has a " +
                    "referenceType that cannot be processed");
        }
        // This should never occur.
        throw new NikitaMisconfigurationException(crossReference + " has a " +
                "referenceType that cannot be processed");
    }

    /**
     * @param crossReference The CrossReference to check
     * @param typeFrom       the object type
     */
    private String getReferenceTypeIfOK(
            @NotNull final CrossReference crossReference,
            @NotNull final String typeFrom) {
        SystemIdEntity entity = getToObjectOrThrow(
                crossReference.getToSystemId());
        String typeTo = entity.getBaseTypeName();
        if (((typeFrom.equals(FILE) || typeFrom.equals(RECORD)) &&
                (typeTo.equals(FILE) || typeTo.equals(RECORD)))
                || (typeFrom.equals(CLASS) && typeTo.equals(CLASS))) {
            return typeTo;
        }
        throw new NikitaMalformedInputDataException("It is not " +
                "possible to create a CrossReference from a " + typeFrom + " " +
                "to a " + typeTo);
    }

    /**
     * Check that the systemId of the request URL matches the value that is
     * in the fromSystemID field. If they don't match throw a BAD REQUEST
     * (400) exception. This test is so that we can trust the value i
     * fromSystemId. Also if the incoming request does not match, then the
     * client does not know what they are doing.
     * Then check that there is not an e
     *
     * @param crossReference the incoming CrossReference object
     */
    private void validateIncomingCrossReference(
            @NotNull final CrossReference crossReference) {
        UUID systemId = getFirstSystemIDFromRequest();
        if (!crossReference.getFromSystemId().equals(systemId)) {
            String error = String.format(CROSS_REFERENCE_BAD_SYSTEM_ID,
                    crossReference.getFromSystemId().toString(), systemId);
            throw new NikitaMalformedInputDataException(error);
        }
        validateNotDuplicate(crossReference);
    }

    private void validateNotDuplicate(
            @NotNull final CrossReference crossReference) {
        UUID fromSystemId = crossReference.getFromSystemId();
        UUID toSystemId = crossReference.getToSystemId();
        Optional<CrossReference> crossReferenceOpt =
                crossReferenceRepository
                        .findByFromSystemIdAndToSystemId(
                                fromSystemId, toSystemId);
        if (crossReferenceOpt.isPresent()) {
            String error = String.format(CROSS_REFERENCE_DUPLICATE,
                    fromSystemId, toSystemId);
            throw new NoarkConflictException(error);
        }
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, you
     * will only ever get a valid object (arkivenhet) back. If there is no valid
     * object (arkivenhet), an exception is thrown
     *
     * @param systemId the systemId of the object to to retrieve
     * @return the (arkivenhet) object
     */
    protected SystemIdEntity getToObjectOrThrow(
            @NotNull final UUID systemId) {
        Optional<SystemIdEntity> systemIdEntityOpt =
                systemIdEntityRepository.findById(systemId);
        if (systemIdEntityOpt.isEmpty()) {
            throw new NoarkEntityNotFoundException(INFO_CANNOT_FIND_OBJECT +
                    " with systemId " + systemId + " " +
                    "that was attempted to be associated with CrossReference");
        }
        return systemIdEntityOpt.get();
    }

    private CrossReferenceHateoas packAsHateoas(CrossReference crossReference) {
        CrossReferenceHateoas crossReferenceHateoas =
                new CrossReferenceHateoas(crossReference);
        applyLinksAndHeader(crossReferenceHateoas, crossReferenceHateoasHandler);
        return crossReferenceHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, you
     * will only ever get a valid CrossReference back. If there is no valid
     * CrossReference, an exception is thrown
     *
     * @param crossReferenceSystemId systemId of the CrossReference object to
     *                               retrieve
     * @return the CrossReference object
     */
    protected CrossReference getCrossReferenceOrThrow(
            @NotNull final UUID crossReferenceSystemId) {
        CrossReference crossReference = crossReferenceRepository.
                findBySystemId(crossReferenceSystemId);
        if (crossReference == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " CrossReference, using systemId " + crossReferenceSystemId;
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
        return crossReference;
    }
}
