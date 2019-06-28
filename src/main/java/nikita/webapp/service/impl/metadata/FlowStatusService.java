package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.FlowStatus;
import nikita.common.repository.n5v5.metadata.IFlowStatusRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IFlowStatusService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.FLOW_STATUS;

/**
 * Created by tsodring on 17/02/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class FlowStatusService
        extends NoarkService
        implements IFlowStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(FlowStatusService.class);

    private IFlowStatusRepository flowStatusRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public FlowStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IFlowStatusRepository flowStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.flowStatusRepository = flowStatusRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new FlowStatus object to the database.
     *
     * @param flowStatus FlowStatus object with values set
     * @return the newly persisted FlowStatus object wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas createNewFlowStatus(
            FlowStatus flowStatus) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                flowStatusRepository.save(flowStatus));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all FlowStatus objects
     *
     * @return list of FlowStatus objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        flowStatusRepository.findAll(), FLOW_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single FlowStatus object identified by systemId
     *
     * @param systemId systemId of the FlowStatus you wish to retrieve
     * @return single FlowStatus object wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                flowStatusRepository
                        .findBySystemId(systemId));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all FlowStatus that have a given description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of FlowStatus objects wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        flowStatusRepository
                                .findByDescription(description), FLOW_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all FlowStatus that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of FlowStatus objects wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        flowStatusRepository.findByCode(code), FLOW_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default FlowStatus object
     *
     * @return the FlowStatus object wrapped as a FlowStatusHateoas object
     */
    @Override
    public FlowStatus generateDefaultFlowStatus() {

        FlowStatus flowStatus = new FlowStatus();
        flowStatus.setCode(TEMPLATE_FLOW_STATUS_CODE);
        flowStatus.setDescription(TEMPLATE_FLOW_STATUS_DESCRIPTION);

        return flowStatus;
    }

    /**
     * Update a FlowStatus identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId The systemId of the flowStatus object you wish to update
     * @param incomingFlowStatus The updated flowStatus object. Note the
     *                           values you are allowed to change are copied
     *                           from this object. This object is not persisted.
     * @return the updated flowStatus
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final FlowStatus incomingFlowStatus) {

        FlowStatus existingFlowStatus = getFlowStatusOrThrow(systemId);
        updateCodeAndDescription(incomingFlowStatus, existingFlowStatus);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingFlowStatus.setVersion(version);

        MetadataHateoas flowStatusHateoas = new MetadataHateoas(
                flowStatusRepository.save(existingFlowStatus));

        metadataHateoasHandler.addLinks(flowStatusHateoas, new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this, existingFlowStatus));
        return flowStatusHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid FlowStatus object back. If there is
     * no FlowStatus object, a NoarkEntityNotFoundException exception is thrown
     *
     * @param systemId The systemId of the FlowStatus object to retrieve
     * @return the FlowStatus object
     */
    private FlowStatus
    getFlowStatusOrThrow(@NotNull String systemId) {
        FlowStatus flowStatus = flowStatusRepository.findBySystemId(systemId);
        if (flowStatus == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " FlowStatus, using " +
                    "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return flowStatus;
    }
}
