package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public interface IClassService {

	// -- All CREATE operations
    ClassHateoas save(Class klass);

    ClassHateoas createClassAssociatedWithClass(
            String classSystemId, Class klass);

    FileHateoas createFileAssociatedWithClass(
            String classSystemId, File file);

    CaseFileHateoas createCaseFileAssociatedWithClass(
            String classSystemId, CaseFile caseFile);

    ResponseEntity<RecordHateoas> createRecordAssociatedWithClass(
            String classSystemId, Record record);

    ClassHateoas generateDefaultSubClass(@NotNull String classSystemId);

	// -- All READ operations
    ClassHateoas findAll(String ownedBy);

    ClassHateoas findSingleClass(String classSystemId);

    ClassHateoas findAllChildren(@NotNull String classSystemId);

    ResponseEntity<ClassHateoas>
    findClassAssociatedWithClass(@NotNull final String systemId);

    ResponseEntity<ClassificationSystemHateoas>
    findClassificationSystemAssociatedWithClass(@NotNull final String systemId);

    ResponseEntity<FileHateoas> findAllFileAssociatedWithClass(
            @NotNull final String systemID);

    ResponseEntity<RecordHateoas> findAllRecordAssociatedWithClass(
            @NotNull final String systemID);

    // All UPDATE operations
    ClassHateoas handleUpdate(@NotNull final String systemId,
                              @NotNull final Long version,
                              @NotNull final Class klass);

	// All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
