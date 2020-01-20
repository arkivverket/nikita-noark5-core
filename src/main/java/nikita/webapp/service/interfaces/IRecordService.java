package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.PartUnit;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.CadastralUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.DNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PlanHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.NationalIdentifierHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.SocialSecurityNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.UnitHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit;
import nikita.common.model.noark5.v5.nationalidentifier.DNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Plan;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Unit;
import nikita.common.model.noark5.v5.secondary.Author;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IRecordService {

    // -- All CREATE operations
    ResponseEntity<RecordHateoas> save(Record record);

    DocumentDescriptionHateoas createDocumentDescriptionAssociatedWithRecord(
            String systemID, DocumentDescription documentDescription);

    RecordHateoas generateDefaultRecord();

    CorrespondencePartInternalHateoas generateDefaultCorrespondencePartInternal(
            String recordSystemId);

    CorrespondencePartPersonHateoas generateDefaultCorrespondencePartPerson(
            String recordSystemId);

    CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            String recordSystemId);

    CorrespondencePartHateoas getCorrespondencePartAssociatedWithRecord(
            final String systemID);

    PartHateoas getPartAssociatedWithRecord(final String systemID);

    NationalIdentifierHateoas getNationalIdentifierAssociatedWithRecord
	(@NotNull final String systemID);

    PartPersonHateoas generateDefaultPartPerson(
            String recordSystemId);

    PartUnitHateoas generateDefaultPartUnit(
            String recordSystemId);

    CorrespondencePartPersonHateoas
    createCorrespondencePartPersonAssociatedWithRecord(
            String systemID, CorrespondencePartPerson correspondencePart);

    CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRecord(
            String systemID, CorrespondencePartUnit correspondencePart);

    CorrespondencePartInternalHateoas
    createCorrespondencePartInternalAssociatedWithRecord(
            String systemID, CorrespondencePartInternal correspondencePart);

    PartPersonHateoas createPartPersonAssociatedWithRecord(
            String systemID, PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithRecord(
            String systemID, PartUnit partUnit);

    BuildingHateoas createBuildingAssociatedWithRecord
	(@NotNull String systemID, @NotNull Building building);

    CadastralUnitHateoas createCadastralUnitAssociatedWithRecord
	(@NotNull String systemID, @NotNull CadastralUnit cadastralUnit);

    DNumberHateoas createDNumberAssociatedWithRecord
	(@NotNull String systemID, @NotNull DNumber dNumber);

    PlanHateoas createPlanAssociatedWithRecord
	(@NotNull String systemID, @NotNull Plan plan);

    PositionHateoas createPositionAssociatedWithRecord
	(@NotNull String systemID, @NotNull Position position);

    SocialSecurityNumberHateoas createSocialSecurityNumberAssociatedWithRecord
        (@NotNull String systemID,
         @NotNull SocialSecurityNumber socialSecurityNumber);

    UnitHateoas createUnitAssociatedWithRecord
	(@NotNull String systemID, @NotNull Unit unit);

    // -- All READ operations
    List<Record> findAll();

    ResponseEntity<RecordHateoas>
    findByReferenceDocumentDescription(@NotNull final String systemId);

    ResponseEntity<FileHateoas>
    findFileAssociatedWithRecord(@NotNull final String systemId);

    ResponseEntity<ClassHateoas>
    findClassAssociatedWithRecord(@NotNull final String systemId);

    ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithRecord(@NotNull final String systemId);

    Record findBySystemId(String systemId);

    List<Record> findByOwnedBy(String ownedBy);

    AuthorHateoas associatedAuthorWithRecord(String systemId, Author author);

    // -- All UPDATE operations
    Record handleUpdate(@NotNull String systemId,
                        @NotNull Long version, @NotNull Record record);

    // -- All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();

    AuthorHateoas findAllAuthorWithRecordBySystemId(String systemID);

    AuthorHateoas generateDefaultAuthor(String systemID);

    BuildingHateoas generateDefaultBuilding();

    CadastralUnitHateoas generateDefaultCadastralUnit();

    DNumberHateoas generateDefaultDNumber();

    PlanHateoas generateDefaultPlan();

    PositionHateoas generateDefaultPosition();

    SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber();

    UnitHateoas generateDefaultUnit();
}
