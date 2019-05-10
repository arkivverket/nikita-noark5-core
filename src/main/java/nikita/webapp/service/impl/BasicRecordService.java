package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.BasicRecord;
import nikita.common.repository.n5v4.IBasicRecordRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.interfaces.IBasicRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.List;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
@Transactional
public class BasicRecordService
        extends NoarkService
        implements IBasicRecordService {

    private static final Logger logger =
            LoggerFactory.getLogger(BasicRecordService.class);

    private IBasicRecordRepository basicRecordRepository;

    public BasicRecordService(EntityManager entityManager,
                              ApplicationEventPublisher
                                      applicationEventPublisher,
                              IBasicRecordRepository basicRecordRepository) {
        super(entityManager, applicationEventPublisher);
        this.basicRecordRepository = basicRecordRepository;
    }

    // All READ operations
    @Override
    public List<BasicRecord> findAllBasicRecordByOwner() {
        String loggedInUser = SecurityContextHolder.getContext().
                getAuthentication().getName();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BasicRecord> criteriaQuery =
                criteriaBuilder.createQuery(BasicRecord.class);
        Root<BasicRecord> from = criteriaQuery.from(BasicRecord.class);
        CriteriaQuery<BasicRecord> select = criteriaQuery.select(from);

        criteriaQuery.where(criteriaBuilder.equal(from.get("ownedBy"),
                loggedInUser));
        TypedQuery<BasicRecord> typedQuery = entityManager.createQuery(select);
        return typedQuery.getResultList();
    }

    // systemId
    public BasicRecord findBySystemId(String systemId) {
        return getBasicRecordOrThrow(systemId);
    }

    // All UPDATE operations

    /**
     * Updates a BasicRecord object in the database. First we try to locate the
     * BasicRecord object. If the BasicRecord object does not exist a
     * NoarkEntityNotFoundException exception is thrown that the caller has
     * to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingBasicRecord object to the existingBasicRecord object and the
     * existingBasicRecord object will be persisted to the database when the
     * transaction boundary is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the BasicRecord object from
     * the database a NoarkConcurrencyException is thrown. Note. This happens
     * when the call to BasicRecord.setVersion() occurs.
     *
     * @param systemId            systemId of the incoming basicRecord object
     * @param version             ETag version
     * @param incomingBasicRecord the incoming basicRecord
     * @return the updated basicRecord object after it is persisted
     */
    @Override
    public BasicRecord handleUpdate(@NotNull final String systemId,
                                    @NotNull final Long version,
                                    @NotNull final
                                    BasicRecord incomingBasicRecord) {
        BasicRecord existingBasicRecord = getBasicRecordOrThrow(systemId);
        // Here copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingBasicRecord, existingBasicRecord);
        if (null != incomingBasicRecord.getDocumentMedium()) {
            existingBasicRecord.setDocumentMedium(
                    incomingBasicRecord.getDocumentMedium());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingBasicRecord.setVersion(version);
        basicRecordRepository.save(existingBasicRecord);
        return existingBasicRecord;
    }

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String basicRecordSystemId) {
        BasicRecord basicRecord = getBasicRecordOrThrow(basicRecordSystemId);
        basicRecordRepository.delete(basicRecord);
    }

    // All HELPER operations
    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this you
     * will only ever get a valid BasicRecord back. If there is no valid
     * BasicRecord, an exception is thrown
     *
     * @param basicRecordSystemId systemID of the BasicRecord to retrieve
     * @return the BascRecord object
     */
    protected BasicRecord getBasicRecordOrThrow(
            @NotNull String basicRecordSystemId) {
        BasicRecord basicRecord = basicRecordRepository.
                findBySystemId(basicRecordSystemId);
        if (basicRecord == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " BasicRecord, using systemId " + basicRecordSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return basicRecord;
    }
}
