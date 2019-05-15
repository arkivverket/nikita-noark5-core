package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v4.Class;
import nikita.common.model.noark5.v4.File;
import nikita.common.model.noark5.v4.Record;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.model.noark5.v4.hateoas.ClassHateoas;
import nikita.common.model.noark5.v4.hateoas.FileHateoas;
import nikita.common.model.noark5.v4.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v4.hateoas.RecordHateoas;
import nikita.common.model.noark5.v4.hateoas.casehandling.CaseFileHateoas;

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

    RecordHateoas createRecordAssociatedWithClass(
            String classSystemId, Record record);

    ClassHateoas generateDefaultSubClass(@NotNull String classSystemId);

	// -- All READ operations
    ClassHateoas findAll(String ownedBy);

    ClassHateoas findSingleClass(String classSystemId);

    ClassHateoas findAllChildren(@NotNull String classSystemId);

    // All UPDATE operations
    ClassHateoas handleUpdate(@NotNull final String systemId,
                              @NotNull final Long version,
                              @NotNull final Class klass);

	// All DELETE operations
    HateoasNoarkObject deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
