package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.repository.n5v5.secondary.IStorageLocationRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IStorageLocationHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.secondary.IStorageLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
public class StorageLocationService
        extends NoarkService
        implements IStorageLocationService {

    private static final Logger logger =
            LoggerFactory.getLogger(StorageLocationService.class);

    private final IStorageLocationRepository storageLocationRepository;
    private final IStorageLocationHateoasHandler storageLocationHateoasHandler;

    public StorageLocationService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IPatchService patchService,
            IStorageLocationRepository storageLocationRepository,
            IStorageLocationHateoasHandler storageLocationHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService);
        this.storageLocationRepository = storageLocationRepository;
        this.storageLocationHateoasHandler = storageLocationHateoasHandler;
    }

    // All CREATE methods

    @Override
    @Transactional
    public StorageLocationHateoas createStorageLocationAssociatedWithFile
            (StorageLocation storageLocation, File file) {
        storageLocation.addReferenceFile(file);
        storageLocation = storageLocationRepository.save(storageLocation);
        StorageLocationHateoas storageLocationHateoas =
                new StorageLocationHateoas(storageLocation);
        storageLocationHateoasHandler.addLinks(
                storageLocationHateoas, new Authorisation());
        setOutgoingRequestHeader(storageLocationHateoas);
        return storageLocationHateoas;
    }

    @Override
    @Transactional
    public StorageLocationHateoas createStorageLocationAssociatedWithFonds
            (StorageLocation storageLocation, Fonds fonds) {
        storageLocation.addReferenceFonds(fonds);
        storageLocation = storageLocationRepository.save(storageLocation);
        StorageLocationHateoas storageLocationHateoas =
                new StorageLocationHateoas(storageLocation);
        storageLocationHateoasHandler.addLinks(storageLocationHateoas,
                new Authorisation());
        setOutgoingRequestHeader(storageLocationHateoas);
        return storageLocationHateoas;
    }

    @Override
    @Transactional
    public StorageLocationHateoas createStorageLocationAssociatedWithSeries
            (StorageLocation storageLocation, Series series) {
        storageLocation.addReferenceSeries(series);
        storageLocation = storageLocationRepository.save(storageLocation);
        StorageLocationHateoas storageLocationHateoas =
                new StorageLocationHateoas(storageLocation);
        storageLocationHateoasHandler.addLinks(storageLocationHateoas,
                new Authorisation());
        setOutgoingRequestHeader(storageLocationHateoas);
        return storageLocationHateoas;
    }

    @Override
    @Transactional
    public StorageLocationHateoas createStorageLocationAssociatedWithRecord
            (StorageLocation storageLocation, Record record) {
        storageLocation.addReferenceRecord(record);
        storageLocation = storageLocationRepository.save(storageLocation);
        StorageLocationHateoas storageLocationHateoas =
                new StorageLocationHateoas(storageLocation);
        storageLocationHateoasHandler.addLinks(storageLocationHateoas,
                new Authorisation());
        setOutgoingRequestHeader(storageLocationHateoas);
        return storageLocationHateoas;
    }

    // All READ methods

    @Override
    public StorageLocationHateoas findBySystemId(UUID systemId) {
        StorageLocationHateoas storageLocationHateoas =
                new StorageLocationHateoas(getStorageLocationOrThrow(systemId));
        storageLocationHateoasHandler.addLinks(storageLocationHateoas,
                new Authorisation());
        setOutgoingRequestHeader(storageLocationHateoas);
        return storageLocationHateoas;
    }

    @Override
    public StorageLocationHateoas findAllByOwner() {
        StorageLocationHateoas storageLocationHateoas =
                new StorageLocationHateoas(List.copyOf(
                        storageLocationRepository.findByOwnedBy(getUser())));
        storageLocationHateoasHandler.addLinks(storageLocationHateoas,
                new Authorisation());
        setOutgoingRequestHeader(storageLocationHateoas);
        return storageLocationHateoas;
    }

    // All UPDATE methods

    @Override
    @Transactional
    public StorageLocationHateoas updateStorageLocationBySystemId(
            UUID systemId, Long version,
            StorageLocation incomingStorageLocation) {
        StorageLocation existingStorageLocation =
                getStorageLocationOrThrow(systemId);
        existingStorageLocation.setStorageLocation(
                incomingStorageLocation.getStorageLocation());
        existingStorageLocation.setVersion(version);
        StorageLocationHateoas storageLocationHateoas =
                new StorageLocationHateoas(existingStorageLocation);
        storageLocationHateoasHandler.addLinks(
                storageLocationHateoas, new Authorisation());
        setOutgoingRequestHeader(storageLocationHateoas);
        return storageLocationHateoas;
    }

    // All DELETE methods

    @Override
    @Transactional
    public void deleteStorageLocationBySystemId(UUID systemId) {
        StorageLocation storageLocation = getStorageLocationOrThrow(systemId);
        // Remove any associations between a Record and the given StorageLocation
        for (Fonds fonds : storageLocation.getReferenceFonds()) {
            fonds.removeReferenceStorageLocation(storageLocation);
        }
        // Remove any associations between a Series and the given StorageLocation
        for (Series series : storageLocation.getReferenceSeries()) {
            series.removeReferenceStorageLocation(storageLocation);
        }
        // Remove any associations between a File and the given StorageLocation
        for (File file : storageLocation.getReferenceFile()) {
            file.removeReferenceStorageLocation(storageLocation);
        }
        // Remove any associations between a Record and the given StorageLocation
        for (Record record : storageLocation.getReferenceRecord()) {
            record.removeReferenceStorageLocation(storageLocation);
        }
        storageLocationRepository.delete(storageLocation);
    }

    public boolean deleteStorageLocationIfNotEmpty(StorageLocation storageLocation) {
        if (storageLocation.getReferenceFonds().size() > 0) {
            return false;
        }
        if (storageLocation.getReferenceSeries().size() > 0) {
            return false;
        }
        if (storageLocation.getReferenceFile().size() > 0) {
            return false;
        }
        if (storageLocation.getReferenceRecord().size() > 0) {
            return false;
        }
        storageLocationRepository.delete(storageLocation);
        return true;
    }

    // All template methods

    public StorageLocationHateoas getDefaultStorageLocation(
            UUID systemId) {
        StorageLocation suggestedStorageLocation = new StorageLocation();
        StorageLocationHateoas storageLocationHateoas =
                new StorageLocationHateoas(suggestedStorageLocation);
        storageLocationHateoasHandler.addLinksOnTemplate(
                storageLocationHateoas, new Authorisation());
        return storageLocationHateoas;
    }

    // All helper methods

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, you
     * will only ever get a valid StorageLocation back. If there is no valid
     * StorageLocation, an exception is thrown
     *
     * @param storageLocationSystemId systemID of the StorageLocation object to retrieve
     * @return the StorageLocation object
     */
    protected StorageLocation getStorageLocationOrThrow(
            @NotNull UUID storageLocationSystemId) {
        StorageLocation storageLocation = storageLocationRepository.
                findBySystemId(storageLocationSystemId);
        if (storageLocation == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " StorageLocation, using systemId " + storageLocationSystemId;
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
        return storageLocation;
    }
}
