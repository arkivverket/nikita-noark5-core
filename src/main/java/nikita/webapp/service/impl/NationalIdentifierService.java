package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.metadata.CoordinateSystem;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.repository.n5v5.INationalIdentifierRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.nationalidentifier.*;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.INationalIdentifierService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.COORDINATE_SYSTEM;

@Service
public class NationalIdentifierService
        extends NoarkService
        implements INationalIdentifierService {

    private final INationalIdentifierRepository nationalIdentifierRepository;
    private final IBuildingHateoasHandler buildingHateoasHandler;
    private final ICadastralUnitHateoasHandler cadastralUnitHateoasHandler;
    private final IDNumberHateoasHandler dNumberHateoasHandler;
    private final IPlanHateoasHandler planHateoasHandler;
    private final IPositionHateoasHandler positionHateoasHandler;
    private final ISocialSecurityNumberHateoasHandler
            socialSecurityNumberHateoasHandler;
    private final IUnitHateoasHandler unitHateoasHandler;
    private final IMetadataService metadataService;

    public NationalIdentifierService
            (EntityManager entityManager,
             ApplicationEventPublisher applicationEventPublisher,
             IODataService odataService,
             IPatchService patchService,
             INationalIdentifierRepository nationalIdentifierRepository,
             IBuildingHateoasHandler buildingHateoasHandler,
             ICadastralUnitHateoasHandler cadastralUnitHateoasHandler,
             IDNumberHateoasHandler dNumberHateoasHandler,
             IPlanHateoasHandler planHateoasHandler,
             IPositionHateoasHandler positionHateoasHandler,
             ISocialSecurityNumberHateoasHandler
                     socialSecurityNumberHateoasHandler,
             IUnitHateoasHandler unitHateoasHandler,
             IMetadataService metadataService) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.nationalIdentifierRepository = nationalIdentifierRepository;
        this.buildingHateoasHandler = buildingHateoasHandler;
        this.cadastralUnitHateoasHandler = cadastralUnitHateoasHandler;
        this.dNumberHateoasHandler = dNumberHateoasHandler;
        this.planHateoasHandler = planHateoasHandler;
        this.positionHateoasHandler = positionHateoasHandler;
        this.socialSecurityNumberHateoasHandler = socialSecurityNumberHateoasHandler;
        this.unitHateoasHandler = unitHateoasHandler;
        this.metadataService = metadataService;
    }

    // All CREATE methods

    @Override
    @Transactional
    public BuildingHateoas createNewBuilding(
            @NotNull final Building building, @NotNull final Record record) {
        record.addNationalIdentifier(building);
        building.setReferenceRecord(record);
        return packAsHateoas(nationalIdentifierRepository.save(building));
    }

    @Override
    @Transactional
    public BuildingHateoas createNewBuilding(
            @NotNull final Building building, @NotNull final File file) {
        file.addNationalIdentifier(building);
        building.setReferenceFile(file);
        return packAsHateoas(nationalIdentifierRepository.save(building));
    }

    @Override
    @Transactional
    public CadastralUnitHateoas createNewCadastralUnit
            (@NotNull final CadastralUnit cadastralUnit,
             @NotNull final Record record) {
        record.addNationalIdentifier(cadastralUnit);
        cadastralUnit.setReferenceRecord(record);
        return packAsHateoas(nationalIdentifierRepository.save(cadastralUnit));
    }

    @Override
    @Transactional
    public CadastralUnitHateoas createNewCadastralUnit(
            @NotNull final CadastralUnit cadastralUnit,
            @NotNull final File file) {
        file.addNationalIdentifier(cadastralUnit);
        cadastralUnit.setReferenceFile(file);
        return packAsHateoas(nationalIdentifierRepository.save(cadastralUnit));
    }

    @Override
    @Transactional
    public DNumberHateoas createNewDNumber(
            @NotNull final DNumber dNumber, @NotNull final Record record) {
        record.addNationalIdentifier(dNumber);
        dNumber.setReferenceRecord(record);
        return packAsHateoas(nationalIdentifierRepository.save(dNumber));
    }

    @Override
    @Transactional
    public DNumberHateoas createNewDNumber(
            @NotNull final DNumber dNumber, @NotNull final File file) {
        file.addNationalIdentifier(dNumber);
        dNumber.setReferenceFile(file);
        return packAsHateoas(nationalIdentifierRepository.save(dNumber));
    }

    @Override
    @Transactional
    public PlanHateoas createNewPlan(
            @NotNull final Plan plan, @NotNull final Record record) {
        record.addNationalIdentifier(plan);
        plan.setReferenceRecord(record);
        return packAsHateoas(nationalIdentifierRepository.save(plan));
    }

    @Override
    @Transactional
    public PlanHateoas createNewPlan(
            @NotNull final Plan plan, @NotNull final File file) {
        file.addNationalIdentifier(plan);
        plan.setReferenceFile(file);
        return packAsHateoas(nationalIdentifierRepository.save(plan));
    }

    @Override
    @Transactional
    public PositionHateoas createNewPosition(
            @NotNull Position position, @NotNull Record record) {
        record.addNationalIdentifier(position);
        return packAsHateoas(nationalIdentifierRepository.save(position));
    }

    @Override
    @Transactional
    public PositionHateoas createNewPosition(
            @NotNull Position position, @NotNull File file) {
        file.addNationalIdentifier(position);
        return packAsHateoas(nationalIdentifierRepository.save(position));
    }

    @Override
    @Transactional
    public SocialSecurityNumberHateoas createNewSocialSecurityNumber(
            @NotNull final SocialSecurityNumber socialSecurityNumber,
            @NotNull final Record record) {
        record.addNationalIdentifier(socialSecurityNumber);
        socialSecurityNumber.setReferenceRecord(record);
        return packAsHateoas(
                nationalIdentifierRepository.save(socialSecurityNumber));
    }

    @Override
    @Transactional
    public SocialSecurityNumberHateoas createNewSocialSecurityNumber(
            @NotNull final SocialSecurityNumber socialSecurityNumber,
            @NotNull final File file) {
        file.addNationalIdentifier(socialSecurityNumber);
        socialSecurityNumber.setReferenceFile(file);
        return packAsHateoas(nationalIdentifierRepository
                .save(socialSecurityNumber));
    }

    @Override
    @Transactional
    public UnitHateoas createNewUnit(
            @NotNull final Unit unit, @NotNull final Record record) {
        record.addNationalIdentifier(unit);
        return packAsHateoas(nationalIdentifierRepository.save(unit));
    }

    @Override
    @Transactional
    public UnitHateoas createNewUnit(
            @NotNull final Unit unit, @NotNull final File file) {
        file.addNationalIdentifier(unit);
        return packAsHateoas(nationalIdentifierRepository.save(unit));
    }

    // All READ methods

    @Override
    public BuildingHateoas findBuildingBySystemId(
            @NotNull final UUID systemId) {
        return packAsHateoas((Building) getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public UnitHateoas findUnitBySystemId(
            @NotNull final UUID systemId) {
        return packAsHateoas((Unit) getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public CadastralUnitHateoas findCadastralUnitBySystemId(
            @NotNull final UUID systemId) {
        return packAsHateoas((CadastralUnit) getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public PositionHateoas findPositionBySystemId(
            @NotNull final UUID systemId) {
        return packAsHateoas((Position) getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public PlanHateoas findPlanBySystemId(
            @NotNull final UUID systemId) {
        return packAsHateoas((Plan) getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public SocialSecurityNumberHateoas findSocialSecurityNumberBySystemId(
            @NotNull final UUID systemId) {
        return packAsHateoas((SocialSecurityNumber) getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public DNumberHateoas findDNumberBySystemId(
            @NotNull final UUID systemId) {
        return packAsHateoas((DNumber) getNationalIdentifierOrThrow(systemId));
    }

    // All UPDATE methods

    @Override
    @Transactional
    public BuildingHateoas updateBuilding(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final Building incomingBuilding) {
        Building existingBuilding =
                (Building) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingBuilding.setBuildingNumber
                (incomingBuilding.getBuildingNumber());
        existingBuilding.setRunningChangeNumber
                (incomingBuilding.getRunningChangeNumber());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingBuilding.setVersion(version);
        return packAsHateoas(existingBuilding);
    }

    @Override
    @Transactional
    public CadastralUnitHateoas updateCadastralUnit(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final CadastralUnit incomingCadastralUnit) {
        CadastralUnit existingCadastralUnit =
                (CadastralUnit) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingCadastralUnit.setMunicipalityNumber
                (incomingCadastralUnit.getMunicipalityNumber());
        existingCadastralUnit.setHoldingNumber
                (incomingCadastralUnit.getHoldingNumber());
        existingCadastralUnit.setSubHoldingNumber
                (incomingCadastralUnit.getSubHoldingNumber());
        existingCadastralUnit.setLeaseNumber
                (incomingCadastralUnit.getLeaseNumber());
        existingCadastralUnit.setSectionNumber
                (incomingCadastralUnit.getSectionNumber());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingCadastralUnit.setVersion(version);
        return packAsHateoas(existingCadastralUnit);
    }

    @Override
    @Transactional
    public DNumberHateoas updateDNumber(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final DNumber incomingDNumber) {
        DNumber existingDNumber =
                (DNumber) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingDNumber.setdNumber
                (incomingDNumber.getdNumber());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingDNumber.setVersion(version);
        return packAsHateoas(existingDNumber);
    }

    @Override
    @Transactional
    public PlanHateoas updatePlan(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final Plan incomingPlan) {
        Plan existingPlan =
                (Plan) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingPlan.setMunicipalityNumber
                (incomingPlan.getMunicipalityNumber());
        existingPlan.setCountyNumber
                (incomingPlan.getCountyNumber());
        existingPlan.setCountry
                (incomingPlan.getCountry());
        existingPlan.setPlanIdentification
                (incomingPlan.getPlanIdentification());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingPlan.setVersion(version);
        return packAsHateoas(existingPlan);
    }

    @Override
    @Transactional
    public PositionHateoas updatePosition(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final Position incomingPosition) {
        Position existingPosition =
                (Position) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingPosition.setX(incomingPosition.getX());
        existingPosition.setY(incomingPosition.getY());
        existingPosition.setZ(incomingPosition.getZ());
        existingPosition.setCoordinateSystem
                (incomingPosition.getCoordinateSystem());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingPosition.setVersion(version);
        return packAsHateoas(existingPosition);
    }

    @Override
    @Transactional
    public SocialSecurityNumberHateoas updateSocialSecurityNumber(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final SocialSecurityNumber incomingSocialSecurityNumber) {
        SocialSecurityNumber existingSocialSecurityNumber =
                (SocialSecurityNumber) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingSocialSecurityNumber.setSocialSecurityNumber
                (incomingSocialSecurityNumber.getSocialSecurityNumber());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingSocialSecurityNumber.setVersion(version);
        return packAsHateoas(existingSocialSecurityNumber);
    }

    @Override
    @Transactional
    public UnitHateoas updateUnit(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final Unit incomingUnit) {
        Unit existingUnit =
                (Unit) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingUnit
                .setUnitIdentifier(incomingUnit.getUnitIdentifier());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingUnit.setVersion(version);
        return packAsHateoas(existingUnit);
    }

    // All DELETE methods

    @Override
    @Transactional
    public void deleteBuilding(@NotNull final UUID systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    @Transactional
    public void deleteCadastralUnit(@NotNull final UUID systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    @Transactional
    public void deleteDNumber(@NotNull final UUID systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    @Transactional
    public void deletePlan(@NotNull final UUID systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    @Transactional
    public void deletePosition(@NotNull final UUID systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    @Transactional
    public void deleteSocialSecurityNumber(@NotNull final UUID systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    @Transactional
    public void deleteUnit(@NotNull final UUID systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    // All template methods

    /**
     * Generate a Default Building object.
     *
     * @return the Building object wrapped as a BuildingHateoas object
     */
    @Override
    public BuildingHateoas generateDefaultBuilding(
            @NotNull final UUID systemId) {
        Building building = new Building();
        building.setVersion(-1L, true);
        return packAsHateoas(building);
    }

    /**
     * Generate a Default CadastralUnit object.
     *
     * @return the CadastralUnit object wrapped as a CadastralUnitHateoas object
     */
    @Override
    public CadastralUnitHateoas generateDefaultCadastralUnit(
            @NotNull final UUID systemId) {
        CadastralUnit cadastralUnit = new CadastralUnit();
        cadastralUnit.setVersion(-1L, true);
        return packAsHateoas(cadastralUnit);
    }

    /**
     * Generate a Default DNumber object.
     *
     * @return the DNumber object wrapped as a DNumberHateoas object
     */
    @Override
    public DNumberHateoas generateDefaultDNumber(@NotNull final UUID systemId) {
        DNumber dNumber = new DNumber();
        dNumber.setVersion(-1L, true);
        return packAsHateoas(dNumber);
    }

    /**
     * Generate a Default Plan object.
     *
     * @return the Plan object wrapped as a PlanHateoas object
     */
    @Override
    public PlanHateoas generateDefaultPlan(@NotNull final UUID systemId) {
        Plan plan = new Plan();
        plan.setVersion(-1L, true);
        return packAsHateoas(plan);
    }

    /**
     * Generate a Default Position object.
     *
     * @return the Position object wrapped as a PositionHateoas object
     */
    @Override
    public PositionHateoas generateDefaultPosition(
            @NotNull final UUID systemId) {
        Position position = new Position();
        CoordinateSystem coordinateSystem = (CoordinateSystem)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (COORDINATE_SYSTEM, "EPSG:4326", "WGS84");
        position.setCoordinateSystem(coordinateSystem);
        position.setVersion(-1L, true);
        return packAsHateoas(position);
    }

    /**
     * Generate a Default SocialSecurityNumber object.
     *
     * @return the SocialSecurityNumber object wrapped as a SocialSecurityNumberHateoas object
     */
    @Override
    public SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber(
            @NotNull final UUID systemId) {
        SocialSecurityNumber socialSecurityNumber = new SocialSecurityNumber();
        socialSecurityNumber.setVersion(-1L, true);
        return packAsHateoas(socialSecurityNumber);
    }

    /**
     * Generate a Default Unit object.
     *
     * @return the Unit object wrapped as a UnitHateoas object
     */
    @Override
    public UnitHateoas generateDefaultUnit(@NotNull final UUID systemId) {
        Unit unit = new Unit();
        unit.setVersion(-1L, true);
        return packAsHateoas(unit);
    }

    // All helper methods

    public SocialSecurityNumberHateoas packAsHateoas(
            @NotNull final SocialSecurityNumber socialSecurityNumber) {
        SocialSecurityNumberHateoas socialSecurityNumberHateoas =
                new SocialSecurityNumberHateoas(socialSecurityNumber);
        applyLinksAndHeader(socialSecurityNumberHateoas,
                socialSecurityNumberHateoasHandler);
        return socialSecurityNumberHateoas;
    }

    public UnitHateoas packAsHateoas(@NotNull final Unit unit) {
        UnitHateoas unitHateoas = new UnitHateoas(unit);
        applyLinksAndHeader(unitHateoas, unitHateoasHandler);
        return unitHateoas;
    }

    public PositionHateoas packAsHateoas(@NotNull final Position position) {
        PositionHateoas positionHateoas = new PositionHateoas(position);
        applyLinksAndHeader(positionHateoas, positionHateoasHandler);
        return positionHateoas;
    }

    public PlanHateoas packAsHateoas(@NotNull final Plan plan) {
        PlanHateoas planHateoas = new PlanHateoas(plan);
        applyLinksAndHeader(planHateoas, planHateoasHandler);
        return planHateoas;
    }

    public DNumberHateoas packAsHateoas(@NotNull final DNumber dNumber) {
        DNumberHateoas dNumberHateoas = new DNumberHateoas(dNumber);
        applyLinksAndHeader(dNumberHateoas, dNumberHateoasHandler);
        return dNumberHateoas;
    }

    public CadastralUnitHateoas packAsHateoas(
            @NotNull final CadastralUnit cadastralUnit) {
        CadastralUnitHateoas cadastralUnitHateoas =
                new CadastralUnitHateoas(cadastralUnit);
        applyLinksAndHeader(cadastralUnitHateoas, cadastralUnitHateoasHandler);
        return cadastralUnitHateoas;
    }

    public BuildingHateoas packAsHateoas(@NotNull final Building building) {
        BuildingHateoas buildingHateoas = new BuildingHateoas(building);
        applyLinksAndHeader(buildingHateoas, buildingHateoasHandler);
        return buildingHateoas;
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
            @NotNull final UUID systemId) {
        NationalIdentifier id = nationalIdentifierRepository
                .findBySystemId(systemId);
        if (id == null) {
            String error = INFO_CANNOT_FIND_OBJECT + " NationalIdentifier, " +
                    "using systemId " + systemId;
            throw new NoarkEntityNotFoundException(error);
        }
        return id;
    }
}
