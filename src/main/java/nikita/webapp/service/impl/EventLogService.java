package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.EventLog;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.EventLogHateoas;
import nikita.common.repository.n5v5.IEventLogRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IEventLogHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.IEventLogService;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
public class EventLogService
        extends NoarkService
        implements IEventLogService {

    private static final Logger logger =
            LoggerFactory.getLogger(EventLogService.class);

    private final IEventLogRepository eventLogRepository;
    private final IEventLogHateoasHandler eventLogHateoasHandler;

    public EventLogService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IEventLogRepository eventLogRepository,
            IEventLogHateoasHandler eventLogHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.eventLogRepository = eventLogRepository;
        this.eventLogHateoasHandler = eventLogHateoasHandler;
    }

    // All CREATE operations

    @Override
    @Transactional
    public EventLogHateoas createNewEventLog(
            @NotNull final EventLog eventLog,
            @NotNull final SystemIdEntity entity) {
        if (null == eventLog.getChangedDate()) {
            eventLog.setChangedDate(OffsetDateTime.now());
        }
        return packAsHateoas(eventLogRepository.save(eventLog));
    }

    // All READ operations

    @Override
    public EventLogHateoas findEventLogByOwner() {
        return (EventLogHateoas) odataService.processODataQueryGet();
    }

    @Override
    public EventLogHateoas findSingleEventLog(@NotNull final UUID systemId) {
        return packAsHateoas(getEventLogOrThrow(systemId));
    }

    // All UPDATE operations

    @Override
    @Transactional
    public EventLogHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final EventLog incomingEventLog) {
        EventLog existingEventLog = getEventLogOrThrow(systemId);
        /*
        // Copy all the values you are allowed to copy ....
        existingEventLog.setEventLogText(incomingEventLog.getEventLogText());
        existingEventLog.setEventLogDate(incomingEventLog.getEventLogDate());
        existingEventLog.setEventLogRegisteredBy
            (incomingEventLog.getEventLogRegisteredBy());
        */
        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingEventLog.setVersion(version);
        return packAsHateoas(existingEventLog);
    }

    // All DELETE operations

    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        deleteEntity(getEventLogOrThrow(systemId));
    }

    // All template operations

    @Override
    public EventLogHateoas generateDefaultEventLog(
            @NotNull final SystemIdEntity entity) {
        EventLog defaultEventLog = new EventLog();
        defaultEventLog.setChangedDate(OffsetDateTime.now());
        defaultEventLog.setChangedBy(getUser());
        defaultEventLog.setVersion(-1L, true);
        return packAsHateoas(defaultEventLog);
    }

    // All helper operations


    public EventLogHateoas packAsHateoas(
            @NotNull final EventLog eventLog) {
        EventLogHateoas eventLogHateoas = new EventLogHateoas(eventLog);
        applyLinksAndHeader(eventLogHateoas, eventLogHateoasHandler);
        return eventLogHateoas;
    }

    protected EventLog getEventLogOrThrow(@NotNull final UUID eventLogSystemId) {
        EventLog eventLog = eventLogRepository.findBySystemId(eventLogSystemId);
        if (eventLog == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " EventLog, using systemId " + eventLogSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return eventLog;
    }
}
