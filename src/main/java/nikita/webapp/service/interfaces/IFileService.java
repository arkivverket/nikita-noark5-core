package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.PartUnit;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.NationalIdentifierHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IFileService {

    // -- All CREATE operations
    File createFile(File file);

    FileHateoas createFileAssociatedWithFile(String systemId, File file);

    PartPersonHateoas createPartPersonAssociatedWithFile(
            String systemID, PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithFile(
            String systemID, PartUnit partUnit);

    FileHateoas save(File file);

    ResponseEntity<RecordHateoas> createRecordAssociatedWithFile(
            @NotNull final String fileSystemId,
            @NotNull final Record record);

    // -- All READ operations

    PartPersonHateoas generateDefaultPartPerson(String systemID);

    PartUnitHateoas generateDefaultPartUnit(String systemID);

    BuildingHateoas
    createBuildingAssociatedWithFile(
            @NotNull String systemID, @NotNull Building building);

    PositionHateoas
    createPositionAssociatedWithFile(
            @NotNull String systemID, @NotNull Position position);

    List<File> findAll();

    FileHateoas findAllChildren(@NotNull String systemId);

    File findBySystemId(String systemId);

    List<File> findByOwnedBy(String ownedBy);

    ResponseEntity<ClassHateoas>
    findClassAssociatedWithFile(@NotNull final String systemId);

    ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithFile(@NotNull final String systemId);

    PartHateoas getPartAssociatedWithFile(@NotNull final String systemID);

    NationalIdentifierHateoas getNationalIdentifierAssociatedWithFile
	(@NotNull final String systemID);

    BuildingHateoas generateDefaultBuilding();

    PositionHateoas generateDefaultPosition();

    // -- All UPDATE operations
    File handleUpdate(@NotNull String systemId,
                      @NotNull Long version,
                      @NotNull File file);

    // -- All DELETE operations
    void deleteEntity(@NotNull String systemId);

    FileHateoas generateDefaultFile();

    long deleteAllByOwnedBy();
}
