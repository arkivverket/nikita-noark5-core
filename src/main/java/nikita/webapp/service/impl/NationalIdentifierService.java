package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.CadastralUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.DNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PlanHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.SocialSecurityNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.UnitHateoas;
import nikita.common.model.noark5.v5.metadata.CoordinateSystem;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit;
import nikita.common.model.noark5.v5.nationalidentifier.DNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Plan;
import nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Unit;
import nikita.common.repository.n5v5.INationalIdentifierRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.nationalidentifier.INationalIdentifierHateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.IPositionHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.INationalIdentifierService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.COORDINATE_SYSTEM;

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
    private final IMetadataService metadataService;
    private final IPositionHateoasHandler positionHateoasHandler;

    public NationalIdentifierService
            (EntityManager entityManager,
             ApplicationEventPublisher applicationEventPublisher,
             INationalIdentifierRepository nationalIdentifierRepository,
             INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler,
             IMetadataService metadataService,
             IPositionHateoasHandler positionHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.nationalIdentifierRepository = nationalIdentifierRepository;
        this.nationalIdentifierHateoasHandler =
                nationalIdentifierHateoasHandler;
        this.metadataService = metadataService;
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
    public CadastralUnitHateoas createNewCadastralUnit
            (@NotNull CadastralUnit cadastralUnit, @NotNull Record record) {
        record.addNationalIdentifier(cadastralUnit);
        cadastralUnit.setReferenceRecord(record);
        nationalIdentifierRepository.save(cadastralUnit);
        CadastralUnitHateoas cadastralUnitHateoas =
            new CadastralUnitHateoas(cadastralUnit);
        nationalIdentifierHateoasHandler
            .addLinks(cadastralUnitHateoas, new Authorisation());
        return cadastralUnitHateoas;
    }

    @Override
    public CadastralUnitHateoas createNewCadastralUnit
            (@NotNull CadastralUnit cadastralUnit, @NotNull File file) {
        file.addNationalIdentifier(cadastralUnit);
        cadastralUnit.setReferenceFile(file);
        nationalIdentifierRepository.save(cadastralUnit);
        CadastralUnitHateoas cadastralUnitHateoas =
            new CadastralUnitHateoas(cadastralUnit);
        nationalIdentifierHateoasHandler
            .addLinks(cadastralUnitHateoas, new Authorisation());
        return cadastralUnitHateoas;
    }

    @Override
    public DNumberHateoas createNewDNumber
            (@NotNull DNumber dNumber, @NotNull Record record) {
        record.addNationalIdentifier(dNumber);
        dNumber.setReferenceRecord(record);
        nationalIdentifierRepository.save(dNumber);
        DNumberHateoas dNumberHateoas = new DNumberHateoas(dNumber);
        nationalIdentifierHateoasHandler
            .addLinks(dNumberHateoas, new Authorisation());
        return dNumberHateoas;
    }

    @Override
    public DNumberHateoas createNewDNumber
            (@NotNull DNumber dNumber, @NotNull File file) {
        file.addNationalIdentifier(dNumber);
        dNumber.setReferenceFile(file);
        nationalIdentifierRepository.save(dNumber);
        DNumberHateoas dNumberHateoas = new DNumberHateoas(dNumber);
        nationalIdentifierHateoasHandler
            .addLinks(dNumberHateoas, new Authorisation());
        return dNumberHateoas;
    }

    @Override
    public PlanHateoas createNewPlan
            (@NotNull Plan plan, @NotNull Record record) {
        record.addNationalIdentifier(plan);
        plan.setReferenceRecord(record);
        nationalIdentifierRepository.save(plan);
        PlanHateoas planHateoas = new PlanHateoas(plan);
        nationalIdentifierHateoasHandler
            .addLinks(planHateoas, new Authorisation());
        return planHateoas;
    }

    @Override
    public PlanHateoas createNewPlan
            (@NotNull Plan plan, @NotNull File file) {
        file.addNationalIdentifier(plan);
        plan.setReferenceFile(file);
        nationalIdentifierRepository.save(plan);
        PlanHateoas planHateoas = new PlanHateoas(plan);
        nationalIdentifierHateoasHandler
            .addLinks(planHateoas, new Authorisation());
        return planHateoas;
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
    public SocialSecurityNumberHateoas createNewSocialSecurityNumber
            (@NotNull SocialSecurityNumber socialSecurityNumber, @NotNull Record record) {
        record.addNationalIdentifier(socialSecurityNumber);
        socialSecurityNumber.setReferenceRecord(record);
        nationalIdentifierRepository.save(socialSecurityNumber);
        SocialSecurityNumberHateoas socialSecurityNumberHateoas =
            new SocialSecurityNumberHateoas(socialSecurityNumber);
        nationalIdentifierHateoasHandler
            .addLinks(socialSecurityNumberHateoas, new Authorisation());
        return socialSecurityNumberHateoas;
    }

    @Override
    public SocialSecurityNumberHateoas createNewSocialSecurityNumber
            (@NotNull SocialSecurityNumber socialSecurityNumber, @NotNull File file) {
        file.addNationalIdentifier(socialSecurityNumber);
        socialSecurityNumber.setReferenceFile(file);
        nationalIdentifierRepository.save(socialSecurityNumber);
        SocialSecurityNumberHateoas socialSecurityNumberHateoas =
            new SocialSecurityNumberHateoas(socialSecurityNumber);
        nationalIdentifierHateoasHandler.addLinks(
                socialSecurityNumberHateoas, new Authorisation());
        return socialSecurityNumberHateoas;
    }

    @Override
    public UnitHateoas createNewUnit
            (@NotNull Unit unit, @NotNull Record record) {
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        record.addNationalIdentifier(unit);
        unit.setReferenceRecord(record);
        nationalIdentifierRepository.save(unit);
        UnitHateoas unitHateoas = new UnitHateoas(unit);
        nationalIdentifierHateoasHandler
            .addLinks(unitHateoas, new Authorisation());
        return unitHateoas;
    }

    @Override
    public UnitHateoas createNewUnit
            (@NotNull Unit unit, @NotNull File file) {
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        file.addNationalIdentifier(unit);
        unit.setReferenceFile(file);
        nationalIdentifierRepository.save(unit);
        UnitHateoas unitHateoas = new UnitHateoas(unit);
        nationalIdentifierHateoasHandler
            .addLinks(unitHateoas, new Authorisation());
        return unitHateoas;
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
        existingBuilding.setRunningChangeNumber
            (incomingBuilding.getRunningChangeNumber());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingBuilding.setVersion(version);
        nationalIdentifierRepository.save(existingBuilding);
        return existingBuilding;
    }

    @Override
    public CadastralUnit updateCadastralUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CadastralUnit incomingCadastralUnit) {
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
        nationalIdentifierRepository.save(existingCadastralUnit);
        return existingCadastralUnit;
    }

    @Override
    public DNumber updateDNumber(
            @NotNull String systemId, @NotNull Long version,
            @NotNull DNumber incomingDNumber) {
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
        nationalIdentifierRepository.save(existingDNumber);
        return existingDNumber;
    }

    @Override
    public Plan updatePlan(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Plan incomingPlan) {
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
        nationalIdentifierRepository.save(existingPlan);
        return existingPlan;
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
        existingPosition.setCoordinateSystem
            (incomingPosition.getCoordinateSystem());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingPosition.setVersion(version);
        nationalIdentifierRepository.save(existingPosition);
        return existingPosition;
    }

    @Override
    public SocialSecurityNumber updateSocialSecurityNumber(
            @NotNull String systemId, @NotNull Long version,
            @NotNull SocialSecurityNumber incomingSocialSecurityNumber) {
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
        nationalIdentifierRepository.save(existingSocialSecurityNumber);
        return existingSocialSecurityNumber;
    }

    @Override
    public Unit updateUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Unit incomingUnit) {
        Unit existingUnit =
            (Unit) getNationalIdentifierOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingUnit
            .setOrganisationNumber(incomingUnit.getOrganisationNumber());

        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG
        // value
        existingUnit.setVersion(version);
        nationalIdentifierRepository.save(existingUnit);
        return existingUnit;
    }

    @Override
    public void deleteBuilding(@NotNull String systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public void deleteCadastralUnit(@NotNull String systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public void deleteDNumber(@NotNull String systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public void deletePlan(@NotNull String systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public void deletePosition(@NotNull String systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public void deleteSocialSecurityNumber(@NotNull String systemId) {
        deleteEntity(getNationalIdentifierOrThrow(systemId));
    }

    @Override
    public void deleteUnit(@NotNull String systemId) {
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
        BuildingHateoas buildingHateoas =
                new BuildingHateoas(building);
        nationalIdentifierHateoasHandler
                .addLinksOnTemplate(buildingHateoas, new Authorisation());
        return buildingHateoas;
    }

    /**
     * Generate a Default CadastralUnit object.
     *
     * @return the CadastralUnit object wrapped as a CadastralUnitHateoas object
     */
    @Override
    public CadastralUnitHateoas generateDefaultCadastralUnit() {
        CadastralUnit cadastralUnit = new CadastralUnit();
        CadastralUnitHateoas cadastralUnitHateoas =
                new CadastralUnitHateoas(cadastralUnit);
        nationalIdentifierHateoasHandler
                .addLinksOnTemplate(cadastralUnitHateoas, new Authorisation());
        return cadastralUnitHateoas;
    }

    /**
     * Generate a Default DNumber object.
     *
     * @return the DNumber object wrapped as a DNumberHateoas object
     */
    @Override
    public DNumberHateoas generateDefaultDNumber() {
        DNumber dNumber = new DNumber();
        DNumberHateoas dNumberHateoas =
                new DNumberHateoas(dNumber);
        nationalIdentifierHateoasHandler
                .addLinksOnTemplate(dNumberHateoas, new Authorisation());
        return dNumberHateoas;
    }

    /**
     * Generate a Default Plan object.
     *
     * @return the Plan object wrapped as a PlanHateoas object
     */
    @Override
    public PlanHateoas generateDefaultPlan() {
        Plan plan = new Plan();
        PlanHateoas planHateoas =
                new PlanHateoas(plan);
        nationalIdentifierHateoasHandler
                .addLinksOnTemplate(planHateoas, new Authorisation());
        return planHateoas;
    }

    /**
     * Generate a Default Position object.
     *
     * @return the Position object wrapped as a PositionHateoas object
     */
    @Override
    public PositionHateoas generateDefaultPosition() {
        Position position = new Position();
        CoordinateSystem coordinateSystem = (CoordinateSystem)
            metadataService.findValidMetadataByEntityTypeOrThrow
            (COORDINATE_SYSTEM, "EPSG:4326", "WGS84");
        position.setCoordinateSystem(coordinateSystem);
        PositionHateoas positionHateoas =
                new PositionHateoas(position);
        positionHateoasHandler
            .addLinksOnTemplate(positionHateoas, new Authorisation());
        return positionHateoas;
    }

    /**
     * Generate a Default SocialSecurityNumber object.
     *
     * @return the SocialSecurityNumber object wrapped as a SocialSecurityNumberHateoas object
     */
    @Override
    public SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber() {
        SocialSecurityNumber socialSecurityNumber = new SocialSecurityNumber();
        SocialSecurityNumberHateoas socialSecurityNumberHateoas =
                new SocialSecurityNumberHateoas(socialSecurityNumber);
        nationalIdentifierHateoasHandler
                .addLinksOnTemplate(socialSecurityNumberHateoas, new Authorisation());
        return socialSecurityNumberHateoas;
    }

    /**
     * Generate a Default Unit object.
     *
     * @return the Unit object wrapped as a UnitHateoas object
     */
    @Override
    public UnitHateoas generateDefaultUnit() {
        Unit unit = new Unit();
        UnitHateoas unitHateoas = new UnitHateoas(unit);
        nationalIdentifierHateoasHandler
                .addLinksOnTemplate(unitHateoas, new Authorisation());
        return unitHateoas;
    }
}
