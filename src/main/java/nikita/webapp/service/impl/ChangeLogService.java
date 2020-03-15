package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.ChangeLog;
import nikita.common.model.noark5.v5.hateoas.ChangeLogHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.repository.n5v5.IChangeLogRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IChangeLogHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.IChangeLogService;
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

@Service
@Transactional
public class ChangeLogService
        extends NoarkService
        implements IChangeLogService {

    private static final Logger logger =
            LoggerFactory.getLogger(ChangeLogService.class);

    private IChangeLogRepository changeLogRepository;
    private IChangeLogHateoasHandler changeLogHateoasHandler;

    public ChangeLogService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IChangeLogRepository changeLogRepository,
            IChangeLogHateoasHandler changeLogHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.changeLogRepository = changeLogRepository;
        this.changeLogHateoasHandler = changeLogHateoasHandler;
    }

    @Override
    public ChangeLogHateoas generateDefaultChangeLog() {
        ChangeLog defaultChangeLog = new ChangeLog();

        defaultChangeLog.setChangedDate(OffsetDateTime.now());
        defaultChangeLog.setChangedBy(getUser());
        ChangeLogHateoas changeLogHateoas =
	    new ChangeLogHateoas(defaultChangeLog);
        changeLogHateoasHandler
	    .addLinksOnTemplate(changeLogHateoas, new Authorisation());
        return changeLogHateoas;
    }

    @Override
    public ChangeLogHateoas createNewChangeLog(ChangeLog changeLog) {
        if (null == changeLog.getChangedDate())
            changeLog.setChangedDate(OffsetDateTime.now());
        changeLog = changeLogRepository.save(changeLog);
        ChangeLogHateoas changeLogHateoas = new ChangeLogHateoas(changeLog);
        changeLogHateoasHandler.addLinks(changeLogHateoas, new Authorisation());
        return changeLogHateoas;
    }

    @Override
    public ChangeLogHateoas findChangeLogByOwner() {
        ChangeLogHateoas changeLogHateoas = new
                ChangeLogHateoas((List<INoarkEntity>) (List)
                changeLogRepository.findByOwnedBy(getUser()));
        changeLogHateoasHandler
            .addLinksOnRead(changeLogHateoas, new Authorisation());
        return changeLogHateoas;
    }

    @Override
    public ChangeLogHateoas findSingleChangeLog(String changeLogSystemId) {
        ChangeLog existingChangeLog = getChangeLogOrThrow(changeLogSystemId);

        ChangeLogHateoas changeLogHateoas =
            new ChangeLogHateoas(changeLogRepository.save(existingChangeLog));
        changeLogHateoasHandler.addLinks(changeLogHateoas, new Authorisation());
        return changeLogHateoas;
    }

    @Override
    public ChangeLogHateoas handleUpdate(@NotNull String changeLogSystemId,
                                       @NotNull Long version,
                                       @NotNull ChangeLog incomingChangeLog) {
        ChangeLog existingChangeLog = getChangeLogOrThrow(changeLogSystemId);
	/*
        // Copy all the values you are allowed to copy ....
        existingChangeLog.setChangeLogText(incomingChangeLog.getChangeLogText());
        existingChangeLog.setChangeLogDate(incomingChangeLog.getChangeLogDate());
        existingChangeLog.setChangeLogRegisteredBy
            (incomingChangeLog.getChangeLogRegisteredBy());
	*/
        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingChangeLog.setVersion(version);

        ChangeLogHateoas changeLogHateoas =
            new ChangeLogHateoas(changeLogRepository.save(existingChangeLog));
        changeLogHateoasHandler.addLinks(changeLogHateoas, new Authorisation());
        return changeLogHateoas;
    }

    public void deleteEntity(String systemID) {
        deleteEntity(getChangeLogOrThrow(systemID));
    }

    protected ChangeLog getChangeLogOrThrow(@NotNull String changeLogSystemId) {
        ChangeLog changeLog = changeLogRepository.
                findBySystemId(UUID.fromString(changeLogSystemId));
        if (changeLog == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " ChangeLog, using systemId " + changeLogSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return changeLog;
    }
}
