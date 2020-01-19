package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.repository.n5v5.INationalIdentifierRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.nationalidentifier.INationalIdentifierHateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.IPositionHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.INationalIdentifierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
@Transactional
public class NationalIdentifierService
        extends NoarkService
        implements INationalIdentifierService {

    private static final Logger logger =
            LoggerFactory.getLogger(NationalIdentifierService.class);

    private final INationalIdentifierRepository nationalIdentifierRepository;
    private final INationalIdentifierHateoasHandler
            nationalIdentifierHateoasHandler;
    private final IPositionHateoasHandler positionHateoasHandler;

    public NationalIdentifierService
            (EntityManager entityManager,
             ApplicationEventPublisher applicationEventPublisher,
             INationalIdentifierRepository nationalIdentifierRepository,
             INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler,
             IPositionHateoasHandler positionHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.nationalIdentifierRepository = nationalIdentifierRepository;
        this.nationalIdentifierHateoasHandler =
                nationalIdentifierHateoasHandler;
        this.positionHateoasHandler = positionHateoasHandler;
    }

    @Override
    public NationalIdentifier findBySystemId(@NotNull String systemId) {
        return getNationalIdentifierOrThrow(systemId);
    }

    @Override
    public BuildingHateoas createNewBuilding
            (@NotNull Building building, @NotNull Record record) {
        record.addNationalIdentifier(building);
        building.setReferenceRecord(record);
        nationalIdentifierRepository.save(building);
        BuildingHateoas buildingHateoas = new BuildingHateoas(building);
        nationalIdentifierHateoasHandler
	    .addLinks(buildingHateoas, new Authorisation());
        return buildingHateoas;
    }

    @Override
    public BuildingHateoas createNewBuilding
            (@NotNull Building building, @NotNull File file) {
        file.addNationalIdentifier(building);
        building.setReferenceFile(file);
        BuildingHateoas buildingHateoas =
                new BuildingHateoas(
                        nationalIdentifierRepository.save(building));
        nationalIdentifierHateoasHandler.addLinks(
                buildingHateoas, new Authorisation());
        return buildingHateoas;
    }

    @Override
    public PositionHateoas createNewPosition
            (@NotNull Position position, @NotNull Record record) {
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        record.addNationalIdentifier(position);
        position.setReferenceRecord(record);
        nationalIdentifierRepository.save(position);
        PositionHateoas positionHateoas = new PositionHateoas(position);
        positionHateoasHandler.addLinks(positionHateoas, new Authorisation());
        return positionHateoas;
    }

    @Override
    public PositionHateoas createNewPosition
            (@NotNull Position position, @NotNull File file) {
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        file.addNationalIdentifier(position);
        position.setReferenceFile(file);
        nationalIdentifierRepository.save(position);
        PositionHateoas positionHateoas = new PositionHateoas(position);
        positionHateoasHandler.addLinks(positionHateoas, new Authorisation());
        return positionHateoas;
    }

    @Override
    public Building updateBuilding(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Building incomingBuilding) {
	Building existingBuilding =
	    (Building) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
	existingBuilding.setBuildingNumber
	    (incomingBuilding.getBuildingNumber());
	existingBuilding.setContinuousNumberingOfBuildingChange
	    (incomingBuilding.getContinuousNumberingOfBuildingChange());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingBuilding.setVersion(version);
        nationalIdentifierRepository.save(existingBuilding);
        return existingBuilding;
    }

    @Override
    public Position updatePosition(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Position incomingPosition) {
	Position existingPosition =
	    (Position) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingPosition.setX(incomingPosition.getX());
        existingPosition.setY(incomingPosition.getY());
        existingPosition.setZ(incomingPosition.getZ());
        existingPosition.setCoordinateSystemCode
            (incomingPosition.getCoordinateSystemCode());
        existingPosition.setCoordinateSystemCodeName
            (incomingPosition.getCoordinateSystemCodeName());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingPosition.setVersion(version);
        nationalIdentifierRepository.save(existingPosition);
        return existingPosition;
    }

    @Override
    public void deleteBuilding(@NotNull String systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public void deletePosition(@NotNull String systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    /**
     * Internal helper method. Rather than having a find and try catch
     * in multiple methods, we have it here once. If you call this, be
     * aware that you will only ever get a valid NationalIdentifier
     * back. If there is no valid NationalIdentifier, an exception is
     * thrown.
     *
     * @param systemId systemId of identifier to retrieve
     * @return the retrieved NationalIdentifier
     */
    private NationalIdentifier getNationalIdentifierOrThrow(
            @NotNull String systemId) {
        NationalIdentifier id = nationalIdentifierRepository.findBySystemId(
                UUID.fromString(systemId));
        if (id == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " NationalIdentifier, " +
                    "using systemId " + systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return id;
    }

    /**
     * Generate a Default Building object.
     *
     * @return the Building object wrapped as a BuildingHateoas object
     */
    @Override
    public BuildingHateoas generateDefaultBuilding() {
        Building building = new Building();
	// FIXME find way to return empty template
        building.setBuildingNumber(100);
        building.setContinuousNumberingOfBuildingChange(100);
        BuildingHateoas buildingHateoas =
                new BuildingHateoas(building);
        nationalIdentifierHateoasHandler
                .addLinksOnTemplate(buildingHateoas, new Authorisation());
        return buildingHateoas;
    }

    /**
     * Generate a Default Position object.
     *
     * @return the Position object wrapped as a PositionHateoas object
     */
    @Override
    public PositionHateoas generateDefaultPosition() {
        Position position = new Position();
        // FIXME find way to return empty template with only _links
        position.setCoordinateSystemCode("EPSG:4326");
        position.setCoordinateSystemCodeName("WGS84");
        position.setX(1.0);
        position.setY(1.0);
        position.setZ(1.0);
        PositionHateoas positionHateoas =
                new PositionHateoas(position);
        positionHateoasHandler
            .addLinksOnTemplate(positionHateoas, new Authorisation());
        return positionHateoas;
    }
}
