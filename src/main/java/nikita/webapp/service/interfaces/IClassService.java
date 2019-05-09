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
    ClassHateoas save(Class klass, String outgoingAddress);

    ClassHateoas createClassAssociatedWithClass(
            String classSystemId, Class klass, String outgoingAddress);

    FileHateoas createFileAssociatedWithClass(
            String classSystemId, File file, String outgoingAddress);

    CaseFileHateoas createCaseFileAssociatedWithClass(
            String classSystemId, CaseFile caseFile, String outgoingAddress);

    RecordHateoas createRecordAssociatedWithClass(
            String classSystemId, Record record, String outgoingAddress);

    ClassHateoas generateDefaultSubClass(@NotNull String classSystemId,
                                         String outgoingAddress);

    // -- All READ operations
    ClassHateoas findAll(String ownedBy, String outgoingAddress);

    ClassHateoas findSingleClass(String classSystemId, String outgoingAddress);

    ClassHateoas findAllChildren(@NotNull String classSystemId,
                                 String outgoingAddress);

    // All UPDATE operations
    ClassHateoas handleUpdate(@NotNull String systemId, @NotNull Long version,
                              @NotNull Class klass, String outgoingAddress);

    // All DELETE operations
    HateoasNoarkObject deleteEntity(@NotNull String systemId,
                                    String outgoingAddress);
}
