package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.PartUnit;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface IRecordService {

    // -- All CREATE operations
    ResponseEntity<RecordHateoas> save(Record record);

    DocumentDescriptionHateoas createDocumentDescriptionAssociatedWithRecord(
            String systemID, DocumentDescription documentDescription);

    CorrespondencePartInternalHateoas generateDefaultCorrespondencePartInternal(
            String recordSystemId);

    CorrespondencePartPersonHateoas generateDefaultCorrespondencePartPerson(
            String recordSystemId);

    CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
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

    CorrespondencePartPersonHateoas
    getCorrespondencePartPersonAssociatedWithRecord(String systemID);

    CorrespondencePartInternalHateoas
    getCorrespondencePartInternalAssociatedWithRecord(String systemID);

    CorrespondencePartUnitHateoas
    getCorrespondencePartUnitAssociatedWithRecord(String systemID);

    PartPersonHateoas createPartPersonAssociatedWithRecord(
            String systemID, PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithRecord(
            String systemID, PartUnit partUnit);

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

    Optional<Record> findById(Long id);

    Record findBySystemId(String systemId);

    List<Record> findByOwnedBy(String ownedBy);

    // -- All UPDATE operations
    Record handleUpdate(@NotNull String systemId,
                        @NotNull Long version, @NotNull Record record);

    // -- All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();


}
