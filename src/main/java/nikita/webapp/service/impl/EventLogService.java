package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.EventLog;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.EventLogHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.repository.n5v5.IEventLogRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IEventLogHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.IEventLogService;
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
public class EventLogService
        extends NoarkService
        implements IEventLogService {

    private static final Logger logger =
            LoggerFactory.getLogger(EventLogService.class);

    private IEventLogRepository eventLogRepository;
    private IEventLogHateoasHandler eventLogHateoasHandler;

    public EventLogService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IEventLogRepository eventLogRepository,
            IEventLogHateoasHandler eventLogHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.eventLogRepository = eventLogRepository;
        this.eventLogHateoasHandler = eventLogHateoasHandler;
    }

    @Override
    public EventLogHateoas generateDefaultEventLog(SystemIdEntity entity) {
        EventLog defaultEventLog = new EventLog();

        defaultEventLog.setChangedDate(OffsetDateTime.now());
        defaultEventLog.setChangedBy(getUser());
        EventLogHateoas eventLogHateoas =
            new EventLogHateoas(defaultEventLog);
        eventLogHateoasHandler
            .addLinksOnTemplate(eventLogHateoas, new Authorisation());
        return eventLogHateoas;
    }

    @Override
    public EventLogHateoas createNewEventLog(EventLog eventLog,
                                             SystemIdEntity entity) {
        if (null == eventLog.getChangedDate())
            eventLog.setChangedDate(OffsetDateTime.now());
        eventLog = eventLogRepository.save(eventLog);
        EventLogHateoas eventLogHateoas = new EventLogHateoas(eventLog);
        eventLogHateoasHandler.addLinks(eventLogHateoas, new Authorisation());
        return eventLogHateoas;
    }

    @Override
    public EventLogHateoas findEventLogByOwner() {
        EventLogHateoas eventLogHateoas = new
                EventLogHateoas((List<INoarkEntity>) (List)
                eventLogRepository.findByOwnedBy(getUser()));
        eventLogHateoasHandler
            .addLinksOnRead(eventLogHateoas, new Authorisation());
        return eventLogHateoas;
    }

    @Override
    public EventLogHateoas findSingleEventLog(String eventLogSystemId) {
        EventLog existingEventLog = getEventLogOrThrow(eventLogSystemId);

        EventLogHateoas eventLogHateoas =
            new EventLogHateoas(eventLogRepository.save(existingEventLog));
        eventLogHateoasHandler.addLinks(eventLogHateoas, new Authorisation());
        return eventLogHateoas;
    }

    @Override
    public EventLogHateoas handleUpdate(@NotNull String eventLogSystemId,
                                       @NotNull Long version,
                                       @NotNull EventLog incomingEventLog) {
        EventLog existingEventLog = getEventLogOrThrow(eventLogSystemId);
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

        EventLogHateoas eventLogHateoas =
            new EventLogHateoas(eventLogRepository.save(existingEventLog));
        eventLogHateoasHandler.addLinks(eventLogHateoas, new Authorisation());
        return eventLogHateoas;
    }

    public void deleteEntity(String systemID) {
        deleteEntity(getEventLogOrThrow(systemID));
    }

    protected EventLog getEventLogOrThrow(@NotNull String eventLogSystemId) {
        EventLog eventLog = eventLogRepository.
                findBySystemId(UUID.fromString(eventLogSystemId));
        if (eventLog == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " EventLog, using systemId " + eventLogSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return eventLog;
    }
}
