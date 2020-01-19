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
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.NationalIdentifierHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
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

    PositionHateoas createPositionAssociatedWithRecord
	(@NotNull String systemID, @NotNull Position position);

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

    PositionHateoas generateDefaultPosition();

}
