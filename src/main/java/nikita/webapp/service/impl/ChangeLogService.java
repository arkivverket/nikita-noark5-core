package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.ChangeLog;
import nikita.common.model.noark5.v5.hateoas.ChangeLogHateoas;
import nikita.common.repository.n5v5.IChangeLogRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IChangeLogHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.IChangeLogService;
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
public class ChangeLogService
        extends NoarkService
        implements IChangeLogService {

    private final IChangeLogRepository changeLogRepository;
    private final IChangeLogHateoasHandler changeLogHateoasHandler;

    public ChangeLogService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IChangeLogRepository changeLogRepository,
            IChangeLogHateoasHandler changeLogHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.changeLogRepository = changeLogRepository;
        this.changeLogHateoasHandler = changeLogHateoasHandler;
    }

    @Override
    public ChangeLogHateoas generateDefaultChangeLog() {
        ChangeLog defaultChangeLog = new ChangeLog();
        defaultChangeLog.setChangedDate(OffsetDateTime.now());
        defaultChangeLog.setChangedBy(getUser());
        defaultChangeLog.setVersion(-1L, true);
        return packAsHateoas(defaultChangeLog);
    }

    @Override
    @Transactional
    public ChangeLogHateoas createNewChangeLog(ChangeLog changeLog) {
        if (null == changeLog.getChangedDate())
            changeLog.setChangedDate(OffsetDateTime.now());
        return packAsHateoas(changeLogRepository.save(changeLog));
    }

    @Override
    public ChangeLogHateoas findChangeLogByOwner() {
        return (ChangeLogHateoas) odataService.processODataQueryGet();
    }

    @Override
    public ChangeLogHateoas findSingleChangeLog(@NotNull final UUID systemId) {
        return packAsHateoas(getChangeLogOrThrow(systemId));
    }

    @Override
    @Transactional
    public ChangeLogHateoas handleUpdate(@NotNull final UUID systemId,
                                         @NotNull final Long version,
                                         @NotNull ChangeLog incomingChangeLog) {
        ChangeLog existingChangeLog = getChangeLogOrThrow(systemId);
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
        return packAsHateoas(existingChangeLog);
    }

    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        deleteEntity(getChangeLogOrThrow(systemId));
    }


    public ChangeLogHateoas packAsHateoas(@NotNull final ChangeLog changeLog) {
        ChangeLogHateoas changeLogHateoas = new ChangeLogHateoas(changeLog);
        applyLinksAndHeader(changeLogHateoas, changeLogHateoasHandler);
        return changeLogHateoas;
    }

    protected ChangeLog getChangeLogOrThrow(@NotNull final UUID systemId) {
        ChangeLog changeLog = changeLogRepository.
                findBySystemId(systemId);
        if (changeLog == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " ChangeLog, using systemId " + systemId;
            throw new NoarkEntityNotFoundException(info);
        }
        return changeLog;
    }
}
